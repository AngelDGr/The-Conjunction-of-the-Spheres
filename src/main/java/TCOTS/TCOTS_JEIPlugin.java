package TCOTS;

import TCOTS.blocks.TCOTS_Blocks;
import TCOTS.items.HerbalMixture;
import TCOTS.items.TCOTS_ArmorMaterials;
import TCOTS.items.TCOTS_Items;
import TCOTS.items.TCOTS_ToolMaterials;
import TCOTS.items.components.RecipeTeacherComponent;
import TCOTS.items.concoctions.recipes.AlchemyTableRecipe;
import TCOTS.items.concoctions.recipes.HerbalTableRecipe;
import TCOTS.screen.HerbalTableScreen;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.helpers.IJeiHelpers;
import mezz.jei.api.ingredients.subtypes.ISubtypeInterpreter;
import mezz.jei.api.ingredients.subtypes.UidContext;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.recipe.vanilla.IJeiAnvilRecipe;
import mezz.jei.api.recipe.vanilla.IVanillaRecipeFactory;
import mezz.jei.api.registration.*;
import mezz.jei.api.runtime.IIngredientManager;
import mezz.jei.common.util.ErrorUtil;
import mezz.jei.common.util.Translator;
import mezz.jei.library.plugins.vanilla.crafting.CategoryRecipeValidator;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.Potions;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.RecipeManager;
import net.minecraft.recipe.input.RecipeInput;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@JeiPlugin
public class TCOTS_JEIPlugin implements IModPlugin {

    public static final RecipeType<RecipeEntry<AlchemyTableRecipe>> ALCHEMY_TABLE = RecipeType.createFromVanilla(AlchemyTableRecipe.Type.INSTANCE);
    public static final RecipeType<RecipeEntry<HerbalTableRecipe>> HERBAL_TABLE = RecipeType.createFromVanilla(HerbalTableRecipe.Type.INSTANCE);


    @Nullable
    private IRecipeCategory<RecipeEntry<AlchemyTableRecipe>> alchemyTableCategory;
    @Nullable
    private IRecipeCategory<RecipeEntry<HerbalTableRecipe>> herbalTableCategory;

    @Override
    public @NotNull Identifier getPluginUid() {
        return Identifier.of(TCOTS_Main.MOD_ID, "jei-witcher");
    }

    @Override
    public void registerItemSubtypes(ISubtypeRegistration registration) {
        registration.registerSubtypeInterpreter(TCOTS_Items.ALCHEMY_FORMULA, AlchemyFormulaInterpreter.INSTANCE);
    }

    public static class AlchemyFormulaInterpreter implements ISubtypeInterpreter<ItemStack> {
        public static final AlchemyFormulaInterpreter INSTANCE = new AlchemyFormulaInterpreter();

        private AlchemyFormulaInterpreter() {}


        @Override
        public @Nullable Object getSubtypeData(ItemStack ingredient, @NotNull UidContext context) {
            return ingredient.get(TCOTS_Items.RECIPE_TEACHER_COMPONENT);
        }

        @Override
        public @NotNull String getLegacyStringSubtypeInfo(ItemStack ingredient, @NotNull UidContext context) {
            if (!ingredient.contains(TCOTS_Items.RECIPE_TEACHER_COMPONENT)) {
                return "";
            }

            RecipeTeacherComponent recipeTeacher = ingredient.get(TCOTS_Items.RECIPE_TEACHER_COMPONENT);

            String formulaString=null;
            if(recipeTeacher != null){
                formulaString = recipeTeacher.recipeName();
            }

            return Objects.requireNonNullElse(formulaString, "");
        }
    }

    @Override
    public void registerCategories(@NotNull IRecipeCategoryRegistration registration) {
        IJeiHelpers jeiHelpers = registration.getJeiHelpers();
        IGuiHelper guiHelper = jeiHelpers.getGuiHelper();

        registration.addRecipeCategories(
                alchemyTableCategory =  new AlchemyTableRecipeCategory(guiHelper),
                herbalTableCategory  =  new HerbalTableRecipeCategory(guiHelper)
        );
    }


    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        ErrorUtil.checkNotNull(alchemyTableCategory, "alchemyTableCategory");
        ErrorUtil.checkNotNull(herbalTableCategory, "herbalTableCategory");

        IIngredientManager ingredientManager = registration.getIngredientManager();
        TCOTSRecipes TCOTSRecipes = new TCOTSRecipes(ingredientManager);
        IVanillaRecipeFactory vanillaRecipeFactory = registration.getVanillaRecipeFactory();


