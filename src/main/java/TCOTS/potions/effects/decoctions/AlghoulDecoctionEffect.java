package TCOTS.potions.effects.decoctions;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.player.PlayerEntity;

public class AlghoulDecoctionEffect extends StatusEffect {
    public AlghoulDecoctionEffect(StatusEffectCategory category, int color) {
        super(category, color);
    }

    private int cooldownAttacks=0;

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        if(!entity.getWorld().isClient) {
            if (entity instanceof PlayerEntity playerEntity){
                if (entity.age < (entity.getLastAttackTime() + 300)
                        && cooldownAttacks==0
                        && entity.getAttacker() == null
                        && entity.age % 20 == 0
                ) {
                    playerEntity.getHungerManager().add(amplifier + 1, 0.5f);
                }
            }
            if(entity.getAttacker() != null){
                cooldownAttacks=500;
            }
            if(cooldownAttacks>0){
                --cooldownAttacks;
            }
        }

        super.applyUpdateEffect(entity, amplifier);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }
}
