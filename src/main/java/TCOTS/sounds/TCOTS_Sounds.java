package TCOTS.sounds;

import TCOTS.TCOTS_Main;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class TCOTS_Sounds {

    //Drowner
    public static SoundEvent DROWNER_ATTACK = registerSoundEvent("drowner_attack");
    public static SoundEvent DROWNER_HURT = registerSoundEvent("drowner_hurt");
    public static SoundEvent DROWNER_IDLE = registerSoundEvent("drowner_idle");
    public static SoundEvent DROWNER_DEATH = registerSoundEvent("drowner_death");
    public static SoundEvent DROWNER_FOOTSTEP = registerSoundEvent("drowner_footstep");
    public static SoundEvent DROWNER_LUNGE = registerSoundEvent("drowner_lunge");
    public static SoundEvent DROWNER_EMERGING = registerSoundEvent("drowner_emerging");
    public static SoundEvent DROWNER_DIGGING = registerSoundEvent("drowner_digging");
//    public static SoundEvent DROWNER_SCREAM = registerSoundEvent("drowner_scream");

    //Rotfiend
    public static SoundEvent ROTFIEND_ATTACK = registerSoundEvent("rotfiend_attack");
    public static SoundEvent ROTFIEND_HURT = registerSoundEvent("rotfiend_hurt");
    public static SoundEvent ROTFIEND_IDLE = registerSoundEvent("rotfiend_idle");
    public static SoundEvent ROTFIEND_DEATH = registerSoundEvent("rotfiend_death");
    public static SoundEvent ROTFIEND_LUNGE = registerSoundEvent("rotfiend_lunge");
    public static SoundEvent ROTFIEND_EXPLODING = registerSoundEvent("rotfiend_exploding");
    public static SoundEvent ROTFIEND_BLOOD_EXPLOSION = registerSoundEvent("rotfiend_blood_explosion");
    public static SoundEvent ROTFIEND_EMERGING = registerSoundEvent("rotfiend_emerging");
    public static SoundEvent ROTFIEND_DIGGING = registerSoundEvent("rotfiend_digging");


    //Grave Hag
    public static SoundEvent GRAVE_HAG_ATTACK = registerSoundEvent("grave_hag_attack");
    public static SoundEvent GRAVE_HAG_HURT = registerSoundEvent("grave_hag_hurt");
    public static SoundEvent GRAVE_HAG_IDLE = registerSoundEvent("grave_hag_idle");
    public static SoundEvent GRAVE_HAG_DEATH = registerSoundEvent("grave_hag_death");
    public static SoundEvent GRAVE_HAG_RUN = registerSoundEvent("grave_hag_run");
    public static SoundEvent GRAVE_HAG_TONGUE_ATTACK = registerSoundEvent("grave_hag_tongue_attack");

    //Water Hag
    public static SoundEvent WATER_HAG_ATTACK = registerSoundEvent("water_hag_attack");
    public static SoundEvent WATER_HAG_HURT = registerSoundEvent("water_hag_hurt");
    public static SoundEvent WATER_HAG_IDLE = registerSoundEvent("water_hag_idle");
    public static SoundEvent WATER_HAG_DEATH = registerSoundEvent("water_hag_death");
    public static SoundEvent WATER_HAG_EMERGING = registerSoundEvent("water_hag_emerging");
    public static SoundEvent WATER_HAG_DIGGING = registerSoundEvent("water_hag_digging");
    public static SoundEvent WATER_HAG_MUD_BALL_LAUNCH = registerSoundEvent("water_hag_mud_ball_launch");
//    public static SoundEvent WATER_HAG_MUD_BALL_HIT = registerSoundEvent("water_hag_mud_ball_hit");

    //Foglet
    public static SoundEvent FOGLET_ATTACK = registerSoundEvent("foglet_attack");
    public static SoundEvent FOGLET_HURT = registerSoundEvent("foglet_hurt");
    public static SoundEvent FOGLET_IDLE = registerSoundEvent("foglet_idle");
    public static SoundEvent FOGLET_DEATH = registerSoundEvent("foglet_death");
    public static SoundEvent FOGLET_FOG = registerSoundEvent("foglet_fog");

    public static SoundEvent FOGLET_FOGLING_DISAPPEAR = registerSoundEvent("fogling_disappear");

    //Nekker
    public static SoundEvent NEKKER_ATTACK = registerSoundEvent("nekker_attack");
    public static SoundEvent NEKKER_HURT = registerSoundEvent("nekker_hurt");
    public static SoundEvent NEKKER_IDLE = registerSoundEvent("nekker_idle");
    public static SoundEvent NEKKER_DEATH = registerSoundEvent("nekker_death");
    public static SoundEvent NEKKER_LUNGE = registerSoundEvent("nekker_lunge");
    public static SoundEvent NEKKER_EMERGING = registerSoundEvent("nekker_emerging");
    public static SoundEvent NEKKER_DIGGING = registerSoundEvent("nekker_digging");

    public static SoundEvent POTION_REFILLED = registerSoundEvent("potion_refill");

    public static SoundEvent OIL_APPLIED = registerSoundEvent("oil_applied");

    public static SoundEvent OIL_RAN_OUT = registerSoundEvent("oil_ran_out");

    private static SoundEvent registerSoundEvent(String name) {
        Identifier id = new Identifier(TCOTS_Main.MOD_ID, name);
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
    }

    public static void init() {
        TCOTS_Main.LOGGER.debug("Registering sounds");
    }
}
