package TCOTS.potions.effects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.*;
import net.minecraft.entity.effect.StatusEffectCategory;

import java.util.Map;


public class KillerWhaleEffect extends WitcherEffect {
    //xTODO: Add attack power to the player
    //xTODO???: Add improved vision underwater (like with water breathing)

    //Each level increase strength by 4
    //Works like Respiration II


    public KillerWhaleEffect(StatusEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {

        if(entity.isTouchingWater()){
            for (Map.Entry<EntityAttribute, AttributeModifierCreator> entry : this.getAttributeModifiers().entrySet()) {
                EntityAttributeInstance entityAttributeInstance = entity.getAttributes().getCustomInstance(entry.getKey());
                if (entityAttributeInstance == null) continue;
                entityAttributeInstance.removeModifier(entry.getValue().getUuid());
                entityAttributeInstance.addPersistentModifier(entry.getValue().createAttributeModifier(amplifier));
            }
        }else{
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
