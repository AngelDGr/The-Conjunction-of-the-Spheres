package TCOTS.registry;

import TCOTS.TCOTS_Main;
import TCOTS.TCOTS_Registries;
import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

public class TCOTS_Sounds {

    public static RegistrySupplier<SoundEvent> MONSTER_EMERGING = registerSoundEvent("monster_emerging");
    public static RegistrySupplier<SoundEvent> MONSTER_DIGGING = registerSoundEvent("monster_digging");
    public static RegistrySupplier<SoundEvent> GROUND_PUNCH = registerSoundEvent("ground_punch");
    public static RegistrySupplier<SoundEvent> BIG_IMPACT = registerSoundEvent("big_impact");
    public static RegistrySupplier<SoundEvent> MEDIUM_IMPACT = registerSoundEvent("medium_impact");

    //Drowner
    public static RegistrySupplier<SoundEvent> DROWNER_ATTACK = registerSoundEvent("drowner_attack");
    public static RegistrySupplier<SoundEvent> DROWNER_HURT = registerSoundEvent("drowner_hurt");
    public static RegistrySupplier<SoundEvent> DROWNER_IDLE = registerSoundEvent("drowner_idle");
    public static RegistrySupplier<SoundEvent> DROWNER_DEATH = registerSoundEvent("drowner_death");
    public static RegistrySupplier<SoundEvent> WATERY_FOOTSTEP = registerSoundEvent("drowner_footstep");
    public static RegistrySupplier<SoundEvent> DROWNER_LUNGE = registerSoundEvent("drowner_lunge");
    public static RegistrySupplier<SoundEvent> DROWNER_EMERGING = registerSoundEvent("drowner_emerging");
    public static RegistrySupplier<SoundEvent> DROWNER_DIGGING = registerSoundEvent("drowner_digging");

    //Rotfiend
    public static RegistrySupplier<SoundEvent> ROTFIEND_ATTACK = registerSoundEvent("rotfiend_attack");
    public static RegistrySupplier<SoundEvent> ROTFIEND_HURT = registerSoundEvent("rotfiend_hurt");
    public static RegistrySupplier<SoundEvent> ROTFIEND_IDLE = registerSoundEvent("rotfiend_idle");
    public static RegistrySupplier<SoundEvent> ROTFIEND_DEATH = registerSoundEvent("rotfiend_death");
    public static RegistrySupplier<SoundEvent> ROTFIEND_LUNGE = registerSoundEvent("rotfiend_lunge");
    public static RegistrySupplier<SoundEvent> ROTFIEND_EXPLODING = registerSoundEvent("rotfiend_exploding");


    //Grave Hag
    public static RegistrySupplier<SoundEvent> GRAVE_HAG_ATTACK = registerSoundEvent("grave_hag_attack");
    public static RegistrySupplier<SoundEvent> GRAVE_HAG_HURT = registerSoundEvent("grave_hag_hurt");
    public static RegistrySupplier<SoundEvent> GRAVE_HAG_IDLE = registerSoundEvent("grave_hag_idle");
    public static RegistrySupplier<SoundEvent> GRAVE_HAG_DEATH = registerSoundEvent("grave_hag_death");
    public static RegistrySupplier<SoundEvent> GRAVE_HAG_RUN = registerSoundEvent("grave_hag_run");
    public static RegistrySupplier<SoundEvent> GRAVE_HAG_TONGUE_ATTACK = registerSoundEvent("grave_hag_tongue_attack");

    //Water Hag
    public static RegistrySupplier<SoundEvent> WATER_HAG_ATTACK = registerSoundEvent("water_hag_attack");
    public static RegistrySupplier<SoundEvent> WATER_HAG_HURT = registerSoundEvent("water_hag_hurt");
    public static RegistrySupplier<SoundEvent> WATER_HAG_IDLE = registerSoundEvent("water_hag_idle");
    public static RegistrySupplier<SoundEvent> WATER_HAG_DEATH = registerSoundEvent("water_hag_death");
    public static RegistrySupplier<SoundEvent> WATER_HAG_EMERGING = registerSoundEvent("water_hag_emerging");
    public static RegistrySupplier<SoundEvent> WATER_HAG_DIGGING = registerSoundEvent("water_hag_digging");
    public static RegistrySupplier<SoundEvent> WATER_HAG_MUD_BALL_LAUNCH = registerSoundEvent("water_hag_mud_ball_launch");
    public static RegistrySupplier<SoundEvent> WATER_HAG_MUD_BALL_HIT = registerSoundEvent("water_hag_mud_ball_hit");

