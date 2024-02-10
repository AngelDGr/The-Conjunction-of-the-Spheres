package TCOTS.potions.recipes.recipebook.widget;

import TCOTS.potions.recipes.AlchemyTableRecipe;
import TCOTS.potions.recipes.recipebook.RecipeAlchemyResultCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.recipebook.*;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.ToggleButtonWidget;
import net.minecraft.client.recipebook.RecipeBookGroup;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.book.RecipeBook;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class AlchemyRecipeBookResults {
    private final List<AlchemyRecipeResultButton> resultButtons = Lists.newArrayListWithCapacity(5);
    private final List<AlchemyTableRecipe> listRecipes = new ArrayList<>();
    private final List<AlchemyTableRecipe> ActiveRecipesList = new ArrayList<>();
    public AlchemyRecipeBookButton recipeBookButton;
    @Nullable
    private AlchemyRecipeResultButton hoveredResultButton;
    private final RecipeAlternativesWidget alternatesWidget = new RecipeAlternativesWidget();

    private List<RecipeResultCollection> resultCollections = ImmutableList.of();
    private MinecraftClient client;
//    private final List<RecipeDisplayListener> recipeDisplayListeners = Lists.newArrayList();
//    private List<RecipeResultCollection> resultCollections = ImmutableList.of();
    private ToggleButtonWidget nextPageButton;
    private final List<RecipeDisplayListener> recipeDisplayListeners = Lists.newArrayList();
    private ToggleButtonWidget prevPageButton;
    private int pageCount;
    public int currentPage;
    private RecipeBook recipeBook;
//    @Nullable
//    private Recipe<?> lastClickedRecipe;
//    @Nullable
//    private RecipeResultCollection resultCollection;


    private int parentI;

    private int parentJ;

    public AlchemyRecipeBookResults() {
        for (int i = 0; i < 5; ++i) {
            this.resultButtons.add(new AlchemyRecipeResultButton());
        }
    }

    public void initialize(MinecraftClient client, int parentI, int parentJ) {
        this.client = client;
        this.recipeBook = client.player.getRecipeBook();
        for (int i = 0; i < this.resultButtons.size(); ++i) {
                this.resultButtons.get(i).setPosition(parentI + 14, (parentJ + 26) + (i * 23 ));
        }
        this.nextPageButton = new ToggleButtonWidget(parentI + 90, parentJ + 139, 12, 17, false);
        this.nextPageButton.setTextureUV(1, 208, 13, 18, AlchemyRecipeBookWidget.RECIPE_GUI_TEXTURE);
        this.prevPageButton = new ToggleButtonWidget(parentI + 47, parentJ + 139, 12, 17, true);
        this.prevPageButton.setTextureUV(1, 208, 13, 18, AlchemyRecipeBookWidget.RECIPE_GUI_TEXTURE);
        this.parentI=parentI;
        this.parentJ=parentJ;

    }

    public void setResults(boolean resetCurrentPage) {
        this.pageCount = (int)Math.ceil((double)listRecipes.size() / 5.0);

        if (this.pageCount <= this.currentPage || resetCurrentPage) {
            this.currentPage = 0;
        }
        this.refreshResultButtons();
    }

    public void sendList(List<AlchemyTableRecipe> listRecipes){
        if(this.listRecipes.isEmpty()) {
            this.listRecipes.addAll(listRecipes);
            for (int i = 0; i < this.listRecipes.size() && i < 5; i++) {
//            this.resultButtons.get(i).receiveRecipe(listRecipes.get(i));
                this.resultButtons.get(i).receiveTextRenderer(client.textRenderer);
            }
        }
    }

    public void draw(DrawContext context, int x, int y, int mouseX, int mouseY, float delta) {
        if (this.pageCount > 1) {
            String string = this.currentPage + 1 + "/" + this.pageCount;
            int i = this.client.textRenderer.getWidth(string);
            context.drawText(this.client.textRenderer, string, x - i / 2 + 73, y + 143, 0xffffff, true);
        }
        this.hoveredResultButton = null;
        for (AlchemyRecipeResultButton animatedResultButton : this.resultButtons) {
            animatedResultButton.render(context, mouseX, mouseY, delta);
            if (!animatedResultButton.visible || !animatedResultButton.isSelected()) continue;
            this.hoveredResultButton = animatedResultButton;
        }
        this.prevPageButton.render(context, mouseX, mouseY, delta);
        this.nextPageButton.render(context, mouseX, mouseY, delta);
        this.alternatesWidget.render(context, mouseX, mouseY, delta);
    }

    protected void forEachButton(Consumer<ClickableWidget> consumer) {
        consumer.accept(this.nextPageButton);
        consumer.accept(this.prevPageButton);
        this.resultButtons.forEach(consumer);
    }

    public void onRecipesDisplayed(List<Recipe<?>> recipes) {
        for (RecipeDisplayListener recipeDisplayListener : this.recipeDisplayListeners) {
            recipeDisplayListener.onRecipesDisplayed(recipes);
        }
    }

    private void refreshResultButtons() {

        this.resultButtons.forEach(
                button ->{
                    button.receiveRecipe(null);
                }
        );


        int b=0;
        for (int j = 5 * this.currentPage; j < listRecipes.size(); ++j) {
            if(b==5){
                break;
            }

            this.resultButtons.get(b).receiveRecipe(listRecipes.get(j));
            b=b+1;
        }

        this.hideShowPageButtons();
    }

    private void hideShowPageButtons() {
        this.nextPageButton.visible = this.pageCount > 1 && this.currentPage < this.pageCount - 1;
        this.prevPageButton.visible = this.pageCount > 1 && this.currentPage > 0;
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button, int areaLeft, int areaTop, int areaWidth, int areaHeight) {

        if (this.nextPageButton.mouseClicked(mouseX, mouseY, button)) {
            ++this.currentPage;
            this.refreshResultButtons();
            return true;
        }
        if (this.prevPageButton.mouseClicked(mouseX, mouseY, button)) {
            --this.currentPage;
            this.refreshResultButtons();
            return true;
        }

        return false;
    }

    public RecipeBook getRecipeBook() {
        return this.recipeBook;
    }
    public MinecraftClient getClient() {
        return this.client;
    }
}
