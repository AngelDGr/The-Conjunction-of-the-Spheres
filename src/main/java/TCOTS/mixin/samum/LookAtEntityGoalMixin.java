package TCOTS.mixin.samum;

import TCOTS.items.concoctions.bombs.SamumBomb;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.mob.MobEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LookAtEntityGoal.class)
public class LookAtEntityGoalMixin {
    @Shadow
    @Final
    protected MobEntity mob;
    @Inject(method = "shouldContinue", at = @At("HEAD"), cancellable = true)
    private void injectNoSamumEffect(CallbackInfoReturnable<Boolean> cir){
        if(SamumBomb.checkSamumEffect(mob)){
            cir.setReturnValue(false);
        }
    }
}
