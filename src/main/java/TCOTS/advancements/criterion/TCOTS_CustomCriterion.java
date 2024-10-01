package TCOTS.advancements.criterion;

import TCOTS.advancements.TCOTS_Criteria;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancement.AdvancementCriterion;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.dynamic.Codecs;

import java.util.Optional;

public class TCOTS_CustomCriterion extends AbstractCriterion<TCOTS_CustomCriterion.Conditions> {

    @Override
    public Codec<TCOTS_CustomCriterion.Conditions> getConditionsCodec() {
        return Conditions.CODEC;
    }

    public void trigger(ServerPlayerEntity player) {
        this.trigger(player, conditions -> true);
    }

    public record Conditions(Optional<LootContextPredicate> player) implements AbstractCriterion.Conditions {

        public static final Codec<TCOTS_CustomCriterion.Conditions> CODEC =
                RecordCodecBuilder.create(instance ->
                        instance.group(
                                Codecs.createStrictOptionalFieldCodec(EntityPredicate.LOOT_CONTEXT_PREDICATE_CODEC, "player")
                                        .forGetter(TCOTS_CustomCriterion.Conditions::player))
                                .apply(instance, TCOTS_CustomCriterion.Conditions::new));


        public static AdvancementCriterion<TCOTS_CustomCriterion.Conditions> createMaxToxicityCriterion() {
            return TCOTS_Criteria.MAX_TOXICITY_REACHED.create(new Conditions(Optional.empty()));
        }

        public static AdvancementCriterion<TCOTS_CustomCriterion.Conditions> createKillWithHangedCriterion() {
            return TCOTS_Criteria.KILL_WITH_HANGED.create(new Conditions(Optional.empty()));
        }

        public static AdvancementCriterion<TCOTS_CustomCriterion.Conditions> createDestroyNestCriterion() {
            return TCOTS_Criteria.DESTROY_MONSTER_NEST.create(new Conditions(Optional.empty()));
        }

        public static AdvancementCriterion<TCOTS_CustomCriterion.Conditions> createDragonsDreamBurningCriterion() {
            return TCOTS_Criteria.DRAGONS_DREAM_BURNING.create(new Conditions(Optional.empty()));
        }

        public static AdvancementCriterion<TCOTS_CustomCriterion.Conditions> createStopCreeperCriterion() {
            return TCOTS_Criteria.STOP_CREEPER.create(new Conditions(Optional.empty()));
        }

        public static AdvancementCriterion<TCOTS_CustomCriterion.Conditions> createRefillConcoctionCriterion() {
            return TCOTS_Criteria.REFILL_CONCOCTION.create(new Conditions(Optional.empty()));
        }

    }
}
