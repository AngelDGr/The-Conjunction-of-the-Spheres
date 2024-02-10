package TCOTS.potions.recipes.recipebook.widget;

import TCOTS.items.TCOTS_Items;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.recipebook.RecipeBookWidget;
import net.minecraft.client.gui.screen.recipebook.RecipeResultCollection;
import net.minecraft.client.gui.widget.ToggleButtonWidget;
import net.minecraft.client.recipebook.ClientRecipeBook;
import net.minecraft.client.recipebook.RecipeBookGroup;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.Recipe;
import net.minecraft.screen.AbstractRecipeScreenHandler;

import java.util.ArrayList;
import java.util.List;

public class AlchemyRecipeGroupButton extends ToggleButtonWidget {

    private static final float field_32412 = 15.0f;


    ItemStack icon;
    private float bounce;

    public AlchemyRecipeGroupButton(ItemStack icon) {
        super(0, 0, 39, 27, false);

        this.icon=icon;

        this.setTextureUV(153, 2, 39, 0, AlchemyRecipeBookWidget.RECIPE_GUI_TEXTURE);

    }

    public void checkForNewRecipes(MinecraftClient client) {
        ClientRecipeBook clientRecipeBook = client.player.getRecipeBook();
//        List<RecipeResultCollection> list = clientRecipeBook.getResultsForGroup(this.category);
//        if (!(client.player.currentScreenHandler instanceof AbstractRecipeScreenHandler)) {
//            return;
//        }
//        for (RecipeResultCollection recipeResultCollection : list) {
//            for (Recipe<?> recipe : recipeResultCollection.getResults(clientRecipeBook.isFilteringCraftable((AbstractRecipeScreenHandler)client.player.currentScreenHandler))) {
//                if (!clientRecipeBook.shouldDisplay(recipe)) continue;
//                this.bounce = 15.0f;
//                return;
//            }
//        }
    }

    @Override
    public void renderButton(DrawContext context, int mouseX, int mouseY, float delta) {
        if (this.bounce > 0.0f) {
            float f = 1.0f + 0.1f * (float)Math.sin(this.bounce / 15.0f * (float)Math.PI);
            context.getMatrices().push();
            context.getMatrices().translate(this.getX() + 8, this.getY() + 12, 0.0f);
            context.getMatrices().scale(1.0f, f, 1.0f);
            context.getMatrices().translate(-(this.getX() + 8), -(this.getY() + 12), 0.0f);
        }
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
//        RenderSystem.disableDepthTest();
        int i = this.u;
        int j = this.v;
        if (this.toggled) {
            i += this.pressedUOffset;
        }
        if (this.isSelected()) {
            j += this.hoverVOffset;
        }
        int k = this.getX();
        if (this.toggled) {
            k -= 2;
        }

        context.drawTexture(this.texture, k, this.getY(), i, j, this.width, this.height);

//        RenderSystem.enableDepthTest();
        this.renderIcons(context, minecraftClient.getItemRenderer());
        if (this.bounce > 0.0f) {
            context.getMatrices().pop();
            this.bounce -= delta;
        }
    }

    private void renderIcons(DrawContext context, ItemRenderer itemRenderer) {
        int i;
//        List<ItemStack> list = this.category.getIcons();
        int n = i = this.toggled ? -2 : 0;
//        if (list.size() == 1) {
            context.drawItemWithoutEntity(this.icon, this.getX() + 16 + i, this.getY() + 5);
////        } else if (list.size() == 2) {
//            context.drawItemWithoutEntity(iconslist.get(0), this.getX() + 3 + i, this.getY() + 5);
//            context.drawItemWithoutEntity(iconslist.get(1), this.getX() + 14 + i, this.getY() + 5);
//        }
    }

//    public RecipeBookGroup getCategory() {
//        return this.category;
//    }

//    public boolean hasKnownRecipes(ClientRecipeBook recipeBook) {
//        List<RecipeResultCollection> list = recipeBook.getResultsForGroup(this.category);
//        this.visible = false;
//        if (list != null) {
//            for (RecipeResultCollection recipeResultCollection : list) {
//                if (!recipeResultCollection.isInitialized() || !recipeResultCollection.hasFittingRecipes()) continue;
//                this.visible = true;
//                break;
//            }
//        }
//        return this.visible;
//    }
}
