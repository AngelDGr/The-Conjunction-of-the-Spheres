package TCOTS.potions.effects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.enchantment.Enchantment;


public class KillerWhaleEffect extends StatusEffect {
    public KillerWhaleEffect(StatusEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier){


//        if (entity.getHealth() < entity.getMaxHealth()) {
//
//            entity.heal(0.05F * (float) (amplifier + 1));
//            if (entity instanceof PlayerEntity) {
//                ((PlayerEntity) entity).addExhaustion(0.1F * (float) (amplifier + 1));
//            }
//        }
        entity.getMaxAir();

        super.applyUpdateEffect(entity, amplifier);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier){
        return true;
    }

}
