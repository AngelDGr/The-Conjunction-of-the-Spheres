package TCOTS.effects;

import TCOTS.registry.TCOTS_DamageTypes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class BleedingBlackBloodEffect extends MobEffect {
    public BleedingBlackBloodEffect(final MobEffectCategory category, final int color) {
        super(category, color);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(final int duration, final int amplifier) {
        return duration%20==0;
    }

    @Override
    public boolean applyEffectTick(final LivingEntity entity, final int amplifier) {
        entity.hurt(TCOTS_DamageTypes.bleedDamage(entity.level()), amplifier+1);
        return super.applyEffectTick(entity, amplifier);
    }
}