    //Foglet
    public static RegistrySupplier<SoundEvent> FOGLET_ATTACK = registerSoundEvent("foglet_attack");
    public static RegistrySupplier<SoundEvent> FOGLET_HURT = registerSoundEvent("foglet_hurt");
    public static RegistrySupplier<SoundEvent> FOGLET_IDLE = registerSoundEvent("foglet_idle");
    public static RegistrySupplier<SoundEvent> FOGLET_DEATH = registerSoundEvent("foglet_death");
    public static RegistrySupplier<SoundEvent> FOGLET_FOG = registerSoundEvent("foglet_fog");

    public static RegistrySupplier<SoundEvent> FOGLET_FOGLING_DISAPPEAR = registerSoundEvent("fogling_disappear");

    //Ghoul
    public static RegistrySupplier<SoundEvent> GHOUL_ATTACK = registerSoundEvent("ghoul_attack");
    public static RegistrySupplier<SoundEvent> GHOUL_HURT = registerSoundEvent("ghoul_hurt");
    public static RegistrySupplier<SoundEvent> GHOUL_IDLE = registerSoundEvent("ghoul_idle");
    public static RegistrySupplier<SoundEvent> GHOUL_DEATH = registerSoundEvent("ghoul_death");
    public static RegistrySupplier<SoundEvent> GHOUL_LUNGES = registerSoundEvent("ghoul_lunge");
    public static RegistrySupplier<SoundEvent> GHOUL_SCREAMS = registerSoundEvent("ghoul_scream");
    public static RegistrySupplier<SoundEvent> GHOUL_REGEN = registerSoundEvent("ghoul_regen");

    //Alghoul
    public static RegistrySupplier<SoundEvent> ALGHOUL_ATTACK = registerSoundEvent("alghoul_attack");
    public static RegistrySupplier<SoundEvent> ALGHOUL_HURT = registerSoundEvent("alghoul_hurt");
    public static RegistrySupplier<SoundEvent> ALGHOUL_IDLE = registerSoundEvent("alghoul_idle");
    public static RegistrySupplier<SoundEvent> ALGHOUL_DEATH = registerSoundEvent("alghoul_death");
    public static RegistrySupplier<SoundEvent> ALGHOUL_LUNGES = registerSoundEvent("alghoul_lunge");
    public static RegistrySupplier<SoundEvent> ALGHOUL_SCREAMS = registerSoundEvent("alghoul_scream");
    public static RegistrySupplier<SoundEvent> ALGHOUL_REGEN = registerSoundEvent("alghoul_regen");
    public static RegistrySupplier<SoundEvent> ALGHOUL_SPIKES = registerSoundEvent("alghoul_spikes");

    //Scurver
    public static RegistrySupplier<SoundEvent> SCURVER_ATTACK = registerSoundEvent("scurver_attack");
    public static RegistrySupplier<SoundEvent> SCURVER_HURT = registerSoundEvent("scurver_hurt");
    public static RegistrySupplier<SoundEvent> SCURVER_IDLE = registerSoundEvent("scurver_idle");
    public static RegistrySupplier<SoundEvent> SCURVER_DEATH = registerSoundEvent("scurver_death");
    public static RegistrySupplier<SoundEvent> SCURVER_LUNGE = registerSoundEvent("scurver_lunge");
    public static RegistrySupplier<SoundEvent> SCURVER_EXPLODING = registerSoundEvent("scurver_exploding");

    //Devourer
    public static RegistrySupplier<SoundEvent> DEVOURER_ATTACK = registerSoundEvent("devourer_attack");
    public static RegistrySupplier<SoundEvent> DEVOURER_HURT = registerSoundEvent("devourer_hurt");
    public static RegistrySupplier<SoundEvent> DEVOURER_IDLE = registerSoundEvent("devourer_idle");
    public static RegistrySupplier<SoundEvent> DEVOURER_DEATH = registerSoundEvent("devourer_death");
    public static RegistrySupplier<SoundEvent> DEVOURER_JUMP = registerSoundEvent("devourer_jump");

    //Bloedzuiger
    public static RegistrySupplier<SoundEvent> BLOEDZUIGER_ATTACK = registerSoundEvent("bloedzuiger_attack");
    public static RegistrySupplier<SoundEvent> BLOEDZUIGER_HURT = registerSoundEvent("bloedzuiger_hurt");
    public static RegistrySupplier<SoundEvent> BLOEDZUIGER_IDLE = registerSoundEvent("bloedzuiger_idle");
    public static RegistrySupplier<SoundEvent> BLOEDZUIGER_DEATH = registerSoundEvent("bloedzuiger_death");
    public static RegistrySupplier<SoundEvent> BLOEDZUIGER_EXPLOSION = registerSoundEvent("bloedzuiger_exploding");

