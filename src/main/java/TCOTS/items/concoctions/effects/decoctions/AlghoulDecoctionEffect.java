package TCOTS.items.concoctions.effects.decoctions;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.player.PlayerEntity;

public class AlghoulDecoctionEffect extends DecoctionEffectBase {
    public AlghoulDecoctionEffect(StatusEffectCategory category, int color) {
        super(category, color,50);
    }

    private int cooldownAttacks=0;

    @Override
    public boolean applyUpdateEffect(LivingEntity entity, int amplifier) {
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

        return super.applyUpdateEffect(entity, amplifier);
    }

}
