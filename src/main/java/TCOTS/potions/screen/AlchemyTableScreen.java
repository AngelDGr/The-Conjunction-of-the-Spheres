package TCOTS.potions.screen;

import TCOTS.TCOTS_Main;
import TCOTS.potions.recipes.AlchemyTableRecipe;
import TCOTS.potions.recipes.recipebook.RecipeAlchemyResultCollection;
import TCOTS.potions.recipes.recipebook.widget.AlchemyRecipeBookButton;
import TCOTS.potions.recipes.recipebook.widget.AlchemyRecipeBookButtonTextured;
import TCOTS.potions.recipes.recipebook.widget.AlchemyRecipeBookResults;
import TCOTS.potions.recipes.recipebook.widget.AlchemyRecipeBookWidget;
import com.google.common.collect.Lists;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.font.FontManager;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.client.gui.screen.recipebook.*;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.client.recipebook.RecipeBookGroup;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeMatcher;
import net.minecraft.screen.AbstractRecipeScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.*;

@Environment(EnvType.CLIENT)
public class AlchemyTableScreen extends HandledScreen<AlchemyTableScreenHandler> {

    private static final Identifier TEXTURE =
            new Identifier(TCOTS_Main.MOD_ID, "textures/gui/alchemy_table.png");
    private static final Identifier RECIPE_BUTTON_TEXTURE = new Identifier(TCOTS_Main.MOD_ID,"textures/gui/alchemy_recipe_button.png");

    private final AlchemyRecipeBookWidget recipeBook = new AlchemyRecipeBookWidget();
    private boolean narrow;

    public AlchemyTableScreen(AlchemyTableScreenHandler handler,  PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
//        this.background=TEXTURE;
    }

    @Override
    protected void init() {
        this.narrow = this.width < 379;
        super.init();
        recipeBook.init(this.height,this.width, this.textRenderer, this.client);
        addDrawableChild(new AlchemyRecipeBookButtonTextured(this.x + 5, this.height / 2 - 49, 20, 18, 0, 0, 19, RECIPE_BUTTON_TEXTURE,
                //Action when press
                button -> {
                    recipeBook.toggleOpen();
                    this.x = recipeBook.findLeftEdge(this.width, this.backgroundWidth);
                    button.setPosition(this.x + 5, this.height / 2 - 49);
                }));

        this.addSelectableChild(this.recipeBook);
        this.setInitialFocus(this.recipeBook);
        titleX = (backgroundWidth - textRenderer.getWidth(title)) / 2;
    }




    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        recipeBook.drawBackground(context, delta, mouseX, mouseY);
//        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
//        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
//        RenderSystem.setShaderTexture(0, TEXTURE);
//        int x = (width - backgroundWidth) / 2;
//        int y = (height - backgroundHeight) / 2;
//
//        context.drawTexture(TEXTURE, x, y, 0, 0, backgroundWidth, backgroundHeight);

        int i = this.x;
        int j =(this.height - this.backgroundHeight) / 2;

        context.drawTexture(TEXTURE, i, j, 0, 0, this.backgroundWidth, this.backgroundHeight);

        renderProgressArrow(context, x, y);
        recipeBook.render(context, mouseX, mouseY, delta);
//        renderRecipeBook(context, x, y);
    }


    private void renderProgressArrow(DrawContext context, int x, int y) {
        if(handler.isCrafting()) {
            context.drawTexture(TEXTURE,

                    //Top left where it will draw
                    x + 145, y + 16,

                    //Position where it is the texture
                    176, 0,

                    //Size of the texture
                    8, handler.getScaledProgress());
        }
    }


    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context);
        super.render(context, mouseX, mouseY, delta);
        drawMouseoverTooltip(context, mouseX, mouseY);
    }
}
