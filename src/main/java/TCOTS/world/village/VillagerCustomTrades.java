package TCOTS.world.village;

import TCOTS.TCOTS_Main;
import TCOTS.items.AlchemyRecipeRandomlyLootFunction;
import TCOTS.items.TCOTS_Items;
import TCOTS.items.components.RecipeTeacherComponent;
import TCOTS.items.maps.TCOTS_MapIcons;
import TCOTS.utils.AlchemyFormulaUtil;
import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOffers;
import net.minecraft.village.TradedItem;
import net.minecraft.village.VillagerProfession;

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
                           factories.add(new TradeOffers.SellItemFactory(
                                   //Gives
                                   TCOTS_Items.ALCHEMY_BOOK,
                                   6,
                                   1,
                                   3,
                                   10
                           ));


                            factories.add(new TradeOffers.SellItemFactory(
                                    //Gives
                                    TCOTS_Items.ICY_SPIRIT,
                                    4,
                                    1,
                                    16,
                                    1)
                            );
                        }

                        //Buys
                        {
                            factories.add(new TradeOffers.BuyItemFactory(
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
                                    new TradeOffer(
                                    //Wants
                                    new TradedItem(Items.EMERALD, 16+ random.nextBetween(0,32)),
                                    //Gives
                                    AlchemyRecipeRandomlyLootFunction.getRandomFormula(TCOTS_Items.ALCHEMY_FORMULA.getDefaultStack(), random, AlchemyRecipeRandomlyLootFunction.getConcoctionsID()),
                                    3,
                                    60,
                                    0.2f));

                            factories.add(new TradeOffers.SellItemFactory(
                                    TCOTS_Items.ALLSPICE,
                                    6,
                                    1,
                                    5)
                            );

                            factories.add(new TradeOffers.SellItemFactory(
                                    Items.HONEYCOMB,
                                    8,
                                    1,
                                    5)
                            );

                        }

                        //Buys
                        {

                            factories.add((entity, random) -> new TradeOffer(
                                    //Wants
                                    new TradedItem(Items.ALLIUM, 4),
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
                            factories.add((entity, random) -> new TradeOffer(
                                    //Wants
                                    new TradedItem(Items.EMERALD, 8),
                                    //Gives
                                    new ItemStack(TCOTS_Items.CHERRY_CORDIAL, 1),
                                    12,
                                    10,
                                    0.05f));

                            factories.add((entity, random) -> new TradeOffer(
                                    //Wants
                                    new TradedItem(Items.EMERALD, 12),
                                    //Gives
                                    new ItemStack(TCOTS_Items.MANDRAKE_CORDIAL, 1),
                                    12,
                                    10,
                                    0.05f));


                            factories.add((entity, random) -> new TradeOffer(
                                    //Wants
                                    new TradedItem(Items.EMERALD, 8),
                                    //Gives
                                    new ItemStack(TCOTS_Items.PUFFBALL, 2),
                                    12,
                                    10,
                                    0.05f));

                        }

                        //Buys
                        {

                            factories.add((entity, random) -> new TradeOffer(
                                    //Wants
                                    new TradedItem(TCOTS_Items.MONSTER_FAT, 4),
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
                            factories.add((entity, random) -> new TradeOffer(
                                    //Wants
                                    new TradedItem(Items.EMERALD, 16 + random.nextBetween(0, 32)),
                                    //Gives
                                    AlchemyRecipeRandomlyLootFunction.getRandomFormula(TCOTS_Items.ALCHEMY_FORMULA.getDefaultStack(), random, AlchemyRecipeRandomlyLootFunction.getConcoctionsID()),
                                    3,
                                    100,
                                    0.2f));

                            factories.add((entity, random) -> new TradeOffer(
                                    //Wants
                                    new TradedItem(Items.EMERALD, 6),
                                    //Gives
                                    new ItemStack(TCOTS_Items.ALCHEMY_PASTE, 1),
                                    16,
                                    15,
                                    0.05f));

                            factories.add((entity, random) -> new TradeOffer(
                                    //Wants
                                    new TradedItem(Items.EMERALD, 8),
                                    //Gives
                                    new ItemStack(TCOTS_Items.ALCHEMISTS_POWDER, 2),
                                    16,
                                    15,
                                    0.05f));

                        }

                        //Buys
                        {
                            factories.add((entity, random) -> new TradeOffer(
                                    //Wants
                                    new TradedItem(TCOTS_Items.WATER_ESSENCE, 2),
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
                                    factories.add((entity, random) -> upgradeRecipeTrade(TCOTS_Items.SWALLOW_POTION, 32, TCOTS_Items.SWALLOW_POTION_ENHANCED));
                                    factories.add((entity, random) -> upgradeRecipeTrade(TCOTS_Items.CAT_POTION, 32, TCOTS_Items.CAT_POTION_ENHANCED));
                                    factories.add((entity, random) -> upgradeRecipeTrade(TCOTS_Items.WHITE_RAFFARDS_DECOCTION, 32, TCOTS_Items.WHITE_RAFFARDS_DECOCTION_ENHANCED));
                                    factories.add((entity, random) -> upgradeRecipeTrade(TCOTS_Items.BLACK_BLOOD_POTION, 32, TCOTS_Items.BLACK_BLOOD_POTION_ENHANCED));
                                    factories.add((entity, random) -> upgradeRecipeTrade(TCOTS_Items.MARIBOR_FOREST_POTION, 32, TCOTS_Items.MARIBOR_FOREST_POTION_ENHANCED));
                                    factories.add((entity, random) -> upgradeRecipeTrade(TCOTS_Items.WOLF_POTION, 32, TCOTS_Items.WOLF_POTION_ENHANCED));
                                    factories.add((entity, random) -> upgradeRecipeTrade(TCOTS_Items.ROOK_POTION, 32, TCOTS_Items.ROOK_POTION_ENHANCED));

                                    factories.add((entity, random) -> upgradeRecipeTrade(TCOTS_Items.WHITE_HONEY_POTION, 16, TCOTS_Items.WHITE_HONEY_POTION_ENHANCED));


                                    //Superior
                                    factories.add((entity, random) -> upgradeRecipeTrade(TCOTS_Items.SWALLOW_POTION_ENHANCED, 48, TCOTS_Items.SWALLOW_POTION_SUPERIOR));
                                    factories.add((entity, random) -> upgradeRecipeTrade(TCOTS_Items.CAT_POTION_ENHANCED, 48, TCOTS_Items.CAT_POTION_SUPERIOR));
                                    factories.add((entity, random) -> upgradeRecipeTrade(TCOTS_Items.WHITE_RAFFARDS_DECOCTION_ENHANCED, 48, TCOTS_Items.WHITE_RAFFARDS_DECOCTION_SUPERIOR));
                                    factories.add((entity, random) -> upgradeRecipeTrade(TCOTS_Items.BLACK_BLOOD_POTION_ENHANCED, 48, TCOTS_Items.BLACK_BLOOD_POTION_SUPERIOR));
                                    factories.add((entity, random) -> upgradeRecipeTrade(TCOTS_Items.MARIBOR_FOREST_POTION_ENHANCED, 48, TCOTS_Items.MARIBOR_FOREST_POTION_SUPERIOR));
                                    factories.add((entity, random) -> upgradeRecipeTrade(TCOTS_Items.WOLF_POTION_ENHANCED, 48, TCOTS_Items.WOLF_POTION_SUPERIOR));
                                    factories.add((entity, random) -> upgradeRecipeTrade(TCOTS_Items.ROOK_POTION_ENHANCED, 48, TCOTS_Items.ROOK_POTION_SUPERIOR));

                                    factories.add((entity, random) -> upgradeRecipeTrade(TCOTS_Items.WHITE_HONEY_POTION_ENHANCED, 32, TCOTS_Items.WHITE_HONEY_POTION_SUPERIOR));
                                }

                                //Bombs
                                {
                                    //Enhanced
                                    factories.add((entity, random) -> upgradeRecipeTrade(TCOTS_Items.GRAPESHOT, 32, TCOTS_Items.GRAPESHOT_ENHANCED));
                                    factories.add((entity, random) -> upgradeRecipeTrade(TCOTS_Items.SAMUM, 32, TCOTS_Items.SAMUM_ENHANCED));
                                    factories.add((entity, random) -> upgradeRecipeTrade(TCOTS_Items.DANCING_STAR, 32, TCOTS_Items.DANCING_STAR_ENHANCED));
                                    factories.add((entity, random) -> upgradeRecipeTrade(TCOTS_Items.DEVILS_PUFFBALL, 32, TCOTS_Items.DEVILS_PUFFBALL_ENHANCED));
                                    factories.add((entity, random) -> upgradeRecipeTrade(TCOTS_Items.DRAGONS_DREAM, 32, TCOTS_Items.DRAGONS_DREAM_ENHANCED));
                                    factories.add((entity, random) -> upgradeRecipeTrade(TCOTS_Items.NORTHERN_WIND, 32, TCOTS_Items.NORTHERN_WIND_ENHANCED));
                                    factories.add((entity, random) -> upgradeRecipeTrade(TCOTS_Items.DIMERITIUM_BOMB, 32, TCOTS_Items.DIMERITIUM_BOMB_ENHANCED));
                                    factories.add((entity, random) -> upgradeRecipeTrade(TCOTS_Items.MOON_DUST, 32, TCOTS_Items.MOON_DUST_ENHANCED));

                                    //Superior
                                    factories.add((entity, random) -> upgradeRecipeTrade(TCOTS_Items.GRAPESHOT_ENHANCED, 56, TCOTS_Items.GRAPESHOT_SUPERIOR));
                                    factories.add((entity, random) -> upgradeRecipeTrade(TCOTS_Items.SAMUM_ENHANCED, 56, TCOTS_Items.SAMUM_SUPERIOR));
                                    factories.add((entity, random) -> upgradeRecipeTrade(TCOTS_Items.DANCING_STAR_ENHANCED, 56, TCOTS_Items.DANCING_STAR_SUPERIOR));
                                    factories.add((entity, random) -> upgradeRecipeTrade(TCOTS_Items.DEVILS_PUFFBALL_ENHANCED, 56, TCOTS_Items.DEVILS_PUFFBALL_SUPERIOR));
                                    factories.add((entity, random) -> upgradeRecipeTrade(TCOTS_Items.DRAGONS_DREAM_ENHANCED, 56, TCOTS_Items.DRAGONS_DREAM_SUPERIOR));
                                    factories.add((entity, random) -> upgradeRecipeTrade(TCOTS_Items.NORTHERN_WIND_ENHANCED, 56, TCOTS_Items.NORTHERN_WIND_SUPERIOR));
                                    factories.add((entity, random) -> upgradeRecipeTrade(TCOTS_Items.DIMERITIUM_BOMB_ENHANCED, 56, TCOTS_Items.DIMERITIUM_BOMB_SUPERIOR));
                                    factories.add((entity, random) -> upgradeRecipeTrade(TCOTS_Items.MOON_DUST_ENHANCED, 56, TCOTS_Items.MOON_DUST_SUPERIOR));
                                }

                                //Oils
                                {
                                    //Enhanced
                                    factories.add((entity, random) -> upgradeRecipeTrade(TCOTS_Items.NECROPHAGE_OIL, 16, TCOTS_Items.ENHANCED_NECROPHAGE_OIL));
                                    factories.add((entity, random) -> upgradeRecipeTrade(TCOTS_Items.OGROID_OIL, 16, TCOTS_Items.ENHANCED_OGROID_OIL));
                                    factories.add((entity, random) -> upgradeRecipeTrade(TCOTS_Items.BEAST_OIL, 16, TCOTS_Items.ENHANCED_BEAST_OIL));
                                    factories.add((entity, random) -> upgradeRecipeTrade(TCOTS_Items.HANGED_OIL, 16, TCOTS_Items.ENHANCED_HANGED_OIL));

                                    factories.add((entity, random) -> upgradeRecipeTrade(TCOTS_Items.ENHANCED_NECROPHAGE_OIL, 36, TCOTS_Items.SUPERIOR_NECROPHAGE_OIL));
                                    factories.add((entity, random) -> upgradeRecipeTrade(TCOTS_Items.ENHANCED_OGROID_OIL, 36, TCOTS_Items.SUPERIOR_OGROID_OIL));
                                    factories.add((entity, random) -> upgradeRecipeTrade(TCOTS_Items.ENHANCED_BEAST_OIL, 36, TCOTS_Items.SUPERIOR_BEAST_OIL));
                                    factories.add((entity, random) -> upgradeRecipeTrade(TCOTS_Items.ENHANCED_HANGED_OIL, 36, TCOTS_Items.SUPERIOR_HANGED_OIL));

                                }

                                //Ingredients
                                {
                                    factories.add((entity, random) -> miscRecipeTrade(TCOTS_Items.WHITE_GULL));
                                    factories.add((entity, random) -> miscRecipeTrade(TCOTS_Items.STAMMELFORDS_DUST));
                                    factories.add((entity, random) -> miscRecipeTrade(TCOTS_Items.AETHER));
                                    factories.add((entity, random) -> miscRecipeTrade(TCOTS_Items.HYDRAGENUM));
                                    factories.add((entity, random) -> miscRecipeTrade(TCOTS_Items.NIGREDO));
                                    factories.add((entity, random) -> miscRecipeTrade(TCOTS_Items.QUEBRITH));
                                    factories.add((entity, random) -> miscRecipeTrade(TCOTS_Items.REBIS));
                                    factories.add((entity, random) -> miscRecipeTrade(TCOTS_Items.RUBEDO));
                                    factories.add((entity, random) -> miscRecipeTrade(TCOTS_Items.VERMILION));
                                    factories.add((entity, random) -> miscRecipeTrade(TCOTS_Items.VITRIOL));
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
                        factories.add((entity, random) -> new TradeOffer(
                                //Wants
                                new TradedItem(Items.EMERALD, 1),
                                //Gives
                                new ItemStack(TCOTS_Items.ERGOT_SEEDS, 1),
                                16,
                                5,
                                0.05f));
                    }
            );

            TradeOfferHelper.registerVillagerOffers(VillagerProfession.FARMER, 3,
                    factories -> {
                        factories.add((entity, random) -> new TradeOffer(
                                //Wants
                                new TradedItem(Items.EMERALD, 6),
                                //Gives
                                new ItemStack(TCOTS_Items.VILLAGE_HERBAL, 1),
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
                        factories.add((entity, random) -> new TradeOffer(
                                //Wants
                                new TradedItem(Items.EMERALD, 12),
                                //Gives
                                new ItemStack(TCOTS_Items.MONSTER_FAT, 1),
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
                        factories.add((entity, random) -> new TradeOffer(
                                //Wants
                                new TradedItem(Items.EMERALD, 12),
                                //Gives
                                new ItemStack(TCOTS_Items.WITCHER_BESTIARY, 1),
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
                        factories.add((entity, random) -> new TradeOffer(
                                //Wants
                                new TradedItem(TCOTS_Items.DEVOURER_TEETH, 8),
                                //Gives
                                new ItemStack(Items.EMERALD, 16),
                                3,
                                15,
                                0.2f));
                    }
            );

            TradeOfferHelper.registerVillagerOffers(VillagerProfession.CLERIC, 1,
                    factories -> {
                        factories.add((entity, random) -> new TradeOffer(
                                //Wants
                                new TradedItem(TCOTS_Items.BULLVORE_HORN_FRAGMENT, 1),
                                //Gives
                                new ItemStack(Items.EMERALD, 16),
                                3,
                                20,
                                0.2f));
                    }
            );

            TradeOfferHelper.registerVillagerOffers(VillagerProfession.CLERIC, 1,
                    factories -> {
                        factories.add((entity, random) -> new TradeOffer(
                                //Wants
                                new TradedItem(TCOTS_Items.GRAVEIR_BONE, 2),
                                //Gives
                                new ItemStack(Items.EMERALD, 16),
                                3,
                                15,
                                0.2f));
                    }
            );

            TradeOfferHelper.registerVillagerOffers(VillagerProfession.CLERIC, 1,
                    factories -> {
                        factories.add((entity, random) -> new TradeOffer(
                                //Wants
                                new TradedItem(TCOTS_Items.CADAVERINE, 16),
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
                        new TradeOffers.SellMapFactory(
                                16,
                                TagKey.of(RegistryKeys.STRUCTURE, Identifier.of(TCOTS_Main.MOD_ID, "on_ice_giant_maps")),
                                "filled_map.giant_cave",
                                TCOTS_MapIcons.GIANT_CAVE,
                                12,
                                10))
            );
        }
    }



    private static TradeOffer upgradeRecipeTrade(Item recipeToUpgrade, int Cost, Item upgradedRecipe){
        return new TradeOffer(
                //Wants
                new TradedItem(TCOTS_Items.ALCHEMY_FORMULA)
                        .withComponents(builder -> builder.add(TCOTS_Items.RECIPE_TEACHER_COMPONENT,
                        new RecipeTeacherComponent(Registries.ITEM.getId(recipeToUpgrade).toString(), false))),
                Optional.of(new TradedItem(Items.EMERALD, Cost)),
                //Gives
                AlchemyFormulaUtil.setFormula(Registries.ITEM.getId(upgradedRecipe)),
                3,
                30,
                0.2f);
    }

    private static TradeOffer miscRecipeTrade(Item item){
        return new TradeOffer(
                //Wants
                new TradedItem(Items.EMERALD, 16),
                //Gives
                AlchemyFormulaUtil.setFormula(Registries.ITEM.getId(item)),
                3,
                30,
                0.2f);
    }
}
