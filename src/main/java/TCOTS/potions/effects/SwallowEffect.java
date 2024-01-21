package TCOTS.potions.effects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.client.gui.hud.PlayerListHud;
import net.minecraft.entity.effect.StatusEffectUtil;


public class SwallowEffect extends StatusEffect {

//             Swallow = Swallow I   = 1.0hp x s =  20s for complete heal
//    Enhanced Swallow = Swallow II  = 2.0hp x s =  10s for complete heal
//    Superior Swallow = Swallow III = 3.0hp x s = 6.6s for complete heal

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

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier){
        return true;
    }
}
