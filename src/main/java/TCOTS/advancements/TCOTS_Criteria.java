package TCOTS.advancements;

import TCOTS.advancements.criterion.DestroyMultipleMonsterNestsCriterion;
import TCOTS.advancements.criterion.GetTrollFollowerCriterion;
import TCOTS.advancements.criterion.TCOTS_CustomCriterion;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.util.Identifier;

public class TCOTS_Criteria {
    public static final TCOTS_CustomCriterion MAX_TOXICITY_REACHED =
            Criteria.register(new TCOTS_CustomCriterion(new Identifier("tcots-witcher/max_toxicity")));

    public static final TCOTS_CustomCriterion KILL_WITH_HANGED =
            Criteria.register(new TCOTS_CustomCriterion(new Identifier("tcots-witcher/kill_with_hanged")));

    public static final TCOTS_CustomCriterion DESTROY_MONSTER_NEST =
            Criteria.register(new TCOTS_CustomCriterion(new Identifier("tcots-witcher/destroy_monster_nest")));

    public static final DestroyMultipleMonsterNestsCriterion DESTROY_MULTIPLE_MONSTER_NEST =
            Criteria.register(new DestroyMultipleMonsterNestsCriterion());

    public static final TCOTS_CustomCriterion DRAGONS_DREAM_BURNING =
            Criteria.register(new TCOTS_CustomCriterion(new Identifier("tcots-witcher/dragons_dream_burning")));

    public static final TCOTS_CustomCriterion STOP_CREEPER =
            Criteria.register(new TCOTS_CustomCriterion(new Identifier("tcots-witcher/stop_creeper")));

    public static final TCOTS_CustomCriterion REFILL_CONCOCTION =
            Criteria.register(new TCOTS_CustomCriterion(new Identifier("tcots-witcher/refill_concoction")));

    public static final TCOTS_CustomCriterion KILL_ROTFIEND =
            Criteria.register(new TCOTS_CustomCriterion(new Identifier("tcots-witcher/kill_rotfiend")));

    public static final GetTrollFollowerCriterion GET_TROLL_FOLLOWER =
            Criteria.register(new GetTrollFollowerCriterion());

    public static void registerCriteria(){
    }
}
