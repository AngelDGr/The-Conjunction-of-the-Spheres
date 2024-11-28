package TCOTS.items.concoctions.effects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

import java.util.Map;

public class WitcherPotionEffect extends StatusEffect {
    public WitcherPotionEffect(StatusEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier){
        return true;
    }

    public boolean hasCustomApplyTooltip(){
        return false;
    }

    public boolean hasSpecialAttributes(){
        return false;
    }

    public int getSpecialAttributesValue(int amplifier){
        return 0;
    }

    public boolean hasExtraInfo(){
        return false;
    }

    public boolean hasExtraLine(int amplifier){
        return false;
    }

    protected void removeAndApplyAttributes(LivingEntity entity, int amplifier, boolean conditional){
        if(conditional){
            for (Map.Entry<EntityAttribute, EntityAttributeModifier> entry : this.getAttributeModifiers().entrySet()) {
                EntityAttributeInstance entityAttributeInstance = entity.getAttributes().getCustomInstance(entry.getKey());
                if (entityAttributeInstance != null) {
                    EntityAttributeModifier entityAttributeModifier = entry.getValue();
                    entityAttributeInstance.removeModifier(entityAttributeModifier);
                    entityAttributeInstance.addPersistentModifier(
                            new EntityAttributeModifier(
                                    entityAttributeModifier.getId(),
                                    this.getTranslationKey() + " " + amplifier,
                                    this.adjustModifierAmount(amplifier, entityAttributeModifier),
                                    entityAttributeModifier.getOperation()
                            )
                    );
                }
            }
        } else {
            for (Map.Entry<EntityAttribute, EntityAttributeModifier> entry : this.getAttributeModifiers().entrySet()) {
                EntityAttributeInstance entityAttributeInstance = entity.getAttributes().getCustomInstance(entry.getKey());
                if (entityAttributeInstance != null) {
                    entityAttributeInstance.removeModifier(entry.getValue());
                }
            }
        }
    }
}
