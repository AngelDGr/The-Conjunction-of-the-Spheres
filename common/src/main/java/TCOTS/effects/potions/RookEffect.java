package TCOTS.effects.potions;

import TCOTS.effects.WitcherPotionEffect;
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
    public boolean hasSpecialAttributes() {
        return true;
    }

    @Override
    public int getSpecialAttributesValue(final int amplifier) {
        return amplifier+2;
    }
}
