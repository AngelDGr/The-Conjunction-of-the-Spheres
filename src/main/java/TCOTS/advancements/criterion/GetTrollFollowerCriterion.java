package TCOTS.advancements.criterion;

import com.google.gson.JsonObject;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.loot.context.LootContext;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.AdvancementEntityPredicateSerializer;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class GetTrollFollowerCriterion extends AbstractCriterion<GetTrollFollowerCriterion.Conditions> {

    public void trigger(ServerPlayerEntity player, PathAwareEntity entity) {
        LootContext lootContext = EntityPredicate.createAdvancementEntityLootContext(player, entity);
        this.trigger(player, conditions ->
                conditions.test(lootContext));
    }

    @Override
    protected Conditions conditionsFromJson(JsonObject obj, LootContextPredicate playerPredicate, AdvancementEntityPredicateDeserializer predicateDeserializer) {
        LootContextPredicate lootContextPredicate2 = EntityPredicate.contextPredicateFromJson(obj, "entity", predicateDeserializer);
        return new GetTrollFollowerCriterion.Conditions(playerPredicate, lootContextPredicate2);
    }

    @Override
    public Identifier getId() {
        return new Identifier("tcots-witcher/get_troll_follower");
    }

    public static class Conditions extends AbstractCriterionConditions {
        private final LootContextPredicate entity;

        public Conditions(LootContextPredicate player, LootContextPredicate entity) {
            super(new Identifier("tcots-witcher/get_troll_follower"), player);
            this.entity = entity;
        }

        public boolean test(LootContext killedEntityContext) {
            return this.entity.test(killedEntityContext);
        }

        public static GetTrollFollowerCriterion.Conditions create(EntityPredicate entity) {
            return new GetTrollFollowerCriterion.Conditions(LootContextPredicate.EMPTY, EntityPredicate.asLootContextPredicate(entity));
        }

        @Override
        public JsonObject toJson(AdvancementEntityPredicateSerializer predicateSerializer) {
            JsonObject jsonObject = super.toJson(predicateSerializer);
            jsonObject.add("entity", this.entity.toJson(predicateSerializer));
            return jsonObject;
        }
    }
}
