package TCOTS.items.concoctions.effects.decoctions;

import TCOTS.particles.TCOTS_Particles;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

public class FogletDecoctionEffect extends DecoctionEffectBase {
    public FogletDecoctionEffect(MobEffectCategory category, int color) {
        super(category, color,50);
    }

    protected void spawnFogParticlesItself(LivingEntity entity){
        if(entity.tickCount%22 == 0){
            double d = entity.getX() + (double) Mth.randomBetween(entity.getRandom(), -0.8F, 0.8F);
            double e = (entity.getEyeY()-0.5f)+ (double) Mth.randomBetween(entity.getRandom(), -1F, 1F);
            double f = entity.getZ() + (double) Mth.randomBetween(entity.getRandom(), -0.8F, 0.8F);
            entity.level().addParticle(TCOTS_Particles.FOGLET_FOG, d,e,f,0,0,0);
        }
    }

    @Override
    public boolean applyEffectTick(@NotNull LivingEntity entity, int amplifier) {
        if(entity.level().isThundering() || entity.level().isRaining()){
            spawnFogParticlesItself(entity);
        }

        return super.applyEffectTick(entity, amplifier);
    }
}
