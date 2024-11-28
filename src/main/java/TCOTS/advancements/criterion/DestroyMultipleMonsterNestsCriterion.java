package TCOTS.advancements.criterion;

import TCOTS.TCOTS_Main;
import TCOTS.advancements.TCOTS_Criteria;
import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancement.AdvancementCriterion;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.dynamic.Codecs;

import java.util.Optional;

public class DestroyMultipleMonsterNestsCriterion extends AbstractCriterion<DestroyMultipleMonsterNestsCriterion.Conditions> {

    public void trigger(ServerPlayerEntity player, int stat) {
        this.trigger(player, conditions -> conditions.quantity.filter(integer -> stat >= integer).isPresent()
        );
    }

    @Override
    protected Conditions conditionsFromJson(JsonObject obj, LootContextPredicate playerPredicate, AdvancementEntityPredicateDeserializer predicateDeserializer) {
        return null;
    }

    @Override
    public Identifier getId() {
        return new Identifier(TCOTS_Main.MOD_ID, "destroy_nests");
    }


    public static class Conditions(Optional<LootContextPredicate> player, Optional<Integer> quantity) extends AbstractCriterionConditions {

        public static final Codec<DestroyMultipleMonsterNestsCriterion.Conditions> CODEC =
                RecordCodecBuilder.create(instance ->
                        instance.group(
                                        Codecs.createStrictOptionalFieldCodec(EntityPredicate.LOOT_CONTEXT_PREDICATE_CODEC, "player")
                                                .forGetter(DestroyMultipleMonsterNestsCriterion.Conditions::player),
                                        Codecs.createStrictOptionalFieldCodec(Codec.INT ,"quantity")
                                                .forGetter(DestroyMultipleMonsterNestsCriterion.Conditions::quantity)
                                )
                                .apply(instance, DestroyMultipleMonsterNestsCriterion.Conditions::new));

        public Conditions(Identifier id, LootContextPredicate entity) {
            super(id, entity);
        }


        public static AdvancementCriterion<DestroyMultipleMonsterNestsCriterion.Conditions> createMultipleDestroyNestCriterion(int quantity) {
            return TCOTS_Criteria.DESTROY_MULTIPLE_MONSTER_NEST.create(
                    new Conditions(
                            Optional.empty(),
                            Optional.of(quantity)));
        }

    }

}
