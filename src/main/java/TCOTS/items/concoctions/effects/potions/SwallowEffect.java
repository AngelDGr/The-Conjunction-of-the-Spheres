package TCOTS.items.concoctions.effects.potions;

import TCOTS.items.concoctions.effects.WitcherPotionEffect;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.player.PlayerEntity;


public class SwallowEffect extends WitcherPotionEffect {

//             Swallow = Swallow I   = 1.0hp x s =  20s for complete healing
//    Enhanced Swallow = Swallow II  = 2.0hp x s =  10s for complete healing
//    Superior Swallow = Swallow III = 3.0hp x s = 6.6s for complete healing

    public SwallowEffect(StatusEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier){
        if (entity.getHealth() < entity.getMaxHealth()) {

            entity.heal(0.05F * (float) (amplifier + 1));
            if (entity instanceof PlayerEntity) {
                ((PlayerEntity) entity).addExhaustion(0.1F * (float) (amplifier + 1));
            }
        }


        super.applyUpdateEffect(entity, amplifier);
    }

}
