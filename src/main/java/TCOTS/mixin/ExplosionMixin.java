package TCOTS.mixin;

import TCOTS.particles.TCOTS_Particles;
import TCOTS.sounds.TCOTS_Sounds;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.sound.SoundEvent;
import net.minecraft.world.explosion.Explosion;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(Explosion.class)
public abstract class ExplosionMixin {
    @Shadow @Final private float power;

    @ModifyArg(method = "affectWorld",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/world/World;playSound(DDDLnet/minecraft/sound/SoundEvent;Lnet/minecraft/sound/SoundCategory;FFZ)V"))
    private SoundEvent modifyRotfiendExplosionSound(SoundEvent sound){
        return this.power == 3.001234f || power == 2.601234? TCOTS_Sounds.ROTFIEND_BLOOD_EXPLOSION: sound;
    }

    @ModifyArg(method = "affectWorld",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/world/World;addParticle(Lnet/minecraft/particle/ParticleEffect;DDDDDD)V", ordinal = 0))
    private ParticleEffect modifyRotfiendExplosionParticleEmitter(ParticleEffect particleEffect){
        return this.power == 3.001234f || power == 2.601234? TCOTS_Particles.ROTFIEND_BLOOD_EMITTER: particleEffect;
    }

    @ModifyArg(method = "affectWorld",
            at = @At(value = "INVOKE",
                    target = "Lnet/minecraft/world/World;addParticle(Lnet/minecraft/particle/ParticleEffect;DDDDDD)V", ordinal = 1))
    private ParticleEffect modifyRotfiendExplosionParticleNormal(ParticleEffect particleEffect){
        return this.power == 3.001234f || power == 2.601234? TCOTS_Particles.ROTFIEND_BLOOD_EMITTER: particleEffect;
    }
}
