package mors.tcots.mixin.northern_wind;

import net.minecraft.world.entity.Mob;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Mob.class)
public class MobMixin {
    @Unique
    Mob tcots$THIS = (Mob) (Object) this;

    @Inject(method = "playAmbientSound", at = @At("HEAD"), cancellable = true)
    private void tcots$stopSound(final CallbackInfo ci){
        if(tcots$THIS.tcots$isFrozen()){
            ci.cancel();
        }
    }
}
