package TCOTS.sounds;

import TCOTS.TCOTS_Main;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class TCOTS_Sounds {
    public static SoundEvent DROWNER_ATTACK = registerSoundEvent("drowner_attack");
    public static SoundEvent DROWNER_HURT = registerSoundEvent("drowner_hurt");
    public static SoundEvent DROWNER_IDLE = registerSoundEvent("drowner_idle");
    public static SoundEvent DROWNER_DEATH = registerSoundEvent("drowner_death");

    public static SoundEvent DROWNER_FOOTSTEP = registerSoundEvent("drowner_footstep");

    public static SoundEvent DROWNER_SCREAM = registerSoundEvent("drowner_scream");

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
