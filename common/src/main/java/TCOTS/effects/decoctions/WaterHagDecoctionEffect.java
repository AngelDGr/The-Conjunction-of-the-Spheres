package TCOTS.effects.decoctions;

import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

public class WaterHagDecoctionEffect extends DecoctionEffectBase {

    public WaterHagDecoctionEffect(MobEffectCategory category, int color) {
        super(category, color,50);
    }

    @Override
    public boolean applyEffectTick(@NotNull LivingEntity entity, int amplifier) {
        boolean up = super.applyEffectTick(entity, amplifier);

        this.removeAndApplyAttributes(entity, amplifier, entity.getHealth() == entity.getMaxHealth());

        return up;
    }

    @Override
    public boolean hasCustomApplyTooltip() {
        return true;
    }
}
