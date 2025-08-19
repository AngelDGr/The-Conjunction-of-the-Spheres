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
    public WitcherPotionEffect(final MobEffectCategory category, final int color) {
        super(category, color);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(final int duration, final int amplifier){
        return true;
    }

    public boolean hasCustomApplyTooltip(){
        return false;
    }

    public boolean hasSpecialAttributes(){
        return false;
    }

    public int getSpecialAttributesValue(final int amplifier){
        return 0;
    }

    public boolean hasExtraInfo(){
        return false;
    }

    public boolean hasExtraLine(final int amplifier){
        return false;
    }

    protected final Map<Holder<Attribute>, EffectAttributeModifierCreator> attributeModifiersExtra = new Object2ObjectOpenHashMap<>();

    @Override
    public @NotNull MobEffect addAttributeModifier(@NotNull final Holder<Attribute> attribute, @NotNull final ResourceLocation id, final double amount, final AttributeModifier.@NotNull Operation operation) {
        this.attributeModifiersExtra.put(attribute, new EffectAttributeModifierCreator(id, amount, operation));
        return super.addAttributeModifier(attribute, id, amount, operation);
    }

    protected record EffectAttributeModifierCreator(ResourceLocation id, double baseValue, AttributeModifier.Operation operation) {
        public AttributeModifier createAttributeModifier(final int amplifier) {
            return new AttributeModifier(this.id, this.baseValue * (double)(amplifier + 1), this.operation);
        }
    }

    protected void removeAndApplyAttributes(final LivingEntity entity, final int amplifier, final boolean conditional){
        if(conditional){
            for (final Map.Entry<Holder<Attribute>, EffectAttributeModifierCreator> entry : this.attributeModifiersExtra.entrySet()) {
                final AttributeInstance entityAttributeInstance = entity.getAttributes().getInstance(entry.getKey());
                if (entityAttributeInstance != null) {
                    entityAttributeInstance.removeModifier(entry.getValue().id());
                    entityAttributeInstance.addPermanentModifier(entry.getValue().createAttributeModifier(amplifier));
                }
            }
        } else {
            for (final Map.Entry<Holder<Attribute>, EffectAttributeModifierCreator> entry : this.attributeModifiersExtra.entrySet()) {
                final AttributeInstance entityAttributeInstance = entity.getAttributes().getInstance(entry.getKey());
                if (entityAttributeInstance != null) {
                    entityAttributeInstance.removeModifier(entry.getValue().id());
                }
            }
        }
    }
}
