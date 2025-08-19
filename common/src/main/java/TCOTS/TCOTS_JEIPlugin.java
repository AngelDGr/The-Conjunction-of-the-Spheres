package TCOTS;

import TCOTS.registry.TCOTS_Blocks;
import TCOTS.registry.TCOTS_Items;
import TCOTS.items.HerbalMixture;
import TCOTS.registry.TCOTS_ItemsMaterials;
import TCOTS.items.components.RecipeTeacherComponent;
import TCOTS.recipes.AlchemyTableRecipe;
import TCOTS.recipes.HerbalTableRecipe;
import TCOTS.screen.HerbalTableScreen;
import dev.architectury.platform.Platform;
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
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeInput;
import net.minecraft.world.item.crafting.RecipeManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Stream;

@JeiPlugin
public class TCOTS_JEIPlugin implements IModPlugin {

    public static final Supplier<RecipeType<RecipeHolder<AlchemyTableRecipe>>> ALCHEMY_TABLE =
            Platform.isFabric()?
                    () -> RecipeType.createFromVanilla(AlchemyTableRecipe.Type.INSTANCE):
                    RecipeType.createFromDeferredVanilla(()-> AlchemyTableRecipe.Type.INSTANCE);

    public static final Supplier<RecipeType<RecipeHolder<HerbalTableRecipe>>> HERBAL_TABLE =
            Platform.isFabric()?
                    () -> RecipeType.createFromVanilla(HerbalTableRecipe.Type.INSTANCE):
                    RecipeType.createFromDeferredVanilla(()-> HerbalTableRecipe.Type.INSTANCE);


    @Nullable
    private IRecipeCategory<RecipeHolder<AlchemyTableRecipe>> alchemyTableCategory;
    @Nullable
    private IRecipeCategory<RecipeHolder<HerbalTableRecipe>> herbalTableCategory;

