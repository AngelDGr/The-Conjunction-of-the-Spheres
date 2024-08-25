package TCOTS.items.concoctions.effects.potions;

import TCOTS.items.concoctions.effects.WitcherPotionEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

public class RookEffect extends WitcherPotionEffect {
    public RookEffect(StatusEffectCategory category, int color) {
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
    public int getSpecialAttributesValue(int amplifier) {
        return amplifier+2;
    }
}
