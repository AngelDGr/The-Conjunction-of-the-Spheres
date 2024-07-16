package TCOTS.items.concoctions.effects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

public class BleedingBlackBloodEffect extends StatusEffect {
    public BleedingBlackBloodEffect(StatusEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        if(entity.age%20==0){
        entity.damage(entity.getDamageSources().magic(), amplifier+1);
        }
    }
}