    //Graveir
    public static RegistrySupplier<SoundEvent> GRAVEIR_ATTACK = registerSoundEvent("graveir_attack");
    public static RegistrySupplier<SoundEvent> GRAVEIR_HURT = registerSoundEvent("graveir_hurt");
    public static RegistrySupplier<SoundEvent> GRAVEIR_IDLE = registerSoundEvent("graveir_idle");
    public static RegistrySupplier<SoundEvent> GRAVEIR_DEATH = registerSoundEvent("graveir_death");
    public static RegistrySupplier<SoundEvent> GRAVEIR_GROUND_PUNCH = registerSoundEvent("graveir_ground_punch");

    //Bullvore
    public static RegistrySupplier<SoundEvent> BULLVORE_ATTACK = registerSoundEvent("bullvore_attack");
    public static RegistrySupplier<SoundEvent> BULLVORE_HURT = registerSoundEvent("bullvore_hurt");
    public static RegistrySupplier<SoundEvent> BULLVORE_IDLE = registerSoundEvent("bullvore_idle");
    public static RegistrySupplier<SoundEvent> BULLVORE_DEATH = registerSoundEvent("bullvore_death");
    public static RegistrySupplier<SoundEvent> BULLVORE_CHARGE = registerSoundEvent("bullvore_charge");

    //Nekker
    public static RegistrySupplier<SoundEvent> NEKKER_ATTACK = registerSoundEvent("nekker_attack");
    public static RegistrySupplier<SoundEvent> NEKKER_HURT = registerSoundEvent("nekker_hurt");
    public static RegistrySupplier<SoundEvent> NEKKER_IDLE = registerSoundEvent("nekker_idle");
    public static RegistrySupplier<SoundEvent> NEKKER_DEATH = registerSoundEvent("nekker_death");
    public static RegistrySupplier<SoundEvent> NEKKER_LUNGE = registerSoundEvent("nekker_lunge");

    //Nekker Warrior
    public static RegistrySupplier<SoundEvent> NEKKER_WARRIOR_ATTACK = registerSoundEvent("nekker_warrior_attack");
    public static RegistrySupplier<SoundEvent> NEKKER_WARRIOR_HURT = registerSoundEvent("nekker_warrior_hurt");
    public static RegistrySupplier<SoundEvent> NEKKER_WARRIOR_IDLE = registerSoundEvent("nekker_warrior_idle");
    public static RegistrySupplier<SoundEvent> NEKKER_WARRIOR_DEATH = registerSoundEvent("nekker_warrior_death");
    public static RegistrySupplier<SoundEvent> NEKKER_WARRIOR_LUNGE = registerSoundEvent("nekker_warrior_lunge");

    //Cyclops
    public static RegistrySupplier<SoundEvent> CYCLOPS_ATTACK = registerSoundEvent("cyclops_attack");
    public static RegistrySupplier<SoundEvent> CYCLOPS_HURT = registerSoundEvent("cyclops_hurt");
    public static RegistrySupplier<SoundEvent> CYCLOPS_IDLE = registerSoundEvent("cyclops_idle");
    public static RegistrySupplier<SoundEvent> CYCLOPS_DEATH = registerSoundEvent("cyclops_death");
    public static RegistrySupplier<SoundEvent> CYCLOPS_PUNCH = registerSoundEvent("cyclops_punch");

