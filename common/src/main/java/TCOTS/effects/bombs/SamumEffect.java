package TCOTS.effects.bombs;

import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import org.jetbrains.annotations.NotNull;

public class SamumEffect extends BombEffectBase {
    public SamumEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public boolean applyEffectTick(@NotNull LivingEntity entity, int amplifier) {
        if(entity instanceof Mob mob){
            mob.setTarget(null);
            mob.getLookControl().setLookAt(entity.getX(), entity.getY()-5, entity.getZ());
        }

        return super.applyEffectTick(entity,amplifier);
    }
}
