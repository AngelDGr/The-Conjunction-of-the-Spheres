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
import net.minecraft.util.ExtraCodecs;
import org.jetbrains.annotations.NotNull;

public class DestroyMultipleMonsterNestsCriterion extends SimpleCriterionTrigger<DestroyMultipleMonsterNestsCriterion.Conditions> {

    @Override
    public @NotNull Codec<Conditions> codec() {
        return Conditions.CODEC;
    }

    public void trigger(final ServerPlayer player, final int stat) {
        this.trigger(player, conditions -> conditions.quantity.filter(integer -> stat >= integer).isPresent()
        );
    }


    public record Conditions(Optional<ContextAwarePredicate> player, Optional<Integer> quantity) implements SimpleCriterionTrigger.SimpleInstance {

        public static final Codec<DestroyMultipleMonsterNestsCriterion.Conditions> CODEC =
                RecordCodecBuilder.create(instance ->
                        instance.group(
                                        EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player")
                                                .forGetter(DestroyMultipleMonsterNestsCriterion.Conditions::player),
                                        ExtraCodecs.POSITIVE_INT.optionalFieldOf("quantity")
                                                .forGetter(DestroyMultipleMonsterNestsCriterion.Conditions::quantity)
                                )
                                .apply(instance, DestroyMultipleMonsterNestsCriterion.Conditions::new));


        public static Criterion<DestroyMultipleMonsterNestsCriterion.Conditions> createMultipleDestroyNestCriterion(final int quantity) {
            return TCOTS_Criteria.DestroyMultipleMonsterNest().createCriterion(
                    new DestroyMultipleMonsterNestsCriterion.Conditions(
                            Optional.empty(),
                            Optional.of(quantity)));
        }

    }

}