    //Troll (Rock/Ice)
    public static RegistrySupplier<SoundEvent> TROLL_ATTACK = registerSoundEvent("troll_attack");
    public static RegistrySupplier<SoundEvent> TROLL_HURT = registerSoundEvent("troll_hurt");
    public static RegistrySupplier<SoundEvent> TROLL_IDLE = registerSoundEvent("troll_idle");
    public static RegistrySupplier<SoundEvent> TROLL_DEATH = registerSoundEvent("troll_death");
    public static RegistrySupplier<SoundEvent> TROLL_BLOCK_IMPACT = registerSoundEvent("troll_block_impact");
    public static RegistrySupplier<SoundEvent> TROLL_BLOCK_IMPACT_BREAK = registerSoundEvent("troll_block_impact_break");
    public static RegistrySupplier<SoundEvent> ROCK_PROJECTILE_IMPACT = registerSoundEvent("rock_projectile_impact");
    public static RegistrySupplier<SoundEvent> ROCK_PROJECTILE_THROWS = registerSoundEvent("rock_projectile_throws");
    public static RegistrySupplier<SoundEvent> TROLL_FOLLOW = registerSoundEvent("troll_follow");
    public static RegistrySupplier<SoundEvent> TROLL_WAITING = registerSoundEvent("troll_waiting");
    public static RegistrySupplier<SoundEvent> TROLL_DISMISS = registerSoundEvent("troll_dismiss");
    public static RegistrySupplier<SoundEvent> TROLL_FURIOUS = registerSoundEvent("troll_furious");
    public static RegistrySupplier<SoundEvent> TROLL_GRUNT = registerSoundEvent("troll_grunt");
    public static RegistrySupplier<SoundEvent> TROLL_BARTERING = registerSoundEvent("troll_bartering");

    //Ice Giant
    public static RegistrySupplier<SoundEvent> ICE_GIANT_ATTACK = registerSoundEvent("ice_giant_attack");
    public static RegistrySupplier<SoundEvent> ICE_GIANT_HURT = registerSoundEvent("ice_giant_hurt");
    public static RegistrySupplier<SoundEvent> ICE_GIANT_IDLE = registerSoundEvent("ice_giant_idle");
    public static RegistrySupplier<SoundEvent> ICE_GIANT_DEATH = registerSoundEvent("ice_giant_death");
    public static RegistrySupplier<SoundEvent> ICE_GIANT_PUNCH = registerSoundEvent("ice_giant_punch");
    public static RegistrySupplier<SoundEvent> ICE_GIANT_CHARGE = registerSoundEvent("ice_giant_charge");
    public static RegistrySupplier<SoundEvent> ICE_GIANT_SNORE = registerSoundEvent("ice_giant_snore");
    public static RegistrySupplier<SoundEvent> ICE_GIANT_WAKE_UP = registerSoundEvent("ice_giant_wake_up");
    public static RegistrySupplier<SoundEvent> ANCHOR_CHAIN = registerSoundEvent("anchor_chain");
    public static RegistrySupplier<SoundEvent> ANCHOR_IMPACT = registerSoundEvent("anchor_impact");
    public static RegistrySupplier<SoundEvent> ANCHOR_THROW = registerSoundEvent("anchor_throw");

    //Misc
    public static RegistrySupplier<SoundEvent> POTION_REFILLED = registerSoundEvent("potion_refill");
    public static RegistrySupplier<SoundEvent> OIL_APPLIED = registerSoundEvent("oil_applied");
    public static RegistrySupplier<SoundEvent> OIL_RAN_OUT = registerSoundEvent("oil_ran_out");
    public static RegistrySupplier<SoundEvent> INGREDIENT_POPS = registerSoundEvent("ingredient_pops");
    public static RegistrySupplier<SoundEvent> BLACK_BLOOD_HIT = registerSoundEvent("black_blood_hit");
    public static RegistrySupplier<SoundEvent> HERBALIST_WORKS = registerSoundEvent("work_herbalist");