    @Override
    public @NotNull ResourceLocation getPluginUid() {
        return ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "jei-witcher");
    }

    @Override
    public void registerItemSubtypes(final ISubtypeRegistration registration) {
        registration.registerSubtypeInterpreter(TCOTS_Items.ALCHEMY_FORMULA.get(), AlchemyFormulaInterpreter.INSTANCE);
    }

    public static class AlchemyFormulaInterpreter implements ISubtypeInterpreter<ItemStack> {
        public static final AlchemyFormulaInterpreter INSTANCE = new AlchemyFormulaInterpreter();

        private AlchemyFormulaInterpreter() {}


        @Override
        public @Nullable Object getSubtypeData(final ItemStack ingredient, @NotNull final UidContext context) {
            return ingredient.get(TCOTS_Items.RecipeTeacher());
        }

        @Override
        public @NotNull String getLegacyStringSubtypeInfo(final ItemStack ingredient, @NotNull final UidContext context) {
            if (!ingredient.has(TCOTS_Items.RecipeTeacher())) {
                return "";
            }

            final RecipeTeacherComponent recipeTeacher = ingredient.get(TCOTS_Items.RecipeTeacher());

            String formulaString=null;
            if(recipeTeacher != null){
                formulaString = recipeTeacher.recipeName();
            }

            return Objects.requireNonNullElse(formulaString, "");
        }
    }

    @Override
    public void registerCategories(@NotNull final IRecipeCategoryRegistration registration) {
        final IJeiHelpers jeiHelpers = registration.getJeiHelpers();
        final IGuiHelper guiHelper = jeiHelpers.getGuiHelper();

        registration.addRecipeCategories(
                alchemyTableCategory =  new AlchemyTableRecipeCategory(guiHelper),
                herbalTableCategory  =  new HerbalTableRecipeCategory(guiHelper)
        );
    }


    @Override
    public void registerRecipes(final IRecipeRegistration registration) {
        ErrorUtil.checkNotNull(alchemyTableCategory, "alchemyTableCategory");
        ErrorUtil.checkNotNull(herbalTableCategory, "herbalTableCategory");

        final IIngredientManager ingredientManager = registration.getIngredientManager();
        final TCOTSRecipes TCOTSRecipes = new TCOTSRecipes(ingredientManager);
        final IVanillaRecipeFactory vanillaRecipeFactory = registration.getVanillaRecipeFactory();


        registration.addRecipes(ALCHEMY_TABLE.get(), TCOTSRecipes.getAlchemyTableRecipes(alchemyTableCategory));
        registration.addRecipes(HERBAL_TABLE.get(), TCOTSRecipes.getHerbalTableRecipes(herbalTableCategory));

        registration.addRecipes(RecipeTypes.ANVIL, RepairDataMaker.getAnvilRecipes(vanillaRecipeFactory));
    }

    @Override
    public void registerRecipeCatalysts(final IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(TCOTS_Blocks.AlchemyTable()), ALCHEMY_TABLE.get());
        registration.addRecipeCatalyst(new ItemStack(TCOTS_Blocks.HerbalTable()), HERBAL_TABLE.get());
    }

    @Override
    public void registerModInfo(final IModInfoRegistration registration) {
        registration.addModAliases(TCOTS_Main.MOD_ID, "tcots", "witcher");
    }

    @Override
    public void registerGuiHandlers(final IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(HerbalTableScreen.class, 102, 48, 22, 15, HERBAL_TABLE.get());
    }

    private static class AlchemyTableRecipeCategory implements IRecipeCategory<RecipeHolder<AlchemyTableRecipe>> {
        private final IDrawable background;
        private final IDrawable icon;

        public AlchemyTableRecipeCategory(final IGuiHelper guiHelper) {

            background = guiHelper.drawableBuilder(ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "textures/gui/alchemy_table_jei.png"),
                            0,0,
                            118, 100)
                    .setTextureSize(118,100)
                    .build();

            icon = guiHelper.createDrawableItemStack(new ItemStack(TCOTS_Blocks.AlchemyTable()));
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
        public @NotNull RecipeType<RecipeHolder<AlchemyTableRecipe>> getRecipeType() {
            return TCOTS_JEIPlugin.ALCHEMY_TABLE.get();
        }

        @Override
        public @NotNull Component getTitle() {
            return TCOTS_Blocks.AlchemyTable().getName();
        }

        @Override
        public @NotNull IDrawable getIcon() {
            return icon;
        }

        @Override
        public void setRecipe(@NotNull final IRecipeLayoutBuilder builder, @NotNull final RecipeHolder<AlchemyTableRecipe> recipe, @NotNull final IFocusGroup focuses) {
            final List<ItemStack> potionInputs = recipe.value().returnItemStackWithQuantity();
            final ItemStack baseItem = recipe.value().getBaseItem();
            final ItemStack result = recipe.value().getResultItem(null);

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
        public void draw(@NotNull final RecipeHolder<AlchemyTableRecipe> recipe, @NotNull final IRecipeSlotsView recipeSlotsView, @NotNull final GuiGraphics guiGraphics, final double mouseX, final double mouseY) {
            final Minecraft minecraft = Minecraft.getInstance();
            this.background.draw(guiGraphics);
            if(minecraft.player != null && minecraft.player.getRecipeBook().contains(recipe)){
                return;
            }

            final String text = Translator.translateToLocalFormatted("gui.jei.tcots_witcher.requires_recipe");

            final Font font = minecraft.font;
            guiGraphics.drawString(font, text, 0, 0, 0xFF5555, true);

        }
    }

    private static class HerbalTableRecipeCategory implements IRecipeCategory<RecipeHolder<HerbalTableRecipe>> {
        private final IDrawable background;
        private final IDrawable icon;

        public HerbalTableRecipeCategory(final IGuiHelper guiHelper) {

            background = guiHelper.drawableBuilder(ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "textures/gui/herbal_table_jei.png"),
                            0,0,
                            157, 32)
                    .setTextureSize(157,32)
                    .addPadding(10, 0, 0 ,0)
                    .build();

            icon = guiHelper.createDrawableItemStack(new ItemStack(TCOTS_Blocks.HerbalTable()));
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
        public @NotNull RecipeType<RecipeHolder<HerbalTableRecipe>> getRecipeType() {
            return TCOTS_JEIPlugin.HERBAL_TABLE.get();
        }

        @Override
        public @NotNull Component getTitle() {
            return TCOTS_Blocks.HerbalTable().getName();
        }

        @Override
        public @NotNull IDrawable getIcon() {
            return icon;
        }

        @Override
        public void setRecipe(@NotNull final IRecipeLayoutBuilder builder, @NotNull final RecipeHolder<HerbalTableRecipe> recipeEntry, @NotNull final IFocusGroup focuses) {
            final HerbalTableRecipe recipe = recipeEntry.value();

            //Herb
            final ItemStack herb = new ItemStack(recipe.getHerb().getItem(), 1);

            //Bottle
            final ItemStack bottle =
                    recipe.getBasePotion() == 1?
                            PotionContents.createItemStack(Items.POTION, Potions.MUNDANE) :
                            recipe.getBasePotion() == 2? PotionContents.createItemStack(Items.POTION, Potions.THICK):
                                    PotionContents.createItemStack(Items.POTION, Potions.WATER);

            //Result
            final List<MobEffectInstance> totalEffectsFirst = new ArrayList<>();
            recipe.getEffects().forEach(effect -> {
                if (!effect.value().isBeneficial()) {
                    totalEffectsFirst.add(new MobEffectInstance(effect, effect.value().isInstantenous()? 1:recipe.getTickEffectTime(), recipe.getBadAmplifier()));
                } else {
                    totalEffectsFirst.add(new MobEffectInstance(effect, effect.value().isInstantenous()? 1:recipe.getTickEffectTime()));
                }
            });
            ItemStack result = recipe.getResultItem(null);

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
        public void draw(@NotNull final RecipeHolder<HerbalTableRecipe> recipe, @NotNull final IRecipeSlotsView recipeSlotsView, @NotNull final GuiGraphics guiGraphics, final double mouseX, final double mouseY) {
            IRecipeCategory.super.draw(recipe, recipeSlotsView, guiGraphics, mouseX, mouseY);
            final Minecraft minecraft = Minecraft.getInstance();
            final String text = Translator.translateToLocalFormatted("gui.jei.tcots_witcher.for_herb", recipe.value().getTickEffectTime()/20);

            final Font font = minecraft.font;
            guiGraphics.drawString(font, text, 0, 2, 43520, true);

            this.background.draw(guiGraphics);
        }
    }

    private static class TCOTSRecipes {
        private final RecipeManager recipeManager;
        private final IIngredientManager ingredientManager;

        public TCOTSRecipes(final IIngredientManager ingredientManager) {
            final Minecraft minecraft = Minecraft.getInstance();
            ErrorUtil.checkNotNull(minecraft, "minecraft");
            final ClientLevel world = minecraft.level;
            ErrorUtil.checkNotNull(world, "minecraft world");
            this.recipeManager = world.getRecipeManager();
            this.ingredientManager = ingredientManager;
        }

        public List<RecipeHolder<AlchemyTableRecipe>> getAlchemyTableRecipes(final IRecipeCategory<RecipeHolder<AlchemyTableRecipe>> alchemyTableCategory) {
            final var validator = new CategoryRecipeValidator<>(alchemyTableCategory, ingredientManager, 6);
            return getValidAlchemyTableRecipes(recipeManager, AlchemyTableRecipe.Type.INSTANCE, validator);
        }

        public List<RecipeHolder<HerbalTableRecipe>> getHerbalTableRecipes(final IRecipeCategory<RecipeHolder<HerbalTableRecipe>> herbalTableCategory) {
            final var validator = new CategoryRecipeValidator<>(herbalTableCategory, ingredientManager, 2);
            return getValidHandledRecipes(recipeManager, HerbalTableRecipe.Type.INSTANCE, validator);
        }


        @SuppressWarnings("all")
        private static <C extends RecipeInput, T extends Recipe<C>> List<RecipeHolder<T>> getValidHandledRecipes(RecipeManager recipeManager, net.minecraft.world.item.crafting.RecipeType<T> recipeType, CategoryRecipeValidator<T> validator) {
            return recipeManager.getAllRecipesFor(recipeType).stream().filter((r) -> {
                return validator.isRecipeValid(r) && validator.isRecipeHandled(r);
            }).toList();
        }

        @SuppressWarnings("all")
        private static List<RecipeHolder<AlchemyTableRecipe>> getValidAlchemyTableRecipes(
                RecipeManager recipeManager,
                net.minecraft.world.item.crafting.RecipeType<AlchemyTableRecipe> recipeType,
                CategoryRecipeValidator<AlchemyTableRecipe> validator
        ) {
            return recipeManager.getAllRecipesFor(recipeType)
                    .stream()
                    .filter(r -> validator.isRecipeValid(r) && validator.isRecipeHandled(r))
                    .sorted(Comparator.comparing(recipeEntry -> recipeEntry.value().getOrder()))
                    .toList();
        }
    }

    private static class RepairDataMaker {
        public static List<IJeiAnvilRecipe> getAnvilRecipes(final IVanillaRecipeFactory vanillaRecipeFactory) {
            return getRepairData().flatMap(repairData -> getRepairRecipes(repairData, vanillaRecipeFactory)).toList();
        }

        private static Stream<RepairData> getRepairData() {
            return Stream.of(
                    new RepairData(TCOTS_ItemsMaterials.Gvalchir().getRepairIngredient(),
                            new ItemStack(TCOTS_Items.GVALCHIR.get())),

                    new RepairData(TCOTS_ItemsMaterials.Moonblade().getRepairIngredient(),
                            new ItemStack(TCOTS_Items.MOONBLADE.get())),

                    new RepairData(TCOTS_ItemsMaterials.Dyaebl().getRepairIngredient(),
                            new ItemStack(TCOTS_Items.DYAEBL.get())),

                    new RepairData(TCOTS_ItemsMaterials.WintersBlade().getRepairIngredient(),
                            new ItemStack(TCOTS_Items.WINTERS_BLADE.get())),

                    new RepairData(TCOTS_ItemsMaterials.Ardaenye().getRepairIngredient(),
                            new ItemStack(TCOTS_Items.ARDAENYE.get())),

                    new RepairData(TCOTS_ItemsMaterials.Anchor().getRepairIngredient(),
                            new ItemStack(TCOTS_Items.GIANT_ANCHOR.get())),

                    new RepairData(TCOTS_ItemsMaterials.Manticore().value().repairIngredient().get(),
                            new ItemStack(TCOTS_Items.MANTICORE_ARMOR.get()),
                            new ItemStack(TCOTS_Items.MANTICORE_TROUSERS.get()),
                            new ItemStack(TCOTS_Items.MANTICORE_BOOTS.get())),

                    new RepairData(TCOTS_ItemsMaterials.WarriorsLeather().value().repairIngredient().get(),
                            new ItemStack(TCOTS_Items.WARRIORS_LEATHER_JACKET.get()),
                            new ItemStack(TCOTS_Items.WARRIORS_LEATHER_TROUSERS.get()),
                            new ItemStack(TCOTS_Items.WARRIORS_LEATHER_BOOTS.get())),

                    new RepairData(TCOTS_ItemsMaterials.Raven().value().repairIngredient().get(),
                            new ItemStack(TCOTS_Items.RAVENS_ARMOR.get()),
                            new ItemStack(TCOTS_Items.RAVENS_TROUSERS.get()),
                            new ItemStack(TCOTS_Items.RAVENS_BOOTS.get())
                            )
            );
        }

        private static Stream<IJeiAnvilRecipe> getRepairRecipes(final RepairDataMaker.RepairData repairData, final IVanillaRecipeFactory vanillaRecipeFactory) {
            final Ingredient repairIngredient = repairData.getRepairIngredient();
            final List<ItemStack> repairable = repairData.getRepairable();

            final List<ItemStack> repairMaterials = List.of(repairIngredient.getItems());

            return repairable.stream()
                    .mapMulti((itemStack, consumer) -> {
                        final ItemStack damagedThreeQuarters = itemStack.copy();
                        damagedThreeQuarters.setDamageValue(damagedThreeQuarters.getMaxDamage() * 3 / 4);
                        final ItemStack damagedHalf = itemStack.copy();
                        damagedHalf.setDamageValue(damagedHalf.getMaxDamage() / 2);



                        final IJeiAnvilRecipe repairWithSame = vanillaRecipeFactory.createAnvilRecipe(List.of(damagedThreeQuarters), List.of(damagedThreeQuarters), List.of(damagedHalf));
                        consumer.accept(repairWithSame);

                        if (!repairMaterials.isEmpty()) {
                            final ItemStack damagedFully = itemStack.copy();
                            damagedFully.setDamageValue(damagedFully.getMaxDamage());
                            final IJeiAnvilRecipe repairWithMaterial = vanillaRecipeFactory.createAnvilRecipe(List.of(damagedFully), repairMaterials, List.of(damagedThreeQuarters));
                            consumer.accept(repairWithMaterial);
                        }
                    });
        }

        private static class RepairData {
            private final Ingredient repairIngredient;
            private final List<ItemStack> repairable;

            public RepairData(final Ingredient repairIngredient, final ItemStack... repairable) {
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
