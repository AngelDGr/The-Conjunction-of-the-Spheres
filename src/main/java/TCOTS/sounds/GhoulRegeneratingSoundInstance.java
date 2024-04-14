package TCOTS.sounds;

import TCOTS.entity.necrophages.GhoulEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.sound.MovingSoundInstance;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.sound.SoundCategory;

@Environment(value= EnvType.CLIENT)
public class GhoulRegeneratingSoundInstance extends MovingSoundInstance {
    private final GhoulEntity ghoul;

    private int timer;

    public GhoulRegeneratingSoundInstance(GhoulEntity ghoul) {
        super(TCOTS_Sounds.GHOUL_REGEN, SoundCategory.HOSTILE, SoundInstance.createRandom());
        this.ghoul = ghoul;
        this.attenuationType = AttenuationType.LINEAR;
        this.repeat = true;
        this.repeatDelay = 0;
        this.timer=0;
        this.volume = 0.5f;
        this.pitch = 1.0f;
    }

    @Override
    public void tick() {
        ++timer;

        if (this.ghoul.isRemoved() || !this.ghoul.getIsRegenerating()) {
            this.setDone();
            return;
        }

        this.x = (float)this.ghoul.getX();
        this.y = (float)this.ghoul.getY();
        this.z = (float)this.ghoul.getZ();

        if(timer > (GhoulEntity.GHOUL_REGENERATION_TIME-5)){
            volume = volume - 0.1f;
        }
    }

    @Override
    public boolean canPlay() {
        return !this.ghoul.isSilent();
    }
}
