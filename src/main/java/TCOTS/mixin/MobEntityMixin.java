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
    private boolean northernWindApplied=false;

    @Unique
    double x=-1;
    @Unique
    double z=-1;

    @Inject(method = "tick", at = @At("TAIL"))
    private void injectNorthernWindFreeze(CallbackInfo ci){
        MobEntity THIS = (MobEntity)(Object)this;
        if(THIS.hasStatusEffect(TCOTS_Effects.NORTHERN_WIND_EFFECT)){

            if(x==-1) {
                x = THIS.getX();
                z = THIS.getZ();
            }

            THIS.teleport(x, THIS.getY(), z);
            THIS.setVelocity(0, 0, 0);

            if(!(THIS instanceof EnderDragonEntity)){
                THIS.addVelocity(0,-0.5,0);
            }
            northernWindApplied=true;
        } else if (northernWindApplied) {
            x=-1;
            z=-1;
            northernWindApplied=false;
        }
    }

    @Inject(method = "tickNewAi", at = @At("HEAD"), cancellable = true)
    private void injectNorthernWindMove(CallbackInfo ci){
        MobEntity THIS = (MobEntity)(Object)this;
        if(THIS.hasStatusEffect(TCOTS_Effects.NORTHERN_WIND_EFFECT)){
            ci.cancel();
        }
    }
}
