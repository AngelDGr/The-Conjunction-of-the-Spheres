package mors.tcots.client.screen.recipebook;

import mors.tcots.TCOTS_Main;
import mors.tcots.registry.TCOTS_Items;
import mors.tcots.recipes.AlchemyTableRecipe;
import mors.tcots.recipes.AlchemyTableRecipeCategory;
import mors.tcots.client.screen.AlchemyTableScreenHandler;
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
    private Font parentTextRenderer;
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

    public void init(final int height, final int width, final Font parentTextRenderer, final Minecraft client, final AlchemyTableScreenHandler craftingScreenHandler) {
        this.narrow = width < 379;
        this.parentWidth=width;
        this.parentHeight=height;
        this.craftingScreenHandler=craftingScreenHandler;
        this.parentTextRenderer =parentTextRenderer;
        this.client=client;
        assert this.client.player != null;
        this.cachedInvChangeCount = client.player.getInventory().getTimesChanged();
        assert this.client.player != null;
        this.recipeBook = this.client.player.getRecipeBook();
//        this.cachedInvBlockChangeCount = craftingScreenHandler.getChangeCount();



        if(listRecipes.isEmpty()){
            assert this.client.player != null;

            final List<RecipeCollection> list= recipeBook.getCollection(RecipeBookCategories.UNKNOWN);

            for (final RecipeCollection resultCollection: list){
                if(resultCollection.getRecipes().getFirst().value() instanceof final AlchemyTableRecipe recipe){

                    listRecipes.add((RecipeHolder<AlchemyTableRecipe>) resultCollection.getRecipes().getFirst());

                }
            }

        }

        if (this.open) {
            this.reset();
        }
    }

    public int findLeftEdge(final int width, final int backgroundWidth) {
        return this.isOpen() && !this.narrow ? 177 + (width - backgroundWidth - 200) / 2 : (width - backgroundWidth) / 2;
    }

    public void toggleOpen() {
        this.setOpen(!this.isOpen());
    }

    public boolean isOpen() {
        return this.open;
    }

    public void drawBackground(final GuiGraphics context, final float delta, final int mouseX, final int mouseY) {
        this.leftOffset = this.narrow ? 0 : 86;
    }

    @Override
    public void render(@NotNull final GuiGraphics context, final int mouseX, final int mouseY, final float delta) {
        if (!this.open) {
            return;
        }
        context.pose().pushPose();
        context.pose().translate(0,0,100);
        final int i = (parentWidth - 147) / 2 - this.leftOffset;
        final int j = (parentHeight - 189) / 2;

        final int color = 0x395026;

        switch (Objects.requireNonNull(currentTab).getCategory()){
            case POTIONS:
                context.drawString(this.parentTextRenderer, Component.translatable("gui.tcots_witcher.recipe_book.category.potions"), i+18,j+15, color,false);
                break;
            case DECOCTIONS:
                context.drawString(this.parentTextRenderer, Component.translatable("gui.tcots_witcher.recipe_book.category.decoctions"), i+18,j+15, color,false);
                break;
            case BOMBS_OILS:
                context.drawString(this.parentTextRenderer, Component.translatable("gui.tcots_witcher.recipe_book.category.bombs_oils"), i+18,j+15, color,false);
                break;
            case MISC:
                context.drawString(this.parentTextRenderer, Component.translatable("gui.tcots_witcher.recipe_book.category.misc"), i+18,j+15, color,false);
                break;

            default:
                break;
        }

        for (final AlchemyRecipeGroupButton recipeGroupButtonWidget : this.tabButtons) {
            recipeGroupButtonWidget.render(context, mouseX, mouseY, delta);
        }
        context.pose().popPose();


        renderRecipeBookBackground(context, this.parentWidth, this.parentHeight, i, j);
        this.recipesArea.draw(context, i,j,mouseX,mouseY,delta);
    }
    public void renderRecipeBookBackground(final GuiGraphics context, final int width, final int height, final int i, final int j){
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
        final int i = (parentWidth - 147) / 2 - this.leftOffset;
        final int j = (parentHeight - 189) / 2;

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
        this.currentTab = tabButtons.getFirst();
        currentTab.setStateTriggered(true);

        this.recipesArea.initialize(this.client, i, j, craftingScreenHandler);

        listRecipes.sort(Comparator.comparing(recipeEntry -> recipeEntry.value().getOrder()));

        this.recipesArea.receiveRecipesList(listRecipes, this.recipeBook);

        this.recipesArea.setResults(false, currentTab.getCategory());

        this.refreshTabButtons();
    }

    protected void setOpen(final boolean opened) {
        if (opened) {
            this.reset();
        }
        this.open = opened;
    }

    @Override
    public void setFocused(final boolean focused) {

    }

    @Override
    public boolean isFocused() {
        return false;
    }

    @Override
    public boolean mouseClicked(final double mouseX, final double mouseY, final int button) {
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
            currentTab = this.tabButtons.getFirst();
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


        for (final AlchemyRecipeGroupButton recipeGroupButtonWidget : this.tabButtons) {
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
    public void updateNarration(@NotNull final NarrationElementOutput builder) {
        final ArrayList<AbstractWidget> list = Lists.newArrayList();
        this.recipesArea.forEachButton(button -> {
            if (button.isActive()) {
                list.add(button);
            }
        });

        final Screen.NarratableSearchResult selectedElementNarrationData = Screen.findNarratableWidget(list, null);
        if (selectedElementNarrationData != null) {
            selectedElementNarrationData.entry.updateNarration(builder.nest());
        }
    }

    @Override
    public void addItemToSlot(@NotNull final Ingredient input, final int slot, final int amount, final int gridX, final int gridY) {

    }

    @Override
    public boolean keyPressed(final int keyCode, final int scanCode, final int modifiers) {
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
    public boolean keyReleased(final int keyCode, final int scanCode, final int modifiers) {
        this.searching = false;
        return GuiEventListener.super.keyReleased(keyCode, scanCode, modifiers);
    }

    private void refreshTabButtons() {
        final int i = (this.parentWidth - 147) / 2 - this.leftOffset - 32;
        final int j = (this.parentHeight - 189) / 2 + 3;
        int l = 0;
        for (final AlchemyRecipeGroupButton recipeGroupButtonWidget : this.tabButtons) {
            recipeGroupButtonWidget.setPosition(i, j + 14 + (l++ * 28));
            recipeGroupButtonWidget.checkForNewRecipes(this.client);
        }
    }

    @Override
    public boolean charTyped(final char chr, final int modifiers) {
        if (this.searching) {
            return false;
        }
        if (!this.isOpen() || Objects.requireNonNull(this.client.player).isSpectator()) {
            return false;
        }
        return GuiEventListener.super.charTyped(chr, modifiers);
    }

    @Override
    public void recipesShown(@NotNull final List<RecipeHolder<?>> recipes) {

    }
}
