package TCOTS.potions.recipes.recipebook.widget;

import TCOTS.TCOTS_Main;
import TCOTS.items.TCOTS_Items;
import TCOTS.potions.recipes.AlchemyTableRecipe;
import TCOTS.potions.recipes.AlchemyTableRecipeCategory;
import TCOTS.potions.screen.AlchemyTableScreenHandler;
import com.google.common.collect.Lists;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Drawable;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.Selectable;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.screen.recipebook.*;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.recipebook.RecipeBookGroup;
import net.minecraft.item.Items;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeGridAligner;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Environment(value= EnvType.CLIENT)
public class AlchemyRecipeBookWidget implements RecipeGridAligner<Ingredient>,
        Drawable,
        Element,
        Selectable,
        RecipeDisplayListener {
    public static final Identifier RECIPE_GUI_TEXTURE = new Identifier(TCOTS_Main.MOD_ID,"textures/gui/alchemy_recipe_book.png");

    //TODO: Fix the order of the recipes

    private final List<AlchemyTableRecipe> listRecipes = new ArrayList<>();
    private boolean open=false;
    private boolean narrow;
    private int leftOffset;
    private final AlchemyRecipeBookResults recipesArea = new AlchemyRecipeBookResults();
    private final List<AlchemyRecipeGroupButton> tabButtons = Lists.newArrayList();
    @Nullable
    private AlchemyRecipeGroupButton currentTab;
    private int parentWidth;
    private int parentHeight;
    private boolean searching;

    protected MinecraftClient client;
    private TextRenderer parenttextRenderer;
    private int cachedInvChangeCount;
    private int cachedInvBlockChangeCount;
    protected AlchemyTableScreenHandler craftingScreenHandler;

    public void update() {
        if(!isOpen()){
            return;
        }

        if (this.cachedInvChangeCount != this.client.player.getInventory().getChangeCount()
                || this.cachedInvBlockChangeCount != this.craftingScreenHandler.getChangeCount()) {

            recipesArea.updateCanCraft();
            this.cachedInvChangeCount = this.client.player.getInventory().getChangeCount();
            this.cachedInvBlockChangeCount = this.craftingScreenHandler.getChangeCount();
        }
    }

    public void init(int height, int width, TextRenderer parenttextRenderer, MinecraftClient client, AlchemyTableScreenHandler craftingScreenHandler) {
        int i = (parentWidth - 147) / 2 - this.leftOffset;
        int j = (parentHeight - 166) / 2;
        this.narrow = width < 379;
        this.parentWidth=width;
        this.parentHeight=height;
        this.craftingScreenHandler=craftingScreenHandler;
        this.parenttextRenderer=parenttextRenderer;
        this.client=client;
        this.cachedInvChangeCount = client.player.getInventory().getChangeCount();
        this.cachedInvBlockChangeCount = craftingScreenHandler.getChangeCount();


        if(listRecipes.isEmpty()){
            assert client.player != null;
            List<RecipeResultCollection> list= client.player.getRecipeBook().getResultsForGroup(RecipeBookGroup.UNKNOWN);
            list.forEach(
                    recipeResultCollection -> {
                        if(recipeResultCollection.getAllRecipes().get(0).getType() == AlchemyTableRecipe.Type.INSTANCE){

                            listRecipes.add((AlchemyTableRecipe) recipeResultCollection.getAllRecipes().get(0));
                        }
                    }
            );
        }

        if (this.open) {
            this.reset();
        }
    }

    public int findLeftEdge(int width, int backgroundWidth) {
        int i = this.isOpen() && !this.narrow ? 177 + (width - backgroundWidth - 200) / 2 : (width - backgroundWidth) / 2;
        return i;
    }

    public void toggleOpen() {
        this.setOpen(!this.isOpen());
    }

    public boolean isOpen() {
        return this.open;
    }

    public void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        this.leftOffset = this.narrow ? 0 : 86;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        if (!this.open) {
            return;
        }
        context.getMatrices().push();
        context.getMatrices().translate(0,0,100);
        int i = (parentWidth - 147) / 2 - this.leftOffset;
        int j = (parentHeight - 166) / 2;

        int color = 0x395026;

        switch (currentTab.getCategory()){
            case POTIONS:
                context.drawText(this.parenttextRenderer, "Potions", i+18,j+15, color,false);
                break;
            case DECOCTIONS:
                context.drawText(this.parenttextRenderer, "Decoctions", i+18,j+15, color,false);
                break;
            case BOMBS:
                context.drawText(this.parenttextRenderer, "Bombs", i+18,j+15, color,false);
                break;
            case MISC:
                context.drawText(this.parenttextRenderer, "Misc", i+18,j+15, color,false);
                break;

            default:
                break;
        }

        for (AlchemyRecipeGroupButton recipeGroupButtonWidget : this.tabButtons) {
            recipeGroupButtonWidget.render(context, mouseX, mouseY, delta);
        }
        context.getMatrices().pop();


        renderRecipeBook(context, this.parentWidth, this.parentHeight, this.parenttextRenderer, i, j);
        this.recipesArea.draw(context, i,j,mouseX,mouseY,delta);
    }
    public void renderRecipeBook(DrawContext context, int width, int height, TextRenderer textRenderer, int i, int j){
        if (open) {
            context.getMatrices().push();
            context.getMatrices().translate(0,0,0);
            //BookRender
            context.drawTexture(RECIPE_GUI_TEXTURE,
                    i, j,
                    1, 1,
                    147, 166);
            context.getMatrices().pop();
        }
    }

    public void reset(){
        int i = (parentWidth - 147) / 2 - this.leftOffset;
        int j = (parentHeight - 166) / 2;

        if(this.tabButtons.isEmpty()){
            //Potions
            this.tabButtons.add(0,new AlchemyRecipeGroupButton(TCOTS_Items.DWARVEN_SPIRIT.getDefaultStack(), AlchemyTableRecipeCategory.POTIONS));
            //Decoctions
            this.tabButtons.add(1,new AlchemyRecipeGroupButton(Items.SPIDER_EYE.getDefaultStack(), AlchemyTableRecipeCategory.DECOCTIONS));
            //Bombs
            this.tabButtons.add(2,new AlchemyRecipeGroupButton(Items.TNT.getDefaultStack(), AlchemyTableRecipeCategory.BOMBS));
            //Misc
            this.tabButtons.add(3,new AlchemyRecipeGroupButton(Items.GLOWSTONE.getDefaultStack(), AlchemyTableRecipeCategory.MISC));
        }
        if(currentTab!=null) {currentTab.setToggled(false);}
        this.currentTab = tabButtons.get(0);
        currentTab.setToggled(true);

        this.recipesArea.initialize(this.client, i, j, craftingScreenHandler);
        this.recipesArea.sendList(listRecipes);
        this.recipesArea.setResults(false, currentTab.getCategory());

        this.refreshTabButtons();
    }

    protected void setOpen(boolean opened) {
        if (opened) {
            this.reset();
        }
        this.open = opened;
    }

    @Override
    public void setFocused(boolean focused) {

    }

    @Override
    public boolean isFocused() {
        return false;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (!this.isOpen() || this.client.player.isSpectator()) {
            return false;
        }


        if(this.recipesArea.mouseClicked(mouseX, mouseY, button,
                (this.parentWidth - 147) / 2 - this.leftOffset,
                (this.parentHeight - 166) / 2,
                147, 166, currentTab.getCategory())){

            if(recipesArea.recipe != null ){
            this.client.interactionManager.clickRecipe(this.client.player.currentScreenHandler.syncId, recipesArea.recipe, Screen.hasShiftDown());
            }
        }

        if(this.tabButtons.get(0).mouseClicked(mouseX, mouseY, button)){
            currentTab.setToggled(false);
            currentTab = this.tabButtons.get(0);
            currentTab.setToggled(true);
            this.recipesArea.setResults(true, currentTab.getCategory());
        }

        if(this.tabButtons.get(1).mouseClicked(mouseX, mouseY, button)){
            currentTab.setToggled(false);
            currentTab = this.tabButtons.get(1);
            currentTab.setToggled(true);
            this.recipesArea.setResults(true, currentTab.getCategory());
        }

        if(this.tabButtons.get(2).mouseClicked(mouseX, mouseY, button)){
            currentTab.setToggled(false);
            currentTab = this.tabButtons.get(2);
            currentTab.setToggled(true);
            this.recipesArea.setResults(true, currentTab.getCategory());
        }

        if(this.tabButtons.get(3).mouseClicked(mouseX, mouseY, button)){
            currentTab.setToggled(false);
            currentTab = this.tabButtons.get(3);
            currentTab.setToggled(true);
            this.recipesArea.setResults(true, currentTab.getCategory());
        }


        for (AlchemyRecipeGroupButton recipeGroupButtonWidget : this.tabButtons) {
            if (!recipeGroupButtonWidget.mouseClicked(mouseX, mouseY, button)) continue;
            if (this.currentTab != recipeGroupButtonWidget) {
                if (this.currentTab != null) {
                    this.currentTab.setToggled(false);
                }
                this.currentTab = recipeGroupButtonWidget;
                this.currentTab.setToggled(true);
            }
            return true;
        }
        return false;
    }

    @Override
    public Selectable.SelectionType getType() {
        return this.open ? Selectable.SelectionType.HOVERED : Selectable.SelectionType.NONE;
    }

    @Override
    public void appendNarrations(NarrationMessageBuilder builder) {
        ArrayList<ClickableWidget> list = Lists.newArrayList();
        this.recipesArea.forEachButton(button -> {
            if (button.isNarratable()) {
                list.add((ClickableWidget)button);
            }
        });
//        list.add(this.searchField);
//        list.add(this.toggleCraftableButton);
//        list.addAll(this.tabButtons);
        Screen.SelectedElementNarrationData selectedElementNarrationData = Screen.findSelectedElementData(list, null);
        if (selectedElementNarrationData != null) {
            selectedElementNarrationData.selectable.appendNarrations(builder.nextMessage());
        }
    }

    @Override
    public void onRecipesDisplayed(List<Recipe<?>> recipes) {

    }

    @Override
    public void acceptAlignedInput(Iterator<Ingredient> inputs, int slot, int amount, int gridX, int gridY) {

    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        this.searching = false;
        if (!this.isOpen() || this.client.player.isSpectator()) {
            return false;
        }
        if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
            this.setOpen(false);
            return true;
        }

        return false;
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        this.searching = false;
        return Element.super.keyReleased(keyCode, scanCode, modifiers);
    }

    private void refreshTabButtons() {
        int i = (this.parentWidth - 147) / 2 - this.leftOffset - 30;
        int j = (this.parentHeight - 166) / 2 + 3;
        int k = 27;
        int l = 0;
        for (AlchemyRecipeGroupButton recipeGroupButtonWidget : this.tabButtons) {
            recipeGroupButtonWidget.setPosition(i, j + 14 + (l++ * 28));
            recipeGroupButtonWidget.checkForNewRecipes(this.client);
        }
    }

    @Override
    public boolean charTyped(char chr, int modifiers) {
        if (this.searching) {
            return false;
        }
        if (!this.isOpen() || this.client.player.isSpectator()) {
            return false;
        }
        return Element.super.charTyped(chr, modifiers);
    }

}
