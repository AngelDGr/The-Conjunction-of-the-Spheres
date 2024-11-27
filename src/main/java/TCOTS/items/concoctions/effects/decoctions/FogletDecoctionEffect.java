package TCOTS.items.concoctions.effects.decoctions;

import TCOTS.particles.TCOTS_Particles;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.util.math.MathHelper;

public class FogletDecoctionEffect extends DecoctionEffectBase {
    public FogletDecoctionEffect(StatusEffectCategory category, int color) {
        super(category, color,50);
    }

    protected void spawnFogParticlesItself(LivingEntity entity){
        if(entity.age%22 == 0){
            double d = entity.getX() + (double) MathHelper.nextBetween(entity.getRandom(), -0.8F, 0.8F);
            double e = (entity.getEyeY()-0.5f)+ (double) MathHelper.nextBetween(entity.getRandom(), -1F, 1F);
            double f = entity.getZ() + (double) MathHelper.nextBetween(entity.getRandom(), -0.8F, 0.8F);
            entity.getWorld().addParticle(TCOTS_Particles.FOGLET_FOG, d,e,f,0,0,0);
        }
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        if(entity.getWorld().isThundering() || entity.getWorld().isRaining()){
            spawnFogParticlesItself(entity);
        }

        super.applyUpdateEffect(entity, amplifier);
    }
}
