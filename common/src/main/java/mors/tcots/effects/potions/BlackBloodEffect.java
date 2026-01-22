package mors.tcots.effects.potions;

import mors.tcots.effects.WitcherPotionEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class BlackBloodEffect extends WitcherPotionEffect {
    public BlackBloodEffect(final MobEffectCategory category, final int color) {
        super(category, color);
    }

    @Override
    public int specialAttributesValue(final int amplifier) {
        return amplifier == 0? 15: amplifier == 1? 20: 30;
    }

    @Override
    public boolean hasPerLevelDescription() {
        return true;
    }

    @Override
    public boolean hasCustomApplyTooltip() {
        return true;
    }
}
