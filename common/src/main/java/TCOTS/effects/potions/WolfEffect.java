package TCOTS.effects.potions;

import TCOTS.effects.WitcherPotionEffect;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectCategory;

public class WolfEffect extends WitcherPotionEffect {
    public WolfEffect(final MobEffectCategory category, final int color) {
        super(category, color);
    }

    @Override
    public boolean hasSpecialAttributes() {
        return true;
    }

    public int getSpecialAttributesValue(final int amplifier){
        return (Mth.ceil((1f/0.03f) + (amplifier*(1f/0.03f))))-1;
    }

}
