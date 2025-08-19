package TCOTS.effects.potions;

import TCOTS.effects.WitcherPotionEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class BlackBloodEffect extends WitcherPotionEffect {
    public BlackBloodEffect(final MobEffectCategory category, final int color) {
        super(category, color);
    }

    @Override
    public boolean hasSpecialAttributes() {
        return true;
    }

    @Override
    public int getSpecialAttributesValue(final int amplifier) {
        return amplifier == 0? 15: amplifier == 1? 20: 30;
    }

    @Override
    public boolean hasExtraInfo() {
        return true;
    }

    @Override
    public boolean hasExtraLine(final int amplifier) {
        return amplifier > 1;
    }

    @Override
    public boolean hasCustomApplyTooltip() {
        return true;
    }
}
