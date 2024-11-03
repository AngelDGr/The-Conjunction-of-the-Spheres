package TCOTS.mixin;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


//@Debug(export = true)
@Mixin(Item.class)
public class TestingMixin {

    @Inject(method = "use", at = @At("TAIL"))
    private void modifyEyes(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir){
        if(world.isClient){
            return;
        }


        //Up/Down
        if(user.getStackInHand(hand).isOf(Items.STICK)) {
            user.theConjunctionOfTheSpheres$setEyesPivot(user.theConjunctionOfTheSpheres$getEyesPivot()
                    .add(0,user.isSneaking()?-1:1,0));
        }

        //Right/Left
        if(user.getStackInHand(hand).isOf(Items.BLAZE_ROD)) {
            user.theConjunctionOfTheSpheres$setEyesPivot(user.theConjunctionOfTheSpheres$getEyesPivot()
                    .add(user.isSneaking()?-1:1,0,0));
        }

        //Separation
        if(user.getStackInHand(hand).isOf(Items.SPECTRAL_ARROW)) {
            user.theConjunctionOfTheSpheres$setEyeSeparation(
                    user.theConjunctionOfTheSpheres$getEyeSeparation() + (user.isSneaking()?-1:1)
            );
        }

        //Shape
        if(user.getStackInHand(hand).isOf(Items.ARROW)) {
            user.theConjunctionOfTheSpheres$setEyeShape(
                    user.theConjunctionOfTheSpheres$getEyeShape() + (user.isSneaking()?-1:1)
            );
        }

        //Activate/Deactivate
        if(user.getStackInHand(hand).isOf(Items.BONE)) {
            user.theConjunctionOfTheSpheres$setWitcherEyesActivated(
                    !user.theConjunctionOfTheSpheres$getWitcherEyesActivated()
            );
        }
    }

}
