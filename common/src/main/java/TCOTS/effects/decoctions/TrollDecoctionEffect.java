package TCOTS.effects.decoctions;

import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

public class TrollDecoctionEffect extends DecoctionEffectBase{
//             Swallow = Swallow I   = 1.0hp x s =  20s for complete healing

    public TrollDecoctionEffect(final MobEffectCategory category, final int color) {
        super(category, color, 50);
    }

    @Override
    public boolean applyEffectTick(@NotNull final LivingEntity entity, final int amplifier){
        if (entity.getHealth() < entity.getMaxHealth()) {
            entity.heal(0.05F * (float) (amplifier + 1));
        }

       return super.applyEffectTick(entity, amplifier);
    }
}
