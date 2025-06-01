package TCOTS.registry;

import TCOTS.TCOTS_Main;
import TCOTS.TCOTS_Registries;
import TCOTS.advancements.criterion.DestroyMultipleMonsterNestsCriterion;
import TCOTS.advancements.criterion.GetTrollFollowerCriterion;
import TCOTS.advancements.criterion.TCOTS_CustomCriterion;
import dev.architectury.registry.registries.RegistrySupplier;

public class TCOTS_Criteria {

    private static final RegistrySupplier<TCOTS_CustomCriterion> MAX_TOXICITY_REACHED =
            TCOTS_Registries.CRITERIA.register(TCOTS_Main.MOD_ID+"/max_toxicity", TCOTS_CustomCriterion::new);

    private static final RegistrySupplier<TCOTS_CustomCriterion> KILL_WITH_HANGED =
            TCOTS_Registries.CRITERIA.register(TCOTS_Main.MOD_ID+"/kill_with_hanged", TCOTS_CustomCriterion::new);

    private static final RegistrySupplier<TCOTS_CustomCriterion> DESTROY_MONSTER_NEST =
            TCOTS_Registries.CRITERIA.register(TCOTS_Main.MOD_ID+"/destroy_monster_nest", TCOTS_CustomCriterion::new);

    private static final RegistrySupplier<DestroyMultipleMonsterNestsCriterion> DESTROY_MULTIPLE_MONSTER_NEST =
            TCOTS_Registries.CRITERIA.register(TCOTS_Main.MOD_ID+"/destroy_multiple_monster_nest", DestroyMultipleMonsterNestsCriterion::new);

    private static final RegistrySupplier<TCOTS_CustomCriterion> DRAGONS_DREAM_BURNING =
            TCOTS_Registries.CRITERIA.register(TCOTS_Main.MOD_ID+"/dragons_dream_burning", TCOTS_CustomCriterion::new);

    private static final RegistrySupplier<TCOTS_CustomCriterion> STOP_CREEPER =
            TCOTS_Registries.CRITERIA.register(TCOTS_Main.MOD_ID+"/stop_creeper", TCOTS_CustomCriterion::new);

    private static final RegistrySupplier<TCOTS_CustomCriterion> REFILL_CONCOCTION =
            TCOTS_Registries.CRITERIA.register(TCOTS_Main.MOD_ID+"/refill_concoction", TCOTS_CustomCriterion::new);

    private static final RegistrySupplier<TCOTS_CustomCriterion> KILL_ROTFIEND =
            TCOTS_Registries.CRITERIA.register(TCOTS_Main.MOD_ID+"/kill_rotfiend", TCOTS_CustomCriterion::new);

    private static final RegistrySupplier<GetTrollFollowerCriterion> GET_TROLL_FOLLOWER =
            TCOTS_Registries.CRITERIA.register(TCOTS_Main.MOD_ID+"/get_troll_follower", GetTrollFollowerCriterion::new);

    public static void initCriteria(){ }

    public static TCOTS_CustomCriterion MaxToxicityReached(){ return MAX_TOXICITY_REACHED.get(); }
    public static TCOTS_CustomCriterion KillWithHanged(){ return KILL_WITH_HANGED.get(); }
    public static TCOTS_CustomCriterion DestroyMonsterNest(){ return DESTROY_MONSTER_NEST.get(); }
    public static DestroyMultipleMonsterNestsCriterion DestroyMultipleMonsterNest(){ return DESTROY_MULTIPLE_MONSTER_NEST.get(); }
    public static TCOTS_CustomCriterion DragonsDreamBurning(){ return DRAGONS_DREAM_BURNING.get(); }
    public static TCOTS_CustomCriterion StopCreeper(){ return STOP_CREEPER.get(); }
    public static TCOTS_CustomCriterion RefillConcoction(){ return REFILL_CONCOCTION.get(); }
    public static TCOTS_CustomCriterion KillRotfiend(){ return KILL_ROTFIEND.get(); }
    public static GetTrollFollowerCriterion GetTrollFollower(){ return GET_TROLL_FOLLOWER.get(); }
}
