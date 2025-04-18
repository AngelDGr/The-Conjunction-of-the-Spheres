package TCOTS.mixin.northern_wind;

import net.minecraft.client.model.BlazeModel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlazeModel.class)
public class BlazeModelMixin {

//    @Inject(method = "setupAnim", at = @At("HEAD"), cancellable = true)
//    private <T extends Entity>  void stopAnim(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci){
//        if(entity instanceof LivingEntity living && living.theConjunctionOfTheSpheres$isFrozen()){
//            ci.cancel();
//        }
//    }
}
