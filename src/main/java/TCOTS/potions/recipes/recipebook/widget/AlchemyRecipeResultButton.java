package TCOTS.potions.recipes.recipebook.widget;

import TCOTS.potions.recipes.AlchemyTableRecipe;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.screen.recipebook.RecipeBookResults;
import net.minecraft.client.gui.screen.recipebook.RecipeResultCollection;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.book.RecipeBook;
import net.minecraft.screen.AbstractRecipeScreenHandler;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class AlchemyRecipeResultButton extends ClickableWidget {


    private static final float field_32414 = 15.0f;
    private static final int field_32415 = 25;
    public static final int field_32413 = 30;
    private static final Text MORE_RECIPES_TEXT = Text.translatable("gui.recipebook.moreRecipes");
    private AbstractRecipeScreenHandler<?> craftingScreenHandler;
    private AlchemyTableRecipe recipe;
    private RecipeBook recipeBook;
    private RecipeResultCollection resultCollection;
    private float time;
    private float bounce;
    private int currentResultIndex;

    private TextRenderer textRenderer;
    public AlchemyRecipeResultButton() {
        super(0,0,134, 22, ScreenTexts.EMPTY);
    }

    int j = 173;
    public void showResultCollection(RecipeResultCollection resultCollection, AlchemyRecipeBookResults results) {
        this.resultCollection = resultCollection;
        this.craftingScreenHandler = (AbstractRecipeScreenHandler)results.getClient().player.currentScreenHandler;
        this.recipeBook = results.getRecipeBook();
        List<Recipe<?>> list = resultCollection.getResults(this.recipeBook.isFilteringCraftable(this.craftingScreenHandler));
        for (Recipe<?> recipe : list) {
            if (!this.recipeBook.shouldDisplay(recipe)) continue;
            results.onRecipesDisplayed(list);
            this.bounce = 15.0f;
            break;
        }
    }



    public void receiveRecipe(AlchemyTableRecipe recipe){
        this.recipe=recipe;
    }

    public void receiveTextRenderer(TextRenderer renderer){
        this.textRenderer=renderer;
    }

    @Override
    protected void renderButton(DrawContext context, int mouseX, int mouseY, float delta) {
//        RenderSystem.disableDepthTest();
        if(recipe == null){
            return;
        }

        if (this.isSelected()) {
            j = 173 + 23;
            context.drawTooltip(textRenderer, Text.translatable("tcots-witcher.tooltip.gui.formula", recipe.getOutput(null).getName().getString()), this.getX()-20, this.getY());
        }
        else{
            j = 173;
        }

        context.drawTexture(AlchemyRecipeBookWidget.RECIPE_GUI_TEXTURE,
                this.getX(), this.getY(),
                32, j,
                134, 22);
        //Recipes Render
                    //DrawOutput
                    context.drawItemWithoutEntity(recipe.getOutput(null),
                            this.getX() + 3,
                            this.getY() + 3);

                    //Draw ingredients
                    for (int l = 0; l < recipe.getIngredients().size(); l++) {
                        ItemStack stack = recipe.getIngredients().get(l).getMatchingStacks()[0];
                        int number = recipe.getIngredientsCounts()[l];
                        switch (l) {
                            case 0:
                                //Draw ingredient Item
                                context.drawItemWithoutEntity(stack, this.getX() + 62, this.getY() + 3);

                                //Draw ingredient number
                                if(number>1){
                                    context.getMatrices().push();
                                    context.getMatrices().translate(0,0,200);
                                    context.drawText(textRenderer, String.valueOf(number), this.getX() + 62, this.getY() + 11, 0xffffff, true);
                                    context.getMatrices().pop();}
                                break;
                            case 1:
                                context.drawItemWithoutEntity(stack, this.getX() + 43, this.getY() + 3);

                                //Draw ingredient number
                                if(number>1){
                                    context.getMatrices().push();
                                    context.getMatrices().translate(0,0,200);
                                    context.drawText(textRenderer, String.valueOf(number), this.getX() + 43, this.getY() + 11, 0xffffff, true);
                                    context.getMatrices().pop();}
                                break;
                            case 2:
                                context.drawItemWithoutEntity(stack, this.getX() + 81, this.getY() + 3);

                                //Draw ingredient number
                                if(number>1){
                                    context.getMatrices().push();
                                    context.getMatrices().translate(0,0,200);
                                    context.drawText(textRenderer, String.valueOf(number), this.getX() + 81, this.getY() + 11, 0xffffff, true);
                                    context.getMatrices().pop();}
                                break;
                            case 3:
                                context.drawItemWithoutEntity(stack, this.getX() + 24, this.getY() + 3);

                                //Draw ingredient number
                                if(number>1){
                                    context.getMatrices().push();
                                    context.getMatrices().translate(0,0,200);
                                    context.drawText(textRenderer, String.valueOf(number), this.getX() + 24, this.getY() + 11, 0xffffff, true);
                                    context.getMatrices().pop();}
                                break;
                            case 4:
                                context.drawItemWithoutEntity(stack, this.getX() + 100, this.getY() + 3);

                                //Draw ingredient number
                                if(number>1){
                                    context.getMatrices().push();
                                    context.getMatrices().translate(0,0,200);
                                    context.drawText(textRenderer, String.valueOf(number), this.getX() + 100, this.getY() + 11, 0xffffff, true);
                                    context.getMatrices().pop();}
                                break;

                            default:
                                break;
                        }
                    }

                    //Draw base
                    context.drawItemWithoutEntity(recipe.getBaseItem(), this.getX() + 117, this.getY() + 3);

//        RenderSystem.enableDepthTest();
//        boolean bl;
//        if (!Screen.hasControlDown()) {
//            this.time += delta;
//        }
//        MinecraftClient minecraftClient = MinecraftClient.getInstance();
//        int i = 29;
//        if (!this.resultCollection.hasCraftableRecipes()) {
//            i += 25;
//        }
//        int j = 206;
//        if (this.resultCollection.getResults(this.recipeBook.isFilteringCraftable(this.craftingScreenHandler)).size() > 1) {
//            j += 25;
//        }
//        boolean bl2 = bl = this.bounce > 0.0f;
//        if (bl) {
//            float f = 1.0f + 0.1f * (float)Math.sin(this.bounce / 15.0f * (float)Math.PI);
//            context.getMatrices().push();
//            context.getMatrices().translate(this.getX() + 8, this.getY() + 12, 0.0f);
//            context.getMatrices().scale(f, f, 1.0f);
//            context.getMatrices().translate(-(this.getX() + 8), -(this.getY() + 12), 0.0f);
//            this.bounce -= delta;
//        }
//        context.drawTexture(AlchemyRecipeBookWidget.RECIPE_GUI_TEXTURE, this.getX(), this.getY(), i, j, this.width, this.height);
//        List<Recipe<?>> list = this.getResults();
//        this.currentResultIndex = MathHelper.floor(this.time / 30.0f) % list.size();
//        ItemStack itemStack = list.get(this.currentResultIndex).getOutput(this.resultCollection.getRegistryManager());
//        int k = 4;
//        if (this.resultCollection.hasSingleOutput() && this.getResults().size() > 1) {
//            context.drawItem(itemStack, this.getX() + k + 1, this.getY() + k + 1, 0, 10);
//            --k;
//        }
//        context.drawItemWithoutEntity(itemStack, this.getX() + k, this.getY() + k);
//        if (bl) {
//            context.getMatrices().pop();
//        }
    }

    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder builder) {

    }

    private List<Recipe<?>> getResults() {
        List<Recipe<?>> list = this.resultCollection.getRecipes(true);
        if (!this.recipeBook.isFilteringCraftable(this.craftingScreenHandler)) {
            list.addAll(this.resultCollection.getRecipes(false));
        }
        return list;
    }

    @Override
    protected boolean isValidClickButton(int button) {
        return button == 0 || button == 1;
    }



    @Override
    public int getWidth() {
        return 134;
    }
}
