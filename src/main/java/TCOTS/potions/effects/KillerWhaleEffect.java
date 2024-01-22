package TCOTS.potions.effects;

import com.google.common.collect.Maps;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.*;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.client.render.GameRenderer;

import java.util.Map;


public class KillerWhaleEffect extends StatusEffect {
    //xTODO: Add attack power to the player
    //xTODO???: Add improved vision underwater (like with water breathing)


    //Each level increase strength by 4
    //Works like Respiration II
    protected final double modifier;

    private final Map<EntityAttribute, EntityAttributeModifier> attributeModifiers = Maps.newHashMap();


    public KillerWhaleEffect(StatusEffectCategory category, int color, double modifier) {
        super(category, color);
        this.modifier = modifier;
    }

    EntityAttributeInstance entityAttributeInstance;
    EntityAttributeModifier entityAttributeModifierInfo;
    boolean appliedStrength =false;
    //It's called every tick
    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {

        if(entityAttributeModifierInfo==null){
            this.addAttributeAttackModifier(amplifier);
        }

        if(entity.isTouchingWater()){
            if (entityAttributeInstance != null && !appliedStrength) {
                entityAttributeInstance.addPersistentModifier(entityAttributeModifierInfo);
                appliedStrength=true;
            }
        }else{
            if(entityAttributeInstance != null && appliedStrength){
                entityAttributeInstance.removeModifier(entityAttributeModifierInfo);
                appliedStrength=false;
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
        if(appliedStrength){
            entityAttributeInstance.removeModifier(entityAttributeModifierInfo);
            appliedStrength=false;
        }
        super.onRemoved(entity,attributes,amplifier);
    }


    public void addAttributeAttackModifier(int amplifier) {
        entityAttributeModifierInfo = new EntityAttributeModifier(
                "648D7064-6A60-4F59-8ABE-C2C23A6DD7A9",
                4*(amplifier+1),
                EntityAttributeModifier.Operation.ADDITION);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier){
        return true;
    }

    public double adjustModifierAmount(int amplifier, EntityAttributeModifier modifier) {
        return this.modifier * (double)(amplifier + 1);
    }
}
