package TCOTS.items.potions.effects;

import net.minecraft.entity.effect.StatusEffectCategory;

public class BlackBloodEffect extends WitcherPotionEffect {
    public BlackBloodEffect(StatusEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public boolean hasSpecialAttributes() {
        return true;
    }

    @Override
    public int getSpecialAttributesValue(int amplifier) {
        return amplifier == 0? 15: amplifier == 1? 20: 30;
    }

    @Override
    public boolean hasExtraInfo() {
        return true;
    }

    @Override
    public boolean hasExtraLine(int amplifier) {
        return amplifier > 1;
    }
}
