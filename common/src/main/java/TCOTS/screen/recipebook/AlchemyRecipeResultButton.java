package TCOTS.screen.recipebook;

import TCOTS.items.concoctions.WitcherAlcohol_Base;
import TCOTS.items.concoctions.WitcherMonsterOil_Base;
import TCOTS.items.concoctions.WitcherPotions_Base;
import TCOTS.items.concoctions.WitcherWhiteHoney;
import TCOTS.items.concoctions.*;
import TCOTS.recipes.AlchemyTableRecipe;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.RecipeBook;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;

public class AlchemyRecipeResultButton extends AbstractWidget {

    @Nullable
    private AlchemyTableRecipe recipe;

    @Nullable
    private RecipeHolder<AlchemyTableRecipe> recipeEntry;

    private Font textRenderer;
    private boolean craftable;
    private boolean playerHasRecipe;
    public AlchemyRecipeResultButton() {
        super(0,0,134, 22, CommonComponents.EMPTY);
        craftable=false;
    }

    int j = 193;

    public void receiveRecipe(RecipeHolder<AlchemyTableRecipe> recipe, RecipeBook recipeBook){

        this.playerHasRecipe = recipeBook.contains(recipe);

        this.recipeEntry=recipe;
        if(recipe != null){
        this.recipe=recipe.value();}
        else {
            this.recipe=null;
        }
    }

