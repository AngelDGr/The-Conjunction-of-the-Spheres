package TCOTS.effects.decoctions;

import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import org.jetbrains.annotations.NotNull;

public class NekkerWarriorDecoctionEffect extends DecoctionEffectBase{
    public NekkerWarriorDecoctionEffect(final MobEffectCategory category, final int color) {
        super(category, color, 50);
    }

    @Override
    public boolean hasCustomApplyTooltip() {
        return true;
    }

    @Override
    public boolean hasSpecialAttributes() {
        return true;
    }

    @Override
    public void addAttributeModifiers(@NotNull final AttributeMap attributeContainer, final int amplifier) {

    }

    @Override
    public boolean applyEffectTick(@NotNull final LivingEntity entity, final int amplifier) {
        final boolean up = super.applyEffectTick(entity, amplifier);

        this.removeAndApplyAttributes(entity, amplifier, entity.isPassenger() && entity.getVehicle() instanceof LivingEntity);

        return up;
    }

    @Override
    public int getSpecialAttributesValue(final int amplifier) {
        return 50;
    }
}
