package TCOTS.screen.recipebook.widget;

import TCOTS.items.concoctions.*;
import TCOTS.items.concoctions.recipes.AlchemyTableRecipe;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.book.RecipeBook;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class AlchemyRecipeResultButton extends ClickableWidget {

    @Nullable
    private AlchemyTableRecipe recipe;

    @Nullable
    private RecipeEntry<AlchemyTableRecipe> recipeEntry;

    private TextRenderer textRenderer;
    private boolean craftable;
    private boolean playerHasRecipe;
    public AlchemyRecipeResultButton() {
        super(0,0,134, 22, ScreenTexts.EMPTY);
        craftable=false;
    }

    int j = 193;

    public void receiveRecipe(RecipeEntry<AlchemyTableRecipe> recipe, RecipeBook recipeBook){

        this.playerHasRecipe = recipeBook.contains(recipe);

        this.recipeEntry=recipe;
        if(recipe != null){
        this.recipe=recipe.value();}
        else {
            this.recipe=null;
        }
    }

    public void receiveTextRenderer(TextRenderer renderer){
        this.textRenderer=renderer;
    }

    public void setCraftable(boolean craftable){
        this.craftable=craftable;
    }

    public boolean getCraftable(){
        return this.craftable;
    }

    public @Nullable AlchemyTableRecipe getRecipe(){
        return this.recipe;
    }

    public @Nullable RecipeEntry<AlchemyTableRecipe> getRecipeEntry(){
        return this.recipeEntry;
    }

    int textColor1=0xb43d2c;
    int textColor2=0xb43d2c;
    int textColor3=0xb43d2c;
    int textColor4=0xb43d2c;
    int textColor5=0xb43d2c;
    int textColorBase=0xb43d2c;
    public void setTextColor(int index, boolean colorWhite){
        switch (index){
            case 0:
                textColor1 = colorWhite ? 0xffffff: 0xb43d2c;
                break;
            case 1:
                textColor2 = colorWhite ? 0xffffff: 0xb43d2c;
                break;
            case 2:
                textColor3 = colorWhite ? 0xffffff: 0xb43d2c;
                break;
            case 3:
                textColor4 = colorWhite ? 0xffffff: 0xb43d2c;
                break;
            case 4:
                textColor5 = colorWhite ? 0xffffff: 0xb43d2c;
                break;
            default:
                break;
        }
    }

    boolean baseNotPresent=false;

    public void setBaseNotPresent(boolean baseNotPresent) {
        this.baseNotPresent = baseNotPresent;
    }

    private void drawNotBase(DrawContext context){
        context.getMatrices().push();
        context.getMatrices().translate(0,0,101);
        context.drawTexture(
                AlchemyRecipeBookWidget.RECIPE_GUI_TEXTURE,
                this.getX()+117, this.getY()+2,
                168, 241,
                17, 18,
                256,300);
        context.getMatrices().pop();
    }

    @Override
    protected void renderWidget(DrawContext context, int mouseX, int mouseY, float delta) {
        if(recipeEntry == null || recipe == null){
            return;
        }

        if (this.isSelected() && playerHasRecipe) {
            //If craftable puts the green selected color
            if(craftable) {j = 193 + 23;}
            else {
                j = 193+23+23;
            }

            Formatting textColor;

            if(this.recipe.getResult(null).getItem() instanceof WitcherPotions_Base && !(this.recipe.getResult(null).getItem() instanceof WitcherAlcohol_Base) && !(this.recipe.getResult(null).getItem() instanceof WitcherWhiteHoney)){
                List<Text> list= new ArrayList<>();

                if(((WitcherPotions_Base) this.recipe.getResult(null).getItem()).getStatusEffect().getAmplifier() > 0){
                    textColor = Formatting.YELLOW;
                } else {
                    textColor = Formatting.WHITE;
                }

                int tooltipY;
                //Name
                if((((WitcherPotions_Base) this.recipe.getResult(null).getItem()).isDecoction())){
                    tooltipY=12;
                    list.add(Text.translatable("tcots-witcher.tooltip.gui.formula", recipe.getResult(null).getName().getString()).withColor(0x41d331));
                }else {
                    tooltipY=22;
                    list.add(Text.translatable("tcots-witcher.tooltip.gui.formula", recipe.getResult(null).getName().getString()).formatted(textColor));
                }

                //Toxicity
                int tox = ((WitcherPotions_Base) this.recipe.getResult(null).getItem()).getToxicity();
                list.add(Text.translatable("tcots-witcher.tooltip.toxicity", tox).formatted(Formatting.DARK_GREEN));

                //Stack
                int maxCount = this.recipe.getResult(null).getMaxCount();
                if(!(((WitcherPotions_Base) this.recipe.getResult(null).getItem()).isDecoction())){
                    list.add(Text.translatable("tcots-witcher.tooltip.max_stack", maxCount).formatted(Formatting.DARK_BLUE));
                }


                context.drawTooltip(textRenderer, list, this.getX()-20, this.getY()-tooltipY);

            } else if (this.recipe.getResult(null).getItem() instanceof WitcherMonsterOil_Base) {

                if(((WitcherMonsterOil_Base) this.recipe.getResult(null).getItem()).getLevel() > 1){
                    textColor = Formatting.YELLOW;
                } else {
                    textColor = Formatting.WHITE;
                }

                List<Text> list= new ArrayList<>();
                //Name
                list.add(Text.translatable("tcots-witcher.tooltip.gui.formula", recipe.getResult(null).getName().getString()).formatted(textColor));

                //Damage
                int damage = ((WitcherMonsterOil_Base) this.recipe.getResult(null).getItem()).getLevel() * 2;
                list.add(Text.translatable("tcots-witcher.tooltip.gui.oil_damage", damage).formatted(Formatting.RED));

                //Uses
                int uses = ((WitcherMonsterOil_Base) this.recipe.getResult(null).getItem()).getUses();
                list.add(Text.translatable("tcots-witcher.tooltip.gui.oil_uses", uses).formatted(Formatting.DARK_BLUE));
                context.drawTooltip(textRenderer, list, this.getX()-20, this.getY()-22);
            } else if (this.recipe.getResult(null).getItem() instanceof WitcherWhiteHoney) {

                List<Text> list= new ArrayList<>();
                list.add(Text.translatable("tcots-witcher.tooltip.gui.formula", recipe.getResult(null).getName().getString()));
                //Stack
                int maxCount = this.recipe.getResult(null).getMaxCount();

                list.add(Text.translatable("tcots-witcher.tooltip.max_stack", maxCount).formatted(Formatting.DARK_BLUE));

                context.drawTooltip(textRenderer, list, this.getX()-20, this.getY()-12);

            } else if (this.recipe.getResult(null).getItem() instanceof WitcherBombs_Base) {
                if(((WitcherBombs_Base) this.recipe.getResult(null).getItem()).getLevel() > 0){
                    textColor = Formatting.YELLOW;
                } else {
                    textColor = Formatting.WHITE;
                }

                List<Text> list= new ArrayList<>();
                //Name
                list.add(Text.translatable("tcots-witcher.tooltip.gui.formula", recipe.getResult(null).getName().getString()).formatted(textColor));

                //Stack
                int maxCount = this.recipe.getResult(null).getMaxCount();

                list.add(Text.translatable("tcots-witcher.tooltip.max_stack", maxCount).formatted(Formatting.DARK_BLUE));

                context.drawTooltip(textRenderer, list, this.getX()-20, this.getY()-12);

            } else {
                context.drawTooltip(textRenderer, Text.translatable("tcots-witcher.tooltip.gui.formula", recipe.getResult(null).getName().getString()), this.getX()-20, this.getY());
            }
        }
        else{
            //If not craftable, just put the button in red
            if(!playerHasRecipe){
                j = 193+23+23+23;
            }
            else if(!craftable){
                j = 193+23+23;
            }
            else {
                j = 193;
            }
        }


        context.drawTexture(AlchemyRecipeBookWidget.RECIPE_GUI_TEXTURE,
                this.getX(), this.getY(),
                32, j,
                134, 22,
                256,300);

        //Recipes Items Draw
        if(playerHasRecipe)
        {
            if (!baseNotPresent) {
                drawNotBase(context);
            }

            //DrawOutput
            context.drawItemWithoutEntity(recipe.getResult(null),
                    this.getX() + 3,
                    this.getY() + 3);

            int resultCount = recipe.getResult(null).getCount();

            //Draw result count
            if (resultCount > 1) {
                context.getMatrices().push();
                context.getMatrices().translate(0, 0, 200);
                context.drawText(textRenderer, String.valueOf(resultCount), this.getX() + 3, this.getY() + 11, 0xffffff, true);
                context.getMatrices().pop();
            }

            //Draw ingredients
            for (int l = 0; l < recipe.getIngredients().size(); l++) {
                ItemStack stack = recipe.getIngredients().get(l).getMatchingStacks()[0];
                int number = recipe.getIngredientsCounts().get(l);
                switch (l) {
                    case 0:
                        //Draw ingredient Item
                        context.drawItemWithoutEntity(stack, this.getX() + 62, this.getY() + 3);

                        //Draw ingredient number
                        if (number > 1) {
                            context.getMatrices().push();
                            context.getMatrices().translate(0, 0, 200);
                            context.drawText(textRenderer, String.valueOf(number), this.getX() + 62, this.getY() + 11, textColor1, true);
                            context.getMatrices().pop();
                        }
                        break;
                    case 1:
                        context.drawItemWithoutEntity(stack, this.getX() + 43, this.getY() + 3);

                        //Draw ingredient number
                        if (number > 1) {
                            context.getMatrices().push();
                            context.getMatrices().translate(0, 0, 200);
                            context.drawText(textRenderer, String.valueOf(number), this.getX() + 43, this.getY() + 11, textColor2, true);
                            context.getMatrices().pop();
                        }
                        break;
                    case 2:
                        context.drawItemWithoutEntity(stack, this.getX() + 81, this.getY() + 3);

                        //Draw ingredient number
                        if (number > 1) {
                            context.getMatrices().push();
                            context.getMatrices().translate(0, 0, 200);
                            context.drawText(textRenderer, String.valueOf(number), this.getX() + 81, this.getY() + 11, textColor3, true);
                            context.getMatrices().pop();
                        }
                        break;
                    case 3:
                        context.drawItemWithoutEntity(stack, this.getX() + 24, this.getY() + 3);

                        //Draw ingredient number
                        if (number > 1) {
                            context.getMatrices().push();
                            context.getMatrices().translate(0, 0, 200);
                            context.drawText(textRenderer, String.valueOf(number), this.getX() + 24, this.getY() + 11, textColor4, true);
                            context.getMatrices().pop();
                        }
                        break;
                    case 4:
                        context.drawItemWithoutEntity(stack, this.getX() + 100, this.getY() + 3);

                        //Draw ingredient number
                        if (number > 1) {
                            context.getMatrices().push();
                            context.getMatrices().translate(0, 0, 200);
                            context.drawText(textRenderer, String.valueOf(number), this.getX() + 100, this.getY() + 11, textColor5, true);
                            context.getMatrices().pop();
                        }
                        break;

                    default:
                        break;
                }
            }

            //Draw base
            int baseCount = recipe.getBaseItem().getCount();
            context.drawItemWithoutEntity(recipe.getBaseItem(), this.getX() + 117, this.getY() + 3);
            //Draw base count
            if (baseCount > 1) {
                context.getMatrices().push();
                context.getMatrices().translate(0, 0, 200);
                context.drawText(textRenderer, String.valueOf(baseCount), this.getX() + 119, this.getY() + 11, textColorBase, true);
                context.getMatrices().pop();
            }
        }
    }

    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder builder) {

    }

    @Override
    public void playDownSound(SoundManager soundManager) {
        soundManager.play(PositionedSoundInstance.master(SoundEvents.UI_CARTOGRAPHY_TABLE_TAKE_RESULT, 1.0f));
    }

    @Override
    protected boolean isValidClickButton(int button) {
        return craftable && playerHasRecipe;
    }

    @Override
    public int getWidth() {
        return 134;
    }
}
