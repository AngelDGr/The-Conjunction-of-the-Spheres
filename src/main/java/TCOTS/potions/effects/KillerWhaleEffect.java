package TCOTS.potions.effects;

import com.google.common.collect.Maps;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.*;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

import java.util.Map;
import java.util.UUID;


public class KillerWhaleEffect extends StatusEffect {
    //xTODO: Add attack power to the player
    //xTODO???: Add improved vision underwater (like with water breathing)


    //Each level increase strength by 4
    //Works like Respiration II
    protected final double modifier;


    public KillerWhaleEffect(StatusEffectCategory category, int color, double modifier) {
        super(category, color);
        this.modifier = modifier;
    }

    EntityAttributeInstance entityAttributeInstance;
    EntityAttributeModifier entityAttributeModifierInfo;
    //It's called every tick
    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {

        if(entityAttributeModifierInfo==null){
            this.addAttributeAttackModifier(amplifier);
        }

        if(entity.isTouchingWater()){
            if (entityAttributeInstance != null && !entityAttributeInstance.hasModifier(entityAttributeModifierInfo)) {
                entityAttributeInstance.addPersistentModifier(entityAttributeModifierInfo);
            }
        }else{
            if(entityAttributeInstance != null && entityAttributeInstance.hasModifier(entityAttributeModifierInfo)){
                entityAttributeInstance.removeModifier(entityAttributeModifierInfo);
            }
        }

        super.applyUpdateEffect(entity, amplifier);
    }

    //Called when applied
    @Override
    public void onApplied(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        entityAttributeInstance = entity.getAttributes().getCustomInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE);
        super.onApplied(entity,attributes,amplifier);
    }

    //Called when removed
    @Override
    public void onRemoved(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        if(entityAttributeInstance.hasModifier(entityAttributeModifierInfo)){
            entityAttributeInstance.removeModifier(entityAttributeModifierInfo);
        }
        super.onRemoved(entity,attributes,amplifier);
    }


    public void addAttributeAttackModifier(int amplifier) {
        entityAttributeModifierInfo = new EntityAttributeModifier(
                UUID.fromString("648D7064-6A60-4F59-8ABE-C2C23A6DD7A9"),
                "killer_whale_effect",
                4*(amplifier+1),
                EntityAttributeModifier.Operation.ADDITION);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier){
        return true;
    }

    @Override
    public double adjustModifierAmount(int amplifier, EntityAttributeModifier modifier) {
        return this.modifier * (double)(amplifier + 1);
    }
}
