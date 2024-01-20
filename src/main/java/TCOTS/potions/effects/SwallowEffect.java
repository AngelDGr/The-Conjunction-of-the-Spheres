package TCOTS.potions.effects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

public class SwallowEffect extends StatusEffect {
    public SwallowEffect(StatusEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public void applyUpdateEffect(LivingEntity livingEntity, int amplifier){

        super.applyUpdateEffect(livingEntity, amplifier);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier){

        return true;
    }
}
