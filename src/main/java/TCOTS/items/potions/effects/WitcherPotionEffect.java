package TCOTS.items.potions.effects;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

public class WitcherPotionEffect extends StatusEffect {
    public WitcherPotionEffect(StatusEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier){
        return true;
    }

    public boolean hasCustomApplyTooltip(){
        return false;
    }

    public boolean hasSpecialAttributes(){
        return false;
    }

    public int getSpecialAttributesValue(int amplifier){
        return 0;
    }

    public boolean hasExtraInfo(){
        return false;
    }

    public boolean hasExtraLine(int amplifier){
        return false;
    }
}