        registration.addRecipes(ALCHEMY_TABLE, TCOTSRecipes.getAlchemyTableRecipes(alchemyTableCategory));
        registration.addRecipes(HERBAL_TABLE, TCOTSRecipes.getHerbalTableRecipes(herbalTableCategory));

        registration.addRecipes(RecipeTypes.ANVIL, RepairDataMaker.getAnvilRecipes(vanillaRecipeFactory));
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(TCOTS_Blocks.ALCHEMY_TABLE), ALCHEMY_TABLE);
        registration.addRecipeCatalyst(new ItemStack(TCOTS_Blocks.HERBAL_TABLE), HERBAL_TABLE);
    }

    @Override
    public void registerModInfo(IModInfoRegistration registration) {
        registration.addModAliases(TCOTS_Main.MOD_ID, "tcots", "witcher");
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(HerbalTableScreen.class, 102, 48, 22, 15, HERBAL_TABLE);
    }

    private static class AlchemyTableRecipeCategory implements IRecipeCategory<RecipeEntry<AlchemyTableRecipe>> {
        private final IDrawable background;
        private final IDrawable icon;

        public AlchemyTableRecipeCategory(IGuiHelper guiHelper) {

            background = guiHelper.drawableBuilder(Identifier.of(TCOTS_Main.MOD_ID, "textures/gui/alchemy_table_jei.png"),
                            0,0,
                            118, 100)
                    .setTextureSize(118,100)
                    .build();

            icon = guiHelper.createDrawableItemStack(new ItemStack(TCOTS_Blocks.ALCHEMY_TABLE));
        }

        @Override
        public int getHeight() {
            return 100;
        }

        @Override
        public int getWidth() {
            return 118;
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
                int yPosition=22;
                switch (i){
                    case 1:
                        xPosition=27;
                        yPosition=13;
                        break;
                    case 2:
                        xPosition=75;
                        yPosition=13;
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

                builder.addSlot(RecipeIngredientRole.INPUT, xPosition, yPosition)
                        .addItemStack(potionInputs.get(i));
            }

            //Put the base
            builder.addSlot(RecipeIngredientRole.INPUT, 51, 52)
                    .addItemStack(baseItem);

            //Put the result
            builder.addSlot(RecipeIngredientRole.OUTPUT, 51, 81)
                    .addItemStack(result);
        }

        @Override
        public void draw(@NotNull RecipeEntry<AlchemyTableRecipe> recipe, @NotNull IRecipeSlotsView recipeSlotsView, @NotNull DrawContext guiGraphics, double mouseX, double mouseY) {
            MinecraftClient minecraft = MinecraftClient.getInstance();
            this.background.draw(guiGraphics);
            if(minecraft.player != null && minecraft.player.getRecipeBook().contains(recipe)){
                return;
            }

            String text = Translator.translateToLocalFormatted("gui.jei.tcots-witcher.requires_recipe");

            TextRenderer font = minecraft.textRenderer;
            guiGraphics.drawText(font, text, 0, 0, 0xFF5555, true);

        }
    }

    private static class HerbalTableRecipeCategory implements IRecipeCategory<RecipeEntry<HerbalTableRecipe>> {
        private final IDrawable background;
        private final IDrawable icon;

        public HerbalTableRecipeCategory(IGuiHelper guiHelper) {

            background = guiHelper.drawableBuilder(Identifier.of(TCOTS_Main.MOD_ID, "textures/gui/herbal_table_jei.png"),
                            0,0,
                            157, 32)
                    .setTextureSize(157,32)
                    .addPadding(10, 0, 0 ,0)
                    .build();

            icon = guiHelper.createDrawableItemStack(new ItemStack(TCOTS_Blocks.HERBAL_TABLE));
        }

        @Override
        public int getWidth() {
            return 157;
        }

        @Override
        public int getHeight() {
            return 42;
        }

        @Override
        public @NotNull RecipeType<RecipeEntry<HerbalTableRecipe>> getRecipeType() {
            return TCOTS_JEIPlugin.HERBAL_TABLE;
        }

        @Override
        public @NotNull Text getTitle() {
            return TCOTS_Blocks.HERBAL_TABLE.getName();
        }

        @Override
        public @NotNull IDrawable getIcon() {
            return icon;
        }

        @Override
        public void setRecipe(@NotNull IRecipeLayoutBuilder builder, @NotNull RecipeEntry<HerbalTableRecipe> recipeEntry, @NotNull IFocusGroup focuses) {
            HerbalTableRecipe recipe = recipeEntry.value();

            //Herb
            ItemStack herb = new ItemStack(recipe.getHerb().getItem(), 1);

            //Bottle
            ItemStack bottle =
                    recipe.getBasePotion() == 1?
                            PotionContentsComponent.createStack(Items.POTION, Potions.MUNDANE) :
                            recipe.getBasePotion() == 2? PotionContentsComponent.createStack(Items.POTION, Potions.THICK):
                                    PotionContentsComponent.createStack(Items.POTION, Potions.WATER);

            //Result
            List<StatusEffectInstance> totalEffectsFirst = new ArrayList<>();
            recipe.getEffects().forEach(effect -> {
                if (!effect.value().isBeneficial()) {
                    totalEffectsFirst.add(new StatusEffectInstance(effect, effect.value().isInstant()? 1:recipe.getTickEffectTime(), recipe.getBadAmplifier()));
                } else {
                    totalEffectsFirst.add(new StatusEffectInstance(effect, effect.value().isInstant()? 1:recipe.getTickEffectTime()));
                }
            });
            ItemStack result = recipe.getResult(null);

            result = HerbalMixture.writeEffects(result.copy(), totalEffectsFirst);


            //Put the herb
            builder.addSlot(RecipeIngredientRole.INPUT, 17, 18)
                    .addItemStack(herb);

            //Put the bottle
            builder.addSlot(RecipeIngredientRole.INPUT, 66, 18)
                    .addItemStack(bottle);

            //Put the result
            builder.addSlot(RecipeIngredientRole.OUTPUT, 124, 18)
                    .addItemStack(result);

        }

        @Override
        public void draw(@NotNull RecipeEntry<HerbalTableRecipe> recipe, @NotNull IRecipeSlotsView recipeSlotsView, @NotNull DrawContext guiGraphics, double mouseX, double mouseY) {
            IRecipeCategory.super.draw(recipe, recipeSlotsView, guiGraphics, mouseX, mouseY);
            MinecraftClient minecraft = MinecraftClient.getInstance();
            String text = Translator.translateToLocalFormatted("gui.jei.tcots-witcher.for_herb", recipe.value().getTickEffectTime()/20);

            TextRenderer font = minecraft.textRenderer;
            guiGraphics.drawText(font, text, 0, 2, 43520, true);

            this.background.draw(guiGraphics);
        }
    }

    private static class TCOTSRecipes {
        private final RecipeManager recipeManager;
        private final IIngredientManager ingredientManager;

        public TCOTSRecipes(IIngredientManager ingredientManager) {
            MinecraftClient minecraft = MinecraftClient.getInstance();
            ErrorUtil.checkNotNull(minecraft, "minecraft");
            ClientWorld world = minecraft.world;
            ErrorUtil.checkNotNull(world, "minecraft world");
            this.recipeManager = world.getRecipeManager();
            this.ingredientManager = ingredientManager;
        }

        public List<RecipeEntry<AlchemyTableRecipe>> getAlchemyTableRecipes(IRecipeCategory<RecipeEntry<AlchemyTableRecipe>> alchemyTableCategory) {
            var validator = new CategoryRecipeValidator<>(alchemyTableCategory, ingredientManager, 6);
            return getValidAlchemyTableRecipes(recipeManager, AlchemyTableRecipe.Type.INSTANCE, validator);
        }

        public List<RecipeEntry<HerbalTableRecipe>> getHerbalTableRecipes(IRecipeCategory<RecipeEntry<HerbalTableRecipe>> herbalTableCategory) {
            var validator = new CategoryRecipeValidator<>(herbalTableCategory, ingredientManager, 2);
            return getValidHandledRecipes(recipeManager, HerbalTableRecipe.Type.INSTANCE, validator);
        }


        @SuppressWarnings("all")
        private static <C extends RecipeInput, T extends Recipe<C>> List<RecipeEntry<T>> getValidHandledRecipes(RecipeManager recipeManager, net.minecraft.recipe.RecipeType<T> recipeType, CategoryRecipeValidator<T> validator) {
            return recipeManager.listAllOfType(recipeType).stream().filter((r) -> {
                return validator.isRecipeValid(r) && validator.isRecipeHandled(r);
            }).toList();
        }

        @SuppressWarnings("all")
        private static List<RecipeEntry<AlchemyTableRecipe>> getValidAlchemyTableRecipes(
                RecipeManager recipeManager,
                net.minecraft.recipe.RecipeType<AlchemyTableRecipe> recipeType,
                CategoryRecipeValidator<AlchemyTableRecipe> validator
        ) {
            return recipeManager.listAllOfType(recipeType)
                    .stream()
                    .filter(r -> validator.isRecipeValid(r) && validator.isRecipeHandled(r))
                    .sorted(Comparator.comparing(recipeEntry -> recipeEntry.value().getOrder()))
                    .toList();
        }
    }

    private static class RepairDataMaker {
        public static List<IJeiAnvilRecipe> getAnvilRecipes(IVanillaRecipeFactory vanillaRecipeFactory) {
            return getRepairData().flatMap(repairData -> getRepairRecipes(repairData, vanillaRecipeFactory)).toList();
        }

        private static Stream<RepairData> getRepairData() {
            return Stream.of(
                    new RepairData(TCOTS_ToolMaterials.GVALCHIR.getRepairIngredient(),
                            new ItemStack(TCOTS_Items.GVALCHIR)),

                    new RepairData(TCOTS_ToolMaterials.MOONBLADE.getRepairIngredient(),
                            new ItemStack(TCOTS_Items.MOONBLADE)),

                    new RepairData(TCOTS_ToolMaterials.DYAEBL.getRepairIngredient(),
                            new ItemStack(TCOTS_Items.DYAEBL)),

                    new RepairData(TCOTS_ToolMaterials.WINTERS_BLADE.getRepairIngredient(),
                            new ItemStack(TCOTS_Items.WINTERS_BLADE)),

                    new RepairData(TCOTS_ToolMaterials.ARDAENYE.getRepairIngredient(),
                            new ItemStack(TCOTS_Items.ARDAENYE)),

                    new RepairData(TCOTS_ToolMaterials.ANCHOR.getRepairIngredient(),
                            new ItemStack(TCOTS_Items.GIANT_ANCHOR)),

                    new RepairData(TCOTS_ArmorMaterials.MANTICORE.value().repairIngredient().get(),
                            new ItemStack(TCOTS_Items.MANTICORE_ARMOR),
                            new ItemStack(TCOTS_Items.MANTICORE_TROUSERS),
                            new ItemStack(TCOTS_Items.MANTICORE_BOOTS)),

                    new RepairData(TCOTS_ArmorMaterials.WARRIORS_LEATHER.value().repairIngredient().get(),
                            new ItemStack(TCOTS_Items.WARRIORS_LEATHER_JACKET),
                            new ItemStack(TCOTS_Items.WARRIORS_LEATHER_TROUSERS),
                            new ItemStack(TCOTS_Items.WARRIORS_LEATHER_BOOTS)),

                    new RepairData(TCOTS_ArmorMaterials.RAVEN.value().repairIngredient().get(),
                            new ItemStack(TCOTS_Items.RAVENS_ARMOR),
                            new ItemStack(TCOTS_Items.RAVENS_TROUSERS),
                            new ItemStack(TCOTS_Items.RAVENS_BOOTS)
                            )
            );
        }

        private static Stream<IJeiAnvilRecipe> getRepairRecipes(RepairDataMaker.RepairData repairData, IVanillaRecipeFactory vanillaRecipeFactory) {
            Ingredient repairIngredient = repairData.getRepairIngredient();
            List<ItemStack> repairable = repairData.getRepairable();

            List<ItemStack> repairMaterials = List.of(repairIngredient.getMatchingStacks());

            return repairable.stream()
                    .mapMulti((itemStack, consumer) -> {
                        ItemStack damagedThreeQuarters = itemStack.copy();
                        damagedThreeQuarters.setDamage(damagedThreeQuarters.getMaxDamage() * 3 / 4);
                        ItemStack damagedHalf = itemStack.copy();
                        damagedHalf.setDamage(damagedHalf.getMaxDamage() / 2);



                        IJeiAnvilRecipe repairWithSame = vanillaRecipeFactory.createAnvilRecipe(List.of(damagedThreeQuarters), List.of(damagedThreeQuarters), List.of(damagedHalf));
                        consumer.accept(repairWithSame);

                        if (!repairMaterials.isEmpty()) {
                            ItemStack damagedFully = itemStack.copy();
                            damagedFully.setDamage(damagedFully.getMaxDamage());
                            IJeiAnvilRecipe repairWithMaterial = vanillaRecipeFactory.createAnvilRecipe(List.of(damagedFully), repairMaterials, List.of(damagedThreeQuarters));
                            consumer.accept(repairWithMaterial);
                        }
                    });
        }

        private static class RepairData {
            private final Ingredient repairIngredient;
            private final List<ItemStack> repairable;

            public RepairData(Ingredient repairIngredient, ItemStack... repairable) {
                this.repairIngredient = repairIngredient;
                this.repairable = List.of(repairable);
            }

            public Ingredient getRepairIngredient() {
                return repairIngredient;
            }

            public List<ItemStack> getRepairable() {
                return repairable;
            }
        }
    }
}
