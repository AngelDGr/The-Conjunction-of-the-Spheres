package TCOTS.potions.recipes.recipebook.widget;

import TCOTS.potions.recipes.AlchemyTableRecipe;
import TCOTS.potions.recipes.AlchemyTableRecipeCategory;
import TCOTS.potions.recipes.recipebook.AbstractRecipeAlchemyScreenHandler;
import TCOTS.potions.screen.AlchemyTableScreenHandler;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.recipebook.*;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.ToggleButtonWidget;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeMatcher;
import net.minecraft.recipe.book.RecipeBook;
import net.minecraft.util.collection.DefaultedList;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class AlchemyRecipeBookResults {
    private final List<AlchemyRecipeResultButton> resultButtons = Lists.newArrayListWithCapacity(5);
    private final List<AlchemyTableRecipe> listRecipes = new ArrayList<>();
    private final List<AlchemyTableRecipe> listRecipesPotions = new ArrayList<>();
    private final List<AlchemyTableRecipe> listRecipesBombs = new ArrayList<>();
    private final List<AlchemyTableRecipe> listRecipesDecoctions = new ArrayList<>();
    private final List<AlchemyTableRecipe> listRecipesMisc = new ArrayList<>();
    private final List<AlchemyTableRecipe> Active_listRecipes = new ArrayList<>();
    public AlchemyRecipeBookButton recipeBookButton;
    private final RecipeMatcher recipeFinder = new RecipeMatcher();
    private int cachedInvChangeCount;
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

    public DefaultedList<ItemStack> PlayerInventoryItems;
    public List<ItemStack> TotalInventoryItems=new ArrayList<>();
    public List<ItemStack> AlchemyTableInventoryItems = new ArrayList<>();
    protected AlchemyTableScreenHandler craftingScreenHandler;
    private int parentI;

    private int parentJ;

    public AlchemyRecipeBookResults() {
        for (int i = 0; i < 5; ++i) {
            this.resultButtons.add(new AlchemyRecipeResultButton());
        }
    }

    public void initialize(MinecraftClient client, int parentI, int parentJ, AlchemyTableScreenHandler craftingScreenHandler) {
        this.client = client;

        this.craftingScreenHandler = craftingScreenHandler;

        assert this.client.player != null;
        assert client.player != null;
        this.recipeBook = client.player.getRecipeBook();

        for (int i=0;i<craftingScreenHandler.getInventory().size();i++){
            AlchemyTableInventoryItems.add(craftingScreenHandler.getInventory().getStack(i));
        }

        PlayerInventoryItems = this.client.player.getInventory().main;

        TotalInventoryItems.addAll(PlayerInventoryItems);
        TotalInventoryItems.addAll(AlchemyTableInventoryItems);
        this.recipeFinder.clear();
        for(ItemStack stack: this.TotalInventoryItems){
            recipeFinder.addInput(stack, stack.getCount());
        }


        this.cachedInvChangeCount = client.player.getInventory().getChangeCount();

        for (int i = 0; i < this.resultButtons.size(); ++i) {
                this.resultButtons.get(i).setPosition(parentI + 14, (parentJ + 26) + (i * 23 ));
        }
        this.nextPageButton = new AlchemyRecipeBookButtonPage(parentI + 100, parentJ + 143, 18, 10, false);
        this.nextPageButton.setTextureUV(157, 80, 23, 13, AlchemyRecipeBookWidget.RECIPE_GUI_TEXTURE);
        this.prevPageButton = new AlchemyRecipeBookButtonPage(parentI + 30, parentJ + 143, 18, 10, true);
        this.prevPageButton.setTextureUV(157, 80, 23, 13, AlchemyRecipeBookWidget.RECIPE_GUI_TEXTURE);
        this.parentI=parentI;
        this.parentJ=parentJ;
    }


    public void setResults(boolean resetCurrentPage, AlchemyTableRecipeCategory category) {

        if(resetCurrentPage){
            this.currentPage=0;
        }
        this.refreshResultButtons(category);
        this.resetPageCount(resetCurrentPage);
    }

    public void updateCantCraft() {

        assert this.client.player != null;

        AlchemyTableInventoryItems.clear();
        for (int i=0;i<craftingScreenHandler.getInventory().size();i++){
            AlchemyTableInventoryItems.add(craftingScreenHandler.getInventory().getStack(i));}



        PlayerInventoryItems = this.client.player.getInventory().main;

        TotalInventoryItems.clear();
        TotalInventoryItems.addAll(PlayerInventoryItems);
        TotalInventoryItems.addAll(AlchemyTableInventoryItems);


        this.recipeFinder.clear();
        for(ItemStack stack: this.TotalInventoryItems){
            recipeFinder.addInput(stack, stack.getCount());
        }

        boolean craftable;
        for(AlchemyRecipeResultButton button: this.resultButtons){
            button.setCraftable(false);

            if(button.getRecipe() != null) {

                if (recipeFinder.match(button.getRecipe(), null)) {

                    for (int i = 0; i < button.getRecipe().getIngredients().size(); i++) {
                        int ItemId = Item.getRawId(button.getRecipe().getIngredients().get(i).getMatchingStacks()[0].getItem());
                        if (recipeFinder.inputs.get(ItemId) >= button.getRecipe().getIngredientsCounts()[i]) {

                            if(recipeFinder.inputs.get(Item.getRawId(button.getRecipe().getBaseItem().getItem())) >= 1){
                                craftable = true;
                                button.setCraftable(craftable);

                            }
                        } else {
                            craftable = false;
                            button.setCraftable(craftable);
                            break;
                        }
                    }
                } else {
                    craftable = false;
                    button.setCraftable(craftable);
                }

                button.setBaseNotPresent(recipeFinder.inputs.get(Item.getRawId(button.getRecipe().getBaseItem().getItem())) >= 1);

                for (int i = 0; i < button.getRecipe().getIngredients().size(); i++) {
                    int ItemId = Item.getRawId(button.getRecipe().getIngredients().get(i).getMatchingStacks()[0].getItem());
                    button.setTextColor(i, recipeFinder.inputs.get(ItemId) >= button.getRecipe().getIngredientsCounts()[i]);
                }
            }
        }
    }


    private void resetPageCount(boolean resetCurrentPage){
        this.pageCount = (int)Math.ceil((double)Active_listRecipes.size() / 5.0);

        if (this.pageCount <= this.currentPage || resetCurrentPage) {
            this.currentPage = 0;
        }
    }


    //Receive the Recipes list
    public void sendList(List<AlchemyTableRecipe> listRecipes){
        if(this.listRecipes.isEmpty()) {
            this.listRecipes.addAll(listRecipes);
            for (int i = 0; i < 5; i++) {
//            this.resultButtons.get(i).receiveRecipe(listRecipes.get(i));
                this.resultButtons.get(i).receiveTextRenderer(client.textRenderer);
            }

            this.listRecipes.forEach(
                    recipe -> {
                        if(recipe.getCategory() == AlchemyTableRecipeCategory.POTIONS){
                            listRecipesPotions.add(recipe);
                        }

                        if(recipe.getCategory() == AlchemyTableRecipeCategory.DECOCTIONS){
                            listRecipesDecoctions.add(recipe);
                        }

                        if(recipe.getCategory() == AlchemyTableRecipeCategory.BOMBS){
                            listRecipesBombs.add(recipe);
                        }

                        if(recipe.getCategory() == AlchemyTableRecipeCategory.MISC){
                            listRecipesMisc.add(recipe);
                        }
                    }
            );
            Active_listRecipes.clear();
            Active_listRecipes.addAll(listRecipesPotions);
            this.resetPageCount(false);

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


    public void refreshResultButtons(AlchemyTableRecipeCategory category) {
        this.resultButtons.forEach(
                button ->{
                    button.receiveRecipe(null);
                }
        );

        int b=0;
        switch (category){
            case POTIONS:
                for (int j = 5 * this.currentPage; j < listRecipesPotions.size(); ++j) {
                    if(b==5){
                        Active_listRecipes.clear();
                        Active_listRecipes.addAll(listRecipesPotions);
                        break;
                    }

                    this.resultButtons.get(b).receiveRecipe(listRecipesPotions.get(j));
                    b=b+1;
                }
                Active_listRecipes.clear();
                Active_listRecipes.addAll(listRecipesPotions);
                break;
            case DECOCTIONS:
                for (int j = 5 * this.currentPage; j < listRecipesDecoctions.size(); ++j) {
                    if(b==5){
                        Active_listRecipes.clear();
                        Active_listRecipes.addAll(listRecipesDecoctions);
                        break;
                    }

                    this.resultButtons.get(b).receiveRecipe(listRecipesDecoctions.get(j));
                    b=b+1;
                }
                Active_listRecipes.clear();
                Active_listRecipes.addAll(listRecipesDecoctions);
                break;

            case BOMBS:
                for (int j = 5 * this.currentPage; j < listRecipesBombs.size(); ++j) {
                    if(b==5){
                        Active_listRecipes.clear();
                        Active_listRecipes.addAll(listRecipesBombs);
                        break;
                    }

                    this.resultButtons.get(b).receiveRecipe(listRecipesBombs.get(j));
                    b=b+1;
                }
                Active_listRecipes.clear();
                Active_listRecipes.addAll(listRecipesBombs);
                break;

            case MISC:
                for (int j = 5 * this.currentPage; j < listRecipesMisc.size(); ++j) {
                    if(b==5){
                        Active_listRecipes.clear();
                        Active_listRecipes.addAll(listRecipesMisc);
                        break;
                    }

                    this.resultButtons.get(b).receiveRecipe(listRecipesMisc.get(j));
                    b=b+1;
                }
                Active_listRecipes.clear();
                Active_listRecipes.addAll(listRecipesMisc);
                break;

            case ALL:
                for (int j = 5 * this.currentPage; j < listRecipes.size(); ++j) {
                    if(b==5){
                        Active_listRecipes.clear();
                        Active_listRecipes.addAll(listRecipes);
                        break;
                    }

                    this.resultButtons.get(b).receiveRecipe(listRecipes.get(j));
                    b=b+1;
                }
                Active_listRecipes.clear();
                Active_listRecipes.addAll(listRecipes);
                break;
        }

        this.resetPageCount(false);
        this.hideShowPageButtons();
        this.updateCantCraft();
    }

    private void hideShowPageButtons() {
        this.nextPageButton.visible = this.pageCount > 1 && this.currentPage < this.pageCount - 1;
        this.prevPageButton.visible = this.pageCount > 1 && this.currentPage > 0;
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button, int areaLeft, int areaTop, int areaWidth, int areaHeight, AlchemyTableRecipeCategory category) {

        if (this.nextPageButton.mouseClicked(mouseX, mouseY, button)) {
            ++this.currentPage;
            this.refreshResultButtons(category);
            return true;
        }

        if (this.prevPageButton.mouseClicked(mouseX, mouseY, button)) {
            --this.currentPage;
            this.refreshResultButtons(category);
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
