package mors.tcots.mixin.samum;


import mors.tcots.items.concoctions.bombs.SamumBomb;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RandomLookAroundGoal.class)
public class LookAroundGoalMixin {
    @Shadow @Final private Mob mob;

    @Inject(method = "canUse", at = @At("HEAD"), cancellable = true)
    private void tcots$injectNoSamumEffect(final CallbackInfoReturnable<Boolean> cir){
        if(SamumBomb.checkSamumEffect(mob)){
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "canContinueToUse", at = @At("HEAD"), cancellable = true)
    private void tcots$injectNoSamumEffectContinue(final CallbackInfoReturnable<Boolean> cir){
        if(SamumBomb.checkSamumEffect(mob)){
            cir.setReturnValue(false);
        }
    }
}
