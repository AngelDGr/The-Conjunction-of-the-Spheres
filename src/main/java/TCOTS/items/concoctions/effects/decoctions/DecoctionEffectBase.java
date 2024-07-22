package TCOTS.items.concoctions.effects.decoctions;

import TCOTS.items.concoctions.effects.WitcherPotionEffect;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.player.PlayerEntity;

public class DecoctionEffectBase extends WitcherPotionEffect {

    private LivingEntity entity;
    private final int decoctionToxicity;
    public DecoctionEffectBase(StatusEffectCategory category, int color, int decoctionToxicity) {
        super(category, color);
        this.decoctionToxicity=decoctionToxicity;
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
                player.theConjunctionOfTheSpheres$decreaseToxicity(this.decoctionToxicity,true);
            }
        }
        super.onRemoved(attributeContainer);
    }

}
