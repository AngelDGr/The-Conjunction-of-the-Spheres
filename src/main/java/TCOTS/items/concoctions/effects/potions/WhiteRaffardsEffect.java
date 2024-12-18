package TCOTS.items.concoctions.effects.potions;

import TCOTS.items.concoctions.effects.WitcherPotionEffect;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.tag.EntityTypeTags;

public class WhiteRaffardsEffect extends WitcherPotionEffect {

//             White Raffard's Decoction = White Raffard's I   = 0.35 x 20 =  7 health =  3.5  hearts
//    Enhanced White Raffard's Decoction = White Raffard's II  = 0.6  x 20 = 12 health =  6    hearts
//    Superior White Raffard's Decoction = White Raffard's III = 1.0  x 20 = XX health =  10   hearts
    public WhiteRaffardsEffect(StatusEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public boolean applyUpdateEffect(LivingEntity entity, int amplifier){
        if(!entity.getType().isIn(EntityTypeTags.UNDEAD)) {
            if(amplifier==0){
                entity.heal(0.35f * entity.getMaxHealth());
            } else if (amplifier==1) {
                entity.heal(0.6f * entity.getMaxHealth());
            }
            else {
                entity.heal(entity.getMaxHealth());
            }
        }

        return super.applyUpdateEffect(entity, amplifier);
    }

    @Override
    public boolean isInstant() {
        return true;
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return duration >= 1;
    }

    @Override
    public boolean hasSpecialAttributes() {
        return true;
    }

    @Override
    public int getSpecialAttributesValue(int amplifier) {
        return amplifier == 0? 35: amplifier == 1? 60 : 100;
    }
}
