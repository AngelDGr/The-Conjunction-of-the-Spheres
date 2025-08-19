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

    public void receiveRecipe(final RecipeHolder<AlchemyTableRecipe> recipe, final RecipeBook recipeBook){

        this.playerHasRecipe = recipeBook.contains(recipe);

        this.recipeEntry=recipe;
        if(recipe != null){
        this.recipe=recipe.value();}
        else {
            this.recipe=null;
        }
    }

    public void receiveTextRenderer(final Font renderer){
        this.textRenderer=renderer;
    }

    public void setCraftable(final boolean craftable){
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
    public void setTextColor(final int index, final boolean colorWhite){
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

    public void setBaseNotPresent(final boolean baseNotPresent) {
        this.baseNotPresent = baseNotPresent;
    }

    private void drawNotBase(final GuiGraphics context){
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
    protected void renderWidget(@NotNull final GuiGraphics context, final int mouseX, final int mouseY, final float delta) {
        if(recipeEntry == null || recipe == null){
            return;
        }

        if (this.isHoveredOrFocused() && playerHasRecipe) {
            //If craftable puts the green selected color
            if(craftable) {j = 193 + 23;}
            else {
                j = 193+23+23;
            }

            final ChatFormatting textColor;

            if(this.recipe.getResultItem(null).getItem() instanceof WitcherPotions_Base && !(this.recipe.getResultItem(null).getItem() instanceof WitcherAlcohol_Base) && !(this.recipe.getResultItem(null).getItem() instanceof WitcherWhiteHoney)){
                final List<Component> list= new ArrayList<>();

                if(((WitcherPotions_Base) this.recipe.getResultItem(null).getItem()).getStatusEffect().getAmplifier() > 0){
                    textColor = ChatFormatting.YELLOW;
                } else {
                    textColor = ChatFormatting.WHITE;
                }

                final int tooltipY;
                //Name
                if((((WitcherPotions_Base) this.recipe.getResultItem(null).getItem()).isDecoction())){
                    tooltipY=12;
                    list.add(Component.translatable("tcots_witcher.tooltip.gui.formula", recipe.getResultItem(null).getHoverName().getString()).withColor(0x41d331));
                }else {
                    tooltipY=22;
                    list.add(Component.translatable("tcots_witcher.tooltip.gui.formula", recipe.getResultItem(null).getHoverName().getString()).withStyle(textColor));
                }

                //Toxicity
                final int tox = ((WitcherPotions_Base) this.recipe.getResultItem(null).getItem()).getToxicity();
                list.add(Component.translatable("tcots_witcher.tooltip.toxicity", tox).withStyle(ChatFormatting.DARK_GREEN));

                //Stack
                final int maxCount = this.recipe.getResultItem(null).getMaxStackSize();
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

                final List<Component> list= new ArrayList<>();
                //Name
                list.add(Component.translatable("tcots_witcher.tooltip.gui.formula", recipe.getResultItem(null).getHoverName().getString()).withStyle(textColor));

                //Damage
                final int damage = ((WitcherMonsterOil_Base) this.recipe.getResultItem(null).getItem()).getLevel() * 2;
                list.add(Component.translatable("tcots_witcher.tooltip.gui.oil_damage", damage).withStyle(ChatFormatting.RED));

                //Uses
                final int uses = ((WitcherMonsterOil_Base) this.recipe.getResultItem(null).getItem()).getUses();
                list.add(Component.translatable("tcots_witcher.tooltip.gui.oil_uses", uses).withStyle(ChatFormatting.DARK_BLUE));
                context.renderComponentTooltip(textRenderer, list, this.getX()-20, this.getY()-22);
            } else if (this.recipe.getResultItem(null).getItem() instanceof WitcherWhiteHoney) {

                final List<Component> list= new ArrayList<>();
                list.add(Component.translatable("tcots_witcher.tooltip.gui.formula", recipe.getResultItem(null).getHoverName().getString()));
                //Stack
                final int maxCount = this.recipe.getResultItem(null).getMaxStackSize();

                list.add(Component.translatable("tcots_witcher.tooltip.max_stack", maxCount).withStyle(ChatFormatting.DARK_BLUE));

                context.renderComponentTooltip(textRenderer, list, this.getX()-20, this.getY()-12);

            } else if (this.recipe.getResultItem(null).getItem() instanceof WitcherBombs_Base) {
                if(((WitcherBombs_Base) this.recipe.getResultItem(null).getItem()).getLevel() > 0){
                    textColor = ChatFormatting.YELLOW;
                } else {
                    textColor = ChatFormatting.WHITE;
                }

                final List<Component> list= new ArrayList<>();
                //Name
                list.add(Component.translatable("tcots_witcher.tooltip.gui.formula", recipe.getResultItem(null).getHoverName().getString()).withStyle(textColor));

                //Stack
                final int maxCount = this.recipe.getResultItem(null).getMaxStackSize();

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

            final int resultCount = recipe.getResultItem(null).getCount();

            drawItemAmount(context, resultCount, 0xffffff, 14);

            //Draw ingredients
            for (int l = 0; l < recipe.getIngredients().size(); l++) {
                final ItemStack stack = recipe.getIngredients().get(l).getItems()[0];
                final int amount = recipe.getIngredientsCounts().get(l);
                switch (l) {
                    case 0:
                        //Draw ingredient Item
                        context.renderFakeItem(stack, this.getX() + 62, this.getY() + 3);
                        drawItemAmount(context, amount, textColor1, 73);
                        break;
                    case 1:
                        context.renderFakeItem(stack, this.getX() + 43, this.getY() + 3);
                        drawItemAmount(context, amount, textColor2, 54);
                        break;
                    case 2:
                        context.renderFakeItem(stack, this.getX() + 81, this.getY() + 3);
                        drawItemAmount(context, amount, textColor4, 92);
                        break;
                    case 3:
                        context.renderFakeItem(stack, this.getX() + 24, this.getY() + 3);
                        drawItemAmount(context, amount, textColor4, 35);
                        break;
                    case 4:
                        context.renderFakeItem(stack, this.getX() + 100, this.getY() + 3);
                        drawItemAmount(context, amount, textColor5, 111);
                        break;

                    default:
                        break;
                }
            }

            //Draw base
            final int baseAmount = recipe.getBaseItem().getCount();
            context.renderFakeItem(recipe.getBaseItem(), this.getX() + 117, this.getY() + 3);
            drawItemAmount(context, baseAmount, textColorBase, 128);
        }
    }

    private void drawItemAmount(final GuiGraphics context, final int amount, final int textColor, final int xOffset, final int yOffset){
        if (amount > 1) {
            context.pose().pushPose();
            context.pose().translate(0, 0, 200);
            context.drawString(textRenderer, String.valueOf(amount), this.getX() + xOffset-(amount>9?6:0), this.getY() + yOffset, textColor, true);
            context.pose().popPose();
        }
    }

    private void drawItemAmount(final GuiGraphics context, final int amount, final int textColor, final int xOffset){
        drawItemAmount(context, amount, textColor, xOffset, 12);
    }

    @Override
    protected void updateWidgetNarration(@NotNull final NarrationElementOutput builder) {

    }

    @Override
    public void playDownSound(final SoundManager soundManager) {
        soundManager.play(SimpleSoundInstance.forUI(SoundEvents.UI_CARTOGRAPHY_TABLE_TAKE_RESULT, 1.0f));
    }

    @Override
    protected boolean isValidClickButton(final int button) {
        return craftable && playerHasRecipe;
    }

    @Override
    public int getWidth() {
        return 134;
    }
}
