package TCOTS.mixin.samum;


import TCOTS.items.concoctions.bombs.SamumBomb;
import net.minecraft.entity.ai.goal.LookAroundGoal;
import net.minecraft.entity.mob.MobEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LookAroundGoal.class)
public class LookAroundGoalMixin {
    @Shadow @Final private MobEntity mob;

    @Inject(method = "canStart", at = @At("HEAD"), cancellable = true)
    private void injectNoSamumEffect(CallbackInfoReturnable<Boolean> cir){
        if(SamumBomb.checkSamumEffect(mob)){
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "shouldContinue", at = @At("HEAD"), cancellable = true)
    private void injectNoSamumEffectContinue(CallbackInfoReturnable<Boolean> cir){
        if(SamumBomb.checkSamumEffect(mob)){
            cir.setReturnValue(false);
        }
    }
}
