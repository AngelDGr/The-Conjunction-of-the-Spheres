package TCOTS.mixin;

import TCOTS.potions.recipes.AlchemyTableRecipe;
import com.mojang.logging.LogUtils;
import net.minecraft.client.recipebook.ClientRecipeBook;
import net.minecraft.client.recipebook.RecipeBookGroup;
import net.minecraft.recipe.AbstractCookingRecipe;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.recipe.book.CookingRecipeCategory;
import net.minecraft.recipe.book.RecipeBookCategory;
import net.minecraft.registry.Registries;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientRecipeBook.class)
public class ClientRecipeBookClientMixin {
    @Final
    @Shadow
    private static final Logger LOGGER = LogUtils.getLogger();

    @Inject(method = "getGroupForRecipe", at = @At("HEAD"), cancellable = true)
    private static void fixWarning(Recipe<?> recipe, CallbackInfoReturnable<RecipeBookGroup> cir){

        if (recipe instanceof AlchemyTableRecipe) {
//            LOGGER.info("alchemy recipe: {}/{}", LogUtils.defer(() -> Registries.RECIPE_TYPE.getId(recipe.getType())), LogUtils.defer(recipe::getId));
            cir.setReturnValue(RecipeBookGroup.UNKNOWN);
        }

    }
}
