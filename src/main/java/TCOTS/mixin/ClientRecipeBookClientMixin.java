package TCOTS.mixin;

import TCOTS.items.potions.recipes.AlchemyTableRecipe;
import net.minecraft.client.recipebook.ClientRecipeBook;
import net.minecraft.client.recipebook.RecipeBookGroup;
import net.minecraft.recipe.RecipeEntry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientRecipeBook.class)
public class ClientRecipeBookClientMixin {
    @Inject(method = "getGroupForRecipe", at = @At("HEAD"), cancellable = true)
    private static void fixWarning(RecipeEntry<?> recipe, CallbackInfoReturnable<RecipeBookGroup> cir){
        if (recipe.value() instanceof AlchemyTableRecipe) {
            cir.setReturnValue(RecipeBookGroup.UNKNOWN);
        }
    }
}
