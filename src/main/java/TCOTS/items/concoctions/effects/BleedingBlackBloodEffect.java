package TCOTS.items.concoctions.effects;

import TCOTS.world.TCOTS_DamageTypes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

public class BleedingBlackBloodEffect extends StatusEffect {
    public BleedingBlackBloodEffect(StatusEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return duration%20==0;
    }

    @Override
    public boolean applyUpdateEffect(LivingEntity entity, int amplifier) {
        entity.damage(TCOTS_DamageTypes.bleedDamage(entity.getWorld()), amplifier+1);
        return super.applyUpdateEffect(entity, amplifier);
    }
}
