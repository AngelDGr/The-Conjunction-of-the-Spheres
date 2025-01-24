package TCOTS.mixin.northern_wind;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.entity.mob.BlazeEntity;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(BlazeEntity.class)
public class BlazeEntityMixin {
    @Unique
    BlazeEntity THIS = (BlazeEntity) (Object) this;

    @WrapWithCondition(
            method = "tickMovement",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;addParticle(Lnet/minecraft/particle/ParticleEffect;DDDDDD)V")
    )
    private boolean particleOnlyWhenNotFrozen(World instance, ParticleEffect parameters, double x, double y, double z, double velocityX, double velocityY, double velocityZ){
        return !THIS.theConjunctionOfTheSpheres$isFrozen();
    }
}
