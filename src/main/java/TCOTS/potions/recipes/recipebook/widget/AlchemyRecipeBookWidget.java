package TCOTS.potions.recipes.recipebook.widget;

import TCOTS.TCOTS_Main;
import TCOTS.items.TCOTS_Items;
import TCOTS.potions.recipes.AlchemyTableRecipe;
import TCOTS.potions.recipes.AlchemyTableRecipeCategory;
import TCOTS.screen.AlchemyTableScreenHandler;
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
import net.minecraft.recipe.*;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

import java.util.*;

@SuppressWarnings("unused")
@Environment(value= EnvType.CLIENT)
public class AlchemyRecipeBookWidget implements RecipeGridAligner<Ingredient>,
        Drawable,
        Element,
        Selectable,
        RecipeDisplayListener {

    public static final Identifier RECIPE_GUI_TEXTURE = new Identifier(TCOTS_Main.MOD_ID,"textures/gui/alchemy_recipe_book.png");

    //xTODO: Fix the order of the recipes

    private final List<RecipeEntry<AlchemyTableRecipe>> listRecipes = new ArrayList<>();
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

        assert this.client.player != null;
        if (this.cachedInvChangeCount != this.client.player.getInventory().getChangeCount()
                || this.cachedInvBlockChangeCount != this.craftingScreenHandler.getChangeCount()) {

            recipesArea.updateCanCraft();
            this.cachedInvChangeCount = this.client.player.getInventory().getChangeCount();
            this.cachedInvBlockChangeCount = this.craftingScreenHandler.getChangeCount();
        }
    }

    public void init(int height, int width, TextRenderer parenttextRenderer, MinecraftClient client, AlchemyTableScreenHandler craftingScreenHandler) {
        this.narrow = width < 379;
        this.parentWidth=width;
        this.parentHeight=height;
        this.craftingScreenHandler=craftingScreenHandler;
        this.parenttextRenderer=parenttextRenderer;
        this.client=client;
        assert client.player != null;
        this.cachedInvChangeCount = client.player.getInventory().getChangeCount();
        this.cachedInvBlockChangeCount = craftingScreenHandler.getChangeCount();


        if(listRecipes.isEmpty()){
            assert this.client.player != null;

            List<RecipeResultCollection> list= client.player.getRecipeBook().getResultsForGroup(RecipeBookGroup.UNKNOWN);


            for (RecipeResultCollection resultCollection: list){
                if(resultCollection.getAllRecipes().get(0).value() instanceof AlchemyTableRecipe){

                    listRecipes.add((RecipeEntry<AlchemyTableRecipe>) resultCollection.getAllRecipes().get(0));

                }
            }

        }

        if (this.open) {
            this.reset();
        }
    }

    public int findLeftEdge(int width, int backgroundWidth) {
        return this.isOpen() && !this.narrow ? 177 + (width - backgroundWidth - 200) / 2 : (width - backgroundWidth) / 2;
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

        switch (Objects.requireNonNull(currentTab).getCategory()){
            case POTIONS:
                context.drawText(this.parenttextRenderer, "Potions", i+18,j+15, color,false);
                break;
            case DECOCTIONS:
                context.drawText(this.parenttextRenderer, "Decoctions", i+18,j+15, color,false);
                break;
            case BOMBS_OILS:
                context.drawText(this.parenttextRenderer, "Bombs & Oils", i+18,j+15, color,false);
                break;
            case MISC:
                context.drawText(this.parenttextRenderer, "Ingredients", i+18,j+15, color,false);
                break;

            default:
                break;
        }

        for (AlchemyRecipeGroupButton recipeGroupButtonWidget : this.tabButtons) {
            recipeGroupButtonWidget.render(context, mouseX, mouseY, delta);
        }
        context.getMatrices().pop();


        renderRecipeBook(context, this.parentWidth, this.parentHeight, i, j);
        this.recipesArea.draw(context, i,j,mouseX,mouseY,delta);
    }
    public void renderRecipeBook(DrawContext context, int width, int height, int i, int j){
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
            this.tabButtons.add(1,new AlchemyRecipeGroupButton(TCOTS_Items.GRAVE_HAG_DECOCTION.getDefaultStack(), AlchemyTableRecipeCategory.DECOCTIONS));
            //Bombs
            this.tabButtons.add(2,new AlchemyRecipeGroupButton(Items.TNT.getDefaultStack(), AlchemyTableRecipeCategory.BOMBS_OILS));
            //Misc
            this.tabButtons.add(3,new AlchemyRecipeGroupButton(TCOTS_Items.AETHER.getDefaultStack(), AlchemyTableRecipeCategory.MISC));
        }
        if(currentTab!=null) {currentTab.setToggled(false);}
        this.currentTab = tabButtons.get(0);
        currentTab.setToggled(true);

        this.recipesArea.initialize(this.client, i, j, craftingScreenHandler);

        listRecipes.sort(Comparator.comparing(recipeEntry -> recipeEntry.value().getId()));

        this.recipesArea.receiveRecipesList(listRecipes);

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
        if (!this.isOpen() || Objects.requireNonNull(this.client.player).isSpectator()) {
            return false;
        }


        assert currentTab != null;
        if(this.recipesArea.mouseClicked(mouseX, mouseY, button,
                currentTab.getCategory())){

            if(recipesArea.recipe != null ){
                assert this.client.interactionManager != null;
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
                list.add(button);
            }
        });

        Screen.SelectedElementNarrationData selectedElementNarrationData = Screen.findSelectedElementData(list, null);
        if (selectedElementNarrationData != null) {
            selectedElementNarrationData.selectable.appendNarrations(builder.nextMessage());
        }
    }

    @Override
    public void acceptAlignedInput(Iterator<Ingredient> inputs, int slot, int amount, int gridX, int gridY) {

    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        this.searching = false;
        if (!this.isOpen() || Objects.requireNonNull(this.client.player).isSpectator()) {
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
        int i = (this.parentWidth - 147) / 2 - this.leftOffset - 32;
        int j = (this.parentHeight - 166) / 2 + 3;
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
        if (!this.isOpen() || Objects.requireNonNull(this.client.player).isSpectator()) {
            return false;
        }
        return Element.super.charTyped(chr, modifiers);
    }

    @Override
    public void onRecipesDisplayed(List<RecipeEntry<?>> recipes) {

    }
}
