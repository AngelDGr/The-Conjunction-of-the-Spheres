package TCOTS.mixin.samum;

import TCOTS.items.concoctions.bombs.SamumBomb;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LookAtPlayerGoal.class)
public class LookAtEntityGoalMixin {
    @Shadow
    @Final
    protected Mob mob;
    @Inject(method = "canContinueToUse", at = @At("HEAD"), cancellable = true)
    private void injectNoSamumEffect(final CallbackInfoReturnable<Boolean> cir){
        if(SamumBomb.checkSamumEffect(mob)){
            cir.setReturnValue(false);
        }
    }
}
