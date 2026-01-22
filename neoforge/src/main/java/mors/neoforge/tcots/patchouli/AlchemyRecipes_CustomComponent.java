package mors.neoforge.tcots.patchouli;

import mors.tcots.TCOTS_Main;
import mors.tcots.recipes.AlchemyTableRecipe;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.HolderLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.jetbrains.annotations.NotNull;
import vazkii.patchouli.api.IComponentRenderContext;
import vazkii.patchouli.api.ICustomComponent;
import vazkii.patchouli.api.IVariable;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.UnaryOperator;

@SuppressWarnings("all")
public class AlchemyRecipes_CustomComponent implements ICustomComponent {
    private transient int x, y;

    private String title = "Potions";

    private List<IVariable> recipes;
    private transient List<RecipeHolder<AlchemyTableRecipe>> recipes_list=new ArrayList<>();


    @Override
    public void onVariablesAvailable(UnaryOperator<IVariable> lookup, HolderLookup.Provider registries) {
        Minecraft client = Minecraft.getInstance();

        //Look for the variable "recipes"
        recipes = lookup.apply(IVariable.wrap("#recipes", registries)).asList(registries);

        //Look for the variable "title"
        title = lookup.apply(IVariable.wrap("#title", registries)).asString();

        //Add recipes
        for(IVariable recipe_variable: recipes){
            RecipeHolder<?> holder = client.level.getRecipeManager().byKey(ResourceLocation.tryParse(lookup.apply(recipe_variable).asString()))
                    .orElseThrow(() -> new NoSuchElementException());

            if(holder.value() instanceof AlchemyTableRecipe){
                @SuppressWarnings("unchecked")
                RecipeHolder<AlchemyTableRecipe> castedHolder = (RecipeHolder<AlchemyTableRecipe>) holder;
                recipes_list.add(castedHolder);
            }
        }
    }

    @Override
    public void build(int componentX, int componentY, int pageNum) {
        x = componentX;
        y = componentY;
    }

    @Override
    public void render(GuiGraphics graphics, IComponentRenderContext context, float pticks, int mouseX, int mouseY) {

        MutableComponent titleToRender = Component.literal(title).withStyle(style -> style.withColor(Integer.parseInt(String.valueOf(395026), 16)).withFont(Minecraft.UNIFORM_FONT).withBold(true));

        graphics.drawString(Minecraft.getInstance().font, titleToRender, x+2, y-5, -1, false);

        int mainY=4;


        for (int i = 0; i < recipes_list.size(); i++) {
            renderRecipe(recipes_list.get(i), graphics, context, pticks, mouseX, mouseY, mainY+(48*i));
        }
    }

    public void renderRecipe(RecipeHolder<AlchemyTableRecipe> recipeHolder, GuiGraphics graphics, IComponentRenderContext context, float pticks, int mouseX, int mouseY, int initY){
        ResourceLocation TEXTURE_ID = ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "textures/gui/alchemy_book_gui.png");
        int initX=2;
        AlchemyTableRecipe recipe=recipeHolder.value();


        if(this.hasRecipe(recipeHolder)){
            //Draw background
            {
                graphics.blit(TEXTURE_ID,
                        //Pos
                        37, initY,
                        //UV
                        435, 144,
                        //Size
                        22, 22,
                        //Texture Size
                        512, 256
                );

                graphics.blit(TEXTURE_ID,
                        //Pos
                        1, initY+23,
                        //UV
                        399, 167,
                        //Size
                        111, 18,
                        //Texture Size
                        512, 256
                );
            }

            //Draw ingredients
            {
                for (int i = 0; i < recipe.getIngredients().size(); i++) {
                    //Get the ingredient stack
                    ItemStack stack = recipe.getIngredients().get(i).getItems()[0];
                    //Get the quantity
                    int count = recipe.getIngredientsCounts().get(i);
                    //Creates the stack with the correct quantity
                    ItemStack ingredientStack = new ItemStack(stack.getItem(), count);

                    //Position values
                    int addedX =
                            i==0 ? 38 :
                                    i==1? 19 :
                                            i==2? 57:
                                                    i==3? 0:
                                                            i==4? 76:
                                                                    38;

                    drawItemStack(graphics, ingredientStack, count, initX, initY, addedX);

                    isItemHovered(context, ingredientStack, mouseX, mouseY, addedX, initY+24);
                }
            }

            //Draw base
            {

                drawItemStack(graphics,
                        recipe.getBaseItem(),
                        recipe.getBaseItem().getCount(),
                        initX, initY,
                        94);

                isItemHovered(context, recipe.getBaseItem(),  mouseX, mouseY, 93, initY+24);
            }

            //Draw result
            {

                drawItemStack(graphics,
                        recipe.getResultItem(null),
                        recipe.getResultItem(null).getCount(),
                        initX, initY,
                        38, 3);

                isItemHovered(context, recipe.getResultItem(null),  mouseX, mouseY, 37, initY);
            }
        }
        else
        {

            graphics.blit(TEXTURE_ID,
                    //Pos
                    37, initY,
                    //UV
                    435, 199,
                    //Size
                    22, 22,
                    //Texture Size
                    512, 256
            );

            graphics.blit(TEXTURE_ID,
                    //Pos
                    1, initY+23,
                    //UV
                    399, 222,
                    //Size
                    111, 18,
                    //Texture Size
                    512, 256
            );
        }

    }

    private void drawItemStack(GuiGraphics graphics, ItemStack stack, int count, int initX, int initY, int addedX){
        drawItemStack(graphics, stack, count, initX, initY, addedX, 24);
    }

    private void drawItemStack(GuiGraphics graphics, ItemStack stack, int count, int initX, int initY, int addedX, int addedY){
        graphics.pose().pushPose();
        graphics.pose().translate(0, 0, 200);
        if(count>1) graphics.drawString(Minecraft.getInstance().font, Component.literal(String.valueOf(count)), (initX+addedX)+ (count>9? 6:11), (initY+addedY)+9,-1, true);
        graphics.pose().popPose();

        graphics.renderItem(stack, initX+addedX, initY+addedY);
    }

    private void isItemHovered(IComponentRenderContext context, ItemStack item, int mouseX, int mouseY, int itemX, int itemY){
        if(context.isAreaHovered(mouseX, mouseY, itemX, itemY, 22, 22) && !item.isEmpty()){
            if(Minecraft.getInstance().player!=null){
                TooltipFlag.Default tooltipFlag = Minecraft.getInstance().options.advancedItemTooltips ? TooltipFlag.Default.ADVANCED : TooltipFlag.Default.NORMAL;

                context.setHoverTooltipComponents(item.getTooltipLines(Item.TooltipContext.EMPTY, Minecraft.getInstance().player, tooltipFlag));
            }
        }
    }

    private boolean hasRecipe(@NotNull RecipeHolder<AlchemyTableRecipe> recipeEntry){
        return Minecraft.getInstance().player != null && Minecraft.getInstance().player.getRecipeBook().contains(recipeEntry);
    }
}
