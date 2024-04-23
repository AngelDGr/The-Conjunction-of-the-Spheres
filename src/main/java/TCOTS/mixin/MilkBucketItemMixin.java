package TCOTS.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MilkBucketItem;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MilkBucketItem.class)
public class MilkBucketItemMixin {
    @Inject(method = "finishUsing", at = @At("TAIL"))
    private void injectInTickDecreaseToxicity(ItemStack stack, World world, LivingEntity user, CallbackInfoReturnable<ItemStack> cir) {
        if(!world.isClient){
            if(user instanceof PlayerEntity player){
                player.theConjunctionOfTheSpheres$decreaseToxicity(player.theConjunctionOfTheSpheres$getToxicity(),false);
                player.theConjunctionOfTheSpheres$decreaseToxicity(player.theConjunctionOfTheSpheres$getDecoctionToxicity(),true);
            }
        }
    }
}
