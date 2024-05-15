package TCOTS.items.potions.recipes.recipebook.widget;

import TCOTS.TCOTS_Main;
import TCOTS.items.potions.recipes.AlchemyTableRecipe;
import TCOTS.items.potions.recipes.AlchemyTableRecipeCategory;
import TCOTS.screen.AlchemyTableScreenHandler;
import com.google.common.collect.Lists;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ButtonTextures;
import net.minecraft.client.gui.screen.recipebook.*;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.ToggleButtonWidget;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.RecipeMatcher;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;

public class AlchemyRecipeBookResults {
    private final List<AlchemyRecipeResultButton> resultButtons = Lists.newArrayListWithCapacity(5);
    private final List<RecipeEntry<AlchemyTableRecipe>> listRecipes = new ArrayList<>();
    private final List<RecipeEntry<AlchemyTableRecipe>> listRecipesPotions = new ArrayList<>();
    private final List<RecipeEntry<AlchemyTableRecipe>> listRecipesBombs = new ArrayList<>();
    private final List<RecipeEntry<AlchemyTableRecipe>> listRecipesDecoctions = new ArrayList<>();
    private final List<RecipeEntry<AlchemyTableRecipe>> listRecipesMisc = new ArrayList<>();
    private final List<RecipeEntry<AlchemyTableRecipe>> Active_listRecipes = new ArrayList<>();
    private final RecipeMatcher recipeFinder = new RecipeMatcher();
    private final RecipeAlternativesWidget alternatesWidget = new RecipeAlternativesWidget();

    private MinecraftClient client;
    private ToggleButtonWidget nextPageButton;
    public static final ButtonTextures TEXTURES_RIGHT_PAGE = new ButtonTextures(
            new Identifier(TCOTS_Main.MOD_ID, "buttons/page_right"),
            new Identifier(TCOTS_Main.MOD_ID, "buttons/page_right_highlighted"));

    public static final ButtonTextures TEXTURES_LEFT_PAGE = new ButtonTextures(
            new Identifier(TCOTS_Main.MOD_ID, "buttons/page_left"),
            new Identifier(TCOTS_Main.MOD_ID, "buttons/page_left_highlighted"));

    private ToggleButtonWidget prevPageButton;
    private int pageCount;
    public int currentPage;

    public DefaultedList<ItemStack> PlayerInventoryItems;
    public List<ItemStack> TotalInventoryItems=new ArrayList<>();
    public List<ItemStack> AlchemyTableInventoryItems = new ArrayList<>();
    protected AlchemyTableScreenHandler craftingScreenHandler;

    public AlchemyRecipeBookResults() {
        for (int i = 0; i < 5; ++i) {
            this.resultButtons.add(new AlchemyRecipeResultButton());
        }
    }

    public void initialize(MinecraftClient client, int parentI, int parentJ, AlchemyTableScreenHandler craftingScreenHandler) {
        this.client = client;

        this.craftingScreenHandler = craftingScreenHandler;

        for (int i=0;i<craftingScreenHandler.getInventory().size();i++){
            AlchemyTableInventoryItems.add(craftingScreenHandler.getInventory().getStack(i));
        }

        assert this.client.player != null;
        PlayerInventoryItems = this.client.player.getInventory().main;

        TotalInventoryItems.addAll(PlayerInventoryItems);
        TotalInventoryItems.addAll(AlchemyTableInventoryItems);
        this.recipeFinder.clear();
        for(ItemStack stack: this.TotalInventoryItems){
            recipeFinder.addInput(stack, stack.getCount());
        }

        for (int i = 0; i < this.resultButtons.size(); ++i) {
                this.resultButtons.get(i).setPosition(parentI + 14, (parentJ + 26) + (i * 23 ));
        }
        this.nextPageButton = new AlchemyRecipeBookButtonPage(parentI + 100, parentJ + 143, 18, 10, false);

        this.nextPageButton.setTextures(TEXTURES_RIGHT_PAGE);
        this.prevPageButton = new AlchemyRecipeBookButtonPage(parentI + 30, parentJ + 143, 18, 10, true);
        this.prevPageButton.setTextures(TEXTURES_LEFT_PAGE);
    }


