package TCOTS.world.village;

import TCOTS.TCOTS_Main;
import TCOTS.items.AlchemyRecipeRandomlyLootFunction;
import TCOTS.items.TCOTS_Items_Fabric;
import TCOTS.items.components.RecipeTeacherComponent;
import TCOTS.items.maps.TCOTS_MapIcons;
import TCOTS.utils.AlchemyFormulaUtil;
import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;
import java.util.Optional;

public class VillagerCustomTrades {

    @SuppressWarnings("all")
    public static void registerTrades(){
        //Uses            --> 16/12/3
        //Experience      --> 1/2/5/10/15/20/30
        //PriceMultiplier --> 0.05/0.2


        //Herbalist
        {
            TradeOfferHelper.registerVillagerOffers(TCOTS_PointOfInterest.HERBALIST, 1,
                    factories -> {
                        //Sell
                        {
                           factories.add(new VillagerTrades.ItemsForEmeralds(
                                   //Gives
                                   TCOTS_Items_Fabric.ALCHEMY_BOOK,
                                   6,
                                   1,
                                   3,
                                   10
                           ));


                            factories.add(new VillagerTrades.ItemsForEmeralds(
                                    //Gives
                                    TCOTS_Items_Fabric.ICY_SPIRIT,
                                    4,
                                    1,
                                    16,
                                    1)
                            );
                        }

                        //Buys
                        {
                            factories.add(new VillagerTrades.EmeraldForItems(
                                    Items.DANDELION,
                                    12,
                                    16,
                                    2)
                            );
                        }
                    });

            TradeOfferHelper.registerVillagerOffers(TCOTS_PointOfInterest.HERBALIST, 2,
                    factories -> {
                        //Sell
                        {
                            factories.add((entity, random) ->
                                    new MerchantOffer(
                                    //Wants
                                    new ItemCost(Items.EMERALD, 16+ random.nextIntBetweenInclusive(0,32)),
                                    //Gives
                                    AlchemyRecipeRandomlyLootFunction.getRandomFormula(TCOTS_Items_Fabric.ALCHEMY_FORMULA.getDefaultInstance(), random, AlchemyRecipeRandomlyLootFunction.getConcoctionsID()),
                                    3,
                                    60,
                                    0.2f));

                            factories.add(new VillagerTrades.ItemsForEmeralds(
                                    TCOTS_Items_Fabric.ALLSPICE,
                                    6,
                                    1,
                                    5)
                            );

                            factories.add(new VillagerTrades.ItemsForEmeralds(
                                    Items.HONEYCOMB,
                                    8,
                                    1,
                                    5)
                            );

                        }

                        //Buys
                        {

                            factories.add((entity, random) -> new MerchantOffer(
                                    //Wants
                                    new ItemCost(Items.ALLIUM, 4),
                                    //Gives
                                    new ItemStack(Items.EMERALD, 6),
                                    12,
                                    10,
                                    0.05f));
                        }

                    });

            TradeOfferHelper.registerVillagerOffers(TCOTS_PointOfInterest.HERBALIST, 3,
                    factories -> {

                        //Sell
                        {
                            factories.add((entity, random) -> new MerchantOffer(
                                    //Wants
                                    new ItemCost(Items.EMERALD, 8),
                                    //Gives
                                    new ItemStack(TCOTS_Items_Fabric.CHERRY_CORDIAL, 1),
                                    12,
                                    10,
                                    0.05f));

                            factories.add((entity, random) -> new MerchantOffer(
                                    //Wants
                                    new ItemCost(Items.EMERALD, 12),
                                    //Gives
                                    new ItemStack(TCOTS_Items_Fabric.MANDRAKE_CORDIAL, 1),
                                    12,
                                    10,
                                    0.05f));


                            factories.add((entity, random) -> new MerchantOffer(
                                    //Wants
                                    new ItemCost(Items.EMERALD, 8),
                                    //Gives
                                    new ItemStack(TCOTS_Items_Fabric.PUFFBALL, 2),
                                    12,
                                    10,
                                    0.05f));

                        }

                        //Buys
                        {

                            factories.add((entity, random) -> new MerchantOffer(
                                    //Wants
                                    new ItemCost(TCOTS_Items_Fabric.MONSTER_FAT, 4),
                                    //Gives
                                    new ItemStack(Items.EMERALD, 8),
                                    12,
                                    20,
                                    0.05f));

                        }

                    });

            TradeOfferHelper.registerVillagerOffers(TCOTS_PointOfInterest.HERBALIST, 4,
                    factories -> {
                        //Sells
                        {
                            factories.add((entity, random) -> new MerchantOffer(
                                    //Wants
                                    new ItemCost(Items.EMERALD, 16 + random.nextIntBetweenInclusive(0, 32)),
                                    //Gives
                                    AlchemyRecipeRandomlyLootFunction.getRandomFormula(TCOTS_Items_Fabric.ALCHEMY_FORMULA.getDefaultInstance(), random, AlchemyRecipeRandomlyLootFunction.getConcoctionsID()),
                                    3,
                                    100,
                                    0.2f));

                            factories.add((entity, random) -> new MerchantOffer(
                                    //Wants
                                    new ItemCost(Items.EMERALD, 6),
                                    //Gives
                                    new ItemStack(TCOTS_Items_Fabric.ALCHEMY_PASTE, 1),
                                    16,
                                    15,
                                    0.05f));

                            factories.add((entity, random) -> new MerchantOffer(
                                    //Wants
                                    new ItemCost(Items.EMERALD, 8),
                                    //Gives
                                    new ItemStack(TCOTS_Items_Fabric.ALCHEMISTS_POWDER, 2),
                                    16,
                                    15,
                                    0.05f));

                        }

                        //Buys
                        {
                            factories.add((entity, random) -> new MerchantOffer(
                                    //Wants
                                    new ItemCost(TCOTS_Items_Fabric.WATER_ESSENCE, 2),
                                    //Gives
                                    new ItemStack(Items.EMERALD, 8),
                                    12,
                                    15,
                                    0.05f));

                        }

                    });

            TradeOfferHelper.registerVillagerOffers(TCOTS_PointOfInterest.HERBALIST, 5,
                    factories -> {

                        //Sell
                        {
                            {
                                //Potions
                                {
                                    //Enhanced
                                    factories.add((entity, random) -> upgradeRecipeTrade(TCOTS_Items_Fabric.SWALLOW_POTION, 32, TCOTS_Items_Fabric.SWALLOW_POTION_ENHANCED));
                                    factories.add((entity, random) -> upgradeRecipeTrade(TCOTS_Items_Fabric.CAT_POTION, 32, TCOTS_Items_Fabric.CAT_POTION_ENHANCED));
                                    factories.add((entity, random) -> upgradeRecipeTrade(TCOTS_Items_Fabric.WHITE_RAFFARDS_DECOCTION, 32, TCOTS_Items_Fabric.WHITE_RAFFARDS_DECOCTION_ENHANCED));
                                    factories.add((entity, random) -> upgradeRecipeTrade(TCOTS_Items_Fabric.BLACK_BLOOD_POTION, 32, TCOTS_Items_Fabric.BLACK_BLOOD_POTION_ENHANCED));
                                    factories.add((entity, random) -> upgradeRecipeTrade(TCOTS_Items_Fabric.MARIBOR_FOREST_POTION, 32, TCOTS_Items_Fabric.MARIBOR_FOREST_POTION_ENHANCED));
                                    factories.add((entity, random) -> upgradeRecipeTrade(TCOTS_Items_Fabric.WOLF_POTION, 32, TCOTS_Items_Fabric.WOLF_POTION_ENHANCED));
                                    factories.add((entity, random) -> upgradeRecipeTrade(TCOTS_Items_Fabric.ROOK_POTION, 32, TCOTS_Items_Fabric.ROOK_POTION_ENHANCED));

                                    factories.add((entity, random) -> upgradeRecipeTrade(TCOTS_Items_Fabric.WHITE_HONEY_POTION, 16, TCOTS_Items_Fabric.WHITE_HONEY_POTION_ENHANCED));


                                    //Superior
                                    factories.add((entity, random) -> upgradeRecipeTrade(TCOTS_Items_Fabric.SWALLOW_POTION_ENHANCED, 48, TCOTS_Items_Fabric.SWALLOW_POTION_SUPERIOR));
                                    factories.add((entity, random) -> upgradeRecipeTrade(TCOTS_Items_Fabric.CAT_POTION_ENHANCED, 48, TCOTS_Items_Fabric.CAT_POTION_SUPERIOR));
                                    factories.add((entity, random) -> upgradeRecipeTrade(TCOTS_Items_Fabric.WHITE_RAFFARDS_DECOCTION_ENHANCED, 48, TCOTS_Items_Fabric.WHITE_RAFFARDS_DECOCTION_SUPERIOR));
                                    factories.add((entity, random) -> upgradeRecipeTrade(TCOTS_Items_Fabric.BLACK_BLOOD_POTION_ENHANCED, 48, TCOTS_Items_Fabric.BLACK_BLOOD_POTION_SUPERIOR));
                                    factories.add((entity, random) -> upgradeRecipeTrade(TCOTS_Items_Fabric.MARIBOR_FOREST_POTION_ENHANCED, 48, TCOTS_Items_Fabric.MARIBOR_FOREST_POTION_SUPERIOR));
                                    factories.add((entity, random) -> upgradeRecipeTrade(TCOTS_Items_Fabric.WOLF_POTION_ENHANCED, 48, TCOTS_Items_Fabric.WOLF_POTION_SUPERIOR));
                                    factories.add((entity, random) -> upgradeRecipeTrade(TCOTS_Items_Fabric.ROOK_POTION_ENHANCED, 48, TCOTS_Items_Fabric.ROOK_POTION_SUPERIOR));

                                    factories.add((entity, random) -> upgradeRecipeTrade(TCOTS_Items_Fabric.WHITE_HONEY_POTION_ENHANCED, 32, TCOTS_Items_Fabric.WHITE_HONEY_POTION_SUPERIOR));
                                }

                                //Bombs
                                {
                                    //Enhanced
                                    factories.add((entity, random) -> upgradeRecipeTrade(TCOTS_Items_Fabric.GRAPESHOT, 32, TCOTS_Items_Fabric.GRAPESHOT_ENHANCED));
                                    factories.add((entity, random) -> upgradeRecipeTrade(TCOTS_Items_Fabric.SAMUM, 32, TCOTS_Items_Fabric.SAMUM_ENHANCED));
                                    factories.add((entity, random) -> upgradeRecipeTrade(TCOTS_Items_Fabric.DANCING_STAR, 32, TCOTS_Items_Fabric.DANCING_STAR_ENHANCED));
                                    factories.add((entity, random) -> upgradeRecipeTrade(TCOTS_Items_Fabric.DEVILS_PUFFBALL, 32, TCOTS_Items_Fabric.DEVILS_PUFFBALL_ENHANCED));
                                    factories.add((entity, random) -> upgradeRecipeTrade(TCOTS_Items_Fabric.DRAGONS_DREAM, 32, TCOTS_Items_Fabric.DRAGONS_DREAM_ENHANCED));
                                    factories.add((entity, random) -> upgradeRecipeTrade(TCOTS_Items_Fabric.NORTHERN_WIND, 32, TCOTS_Items_Fabric.NORTHERN_WIND_ENHANCED));
                                    factories.add((entity, random) -> upgradeRecipeTrade(TCOTS_Items_Fabric.DIMERITIUM_BOMB, 32, TCOTS_Items_Fabric.DIMERITIUM_BOMB_ENHANCED));
                                    factories.add((entity, random) -> upgradeRecipeTrade(TCOTS_Items_Fabric.MOON_DUST, 32, TCOTS_Items_Fabric.MOON_DUST_ENHANCED));

                                    //Superior
                                    factories.add((entity, random) -> upgradeRecipeTrade(TCOTS_Items_Fabric.GRAPESHOT_ENHANCED, 56, TCOTS_Items_Fabric.GRAPESHOT_SUPERIOR));
                                    factories.add((entity, random) -> upgradeRecipeTrade(TCOTS_Items_Fabric.SAMUM_ENHANCED, 56, TCOTS_Items_Fabric.SAMUM_SUPERIOR));
                                    factories.add((entity, random) -> upgradeRecipeTrade(TCOTS_Items_Fabric.DANCING_STAR_ENHANCED, 56, TCOTS_Items_Fabric.DANCING_STAR_SUPERIOR));
                                    factories.add((entity, random) -> upgradeRecipeTrade(TCOTS_Items_Fabric.DEVILS_PUFFBALL_ENHANCED, 56, TCOTS_Items_Fabric.DEVILS_PUFFBALL_SUPERIOR));
                                    factories.add((entity, random) -> upgradeRecipeTrade(TCOTS_Items_Fabric.DRAGONS_DREAM_ENHANCED, 56, TCOTS_Items_Fabric.DRAGONS_DREAM_SUPERIOR));
                                    factories.add((entity, random) -> upgradeRecipeTrade(TCOTS_Items_Fabric.NORTHERN_WIND_ENHANCED, 56, TCOTS_Items_Fabric.NORTHERN_WIND_SUPERIOR));
                                    factories.add((entity, random) -> upgradeRecipeTrade(TCOTS_Items_Fabric.DIMERITIUM_BOMB_ENHANCED, 56, TCOTS_Items_Fabric.DIMERITIUM_BOMB_SUPERIOR));
                                    factories.add((entity, random) -> upgradeRecipeTrade(TCOTS_Items_Fabric.MOON_DUST_ENHANCED, 56, TCOTS_Items_Fabric.MOON_DUST_SUPERIOR));
                                }

                                //Oils
                                {
                                    //Enhanced
                                    factories.add((entity, random) -> upgradeRecipeTrade(TCOTS_Items_Fabric.NECROPHAGE_OIL, 16, TCOTS_Items_Fabric.ENHANCED_NECROPHAGE_OIL));
                                    factories.add((entity, random) -> upgradeRecipeTrade(TCOTS_Items_Fabric.OGROID_OIL, 16, TCOTS_Items_Fabric.ENHANCED_OGROID_OIL));
                                    factories.add((entity, random) -> upgradeRecipeTrade(TCOTS_Items_Fabric.BEAST_OIL, 16, TCOTS_Items_Fabric.ENHANCED_BEAST_OIL));
                                    factories.add((entity, random) -> upgradeRecipeTrade(TCOTS_Items_Fabric.HANGED_OIL, 16, TCOTS_Items_Fabric.ENHANCED_HANGED_OIL));

                                    factories.add((entity, random) -> upgradeRecipeTrade(TCOTS_Items_Fabric.ENHANCED_NECROPHAGE_OIL, 36, TCOTS_Items_Fabric.SUPERIOR_NECROPHAGE_OIL));
                                    factories.add((entity, random) -> upgradeRecipeTrade(TCOTS_Items_Fabric.ENHANCED_OGROID_OIL, 36, TCOTS_Items_Fabric.SUPERIOR_OGROID_OIL));
                                    factories.add((entity, random) -> upgradeRecipeTrade(TCOTS_Items_Fabric.ENHANCED_BEAST_OIL, 36, TCOTS_Items_Fabric.SUPERIOR_BEAST_OIL));
                                    factories.add((entity, random) -> upgradeRecipeTrade(TCOTS_Items_Fabric.ENHANCED_HANGED_OIL, 36, TCOTS_Items_Fabric.SUPERIOR_HANGED_OIL));

                                }

                                //Ingredients
                                {
                                    factories.add((entity, random) -> miscRecipeTrade(TCOTS_Items_Fabric.WHITE_GULL));
                                    factories.add((entity, random) -> miscRecipeTrade(TCOTS_Items_Fabric.STAMMELFORDS_DUST));
                                    factories.add((entity, random) -> miscRecipeTrade(TCOTS_Items_Fabric.AETHER));
                                    factories.add((entity, random) -> miscRecipeTrade(TCOTS_Items_Fabric.HYDRAGENUM));
                                    factories.add((entity, random) -> miscRecipeTrade(TCOTS_Items_Fabric.NIGREDO));
                                    factories.add((entity, random) -> miscRecipeTrade(TCOTS_Items_Fabric.QUEBRITH));
                                    factories.add((entity, random) -> miscRecipeTrade(TCOTS_Items_Fabric.REBIS));
                                    factories.add((entity, random) -> miscRecipeTrade(TCOTS_Items_Fabric.RUBEDO));
                                    factories.add((entity, random) -> miscRecipeTrade(TCOTS_Items_Fabric.VERMILION));
                                    factories.add((entity, random) -> miscRecipeTrade(TCOTS_Items_Fabric.VITRIOL));
                                }
                            }
                        }

                    });
        }

        //Farmer
        //Alcohol & Ergot Seeds
        {
            TradeOfferHelper.registerVillagerOffers(VillagerProfession.FARMER, 2,
                    factories -> {
                        factories.add((entity, random) -> new MerchantOffer(
                                //Wants
                                new ItemCost(Items.EMERALD, 1),
                                //Gives
                                new ItemStack(TCOTS_Items_Fabric.ERGOT_SEEDS, 1),
                                16,
                                5,
                                0.05f));
                    }
            );

            TradeOfferHelper.registerVillagerOffers(VillagerProfession.FARMER, 3,
                    factories -> {
                        factories.add((entity, random) -> new MerchantOffer(
                                //Wants
                                new ItemCost(Items.EMERALD, 6),
                                //Gives
                                new ItemStack(TCOTS_Items_Fabric.VILLAGE_HERBAL, 1),
                                12,
                                5,
                                0.05f));
                    }
            );
        }

        //Butcher
        //Monster Fat
        {
            TradeOfferHelper.registerVillagerOffers(VillagerProfession.BUTCHER, 3,
                    factories -> {
                        factories.add((entity, random) -> new MerchantOffer(
                                //Wants
                                new ItemCost(Items.EMERALD, 12),
                                //Gives
                                new ItemStack(TCOTS_Items_Fabric.MONSTER_FAT, 1),
                                12,
                                15,
                                0.2f));
                    }
            );
        }


//        //Wandering Trader
//            //Rare ingredients
//        TradeOfferHelper.registerWanderingTraderOffers(2,
//                factories -> {
//
//
//                });


        //Librarian
            //Bestiary
        {
            TradeOfferHelper.registerVillagerOffers(VillagerProfession.LIBRARIAN, 1,
                    factories -> {
                        factories.add((entity, random) -> new MerchantOffer(
                                //Wants
                                new ItemCost(Items.EMERALD, 12),
                                //Gives
                                new ItemStack(TCOTS_Items_Fabric.WITCHER_BESTIARY, 1),
                                3,
                                10,
                                0.05f));
                    }
            );
        }

        //Cleric
            //Monster Parts
        {
            TradeOfferHelper.registerVillagerOffers(VillagerProfession.CLERIC, 1,
                    factories -> {
                        factories.add((entity, random) -> new MerchantOffer(
                                //Wants
                                new ItemCost(TCOTS_Items_Fabric.DEVOURER_TEETH, 8),
                                //Gives
                                new ItemStack(Items.EMERALD, 16),
                                3,
                                15,
                                0.2f));
                    }
            );

            TradeOfferHelper.registerVillagerOffers(VillagerProfession.CLERIC, 1,
                    factories -> {
                        factories.add((entity, random) -> new MerchantOffer(
                                //Wants
                                new ItemCost(TCOTS_Items_Fabric.BULLVORE_HORN_FRAGMENT, 1),
                                //Gives
                                new ItemStack(Items.EMERALD, 16),
                                3,
                                20,
                                0.2f));
                    }
            );

            TradeOfferHelper.registerVillagerOffers(VillagerProfession.CLERIC, 1,
                    factories -> {
                        factories.add((entity, random) -> new MerchantOffer(
                                //Wants
                                new ItemCost(TCOTS_Items_Fabric.GRAVEIR_BONE, 2),
                                //Gives
                                new ItemStack(Items.EMERALD, 16),
                                3,
                                15,
                                0.2f));
                    }
            );

            TradeOfferHelper.registerVillagerOffers(VillagerProfession.CLERIC, 1,
                    factories -> {
                        factories.add((entity, random) -> new MerchantOffer(
                                //Wants
                                new ItemCost(TCOTS_Items_Fabric.CADAVERINE, 16),
                                //Gives
                                new ItemStack(Items.EMERALD, 2),
                                12,
                                5,
                                0.05f));
                    }
            );
        }

        //Cartographer
        {
            TradeOfferHelper.registerVillagerOffers(VillagerProfession.CARTOGRAPHER, 3,
                factories -> factories.add(
                        new VillagerTrades.TreasureMapForEmeralds(
                                16,
                                TagKey.create(Registries.STRUCTURE, ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "on_ice_giant_maps")),
                                "filled_map.giant_cave",
                                TCOTS_MapIcons.GIANT_CAVE,
                                12,
                                10))
            );
        }
    }



    private static MerchantOffer upgradeRecipeTrade(Item recipeToUpgrade, int Cost, Item upgradedRecipe){
        return new MerchantOffer(
                //Wants
                new ItemCost(TCOTS_Items_Fabric.ALCHEMY_FORMULA)
                        .withComponents(builder -> builder.expect(TCOTS_Items_Fabric.RECIPE_TEACHER_COMPONENT,
                        new RecipeTeacherComponent(BuiltInRegistries.ITEM.getKey(recipeToUpgrade).toString(), false))),
                Optional.of(new ItemCost(Items.EMERALD, Cost)),
                //Gives
                AlchemyFormulaUtil.setFormula(BuiltInRegistries.ITEM.getKey(upgradedRecipe)),
                3,
                30,
                0.2f);
    }

    private static MerchantOffer miscRecipeTrade(Item item){
        return new MerchantOffer(
                //Wants
                new ItemCost(Items.EMERALD, 16),
                //Gives
                AlchemyFormulaUtil.setFormula(BuiltInRegistries.ITEM.getKey(item)),
                3,
                30,
                0.2f);
    }
}
