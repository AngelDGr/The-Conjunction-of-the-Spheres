package TCOTS.potions.recipes.recipebook.widget;

import TCOTS.potions.AlcohestItem;
import TCOTS.potions.DwarvenSpiritItem;
import TCOTS.potions.MonsterOil_Base;
import TCOTS.potions.WitcherPotions_Base;
import TCOTS.potions.recipes.AlchemyTableRecipe;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.sound.SoundManager;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.List;

public class AlchemyRecipeResultButton extends ClickableWidget {

    private AlchemyTableRecipe recipe;

    private TextRenderer textRenderer;
    private boolean craftable;
    public AlchemyRecipeResultButton() {
        super(0,0,134, 22, ScreenTexts.EMPTY);
        craftable=false;
    }

    int j = 173;




    public void receiveRecipe(AlchemyTableRecipe recipe){
        this.recipe=recipe;
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

    public AlchemyTableRecipe getRecipe(){
        return this.recipe;
    }

    int textColor1=0xb43d2c;
    int textColor2=0xb43d2c;
    int textColor3=0xb43d2c;
    int textColor4=0xb43d2c;
    int textColor5=0xb43d2c;

    public void setTextColor(int index, boolean colorWhite){
        switch (index){
            case 0:
                if(colorWhite){textColor1 = 0xffffff;}
                else{textColor1 = 0xb43d2c;}
                break;
            case 1:
                if(colorWhite){textColor2 = 0xffffff;}
                else{textColor2 = 0xb43d2c;}
                break;
            case 2:
                if(colorWhite){textColor3 = 0xffffff;}
                else{textColor3 = 0xb43d2c;}
                break;
            case 3:
                if(colorWhite){textColor4 = 0xffffff;}
                else{textColor4 = 0xb43d2c;}
                break;
            case 4:
                if(colorWhite){textColor5 = 0xffffff;}
                else{textColor5 = 0xb43d2c;}
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
                168, 221,
                17, 18);
        context.getMatrices().pop();
    }

    @Override
    protected void renderButton(DrawContext context, int mouseX, int mouseY, float delta) {
        if(recipe == null){
            return;
        }

        if (this.isSelected()) {
            //If craftable puts the green selected color
            if(craftable) {j = 173 + 23;}
            else {
                j = 173+23+23;
            }


            if(this.recipe.getOutput(null).getItem() instanceof WitcherPotions_Base && !(this.recipe.getOutput(null).getItem() instanceof AlcohestItem) && !(this.recipe.getOutput(null).getItem() instanceof DwarvenSpiritItem)){
                List<Text> list= new ArrayList<>();
                int tooltipY;
                if((((WitcherPotions_Base) this.recipe.getOutput(null).getItem()).isDecoction())){
                    tooltipY=12;
                }else {
                    tooltipY=22;
                }

                //Name
                list.add(Text.translatable("tcots-witcher.tooltip.gui.formula", recipe.getOutput(null).getName().getString()));

                //Toxicity
                int tox = ((WitcherPotions_Base) this.recipe.getOutput(null).getItem()).getToxicity();
                list.add(Text.translatable("tcots-witcher.tooltip.toxicity", tox).formatted(Formatting.DARK_GREEN));

                //Stack
                int maxCount = this.recipe.getOutput(null).getMaxCount();
                if(!(((WitcherPotions_Base) this.recipe.getOutput(null).getItem()).isDecoction())){
                    list.add(Text.translatable("tcots-witcher.tooltip.max_stack", maxCount).formatted(Formatting.DARK_BLUE));
                }


                context.drawTooltip(textRenderer, list, this.getX()-20, this.getY()-tooltipY);

            } else if (this.recipe.getOutput(null).getItem() instanceof MonsterOil_Base) {

                List<Text> list= new ArrayList<>();
                //Name
                list.add(Text.translatable("tcots-witcher.tooltip.gui.formula", recipe.getOutput(null).getName().getString()));

                //Damage
                int damage = ((MonsterOil_Base) this.recipe.getOutput(null).getItem()).getLevel() * 2;
                list.add(Text.translatable("tcots-witcher.tooltip.gui.oil_damage", damage).formatted(Formatting.RED));

                //Uses
                int uses = ((MonsterOil_Base) this.recipe.getOutput(null).getItem()).getUses();
                list.add(Text.translatable("tcots-witcher.tooltip.gui.oil_uses", uses).formatted(Formatting.DARK_BLUE));
                context.drawTooltip(textRenderer, list, this.getX()-20, this.getY()-22);
            } else{
                context.drawTooltip(textRenderer, Text.translatable("tcots-witcher.tooltip.gui.formula", recipe.getOutput(null).getName().getString()), this.getX()-20, this.getY());
            }
        }
        else{
            //If not craftable just put the button in red
            if(!craftable){
                j = 173+23+23;
            }
            else{
                j = 173;
            }
        }

        if(!baseNotPresent){
            drawNotBase(context);
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
                                    context.drawText(textRenderer, String.valueOf(number), this.getX() + 62, this.getY() + 11, textColor1, true);
                                    context.getMatrices().pop();}
                                break;
                            case 1:
                                context.drawItemWithoutEntity(stack, this.getX() + 43, this.getY() + 3);

                                //Draw ingredient number
                                if(number>1){
                                    context.getMatrices().push();
                                    context.getMatrices().translate(0,0,200);
                                    context.drawText(textRenderer, String.valueOf(number), this.getX() + 43, this.getY() + 11, textColor2, true);
                                    context.getMatrices().pop();}
                                break;
                            case 2:
                                context.drawItemWithoutEntity(stack, this.getX() + 81, this.getY() + 3);

                                //Draw ingredient number
                                if(number>1){
                                    context.getMatrices().push();
                                    context.getMatrices().translate(0,0,200);
                                    context.drawText(textRenderer, String.valueOf(number), this.getX() + 81, this.getY() + 11, textColor3, true);
                                    context.getMatrices().pop();}
                                break;
                            case 3:
                                context.drawItemWithoutEntity(stack, this.getX() + 24, this.getY() + 3);

                                //Draw ingredient number
                                if(number>1){
                                    context.getMatrices().push();
                                    context.getMatrices().translate(0,0,200);
                                    context.drawText(textRenderer, String.valueOf(number), this.getX() + 24, this.getY() + 11, textColor4, true);
                                    context.getMatrices().pop();}
                                break;
                            case 4:
                                context.drawItemWithoutEntity(stack, this.getX() + 100, this.getY() + 3);

                                //Draw ingredient number
                                if(number>1){
                                    context.getMatrices().push();
                                    context.getMatrices().translate(0,0,200);
                                    context.drawText(textRenderer, String.valueOf(number), this.getX() + 100, this.getY() + 11, textColor5, true);
                                    context.getMatrices().pop();}
                                break;

                            default:
                                break;
                        }
                    }

                    //Draw base
                    context.drawItemWithoutEntity(recipe.getBaseItem(), this.getX() + 117, this.getY() + 3);
    }

    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder builder) {

    }

    @Override
    public void playDownSound(SoundManager soundManager) {
        soundManager.play(PositionedSoundInstance.master(SoundEvents.UI_CARTOGRAPHY_TABLE_TAKE_RESULT, 1.0f));
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    protected boolean isValidClickButton(int button) {
        return craftable;
    }



    @Override
    public int getWidth() {
        return 134;
    }
}
