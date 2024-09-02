package TCOTS.mixin;

import TCOTS.entity.TCOTS_Entities;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.passive.IronGolemEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(IronGolemEntity.class)
public class IronGolemMixin {

    @Inject(method = "canTarget", at = @At("HEAD"), cancellable = true)
    private void injectNotAttackTrolls(EntityType<?> type, CallbackInfoReturnable<Boolean> cir){
        if (type == TCOTS_Entities.ROCK_TROLL) {
            cir.setReturnValue(false);
        }
    }
}
