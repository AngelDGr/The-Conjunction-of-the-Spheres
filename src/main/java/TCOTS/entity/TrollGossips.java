package TCOTS.entity;

import TCOTS.entity.ogroids.RockTrollEntity;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.Dynamic;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.entity.EntityStatuses;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.Uuids;
import net.minecraft.util.annotation.Debug;
import net.minecraft.util.dynamic.Codecs;
import org.slf4j.Logger;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Predicate;
import java.util.stream.Stream;

@SuppressWarnings("unused")
public class TrollGossips {
    private static final Logger LOGGER = LogUtils.getLogger();
    private final Map<UUID, Reputation> entityReputation = Maps.newHashMap();

    @Debug
    public Map<UUID, Object2IntMap<TrollGossipType>> getEntityReputationAssociatedGossips() {
        HashMap<UUID, Object2IntMap<TrollGossipType>> map = Maps.newHashMap();
        this.entityReputation.keySet().forEach(uuid -> {
            Reputation reputation = this.entityReputation.get(uuid);
            map.put(uuid, reputation.associatedReputation);
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
        return this.entityReputation.entrySet().stream().flatMap(entryKey -> entryKey.getValue().entriesFor(entryKey.getKey()));
    }

    @SuppressWarnings("all")
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

    public void shareGossipsWith(RockTrollEntity senderTroll, RockTrollEntity receiverTroll){
        TrollGossips senderGossips= senderTroll.getGossip();
        TrollGossips receiverGossips = receiverTroll.getGossip();

        Collection<TrollGossipEntry>  collectionSender = senderGossips.entries().toList();
        Collection<TrollGossipEntry>  collectionReceiver = receiverGossips.entries().toList();

        List<UUID> listKnowPlayersForSender = new ArrayList<>();
        List<UUID> listKnowPlayersForReceiver = new ArrayList<>();


        collectionSender.forEach(
                gossip ->
                {
                    if(!listKnowPlayersForSender.contains(gossip.target)){
                        listKnowPlayersForSender.add(gossip.target);
                    }
                });

        collectionReceiver.forEach(
                gossip -> {
                    if(!listKnowPlayersForReceiver.contains(gossip.target)){
                        listKnowPlayersForReceiver.add(gossip.target);
                    }
                });

        if(!(new HashSet<>(listKnowPlayersForReceiver).containsAll(listKnowPlayersForSender))){
            AtomicBoolean triggerParticles= new AtomicBoolean(false);
            collectionSender.forEach(
                    gossip ->
                    {
//                        listKnowPlayersForSender.forEach(
//                                player -> {
//                                    //Doesn't know that player, so it add the new gossip
//                                    if (!listKnowPlayersForReceiver.contains(player)) {
//                                        int reputation = gossip.reputationValue;
//                                        int decrement = gossip.type.shareDecrement;
//
//                                        receiverGossips.startGossip(
//                                                gossip.target,
//                                                gossip.type,
//                                                reputation - decrement <= 0 ? 1 : reputation - decrement,
//                                                0);
//
//                                        System.out.println("TriggerGossip");
////                                            triggerParticles.set(true);
//                                    } else {
//                                        System.out.println("NotTriggerGossip");
//                                    }
//                                }
//                        );

                        //Doesn't know that player, so it add the new gossip
                        if (!listKnowPlayersForReceiver.contains(gossip.target)) {
                            int reputation = gossip.reputationValue;
                            int decrement = gossip.type.shareDecrement;

                            receiverGossips.startGossip(
                                    gossip.target,
                                    gossip.type,
                                    reputation - decrement <= 0 ? 1 : reputation - decrement,
                                    0);

                          triggerParticles.set(true);
                        }
                    }
            );

            if(triggerParticles.get()){
                receiverTroll.getWorld().sendEntityStatus(receiverTroll, EntityStatuses.ADD_VILLAGER_HAPPY_PARTICLES);
            }
        }
    }

    public int getReputationFor(UUID target, Predicate<TrollGossipType> gossipTypeFilter) {
        Reputation reputation = this.entityReputation.get(target);
        return reputation != null ? reputation.getReputationValueFor(gossipTypeFilter) : 0;
    }

    public int getFriendshipFor(UUID target, Predicate<TrollGossipType> gossipTypeFilter) {
        Reputation reputation = this.entityReputation.get(target);
        return reputation != null ? reputation.getFriendshipValueFor(gossipTypeFilter) : 0;
    }

    public void startGossip(UUID target, TrollGossipType type, int reputationValue, int friendshipValue) {
        Reputation reputation = this.getReputationFor(target);
        reputation.associatedReputation.mergeInt(type, reputationValue, (left, right) -> this.mergeReputation(type, left, right));
        reputation.associatedFriendship.mergeInt(type, friendshipValue, (left, right) -> this.mergeFriendship(type, left, right));
        reputation.clamp(type,reputation.associatedReputation, type.maxValue);
        reputation.clamp(type,reputation.associatedFriendship, type.maxFriendshipValue);
        if (reputation.isObsolete()) {
            this.entityReputation.remove(target);
        }
    }

    public void removeGossip(UUID target, TrollGossipType type, int reputationValue, int friendshipValue) {
        this.startGossip(target, type, -reputationValue, -friendshipValue);
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
                .forEach(entry -> {
                    this.getReputationFor(entry.target).associatedReputation.put(entry.type, entry.reputationValue);
                    this.getReputationFor(entry.target).associatedFriendship.put(entry.type, entry.friendshipValue);
                });
    }

    private static int max(int left, int right) {
        return Math.max(left, right);
    }

    private int mergeReputation(TrollGossipType type, int left, int right) {
        int i = left + right;
        return i > type.maxValue ? Math.max(type.maxValue, left) : i;
    }

    private int mergeFriendship(TrollGossipType type, int left, int right) {
        int i = left + right;
        return i > type.maxFriendshipValue ? Math.max(type.maxFriendshipValue, left) : i;
    }


    static class Reputation {
        final Object2IntMap<TrollGossipType> associatedReputation = new Object2IntOpenHashMap<>();

        final Object2IntMap<TrollGossipType> associatedFriendship = new Object2IntOpenHashMap<>();

        public Reputation() {
        }

        public int getReputationValueFor(Predicate<TrollGossipType> gossipTypeFilter) {
            return this.associatedReputation.object2IntEntrySet().stream().filter(
                            entry -> gossipTypeFilter.test(entry.getKey()))
                    .mapToInt(entry -> entry.getIntValue() * entry.getKey().multiplier).sum();
        }

        public int getFriendshipValueFor(Predicate<TrollGossipType> gossipTypeFilter) {
            return this.associatedFriendship.object2IntEntrySet().stream().filter(
                            entry -> gossipTypeFilter.test(entry.getKey()))
                    .mapToInt(entry -> entry.getIntValue() * entry.getKey().multiplier).sum();
        }

        public Stream<TrollGossipEntry> entriesFor(UUID target) {
            return this.associatedReputation.object2IntEntrySet().stream().map(
                    entry -> new TrollGossipEntry(target, entry.getKey(), entry.getIntValue(), associatedFriendship.getInt(entry.getKey())));
        }

        public void decay() {
            Iterator<Object2IntMap.Entry<TrollGossipType>> reputationIterator = this.associatedReputation.object2IntEntrySet().iterator();
            Iterator<Object2IntMap.Entry<TrollGossipType>> friendshipIterator = this.associatedFriendship.object2IntEntrySet().iterator();

            while (reputationIterator.hasNext() && friendshipIterator.hasNext()) {
                // Handle reputation
                Object2IntMap.Entry<?> repEntry = reputationIterator.next();
                int repValue = repEntry.getIntValue() - ((TrollGossipType) repEntry.getKey()).decay;

                //To only decay if is a value above 0
                if (!(repEntry.getIntValue() <= 0)){
                    repEntry.setValue(repValue);
                }
                //If for some reason reach below 0, limits to 0
                if(repEntry.getIntValue() < 0){
                    repEntry.setValue(0);
                }

                // Handle friendship
                Object2IntMap.Entry<?> friendEntry = friendshipIterator.next();
                int friendValue = friendEntry.getIntValue() - ((TrollGossipType) friendEntry.getKey()).friendshipDecay;

                //To only decay if is a value above 0
                if (!(friendEntry.getIntValue() <= 0)){
                    friendEntry.setValue(friendValue);
                }
                //If for some reason reach below 0, limits to 0
                if(friendEntry.getIntValue() < 0){
                    friendEntry.setValue(0);
                }

                //Only if both values are 0, remove the gossip
                if (repValue <= 0 && friendValue<=0) {
                    reputationIterator.remove();
                    friendshipIterator.remove();
                }
            }
        }


        public boolean isObsolete() {
            return this.associatedReputation.isEmpty();
        }

        public void clamp(TrollGossipType gossipType, Object2IntMap<TrollGossipType> map, int maxValue) {
            int i = map.getInt(gossipType);
            if (i > maxValue) {
                map.put(gossipType, maxValue);
            }
        }

        public void remove(TrollGossipType gossipType) {
            this.associatedReputation.removeInt(gossipType);
        }
    }

    record TrollGossipEntry(UUID target, TrollGossipType type, int reputationValue, int friendshipValue) {
        public static final Codec<TrollGossipEntry> CODEC =
                RecordCodecBuilder.create(instance -> instance.group(
                                ( Uuids.INT_STREAM_CODEC.fieldOf("Target")).forGetter(TrollGossipEntry::target),
                                (TrollGossipType.CODEC.fieldOf("Type")).forGetter(TrollGossipEntry::type),
                                (Codecs.NONNEGATIVE_INT.fieldOf("Reputation")).forGetter(TrollGossipEntry::reputationValue),
                                (Codecs.NONNEGATIVE_INT.fieldOf("zFriendship")).forGetter(TrollGossipEntry::friendshipValue))
                        .apply(instance, TrollGossipEntry::new));
        public static final Codec<List<TrollGossipEntry>> LIST_CODEC = CODEC.listOf();

        public int getValue() {
            return this.reputationValue * this.type.multiplier;
        }
    }

    public enum TrollGossipType implements StringIdentifiable
    {
        //Reputation
        BARTERING("bartering", 1, 20,20, 2, 2, 15),
        //+20 Max -> Barter
        FEEDING("feeding", 1, 50, 200,10, 0, 20),
        //+50 Max -> Give meat or alcohol
        DEFENDING("defending", 1, 150, 200, 1, 0, 10),
        //+150 Max -> Kill an attacker
        KILL_TROLL("kill_troll", -1, 200, 200, 10, 5, 5),
        //-200 Max -> Kill another troll on sight
        HURT("hurt", -1, 200, 200, 20, 10, 10);
        //-200 Max -> Hurt the troll or a troll friend

        public final String key;
        public final int multiplier;
        public final int maxValue;
        public final int maxFriendshipValue;
        public final int friendshipDecay;
        public final int decay;
        public final int shareDecrement;
        public static final Codec<TrollGossipType> CODEC;

        TrollGossipType(String key, int multiplier, int maxReputation,  int maxFriendshipValue, int decay, int friendshipDecay, int shareDecrement) {
            this.key = key;
            this.multiplier = multiplier;
            this.maxValue = maxReputation;
            this.decay = decay;
            this.friendshipDecay=friendshipDecay;
            this.shareDecrement = shareDecrement;
            this.maxFriendshipValue=maxFriendshipValue;
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
