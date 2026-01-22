package mors.tcots.effects.bombs;

import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import org.jetbrains.annotations.NotNull;

public class SamumEffect extends BombEffectBase {
    public SamumEffect(final MobEffectCategory category, final int color) {
        super(category, color);
    }

    @Override
    public boolean applyEffectTick(@NotNull final LivingEntity entity, final int amplifier) {
        if(entity instanceof final Mob mob){
            mob.setTarget(null);
            mob.getLookControl().setLookAt(entity.getX(), entity.getY()-5, entity.getZ());
        }

        return super.applyEffectTick(entity,amplifier);
    }
}
