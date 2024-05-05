package TCOTS.mixin;

import TCOTS.items.potions.TCOTS_Effects;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.mob.MobEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MobEntity.class)
public class MobEntityMixin {
    @Shadow private @Nullable LivingEntity target;

    //Samum
    @Inject(method = "setTarget", at = @At("HEAD"), cancellable = true)
    private void injectNoSamumEffectSet(LivingEntity target, CallbackInfo ci){
        MobEntity THIS = (MobEntity)(Object)this;
        if(THIS.hasStatusEffect(TCOTS_Effects.SAMUM_EFFECT)){
            this.target=null;
            ci.cancel();
        }
    }

    //NorthernWind
    @Unique
    private boolean northernWindApplied;
    @Inject(method = "tick", at = @At("TAIL"))
    private void injectNorthernWindFreeze(CallbackInfo ci){
        MobEntity THIS = (MobEntity)(Object)this;
        if(THIS.hasStatusEffect(TCOTS_Effects.NORTHERN_WIND_EFFECT)){
            if(!(THIS instanceof EnderDragonEntity)){
                THIS.addVelocity(0,-1,0);
            }
            northernWindApplied=true;
        } else if (northernWindApplied) {
//            THIS.setAiDisabled(false);
            northernWindApplied=false;
        }
    }
}
