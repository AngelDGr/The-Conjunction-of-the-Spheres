package TCOTS.items.concoctions.effects.decoctions;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectCategory;

public class TrollDecoctionEffect extends DecoctionEffectBase{
//             Swallow = Swallow I   = 1.0hp x s =  20s for complete healing

    public TrollDecoctionEffect(StatusEffectCategory category, int color) {
        super(category, color, 50);
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier){
        if (entity.getHealth() < entity.getMaxHealth()) {
            entity.heal(0.05F * (float) (amplifier + 1));
        }

        super.applyUpdateEffect(entity, amplifier);
    }
}
