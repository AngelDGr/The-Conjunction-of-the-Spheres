package TCOTS.items.potions.effects.bombs;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.mob.MobEntity;

public class SamumEffect extends BombEffectBase {
    public SamumEffect(StatusEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        super.applyUpdateEffect(entity,amplifier);
        if(entity instanceof MobEntity mob){
            mob.setTarget(null);
            mob.getLookControl().lookAt(entity.getX(), entity.getY()-5, entity.getZ());
        }
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return true;
    }

}
