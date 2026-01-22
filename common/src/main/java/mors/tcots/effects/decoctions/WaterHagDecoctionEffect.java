package mors.tcots.effects.decoctions;

import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

public class WaterHagDecoctionEffect extends DecoctionEffectBase {

    public WaterHagDecoctionEffect(final MobEffectCategory category, final int color) {
        super(category, color,50);
    }

    @Override
    public boolean applyEffectTick(@NotNull final LivingEntity entity, final int amplifier) {
        final boolean up = super.applyEffectTick(entity, amplifier);

        this.removeAndApplyAttributes(entity, amplifier, entity.getHealth() == entity.getMaxHealth());

        return up;
    }

    @Override
    public boolean hasCustomApplyTooltip() {
        return true;
    }
}
