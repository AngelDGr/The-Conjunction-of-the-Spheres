package TCOTS.items.concoctions.effects.bombs;

import TCOTS.effects.bombs.BombEffectBase;
import TCOTS.items.concoctions.TCOTS_Effects;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;

public class MoonDustEffect extends BombEffectBase {
    public MoonDustEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public boolean applyEffectTick(LivingEntity entity, int amplifier) {
        //Remove invisibility effect
        if(entity.hasEffect(MobEffects.INVISIBILITY))
            entity.removeEffect(MobEffects.INVISIBILITY);

        //Removes regeneration effect
        if(entity.hasEffect(MobEffects.REGENERATION))
            entity.removeEffect(MobEffects.REGENERATION);

        //Removes Swallow
        if(entity.hasEffect(TCOTS_Effects.SWALLOW_EFFECT))
            entity.removeEffect(TCOTS_Effects.SWALLOW_EFFECT);

        //Removes Troll decoction
        if(entity.hasEffect(TCOTS_Effects.TROLL_DECOCTION_EFFECT))
            entity.removeEffect(TCOTS_Effects.TROLL_DECOCTION_EFFECT);

        //Removes Grave Hag decoction
        if(entity.hasEffect(TCOTS_Effects.GRAVE_HAG_DECOCTION_EFFECT))
            entity.removeEffect(TCOTS_Effects.GRAVE_HAG_DECOCTION_EFFECT);

        return super.applyEffectTick(entity, amplifier);
    }
}
