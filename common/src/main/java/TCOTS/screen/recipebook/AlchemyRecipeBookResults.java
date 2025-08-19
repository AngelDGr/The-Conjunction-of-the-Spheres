package TCOTS.screen.recipebook;

import TCOTS.TCOTS_Main;
import TCOTS.recipes.AlchemyTableRecipe;
import TCOTS.recipes.AlchemyTableRecipeCategory;
import TCOTS.screen.AlchemyTableScreenHandler;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.client.ClientRecipeBook;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.StateSwitchingButton;
import net.minecraft.client.gui.components.WidgetSprites;
import net.minecraft.client.gui.screens.recipebook.OverlayRecipeComponent;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;

public class AlchemyRecipeBookResults {
    private final List<AlchemyRecipeResultButton> resultButtons = Lists.newArrayListWithCapacity(5);
    private final List<RecipeHolder<AlchemyTableRecipe>> listFittingRecipes = new ArrayList<>();
    private final List<RecipeHolder<AlchemyTableRecipe>> listRecipesPotions = new ArrayList<>();
    private final List<RecipeHolder<AlchemyTableRecipe>> listRecipesBombs = new ArrayList<>();
    private final List<RecipeHolder<AlchemyTableRecipe>> listRecipesDecoctions = new ArrayList<>();
    private final List<RecipeHolder<AlchemyTableRecipe>> listRecipesMisc = new ArrayList<>();
    private final List<RecipeHolder<AlchemyTableRecipe>> Active_listRecipes = new ArrayList<>();
    private final StackedContents recipeFinder = new StackedContents();
    private final OverlayRecipeComponent alternatesWidget = new OverlayRecipeComponent();

