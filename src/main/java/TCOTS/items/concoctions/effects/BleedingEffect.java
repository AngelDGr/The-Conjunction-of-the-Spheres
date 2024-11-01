package TCOTS.items.concoctions.effects;

import TCOTS.world.TCOTS_DamageTypes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

public class BleedingEffect extends StatusEffect {

    public BleedingEffect(StatusEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return duration%40==0;
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {

        entity.damage(TCOTS_DamageTypes.bleedDamage(entity.getWorld()), amplifier+1);
    }

}
