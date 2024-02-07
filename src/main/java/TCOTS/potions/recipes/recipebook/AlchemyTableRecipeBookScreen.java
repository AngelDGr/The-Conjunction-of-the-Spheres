package TCOTS.potions.recipes.recipebook;

import net.minecraft.client.gui.screen.recipebook.RecipeBookWidget;
import net.minecraft.util.Identifier;

public class AlchemyTableRecipeBookScreen extends RecipeBookWidget {

    protected static final Identifier TEXTURE = new Identifier("textures/gui/recipe_book.png");
    @Override
    protected void setBookButtonTexture() {
        this.toggleCraftableButton.setTextureUV(152, 182, 28, 18, TEXTURE);
    }


}
