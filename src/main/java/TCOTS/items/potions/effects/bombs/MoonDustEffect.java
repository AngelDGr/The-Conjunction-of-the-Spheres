package TCOTS.items.potions.effects.bombs;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffects;

public class MoonDustEffect extends BombEffectBase{
    public MoonDustEffect(StatusEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        //Remove invisibility effect
        if(entity.hasStatusEffect(StatusEffects.INVISIBILITY))
            entity.removeStatusEffect(StatusEffects.INVISIBILITY);

        //Removes regeneration effect
        if(entity.hasStatusEffect(StatusEffects.REGENERATION))
            entity.removeStatusEffect(StatusEffects.REGENERATION);
    }
}
