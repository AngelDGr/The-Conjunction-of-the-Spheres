package mors.tcots.effects.potions;

import mors.tcots.effects.WitcherPotionEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;


public class KillerWhaleEffect extends WitcherPotionEffect {
    //xTODO: Add attack power to the player

    //Each level increase strength by 4
    //Works like Respiration II


    public KillerWhaleEffect(final MobEffectCategory category, final int color) {
        super(category, color);
    }

    @Override
    public boolean applyEffectTick(final LivingEntity entity, final int amplifier) {
        final boolean up = super.applyEffectTick(entity, amplifier);

        this.removeAndApplyAttributes(entity, amplifier, entity.isInWater());

        return up;
    }

    @Override
    public boolean hasCustomApplyTooltip() {
        return true;
    }
}
