package TCOTS.items.concoctions.effects.decoctions;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectCategory;

public class WaterHagDecoctionEffect extends DecoctionEffectBase {

    public WaterHagDecoctionEffect(StatusEffectCategory category, int color) {
        super(category, color,50);
    }

    @Override
    public boolean applyUpdateEffect(LivingEntity entity, int amplifier) {
        boolean up = super.applyUpdateEffect(entity, amplifier);

        this.removeAndApplyAttributes(entity, amplifier, entity.getHealth() == entity.getMaxHealth());

        return up;
    }

    @Override
    public boolean hasCustomApplyTooltip() {
        return true;
    }
}
