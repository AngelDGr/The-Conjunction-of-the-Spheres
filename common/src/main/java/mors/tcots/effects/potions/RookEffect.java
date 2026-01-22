package mors.tcots.effects.potions;

import mors.tcots.effects.WitcherPotionEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class RookEffect extends WitcherPotionEffect {
    public RookEffect(final MobEffectCategory category, final int color) {
        super(category, color);
    }

    @Override
    public boolean hasCustomApplyTooltip() {
        return true;
    }

    @Override
    public int specialAttributesValue(final int amplifier) {
        return amplifier+2;
    }
}
