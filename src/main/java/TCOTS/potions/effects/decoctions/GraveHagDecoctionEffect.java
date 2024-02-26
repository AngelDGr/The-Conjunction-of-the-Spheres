package TCOTS.potions.effects.decoctions;

import TCOTS.mixin.LivingEntityMixin;
import TCOTS.sounds.TCOTS_Sounds;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;

public class GraveHagDecoctionEffect extends StatusEffect {

//             0.005hp/tick     extra for each kill

//             0.1hp/s          extra for each kill


    public GraveHagDecoctionEffect(StatusEffectCategory category, int color) {
        super(category, color);
    }

    private int killCounterIn=0;

    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
        //When killed a mob
        if((killCounterIn != entity.theConjunctionOfTheSpheres$getKillCount()) && killCounterIn < 20){
            killCounterIn = entity.theConjunctionOfTheSpheres$getKillCount();
        }

        if(entity.theConjunctionOfTheSpheres$getKillCountdown()==0){
            killCounterIn=0;
        }

//        System.out.println("KillCounterO: " + entity.theConjunctionOfTheSpheres$getKillCount());
//        System.out.println("KillCounter: "  + killCounterIn);

        if(killCounterIn > 0){
            if(entity.getHealth() < entity.getMaxHealth()){
                //0.005 for tick so with 20kills it's 2 health (1 heart)/second
                entity.heal(0.005f*killCounterIn);
            }
        }

        super.applyUpdateEffect(entity, amplifier);
    }

    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier){
        return true;
    }

    @Override
    public void onRemoved(LivingEntity entity, AttributeContainer attributes, int amplifier) {
        killCounterIn=0;

        super.onRemoved(entity, attributes, amplifier);
    }

    @Override
    public Text getName() {
        return Text.translatable("effect.tcots-witcher.gui.grave_hag_decoction", this.killCounterIn);
    }
}
