package mors.tcots.effects.decoctions;

import net.minecraft.network.chat.Component;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import org.jetbrains.annotations.NotNull;

public class GraveHagDecoctionEffect extends DecoctionEffectBase {

//             0.005hp/tick     extra for each kill

//             0.1hp/s          extra for each kill


    public GraveHagDecoctionEffect(final MobEffectCategory category, final int color) {
        super(category, color,50);
    }

    private int killCounterIn=0;

    @Override
    public boolean applyEffectTick(@NotNull final LivingEntity entity, final int amplifier) {

        //When killed a mob
        if((killCounterIn != entity.tcots$getKillCount()) && killCounterIn < 20){
            killCounterIn = entity.tcots$getKillCount();
        }

        if(entity.tcots$getKillCountdown()==0){
            killCounterIn=0;
        }

        if(killCounterIn > 0){
            if(entity.getHealth() < entity.getMaxHealth()){
                //0.005 for tick, so with 20kills it's 2 health (1 heart)/second
                entity.heal(0.005f*killCounterIn);
            }
        }

        return super.applyEffectTick(entity, amplifier);
    }

    @Override
    public void removeAttributeModifiers(@NotNull final AttributeMap attributes) {
        killCounterIn=0;
        super.removeAttributeModifiers(attributes);
    }

    @Override
    public @NotNull Component getDisplayName() {

        return Component.translatable(this.getDescriptionId()+".gui", this.killCounterIn);
    }
}
