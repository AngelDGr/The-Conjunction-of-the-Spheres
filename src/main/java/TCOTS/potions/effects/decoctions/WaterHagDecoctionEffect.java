package TCOTS.potions.effects.decoctions;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeModifierCreator;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

import java.util.Map;

public class WaterHagDecoctionEffect extends StatusEffect {

    public WaterHagDecoctionEffect(StatusEffectCategory category, int color) {
        super(category, color);
    }


    //It's called every tick
    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {

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

        super.applyUpdateEffect(entity, amplifier);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier){
        return true;
    }

}
