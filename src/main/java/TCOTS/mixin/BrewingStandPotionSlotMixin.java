package TCOTS.mixin;


import TCOTS.potions.DwarvenSpiritItem;
import TCOTS.potions.WitcherPotionsSplash_Base;
import TCOTS.potions.WitcherPotions_Base;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.BrewingStandScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BrewingStandScreenHandler.PotionSlot.class)
public class BrewingStandPotionSlotMixin {
//    @Inject(method = "matches", at = @At("TAIL"), cancellable = true)
//    private static void matches(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
//
//        if((stack.getItem().getClass() == WitcherPotions_Base.class || stack.getItem().getClass() == WitcherPotionsSplash_Base.class
//                || stack.getItem().getClass() == DwarvenSpiritItem.class)
//        )
//        {
//            cir.setReturnValue(true);
//        }
//    }
//
//    @Inject(method = "canInsert", at = @At("TAIL"))
//    private static void canInsert(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
//
//    }
//

}
