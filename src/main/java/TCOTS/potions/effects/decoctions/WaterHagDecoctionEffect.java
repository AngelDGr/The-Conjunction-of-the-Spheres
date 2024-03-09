package TCOTS.potions.effects.decoctions;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

import java.util.UUID;

public class WaterHagDecoctionEffect extends StatusEffect {

    protected final double modifier;

    private final EntityAttributeModifier entityAttributeModifier;
    public WaterHagDecoctionEffect(StatusEffectCategory category, int color, double modifier) {
        super(category, color);
        this.modifier = modifier;
        entityAttributeModifier = new EntityAttributeModifier(
                UUID.fromString("648D7064-6A60-4F59-8ABE-C2C23A6DD7A9"),
                "water_hag_decoction",
                6,
                EntityAttributeModifier.Operation.ADDITION);
    }

    EntityAttributeInstance entityAttributeInstance;

    //It's called every tick
    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {

        if(entityAttributeInstance == null){
            entityAttributeInstance = entity.getAttributes().getCustomInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE);
        }

        if(entity.getHealth() == entity.getMaxHealth()){
            if (!entityAttributeInstance.hasModifier(entityAttributeModifier)) {
                entityAttributeInstance.addPersistentModifier(entityAttributeModifier);
            }
        } else if(entityAttributeInstance.hasModifier(entityAttributeModifier)) {
                entityAttributeInstance.removeModifier(entityAttributeModifier);
            }

        super.applyUpdateEffect(entity, amplifier);
    }

    //Called when applied
    @Override
    public void onApplied(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        entityAttributeInstance = entity.getAttributes().getCustomInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE);
        assert entityAttributeInstance != null;
        entityAttributeInstance.removeModifier(entityAttributeModifier);

        super.onApplied(entity,attributes,amplifier);
    }

    //Called when removed
    @Override
    public void onRemoved(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        if(entityAttributeInstance.hasModifier(entityAttributeModifier)){
            entityAttributeInstance.removeModifier(entityAttributeModifier);
        }
        super.onRemoved(entity,attributes,amplifier);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier){
        return true;
    }

}
