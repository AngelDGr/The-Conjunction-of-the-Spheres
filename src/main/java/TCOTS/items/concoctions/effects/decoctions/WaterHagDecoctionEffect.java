package TCOTS.items.concoctions.effects.decoctions;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeModifierCreator;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.effect.StatusEffectCategory;

import java.util.Map;

public class WaterHagDecoctionEffect extends DecoctionEffectBase {

    public WaterHagDecoctionEffect(StatusEffectCategory category, int color) {
        super(category, color,50);
    }


    //It's called every tick
    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        super.applyUpdateEffect(entity, amplifier);

        if(entity.getHealth() == entity.getMaxHealth()){
            for (Map.Entry<EntityAttribute, AttributeModifierCreator> entry : this.getAttributeModifiers().entrySet()) {
                EntityAttributeInstance entityAttributeInstance = entity.getAttributes().getCustomInstance(entry.getKey());
                if (entityAttributeInstance == null) continue;
                entityAttributeInstance.removeModifier(entry.getValue().getUuid());
                entityAttributeInstance.addPersistentModifier(entry.getValue().createAttributeModifier(amplifier));
            }
        } else {
            for (Map.Entry<EntityAttribute, AttributeModifierCreator> entry : this.getAttributeModifiers().entrySet()) {
                EntityAttributeInstance entityAttributeInstance = entity.getAttributes().getCustomInstance(entry.getKey());
                if (entityAttributeInstance == null) continue;
                entityAttributeInstance.removeModifier(entry.getValue().getUuid());
            }
        }
    }

    @Override
    public boolean hasCustomApplyTooltip() {
        return true;
    }
}
