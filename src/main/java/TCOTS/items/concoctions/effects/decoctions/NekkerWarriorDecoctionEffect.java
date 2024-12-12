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
    public void onApplied(LivingEntity entity, AttributeContainer attributes, int amplifier) {

    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        super.applyUpdateEffect(entity, amplifier);

        this.removeAndApplyAttributes(entity, amplifier, entity.hasVehicle() && entity.getVehicle() instanceof LivingEntity);


    }

    @Override
    public int getSpecialAttributesValue(int amplifier) {
        return 50;
    }
}
