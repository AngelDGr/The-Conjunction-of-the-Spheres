package TCOTS.items.concoctions.effects.decoctions;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectCategory;

public class WaterHagDecoctionEffect extends DecoctionEffectBase {

    public WaterHagDecoctionEffect(StatusEffectCategory category, int color) {
        super(category, color,50);
    }


    //It's called every tick
    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        super.applyUpdateEffect(entity, amplifier);

        this.removeAndApplyAttributes(entity, amplifier, entity.getHealth() == entity.getMaxHealth());
    }

    @Override
    public boolean hasCustomApplyTooltip() {
        return true;
    }
}
