package TCOTS.advancements.criterion;

import com.google.gson.JsonObject;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.predicate.NumberRange;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.AdvancementEntityPredicateSerializer;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class DestroyMultipleMonsterNestsCriterion extends AbstractCriterion<DestroyMultipleMonsterNestsCriterion.Conditions> {

    @Override
    protected Conditions conditionsFromJson(JsonObject obj, LootContextPredicate playerPredicate, AdvancementEntityPredicateDeserializer predicateDeserializer) {
		NumberRange.IntRange quantity = NumberRange.IntRange.fromJson(obj.get("quantity"));

        return new DestroyMultipleMonsterNestsCriterion.Conditions(playerPredicate, quantity);
    }

    public void trigger(ServerPlayerEntity player, int stat) {
        this.trigger(player, conditions -> conditions.quantity.test(stat)
        );
    }

    @Override
    public Identifier getId() {
        return new Identifier("tcots-witcher/destroy_multiple_monster_nest");
    }


    public static class Conditions extends AbstractCriterionConditions {
        private final NumberRange.IntRange quantity;

        public Conditions(LootContextPredicate entity, NumberRange.IntRange quantity) {
            super(new Identifier("tcots-witcher/destroy_multiple_monster_nest"), entity);
            this.quantity=quantity;
        }

        public static DestroyMultipleMonsterNestsCriterion.Conditions createMultipleDestroyNestCriterion(int quantity) {
            return new DestroyMultipleMonsterNestsCriterion.Conditions(LootContextPredicate.EMPTY, NumberRange.IntRange.atLeast(quantity));
        }

        @Override
        public JsonObject toJson(AdvancementEntityPredicateSerializer predicateSerializer) {
            JsonObject jsonObject = super.toJson(predicateSerializer);
            jsonObject.add("quantity", this.quantity.toJson());
            return jsonObject;
        }
    }

}
