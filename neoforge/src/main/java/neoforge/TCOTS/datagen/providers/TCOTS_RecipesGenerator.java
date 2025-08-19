package neoforge.TCOTS.datagen.providers;

import TCOTS.TCOTS_Main;
import TCOTS.registry.TCOTS_Tags;
import TCOTS.recipes.AlchemyTableRecipeCategory;
import TCOTS.recipes.AlchemyTableRecipeJsonBuilder;
import TCOTS.recipes.HerbalTableRecipeJsonBuilder;
import TCOTS.registry.TCOTS_Items;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class TCOTS_RecipesGenerator extends RecipeProvider {

    public TCOTS_RecipesGenerator(final PackOutput output, final CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider);
    }

    @Override
    public void buildRecipes(final @NotNull RecipeOutput exporter) {
        //Crafting Table
        {
            //Alchemy Table
            {
                ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, TCOTS_Items.ALCHEMY_TABLE_ITEM.get())
                        .pattern("B B")
                        .pattern("CWC")
                        .pattern("WWW")
                        .define('B', Items.GLASS_BOTTLE)
                        .define('C', Items.COBBLESTONE)
                        .define('W', ItemTags.PLANKS)

                        .unlockedBy(RecipeProvider.getHasName(Items.GLASS_BOTTLE), RecipeProvider.has(Items.GLASS_BOTTLE))
                        .save(exporter);
            }

            //Herbal Table
            {
                ShapedRecipeBuilder.shaped(RecipeCategory.DECORATIONS, TCOTS_Items.HERBAL_TABLE_ITEM.get())
                        .pattern("FF")
                        .pattern("WW")
                        .pattern("WW")
                        .define('F', ItemTags.SMALL_FLOWERS)
                        .define('W', ItemTags.PLANKS)

                        .unlockedBy(RecipeProvider.getHasName(Items.DANDELION), RecipeProvider.has(Items.DANDELION))
                        .unlockedBy(RecipeProvider.getHasName(Items.POPPY), RecipeProvider.has(Items.POPPY))
                        .unlockedBy(RecipeProvider.getHasName(Items.CORNFLOWER), RecipeProvider.has(Items.CORNFLOWER))
                        .save(exporter);
            }

            //Ingredients Crafting
            {
                ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, TCOTS_Items.CURED_MONSTER_LEATHER.get(), 2)
                        .requires(TCOTS_Items.CADAVERINE.get())
                        .requires(TCOTS_Tags.MONSTER_BLOOD)
                        .requires(Items.ROTTEN_FLESH)
                        .requires(Items.LEATHER)

                        .unlockedBy(RecipeProvider.getHasName(Items.LEATHER), RecipeProvider.has(Items.LEATHER))
                        .save(exporter);
            }

            //Crossbows
            {
                //Knight Crossbow
                {
                    ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, TCOTS_Items.KNIGHT_CROSSBOW.get())
                            .pattern("IWI")
                            .pattern("LHL")
                            .pattern(" S ")
                            .define('I', Items.IRON_BLOCK)
                            .define('W', ItemTags.WOOL)
                            .define('L', Items.LEATHER)
                            .define('H', Items.TRIPWIRE_HOOK)
                            .define('S', Items.STICK)

                            .unlockedBy(RecipeProvider.getHasName(Items.IRON_BLOCK), RecipeProvider.has(Items.IRON_BLOCK))
                            .unlockedBy(RecipeProvider.getHasName(Items.LEATHER), RecipeProvider.has(Items.LEATHER))
                            .unlockedBy(RecipeProvider.getHasName(Items.TRIPWIRE_HOOK), RecipeProvider.has(Items.TRIPWIRE_HOOK))
                            .save(exporter);
                }

                //Crossbow Bolts
                {
                    //Normal Bolt
                    {
                        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, TCOTS_Items.BASE_BOLT.get(), 2)
                                .pattern(" I ")
                                .pattern("FSF")
                                .define('I', Items.IRON_INGOT)
                                .define('F', Items.FEATHER)
                                .define('S', Items.STICK)

                                .unlockedBy(RecipeProvider.getHasName(Items.IRON_INGOT), RecipeProvider.has(Items.IRON_INGOT))
                                .unlockedBy(RecipeProvider.getHasName(Items.FEATHER), RecipeProvider.has(Items.FEATHER))
                                .save(exporter);
                    }

                    //Blunt Bolt
                    {
                        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, TCOTS_Items.BLUNT_BOLT.get(), 2)
                                .pattern("  P")
                                .pattern("L# ")
                                .pattern("TL ")
                                .define('P', Items.GOLD_BLOCK)
                                .define('L', Items.IRON_INGOT)
                                .define('T', Items.IRON_BLOCK)
                                .define('#', TCOTS_Items.BASE_BOLT.get())

                                .unlockedBy(RecipeProvider.getHasName(TCOTS_Items.BASE_BOLT.get()), RecipeProvider.has(TCOTS_Items.BASE_BOLT.get()))
                                .save(exporter);
                    }

                    // Precision Bolt
                    {
                        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, TCOTS_Items.PRECISION_BOLT.get(), 2)
                                .pattern("  P")
                                .pattern("L# ")
                                .pattern("LL ")
                                .define('P', Items.IRON_NUGGET)
                                .define('L', Items.FEATHER)
                                .define('#', TCOTS_Items.BASE_BOLT.get())

                                .unlockedBy(RecipeProvider.getHasName(TCOTS_Items.BASE_BOLT.get()), RecipeProvider.has(TCOTS_Items.BASE_BOLT.get()))
                                .save(exporter);
                    }

                    // Exploding Bolt
                    {
                        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, TCOTS_Items.EXPLODING_BOLT.get(), 2)
                                .pattern("  P")
                                .pattern("L# ")
                                .pattern("TL ")
                                .define('P', TCOTS_Items.STAMMELFORDS_DUST.get())
                                .define('L', Items.STRING)
                                .define('T', Items.PAPER)
                                .define('#', TCOTS_Items.BASE_BOLT.get())

                                .unlockedBy(RecipeProvider.getHasName(TCOTS_Items.BASE_BOLT.get()), RecipeProvider.has(TCOTS_Items.BASE_BOLT.get()))
                                .save(exporter);
                    }

                    // Broadhead Bolt
                    {
                        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, TCOTS_Items.BROADHEAD_BOLT.get(), 2)
                                .pattern("  P")
                                .pattern("L# ")
                                .pattern("TL ")
                                .define('P', TCOTS_Items.FOGLET_TEETH.get())
                                .define('L', Items.IRON_NUGGET)
                                .define('T', Items.FEATHER)
                                .define('#', TCOTS_Items.BASE_BOLT.get())

                                .unlockedBy(RecipeProvider.getHasName(TCOTS_Items.BASE_BOLT.get()), RecipeProvider.has(TCOTS_Items.BASE_BOLT.get()))
                                .save(exporter);
                    }

                }
            }

            //Armors
            {
                //Warrior's Leather Armor
                {
                    //Jacket
                    {
                        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, TCOTS_Items.WARRIORS_LEATHER_JACKET.get())
                                .pattern("I I")
                                .pattern("LLL")
                                .pattern("NIN")
                                .define('L', Items.LEATHER)
                                .define('I', Items.IRON_INGOT)
                                .define('N', Items.IRON_NUGGET)

                                .unlockedBy(RecipeProvider.getHasName(Items.LEATHER), RecipeProvider.has(Items.LEATHER))
                                .save(exporter);
                    }

                    //Trousers
                    {
                        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, TCOTS_Items.WARRIORS_LEATHER_TROUSERS.get())
                                .pattern("NIN")
                                .pattern("L L")
                                .pattern("L L")
                                .define('L', Items.LEATHER)
                                .define('I', Items.IRON_INGOT)
                                .define('N', Items.IRON_NUGGET)

                                .unlockedBy(RecipeProvider.getHasName(Items.LEATHER), RecipeProvider.has(Items.LEATHER))
                                .save(exporter);
                    }

                    //Boots
                    {
                        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, TCOTS_Items.WARRIORS_LEATHER_BOOTS.get())
                                .pattern("L L")
                                .pattern("I I")
                                .define('L', Items.LEATHER)
                                .define('I', Items.IRON_INGOT)

                                .unlockedBy(RecipeProvider.getHasName(Items.LEATHER), RecipeProvider.has(Items.LEATHER))
                                .save(exporter);
                    }
                }

                //Manticore Armor
                {
                    //Chestplate
                    {
                        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, TCOTS_Items.MANTICORE_ARMOR.get())
                                .pattern("L L")
                                .pattern("IEI")
                                .pattern("ELE")
                                .define('L', TCOTS_Items.CURED_MONSTER_LEATHER.get())
                                .define('E', TCOTS_Items.NEKKER_EYE.get())
                                .define('I', Items.IRON_INGOT)

                                .unlockedBy(RecipeProvider.getHasName(TCOTS_Items.CURED_MONSTER_LEATHER.get()), RecipeProvider.has(TCOTS_Items.CURED_MONSTER_LEATHER.get()))
                                .save(exporter);
                    }

                    //Trousers
                    {
                        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, TCOTS_Items.MANTICORE_TROUSERS.get())
                                .pattern("LIL")
                                .pattern("B B")
                                .pattern("L L")
                                .define('L', TCOTS_Items.CURED_MONSTER_LEATHER.get())
                                .define('I', Items.IRON_INGOT)
                                .define('B', TCOTS_Tags.MONSTER_BLOOD)

                                .unlockedBy(RecipeProvider.getHasName(TCOTS_Items.CURED_MONSTER_LEATHER.get()), RecipeProvider.has(TCOTS_Items.CURED_MONSTER_LEATHER.get()))
                                .save(exporter);
                    }

                    //Boots
                    {
                        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, TCOTS_Items.MANTICORE_BOOTS.get())
                                .pattern("L L")
                                .pattern("B B")
                                .pattern("I I")
                                .define('L', TCOTS_Items.CURED_MONSTER_LEATHER.get())
                                .define('I', Items.IRON_INGOT)
                                .define('B', TCOTS_Tags.MONSTER_BLOOD)

                                .unlockedBy(RecipeProvider.getHasName(TCOTS_Items.CURED_MONSTER_LEATHER.get()), RecipeProvider.has(TCOTS_Items.CURED_MONSTER_LEATHER.get()))
                                .save(exporter);
                    }
                }

                //Raven's Armor
                {
                    //Chestplate
                    {
                        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, TCOTS_Items.RAVENS_ARMOR.get())
                                .pattern("D D")
                                .pattern("LDL")
                                .pattern("GIG")
                                .define('D', Items.DIAMOND)
                                .define('L', TCOTS_Items.CURED_MONSTER_LEATHER.get())
                                .define('G', TCOTS_Items.GRAVEIR_BONE.get())
                                .define('I', Items.IRON_INGOT)

                                .unlockedBy(RecipeProvider.getHasName(TCOTS_Items.CURED_MONSTER_LEATHER.get()), RecipeProvider.has(TCOTS_Items.CURED_MONSTER_LEATHER.get()))
                                .save(exporter);
                    }

                    //Trousers
                    {
                        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, TCOTS_Items.RAVENS_TROUSERS.get())
                                .pattern("BLB")
                                .pattern("D D")
                                .pattern("L L")
                                .define('D', Items.DIAMOND)
                                .define('L', TCOTS_Items.CURED_MONSTER_LEATHER.get())
                                .define('B', TCOTS_Items.BULLVORE_HORN_FRAGMENT.get())

                                .unlockedBy(RecipeProvider.getHasName(TCOTS_Items.CURED_MONSTER_LEATHER.get()), RecipeProvider.has(TCOTS_Items.CURED_MONSTER_LEATHER.get()))
                                .save(exporter);
                    }

                    //Boots
                    {
                        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, TCOTS_Items.RAVENS_BOOTS.get())
                                .pattern("D D")
                                .pattern("L L")
                                .pattern("T T")
                                .define('D', Items.DIAMOND)
                                .define('L', TCOTS_Items.CURED_MONSTER_LEATHER.get())
                                .define('T', TCOTS_Items.DEVOURER_TEETH.get())

                                .unlockedBy(RecipeProvider.getHasName(TCOTS_Items.CURED_MONSTER_LEATHER.get()), RecipeProvider.has(TCOTS_Items.CURED_MONSTER_LEATHER.get()))
                                .save(exporter);
                    }
                }
            }


            //Horse Armors
            {
                //Tundra Armor
                {
                    ShapedRecipeBuilder.shaped(RecipeCategory.MISC, TCOTS_Items.TUNDRA_HORSE_ARMOR.get())
                            .pattern("WWI")
                            .pattern("LAI")
                            .pattern("LLL")
                            .define('L', Items.LEATHER)
                            .define('I', Items.IRON_INGOT)
                            .define('W', ItemTags.WOOL)
                            .define('A', Items.LEATHER_HORSE_ARMOR)

                            .unlockedBy(RecipeProvider.getHasName(Items.LEATHER_HORSE_ARMOR), RecipeProvider.has(Items.LEATHER_HORSE_ARMOR))
                            .save(exporter);
                }
            }

            //Bone Meal from bones
            {
                //Devourer teeth
                {
                    ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.BONE_MEAL, 12)
                            .requires(TCOTS_Items.DEVOURER_TEETH.get())
                            .group("bonemeal")

                            .unlockedBy(RecipeProvider.getHasName(TCOTS_Items.DEVOURER_TEETH.get()), RecipeProvider.has(TCOTS_Items.DEVOURER_TEETH.get()))
                            .save(exporter, ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "bone_meal_from_devourer_teeth"));
                }

                //Graveir bone
                {
                    ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.BONE_MEAL, 16)
                            .requires(TCOTS_Items.GRAVEIR_BONE.get())
                            .group("bonemeal")

                            .unlockedBy(RecipeProvider.getHasName(TCOTS_Items.GRAVEIR_BONE.get()), RecipeProvider.has(TCOTS_Items.GRAVEIR_BONE.get()))
                            .save(exporter, ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "bone_meal_from_graveir_bone"));
                }
            }

            //Cadaverine crafting
            {
                //Extract cadaverine
                {
                    ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, TCOTS_Items.CADAVERINE.get(), 3)
                            .requires(TCOTS_Items.BLOEDZUIGER_BLOOD.get())

                            .unlockedBy(RecipeProvider.getHasName(TCOTS_Items.BLOEDZUIGER_BLOOD.get()), RecipeProvider.has(TCOTS_Items.BLOEDZUIGER_BLOOD.get()))
                            .save(exporter, ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "bloedzuiger_to_cadaverine"));
                }

                //Head
                {
                    ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.SKELETON_SKULL)
                            .requires(TCOTS_Items.CADAVERINE.get())
                            .requires(Items.ZOMBIE_HEAD)

                            .unlockedBy(RecipeProvider.getHasName(TCOTS_Items.CADAVERINE.get()), RecipeProvider.has(TCOTS_Items.CADAVERINE.get()))
                            .save(exporter, ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "cadaverine_decay_head"));
                }

                //Bone
                {
                    ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.BONE, 3)
                            .requires(TCOTS_Items.CADAVERINE.get())
                            .requires(TCOTS_Tags.DECAYING_FLESH)

                            .unlockedBy(RecipeProvider.getHasName(TCOTS_Items.CADAVERINE.get()), RecipeProvider.has(TCOTS_Items.CADAVERINE.get()))
                            .save(exporter, ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "cadaverine_decay_flesh"));

                    ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.BONE, 2)
                            .requires(TCOTS_Items.CADAVERINE.get())
                            .requires(Items.ROTTEN_FLESH)

                            .unlockedBy(RecipeProvider.getHasName(TCOTS_Items.CADAVERINE.get()), RecipeProvider.has(TCOTS_Items.CADAVERINE.get()))
                            .save(exporter, ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "cadaverine_decay_rotten"));
                }
            }
        }

        //Alchemy Table
        {
            float order = 0;
            //Potions
            {

                //Swallow
                {
                    order=order+0.01f;
                    AlchemyTableRecipeJsonBuilder.createPotion(
                                    order, TCOTS_Items.SWALLOW_POTION.get(),
                                    ingredientsList(
                                            of(TCOTS_Items.CELANDINE.get(), 5),
                                            of(TCOTS_Items.DROWNER_BRAIN.get(), 1)))
                            .offerTo(exporter);

                    order=order+0.01f;
                    AlchemyTableRecipeJsonBuilder.createPotion(
                            order, TCOTS_Items.SWALLOW_POTION_ENHANCED.get(), 1,
                            ingredientsList(
                                    of(TCOTS_Items.DROWNER_BRAIN.get(), 5),
                                    of(TCOTS_Items.CELANDINE.get(), 6),
                                    of(Items.LILY_OF_THE_VALLEY, 4)),
                            TCOTS_Items.SWALLOW_POTION.get()).offerTo(exporter);

                    order=order+0.01f;
                    AlchemyTableRecipeJsonBuilder.createPotion(
                            order, TCOTS_Items.SWALLOW_POTION_SUPERIOR.get(), 2,
                            ingredientsList(
                                    Items.GLOW_BERRIES, 6,
                                    TCOTS_Items.CELANDINE.get(), 4,
                                    TCOTS_Items.CROWS_EYE.get(), 4,
                                    TCOTS_Items.VITRIOL.get(), 2),
                            TCOTS_Items.SWALLOW_POTION_ENHANCED.get()).offerTo(exporter);

                    order=order+0.01f;
                    AlchemyTableRecipeJsonBuilder.createPotionSplash(
                            order, TCOTS_Items.SWALLOW_SPLASH.get(),
                            TCOTS_Items.SWALLOW_POTION.get()).offerTo(exporter);

                }

                //Cat
                {
                    order=order+0.01f;
                    AlchemyTableRecipeJsonBuilder.createPotion(
                            order, TCOTS_Items.CAT_POTION.get(),
                            ingredientsList(
                                    Items.GLOW_BERRIES, 4,
                                    TCOTS_Items.WATER_ESSENCE.get(), 2)).offerTo(exporter);

                    order=order+0.01f;
                    AlchemyTableRecipeJsonBuilder.createPotion(
                            order, TCOTS_Items.CAT_POTION_ENHANCED.get(), 1,
                            ingredientsList(
                                    Items.GLOW_BERRIES, 5,
                                    Items.BROWN_MUSHROOM, 1,
                                    TCOTS_Items.WATER_ESSENCE.get(), 3),
                            TCOTS_Items.CAT_POTION.get()).offerTo(exporter);

                    order=order+0.01f;
                    AlchemyTableRecipeJsonBuilder.createPotion(
                            order, TCOTS_Items.CAT_POTION_SUPERIOR.get(), 2,
                            ingredientsList(
                                    Items.GLOW_BERRIES, 4,
                                    Items.BROWN_MUSHROOM, 4,
                                    TCOTS_Items.ALLSPICE.get(), 2,
                                    TCOTS_Items.AETHER.get(), 1),
                            TCOTS_Items.CAT_POTION_ENHANCED.get()).offerTo(exporter);
                }

                //White Raffard's
                {
                    order=order+0.01f;
                    AlchemyTableRecipeJsonBuilder.createPotion(
                            order, TCOTS_Items.WHITE_RAFFARDS_DECOCTION.get(),
                            ingredientsList(
                                    Items.OXEYE_DAISY, 2,
                                    TCOTS_Items.NEKKER_HEART.get(), 4)).offerTo(exporter);

                    order=order+0.01f;
                    AlchemyTableRecipeJsonBuilder.createPotion(
                            order, TCOTS_Items.WHITE_RAFFARDS_DECOCTION_ENHANCED.get(), 1,
                            ingredientsList(
                                    Items.OXEYE_DAISY, 4,
                                    TCOTS_Items.BRYONIA.get(), 1,
                                    TCOTS_Items.NEKKER_HEART.get(), 5),
                            TCOTS_Items.WHITE_RAFFARDS_DECOCTION.get()).offerTo(exporter);

                    order=order+0.01f;
                    AlchemyTableRecipeJsonBuilder.createPotion(
                            order, TCOTS_Items.WHITE_RAFFARDS_DECOCTION_SUPERIOR.get(), 2,
                            ingredientsList(
                                    Items.OXEYE_DAISY, 4,
                                    TCOTS_Items.BRYONIA.get(), 4,
                                    Items.FLOWERING_AZALEA, 4,
                                    TCOTS_Items.VERMILION.get(), 1),
                            TCOTS_Items.WHITE_RAFFARDS_DECOCTION_ENHANCED.get()).offerTo(exporter);

                    order=order+0.01f;
                    AlchemyTableRecipeJsonBuilder.createPotionSplash(
                            order, TCOTS_Items.WHITE_RAFFARDS_DECOCTION_SPLASH.get(),
                            TCOTS_Items.WHITE_RAFFARDS_DECOCTION.get()).offerTo(exporter);
                }

                //Killer Whale
                {
                    order=order+0.01f;
                    AlchemyTableRecipeJsonBuilder.createPotion(
                            order, TCOTS_Items.KILLER_WHALE_POTION.get(),
                            ingredientsList(
                                    Items.KELP, 6,
                                    Items.SWEET_BERRIES, 5,
                                    TCOTS_Items.DROWNER_TONGUE.get(), 5)).offerTo(exporter);

                    order=order+0.01f;
                    AlchemyTableRecipeJsonBuilder.createPotionSplash(
                            order, TCOTS_Items.KILLER_WHALE_SPLASH.get(),
                            TCOTS_Items.KILLER_WHALE_POTION.get()).offerTo(exporter);
                }

                //Black Blood
                {
                    order=order+0.01f;
                    AlchemyTableRecipeJsonBuilder.createPotion(
                            order, TCOTS_Items.BLACK_BLOOD_POTION.get(),
                            ingredientsList(
                                    TCOTS_Items.SEWANT_MUSHROOMS.get(), 2,
                                    TCOTS_Items.GHOUL_BLOOD.get(), 4)).offerTo(exporter);

                    order=order+0.01f;
                    AlchemyTableRecipeJsonBuilder.createPotion(
                            order, TCOTS_Items.BLACK_BLOOD_POTION_ENHANCED.get(), 1,
                            ingredientsList(
                                    Items.ALLIUM, 2,
                                    TCOTS_Items.SEWANT_MUSHROOMS.get(), 5,
                                    TCOTS_Items.GHOUL_BLOOD.get(), 5),
                            TCOTS_Items.BLACK_BLOOD_POTION.get()).offerTo(exporter);

                    order=order+0.01f;
                    AlchemyTableRecipeJsonBuilder.createPotion(
                            order, TCOTS_Items.BLACK_BLOOD_POTION_SUPERIOR.get(), 2,
                            ingredientsList(
                                    Items.ALLIUM, 6,
                                    TCOTS_Items.SEWANT_MUSHROOMS.get(), 5,
                                    TCOTS_Items.HAN_FIBER.get(), 2,
                                    TCOTS_Items.REBIS.get(), 1),
                            TCOTS_Items.BLACK_BLOOD_POTION_ENHANCED.get()).offerTo(exporter);
                }

                //Maribor Forest
                {
                    order=order+0.01f;
                    AlchemyTableRecipeJsonBuilder.createPotion(
                            order, TCOTS_Items.MARIBOR_FOREST_POTION.get(),
                            ingredientsList(
                                    Items.GLOW_BERRIES, 3,
                                    TCOTS_Items.DROWNER_TONGUE.get(), 4,
                                    TCOTS_Items.ALGHOUL_BONE_MARROW.get(), 2)).offerTo(exporter);

                    order=order+0.01f;
                    AlchemyTableRecipeJsonBuilder.createPotion(
                            order, TCOTS_Items.MARIBOR_FOREST_POTION_ENHANCED.get(), 1,
                            ingredientsList(
                                    Items.GLOW_BERRIES, 5,
                                    TCOTS_Items.CROWS_EYE.get(), 2,
                                    TCOTS_Items.DROWNER_TONGUE.get(), 2),
                            TCOTS_Items.MARIBOR_FOREST_POTION.get()).offerTo(exporter);

                    order=order+0.01f;
                    AlchemyTableRecipeJsonBuilder.createPotion(
                            order, TCOTS_Items.MARIBOR_FOREST_POTION_SUPERIOR.get(), 2,
                            ingredientsList(
                                    Items.GLOW_BERRIES, 4,
                                    TCOTS_Items.CROWS_EYE.get(), 4,
                                    Items.ALLIUM, 6,
                                    TCOTS_Items.VERMILION.get(), 1),
                            TCOTS_Items.MARIBOR_FOREST_POTION_ENHANCED.get()).offerTo(exporter);
                }

                //Wolf
                {
                    order=order+0.01f;
                    AlchemyTableRecipeJsonBuilder.createPotion(
                            order, TCOTS_Items.WOLF_POTION.get(),
                            ingredientsList(
                                    Items.BONE_MEAL, 12,
                                    TCOTS_Items.DEVOURER_TEETH.get(), 2)).offerTo(exporter);

                    order=order+0.01f;
                    AlchemyTableRecipeJsonBuilder.createPotion(
                            order, TCOTS_Items.WOLF_POTION_ENHANCED.get(), 1,
                            ingredientsList(
                                    Items.BONE_MEAL, 12,
                                    TCOTS_Items.GHOUL_BLOOD.get(), 2,
                                    TCOTS_Items.DEVOURER_TEETH.get(), 8),
                            TCOTS_Items.WOLF_POTION.get()).offerTo(exporter);

                    order=order+0.01f;
                    AlchemyTableRecipeJsonBuilder.createPotion(
                            order, TCOTS_Items.WOLF_POTION_SUPERIOR.get(), 2,
                            ingredientsList(
                                    Items.BONE_MEAL, 16,
                                    TCOTS_Items.GHOUL_BLOOD.get(), 4,
                                    TCOTS_Items.HAN_FIBER.get(), 6,
                                    TCOTS_Items.HYDRAGENUM.get(), 1),
                            TCOTS_Items.WOLF_POTION_ENHANCED.get()).offerTo(exporter);
                }

                //Bindweed
                {
                    order=order+0.01f;
                    AlchemyTableRecipeJsonBuilder.createPotion(
                            order, TCOTS_Items.BINDWEED_POTION.get(),
                            ingredientsList(
                                    Items.GLOW_BERRIES, 6,
                                    TCOTS_Items.BLOEDZUIGER_BLOOD.get(), 4,
                                    TCOTS_Items.ERGOT_SEEDS.get(), 2)).offerTo(exporter);

                    order=order+0.01f;
                    AlchemyTableRecipeJsonBuilder.createPotion(
                            order, TCOTS_Items.BINDWEED_POTION_ENHANCED.get(), 1,
                            ingredientsList(
                                    Items.GLOW_BERRIES, 18,
                                    TCOTS_Items.BLOEDZUIGER_BLOOD.get(), 8,
                                    TCOTS_Items.WATER_ESSENCE.get(), 4,
                                    TCOTS_Items.ROTFIEND_BLOOD.get(), 4),
                            TCOTS_Items.BINDWEED_POTION.get()).offerTo(exporter);

                    order=order+0.01f;
                    AlchemyTableRecipeJsonBuilder.createPotion(
                            order, TCOTS_Items.BINDWEED_POTION_SUPERIOR.get(), 2,
                            ingredientsList(
                                    TCOTS_Items.BLOEDZUIGER_BLOOD.get(), 12,
                                    TCOTS_Items.ROTFIEND_BLOOD.get(), 8,
                                    TCOTS_Items.NEKKER_HEART.get(), 2,
                                    TCOTS_Items.REBIS.get(), 1),
                            TCOTS_Items.BINDWEED_POTION_ENHANCED.get()).offerTo(exporter);
                }

                //Rook
                {
                    order=order+0.01f;
                    AlchemyTableRecipeJsonBuilder.createPotion(
                            order, TCOTS_Items.ROOK_POTION.get(),
                            ingredientsList(
                                    Items.POPPY, 4,
                                    TCOTS_Items.NEKKER_EYE.get(), 3)).offerTo(exporter);

                    order=order+0.01f;
                    AlchemyTableRecipeJsonBuilder.createPotion(
                            order, TCOTS_Items.ROOK_POTION_ENHANCED.get(), 1,
                            ingredientsList(
                                    Items.POPPY, 6,
                                    Items.BROWN_MUSHROOM, 4,
                                    TCOTS_Items.NEKKER_EYE.get(), 6),
                            TCOTS_Items.ROOK_POTION.get()).offerTo(exporter);

                    order=order+0.01f;
                    AlchemyTableRecipeJsonBuilder.createPotion(
                            order, TCOTS_Items.ROOK_POTION_SUPERIOR.get(), 2,
                            ingredientsList(
                                    Items.POPPY, 12,
                                    Items.BROWN_MUSHROOM, 8,
                                    TCOTS_Items.ALGHOUL_BONE_MARROW.get(), 4,
                                    TCOTS_Items.RUBEDO.get(), 1),
                            TCOTS_Items.ROOK_POTION_ENHANCED.get()).offerTo(exporter);
                }

                //White Honey
                {
                    order=order+0.01f;
                    AlchemyTableRecipeJsonBuilder.createPotion(
                            order, TCOTS_Items.WHITE_HONEY_POTION.get(),
                            ingredientsList(
                                    Items.HONEY_BOTTLE, 1)).offerTo(exporter);

                    order=order+0.01f;
                    AlchemyTableRecipeJsonBuilder.createPotion(
                            order, TCOTS_Items.WHITE_HONEY_POTION_ENHANCED.get(), 1,
                            ingredientsList(
                                    Items.HONEY_BOTTLE, 2,
                                    Items.LILY_OF_THE_VALLEY, 2),
                            TCOTS_Items.WHITE_HONEY_POTION.get()).offerTo(exporter);

                    order=order+0.01f;
                    AlchemyTableRecipeJsonBuilder.createPotion(
                            order, TCOTS_Items.WHITE_HONEY_POTION_SUPERIOR.get(), 2,
                            ingredientsList(
                                    Items.HONEY_BOTTLE, 4,
                                    Items.LILY_OF_THE_VALLEY, 4,
                                    Items.ALLIUM, 8,
                                    TCOTS_Items.VITRIOL.get(), 1),
                            TCOTS_Items.WHITE_HONEY_POTION_ENHANCED.get()).offerTo(exporter);
                }

            }


            //Decoctions
            {
                order = order + 0.01f;
                AlchemyTableRecipeJsonBuilder.createDecoction(
                        order, TCOTS_Items.WATER_HAG_DECOCTION.get(),
                        ingredientsList(
                                TCOTS_Items.WATER_HAG_MUTAGEN.get(), 1,
                                Items.ECHO_SHARD, 1,
                                Items.SWEET_BERRIES, 1)).offerTo(exporter);

                order = order + 0.01f;
                AlchemyTableRecipeJsonBuilder.createDecoction(
                        order, TCOTS_Items.GRAVE_HAG_DECOCTION.get(),
                        ingredientsList(
                                TCOTS_Items.GRAVE_HAG_MUTAGEN.get(), 1,
                                Items.ECHO_SHARD, 1,
                                Items.RED_MUSHROOM, 2,
                                Items.BROWN_MUSHROOM, 3)).offerTo(exporter);

                order = order + 0.01f;
                AlchemyTableRecipeJsonBuilder.createDecoction(
                        order, TCOTS_Items.ALGHOUL_DECOCTION.get(),
                        ingredientsList(
                                TCOTS_Items.ALGHOUL_BONE_MARROW.get(), 2,
                                Items.ECHO_SHARD, 4,
                                Items.KELP, 2)).offerTo(exporter);

                order = order + 0.01f;
                AlchemyTableRecipeJsonBuilder.createDecoction(
                        order, TCOTS_Items.FOGLET_DECOCTION.get(),
                        ingredientsList(
                                TCOTS_Items.FOGLET_MUTAGEN.get(), 1,
                                Items.ECHO_SHARD, 1,
                                Items.AZURE_BLUET, 2,
                                Items.DANDELION, 1)).offerTo(exporter);

                order = order + 0.01f;
                AlchemyTableRecipeJsonBuilder.createDecoction(
                        order, TCOTS_Items.NEKKER_WARRIOR_DECOCTION.get(),
                        ingredientsList(
                                TCOTS_Items.NEKKER_WARRIOR_MUTAGEN.get(), 1,
                                Items.ECHO_SHARD, 1,
                                Items.AZURE_BLUET, 2,
                                Items.FERN, 1)).offerTo(exporter);

                order = order + 0.01f;
                AlchemyTableRecipeJsonBuilder.createDecoction(
                        order, TCOTS_Items.TROLL_DECOCTION.get(),
                        ingredientsList(
                                TCOTS_Items.TROLL_MUTAGEN.get(), 1,
                                Items.ECHO_SHARD, 4,
                                TCOTS_Items.CROWS_EYE.get(), 4,
                                Items.HONEY_BOTTLE, 8)).offerTo(exporter);
            }


            //Bombs
            {
                //Grapeshot
                {
                    order=order+0.01f;
                    AlchemyTableRecipeJsonBuilder.createBomb(
                            order, TCOTS_Items.GRAPESHOT.get(),
                            ingredientsList(Items.BONE_MEAL,12)).offerTo(exporter);

                    order=order+0.01f;
                    AlchemyTableRecipeJsonBuilder.createBomb(
                            order, TCOTS_Items.GRAPESHOT_ENHANCED.get(),
                            ingredientsList(
                                    Items.BONE_MEAL,4,
                                    Items.DANDELION,2,
                                    TCOTS_Items.CROWS_EYE.get(),2,
                                    Items.RED_MUSHROOM, 1),
                            TCOTS_Items.GRAPESHOT.get()).offerTo(exporter);

                    order=order+0.01f;
                    AlchemyTableRecipeJsonBuilder.createBomb(
                            order, TCOTS_Items.GRAPESHOT_SUPERIOR.get(),
                            ingredientsList(
                                    Items.BONE_MEAL,4,
                                    Items.BLAZE_POWDER,2,
                                    Items.RED_MUSHROOM,2,
                                    TCOTS_Items.NIGREDO.get(), 1),
                            TCOTS_Items.GRAPESHOT_ENHANCED.get()).offerTo(exporter);
                }

                //Samum
                {
                    order=order+0.01f;
                    AlchemyTableRecipeJsonBuilder.createBomb(
                                    order, TCOTS_Items.SAMUM.get(),
                                    ingredientsList(TCOTS_Items.CELANDINE.get(),2))
                            .offerTo(exporter);

                    order=order+0.01f;
                    AlchemyTableRecipeJsonBuilder.createBomb(
                            order, TCOTS_Items.SAMUM_ENHANCED.get(),
                            ingredientsList(
                                    Items.GLOWSTONE_DUST,2,
                                    TCOTS_Items.FOGLET_TEETH.get(),2,
                                    TCOTS_Items.CELANDINE.get(),1,
                                    Items.DANDELION, 1),
                            TCOTS_Items.SAMUM.get()).offerTo(exporter);

                    order=order+0.01f;
                    AlchemyTableRecipeJsonBuilder.createBomb(
                            order, TCOTS_Items.SAMUM_SUPERIOR.get(),
                            ingredientsList(
                                    Items.GLOWSTONE_DUST,4,
                                    TCOTS_Items.FOGLET_TEETH.get(),4,
                                    TCOTS_Items.CELANDINE.get(),1,
                                    TCOTS_Items.AETHER.get(), 1),
                            TCOTS_Items.SAMUM_ENHANCED.get()).offerTo(exporter);
                }

                //Dancing Star
                {
                    order=order+0.01f;
                    AlchemyTableRecipeJsonBuilder.createBomb(
                            order, TCOTS_Items.DANCING_STAR.get(),
                            ingredientsList(Items.BLAZE_POWDER,2)).offerTo(exporter);

                    order=order+0.01f;
                    AlchemyTableRecipeJsonBuilder.createBomb(
                            order, TCOTS_Items.DANCING_STAR_ENHANCED.get(),
                            ingredientsList(
                                    Items.GLOWSTONE_DUST,2,
                                    Items.BLAZE_POWDER,1,
                                    TCOTS_Items.SEWANT_MUSHROOMS.get(),1,
                                    Items.ALLIUM, 4),
                            TCOTS_Items.DANCING_STAR.get()).offerTo(exporter);

                    order=order+0.01f;
                    AlchemyTableRecipeJsonBuilder.createBomb(
                            order, TCOTS_Items.DANCING_STAR_SUPERIOR.get(),
                            ingredientsList(
                                    Items.GLOWSTONE_DUST,4,
                                    Items.BLAZE_POWDER,2,
                                    TCOTS_Items.SEWANT_MUSHROOMS.get(),2,
                                    TCOTS_Items.NIGREDO.get(), 1),
                            TCOTS_Items.DANCING_STAR_ENHANCED.get()).offerTo(exporter);
                }

                //Devil's Puffball
                {
                    order=order+0.01f;
                    AlchemyTableRecipeJsonBuilder.createBomb(
                            order, TCOTS_Items.DEVILS_PUFFBALL.get(),
                            ingredientsList(TCOTS_Items.SEWANT_MUSHROOMS.get(),2)).offerTo(exporter);

                    order=order+0.01f;
                    AlchemyTableRecipeJsonBuilder.createBomb(
                            order, TCOTS_Items.DEVILS_PUFFBALL_ENHANCED.get(),
                            ingredientsList(
                                    Items.BONE_MEAL,4,
                                    TCOTS_Items.SEWANT_MUSHROOMS.get(),2,
                                    Items.SPIDER_EYE,2,
                                    Items.MOSS_BLOCK, 1),
                            TCOTS_Items.DEVILS_PUFFBALL.get()).offerTo(exporter);

                    order=order+0.01f;
                    AlchemyTableRecipeJsonBuilder.createBomb(
                            order, TCOTS_Items.DEVILS_PUFFBALL_SUPERIOR.get(),
                            ingredientsList(
                                    Items.BONE_MEAL,8,
                                    TCOTS_Items.SEWANT_MUSHROOMS.get(),3,
                                    Items.SPIDER_EYE,2,
                                    TCOTS_Items.REBIS.get(), 1),
                            TCOTS_Items.DEVILS_PUFFBALL_ENHANCED.get()).offerTo(exporter);
                }

                //Dragon's Dream
                {
                    order=order+0.01f;
                    AlchemyTableRecipeJsonBuilder.createBomb(
                            order, TCOTS_Items.DRAGONS_DREAM.get(),
                            ingredientsList(Items.GLOWSTONE_DUST,4)).offerTo(exporter);

                    order=order+0.01f;
                    AlchemyTableRecipeJsonBuilder.createBomb(
                            order, TCOTS_Items.DRAGONS_DREAM_ENHANCED.get(),
                            ingredientsList(
                                    Items.GLOWSTONE_DUST,2,
                                    Items.AMETHYST_SHARD,1,
                                    Items.OXEYE_DAISY,2,
                                    TCOTS_Items.BRYONIA.get(), 2),
                            TCOTS_Items.DRAGONS_DREAM.get()).offerTo(exporter);

                    order=order+0.01f;
                    AlchemyTableRecipeJsonBuilder.createBomb(
                            order, TCOTS_Items.DRAGONS_DREAM_SUPERIOR.get(),
                            ingredientsList(
                                    Items.GLOWSTONE_DUST,4,
                                    Items.AMETHYST_SHARD,2,
                                    TCOTS_Items.BRYONIA.get(),2,
                                    TCOTS_Items.AETHER.get(), 1),
                            TCOTS_Items.DRAGONS_DREAM_ENHANCED.get()).offerTo(exporter);
                }

                //Northern Wind
                {
                    order=order+0.01f;
                    AlchemyTableRecipeJsonBuilder.createBomb(
                            order, TCOTS_Items.NORTHERN_WIND.get(),
                            ingredientsList(
                                    TCOTS_Items.WATER_ESSENCE.get(),1,
                                    Items.PRISMARINE_CRYSTALS,1,
                                    TCOTS_Items.ALLSPICE.get(),2)).offerTo(exporter);

                    order=order+0.01f;
                    AlchemyTableRecipeJsonBuilder.createBomb(
                            order, TCOTS_Items.NORTHERN_WIND_ENHANCED.get(),
                            ingredientsList(
                                    TCOTS_Items.WATER_ESSENCE.get(),2,
                                    Items.PRISMARINE_CRYSTALS,1,
                                    TCOTS_Items.VERBENA.get(),1,
                                    TCOTS_Items.ALLSPICE.get(), 2),
                            TCOTS_Items.NORTHERN_WIND.get()).offerTo(exporter);

                    order=order+0.01f;
                    AlchemyTableRecipeJsonBuilder.create(
                            order, TCOTS_Items.NORTHERN_WIND_SUPERIOR.get(),
                            AlchemyTableRecipeCategory.BOMBS_OILS,
                            ingredientsList(
                                    TCOTS_Items.WATER_ESSENCE.get(),3,
                                    Items.PRISMARINE_CRYSTALS,2,
                                    TCOTS_Items.VERBENA.get(),2,
                                    TCOTS_Items.ALLSPICE.get(), 3,
                                    TCOTS_Items.QUEBRITH.get(), 1),
                            TCOTS_Items.NORTHERN_WIND_ENHANCED.get()).offerTo(exporter);
                }

                //Dimeritium Bomb
                {
                    order=order+0.01f;
                    AlchemyTableRecipeJsonBuilder.create(
                            order,
                            TCOTS_Items.DIMERITIUM_BOMB.get(),
                            AlchemyTableRecipeCategory.BOMBS_OILS,
                            ingredientsList(Items.AMETHYST_SHARD,2),
                            new ItemStack(Items.GUNPOWDER, 5)).offerTo(exporter);

                    order=order+0.01f;
                    AlchemyTableRecipeJsonBuilder.createBomb(
                            order, TCOTS_Items.DIMERITIUM_BOMB_ENHANCED.get(),
                            ingredientsList(
                                    Items.AMETHYST_SHARD,2,
                                    Items.PRISMARINE_CRYSTALS,2,
                                    Items.DANDELION,1,
                                    Items.CORNFLOWER, 3),
                            TCOTS_Items.DIMERITIUM_BOMB.get()).offerTo(exporter);

                    order=order+0.01f;
                    AlchemyTableRecipeJsonBuilder.createBomb(
                            order, TCOTS_Items.DIMERITIUM_BOMB_SUPERIOR.get(),
                            ingredientsList(
                                    Items.AMETHYST_SHARD,2,
                                    Items.PRISMARINE_CRYSTALS,4,
                                    TCOTS_Items.PUFFBALL.get(),2,
                                    TCOTS_Items.NIGREDO.get(), 1),
                            TCOTS_Items.DIMERITIUM_BOMB_ENHANCED.get()).offerTo(exporter);
                }

                //Moon dust Bomb
                {
                    order=order+0.01f;
                    AlchemyTableRecipeJsonBuilder.createBomb(
                            order, TCOTS_Items.MOON_DUST.get(),
                            ingredientsList(Items.GHAST_TEAR,2)).offerTo(exporter);

                    order=order+0.01f;
                    AlchemyTableRecipeJsonBuilder.createBomb(
                            order, TCOTS_Items.MOON_DUST_ENHANCED.get(),
                            ingredientsList(
                                    Items.GHAST_TEAR,1,
                                    Items.BLAZE_POWDER,2,
                                    Items.BEETROOT,4,
                                    Items.HONEY_BOTTLE, 1),
                            TCOTS_Items.MOON_DUST.get()).offerTo(exporter);

                    order=order+0.01f;
                    AlchemyTableRecipeJsonBuilder.createBomb(
                            order, TCOTS_Items.MOON_DUST_SUPERIOR.get(),
                            ingredientsList(
                                    Items.GHAST_TEAR,2,
                                    Items.BLAZE_POWDER,4,
                                    Items.BEETROOT,6,
                                    TCOTS_Items.NIGREDO.get(), 1),
                            TCOTS_Items.MOON_DUST_ENHANCED.get()).offerTo(exporter);
                }
            }


            //Monster Oils
            {
                //Necrophage Oil
                {
                    order=order+0.01f;
                    AlchemyTableRecipeJsonBuilder.createOil(
                            order, TCOTS_Items.NECROPHAGE_OIL.get(),
                            ingredientsList(Items.DANDELION, 4)).offerTo(exporter);

                    order=order+0.01f;
                    AlchemyTableRecipeJsonBuilder.createOil(
                            order, TCOTS_Items.ENHANCED_NECROPHAGE_OIL.get(),
                            ingredientsList(
                                    TCOTS_Items.ROTFIEND_BLOOD.get(), 4,
                                    Items.DANDELION, 4,
                                    TCOTS_Items.ARENARIA.get(), 4,
                                    Items.FLOWERING_AZALEA, 4), 4,
                            TCOTS_Items.NECROPHAGE_OIL.get()).offerTo(exporter);

                    order=order+0.01f;
                    AlchemyTableRecipeJsonBuilder.createOil(
                            order, TCOTS_Items.SUPERIOR_NECROPHAGE_OIL.get(),
                            ingredientsList(
                                    TCOTS_Items.ROTFIEND_BLOOD.get(), 4,
                                    Items.CORNFLOWER, 1,
                                    TCOTS_Items.ARENARIA.get(), 1,
                                    TCOTS_Items.HYDRAGENUM.get(), 1), 5,
                            TCOTS_Items.ENHANCED_NECROPHAGE_OIL.get()).offerTo(exporter);
                }

                //Ogroid Oil
                {
                    order=order+0.01f;
                    AlchemyTableRecipeJsonBuilder.createOil(
                            order, TCOTS_Items.OGROID_OIL.get(),
                            ingredientsList(Items.CORNFLOWER, 4)).offerTo(exporter);

                    order=order+0.01f;
                    AlchemyTableRecipeJsonBuilder.createOil(
                            order, TCOTS_Items.ENHANCED_OGROID_OIL.get(),
                            ingredientsList(
                                    TCOTS_Items.CAVE_TROLL_LIVER.get(), 2,
                                    Items.RED_MUSHROOM, 2,
                                    Items.FERN, 3,
                                    Items.CORNFLOWER, 4), 2,
                            TCOTS_Items.OGROID_OIL.get()).offerTo(exporter);

                    order=order+0.01f;
                    AlchemyTableRecipeJsonBuilder.createOil(
                            order, TCOTS_Items.SUPERIOR_OGROID_OIL.get(),
                            ingredientsList(
                                    TCOTS_Items.CAVE_TROLL_LIVER.get(), 3,
                                    TCOTS_Items.ARENARIA.get(), 2,
                                    Items.FERN, 6,
                                    TCOTS_Items.AETHER.get(), 1), 2,
                            TCOTS_Items.ENHANCED_OGROID_OIL.get()).offerTo(exporter);
                }

                //Beast Oil
                {
                    order=order+0.01f;
                    AlchemyTableRecipeJsonBuilder.createOil(
                            order, TCOTS_Items.BEAST_OIL.get(),
                            ingredientsList(Items.BEEF, 4)).offerTo(exporter);

                    order=order+0.01f;
                    AlchemyTableRecipeJsonBuilder.createOil(
                            order, TCOTS_Items.ENHANCED_BEAST_OIL.get(),
                            ingredientsList(
                                    Items.LEATHER, 2,
                                    TCOTS_Items.CELANDINE.get(), 1,
                                    TCOTS_Items.PUFFBALL.get(), 1,
                                    Items.BEETROOT, 4), 5,
                            TCOTS_Items.BEAST_OIL.get()).offerTo(exporter);

                    order=order+0.01f;
                    AlchemyTableRecipeJsonBuilder.createOil(
                            order, TCOTS_Items.SUPERIOR_BEAST_OIL.get(),
                            ingredientsList(
                                    Items.ENDER_PEARL, 2,
                                    TCOTS_Items.CELANDINE.get(), 1,
                                    TCOTS_Items.PUFFBALL.get(), 1,
                                    TCOTS_Items.RUBEDO.get(), 1), 2,
                            TCOTS_Items.ENHANCED_BEAST_OIL.get()).offerTo(exporter);
                }

                //Hanged Man Oil
                {
                    order=order+0.01f;
                    AlchemyTableRecipeJsonBuilder.createOil(
                            order, TCOTS_Items.HANGED_OIL.get(),
                            ingredientsList(TCOTS_Items.ARENARIA.get(), 4)).offerTo(exporter);

                    order=order+0.01f;
                    AlchemyTableRecipeJsonBuilder.createOil(
                            order, TCOTS_Items.ENHANCED_HANGED_OIL.get(),
                            ingredientsList(
                                    TCOTS_Items.HAN_FIBER.get(), 1,
                                    TCOTS_Items.NEKKER_EYE.get(), 1,
                                    Items.AZURE_BLUET, 1,
                                    TCOTS_Items.ARENARIA.get(), 1), 2,
                            TCOTS_Items.HANGED_OIL.get()).offerTo(exporter);

                    order=order+0.01f;
                    AlchemyTableRecipeJsonBuilder.createOil(
                            order, TCOTS_Items.SUPERIOR_HANGED_OIL.get(),
                            ingredientsList(
                                    Items.MOSS_BLOCK, 1,
                                    TCOTS_Items.ROTFIEND_BLOOD.get(), 2,
                                    Items.AZURE_BLUET, 1,
                                    TCOTS_Items.QUEBRITH.get(), 1), 2,
                            TCOTS_Items.ENHANCED_HANGED_OIL.get()).offerTo(exporter);
                }
            }

            //Ingredients
            {
                order=order+0.01f;
                AlchemyTableRecipeJsonBuilder.createMisc(
                        order,
                        new ItemStack(TCOTS_Items.DWARVEN_SPIRIT.get()),
                        ingredientsList(
                                TCOTS_Items.ICY_SPIRIT.get(), 2,
                                Items.LILY_OF_THE_VALLEY,1),
                        Items.GLASS_BOTTLE).offerTo(exporter);

                order=order+0.01f;
                AlchemyTableRecipeJsonBuilder.createMisc(
                        order,
                        new ItemStack(TCOTS_Items.ALCOHEST.get(), 2),
                        ingredientsList(
                                Items.SWEET_BERRIES, 2,
                                TCOTS_Items.CHERRY_CORDIAL.get(),1,
                                TCOTS_Items.MANDRAKE_CORDIAL.get(),1),
                        Items.GLASS_BOTTLE).offerTo(exporter);

                order=order+0.01f;
                AlchemyTableRecipeJsonBuilder.createMisc(
                        order,
                        new ItemStack(TCOTS_Items.WHITE_GULL.get()),
                        ingredientsList(
                                TCOTS_Items.ARENARIA.get(), 1,
                                TCOTS_Items.VILLAGE_HERBAL.get(),1,
                                TCOTS_Items.CHERRY_CORDIAL.get(),1,
                                TCOTS_Items.MANDRAKE_CORDIAL.get(),1),
                        Items.GLASS_BOTTLE).offerTo(exporter);

                order=order+0.01f;
                AlchemyTableRecipeJsonBuilder.createMisc(
                        order,
                        new ItemStack(TCOTS_Items.STAMMELFORDS_DUST, 2),
                        ingredientsList(
                                Items.BONE_MEAL, 8,
                                Items.GUNPOWDER,4,
                                Items.GLOWSTONE_DUST,4),
                        Items.BLAZE_POWDER).offerTo(exporter);


                //Witcher Substances
                {
                    order=order+0.01f;
                    AlchemyTableRecipeJsonBuilder.createMisc(
                            order,
                            TCOTS_Items.AETHER.get(),
                            ingredientsList(
                                    TCOTS_Items.VERBENA.get(), 1,
                                    TCOTS_Items.ERGOT_SEEDS.get(), 1,
                                    TCOTS_Items.HAN_FIBER.get(), 1,
                                    TCOTS_Items.PUFFBALL.get(), 1,
                                    Items.RED_MUSHROOM, 1)).offerTo(exporter);

                    order=order+0.01f;
                    AlchemyTableRecipeJsonBuilder.createMisc(
                            order,
                            TCOTS_Items.HYDRAGENUM.get(),
                            ingredientsList(
                                    TCOTS_Items.VERBENA.get(), 1,
                                    TCOTS_Items.ERGOT_SEEDS.get(), 1,
                                    Items.FERN, 1,
                                    Items.MOSS_BLOCK, 1,
                                    Items.GLOW_LICHEN, 1)).offerTo(exporter);

                    order=order+0.01f;
                    AlchemyTableRecipeJsonBuilder.createMisc(
                            order,
                            TCOTS_Items.NIGREDO.get(),
                            ingredientsList(
                                    TCOTS_Items.CROWS_EYE.get(), 1,
                                    TCOTS_Items.HAN_FIBER.get(), 1,
                                    Items.ALLIUM, 1,
                                    Items.GLOW_LICHEN, 1,
                                    Items.SWEET_BERRIES, 1)).offerTo(exporter);

                    order=order+0.01f;
                    AlchemyTableRecipeJsonBuilder.createMisc(
                            order,
                            TCOTS_Items.QUEBRITH.get(),
                            ingredientsList(
                                    TCOTS_Items.VERBENA.get(), 1,
                                    TCOTS_Items.PUFFBALL.get(), 1,
                                    Items.RED_MUSHROOM, 1,
                                    Items.GLOW_LICHEN, 1,
                                    Items.FLOWERING_AZALEA, 1)).offerTo(exporter);

                    order=order+0.01f;
                    AlchemyTableRecipeJsonBuilder.createMisc(
                            order,
                            TCOTS_Items.REBIS.get(),
                            ingredientsList(
                                    TCOTS_Items.VERBENA.get(), 1,
                                    TCOTS_Items.ERGOT_SEEDS.get(), 1,
                                    TCOTS_Items.ALLSPICE.get(), 1,
                                    Items.OXEYE_DAISY, 1,
                                    Items.FERN, 1)).offerTo(exporter);

                    order=order+0.01f;
                    AlchemyTableRecipeJsonBuilder.createMisc(
                            order,
                            TCOTS_Items.RUBEDO.get(),
                            ingredientsList(
                                    TCOTS_Items.CROWS_EYE.get(), 1,
                                    TCOTS_Items.HAN_FIBER.get(), 1,
                                    Items.OXEYE_DAISY, 1,
                                    TCOTS_Items.PUFFBALL.get(), 1,
                                    Items.MOSS_BLOCK, 1)).offerTo(exporter);

                    order=order+0.01f;
                    AlchemyTableRecipeJsonBuilder.createMisc(
                            order,
                            TCOTS_Items.VERMILION.get(),
                            ingredientsList(
                                    TCOTS_Items.VERBENA.get(), 1,
                                    TCOTS_Items.ERGOT_SEEDS.get(), 1,
                                    TCOTS_Items.HAN_FIBER.get(), 1,
                                    Items.POPPY, 1,
                                    TCOTS_Items.BRYONIA.get(), 1)).offerTo(exporter);

                    order=order+0.01f;
                    AlchemyTableRecipeJsonBuilder.createMisc(
                            order,
                            TCOTS_Items.VITRIOL.get(),
                            ingredientsList(
                                    Items.MOSS_BLOCK, 1,
                                    TCOTS_Items.ALLSPICE.get(), 1,
                                    Items.FERN, 1,
                                    Items.ALLIUM, 1,
                                    Items.GLOW_LICHEN, 1)).offerTo(exporter);
                }
            }
        }

        //Herbal Table
        {
            {
                //Witcher Plants
                {
                    HerbalTableRecipeJsonBuilder
                            .create(TCOTS_Items.ARENARIA.get().getDefaultInstance(),
                                    List.of(MobEffects.DAMAGE_BOOST.value(), MobEffects.WITHER.value()), 20)
                            .offerTo(exporter);

                    HerbalTableRecipeJsonBuilder
                            .create(TCOTS_Items.BRYONIA.get().getDefaultInstance(),
                                    List.of(MobEffects.SATURATION.value(), MobEffects.HARM.value()))
                            .offerTo(exporter);

                    HerbalTableRecipeJsonBuilder
                            .create(TCOTS_Items.CELANDINE.get().getDefaultInstance(),
                                    List.of(MobEffects.REGENERATION.value(), MobEffects.MOVEMENT_SLOWDOWN.value()), 40, 2)
                            .offerTo(exporter);

                    HerbalTableRecipeJsonBuilder
                            .create(TCOTS_Items.VERBENA.get().getDefaultInstance(),
                                    List.of(MobEffects.DIG_SPEED.value(), MobEffects.MOVEMENT_SLOWDOWN.value()), 80, 3)
                            .offerTo(exporter);

                    HerbalTableRecipeJsonBuilder
                            .create(TCOTS_Items.HAN_FIBER.get().getDefaultInstance(),
                                    List.of(MobEffects.FIRE_RESISTANCE.value(), MobEffects.BLINDNESS.value()))
                            .offerTo(exporter);

                    HerbalTableRecipeJsonBuilder
                            .create(TCOTS_Items.CROWS_EYE.get().getDefaultInstance(),
                                    List.of(MobEffects.MOVEMENT_SPEED.value(), MobEffects.POISON.value()))
                            .offerTo(exporter);
                }

                //Mushrooms
                {
                    HerbalTableRecipeJsonBuilder
                            .create(TCOTS_Items.PUFFBALL.get().getDefaultInstance(),
                                    List.of(MobEffects.SATURATION.value(), MobEffects.NIGHT_VISION.value(), MobEffects.POISON.value()))
                            .offerTo(exporter);

                    HerbalTableRecipeJsonBuilder
                            .create(TCOTS_Items.SEWANT_MUSHROOMS.get().getDefaultInstance(),
                                    List.of(MobEffects.SATURATION.value(), MobEffects.NIGHT_VISION.value(), MobEffects.POISON.value()))
                            .offerTo(exporter);

                    HerbalTableRecipeJsonBuilder
                            .create(Items.RED_MUSHROOM.getDefaultInstance(),
                                    List.of(MobEffects.SATURATION.value(), MobEffects.NIGHT_VISION.value(), MobEffects.POISON.value()))
                            .offerTo(exporter);

                    HerbalTableRecipeJsonBuilder
                            .create(Items.BROWN_MUSHROOM.getDefaultInstance(),
                                    List.of(MobEffects.SATURATION.value(), MobEffects.NIGHT_VISION.value(), MobEffects.POISON.value()))
                            .offerTo(exporter);
                }

                //SuspiciousStew
                {
                    HerbalTableRecipeJsonBuilder
                            .create(Items.ALLIUM.getDefaultInstance(),
                                    List.of(MobEffects.FIRE_RESISTANCE.value(), MobEffects.HUNGER.value()), 40, 1)
                            .offerTo(exporter);

                    HerbalTableRecipeJsonBuilder
                            .create(Items.AZURE_BLUET.getDefaultInstance(),
                                    List.of(MobEffects.INVISIBILITY.value(), MobEffects.BLINDNESS.value()), 80)
                            .offerTo(exporter);

                    HerbalTableRecipeJsonBuilder
                            .create(Items.BLUE_ORCHID.getDefaultInstance(),
                                    List.of(MobEffects.SATURATION.value(), MobEffects.CONFUSION.value()), 80)
                            .offerTo(exporter);

                    HerbalTableRecipeJsonBuilder
                            .create(Items.DANDELION.getDefaultInstance(),
                                    List.of(MobEffects.SATURATION.value(), MobEffects.CONFUSION.value()), 80)
                            .offerTo(exporter);

                    HerbalTableRecipeJsonBuilder
                            .create(Items.CORNFLOWER.getDefaultInstance(),
                                    List.of(MobEffects.JUMP.value(), MobEffects.MOVEMENT_SLOWDOWN.value()), 40, 3)
                            .offerTo(exporter);

                    HerbalTableRecipeJsonBuilder
                            .create(Items.LILY_OF_THE_VALLEY.getDefaultInstance(),
                                    List.of(MobEffects.WATER_BREATHING.value(), MobEffects.POISON.value()), 80)
                            .offerTo(exporter);

                    HerbalTableRecipeJsonBuilder
                            .create(Items.OXEYE_DAISY.getDefaultInstance(),
                                    List.of(MobEffects.REGENERATION.value(), MobEffects.WEAKNESS.value()), 40, 2)
                            .offerTo(exporter);

                    HerbalTableRecipeJsonBuilder
                            .create(Items.POPPY.getDefaultInstance(),
                                    List.of(MobEffects.NIGHT_VISION.value(), MobEffects.DIG_SLOWDOWN.value()), 20)
                            .offerTo(exporter);

                    HerbalTableRecipeJsonBuilder
                            .create(Items.TORCHFLOWER.getDefaultInstance(),
                                    List.of(MobEffects.NIGHT_VISION.value(), MobEffects.DIG_SLOWDOWN.value()), 20)
                            .offerTo(exporter);

                    HerbalTableRecipeJsonBuilder
                            .create(Items.ORANGE_TULIP.getDefaultInstance(),
                                    List.of(MobEffects.MOVEMENT_SPEED.value(), MobEffects.WEAKNESS.value()))
                            .offerTo(exporter);

                    HerbalTableRecipeJsonBuilder
                            .create(Items.PINK_TULIP.getDefaultInstance(),
                                    List.of(MobEffects.MOVEMENT_SPEED.value(), MobEffects.WEAKNESS.value()))
                            .offerTo(exporter);

                    HerbalTableRecipeJsonBuilder
                            .create(Items.RED_TULIP.getDefaultInstance(),
                                    List.of(MobEffects.MOVEMENT_SPEED.value(), MobEffects.WEAKNESS.value()))
                            .offerTo(exporter);

                    HerbalTableRecipeJsonBuilder
                            .create(Items.WHITE_TULIP.getDefaultInstance(),
                                    List.of(MobEffects.MOVEMENT_SPEED.value(), MobEffects.WEAKNESS.value()))
                            .offerTo(exporter);
                }
            }
        }
    }

    private List<ItemStack> ingredientsList(final Item itemA, final int a){
        return List.of(new ItemStack(itemA, a));
    }

    private List<ItemStack> ingredientsList(final Item itemA, final int a, final Item itemB, final int b){
        return List.of(new ItemStack(itemA, a), new ItemStack(itemB, b));
    }

    private List<ItemStack> ingredientsList(final ItemStack... items){
        return new ArrayList<>(Arrays.asList(items));
    }

    private ItemStack of(final Item item, final int quantity){
        return new ItemStack(item, quantity);
    }

    private List<ItemStack> ingredientsList(final Item itemA, final int a, final Item itemB, final int b, final Item itemC, final int c){
        return List.of(new ItemStack(itemA, a), new ItemStack(itemB, b), new ItemStack(itemC, c));
    }

    private List<ItemStack> ingredientsList(final Item itemA, final int a, final Item itemB, final int b, final Item itemC, final int c, final Item itemD, final int d){
        return List.of(new ItemStack(itemA, a), new ItemStack(itemB, b), new ItemStack(itemC, c), new ItemStack(itemD, d));
    }

    @SuppressWarnings("all")
    private List<ItemStack> ingredientsList(Item itemA, int a, Item itemB, int b, Item itemC, int c, Item itemD, int d, Item itemE, int e){
        return List.of(new ItemStack(itemA, a), new ItemStack(itemB, b), new ItemStack(itemC, c), new ItemStack(itemD, d), new ItemStack(itemE, e));
    }
}
