package mors.tcots.mixin;

import mors.tcots.recipes.AlchemyTableRecipe;
import net.minecraft.client.ClientRecipeBook;
import net.minecraft.client.RecipeBookCategories;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientRecipeBook.class)
public class ClientRecipeBookClientMixin {
    @Inject(method = "getCategory", at = @At("HEAD"), cancellable = true)
    private static void tcots$fixWarning(final RecipeHolder<?> recipe, final CallbackInfoReturnable<RecipeBookCategories> cir){
        if (recipe.value() instanceof AlchemyTableRecipe) {
            cir.setReturnValue(RecipeBookCategories.UNKNOWN);
        }
    }
}
