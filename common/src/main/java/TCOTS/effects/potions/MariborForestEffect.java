package TCOTS.effects.potions;

import TCOTS.effects.WitcherPotionEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

public class MariborForestEffect extends WitcherPotionEffect {

    //Maribor Forest I      ----->      0.2 Saturation x 3 seconds
    //Maribor Forest II     ----->      0.5 Saturation x 3 seconds
    //Maribor Forest III    ----->      0.8 Saturation x 3 seconds

    public MariborForestEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public boolean applyEffectTick(LivingEntity entity, int amplifier) {
        if(!entity.level().isClientSide){
            if(entity instanceof Player playerEntity && entity.tickCount%60==0){
                playerEntity.getFoodData().eat(1, 0.2f+(0.3f*amplifier));
            }
        }

        return super.applyEffectTick(entity, amplifier);
    }

}