    public void setResults(boolean resetCurrentPage, AlchemyTableRecipeCategory category) {

        if(resetCurrentPage){
            this.currentPage=0;
        }
        this.refreshResultButtons(category);
        this.resetPageCount(resetCurrentPage);
    }

    public void updateCanCraft() {

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

        for(AlchemyRecipeResultButton button: this.resultButtons){
            button.setCraftable(false);

            if(button.getRecipe() != null) {

                if (recipeFinder.match(button.getRecipe(), null)) {

                    for (int i = 0; i < button.getRecipe().getIngredients().size(); i++) {
                        int ItemId = Item.getRawId(button.getRecipe().getIngredients().get(i).getMatchingStacks()[0].getItem());
                        if (recipeFinder.inputs.get(ItemId) >= button.getRecipe().getIngredientsCounts().get(i)) {

                            if(recipeFinder.inputs.get(Item.getRawId(button.getRecipe().getBaseItem().getItem())) >= 1){
                                button.setCraftable(true);
                            }
                        } else {
                            button.setCraftable(false);
                            break;
                        }
                    }
                } else {
                    button.setCraftable(false);
                }

                button.setBaseNotPresent(recipeFinder.inputs.get(Item.getRawId(button.getRecipe().getBaseItem().getItem())) >= button.getRecipe().getBaseItem().getCount());
                button.textColorBase = recipeFinder.inputs.get(Item.getRawId(button.getRecipe().getBaseItem().getItem())) >= button.getRecipe().getBaseItem().getCount()? 0xffffff: 0xb43d2c;

                for (int i = 0; i < button.getRecipe().getIngredients().size(); i++) {
                    int ItemId = Item.getRawId(button.getRecipe().getIngredients().get(i).getMatchingStacks()[0].getItem());
                    button.setTextColor(i, recipeFinder.inputs.get(ItemId) >= button.getRecipe().getIngredientsCounts().get(i));
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
    public void receiveRecipesList(List<RecipeEntry<AlchemyTableRecipe>> listRecipes){
        if(this.listRecipes.isEmpty()) {
            this.listRecipes.addAll(listRecipes);
            for (int i = 0; i < 5; i++) {
                this.resultButtons.get(i).receiveTextRenderer(client.textRenderer);
            }

            this.listRecipes.forEach(
                    recipe -> {
                        if(recipe.value().getCategory() == AlchemyTableRecipeCategory.POTIONS){
                            listRecipesPotions.add(recipe);
                        }

                        if(recipe.value().getCategory() == AlchemyTableRecipeCategory.DECOCTIONS){
                            listRecipesDecoctions.add(recipe);
                        }

                        if(recipe.value().getCategory() == AlchemyTableRecipeCategory.BOMBS_OILS){
                            listRecipesBombs.add(recipe);
                        }

                        if(recipe.value().getCategory() == AlchemyTableRecipeCategory.MISC){
                            listRecipesMisc.add(recipe);
                        }
                    }
            );
            Active_listRecipes.clear();
            Active_listRecipes.addAll(listRecipesPotions);
            Active_listRecipes.sort(Comparator.comparing(recipeEntry -> recipeEntry.value().getId()));
            this.resetPageCount(false);

        }
    }

    public void draw(DrawContext context, int x, int y, int mouseX, int mouseY, float delta) {
        if (this.pageCount > 1) {
            String string = this.currentPage + 1 + "/" + this.pageCount;
            int i = this.client.textRenderer.getWidth(string);
            context.drawText(this.client.textRenderer, string, x - i / 2 + 73, y + 143, 0xffffff, true);
        }

        for (AlchemyRecipeResultButton animatedResultButton : this.resultButtons) {
            animatedResultButton.render(context, mouseX, mouseY, delta);
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


    public void refreshResultButtons(AlchemyTableRecipeCategory category) {
        this.resultButtons.forEach(
                button -> button.receiveRecipe(null)
        );

        int b=0;
        switch (category){
            case POTIONS:
                for (int j = 5 * this.currentPage; j < listRecipesPotions.size(); ++j) {
                    if(b==5){
                        Active_listRecipes.clear();
                        Active_listRecipes.addAll(listRecipesPotions);
                        Active_listRecipes.sort(Comparator.comparing(recipeEntry -> recipeEntry.value().getId()));
                        break;
                    }

                    this.resultButtons.get(b).receiveRecipe(listRecipesPotions.get(j));
                    b=b+1;
                }
                Active_listRecipes.clear();
                Active_listRecipes.addAll(listRecipesPotions);
                Active_listRecipes.sort(Comparator.comparing(recipeEntry -> recipeEntry.value().getId()));
                break;
            case DECOCTIONS:
                for (int j = 5 * this.currentPage; j < listRecipesDecoctions.size(); ++j) {
                    if(b==5){
                        Active_listRecipes.clear();
                        Active_listRecipes.addAll(listRecipesDecoctions);
                        Active_listRecipes.sort(Comparator.comparing(recipeEntry -> recipeEntry.value().getId()));
                        break;
                    }

                    this.resultButtons.get(b).receiveRecipe(listRecipesDecoctions.get(j));
                    b=b+1;
                }
                Active_listRecipes.clear();
                Active_listRecipes.addAll(listRecipesDecoctions);
                Active_listRecipes.sort(Comparator.comparing(recipeEntry -> recipeEntry.value().getId()));
                break;

            case BOMBS_OILS:
                for (int j = 5 * this.currentPage; j < listRecipesBombs.size(); ++j) {
                    if(b==5){
                        Active_listRecipes.clear();
                        Active_listRecipes.addAll(listRecipesBombs);
                        Active_listRecipes.sort(Comparator.comparing(recipeEntry -> recipeEntry.value().getId()));
                        break;
                    }

                    this.resultButtons.get(b).receiveRecipe(listRecipesBombs.get(j));
                    b=b+1;
                }
                Active_listRecipes.clear();
                Active_listRecipes.addAll(listRecipesBombs);
                Active_listRecipes.sort(Comparator.comparing(recipeEntry -> recipeEntry.value().getId()));
                break;

            case MISC:
                for (int j = 5 * this.currentPage; j < listRecipesMisc.size(); ++j) {
                    if(b==5){
                        Active_listRecipes.clear();
                        Active_listRecipes.addAll(listRecipesMisc);
                        Active_listRecipes.sort(Comparator.comparing(recipeEntry -> recipeEntry.value().getId()));
                        break;
                    }

                    this.resultButtons.get(b).receiveRecipe(listRecipesMisc.get(j));
                    b=b+1;
                }
                Active_listRecipes.clear();
                Active_listRecipes.addAll(listRecipesMisc);
                Active_listRecipes.sort(Comparator.comparing(recipeEntry -> recipeEntry.value().getId()));
                break;
        }

        this.resetPageCount(false);
        this.hideShowPageButtons();
        this.updateCanCraft();
    }

    private void hideShowPageButtons() {
        this.nextPageButton.visible = this.pageCount > 1 && this.currentPage < this.pageCount - 1;
        this.prevPageButton.visible = this.pageCount > 1 && this.currentPage > 0;
    }

    RecipeEntry<
    AlchemyTableRecipe> recipe;
    public boolean mouseClicked(double mouseX, double mouseY, int button, AlchemyTableRecipeCategory category) {

        if (this.nextPageButton.mouseClicked(mouseX, mouseY, button)) {
            recipe = null;
            ++this.currentPage;
            this.refreshResultButtons(category);
            return true;
        }

        if (this.prevPageButton.mouseClicked(mouseX, mouseY, button)) {
            recipe = null;
            --this.currentPage;
            this.refreshResultButtons(category);
            return true;
        }

        for(AlchemyRecipeResultButton resultButton: this.resultButtons){
            if(resultButton.mouseClicked(mouseX, mouseY, button) && resultButton.getCraftable()){
                if(resultButton.getRecipeEntry() != null){
                    recipe = resultButton.getRecipeEntry();
                    return true;
                }
            }
        }

        return false;
    }

    public MinecraftClient getClient() {
        return this.client;
    }
}
