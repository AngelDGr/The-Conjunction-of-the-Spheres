package TCOTS.effects.potions;

import TCOTS.effects.WitcherPotionEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;


public class SwallowEffect extends WitcherPotionEffect {

//             Swallow = Swallow I   = 1.0hp x s =  20s for complete healing
//    Enhanced Swallow = Swallow II  = 2.0hp x s =  10s for complete healing
//    Superior Swallow = Swallow III = 3.0hp x s = 6.6s for complete healing

    public SwallowEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public boolean applyEffectTick(LivingEntity entity, int amplifier){
        if (entity.getHealth() < entity.getMaxHealth()) {

            entity.heal(0.05F * (float) (amplifier + 1));
            if (entity instanceof Player) {
                ((Player) entity).causeFoodExhaustion(0.1F * (float) (amplifier + 1));
            }
        }


        return super.applyEffectTick(entity, amplifier);
    }

}
