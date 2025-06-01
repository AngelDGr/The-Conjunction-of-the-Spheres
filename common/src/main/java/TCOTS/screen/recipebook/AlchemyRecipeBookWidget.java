package TCOTS.screen.recipebook;

import TCOTS.TCOTS_Main;
import TCOTS.registry.TCOTS_Items;
import TCOTS.recipes.AlchemyTableRecipe;
import TCOTS.recipes.AlchemyTableRecipeCategory;
import TCOTS.screen.AlchemyTableScreenHandler;
import com.google.common.collect.Lists;
import net.minecraft.client.ClientRecipeBook;
import net.minecraft.client.Minecraft;
import net.minecraft.client.RecipeBookCategories;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.recipebook.RecipeCollection;
import net.minecraft.client.gui.screens.recipebook.RecipeShownListener;
import net.minecraft.network.chat.Component;
import net.minecraft.recipebook.PlaceRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

import java.util.*;

@SuppressWarnings({"unused","unchecked"})
public class AlchemyRecipeBookWidget implements PlaceRecipe<Ingredient>,
        Renderable,
        GuiEventListener,
        NarratableEntry,
        RecipeShownListener {

    public static final ResourceLocation RECIPE_GUI_TEXTURE = ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID,"textures/gui/alchemy_recipe_book.png");

    private ClientRecipeBook recipeBook;
    private final List<RecipeHolder<AlchemyTableRecipe>> listRecipes = new ArrayList<>();
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

    protected Minecraft client;
    private Font parenttextRenderer;
    private int cachedInvChangeCount;
    private int cachedInvBlockChangeCount;
    protected AlchemyTableScreenHandler craftingScreenHandler;

    public void update() {
        if(!isOpen()){
            return;
        }

        assert this.client.player != null;
        if (this.cachedInvChangeCount != this.client.player.getInventory().getTimesChanged()
//                || this.cachedInvBlockChangeCount != this.craftingScreenHandler.getChangeCount()
        ) {

            recipesArea.updateCanCraft();
            this.cachedInvChangeCount = this.client.player.getInventory().getTimesChanged();
//            this.cachedInvBlockChangeCount = this.craftingScreenHandler.getChangeCount();
        }
    }

    public void init(int height, int width, Font parenttextRenderer, Minecraft client, AlchemyTableScreenHandler craftingScreenHandler) {
        this.narrow = width < 379;
        this.parentWidth=width;
        this.parentHeight=height;
        this.craftingScreenHandler=craftingScreenHandler;
        this.parenttextRenderer=parenttextRenderer;
        this.client=client;
        assert this.client.player != null;
        this.cachedInvChangeCount = client.player.getInventory().getTimesChanged();
        assert this.client.player != null;
        this.recipeBook = this.client.player.getRecipeBook();
//        this.cachedInvBlockChangeCount = craftingScreenHandler.getChangeCount();



        if(listRecipes.isEmpty()){
            assert this.client.player != null;

            List<RecipeCollection> list= recipeBook.getCollection(RecipeBookCategories.UNKNOWN);

            for (RecipeCollection resultCollection: list){
                if(resultCollection.getRecipes().get(0).value() instanceof AlchemyTableRecipe recipe){

                    listRecipes.add((RecipeHolder<AlchemyTableRecipe>) resultCollection.getRecipes().get(0));

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

    public void drawBackground(GuiGraphics context, float delta, int mouseX, int mouseY) {
        this.leftOffset = this.narrow ? 0 : 86;
    }

    @Override
    public void render(@NotNull GuiGraphics context, int mouseX, int mouseY, float delta) {
        if (!this.open) {
            return;
        }
        context.pose().pushPose();
        context.pose().translate(0,0,100);
        int i = (parentWidth - 147) / 2 - this.leftOffset;
        int j = (parentHeight - 189) / 2;

        int color = 0x395026;

        switch (Objects.requireNonNull(currentTab).getCategory()){
            case POTIONS:
                context.drawString(this.parenttextRenderer, Component.translatable("gui.recipe-book.category.potions"), i+18,j+15, color,false);
                break;
            case DECOCTIONS:
                context.drawString(this.parenttextRenderer, Component.translatable("gui.recipe-book.category.decoctions"), i+18,j+15, color,false);
                break;
            case BOMBS_OILS:
                context.drawString(this.parenttextRenderer, Component.translatable("gui.recipe-book.category.bombs_oils"), i+18,j+15, color,false);
                break;
            case MISC:
                context.drawString(this.parenttextRenderer, Component.translatable("gui.recipe-book.category.misc"), i+18,j+15, color,false);
                break;

            default:
                break;
        }

        for (AlchemyRecipeGroupButton recipeGroupButtonWidget : this.tabButtons) {
            recipeGroupButtonWidget.render(context, mouseX, mouseY, delta);
        }
        context.pose().popPose();


        renderRecipeBookBackground(context, this.parentWidth, this.parentHeight, i, j);
        this.recipesArea.draw(context, i,j,mouseX,mouseY,delta);
    }
    public void renderRecipeBookBackground(GuiGraphics context, int width, int height, int i, int j){
        if (open) {
            context.pose().pushPose();
            context.pose().translate(0,0,0);
            //BookRender
            context.blit(RECIPE_GUI_TEXTURE,
                    i, j,
                    1, 1,
                    147, 189,
                    256, 300);
            context.pose().popPose();
        }
    }

    public void reset(){
        int i = (parentWidth - 147) / 2 - this.leftOffset;
        int j = (parentHeight - 189) / 2;

        if(this.tabButtons.isEmpty()){
            //Potions
            this.tabButtons.add(0,new AlchemyRecipeGroupButton(TCOTS_Items.DWARVEN_SPIRIT.get().getDefaultInstance(), AlchemyTableRecipeCategory.POTIONS));
            //Decoctions
            this.tabButtons.add(1,new AlchemyRecipeGroupButton(TCOTS_Items.GRAVE_HAG_DECOCTION.get().getDefaultInstance(), AlchemyTableRecipeCategory.DECOCTIONS));
            //Bombs
            this.tabButtons.add(2,new AlchemyRecipeGroupButton(TCOTS_Items.GRAPESHOT.get().getDefaultInstance(), AlchemyTableRecipeCategory.BOMBS_OILS));
            //Misc
            this.tabButtons.add(3,new AlchemyRecipeGroupButton(TCOTS_Items.AETHER.get().getDefaultInstance(), AlchemyTableRecipeCategory.MISC));
        }
        if(currentTab!=null) {currentTab.setStateTriggered(false);}
        this.currentTab = tabButtons.get(0);
        currentTab.setStateTriggered(true);

        this.recipesArea.initialize(this.client, i, j, craftingScreenHandler);

        listRecipes.sort(Comparator.comparing(recipeEntry -> recipeEntry.value().getOrder()));

        this.recipesArea.receiveRecipesList(listRecipes, this.recipeBook);

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

            if(recipesArea.recipe != null){
                assert this.client.gameMode != null;
                this.client.gameMode.handlePlaceRecipe(this.client.player.containerMenu.containerId, recipesArea.recipe, false);
            }
        }

        if(this.tabButtons.get(0).mouseClicked(mouseX, mouseY, button)){
            currentTab.setStateTriggered(false);
            currentTab = this.tabButtons.get(0);
            currentTab.setStateTriggered(true);
            this.recipesArea.setResults(true, currentTab.getCategory());
        }

        if(this.tabButtons.get(1).mouseClicked(mouseX, mouseY, button)){
            currentTab.setStateTriggered(false);
            currentTab = this.tabButtons.get(1);
            currentTab.setStateTriggered(true);
            this.recipesArea.setResults(true, currentTab.getCategory());
        }

        if(this.tabButtons.get(2).mouseClicked(mouseX, mouseY, button)){
            currentTab.setStateTriggered(false);
            currentTab = this.tabButtons.get(2);
            currentTab.setStateTriggered(true);
            this.recipesArea.setResults(true, currentTab.getCategory());
        }

        if(this.tabButtons.get(3).mouseClicked(mouseX, mouseY, button)){
            currentTab.setStateTriggered(false);
            currentTab = this.tabButtons.get(3);
            currentTab.setStateTriggered(true);
            this.recipesArea.setResults(true, currentTab.getCategory());
        }


        for (AlchemyRecipeGroupButton recipeGroupButtonWidget : this.tabButtons) {
            if (!recipeGroupButtonWidget.mouseClicked(mouseX, mouseY, button)) continue;
            if (this.currentTab != recipeGroupButtonWidget) {
                if (this.currentTab != null) {
                    this.currentTab.setStateTriggered(false);
                }
                this.currentTab = recipeGroupButtonWidget;
                this.currentTab.setStateTriggered(true);
            }
            return true;
        }
        return false;
    }

    @Override
    public NarratableEntry.@NotNull NarrationPriority narrationPriority() {
        return this.open ? NarratableEntry.NarrationPriority.HOVERED : NarratableEntry.NarrationPriority.NONE;
    }

    @Override
    public void updateNarration(@NotNull NarrationElementOutput builder) {
        ArrayList<AbstractWidget> list = Lists.newArrayList();
        this.recipesArea.forEachButton(button -> {
            if (button.isActive()) {
                list.add(button);
            }
        });

        Screen.NarratableSearchResult selectedElementNarrationData = Screen.findNarratableWidget(list, null);
        if (selectedElementNarrationData != null) {
            selectedElementNarrationData.entry.updateNarration(builder.nest());
        }
    }

    @Override
    public void addItemToSlot(@NotNull Ingredient input, int slot, int amount, int gridX, int gridY) {

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
        return GuiEventListener.super.keyReleased(keyCode, scanCode, modifiers);
    }

    private void refreshTabButtons() {
        int i = (this.parentWidth - 147) / 2 - this.leftOffset - 32;
        int j = (this.parentHeight - 189) / 2 + 3;
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
        return GuiEventListener.super.charTyped(chr, modifiers);
    }

    @Override
    public void recipesShown(@NotNull List<RecipeHolder<?>> recipes) {

    }
}
