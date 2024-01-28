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
    public static SoundEvent DROWNER_SCREAM = registerSoundEvent("drowner_scream");

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



    //Nekker
    public static SoundEvent NEKKER_ATTACK = registerSoundEvent("nekker_attack");
    public static SoundEvent NEKKER_HURT = registerSoundEvent("nekker_hurt");
    public static SoundEvent NEKKER_IDLE = registerSoundEvent("nekker_idle");
    public static SoundEvent NEKKER_DEATH = registerSoundEvent("nekker_death");
    public static SoundEvent NEKKER_LUNGE = registerSoundEvent("nekker_lunge");

//    public static final BlockSoundGroup ANIMATED_BLOCK_SOUNDS = new BlockSoundGroup(1f, 1f,
//            ModSounds.ANIMATED_BLOCK_BREAK, ModSounds.ANIMATED_BLOCK_WALK, ModSounds.ANIMATED_BLOCK_PLACE,
//            ModSounds.ANIMATED_BLOCK_HIT, ModSounds.ANIMATED_BLOCK_WALK);

    private static SoundEvent registerSoundEvent(String name) {
        Identifier id = new Identifier(TCOTS_Main.MOD_ID, name);
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
    }

    public static void init() {
        TCOTS_Main.LOGGER.debug("Registering sounds");
    }
}
