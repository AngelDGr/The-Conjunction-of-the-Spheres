package TCOTS.potions.screen;

import TCOTS.TCOTS_Main;
import TCOTS.potions.recipes.AlchemyTableRecipe;
import TCOTS.potions.recipes.recipebook.RecipeAlchemyResultCollection;
import TCOTS.potions.recipes.recipebook.widget.AlchemyRecipeBookButton;
import TCOTS.potions.recipes.recipebook.widget.AlchemyRecipeBookButtonTextured;
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
import org.spongepowered.include.com.google.common.collect.Lists;

import java.util.*;

@Environment(EnvType.CLIENT)
public class AlchemyTableScreen extends HandledScreen<AlchemyTableScreenHandler> {

    private static final Identifier TEXTURE =
            new Identifier(TCOTS_Main.MOD_ID, "textures/gui/alchemy_table.png");
    private static final Identifier RECIPE_BUTTON_TEXTURE = new Identifier(TCOTS_Main.MOD_ID,"textures/gui/alchemy_recipe_button.png");
    private static final Identifier RECIPE_GUI_TEXTURE = new Identifier(TCOTS_Main.MOD_ID,"textures/gui/alchemy_recipe_book.png");
    private boolean open=false;
    private boolean narrow;
    private int leftOffset;
    public AlchemyRecipeBookButton recipeBookButton;
    private final RecipeMatcher recipeMatcher = new RecipeMatcher();
    private final List<AlchemyTableRecipe> listRecipes = new ArrayList<>();

    public AlchemyTableScreen(AlchemyTableScreenHandler handler,  PlayerInventory inventory, Text title) {
        super(handler, inventory, title);
//        this.background=TEXTURE;
    }

    @Override
    protected void init() {
        this.narrow = this.width < 379;
        super.init();
        recipeBookButton = new AlchemyRecipeBookButtonTextured(this.x + 5, this.height / 2 - 49, 20, 18, 0, 0, 19, RECIPE_BUTTON_TEXTURE,
                //Action when press
                button -> {
//            System.out.println("You clicked button1!");
//                    List<RecipeAlchemyResultCollection> list = this.recipeBook.getResultsForGroup(this.currentTab.getCategory());
//                    List<RecipeAlchemyResultCollection> list = Lists.newArrayList();
//                    System.out.println("RecipesList: "+list);
                    this.OnButtonPress();
//                    System.out.println("WidthN: "+this.width);
//                    System.out.println("HeightN: "+this.height);
//                    System.out.println("WidthB: "+this.backgroundWidth);
//                    System.out.println("HeightB: "+this.backgroundHeight);

                    this.x = this.findLeftEdge(this.width, this.backgroundWidth);
                    button.setPosition(this.x + 5, this.height / 2 - 49);
                });

        addDrawableChild(recipeBookButton);
        titleX = (backgroundWidth - textRenderer.getWidth(title)) / 2;
    }

    public int findLeftEdge(int width, int backgroundWidth) {
        int i = this.isOpen() && !this.narrow ? 177 + (width - backgroundWidth - 200) / 2 : (width - backgroundWidth) / 2;
        return i;
    }

    public void OnButtonPress(){
        assert client != null;
        assert client.player != null;
        if(listRecipes.isEmpty()){
            List<RecipeResultCollection> list= client.player.getRecipeBook().getResultsForGroup(RecipeBookGroup.UNKNOWN);
            list.forEach(
                    recipeResultCollection -> {
                        if(recipeResultCollection.getAllRecipes().get(0).getType() == AlchemyTableRecipe.Type.INSTANCE){

                            listRecipes.add((AlchemyTableRecipe) recipeResultCollection.getAllRecipes().get(0));
                        }
                    }
            );
//            listRecipes.forEach(
//                    alchemyTableRecipe -> {
//                        System.out.println(alchemyTableRecipe.getId());
//                    }
//            );
        }

        open= !open;
    }

    public boolean isOpen() {
        return this.open;
    }


    @Override
    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        this.leftOffset = this.narrow ? 0 : 86;
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
        renderRecipeBook(context, x, y);
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

    private void renderRecipeBook(DrawContext context, int x, int y){
        if (open) {
            int i = (this.width - 147) / 2 - this.leftOffset;
            int j = (this.height - 166) / 2;

            //BookRender
            context.drawTexture(RECIPE_GUI_TEXTURE,
                    i, j,
                    1, 1,
                    147, 166);

            //Recipes Render
            if (!listRecipes.isEmpty()) {

                for (int r = 0; r < 5; r++) {

                    //Draw container texture
                    context.drawTexture(RECIPE_GUI_TEXTURE,

                            i + 14, (j + 26) + (r * 24),

                            32, 173,

                            134, 22);

                    //DrawOutput
                    context.drawItem(listRecipes.get(r).getOutput(null), i + 17, (j + 29) + (r * 24));

                    //Draw ingredients
                    for (int l = 0; l < listRecipes.get(r).getIngredients().size(); l++) {

                        ItemStack stack = listRecipes.get(r).getIngredients().get(l).getMatchingStacks()[0];
                        int number = listRecipes.get(r).getIngredientsCounts()[l];
                        switch (l) {
                            case 0:
                                //Draw ingredient Item
                                context.drawItem(stack, i + 76, (j + 29) + (r * 24));

                                //Draw ingredient number
                                if(number>1){
                                    context.getMatrices().push();
                                    context.getMatrices().translate(0,0,200);
                                    context.drawText(this.textRenderer, String.valueOf(number), i + 76, (j + 37) + (r * 24), 0xffffff, true);
                                    context.getMatrices().pop();}
                                break;
                            case 1:
                                context.drawItem(stack, i + 57, (j + 29) + (r * 24));

                                //Draw ingredient number
                                if(number>1){
                                    context.getMatrices().push();
                                    context.getMatrices().translate(0,0,200);
                                    context.drawText(this.textRenderer, String.valueOf(number), i + 57, (j + 37) + (r * 24), 0xffffff, true);
                                    context.getMatrices().pop();}
                                break;
                            case 2:
                                context.drawItem(stack, i + 95, (j + 29) + (r * 24));

                                //Draw ingredient number
                                if(number>1){
                                    context.getMatrices().push();
                                    context.getMatrices().translate(0,0,200);
                                    context.drawText(this.textRenderer, String.valueOf(number), i + 95, (j + 37) + (r * 24), 0xffffff, true);
                                    context.getMatrices().pop();}
                                break;
                            case 3:
                                context.drawItem(stack, i + 36, (j + 29) + (r * 24));

                                //Draw ingredient number
                                if(number>1){
                                    context.getMatrices().push();
                                    context.getMatrices().translate(0,0,200);
                                    context.drawText(this.textRenderer, String.valueOf(number), i + 36, (j + 37) + (r * 24), 0xffffff, true);
                                    context.getMatrices().pop();}
                                break;
                            case 4:
                                context.drawItem(stack, i + 116, (j + 29) + (r * 24));

                                //Draw ingredient number
                                if(number>1){
                                    context.getMatrices().push();
                                    context.getMatrices().translate(0,0,200);
                                    context.drawText(this.textRenderer, String.valueOf(number), i + 116, (j + 37) + (r * 24), 0xffffff, true);
                                    context.getMatrices().pop();}
                                break;
                        }
                    }

                    //Draw base
                    context.drawItem(listRecipes.get(r).getBaseItem(), i + 132, (j + 29) + (r * 24));

                }
            }
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        renderBackground(context);
        super.render(context, mouseX, mouseY, delta);
        drawMouseoverTooltip(context, mouseX, mouseY);
    }
}
