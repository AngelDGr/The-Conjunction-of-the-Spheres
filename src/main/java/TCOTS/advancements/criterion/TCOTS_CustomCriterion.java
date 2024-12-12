package TCOTS.advancements.criterion;

import TCOTS.advancements.TCOTS_Criteria;
import com.google.gson.JsonObject;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.LootContextPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class TCOTS_CustomCriterion extends AbstractCriterion<TCOTS_CustomCriterion.Conditions> {

    private final Identifier id;

    public TCOTS_CustomCriterion(Identifier id){
        this.id=id;
    }

    @Override
    protected Conditions conditionsFromJson(JsonObject obj, LootContextPredicate playerPredicate, AdvancementEntityPredicateDeserializer predicateDeserializer) {
        return new TCOTS_CustomCriterion.Conditions(this.id, playerPredicate);
    }

    public void trigger(ServerPlayerEntity player) {
        this.trigger(player, conditions -> true);
    }

    @Override
    public Identifier getId() {
        return id;
    }

    public static class Conditions extends AbstractCriterionConditions {

        public Conditions(Identifier id, LootContextPredicate entity) {
            super(id, entity);
        }


        public static TCOTS_CustomCriterion.Conditions createMaxToxicityCriterion() {
            return new TCOTS_CustomCriterion.Conditions(TCOTS_Criteria.MAX_TOXICITY_REACHED.id, LootContextPredicate.EMPTY);
        }

        public static TCOTS_CustomCriterion.Conditions createKillWithHangedCriterion() {
            return new TCOTS_CustomCriterion.Conditions(TCOTS_Criteria.KILL_WITH_HANGED.id, LootContextPredicate.EMPTY);
        }

        public static TCOTS_CustomCriterion.Conditions createDestroyNestCriterion() {
            return new TCOTS_CustomCriterion.Conditions(TCOTS_Criteria.DESTROY_MONSTER_NEST.id, LootContextPredicate.EMPTY);
        }

        public static TCOTS_CustomCriterion.Conditions createDragonsDreamBurningCriterion() {
            return new TCOTS_CustomCriterion.Conditions(TCOTS_Criteria.DRAGONS_DREAM_BURNING.id, LootContextPredicate.EMPTY);
        }

        public static TCOTS_CustomCriterion.Conditions createStopCreeperCriterion() {
            return new TCOTS_CustomCriterion.Conditions(TCOTS_Criteria.STOP_CREEPER.id, LootContextPredicate.EMPTY);
        }

        public static TCOTS_CustomCriterion.Conditions createRefillConcoctionCriterion() {
            return new TCOTS_CustomCriterion.Conditions(TCOTS_Criteria.REFILL_CONCOCTION.id, LootContextPredicate.EMPTY);
        }

        public static TCOTS_CustomCriterion.Conditions createKillRotfiendCriterion() {
            return new TCOTS_CustomCriterion.Conditions(TCOTS_Criteria.KILL_ROTFIEND.id, LootContextPredicate.EMPTY);
        }
    }
}
