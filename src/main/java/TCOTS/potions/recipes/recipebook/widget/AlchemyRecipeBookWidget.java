package TCOTS.potions.recipes.recipebook.widget;

import TCOTS.TCOTS_Main;
import TCOTS.items.TCOTS_Items;
import TCOTS.potions.recipes.AlchemyTableRecipe;
import TCOTS.potions.recipes.recipebook.RecipeAlchemyResultCollection;
import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.objects.ObjectLinkedOpenHashSet;
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
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.client.gui.widget.ToggleButtonWidget;
import net.minecraft.client.recipebook.ClientRecipeBook;
import net.minecraft.client.recipebook.RecipeBookGroup;
import net.minecraft.client.resource.language.LanguageDefinition;
import net.minecraft.client.resource.language.LanguageManager;
import net.minecraft.client.search.SearchManager;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.RecipeCategoryOptionsC2SPacket;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeGridAligner;
import net.minecraft.recipe.RecipeMatcher;
import net.minecraft.recipe.book.RecipeBookCategory;
import net.minecraft.screen.AbstractRecipeScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

@Environment(value= EnvType.CLIENT)
public class AlchemyRecipeBookWidget implements RecipeGridAligner<Ingredient>,
        Drawable,
        Element,
        Selectable,
        RecipeDisplayListener {
    public static final Identifier RECIPE_GUI_TEXTURE = new Identifier(TCOTS_Main.MOD_ID,"textures/gui/alchemy_recipe_book.png");
    private static final Text SEARCH_HINT_TEXT = Text.translatable("gui.recipebook.search_hint").formatted(Formatting.ITALIC).formatted(Formatting.GRAY);
    @Nullable
    private TextFieldWidget searchField;
    private String searchText = "";
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



    public void init(int height, int width, TextRenderer parenttextRenderer, MinecraftClient client) {
        int i = (parentWidth - 147) / 2 - this.leftOffset;
        int j = (parentHeight - 166) / 2;
        this.narrow = width < 379;
        this.parentWidth=width;
        this.parentHeight=height;
        this.parenttextRenderer=parenttextRenderer;
        this.client=client;

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
//        this.recipesArea.setResults(list2, resetCurrentPage);
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
        this.searchField.render(context, mouseX, mouseY, delta);
        int i = (parentWidth - 147) / 2 - this.leftOffset;
        int j = (parentHeight - 166) / 2;
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

        this.searchField = new TextFieldWidget(this.client.textRenderer, i + 30, j + 11, 79, this.client.textRenderer.fontHeight + 3, Text.translatable("itemGroup.search"));
        String string = this.searchField != null ? this.searchField.getText() : "";
        this.searchField.setMaxLength(50);
        this.searchField.setVisible(true);
        this.searchField.setEditableColor(0xFFFFFF);
        this.searchField.setText(string);
        this.searchField.setPlaceholder(SEARCH_HINT_TEXT);

        if(this.tabButtons.isEmpty()){
            this.tabButtons.add(new AlchemyRecipeGroupButton(TCOTS_Items.DWARVEN_SPIRIT.getDefaultStack()));
            this.tabButtons.add(new AlchemyRecipeGroupButton(Items.GUNPOWDER.getDefaultStack()));
//            this.tabButtons.add(new AlchemyRecipeGroupButton());

        }

        this.recipesArea.initialize(this.client, i, j);
        this.recipesArea.sendList(listRecipes);
        this.recipesArea.setResults(false);

        this.refreshTabButtons();
    }

    protected void setOpen(boolean opened) {
        if (opened) {
            this.reset();
        }
        this.open = opened;
//        this.recipeBook.setGuiOpen(this.craftingScreenHandler.getCategory(), opened);
//        if (!opened) {
//            this.recipesArea.hideAlternates();
//        }
//        this.sendBookDataPacket();
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
//        assert this.searchField != null;
//        if (this.searchField.mouseClicked(mouseX, mouseY, button)) {
//            this.searchField.setFocused(true);
//            return true;
//        }
        if (!this.isOpen() || this.client.player.isSpectator()) {
            return false;
        }

        if(this.recipesArea.mouseClicked(mouseX, mouseY, button,
                (this.parentWidth - 147) / 2 - this.leftOffset,
                (this.parentHeight - 166) / 2,
                147, 166)){
//            System.out.println("Pressed");
        }


//        assert this.searchField != null;
//        this.searchField.setFocused(false);
        for (AlchemyRecipeGroupButton recipeGroupButtonWidget : this.tabButtons) {
            if (!recipeGroupButtonWidget.mouseClicked(mouseX, mouseY, button)) continue;
            if (this.currentTab != recipeGroupButtonWidget) {
                if (this.currentTab != null) {
                    this.currentTab.setToggled(false);
                }
                this.currentTab = recipeGroupButtonWidget;
                this.currentTab.setToggled(true);
//                this.refreshResults(true);
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
        list.add(this.searchField);
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
        if (this.searchField.keyPressed(keyCode, scanCode, modifiers)) {
//            this.refreshSearchResults();
            return true;
        }
        if (this.searchField.isFocused() && this.searchField.isVisible() && keyCode != GLFW.GLFW_KEY_ESCAPE) {
            return true;
        }
        if (this.client.options.chatKey.matchesKey(keyCode, scanCode) && !this.searchField.isFocused()) {
            this.searching = true;
            this.searchField.setFocused(true);
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
//            RecipeBookGroup recipeBookGroup = recipeGroupButtonWidget.getCategory();
//            if (recipeBookGroup == RecipeBookGroup.CRAFTING_SEARCH || recipeBookGroup == RecipeBookGroup.FURNACE_SEARCH) {
//                recipeGroupButtonWidget.visible = true;
//                recipeGroupButtonWidget.setPosition(i, j + 27 * l++);
//                continue;
//            }
//            if (!recipeGroupButtonWidget.hasKnownRecipes(this.recipeBook)) continue;

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
        if (this.searchField.charTyped(chr, modifiers)) {
//            this.refreshSearchResults();
            return true;
        }
        return Element.super.charTyped(chr, modifiers);
    }

}
