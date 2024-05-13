package TCOTS.mixin.northern_wind;

import net.minecraft.entity.mob.BlazeEntity;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(BlazeEntity.class)
public class BlazeEntityMixin {
    @Unique
    BlazeEntity THIS = (BlazeEntity) (Object) this;

    @Redirect(method = "tickMovement", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;addParticle(Lnet/minecraft/particle/ParticleEffect;DDDDDD)V"))
    private void disableParticlesWhenFrozen(World instance, ParticleEffect parameters, double x, double y, double z, double velocityX, double velocityY, double velocityZ){
        if(!THIS.theConjunctionOfTheSpheres$isFrozen()){
            THIS.getWorld().addParticle(ParticleTypes.LARGE_SMOKE, THIS.getParticleX(0.5),
                    THIS.getRandomBodyY(), THIS.getParticleZ(0.5), 0.0, 0.0, 0.0);
        }
    }
}