    public static SoundEvent getSoundEvent(final String soundName) {
        return switch (soundName) {
            case "monster_emerging" -> MONSTER_EMERGING.get();
            case "monster_digging" -> MONSTER_DIGGING.get();
            case "ground_punch" -> GROUND_PUNCH.get();
            case "big_impact" -> BIG_IMPACT.get();
            case "medium_impact" -> MEDIUM_IMPACT.get();

            // Drowner Sounds
            case "drowner_attack" -> DROWNER_ATTACK.get();
            case "drowner_hurt" -> DROWNER_HURT.get();
            case "drowner_idle" -> DROWNER_IDLE.get();
            case "drowner_death" -> DROWNER_DEATH.get();
            case "drowner_footstep" -> WATERY_FOOTSTEP.get();
            case "drowner_lunge" -> DROWNER_LUNGE.get();
            case "drowner_emerging" -> DROWNER_EMERGING.get();
            case "drowner_digging" -> DROWNER_DIGGING.get();

            // Rotfiend Sounds
            case "rotfiend_attack" -> ROTFIEND_ATTACK.get();
            case "rotfiend_hurt" -> ROTFIEND_HURT.get();
            case "rotfiend_idle" -> ROTFIEND_IDLE.get();
            case "rotfiend_death" -> ROTFIEND_DEATH.get();
            case "rotfiend_lunge" -> ROTFIEND_LUNGE.get();
            case "rotfiend_exploding" -> ROTFIEND_EXPLODING.get();

            // Grave Hag Sounds
            case "grave_hag_attack" -> GRAVE_HAG_ATTACK.get();
            case "grave_hag_hurt" -> GRAVE_HAG_HURT.get();
            case "grave_hag_idle" -> GRAVE_HAG_IDLE.get();
            case "grave_hag_death" -> GRAVE_HAG_DEATH.get();
            case "grave_hag_run" -> GRAVE_HAG_RUN.get();
            case "grave_hag_tongue_attack" -> GRAVE_HAG_TONGUE_ATTACK.get();

            // Water Hag Sounds
            case "water_hag_attack" -> WATER_HAG_ATTACK.get();
            case "water_hag_hurt" -> WATER_HAG_HURT.get();
            case "water_hag_idle" -> WATER_HAG_IDLE.get();
            case "water_hag_death" -> WATER_HAG_DEATH.get();
            case "water_hag_emerging" -> WATER_HAG_EMERGING.get();
            case "water_hag_digging" -> WATER_HAG_DIGGING.get();
            case "water_hag_mud_ball_launch" -> WATER_HAG_MUD_BALL_LAUNCH.get();
            case "water_hag_mud_ball_hit" -> WATER_HAG_MUD_BALL_HIT.get();

            // Foglet Sounds
            case "foglet_attack" -> FOGLET_ATTACK.get();
            case "foglet_hurt" -> FOGLET_HURT.get();
            case "foglet_idle" -> FOGLET_IDLE.get();
            case "foglet_death" -> FOGLET_DEATH.get();
            case "foglet_fog" -> FOGLET_FOG.get();
            case "fogling_disappear" -> FOGLET_FOGLING_DISAPPEAR.get();

            // Ghoul Sounds
            case "ghoul_attack" -> GHOUL_ATTACK.get();
            case "ghoul_hurt" -> GHOUL_HURT.get();
            case "ghoul_idle" -> GHOUL_IDLE.get();
            case "ghoul_death" -> GHOUL_DEATH.get();
            case "ghoul_lunge" -> GHOUL_LUNGES.get();
            case "ghoul_scream" -> GHOUL_SCREAMS.get();
            case "ghoul_regen" -> GHOUL_REGEN.get();

            // Alghoul Sounds
            case "alghoul_attack" -> ALGHOUL_ATTACK.get();
            case "alghoul_hurt" -> ALGHOUL_HURT.get();
            case "alghoul_idle" -> ALGHOUL_IDLE.get();
            case "alghoul_death" -> ALGHOUL_DEATH.get();
            case "alghoul_lunge" -> ALGHOUL_LUNGES.get();
            case "alghoul_scream" -> ALGHOUL_SCREAMS.get();
            case "alghoul_regen" -> ALGHOUL_REGEN.get();
            case "alghoul_spikes" -> ALGHOUL_SPIKES.get();

            // Scurver Sounds
            case "scurver_attack" -> SCURVER_ATTACK.get();
            case "scurver_hurt" -> SCURVER_HURT.get();
            case "scurver_idle" -> SCURVER_IDLE.get();
            case "scurver_death" -> SCURVER_DEATH.get();
            case "scurver_lunge" -> SCURVER_LUNGE.get();
            case "scurver_exploding" -> SCURVER_EXPLODING.get();

            // Devourer Sounds
            case "devourer_attack" -> DEVOURER_ATTACK.get();
            case "devourer_hurt" -> DEVOURER_HURT.get();
            case "devourer_idle" -> DEVOURER_IDLE.get();
            case "devourer_death" -> DEVOURER_DEATH.get();
            case "devourer_jump" -> DEVOURER_JUMP.get();

            // Bloedzuiger Sounds
            case "bloedzuiger_attack" -> BLOEDZUIGER_ATTACK.get();
            case "bloedzuiger_hurt" -> BLOEDZUIGER_HURT.get();
            case "bloedzuiger_idle" -> BLOEDZUIGER_IDLE.get();
            case "bloedzuiger_death" -> BLOEDZUIGER_DEATH.get();
            case "bloedzuiger_exploding" -> BLOEDZUIGER_EXPLOSION.get();

            // Graveir Sounds
            case "graveir_attack" -> GRAVEIR_ATTACK.get();
            case "graveir_hurt" -> GRAVEIR_HURT.get();
            case "graveir_idle" -> GRAVEIR_IDLE.get();
            case "graveir_death" -> GRAVEIR_DEATH.get();
            case "graveir_ground_punch" -> GRAVEIR_GROUND_PUNCH.get();

            // Bullvore Sounds
            case "bullvore_attack" -> BULLVORE_ATTACK.get();
            case "bullvore_hurt" -> BULLVORE_HURT.get();
            case "bullvore_idle" -> BULLVORE_IDLE.get();
            case "bullvore_death" -> BULLVORE_DEATH.get();
            case "bullvore_charge" -> BULLVORE_CHARGE.get();

            // Nekker Sounds
            case "nekker_attack" -> NEKKER_ATTACK.get();
            case "nekker_hurt" -> NEKKER_HURT.get();
            case "nekker_idle" -> NEKKER_IDLE.get();
            case "nekker_death" -> NEKKER_DEATH.get();
            case "nekker_lunge" -> NEKKER_LUNGE.get();

            // Nekker Warrior Sounds
            case "nekker_warrior_attack" -> NEKKER_WARRIOR_ATTACK.get();
            case "nekker_warrior_hurt" -> NEKKER_WARRIOR_HURT.get();
            case "nekker_warrior_idle" -> NEKKER_WARRIOR_IDLE.get();
            case "nekker_warrior_death" -> NEKKER_WARRIOR_DEATH.get();
            case "nekker_warrior_lunge" -> NEKKER_WARRIOR_LUNGE.get();

            // Cyclops Sounds
            case "cyclops_attack" -> CYCLOPS_ATTACK.get();
            case "cyclops_hurt" -> CYCLOPS_HURT.get();
            case "cyclops_idle" -> CYCLOPS_IDLE.get();
            case "cyclops_death" -> CYCLOPS_DEATH.get();
            case "cyclops_punch" -> CYCLOPS_PUNCH.get();

            // Troll Sounds
            case "troll_attack" -> TROLL_ATTACK.get();
            case "troll_hurt" -> TROLL_HURT.get();
            case "troll_idle" -> TROLL_IDLE.get();
            case "troll_death" -> TROLL_DEATH.get();
            case "troll_block_impact" -> TROLL_BLOCK_IMPACT.get();
            case "troll_block_impact_break" -> TROLL_BLOCK_IMPACT_BREAK.get();
            case "rock_projectile_impact" -> ROCK_PROJECTILE_IMPACT.get();
            case "rock_projectile_throws" -> ROCK_PROJECTILE_THROWS.get();
            case "troll_follow" -> TROLL_FOLLOW.get();
            case "troll_waiting" -> TROLL_WAITING.get();
            case "troll_dismiss" -> TROLL_DISMISS.get();
            case "troll_furious" -> TROLL_FURIOUS.get();
            case "troll_grunt" -> TROLL_GRUNT.get();
            case "troll_bartering" -> TROLL_BARTERING.get();

            // Ice Giant Sounds
            case "ice_giant_attack" -> ICE_GIANT_ATTACK.get();
            case "ice_giant_hurt" -> ICE_GIANT_HURT.get();
            case "ice_giant_idle" -> ICE_GIANT_IDLE.get();
            case "ice_giant_death" -> ICE_GIANT_DEATH.get();
            case "ice_giant_punch" -> ICE_GIANT_PUNCH.get();
            case "ice_giant_charge" -> ICE_GIANT_CHARGE.get();
            case "ice_giant_snore" -> ICE_GIANT_SNORE.get();
            case "ice_giant_wake_up" -> ICE_GIANT_WAKE_UP.get();
            case "anchor_chain" -> ANCHOR_CHAIN.get();
            case "anchor_impact" -> ANCHOR_IMPACT.get();
            case "anchor_throw" -> ANCHOR_THROW.get();

            // Miscellaneous Sounds
            case "potion_refill" -> POTION_REFILLED.get();
            case "oil_applied" -> OIL_APPLIED.get();
            case "oil_ran_out" -> OIL_RAN_OUT.get();
            case "ingredient_pops" -> INGREDIENT_POPS.get();
            case "black_blood_hit" -> BLACK_BLOOD_HIT.get();
            case "work_herbalist" -> HERBALIST_WORKS.get();
            default -> null;
        };
    }

    private static RegistrySupplier<SoundEvent> registerSoundEvent(final String name) {
        return TCOTS_Registries.SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, name)));
    }

    @ExpectPlatform
    public static void initSounds() {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static Holder.Reference<SoundEvent> getRotfiendBloodExplosion(){
        throw new AssertionError();
    }
}
