package TCOTS.advancements;

import TCOTS.TCOTS_Main;
import TCOTS.advancements.criterion.DestroyMultipleMonsterNestsCriterion;
import TCOTS.advancements.criterion.GetTrollFollowerCriterion;
import TCOTS.advancements.criterion.TCOTS_CustomCriterion;
import net.minecraft.advancements.CriteriaTriggers;

public class TCOTS_Criteria {
    public static final TCOTS_CustomCriterion MAX_TOXICITY_REACHED =
            CriteriaTriggers.register(TCOTS_Main.MOD_ID+"/max_toxicity", new TCOTS_CustomCriterion());

    public static final TCOTS_CustomCriterion KILL_WITH_HANGED =
            CriteriaTriggers.register(TCOTS_Main.MOD_ID+"/kill_with_hanged", new TCOTS_CustomCriterion());

    public static final TCOTS_CustomCriterion DESTROY_MONSTER_NEST =
            CriteriaTriggers.register(TCOTS_Main.MOD_ID+"/destroy_monster_nest", new TCOTS_CustomCriterion());

    public static final DestroyMultipleMonsterNestsCriterion DESTROY_MULTIPLE_MONSTER_NEST =
            CriteriaTriggers.register(TCOTS_Main.MOD_ID+"/destroy_multiple_monster_nest", new DestroyMultipleMonsterNestsCriterion());

    public static final TCOTS_CustomCriterion DRAGONS_DREAM_BURNING =
            CriteriaTriggers.register(TCOTS_Main.MOD_ID+"/dragons_dream_burning", new TCOTS_CustomCriterion());

    public static final TCOTS_CustomCriterion STOP_CREEPER =
            CriteriaTriggers.register(TCOTS_Main.MOD_ID+"/stop_creeper", new TCOTS_CustomCriterion());

    public static final TCOTS_CustomCriterion REFILL_CONCOCTION =
            CriteriaTriggers.register(TCOTS_Main.MOD_ID+"/refill_concoction", new TCOTS_CustomCriterion());

    public static final TCOTS_CustomCriterion KILL_ROTFIEND =
            CriteriaTriggers.register(TCOTS_Main.MOD_ID+"/kill_rotfiend", new TCOTS_CustomCriterion());

    public static final GetTrollFollowerCriterion GET_TROLL_FOLLOWER =
            CriteriaTriggers.register(TCOTS_Main.MOD_ID+"/get_troll_follower", new GetTrollFollowerCriterion());

    public static void registerCriteria(){
    }
}
