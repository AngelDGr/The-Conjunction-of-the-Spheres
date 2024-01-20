package TCOTS.mixin;

import TCOTS.potions.WitcherPotions_Base;
import TCOTS.potions.WitcherPotions_CustomBrewingRecipe;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.screen.BrewingStandScreenHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BrewingStandScreenHandler.IngredientSlot.class)
public class BrewingStandIngredientSlotMixin {


//    private static void registerWitcherPotionRecipe(Item input, Item item, Item output) {
//        WITCHER_RECIPES_INPUT_ITEMS.add(new WitcherPotions_CustomBrewingRecipe<Item>(input, Ingredient.ofItems(new ItemConvertible[]{item}), output));
//    }


//    @Inject(method = "matches", at = @At("TAIL"))
//    private static void matches(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
//
//        if(stack.getItem().getClass() == WitcherPotions_Base.class)
//        {
//            cir.setReturnValue(true);
//        }
//    }
}
