package TCOTS.entity.ogroids.troll;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.Uuids;
import net.minecraft.util.annotation.Debug;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.util.math.random.Random;
import org.slf4j.Logger;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class TrollGossipsReputation {
    private static final Logger LOGGER = LogUtils.getLogger();
    private final Map<UUID, Reputation> entityReputation = Maps.newHashMap();

    @Debug
    public Map<UUID, Object2IntMap<TrollGossipType>> getEntityReputationAssociatedGossips() {
        HashMap<UUID, Object2IntMap<TrollGossipType>> map = Maps.newHashMap();
        this.entityReputation.keySet().forEach(uuid -> {
            Reputation reputation = this.entityReputation.get(uuid);
            map.put(uuid, reputation.associatedGossip);
        });
        return map;
    }

    public void decay() {
        Iterator<Reputation> iterator = this.entityReputation.values().iterator();
        while (iterator.hasNext()) {
            Reputation reputation = iterator.next();
            reputation.decay();
            if (!reputation.isObsolete()) continue;
            iterator.remove();
        }
    }

    private Stream<TrollGossipEntry> entries() {
        return this.entityReputation.entrySet().stream().flatMap(entry -> entry.getValue().entriesFor(entry.getKey()));
    }

    private Collection<TrollGossipEntry> pickGossips(net.minecraft.util.math.random.Random random, int count) {
        List<TrollGossipEntry> list = this.entries().toList();
        if (list.isEmpty()) {
            return Collections.emptyList();
        }
        int[] is = new int[list.size()];
        int i = 0;
        for (int j = 0; j < list.size(); ++j) {
            TrollGossipEntry gossipEntry = list.get(j);
            is[j] = (i += Math.abs(gossipEntry.getValue())) - 1;
        }
        Set<TrollGossipEntry> set = Sets.newIdentityHashSet();
        for (int k = 0; k < count; ++k) {
            int l = random.nextInt(i);
            int m = Arrays.binarySearch(is, l);
            set.add(list.get(m < 0 ? -m - 1 : m));
        }
        return set;
    }

    private Reputation getReputationFor(UUID target) {
        return this.entityReputation.computeIfAbsent(target, uuid -> new Reputation());
    }

    public void shareGossipFrom(TrollGossipsReputation from, Random random, int count) {
        Collection<TrollGossipEntry> collection = from.pickGossips(random, count);
        collection.forEach(gossip -> {
            int i = gossip.value - gossip.type.shareDecrement;
            if (i >= 2) {
                this.getReputationFor(gossip.target).associatedGossip.mergeInt(gossip.type, i, TrollGossipsReputation::max);
            }
        });
    }

    public int getReputationFor(UUID target, Predicate<TrollGossipType> gossipTypeFilter) {
        Reputation reputation = this.entityReputation.get(target);
        return reputation != null ? reputation.getValueFor(gossipTypeFilter) : 0;
    }

//    public long getReputationCount(TrollGossipType type, DoublePredicate predicate) {
//        return this.entityReputation.values().stream().filter(
//                reputation ->
//                        predicate.test(reputation.associatedGossip.getOrDefault(type, 0) * TrollGossipType.multiplier)).count();
//    }

    public void startGossip(UUID target, TrollGossipType type, int reputationValue) {
        Reputation reputation = this.getReputationFor(target);
        reputation.associatedGossip.mergeInt(type, reputationValue, (left, right) -> this.mergeReputation(type, left, right));
        reputation.clamp(type);
        if (reputation.isObsolete()) {
            this.entityReputation.remove(target);
        }
    }

    public void removeGossip(UUID target, TrollGossipType type, int value) {
        this.startGossip(target, type, -value);
    }

    public void remove(UUID target, TrollGossipType type) {
        Reputation reputation = this.entityReputation.get(target);
        if (reputation != null) {
            reputation.remove(type);
            if (reputation.isObsolete()) {
                this.entityReputation.remove(target);
            }
        }
    }

    public void remove(TrollGossipType type) {
        Iterator<Reputation> iterator = this.entityReputation.values().iterator();
        while (iterator.hasNext()) {
            Reputation reputation = iterator.next();
            reputation.remove(type);
            if (!reputation.isObsolete()) continue;
            iterator.remove();
        }
    }

    public <T> T serialize(DynamicOps<T> ops) {
        return TrollGossipEntry.LIST_CODEC.encodeStart(ops, this.entries().toList()).resultOrPartial(
                error -> LOGGER.warn("Failed to serialize gossips: {}", error)).orElseGet(ops::emptyList);
    }

    public void deserialize(Dynamic<?> dynamic) {
        TrollGossipEntry.LIST_CODEC.decode(dynamic)
                .resultOrPartial(error ->
                LOGGER.warn("Failed to deserialize gossips: {}", error))
                .stream().flatMap(
                        pair -> (pair.getFirst()).stream())
                .forEach(entry -> this.getReputationFor(entry.target).associatedGossip.put(entry.type, entry.value));
    }

    private static int max(int left, int right) {
        return Math.max(left, right);
    }

    private int mergeReputation(TrollGossipType type, int left, int right) {
        int i = left + right;
        return i > type.maxValue ? Math.max(type.maxValue, left) : i;
    }


    static class Reputation {
        final Object2IntMap<TrollGossipType> associatedGossip = new Object2IntOpenHashMap<>();

        public Reputation() {
        }

        public int getValueFor(Predicate<TrollGossipType> gossipTypeFilter) {
            return this.associatedGossip.object2IntEntrySet().stream().filter(
                            entry -> gossipTypeFilter.test(entry.getKey()))
                    .mapToInt(entry -> entry.getIntValue() * entry.getKey().multiplier).sum();
        }

        public Stream<TrollGossipEntry> entriesFor(UUID target) {
            return this.associatedGossip.object2IntEntrySet().stream().map(
                    entry -> new TrollGossipEntry(target, entry.getKey(), entry.getIntValue()));
        }

        public void decay() {
            Iterator<Object2IntMap.Entry<TrollGossipType>> objectIterator = this.associatedGossip.object2IntEntrySet().iterator();
            while (objectIterator.hasNext()) {
                Object2IntMap.Entry<?> entry = objectIterator.next();
                int i = entry.getIntValue() - ((TrollGossipType)entry.getKey()).decay;
                if (i < 2) {
                    objectIterator.remove();
                    continue;
                }
                entry.setValue(i);
            }
        }

        public boolean isObsolete() {
            return this.associatedGossip.isEmpty();
        }

        public void clamp(TrollGossipType gossipType) {
            int i = this.associatedGossip.getInt(gossipType);
            if (i > gossipType.maxValue) {
                this.associatedGossip.put(gossipType, gossipType.maxValue);
            }
            if (i < 2) {
                this.remove(gossipType);
            }
        }

        public void remove(TrollGossipType gossipType) {
            this.associatedGossip.removeInt(gossipType);
        }
    }

    record TrollGossipEntry(UUID target, TrollGossipType type, int value) {
        public static final Codec<TrollGossipEntry> CODEC =
                RecordCodecBuilder.create(instance -> instance.group(
                                ( Uuids.INT_STREAM_CODEC.fieldOf("Target")).forGetter(TrollGossipEntry::target),
                                (TrollGossipType.CODEC.fieldOf("Type")).forGetter(TrollGossipEntry::type),
                                (Codecs.POSITIVE_INT.fieldOf("Reputation")).forGetter(TrollGossipEntry::value))
                        .apply(instance, TrollGossipEntry::new));
        public static final Codec<List<TrollGossipEntry>> LIST_CODEC = CODEC.listOf();

        public int getValue() {
            return this.value * this.type.multiplier;
        }
    }

    public enum TrollGossipType implements StringIdentifiable
    {
        //Reputation
        BARTERING("bartering", 1, 20, 2, 20),
        //+20 Max -> Barter
        FEEDING("feeding", 1, 50, 10, 20),
        //+50 Max -> Give meat or alcohol
        DEFENDING("defending", 1, 150, 1, 20),
        //+150 Max -> Kill an attacker
        KILL_TROLL("kill_troll", -1, 200, 10, 10),
        //-200 Max -> Kill another troll on sight
        HURT("hurt", -1, 200, 20, 20);
        //-200 Max -> Hurt the troll or a troll friend

        public final String key;
        public final int multiplier;
        public final int maxValue;
        public final int decay;
        public final int shareDecrement;
        public static final Codec<TrollGossipType> CODEC;

        TrollGossipType(String key, int multiplier, int maxReputation, int decay, int shareDecrement) {
            this.key = key;
            this.multiplier = multiplier;
            this.maxValue = maxReputation;
            this.decay = decay;
            this.shareDecrement = shareDecrement;
        }

        @Override
        public String asString() {
            return this.key;
        }

        static {
            CODEC = StringIdentifiable.createCodec(TrollGossipType::values);
        }
    }
}
