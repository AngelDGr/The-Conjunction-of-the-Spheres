package TCOTS.jei;

import TCOTS.TCOTS_Main;
import TCOTS.blocks.TCOTS_Blocks;
import TCOTS.items.potions.recipes.AlchemyTableRecipe;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.registration.IModInfoRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.runtime.IIngredientManager;
import mezz.jei.common.util.ErrorUtil;
import mezz.jei.library.plugins.vanilla.crafting.CategoryRecipeValidator;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@JeiPlugin
public class TCOTS_JEIPlugin implements IModPlugin {

    public static final RecipeType<RecipeEntry<AlchemyTableRecipe>> ALCHEMY_TABLE =
            RecipeType.createFromVanilla(AlchemyTableRecipe.Type.INSTANCE);

    @Nullable
    private IRecipeCategory<RecipeEntry<AlchemyTableRecipe>> alchemyTableCategory;

    @Override
    public @NotNull Identifier getPluginUid() {
        return new Identifier(TCOTS_Main.MOD_ID, "jei-witcher");
    }

    @Override
    public void registerCategories(@NotNull IRecipeCategoryRegistration registration) {
        IJeiHelpers jeiHelpers = registration.getJeiHelpers();
        IGuiHelper guiHelper = jeiHelpers.getGuiHelper();

        registration.addRecipeCategories(
                alchemyTableCategory=new AlchemyTableRecipeCategory(guiHelper)
        );
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        ErrorUtil.checkNotNull(alchemyTableCategory, "alchemyTableCategory");

        IIngredientManager ingredientManager = registration.getIngredientManager();
        AlchemyTableRecipes alchemyTableRecipes = new AlchemyTableRecipes(ingredientManager);

        registration.addRecipes(ALCHEMY_TABLE, alchemyTableRecipes.getAlchemyTableRecipes(alchemyTableCategory));
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(TCOTS_Blocks.ALCHEMY_TABLE), ALCHEMY_TABLE);
    }

    @Override
    public void registerModInfo(IModInfoRegistration registration) {
        registration.addModAliases(TCOTS_Main.MOD_ID, "tcots", "witcher");
    }

    private static class AlchemyTableRecipes {
        private final RecipeManager recipeManager;
        private final IIngredientManager ingredientManager;

        public AlchemyTableRecipes(IIngredientManager ingredientManager) {
            MinecraftClient minecraft = MinecraftClient.getInstance();
            ErrorUtil.checkNotNull(minecraft, "minecraft");
            ClientWorld world = minecraft.world;
            ErrorUtil.checkNotNull(world, "minecraft world");
            this.recipeManager = world.getRecipeManager();
            this.ingredientManager = ingredientManager;
        }

        public List<RecipeEntry<AlchemyTableRecipe>> getAlchemyTableRecipes(IRecipeCategory<RecipeEntry<AlchemyTableRecipe>> alchemyTableCategory) {
            var validator = new CategoryRecipeValidator<>(alchemyTableCategory, ingredientManager, 6);
            return getValidHandledRecipes(recipeManager, AlchemyTableRecipe.Type.INSTANCE, validator);
        }



        @SuppressWarnings("all")
        private static <C extends Inventory, T extends Recipe<C>> List<RecipeEntry<T>> getValidHandledRecipes(
                RecipeManager recipeManager,
                net.minecraft.recipe.RecipeType<T> recipeType,
                CategoryRecipeValidator<T> validator
        ) {
            return recipeManager.listAllOfType(recipeType)
                    .stream()
                    .filter(r -> validator.isRecipeValid(r) && validator.isRecipeHandled(r))
                    .toList();
        }
    }

    private static class AlchemyTableRecipeCategory implements IRecipeCategory<RecipeEntry<AlchemyTableRecipe>> {

        private final IDrawable background;
        private final IDrawable icon;

        public AlchemyTableRecipeCategory(IGuiHelper guiHelper) {

            background = guiHelper.drawableBuilder(new Identifier(TCOTS_Main.MOD_ID, "textures/gui/alchemy_table_jei.png"),
                    0,0,
                    118, 90)
                    .setTextureSize(118,90)
                    .build();

            icon = guiHelper.createDrawableItemStack(new ItemStack(TCOTS_Blocks.ALCHEMY_TABLE));
        }

        @Override
        public @NotNull RecipeType<RecipeEntry<AlchemyTableRecipe>> getRecipeType() {
            return TCOTS_JEIPlugin.ALCHEMY_TABLE;
        }

        @Override
        public @NotNull Text getTitle() {
            return TCOTS_Blocks.ALCHEMY_TABLE.getName();
        }

        @Override
        public @NotNull IDrawable getBackground() {
            return background;
        }

        @Override
        public @NotNull IDrawable getIcon() {
            return icon;
        }

        @Override
        public void setRecipe(@NotNull IRecipeLayoutBuilder builder, @NotNull RecipeEntry<AlchemyTableRecipe> recipe, @NotNull IFocusGroup focuses) {
            List<ItemStack> potionInputs = recipe.value().returnItemStackWithQuantity();
            ItemStack baseItem = recipe.value().getBaseItem();
            ItemStack result = recipe.value().getResult(null);

            //Put the ingredients
            for(int i=0; i<potionInputs.size(); i++) {
                //Slot 0
                int xPosition=51;
                switch (i){
                    case 1:
                        xPosition=27;
                        break;
                    case 2:
                        xPosition=75;
                        break;
                    case 3:
                        xPosition=3;
                        break;
                    case 4:
                        xPosition=99;
                        break;

                    default:
                        break;
                }

                builder.addSlot(RecipeIngredientRole.INPUT, xPosition, 2)
                        .addItemStack(potionInputs.get(i));
            }

            //Put the base
            builder.addSlot(RecipeIngredientRole.INPUT, 51, 43)
                    .addItemStack(baseItem);

            //Put the result
            builder.addSlot(RecipeIngredientRole.OUTPUT, 51, 72)
                    .addItemStack(result);
        }
    }
}
