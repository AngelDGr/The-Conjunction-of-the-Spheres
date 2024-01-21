package TCOTS.potions.effects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

import java.util.Iterator;
import java.util.Map;


public class KillerWhaleEffect extends StatusEffect {

    //TODO: Add attack power to the player
    //TODO: Add improved vision underwater (like with water breathing)

    public KillerWhaleEffect(StatusEffectCategory category, int color) {
        super(category, color);
    }


    //It's called every tick
    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier){


//        if (entity.getHealth() < entity.getMaxHealth()) {
//
//            entity.heal(0.05F * (float) (amplifier + 1));
//            if (entity instanceof PlayerEntity) {
//                ((PlayerEntity) entity).addExhaustion(0.1F * (float) (amplifier + 1));
//            }
//        }
//        entity.getMaxAir();

        super.applyUpdateEffect(entity, amplifier);
    }

    @Override
    public void onApplied(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        Iterator var4 = this.getAttributeModifiers().entrySet().iterator();
        while(var4.hasNext()) {
            Map.Entry<EntityAttribute, EntityAttributeModifier> entry = (Map.Entry)var4.next();
            EntityAttributeInstance entityAttributeInstance = attributes.getCustomInstance((EntityAttribute)entry.getKey());
            if (entityAttributeInstance != null) {
                EntityAttributeModifier entityAttributeModifier = (EntityAttributeModifier)entry.getValue();
                entityAttributeInstance.removeModifier(entityAttributeModifier);
                entityAttributeInstance.addPersistentModifier(new EntityAttributeModifier(entityAttributeModifier.getId(), this.getTranslationKey() + " " + amplifier, this.adjustModifierAmount(amplifier, entityAttributeModifier), entityAttributeModifier.getOperation()));
            }
        }

    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier){
        return true;
    }

}