    private Minecraft client;
    private StateSwitchingButton nextPageButton;
    public static final WidgetSprites TEXTURES_RIGHT_PAGE = new WidgetSprites(
            ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "buttons/page_right"),
            ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "buttons/page_right_highlighted"));

    public static final WidgetSprites TEXTURES_LEFT_PAGE = new WidgetSprites(
            ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "buttons/page_left"),
            ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "buttons/page_left_highlighted"));

    private StateSwitchingButton prevPageButton;
    private int pageCount;
    public int currentPage;

    public NonNullList<ItemStack> PlayerInventoryItems;
    public List<ItemStack> TotalInventoryItems=new ArrayList<>();
    public List<ItemStack> AlchemyTableInventoryItems = new ArrayList<>();
    protected AlchemyTableScreenHandler craftingScreenHandler;

    public AlchemyRecipeBookResults() {
        for (int i = 0; i < 6; ++i) {
            this.resultButtons.add(new AlchemyRecipeResultButton());
        }
    }

    public void initialize(final Minecraft client, final int parentI, final int parentJ, final AlchemyTableScreenHandler craftingScreenHandler) {
        this.client = client;

        this.craftingScreenHandler = craftingScreenHandler;

        assert this.client.player != null;
        PlayerInventoryItems = this.client.player.getInventory().items;
        AlchemyTableInventoryItems = craftingScreenHandler.inputInventory.getItems();

        TotalInventoryItems.addAll(PlayerInventoryItems);
        TotalInventoryItems.addAll(AlchemyTableInventoryItems);
        this.recipeFinder.clear();
        for(final ItemStack stack: this.TotalInventoryItems){
            recipeFinder.accountStack(stack, stack.getCount());
        }

        for (int i = 0; i < this.resultButtons.size(); ++i) {
                this.resultButtons.get(i).setPosition(parentI + 14, (parentJ + 26) + (i * 23 ));
        }
        this.nextPageButton = new AlchemyRecipeBookButtonPage(parentI + 100, parentJ + 166, 18, 10, false);

        this.nextPageButton.initTextureValues(TEXTURES_RIGHT_PAGE);
        this.prevPageButton = new AlchemyRecipeBookButtonPage(parentI + 30, parentJ + 166, 18, 10, true);
        this.prevPageButton.initTextureValues(TEXTURES_LEFT_PAGE);
    }


    public void setResults(final boolean resetCurrentPage, final AlchemyTableRecipeCategory category) {

        if(resetCurrentPage){
            this.currentPage=0;
        }
        this.refreshResultButtons(category);
        this.resetPageCount(resetCurrentPage);
    }

    public void updateCanCraft() {

        assert this.client.player != null;

        AlchemyTableInventoryItems = craftingScreenHandler.inputInventory.getItems();
        PlayerInventoryItems = this.client.player.getInventory().items;

        TotalInventoryItems.clear();
        TotalInventoryItems.addAll(PlayerInventoryItems);
        TotalInventoryItems.addAll(AlchemyTableInventoryItems);


        this.recipeFinder.clear();
        for(final ItemStack stack: this.TotalInventoryItems){
            recipeFinder.accountStack(stack, stack.getCount());
        }

        for(final AlchemyRecipeResultButton button: this.resultButtons){
            button.setCraftable(false);

            if(button.getRecipe() != null) {

                if (recipeFinder.canCraft(button.getRecipe(), null)) {
                    for (int i = 0; i < button.getRecipe().getIngredients().size(); i++) {
                        final int ItemId = Item.getId(button.getRecipe().getIngredients().get(i).getItems()[0].getItem());
                        if (recipeFinder.contents.get(ItemId) >= button.getRecipe().getIngredientsCounts().get(i)) {

                            if(recipeFinder.contents.get(Item.getId(button.getRecipe().getBaseItem().getItem())) >= 1){
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

                button.setBaseNotPresent(recipeFinder.contents.get(Item.getId(button.getRecipe().getBaseItem().getItem())) >= button.getRecipe().getBaseItem().getCount());
                button.textColorBase = recipeFinder.contents.get(Item.getId(button.getRecipe().getBaseItem().getItem())) >= button.getRecipe().getBaseItem().getCount()? 0xffffff: 0xb43d2c;

                for (int i = 0; i < button.getRecipe().getIngredients().size(); i++) {
                    final int ItemId = Item.getId(button.getRecipe().getIngredients().get(i).getItems()[0].getItem());
                    button.setTextColor(i, recipeFinder.contents.get(ItemId) >= button.getRecipe().getIngredientsCounts().get(i));
                }

            }
        }
    }


    private void resetPageCount(final boolean resetCurrentPage){
        this.pageCount = (int)Math.ceil((double)Active_listRecipes.size() / 6.0);

        if (this.pageCount <= this.currentPage || resetCurrentPage) {
            this.currentPage = 0;
        }
    }

    private ClientRecipeBook recipeBook;
    //Receive the Recipes list
    public void receiveRecipesList(final List<RecipeHolder<AlchemyTableRecipe>> listRecipes, final ClientRecipeBook recipeBook){
        if(this.listFittingRecipes.isEmpty()) {

            this.recipeBook=recipeBook;

            listFittingRecipes.addAll(listRecipes);

            for (int i = 0; i < 6; i++) {
                this.resultButtons.get(i).receiveTextRenderer(client.font);
            }

            this.listFittingRecipes.forEach(
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
            Active_listRecipes.sort(Comparator.comparing(recipeEntry -> recipeEntry.value().getOrder()));
            this.resetPageCount(false);

        }
    }

    public void draw(final GuiGraphics context, final int x, final int y, final int mouseX, final int mouseY, final float delta) {
        if (this.pageCount > 1) {
            final String string = this.currentPage + 1 + "/" + this.pageCount;
            final int i = this.client.font.width(string);
            context.drawString(this.client.font, string, x - i / 2 + 73, y + 166, 0xffffff, true);
        }

        for (final AlchemyRecipeResultButton animatedResultButton : this.resultButtons) {
            animatedResultButton.render(context, mouseX, mouseY, delta);
        }
        this.prevPageButton.render(context, mouseX, mouseY, delta);
        this.nextPageButton.render(context, mouseX, mouseY, delta);
        this.alternatesWidget.render(context, mouseX, mouseY, delta);
    }

    protected void forEachButton(final Consumer<AbstractWidget> consumer) {
        consumer.accept(this.nextPageButton);
        consumer.accept(this.prevPageButton);
        this.resultButtons.forEach(consumer);
    }


    public void refreshResultButtons(final AlchemyTableRecipeCategory category) {
        this.resultButtons.forEach(
                button -> button.receiveRecipe(null, this.recipeBook)
        );

        int b=0;
        switch (category){
            case POTIONS:
                for (int j = 6 * this.currentPage; j < listRecipesPotions.size(); ++j) {
                    if(b==6){
                        Active_listRecipes.clear();
                        Active_listRecipes.addAll(listRecipesPotions);
                        Active_listRecipes.sort(Comparator.comparing(recipeEntry -> recipeEntry.value().getOrder()));
                        break;
                    }

                    this.resultButtons.get(b).receiveRecipe(listRecipesPotions.get(j), this.recipeBook);
                    b=b+1;
                }
                Active_listRecipes.clear();
                Active_listRecipes.addAll(listRecipesPotions);
                Active_listRecipes.sort(Comparator.comparing(recipeEntry -> recipeEntry.value().getOrder()));
                break;
            case DECOCTIONS:
                for (int j = 6 * this.currentPage; j < listRecipesDecoctions.size(); ++j) {
                    if(b==6){
                        Active_listRecipes.clear();
                        Active_listRecipes.addAll(listRecipesDecoctions);
                        Active_listRecipes.sort(Comparator.comparing(recipeEntry -> recipeEntry.value().getOrder()));
                        break;
                    }

                    this.resultButtons.get(b).receiveRecipe(listRecipesDecoctions.get(j), this.recipeBook);
                    b=b+1;
                }
                Active_listRecipes.clear();
                Active_listRecipes.addAll(listRecipesDecoctions);
                Active_listRecipes.sort(Comparator.comparing(recipeEntry -> recipeEntry.value().getOrder()));
                break;

            case BOMBS_OILS:
                for (int j = 6 * this.currentPage; j < listRecipesBombs.size(); ++j) {
                    if(b==6){
                        Active_listRecipes.clear();
                        Active_listRecipes.addAll(listRecipesBombs);
                        Active_listRecipes.sort(Comparator.comparing(recipeEntry -> recipeEntry.value().getOrder()));
                        break;
                    }

                    this.resultButtons.get(b).receiveRecipe(listRecipesBombs.get(j), this.recipeBook);
                    b=b+1;
                }
                Active_listRecipes.clear();
                Active_listRecipes.addAll(listRecipesBombs);
                Active_listRecipes.sort(Comparator.comparing(recipeEntry -> recipeEntry.value().getOrder()));
                break;

            case MISC:
                for (int j = 6 * this.currentPage; j < listRecipesMisc.size(); ++j) {
                    if(b==6){
                        Active_listRecipes.clear();
                        Active_listRecipes.addAll(listRecipesMisc);
                        Active_listRecipes.sort(Comparator.comparing(recipeEntry -> recipeEntry.value().getOrder()));
                        break;
                    }

                    this.resultButtons.get(b).receiveRecipe(listRecipesMisc.get(j), this.recipeBook);
                    b=b+1;
                }
                Active_listRecipes.clear();
                Active_listRecipes.addAll(listRecipesMisc);
                Active_listRecipes.sort(Comparator.comparing(recipeEntry -> recipeEntry.value().getOrder()));
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

    RecipeHolder<AlchemyTableRecipe> recipe;
    public boolean mouseClicked(final double mouseX, final double mouseY, final int button, final AlchemyTableRecipeCategory category) {

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

        for(final AlchemyRecipeResultButton resultButton: this.resultButtons){
            if(resultButton.mouseClicked(mouseX, mouseY, button) && resultButton.getCraftable()){
                if(resultButton.getRecipeEntry() != null){
                    recipe = resultButton.getRecipeEntry();
                    return true;
                }
            }
        }

        return false;
    }

    public Minecraft getClient() {
        return this.client;
    }
}
