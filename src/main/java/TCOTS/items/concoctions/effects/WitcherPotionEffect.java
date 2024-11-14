package TCOTS.items.concoctions.effects;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

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

    protected final Map<RegistryEntry<EntityAttribute>, EffectAttributeModifierCreator> attributeModifiersExtra = new Object2ObjectOpenHashMap<>();

    @Override
    public StatusEffect addAttributeModifier(RegistryEntry<EntityAttribute> attribute, Identifier id, double amount, EntityAttributeModifier.Operation operation) {
        this.attributeModifiersExtra.put(attribute, new EffectAttributeModifierCreator(id, amount, operation));
        return super.addAttributeModifier(attribute, id, amount, operation);
    }

    protected record EffectAttributeModifierCreator(Identifier id, double baseValue, EntityAttributeModifier.Operation operation) {
        public EntityAttributeModifier createAttributeModifier(int amplifier) {
            return new EntityAttributeModifier(this.id, this.baseValue * (double)(amplifier + 1), this.operation);
        }
    }

    protected void removeAndApplyAttributes(LivingEntity entity, int amplifier, boolean conditional){
        if(conditional){
            for (Map.Entry<RegistryEntry<EntityAttribute>, EffectAttributeModifierCreator> entry : this.attributeModifiersExtra.entrySet()) {
                EntityAttributeInstance entityAttributeInstance = entity.getAttributes().getCustomInstance(entry.getKey());
                if (entityAttributeInstance != null) {
                    entityAttributeInstance.removeModifier(entry.getValue().id());
                    entityAttributeInstance.addPersistentModifier(entry.getValue().createAttributeModifier(amplifier));
                }
            }
        } else {
            for (Map.Entry<RegistryEntry<EntityAttribute>, EffectAttributeModifierCreator> entry : this.attributeModifiersExtra.entrySet()) {
                EntityAttributeInstance entityAttributeInstance = entity.getAttributes().getCustomInstance(entry.getKey());
                if (entityAttributeInstance != null) {
                    entityAttributeInstance.removeModifier(entry.getValue().id());
                }
            }
        }
    }
}
