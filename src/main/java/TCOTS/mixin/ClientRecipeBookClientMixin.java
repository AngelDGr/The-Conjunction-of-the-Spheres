package TCOTS.mixin;

import TCOTS.potions.recipes.AlchemyTableRecipe;
import net.minecraft.client.recipebook.ClientRecipeBook;
import net.minecraft.client.recipebook.RecipeBookGroup;
import net.minecraft.recipe.Recipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientRecipeBook.class)
public class ClientRecipeBookClientMixin {

    @Inject(method = "getGroupForRecipe", at = @At("HEAD"), cancellable = true)
    private static void fixWarning(Recipe<?> recipe, CallbackInfoReturnable<RecipeBookGroup> cir){

        if (recipe instanceof AlchemyTableRecipe) {
//            LOGGER.info("alchemy recipe: {}/{}", LogUtils.defer(() -> Registries.RECIPE_TYPE.getId(recipe.getType())), LogUtils.defer(recipe::getId));
            cir.setReturnValue(RecipeBookGroup.UNKNOWN);
        }

    }
}
