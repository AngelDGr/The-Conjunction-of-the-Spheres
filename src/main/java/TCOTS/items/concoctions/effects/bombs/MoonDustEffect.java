package TCOTS.items.concoctions.effects.bombs;

import TCOTS.items.concoctions.TCOTS_Effects;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffects;

public class MoonDustEffect extends BombEffectBase{
    public MoonDustEffect(StatusEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public boolean applyUpdateEffect(LivingEntity entity, int amplifier) {
        //Remove invisibility effect
        if(entity.hasStatusEffect(StatusEffects.INVISIBILITY))
            entity.removeStatusEffect(StatusEffects.INVISIBILITY);

        //Removes regeneration effect
        if(entity.hasStatusEffect(StatusEffects.REGENERATION))
            entity.removeStatusEffect(StatusEffects.REGENERATION);

        //Removes Swallow
        if(entity.hasStatusEffect(TCOTS_Effects.SWALLOW_EFFECT))
            entity.removeStatusEffect(TCOTS_Effects.SWALLOW_EFFECT);

        //Removes Troll decoction
        if(entity.hasStatusEffect(TCOTS_Effects.TROLL_DECOCTION_EFFECT))
            entity.removeStatusEffect(TCOTS_Effects.TROLL_DECOCTION_EFFECT);

        //Removes Grave Hag decoction
        if(entity.hasStatusEffect(TCOTS_Effects.GRAVE_HAG_DECOCTION_EFFECT))
            entity.removeStatusEffect(TCOTS_Effects.GRAVE_HAG_DECOCTION_EFFECT);

        return super.applyUpdateEffect(entity, amplifier);
    }
}
