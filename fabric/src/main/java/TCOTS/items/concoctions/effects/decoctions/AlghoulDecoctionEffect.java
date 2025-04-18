package TCOTS.items.concoctions.effects.decoctions;

import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

public class AlghoulDecoctionEffect extends DecoctionEffectBase {
    public AlghoulDecoctionEffect(MobEffectCategory category, int color) {
        super(category, color,50);
    }

    private int cooldownAttacks=0;

    @Override
    public boolean applyEffectTick(@NotNull LivingEntity entity, int amplifier) {
        if(!entity.level().isClientSide) {
            if (entity instanceof Player playerEntity){
                if (entity.tickCount < (entity.getLastHurtMobTimestamp() + 300)
                        && cooldownAttacks==0
                        && entity.getLastHurtByMob() == null
                        && entity.tickCount % 20 == 0
                ) {
                    playerEntity.getFoodData().eat(amplifier + 1, 0.5f);
                }
            }
            if(entity.getLastHurtByMob() != null){
                cooldownAttacks=500;
            }
            if(cooldownAttacks>0){
                --cooldownAttacks;
            }
        }

        return super.applyEffectTick(entity, amplifier);
    }

}
