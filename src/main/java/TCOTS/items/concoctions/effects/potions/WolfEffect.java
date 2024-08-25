package TCOTS.items.concoctions.effects.potions;

import TCOTS.items.concoctions.effects.WitcherPotionEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.util.math.MathHelper;

public class WolfEffect extends WitcherPotionEffect {
    public WolfEffect(StatusEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public boolean hasSpecialAttributes() {
        return true;
    }

    public int getSpecialAttributesValue(int amplifier){
        return (MathHelper.ceil((1f/0.03f) + (amplifier*(1f/0.03f))))-1;
    }

}
