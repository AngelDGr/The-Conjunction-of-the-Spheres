package TCOTS.effects;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.util.Map;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import org.jetbrains.annotations.NotNull;

public class WitcherPotionEffect extends MobEffect {
    public WitcherPotionEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier){
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

    protected final Map<Holder<Attribute>, EffectAttributeModifierCreator> attributeModifiersExtra = new Object2ObjectOpenHashMap<>();

    @Override
    public @NotNull MobEffect addAttributeModifier(@NotNull Holder<Attribute> attribute, @NotNull ResourceLocation id, double amount, AttributeModifier.@NotNull Operation operation) {
        this.attributeModifiersExtra.put(attribute, new EffectAttributeModifierCreator(id, amount, operation));
        return super.addAttributeModifier(attribute, id, amount, operation);
    }

    protected record EffectAttributeModifierCreator(ResourceLocation id, double baseValue, AttributeModifier.Operation operation) {
        public AttributeModifier createAttributeModifier(int amplifier) {
            return new AttributeModifier(this.id, this.baseValue * (double)(amplifier + 1), this.operation);
        }
    }

    protected void removeAndApplyAttributes(LivingEntity entity, int amplifier, boolean conditional){
        if(conditional){
            for (Map.Entry<Holder<Attribute>, EffectAttributeModifierCreator> entry : this.attributeModifiersExtra.entrySet()) {
                AttributeInstance entityAttributeInstance = entity.getAttributes().getInstance(entry.getKey());
                if (entityAttributeInstance != null) {
                    entityAttributeInstance.removeModifier(entry.getValue().id());
                    entityAttributeInstance.addPermanentModifier(entry.getValue().createAttributeModifier(amplifier));
                }
            }
        } else {
            for (Map.Entry<Holder<Attribute>, EffectAttributeModifierCreator> entry : this.attributeModifiersExtra.entrySet()) {
                AttributeInstance entityAttributeInstance = entity.getAttributes().getInstance(entry.getKey());
                if (entityAttributeInstance != null) {
                    entityAttributeInstance.removeModifier(entry.getValue().id());
                }
            }
        }
    }
}
