package TCOTS.world.village;

import TCOTS.items.TCOTS_Items;
import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.VillagerProfession;

public class VillagerCustomTrades {

    public static void registerTrades(){
        //Uses            --> 16/12/3
        //Experience      --> 1/2/5/10/15/20/30
        //PriceMultiplier --> 0.05/0.2

        //Herbalist
        TradeOfferHelper.registerVillagerOffers(TCOTS_PointOfInterest.HERBALIST, 1,
                factories -> {

                    factories.add((entity, random) -> new TradeOffer(
                            //Wants
                            new ItemStack(Items.EMERALD, 6),
                            //Gives
                            new ItemStack(TCOTS_Items.ALCHEMY_BOOK, 1),
                            3,
                            5,
                            0.05f));

                    factories.add((entity, random) -> new TradeOffer(
                            //Wants
                            new ItemStack(Items.DANDELION, 12),
                            //Gives
                            new ItemStack(Items.EMERALD, 1),
                            16,
                            2,
                            0.05f));

                    factories.add((entity, random) -> new TradeOffer(
                            //Wants
                            new ItemStack(Items.EMERALD, 4),
                            //Gives
                            new ItemStack(TCOTS_Items.CELANDINE, 2),
                            12,
                            2,
                            0.05f));

                    factories.add((entity, random) -> new TradeOffer(
                            //Wants
                            new ItemStack(Items.EMERALD, 4),
                            //Gives
                            new ItemStack(TCOTS_Items.ICY_SPIRIT, 1),
                            16,
                            1,
                            0.05f));
                });

        TradeOfferHelper.registerVillagerOffers(TCOTS_PointOfInterest.HERBALIST, 2,
                factories -> {

                    factories.add((entity, random) -> new TradeOffer(
                            //Wants
                            new ItemStack(Items.EMERALD, 6),
                            //Gives
                            new ItemStack(TCOTS_Items.ALLSPICE, 1),
                            12,
                            5,
                            0.05f));

                    factories.add((entity, random) -> new TradeOffer(
                            //Wants
                            new ItemStack(Items.EMERALD, 8),
                            //Gives
                            new ItemStack(Items.HONEYCOMB, 1),
                            12,
                            5,
                            0.05f));

                    factories.add((entity, random) -> new TradeOffer(
                            //Wants
                            new ItemStack(Items.EMERALD, 5),
                            //Gives
                            new ItemStack(TCOTS_Items.VERBENA, 2),
                            12,
                            2,
                            0.05f));

                    factories.add((entity, random) -> new TradeOffer(
                            //Wants
                            new ItemStack(Items.EMERALD, 4),
                            //Gives
                            new ItemStack(TCOTS_Items.PEONY_PETALS, 8),
                            12,
                            2,
                            0.05f));

                    factories.add((entity, random) -> new TradeOffer(
                            //Wants
                            new ItemStack(Items.EMERALD, 6),
                            //Gives
                            new ItemStack(TCOTS_Items.ALLIUM_PETALS, 8),
                            12,
                            2,
                            0.05f));

                    factories.add((entity, random) -> new TradeOffer(
                            //Wants
                            new ItemStack(Items.MOSS_BLOCK, 12),
                            //Gives
                            new ItemStack(Items.EMERALD, 4),
                            16,
                            2,
                            0.05f));

                });

        TradeOfferHelper.registerVillagerOffers(TCOTS_PointOfInterest.HERBALIST, 3,
                factories -> {

                    factories.add((entity, random) -> new TradeOffer(
                            //Wants
                            new ItemStack(Items.EMERALD, 8),
                            //Gives
                            new ItemStack(TCOTS_Items.CHERRY_CORDIAL, 1),
                            12,
                            10,
                            0.05f));

                    factories.add((entity, random) -> new TradeOffer(
                            //Wants
                            new ItemStack(Items.EMERALD, 12),
                            //Gives
                            new ItemStack(TCOTS_Items.MANDRAKE_CORDIAL, 1),
                            12,
                            10,
                            0.05f));

                    factories.add((entity, random) -> new TradeOffer(
                            //Wants
                            new ItemStack(Items.EMERALD, 12),
                            //Gives
                            new ItemStack(Items.GLOW_BERRIES, 8),
                            12,
                            5,
                            0.05f));

                    factories.add((entity, random) -> new TradeOffer(
                            //Wants
                            new ItemStack(Items.EMERALD, 12),
                            //Gives
                            new ItemStack(TCOTS_Items.CROWS_EYE, 2),
                            12,
                            10,
                            0.05f));

                    factories.add((entity, random) -> new TradeOffer(
                            //Wants
                            new ItemStack(Items.EMERALD, 8),
                            //Gives
                            new ItemStack(TCOTS_Items.ARENARIA, 1),
                            12,
                            10,
                            0.05f));

                    factories.add((entity, random) -> new TradeOffer(
                            //Wants
                            new ItemStack(Items.EMERALD, 8),
                            //Gives
                            new ItemStack(TCOTS_Items.PUFFBALL, 2),
                            12,
                            10,
                            0.05f));

                    factories.add((entity, random) -> new TradeOffer(
                            //Wants
                            new ItemStack(Items.EMERALD, 8),
                            //Gives
                            new ItemStack(TCOTS_Items.SEWANT_MUSHROOMS, 4),
                            12,
                            10,
                            0.05f));

                    factories.add((entity, random) -> new TradeOffer(
                            //Wants
                            new ItemStack(TCOTS_Items.LILY_OF_THE_VALLEY_PETALS, 16),
                            //Gives
                            new ItemStack(Items.EMERALD, 4),
                            16,
                            10,
                            0.05f));

                    factories.add((entity, random) -> new TradeOffer(
                            //Wants
                            new ItemStack(TCOTS_Items.MONSTER_FAT, 4),
                            //Gives
                            new ItemStack(Items.EMERALD, 8),
                            12,
                            10,
                            0.05f));

                });

        TradeOfferHelper.registerVillagerOffers(TCOTS_PointOfInterest.HERBALIST, 4,
                factories -> {

                    factories.add((entity, random) -> new TradeOffer(
                            //Wants
                            new ItemStack(Items.EMERALD, 6),
                            //Gives
                            new ItemStack(TCOTS_Items.ALCHEMY_PASTE, 1),
                            16,
                            15,
                            0.05f));

                    factories.add((entity, random) -> new TradeOffer(
                            //Wants
                            new ItemStack(Items.EMERALD, 12),
                            //Gives
                            new ItemStack(TCOTS_Items.HAN_FIBER, 2),
                            16,
                            15,
                            0.05f));

                    factories.add((entity, random) -> new TradeOffer(
                            //Wants
                            new ItemStack(Items.EMERALD, 12),
                            //Gives
                            new ItemStack(TCOTS_Items.BRYONIA, 1),
                            16,
                            15,
                            0.05f));

                    factories.add((entity, random) -> new TradeOffer(
                            //Wants
                            new ItemStack(TCOTS_Items.WATER_ESSENCE, 2),
                            //Gives
                            new ItemStack(Items.EMERALD, 8),
                            12,
                            20,
                            0.05f));

                    factories.add((entity, random) -> new TradeOffer(
                            //Wants
                            new ItemStack(TCOTS_Items.ROTFIEND_BLOOD, 2),
                            //Gives
                            new ItemStack(Items.EMERALD, 12),
                            12,
                            20,
                            0.05f));

                });

        TradeOfferHelper.registerVillagerOffers(TCOTS_PointOfInterest.HERBALIST, 5,
                factories -> {

                    factories.add((entity, random) -> new TradeOffer(
                            //Wants
                            new ItemStack(Items.EMERALD, 32),
                            //Gives
                            new ItemStack(TCOTS_Items.VERMILION, 2),
                            3,
                            30,
                            0.2f));

                    factories.add((entity, random) -> new TradeOffer(
                            //Wants
                            new ItemStack(Items.EMERALD, 32),
                            //Gives
                            new ItemStack(TCOTS_Items.VITRIOL, 2),
                            3,
                            30,
                            0.2f));

                    factories.add((entity, random) -> new TradeOffer(
                            //Wants
                            new ItemStack(Items.EMERALD, 32),
                            //Gives
                            new ItemStack(TCOTS_Items.HYDRAGENUM, 2),
                            3,
                            30,
                            0.2f));

                    factories.add((entity, random) -> new TradeOffer(
                            //Wants
                            new ItemStack(Items.EMERALD, 32),
                            //Gives
                            new ItemStack(TCOTS_Items.QUEBRITH, 2),
                            3,
                            30,
                            0.2f));

                    factories.add((entity, random) -> new TradeOffer(
                            //Wants
                            new ItemStack(Items.EMERALD, 32),
                            //Gives
                            new ItemStack(TCOTS_Items.AETHER, 2),
                            3,
                            30,
                            0.2f));

                    factories.add((entity, random) -> new TradeOffer(
                            //Wants
                            new ItemStack(Items.EMERALD, 32),
                            //Gives
                            new ItemStack(TCOTS_Items.RUBEDO, 2),
                            3,
                            30,
                            0.2f));

                    factories.add((entity, random) -> new TradeOffer(
                            //Wants
                            new ItemStack(TCOTS_Items.WHITE_GULL, 1),
                            //Gives
                            new ItemStack(Items.EMERALD, 16),
                            12,
                            30,
                            0.2f));

                });

        //Farmer
                //Alcohol & Ergot Seeds
        TradeOfferHelper.registerVillagerOffers(VillagerProfession.FARMER, 2,
                factories -> {
                    factories.add((entity, random) -> new TradeOffer(
                            //Wants
                            new ItemStack(Items.EMERALD, 1),
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
                            new ItemStack(Items.EMERALD, 6),
                            //Gives
                            new ItemStack(TCOTS_Items.VILLAGE_HERBAL, 1),
                            12,
                            5,
                            0.05f));
                }
        );



        //Cleric
            //Ingredients/
//        TradeOfferHelper.registerVillagerOffers(VillagerProfession.CLERIC, 3,
//                factories -> {
//
//                    factories.add((entity, random) -> new TradeOffer(
//                            //Wants
//                            new ItemStack(Items.EMERALD, 24),
//                            //Gives
//                            new ItemStack(TCOTS_Items.WHITE_GULL, 1),
//                            3,
//                            15,
//                            0.2f));
//
//                }
//        );


        //Butcher
            //Monster Fat
        TradeOfferHelper.registerVillagerOffers(VillagerProfession.BUTCHER, 3,
                factories -> {
                    factories.add((entity, random) -> new TradeOffer(
                            //Wants
                            new ItemStack(Items.EMERALD, 12),
                            //Gives
                            new ItemStack(TCOTS_Items.MONSTER_FAT, 1),
                            3,
                            15,
                            0.2f));
                }
        );


        //Wandering Trader
            //Rare ingredients
//        TradeOfferHelper.registerWanderingTraderOffers(2,
//                factories -> {
//
//
//                });


        //Librarian
            //Bestiary
        TradeOfferHelper.registerVillagerOffers(VillagerProfession.LIBRARIAN, 1,
                factories -> {
                    factories.add((entity, random) -> new TradeOffer(
                            //Wants
                            new ItemStack(Items.EMERALD, 12),
                            //Gives
                            new ItemStack(TCOTS_Items.WITCHER_BESTIARY, 1),
                            3,
                            10,
                            0.05f));
                }
        );
    }
}
