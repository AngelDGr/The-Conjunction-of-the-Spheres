package TCOTS.items.concoctions.effects.decoctions;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffectCategory;

public class NekkerWarriorDecoctionEffect extends DecoctionEffectBase{
    public NekkerWarriorDecoctionEffect(StatusEffectCategory category, int color) {
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
    public void onApplied(AttributeContainer attributeContainer, int amplifier) {

    }

    @Override
    public boolean applyUpdateEffect(LivingEntity entity, int amplifier) {
        boolean up = super.applyUpdateEffect(entity, amplifier);

        this.removeAndApplyAttributes(entity, amplifier, entity.hasVehicle() && entity.getVehicle() instanceof LivingEntity);

        return up;
    }

    @Override
    public int getSpecialAttributesValue(int amplifier) {
        return 50;
    }
}
