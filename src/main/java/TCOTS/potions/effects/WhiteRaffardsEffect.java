package TCOTS.potions.effects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

public class WhiteRaffardsEffect extends StatusEffect {

//             White Raffard's Decoction = White Raffard's I   = 0.35 x 20 =  7 health =  3.5  hearts
//    Enhanced White Raffard's Decoction = White Raffard's II  = 0.6  x 20 = 12 health =  6    hearts
//    Superior White Raffard's Decoction = White Raffard's III = 1.0  x 20 = XX health =  10   hearts
    public WhiteRaffardsEffect(StatusEffectCategory category, int color) {
        super(category, color);
    }

    boolean invulnerabilityActivated;

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier){
        if(!entity.isUndead()) {
            if(amplifier==0){
                entity.heal(0.35f * entity.getMaxHealth());
            } else if (amplifier==1) {
                entity.heal(0.6f * entity.getMaxHealth());
            }
            else {
                entity.heal(entity.getMaxHealth());
//                if(amplifier>=3){
//                    entity.setInvulnerable(true);
//                    invulnerabilityActivated = true;
//                }
            }
        }
        super.applyUpdateEffect(entity, amplifier);
    }

//    @Override
//    public void onRemoved(LivingEntity entity, AttributeContainer attributes, int amplifier) {
//        if(entity.isInvulnerable() && invulnerabilityActivated){
//            entity.setInvulnerable(false);
//        }
//        super.onRemoved(entity,attributes,amplifier);
//    }

    @Override
    public boolean isInstant() {
        return true;
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return duration >= 1;
    }
}
