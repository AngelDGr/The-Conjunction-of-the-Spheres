package mors.tcots.advancements.criterion;

import mors.tcots.registry.TCOTS_Criteria;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

public class TCOTS_CustomCriterion extends SimpleCriterionTrigger<TCOTS_CustomCriterion.Conditions> {

    @Override
    public @NotNull Codec<TCOTS_CustomCriterion.Conditions> codec() {
        return Conditions.CODEC;
    }

    public void trigger(final ServerPlayer player) {
        this.trigger(player, conditions -> true);
    }

    public record Conditions(Optional<ContextAwarePredicate> player) implements SimpleCriterionTrigger.SimpleInstance {

        public static final Codec<TCOTS_CustomCriterion.Conditions> CODEC =
                RecordCodecBuilder.create(instance ->
                        instance.group(
                                        EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player")
                                        .forGetter(TCOTS_CustomCriterion.Conditions::player))
                                .apply(instance, TCOTS_CustomCriterion.Conditions::new));


        public static Criterion<TCOTS_CustomCriterion.Conditions> createMaxToxicityCriterion() {
            return TCOTS_Criteria.MaxToxicityReached().createCriterion(new Conditions(Optional.empty()));
        }

        public static Criterion<TCOTS_CustomCriterion.Conditions> createKillWithHangedCriterion() {
            return TCOTS_Criteria.KillWithHanged().createCriterion(new Conditions(Optional.empty()));
        }

        public static Criterion<TCOTS_CustomCriterion.Conditions> createDestroyNestCriterion() {
            return TCOTS_Criteria.DestroyMonsterNest().createCriterion(new Conditions(Optional.empty()));
        }

        public static Criterion<TCOTS_CustomCriterion.Conditions> createDragonsDreamBurningCriterion() {
            return TCOTS_Criteria.DragonsDreamBurning().createCriterion(new Conditions(Optional.empty()));
        }

        public static Criterion<TCOTS_CustomCriterion.Conditions> createStopCreeperCriterion() {
            return TCOTS_Criteria.StopCreeper().createCriterion(new Conditions(Optional.empty()));
        }

        public static Criterion<TCOTS_CustomCriterion.Conditions> createRefillConcoctionCriterion() {
            return TCOTS_Criteria.RefillConcoction().createCriterion(new Conditions(Optional.empty()));
        }

        public static Criterion<TCOTS_CustomCriterion.Conditions> createKillRotfiendCriterion() {
            return TCOTS_Criteria.KillRotfiend().createCriterion(new Conditions(Optional.empty()));
        }

    }
}
