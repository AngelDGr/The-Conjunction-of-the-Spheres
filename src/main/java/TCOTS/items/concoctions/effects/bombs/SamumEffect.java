package TCOTS.items.concoctions.effects.bombs;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.mob.MobEntity;

public class SamumEffect extends BombEffectBase {
    public SamumEffect(StatusEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public boolean applyUpdateEffect(LivingEntity entity, int amplifier) {
        if(entity instanceof MobEntity mob){
            mob.setTarget(null);
            mob.getLookControl().lookAt(entity.getX(), entity.getY()-5, entity.getZ());
        }

        return super.applyUpdateEffect(entity,amplifier);
    }
}
