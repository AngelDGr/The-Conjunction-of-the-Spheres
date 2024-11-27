package TCOTS.items.concoctions.effects.potions;

import TCOTS.items.concoctions.effects.WitcherPotionEffect;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectCategory;


public class KillerWhaleEffect extends WitcherPotionEffect {
    //xTODO: Add attack power to the player

    //Each level increase strength by 4
    //Works like Respiration II


    public KillerWhaleEffect(StatusEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        super.applyUpdateEffect(entity, amplifier);

        this.removeAndApplyAttributes(entity, amplifier, entity.getHealth() == entity.getMaxHealth());
    }

    @Override
    public boolean hasCustomApplyTooltip() {
        return true;
    }
}
