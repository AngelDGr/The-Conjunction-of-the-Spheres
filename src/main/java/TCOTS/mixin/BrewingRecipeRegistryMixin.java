package TCOTS.mixin;

import net.minecraft.recipe.BrewingRecipeRegistry;
import org.spongepowered.asm.mixin.Mixin;


@Mixin(BrewingRecipeRegistry.class)
public class BrewingRecipeRegistryMixin{
//
//    @Inject(method = "hasRecipe", at = @At("RETURN"), cancellable = true)
//    private static void hasRecipe(ItemStack input, ItemStack ingredient, CallbackInfoReturnable<Boolean> cir) {
//////        if (!POTION_TYPE_PREDICATE.test(input)) {
////
//        if (WitcherPotions_Recipes.POTION_TYPE_PREDICATE.test(input)) {
//
//            cir.setReturnValue(hasItemO(input,ingredient) || hasPotionO(input,ingredient));
//
//        }
////
//////            return false;
//////        } else {
////
////        //            return hasItemRecipe(input, ingredient) || hasPotionRecipe(input, ingredient);
////
//////        }
//    }
//
//    @Inject(method = "isValidIngredient", at = @At("TAIL"), cancellable = true)
//    private static void isValidIngredient(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
//        int i = 0;
//        for(int j = WitcherPotions_Recipes.WITCHER_RECIPES_INPUT_POTIONS.size(); i < j; ++i) {
//            if ((WitcherPotions_Recipes.WITCHER_RECIPES_INPUT_POTIONS.get(i)).ingredient.test(stack)) {
//                cir.setReturnValue(true);
//            }
//        }
//
//        i = 0;
//        for(int j = WitcherPotions_Recipes.WITCHER_RECIPES_INPUT_ITEMS.size(); i < j; ++i) {
//            if ((WitcherPotions_Recipes.WITCHER_RECIPES_INPUT_ITEMS.get(i)).ingredient.test(stack)) {
//                cir.setReturnValue(true);
//            }
//        }
//    }
//    @Inject(method = "craft", at = @At("TAIL"), cancellable = true)
//    private static void craft(ItemStack ingredient, ItemStack input, CallbackInfoReturnable<ItemStack> cir) {
//        if (!input.isEmpty()) {
//            //Put the recipe value
//            WitcherPotions_CustomBrewingRecipe recipe;
//
//            //Get the item value of the input
//            Item item = input.getItem();
//            //Get the potion value of the input
//            Potion potion = PotionUtil.getPotion(input);
//
//            //Counters
//            int i = 0;
//            int j;
//
//            //Check if the Input it's a potion
//            for(j = WitcherPotions_Recipes.WITCHER_RECIPES_INPUT_POTIONS.size(); i < j; ++i) {
//                recipe = (WitcherPotions_CustomBrewingRecipe<Potion>) WitcherPotions_Recipes.WITCHER_RECIPES_INPUT_POTIONS.get(i);
//                if (recipe.input == potion && recipe.ingredient.test(ingredient)) {
//                    cir.setReturnValue(recipe.output.getDefaultStack());
//                }
//            }
//
//            //Reset the i counter
//            i = 0;
//
//            //Check if the Input it's an item
//            for(j = WitcherPotions_Recipes.WITCHER_RECIPES_INPUT_ITEMS.size(); i < j; ++i) {
//                recipe = (WitcherPotions_CustomBrewingRecipe<Item>) WitcherPotions_Recipes.WITCHER_RECIPES_INPUT_ITEMS.get(i);
//                if (recipe.input == item && recipe.ingredient.test(ingredient)) {
//                    cir.setReturnValue(recipe.output.getDefaultStack());
//                }
//            }
//
//        }
//    }
//
//    @Inject(method = "hasItemRecipe", at = @At("TAIL"), cancellable = true)
//    private static void hasItemRecipe(ItemStack input, ItemStack ingredient, CallbackInfoReturnable<Boolean> cir) {
//        Item item = input.getItem();
//        int i = 0;
//
//        for(int j = WitcherPotions_Recipes.WITCHER_RECIPES_INPUT_ITEMS.size(); i < j; ++i) {
//            WitcherPotions_CustomBrewingRecipe<Item> recipe = (WitcherPotions_CustomBrewingRecipe<Item>) WitcherPotions_Recipes.WITCHER_RECIPES_INPUT_ITEMS.get(i);
//            if (recipe.input == item && recipe.ingredient.test(ingredient)) {
//                cir.setReturnValue(true);
//            }
//        }
//    }
//    @Inject(method = "hasPotionRecipe", at = @At("TAIL"), cancellable = true)
//    private static void hasPotionRecipe(ItemStack input, ItemStack ingredient, CallbackInfoReturnable<Boolean> cir) {
//        Potion potion = PotionUtil.getPotion(input);
//        int i = 0;
//
//        for(int j = WitcherPotions_Recipes.WITCHER_RECIPES_INPUT_POTIONS.size(); i < j; ++i) {
//            WitcherPotions_CustomBrewingRecipe<Potion> recipe = (WitcherPotions_CustomBrewingRecipe<Potion>) WitcherPotions_Recipes.WITCHER_RECIPES_INPUT_POTIONS.get(i);
//            if (recipe.input == potion && recipe.ingredient.test(ingredient)) {
//                cir.setReturnValue(true);
//            }
//        }
//    }
//
//    @Unique
//    private static boolean hasPotionO(ItemStack input, ItemStack ingredient){
//        Potion potion = PotionUtil.getPotion(input);
//        int i = 0;
//        for(int j = WitcherPotions_Recipes.WITCHER_RECIPES_INPUT_POTIONS.size(); i < j; ++i) {
//            WitcherPotions_CustomBrewingRecipe<Potion> recipe = (WitcherPotions_CustomBrewingRecipe<Potion>) WitcherPotions_Recipes.WITCHER_RECIPES_INPUT_POTIONS.get(i);
//            if (recipe.input == potion && recipe.ingredient.test(ingredient)) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    @Unique
//    private static boolean hasItemO(ItemStack input, ItemStack ingredient){
//        Item item = input.getItem();
//        int i = 0;
//
//        for(int j = WitcherPotions_Recipes.WITCHER_RECIPES_INPUT_ITEMS.size(); i < j; ++i) {
//            WitcherPotions_CustomBrewingRecipe<Item> recipe = (WitcherPotions_CustomBrewingRecipe<Item>) WitcherPotions_Recipes.WITCHER_RECIPES_INPUT_ITEMS.get(i);
//            if (recipe.input == item && recipe.ingredient.test(ingredient)) {
//                return true;
//            }
//        }
//
//        return false;
//    }

}