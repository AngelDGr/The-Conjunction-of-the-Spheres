package TCOTS.advancements.criterion;

import TCOTS.advancements.TCOTS_Criteria;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancement.AdvancementCriterion;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.loot.context.LootContext;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.dynamic.Codecs;

import java.util.Optional;

public class GetTrollFollowerCriterion extends AbstractCriterion<GetTrollFollowerCriterion.Conditions> {

    @Override
    public Codec<GetTrollFollowerCriterion.Conditions> getConditionsCodec() {
        return GetTrollFollowerCriterion.Conditions.CODEC;
    }

    public void trigger(ServerPlayerEntity player, PathAwareEntity entity) {
        LootContext lootContext = EntityPredicate.createAdvancementEntityLootContext(player, entity);
        this.trigger(player, conditions ->
                conditions.entity.map(lootContextPredicate -> lootContextPredicate.test(lootContext))
                .orElse(false));
    }

    public record Conditions(Optional<LootContextPredicate> player, Optional<LootContextPredicate> entity) implements AbstractCriterion.Conditions {

        public static final Codec<GetTrollFollowerCriterion.Conditions> CODEC =
                RecordCodecBuilder.create(instance ->
                        instance.group(
                                        Codecs.createStrictOptionalFieldCodec(EntityPredicate.LOOT_CONTEXT_PREDICATE_CODEC, "player")
                                                .forGetter(GetTrollFollowerCriterion.Conditions::player),
                                        Codecs.createStrictOptionalFieldCodec(EntityPredicate.LOOT_CONTEXT_PREDICATE_CODEC, "entity")
                                                .forGetter(GetTrollFollowerCriterion.Conditions::entity))
                                .apply(instance, GetTrollFollowerCriterion.Conditions::new));

        public static AdvancementCriterion<GetTrollFollowerCriterion.Conditions> create(EntityPredicate.Builder entity) {
            return TCOTS_Criteria.GET_TROLL_FOLLOWER.create(
                    new GetTrollFollowerCriterion.Conditions(
                            Optional.empty(),
                            Optional.of(EntityPredicate.contextPredicateFromEntityPredicate(entity))));
        }

    }
}
