package TCOTS.effects.bombs;

import TCOTS.registry.TCOTS_Effects;
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
        if(entity.hasEffect(TCOTS_Effects.SwallowEffect()))
            entity.removeEffect(TCOTS_Effects.SwallowEffect());

        //Removes Troll decoction
        if(entity.hasEffect(TCOTS_Effects.TrollDecoctionEffect()))
            entity.removeEffect(TCOTS_Effects.TrollDecoctionEffect());

        //Removes Grave Hag decoction
        if(entity.hasEffect(TCOTS_Effects.GraveHagDecoctionEffect()))
            entity.removeEffect(TCOTS_Effects.GraveHagDecoctionEffect());

        return super.applyEffectTick(entity, amplifier);
    }
}