    public void receiveTextRenderer(Font renderer){
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

    public @Nullable RecipeHolder<AlchemyTableRecipe> getRecipeEntry(){
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

    private void drawNotBase(GuiGraphics context){
        context.pose().pushPose();
        context.pose().translate(0,0,101);
        context.blit(
                AlchemyRecipeBookWidget.RECIPE_GUI_TEXTURE,
                this.getX()+117, this.getY()+2,
                168, 241,
                17, 18,
                256,300);
        context.pose().popPose();
    }

    @Override
    protected void renderWidget(@NotNull GuiGraphics context, int mouseX, int mouseY, float delta) {
        if(recipeEntry == null || recipe == null){
            return;
        }

        if (this.isHoveredOrFocused() && playerHasRecipe) {
            //If craftable puts the green selected color
            if(craftable) {j = 193 + 23;}
            else {
                j = 193+23+23;
            }

            ChatFormatting textColor;

            if(this.recipe.getResultItem(null).getItem() instanceof WitcherPotions_Base && !(this.recipe.getResultItem(null).getItem() instanceof WitcherAlcohol_Base) && !(this.recipe.getResultItem(null).getItem() instanceof WitcherWhiteHoney)){
                List<Component> list= new ArrayList<>();

                if(((WitcherPotions_Base) this.recipe.getResultItem(null).getItem()).getStatusEffect().getAmplifier() > 0){
                    textColor = ChatFormatting.YELLOW;
                } else {
                    textColor = ChatFormatting.WHITE;
                }

                int tooltipY;
                //Name
                if((((WitcherPotions_Base) this.recipe.getResultItem(null).getItem()).isDecoction())){
                    tooltipY=12;
                    list.add(Component.translatable("tcots_witcher.tooltip.gui.formula", recipe.getResultItem(null).getHoverName().getString()).withColor(0x41d331));
                }else {
                    tooltipY=22;
                    list.add(Component.translatable("tcots_witcher.tooltip.gui.formula", recipe.getResultItem(null).getHoverName().getString()).withStyle(textColor));
                }

                //Toxicity
                int tox = ((WitcherPotions_Base) this.recipe.getResultItem(null).getItem()).getToxicity();
                list.add(Component.translatable("tcots_witcher.tooltip.toxicity", tox).withStyle(ChatFormatting.DARK_GREEN));

                //Stack
                int maxCount = this.recipe.getResultItem(null).getMaxStackSize();
                if(!(((WitcherPotions_Base) this.recipe.getResultItem(null).getItem()).isDecoction())){
                    list.add(Component.translatable("tcots_witcher.tooltip.max_stack", maxCount).withStyle(ChatFormatting.DARK_BLUE));
                }


                context.renderComponentTooltip(textRenderer, list, this.getX()-20, this.getY()-tooltipY);

            } else if (this.recipe.getResultItem(null).getItem() instanceof WitcherMonsterOil_Base) {

                if(((WitcherMonsterOil_Base) this.recipe.getResultItem(null).getItem()).getLevel() > 1){
                    textColor = ChatFormatting.YELLOW;
                } else {
                    textColor = ChatFormatting.WHITE;
                }

                List<Component> list= new ArrayList<>();
                //Name
                list.add(Component.translatable("tcots_witcher.tooltip.gui.formula", recipe.getResultItem(null).getHoverName().getString()).withStyle(textColor));

                //Damage
                int damage = ((WitcherMonsterOil_Base) this.recipe.getResultItem(null).getItem()).getLevel() * 2;
                list.add(Component.translatable("tcots_witcher.tooltip.gui.oil_damage", damage).withStyle(ChatFormatting.RED));

                //Uses
                int uses = ((WitcherMonsterOil_Base) this.recipe.getResultItem(null).getItem()).getUses();
                list.add(Component.translatable("tcots_witcher.tooltip.gui.oil_uses", uses).withStyle(ChatFormatting.DARK_BLUE));
                context.renderComponentTooltip(textRenderer, list, this.getX()-20, this.getY()-22);
            } else if (this.recipe.getResultItem(null).getItem() instanceof WitcherWhiteHoney) {

                List<Component> list= new ArrayList<>();
                list.add(Component.translatable("tcots_witcher.tooltip.gui.formula", recipe.getResultItem(null).getHoverName().getString()));
                //Stack
                int maxCount = this.recipe.getResultItem(null).getMaxStackSize();

                list.add(Component.translatable("tcots_witcher.tooltip.max_stack", maxCount).withStyle(ChatFormatting.DARK_BLUE));

                context.renderComponentTooltip(textRenderer, list, this.getX()-20, this.getY()-12);

            } else if (this.recipe.getResultItem(null).getItem() instanceof WitcherBombs_Base) {
                if(((WitcherBombs_Base) this.recipe.getResultItem(null).getItem()).getLevel() > 0){
                    textColor = ChatFormatting.YELLOW;
                } else {
                    textColor = ChatFormatting.WHITE;
                }

                List<Component> list= new ArrayList<>();
                //Name
                list.add(Component.translatable("tcots_witcher.tooltip.gui.formula", recipe.getResultItem(null).getHoverName().getString()).withStyle(textColor));

                //Stack
                int maxCount = this.recipe.getResultItem(null).getMaxStackSize();

                list.add(Component.translatable("tcots_witcher.tooltip.max_stack", maxCount).withStyle(ChatFormatting.DARK_BLUE));

                context.renderComponentTooltip(textRenderer, list, this.getX()-20, this.getY()-12);

            } else {
                context.renderTooltip(textRenderer, Component.translatable("tcots_witcher.tooltip.gui.formula", recipe.getResultItem(null).getHoverName().getString()), this.getX()-20, this.getY());
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


        context.blit(AlchemyRecipeBookWidget.RECIPE_GUI_TEXTURE,
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
            context.renderFakeItem(recipe.getResultItem(null),
                    this.getX() + 3,
                    this.getY() + 3);

            int resultCount = recipe.getResultItem(null).getCount();

            //Draw result count
            if (resultCount > 1) {
                context.pose().pushPose();
                context.pose().translate(0, 0, 200);
                context.drawString(textRenderer, String.valueOf(resultCount), this.getX() + 14, this.getY() + 12, 0xffffff, true);
                context.pose().popPose();
            }

            //Draw ingredients
            for (int l = 0; l < recipe.getIngredients().size(); l++) {
                ItemStack stack = recipe.getIngredients().get(l).getItems()[0];
                int number = recipe.getIngredientsCounts().get(l);
                switch (l) {
                    case 0:
                        //Draw ingredient Item
                        context.renderFakeItem(stack, this.getX() + 62, this.getY() + 3);

                        //Draw ingredient number
                        if (number > 1) {
                            context.pose().pushPose();
                            context.pose().translate(0, 0, 200);
                            context.drawString(textRenderer, String.valueOf(number), this.getX() + 73, this.getY() + 12, textColor1, true);
                            context.pose().popPose();
                        }
                        break;
                    case 1:
                        context.renderFakeItem(stack, this.getX() + 43, this.getY() + 3);

                        //Draw ingredient number
                        if (number > 1) {
                            context.pose().pushPose();
                            context.pose().translate(0, 0, 200);
                            context.drawString(textRenderer, String.valueOf(number), this.getX() + 54, this.getY() + 12, textColor2, true);
                            context.pose().popPose();
                        }
                        break;
                    case 2:
                        context.renderFakeItem(stack, this.getX() + 81, this.getY() + 3);

                        //Draw ingredient number
                        if (number > 1) {
                            context.pose().pushPose();
                            context.pose().translate(0, 0, 200);
                            context.drawString(textRenderer, String.valueOf(number), this.getX() + 92, this.getY() + 12, textColor3, true);
                            context.pose().popPose();
                        }
                        break;
                    case 3:
                        context.renderFakeItem(stack, this.getX() + 24, this.getY() + 3);

                        //Draw ingredient number
                        if (number > 1) {
                            context.pose().pushPose();
                            context.pose().translate(0, 0, 200);
                            context.drawString(textRenderer, String.valueOf(number), this.getX() + 35, this.getY() + 12, textColor4, true);
                            context.pose().popPose();
                        }
                        break;
                    case 4:
                        context.renderFakeItem(stack, this.getX() + 100, this.getY() + 3);

                        //Draw ingredient number
                        if (number > 1) {
                            context.pose().pushPose();
                            context.pose().translate(0, 0, 200);
                            context.drawString(textRenderer, String.valueOf(number), this.getX() + 111, this.getY() + 12, textColor5, true);
                            context.pose().popPose();
                        }
                        break;

                    default:
                        break;
                }
            }

            //Draw base
            int baseCount = recipe.getBaseItem().getCount();
            context.renderFakeItem(recipe.getBaseItem(), this.getX() + 117, this.getY() + 3);
            //Draw base count
            if (baseCount > 1) {
                context.pose().pushPose();
                context.pose().translate(0, 0, 200);
                context.drawString(textRenderer, String.valueOf(baseCount), this.getX() + 128, this.getY() + 12, textColorBase, true);
                context.pose().popPose();
            }
        }
    }

    @Override
    protected void updateWidgetNarration(@NotNull NarrationElementOutput builder) {

    }

    @Override
    public void playDownSound(SoundManager soundManager) {
        soundManager.play(SimpleSoundInstance.forUI(SoundEvents.UI_CARTOGRAPHY_TABLE_TAKE_RESULT, 1.0f));
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
