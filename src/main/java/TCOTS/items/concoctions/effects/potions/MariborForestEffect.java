package TCOTS.items.concoctions.effects.potions;

import TCOTS.items.concoctions.effects.WitcherPotionEffect;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.player.PlayerEntity;

public class MariborForestEffect extends WitcherPotionEffect {

    //Maribor Forest I      ----->      0.2 Saturation x 3 seconds
    //Maribor Forest II     ----->      0.5 Saturation x 3 seconds
    //Maribor Forest III    ----->      0.8 Saturation x 3 seconds

    public MariborForestEffect(StatusEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public boolean applyUpdateEffect(LivingEntity entity, int amplifier) {
        if(!entity.getWorld().isClient){
            if(entity instanceof PlayerEntity playerEntity && entity.age%60==0){
                playerEntity.getHungerManager().add(1, 0.2f+(0.3f*amplifier));
            }
        }

        return super.applyUpdateEffect(entity, amplifier);
    }

}
