package TCOTS.advancements.criterion;

import TCOTS.advancements.TCOTS_Criteria;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.storage.loot.LootContext;
import org.jetbrains.annotations.NotNull;

public class GetTrollFollowerCriterion extends SimpleCriterionTrigger<GetTrollFollowerCriterion.Conditions> {

    @Override
    public @NotNull Codec<GetTrollFollowerCriterion.Conditions> codec() {
        return GetTrollFollowerCriterion.Conditions.CODEC;
    }

    public void trigger(ServerPlayer player, PathfinderMob entity) {
        LootContext lootContext = EntityPredicate.createContext(player, entity);
        this.trigger(player, conditions ->
                conditions.entity.map(lootContextPredicate -> lootContextPredicate.matches(lootContext))
                .orElse(false));
    }

    public record Conditions(Optional<ContextAwarePredicate> player, Optional<ContextAwarePredicate> entity) implements SimpleCriterionTrigger.SimpleInstance {

        public static final Codec<GetTrollFollowerCriterion.Conditions> CODEC =
                RecordCodecBuilder.create(instance ->
                        instance.group(
                                        EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player")
                                                .forGetter(GetTrollFollowerCriterion.Conditions::player),
                                        EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("entity")
                                                .forGetter(GetTrollFollowerCriterion.Conditions::entity))
                                .apply(instance, GetTrollFollowerCriterion.Conditions::new));

        public static Criterion<GetTrollFollowerCriterion.Conditions> create(EntityPredicate.Builder entity) {
            return TCOTS_Criteria.GET_TROLL_FOLLOWER.createCriterion(
                    new GetTrollFollowerCriterion.Conditions(
                            Optional.empty(),
                            Optional.of(EntityPredicate.wrap(entity))));
        }

    }
}
