package mors.neoforge.tcots.datagen.providers;

import mors.tcots.TCOTS_Main;
import mors.tcots.recipes.dynamic.DynamicRecipe;
import mors.tcots.recipes.dynamic.RecipeAdvancementJsonBuilder;
import mors.tcots.recipes.dynamic.ShapedRawRecipeBuilder;
import mors.tcots.registry.TCOTS_Items;
import mors.tcots.registry.TCOTS_Tags;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class TCOTS_DynamicRecipesGenerator implements DataProvider {

    private final PackOutput output;
    private final List<DynamicRecipe> recipes=new ArrayList<>();
    private final Map<String, RecipeAdvancementJsonBuilder> advancements=new HashMap<>();


    public TCOTS_DynamicRecipesGenerator(final PackOutput output) {
        this.output = output;
    }

    public void buildRecipes(final @NotNull List<DynamicRecipe> exporter) {

        //Swords
        {
            // G'valchir
            {
                ShapedRawRecipeBuilder.shaped(RecipeCategory.COMBAT, TCOTS_Items.GVALCHIR.get())
                        .id(TCOTS_Main.id("gvalchir"), "rpg")
                        .pattern("FHD")
                        .pattern("HSH")
                        .pattern("DHF")

                        .define('F', TCOTS_Items.FOGLET_TEETH.get())
                        .define('H', TCOTS_Items.BULLVORE_HORN_FRAGMENT.get())
                        .define('D', TCOTS_Items.DEVOURER_TEETH.get())
                        .defineDynamic('S', Items.IRON_SWORD, TCOTS_Items.WitcherRPG.DARK_IRON_WITCHER_SWORD)

                        .unlockedByDynamic(RecipeProvider.getHasName(TCOTS_Items.BULLVORE_HORN_FRAGMENT.get()), TCOTS_Items.BULLVORE_HORN_FRAGMENT.get(), this.advancements)
                        .saveDynamic(exporter);
            }

            // Moonblade
            {
                ShapedRawRecipeBuilder.shaped(RecipeCategory.COMBAT, TCOTS_Items.MOONBLADE.get())
                        .id(TCOTS_Main.id("moonblade"), "rpg")
                        .pattern("HBG")
                        .pattern("BSB")
                        .pattern("GBH")

                        .define('G', TCOTS_Items.GRAVEIR_BONE.get())
                        .define('H', TCOTS_Items.NEKKER_HEART.get())
                        .define('B', TCOTS_Tags.Item.MONSTER_BLOOD)
                        .defineDynamic('S', Items.GOLDEN_SWORD, TCOTS_Items.WitcherRPG.SILVER_WITCHER_SWORD)

                        .unlockedByDynamic(RecipeProvider.getHasName(TCOTS_Items.GRAVEIR_BONE.get()), TCOTS_Items.GRAVEIR_BONE.get(), this.advancements)
                        .saveDynamic(exporter);
            }


            // Ard'aenye
            {
                ShapedRawRecipeBuilder.shaped(RecipeCategory.COMBAT, TCOTS_Items.ARDAENYE.get())
                        .id(TCOTS_Main.id("ardaenye"), "rpg")
                        .pattern("BDC")
                        .pattern("DSD")
                        .pattern("CDB")

                        .define('C', TCOTS_Items.CADAVERINE.get())
                        .define('D', TCOTS_Items.DEVOURER_TEETH.get())
                        .define('B', Items.BLAZE_POWDER)
                        .defineDynamic('S', Items.DIAMOND_SWORD, TCOTS_Items.WitcherRPG.DARK_STEEL_WITCHER_SWORD)

                        .unlockedByDynamic(RecipeProvider.getHasName(TCOTS_Items.DEVOURER_TEETH.get()), TCOTS_Items.DEVOURER_TEETH.get(), this.advancements)
                        .saveDynamic(exporter);
            }


            // D'yaebl
            {
                ShapedRawRecipeBuilder.shaped(RecipeCategory.COMBAT, TCOTS_Items.DYAEBL.get())
                        .id(TCOTS_Main.id("dyaebl"), "rpg")
                        .pattern("GRG")
                        .pattern("RSR")
                        .pattern("GRG")

                        .define('R', TCOTS_Items.ROTFIEND_BLOOD.get())
                        .define('G', TCOTS_Items.GHOUL_BLOOD.get())
                        .defineDynamic('S', Items.IRON_SWORD, TCOTS_Items.WitcherRPG.STEEL_WITCHER_SWORD)

                        .unlockedByDynamic(RecipeProvider.getHasName(TCOTS_Items.ROTFIEND_BLOOD.get()), TCOTS_Items.ROTFIEND_BLOOD.get(), this.advancements)
                        .saveDynamic(exporter);
            }

        }

        //Armor
        {
            //Manticore Armor
            {
                //Band
                {
                    ShapedRawRecipeBuilder.shaped(RecipeCategory.COMBAT, TCOTS_Items.MANTICORE_HEAD.get())
                            .id(TCOTS_Main.id("manticore_head"), "rpg")
                            .pattern("LIL")
                            .define('L', TCOTS_Items.CURED_MONSTER_LEATHER.get())
                            .defineDynamic('I', Items.IRON_INGOT, TCOTS_Items.WitcherRPG.DARK_IRON_INGOT)

                            .unlockedByDynamic(RecipeProvider.getHasName(TCOTS_Items.CURED_MONSTER_LEATHER.get()), TCOTS_Items.CURED_MONSTER_LEATHER.get(), this.advancements)
                            .saveDynamic(exporter);
                }

                //Chestplate
                {
                    ShapedRawRecipeBuilder.shaped(RecipeCategory.COMBAT, TCOTS_Items.MANTICORE_ARMOR.get())
                            .id(TCOTS_Main.id("manticore_armor"), "rpg")
                            .pattern("L L")
                            .pattern("IEI")
                            .pattern("ELE")
                            .define('L', TCOTS_Items.CURED_MONSTER_LEATHER.get())
                            .define('E', TCOTS_Items.NEKKER_EYE.get())
                            .defineDynamic('I', Items.IRON_INGOT, TCOTS_Items.WitcherRPG.DARK_IRON_INGOT)

                            .unlockedByDynamic(RecipeProvider.getHasName(TCOTS_Items.CURED_MONSTER_LEATHER.get()), TCOTS_Items.CURED_MONSTER_LEATHER.get(), this.advancements)
                            .saveDynamic(exporter);
                }

                //Trousers
                {
                    ShapedRawRecipeBuilder.shaped(RecipeCategory.COMBAT, TCOTS_Items.MANTICORE_TROUSERS.get())
                            .id(TCOTS_Main.id("manticore_trousers"), "rpg")
                            .pattern("LIL")
                            .pattern("B B")
                            .pattern("L L")
                            .define('L', TCOTS_Items.CURED_MONSTER_LEATHER.get())
                            .define('B', TCOTS_Tags.Item.MONSTER_BLOOD)
                            .defineDynamic('I', Items.IRON_INGOT, TCOTS_Items.WitcherRPG.DARK_IRON_INGOT)

                            .unlockedByDynamic(RecipeProvider.getHasName(TCOTS_Items.CURED_MONSTER_LEATHER.get()), TCOTS_Items.CURED_MONSTER_LEATHER.get(), this.advancements)
                            .saveDynamic(exporter);
                }

                //Boots
                {
                    ShapedRawRecipeBuilder.shaped(RecipeCategory.COMBAT, TCOTS_Items.MANTICORE_BOOTS.get())
                            .id(TCOTS_Main.id("manticore_boots"), "rpg")
                            .pattern("L L")
                            .pattern("B B")
                            .pattern("I I")
                            .define('L', TCOTS_Items.CURED_MONSTER_LEATHER.get())
                            .define('B', TCOTS_Tags.Item.MONSTER_BLOOD)
                            .defineDynamic('I', Items.IRON_INGOT, TCOTS_Items.WitcherRPG.DARK_IRON_INGOT)

                            .unlockedByDynamic(RecipeProvider.getHasName(TCOTS_Items.CURED_MONSTER_LEATHER.get()), TCOTS_Items.CURED_MONSTER_LEATHER.get(), this.advancements)
                            .saveDynamic(exporter);
                }
            }

            //Raven's Armor
            {
                //Band
                {
                    ShapedRawRecipeBuilder.shaped(RecipeCategory.COMBAT, TCOTS_Items.RAVENS_HEAD.get())
                            .id(TCOTS_Main.id("ravens_head"), "rpg")
                            .pattern("DLD")
                            .define('L', TCOTS_Items.CURED_MONSTER_LEATHER.get())
                            .defineDynamic('D', Items.DIAMOND, TCOTS_Items.WitcherRPG.METEORITE_SILVER_INGOT)

                            .unlockedByDynamic(RecipeProvider.getHasName(TCOTS_Items.CURED_MONSTER_LEATHER.get()), TCOTS_Items.CURED_MONSTER_LEATHER.get(), this.advancements)
                            .saveDynamic(exporter);
                }

                //Chestplate
                {
                    ShapedRawRecipeBuilder.shaped(RecipeCategory.COMBAT, TCOTS_Items.RAVENS_ARMOR.get())
                            .id(TCOTS_Main.id("ravens_armor"), "rpg")
                            .pattern("D D")
                            .pattern("LDL")
                            .pattern("GIG")
                            .define('L', TCOTS_Items.CURED_MONSTER_LEATHER.get())
                            .define('G', TCOTS_Items.GRAVEIR_BONE.get())
                            .defineDynamic('I', Items.IRON_INGOT, TCOTS_Items.WitcherRPG.SILVER_INGOT)
                            .defineDynamic('D', Items.DIAMOND, TCOTS_Items.WitcherRPG.METEORITE_SILVER_INGOT)

                            .unlockedByDynamic(RecipeProvider.getHasName(TCOTS_Items.CURED_MONSTER_LEATHER.get()), TCOTS_Items.CURED_MONSTER_LEATHER.get(), this.advancements)
                            .saveDynamic(exporter);
                }

                //Trousers
                {
                    ShapedRawRecipeBuilder.shaped(RecipeCategory.COMBAT, TCOTS_Items.RAVENS_TROUSERS.get())
                            .id(TCOTS_Main.id("ravens_trousers"), "rpg")
                            .pattern("BLB")
                            .pattern("D D")
                            .pattern("L L")
                            .define('L', TCOTS_Items.CURED_MONSTER_LEATHER.get())
                            .define('B', TCOTS_Items.BULLVORE_HORN_FRAGMENT.get())
                            .defineDynamic('D', Items.DIAMOND, TCOTS_Items.WitcherRPG.METEORITE_SILVER_INGOT)

                            .unlockedByDynamic(RecipeProvider.getHasName(TCOTS_Items.CURED_MONSTER_LEATHER.get()), TCOTS_Items.CURED_MONSTER_LEATHER.get(), this.advancements)
                            .saveDynamic(exporter);
                }

                //Boots
                {
                    ShapedRawRecipeBuilder.shaped(RecipeCategory.COMBAT, TCOTS_Items.RAVENS_BOOTS.get())
                            .id(TCOTS_Main.id("ravens_boots"), "rpg")
                            .pattern("D D")
                            .pattern("L L")
                            .pattern("T T")
                            .define('L', TCOTS_Items.CURED_MONSTER_LEATHER.get())
                            .define('T', TCOTS_Items.DEVOURER_TEETH.get())
                            .defineDynamic('D', Items.DIAMOND, TCOTS_Items.WitcherRPG.METEORITE_SILVER_INGOT)

                            .unlockedByDynamic(RecipeProvider.getHasName(TCOTS_Items.CURED_MONSTER_LEATHER.get()), TCOTS_Items.CURED_MONSTER_LEATHER.get(), this.advancements)
                            .saveDynamic(exporter);
                }
            }
        }

        //Horse Armor
        {
            ShapedRawRecipeBuilder.shaped(RecipeCategory.MISC, TCOTS_Items.KNIGHT_ERRANTS_HORSE_ARMOR.get())
                    .id(TCOTS_Main.id("knight_errants_horse_armor"), "rpg")
                    .pattern("  I")
                    .pattern("IAN")
                    .pattern("IIN")

                    .define('A', Items.IRON_HORSE_ARMOR)
                    .defineDynamic('N', Items.IRON_NUGGET, TCOTS_Items.WitcherRPG.STEEL_NUGGET)
                    .defineDynamic('I', Items.IRON_INGOT, TCOTS_Items.WitcherRPG.STEEL_INGOT)

                    .unlockedByDynamic(RecipeProvider.getHasName(Items.IRON_HORSE_ARMOR), Items.IRON_HORSE_ARMOR, this.advancements)
                    .saveDynamic(exporter);
        }

        //Manticore Medallion
        {
            ShapedRawRecipeBuilder.shaped(RecipeCategory.COMBAT, TCOTS_Items.MANTICORE_SCHOOL_MEDALLION.get())
                    .id(TCOTS_Main.id("manticore_school_medallion"))
                    .pattern("SCS")
                    .pattern("DPD")
                    .pattern("MSM")
                    .define('C', Items.CHAIN)
                    .define('S', TCOTS_Items.WitcherRPG.SILVER_INGOT)
                    .define('P', TCOTS_Items.WitcherRPG.PURE_SILVER)
                    .define('D', TCOTS_Items.WitcherRPG.DARK_IRON_INGOT)
                    .define('M', TCOTS_Items.WitcherRPG.METEORITE_SILVER_INGOT)

                    .unlockedBy("has_pure_silver", TCOTS_Items.WitcherRPG.PURE_SILVER, this.advancements)
                    .save(exporter);
        }
    }

    @Override
    public @NotNull CompletableFuture<?> run(final @NotNull CachedOutput output) {
        final List<CompletableFuture<?>> futures = new ArrayList<>();
        buildRecipes(recipes);

        for (final DynamicRecipe recipe: recipes) {


            final Path recipesPath = this.output.getOutputFolder()
                    .resolve("data/"
                            + recipe.id().getNamespace()
                            + "/recipe/"
                            + recipe.id().getPath()
                            + ".json");

            //If it has actually an advancement
            if(advancements.containsKey(recipe.id().getPath())){
                final Path advancementsPath = this.output.getOutputFolder()
                        .resolve("data/"
                                + recipe.id().getNamespace()
                                + "/advancement/recipes/"
                                + recipe.category().getFolderName()+"/"
                                + recipe.id().getPath()
                                + ".json");

                futures.add(DataProvider.saveStable(output, advancements.get(recipe.id().getPath()).export(), advancementsPath));
            }

            futures.add(DataProvider.saveStable(output, recipe.export(), recipesPath));
        }

        return CompletableFuture.allOf(futures.toArray(CompletableFuture[]::new));
    }

    @Override
    public @NotNull String getName() {
        return "Generic JSON Provider";
    }
}
