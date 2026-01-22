package mors.tcots.effects.potions;

import mors.tcots.effects.WitcherPotionEffect;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectCategory;

public class WolfEffect extends WitcherPotionEffect {
    public WolfEffect(final MobEffectCategory category, final int color) {
        super(category, color);
    }

    public int specialAttributesValue(final int amplifier){
        return (Mth.ceil((1f/0.03f) + (amplifier*(1f/0.03f))))-1;
    }

}
