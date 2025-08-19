package TCOTS.mixin.northern_wind;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.entity.monster.Blaze;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Blaze.class)
public class BlazeEntityMixin {
    @Unique
    Blaze THIS = (Blaze) (Object) this;

    @WrapWithCondition(
            method = "aiStep",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;addParticle(Lnet/minecraft/core/particles/ParticleOptions;DDDDDD)V")
    )
    private boolean particleOnlyWhenNotFrozen(final Level instance, final ParticleOptions parameters, final double x, final double y, final double z, final double velocityX, final double velocityY, final double velocityZ){
        return !THIS.theConjunctionOfTheSpheres$isFrozen();
    }
}
