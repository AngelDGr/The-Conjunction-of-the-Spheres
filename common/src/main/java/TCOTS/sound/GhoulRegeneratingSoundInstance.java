package TCOTS.sound;

import TCOTS.entity.monsters.necrophages.GhoulEntity;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundSource;

public class GhoulRegeneratingSoundInstance extends AbstractTickableSoundInstance {
    private final GhoulEntity ghoul;
    private int timer;

    public GhoulRegeneratingSoundInstance(final GhoulEntity ghoul) {
        super(ghoul.getRegeneratingSound(), SoundSource.HOSTILE, SoundInstance.createUnseededRandom());
        this.ghoul = ghoul;
        this.attenuation = Attenuation.LINEAR;
        this.looping = true;
        this.delay = 0;
        this.timer=0;
        this.volume = 0.5f;
        this.pitch = 1.0f;
    }

    @Override
    public void tick() {
        ++timer;

        if (this.ghoul.isRemoved() || !this.ghoul.getIsRegenerating()) {
            this.stop();
            return;
        }

        this.x = (float)this.ghoul.getX();
        this.y = (float)this.ghoul.getY();
        this.z = (float)this.ghoul.getZ();

        if(timer > (this.ghoul.getRegenerationTime()-5)){
            volume = volume - 0.1f;
        }
    }

    @Override
    public boolean canPlaySound() {
        return !this.ghoul.isSilent();
    }
}
