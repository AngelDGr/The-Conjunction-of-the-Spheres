package TCOTS.potions.effects.decoctions;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.player.PlayerEntity;

public class DecoctionEffectBase extends StatusEffect {

    private LivingEntity entity;
    public DecoctionEffectBase(StatusEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public void onApplied(LivingEntity entity, int amplifier) {
        this.entity=entity;
    }

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        if (this.entity == null) {
            this.entity = entity;
        }
        super.applyUpdateEffect(entity, amplifier);
    }

    @Override
    public void onRemoved(AttributeContainer attributeContainer) {
        if(!entity.getWorld().isClient){
            if(entity!=null && entity instanceof PlayerEntity player){
                player.theConjunctionOfTheSpheres$decreaseToxicity(50,true);
            }
        }
        super.onRemoved(attributeContainer);
    }
}
