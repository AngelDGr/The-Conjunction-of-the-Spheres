package TCOTS.items;

import TCOTS.TCOTS_Main;
import TCOTS.items.concoctions.*;
import TCOTS.utils.AlchemyFormulaUtil;

import java.util.ArrayList;
import java.util.List;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

@SuppressWarnings("unused")
public class TCOTS_ItemsGroups {

//    public static OwoItemGroup owoItemGroup;
      public static CreativeModeTab vanillaItemGroup;

    public static void registerGroupItems() {
        vanillaItemGroup =
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "main"),
                FabricItemGroup.builder().title(Component.translatable("itemGroup.tcots_witcher.main"))
                        .icon(() -> new ItemStack(TCOTS_Items_Fabric.WITCHER_BESTIARY))
                        .displayItems((itemDisplayParameters, entries) -> {
                            //Combat Tab
                            {
                                //Book
                                entries.accept(TCOTS_Items_Fabric.WITCHER_BESTIARY);

                                //Spawn Eggs
                                {
                                    entries.accept(TCOTS_Items_Fabric.DROWNER_SPAWN_EGG);
                                    entries.accept(TCOTS_Items_Fabric.GHOUL_SPAWN_EGG);
                                    entries.accept(TCOTS_Items_Fabric.ALGHOUL_SPAWN_EGG);
                                    entries.accept(TCOTS_Items_Fabric.ROTFIEND_SPAWN_EGG);
                                    entries.accept(TCOTS_Items_Fabric.FOGLET_SPAWN_EGG);
                                    entries.accept(TCOTS_Items_Fabric.WATER_HAG_SPAWN_EGG);
                                    entries.accept(TCOTS_Items_Fabric.GRAVE_HAG_SPAWN_EGG);
                                    entries.accept(TCOTS_Items_Fabric.SCURVER_SPAWN_EGG);
                                    entries.accept(TCOTS_Items_Fabric.DEVOURER_SPAWN_EGG);
                                    entries.accept(TCOTS_Items_Fabric.GRAVEIR_SPAWN_EGG);
                                    entries.accept(TCOTS_Items_Fabric.BULLVORE_SPAWN_EGG);

                                    entries.accept(TCOTS_Items_Fabric.NEKKER_SPAWN_EGG);
                                    entries.accept(TCOTS_Items_Fabric.NEKKER_WARRIOR_SPAWN_EGG);
                                    entries.accept(TCOTS_Items_Fabric.CYCLOPS_SPAWN_EGG);
                                    entries.accept(TCOTS_Items_Fabric.ROCK_TROLL_SPAWN_EGG);
                                    entries.accept(TCOTS_Items_Fabric.ICE_TROLL_SPAWN_EGG);
                                    entries.accept(TCOTS_Items_Fabric.FOREST_TROLL_SPAWN_EGG);
                                    entries.accept(TCOTS_Items_Fabric.ICE_GIANT_SPAWN_EGG);

                                    entries.accept(TCOTS_Items_Fabric.GIANT_ANCHOR_BLOCK_ITEM);
                                    entries.accept(TCOTS_Items_Fabric.GIANT_ANCHOR);
                                }

                                //Other Ingredients
                                {
                                    entries.accept(TCOTS_Items_Fabric.CURED_MONSTER_LEATHER);
                                }

                                //Weapons
                                {
                                    entries.accept(TCOTS_Items_Fabric.GVALCHIR);
                                    entries.accept(TCOTS_Items_Fabric.MOONBLADE);
                                    entries.accept(TCOTS_Items_Fabric.DYAEBL);
                                    entries.accept(TCOTS_Items_Fabric.ARDAENYE);
                                    entries.accept(TCOTS_Items_Fabric.WINTERS_BLADE);


                                    entries.accept(TCOTS_Items_Fabric.KNIGHT_CROSSBOW);
                                    entries.accept(TCOTS_Items_Fabric.BASE_BOLT);
                                    entries.accept(TCOTS_Items_Fabric.BLUNT_BOLT);
                                    entries.accept(TCOTS_Items_Fabric.PRECISION_BOLT);
                                    entries.accept(TCOTS_Items_Fabric.EXPLODING_BOLT);
                                    entries.accept(TCOTS_Items_Fabric.BROADHEAD_BOLT);
                                }

                                //Armors
                                {
                                    entries.accept(TCOTS_Items_Fabric.WARRIORS_LEATHER_JACKET);
                                    entries.accept(TCOTS_Items_Fabric.WARRIORS_LEATHER_TROUSERS);
                                    entries.accept(TCOTS_Items_Fabric.WARRIORS_LEATHER_BOOTS);

                                    entries.accept(TCOTS_Items_Fabric.MANTICORE_ARMOR);
                                    entries.accept(TCOTS_Items_Fabric.MANTICORE_TROUSERS);
                                    entries.accept(TCOTS_Items_Fabric.MANTICORE_BOOTS);

                                    entries.accept(TCOTS_Items_Fabric.RAVENS_ARMOR);
                                    entries.accept(TCOTS_Items_Fabric.RAVENS_TROUSERS);
                                    entries.accept(TCOTS_Items_Fabric.RAVENS_BOOTS);

                                    //Horse Armors
                                    entries.accept(TCOTS_Items_Fabric.TUNDRA_HORSE_ARMOR);
                                    entries.accept(TCOTS_Items_Fabric.KNIGHT_ERRANTS_HORSE_ARMOR);
                                }

                                //Misc/Blocks
                                {
                                    entries.accept(TCOTS_Items_Fabric.NEST_SLAB_ITEM);
                                    entries.accept(TCOTS_Items_Fabric.NEST_SKULL_ITEM);
                                    entries.accept(TCOTS_Items_Fabric.MONSTER_NEST_ITEM);
                                    entries.accept(TCOTS_Items_Fabric.WINTERS_BLADE_SKELETON_ITEM);
                                    entries.accept(TCOTS_Items_Fabric.SKELETON_BLOCK_ITEM);
                                }
                            }
                            //Alchemy Tab
                            {
                                //Book
                                entries.accept(TCOTS_Items_Fabric.ALCHEMY_BOOK);

                                //Alchemy
                                {
                                    //Ingredients
                                    {
                                        entries.accept(TCOTS_Items_Fabric.ALCHEMY_TABLE_ITEM);
                                        entries.accept(TCOTS_Items_Fabric.HERBAL_TABLE_ITEM);
                                        entries.accept(TCOTS_Items_Fabric.HERBAL_MIXTURE);

                                        entries.accept(TCOTS_Items_Fabric.ALLSPICE);
                                        entries.accept(TCOTS_Items_Fabric.ARENARIA);
                                        entries.accept(TCOTS_Items_Fabric.CELANDINE);
                                        entries.accept(TCOTS_Items_Fabric.BRYONIA);
                                        entries.accept(TCOTS_Items_Fabric.CROWS_EYE);
                                        entries.accept(TCOTS_Items_Fabric.VERBENA);
                                        entries.accept(TCOTS_Items_Fabric.HAN_FIBER);
                                        entries.accept(TCOTS_Items_Fabric.PUFFBALL);
                                        entries.accept(TCOTS_Items_Fabric.SEWANT_MUSHROOMS);

                                        entries.accept(TCOTS_Items_Fabric.ERGOT_SEEDS);

                                        entries.accept(TCOTS_Items_Fabric.PUFFBALL_MUSHROOM_BLOCK_ITEM);
                                        entries.accept(TCOTS_Items_Fabric.SEWANT_MUSHROOM_BLOCK_ITEM);
                                        entries.accept(TCOTS_Items_Fabric.SEWANT_MUSHROOM_STEM_ITEM);


                                        entries.accept(TCOTS_Items_Fabric.ICY_SPIRIT.getDefaultInstance());
                                        entries.accept(TCOTS_Items_Fabric.CHERRY_CORDIAL.getDefaultInstance());
                                        entries.accept(TCOTS_Items_Fabric.VILLAGE_HERBAL.getDefaultInstance());
                                        entries.accept(TCOTS_Items_Fabric.MANDRAKE_CORDIAL.getDefaultInstance());
                                        entries.accept(TCOTS_Items_Fabric.DWARVEN_SPIRIT.getDefaultInstance());
                                        entries.accept(TCOTS_Items_Fabric.ALCOHEST.getDefaultInstance());
                                        entries.accept(TCOTS_Items_Fabric.WHITE_GULL.getDefaultInstance());

                                        entries.accept(TCOTS_Items_Fabric.MONSTER_FAT);
                                        entries.accept(TCOTS_Items_Fabric.ALCHEMY_PASTE);
                                        entries.accept(TCOTS_Items_Fabric.STAMMELFORDS_DUST);
                                        entries.accept(TCOTS_Items_Fabric.ALCHEMISTS_POWDER);

                                        entries.accept(TCOTS_Items_Fabric.AETHER);
                                        entries.accept(TCOTS_Items_Fabric.HYDRAGENUM);
                                        entries.accept(TCOTS_Items_Fabric.NIGREDO);
                                        entries.accept(TCOTS_Items_Fabric.QUEBRITH);
                                        entries.accept(TCOTS_Items_Fabric.REBIS);
                                        entries.accept(TCOTS_Items_Fabric.RUBEDO);
                                        entries.accept(TCOTS_Items_Fabric.VERMILION);
                                        entries.accept(TCOTS_Items_Fabric.VITRIOL);
                                    }

                                    //Drops
                                    {
                                        //Necrophages
                                        entries.accept(TCOTS_Items_Fabric.DROWNER_TONGUE);
                                        entries.accept(TCOTS_Items_Fabric.DROWNER_BRAIN);
                                        entries.accept(TCOTS_Items_Fabric.GHOUL_BLOOD);
                                        entries.accept(TCOTS_Items_Fabric.ALGHOUL_BONE_MARROW);
                                        entries.accept(TCOTS_Items_Fabric.ROTFIEND_BLOOD);
                                        entries.accept(TCOTS_Items_Fabric.FOGLET_TEETH);
                                        entries.accept(TCOTS_Items_Fabric.WATER_ESSENCE);
                                        entries.accept(TCOTS_Items_Fabric.WATER_HAG_MUD_BALL);
                                        entries.accept(TCOTS_Items_Fabric.SCURVER_SPINE);
                                        entries.accept(TCOTS_Items_Fabric.DEVOURER_TEETH);
                                        entries.accept(TCOTS_Items_Fabric.CADAVERINE);
                                        entries.accept(TCOTS_Items_Fabric.GRAVEIR_BONE);
                                        entries.accept(TCOTS_Items_Fabric.BULLVORE_HORN_FRAGMENT);

                                        //Ogroids
                                        entries.accept(TCOTS_Items_Fabric.NEKKER_EYE);
                                        entries.accept(TCOTS_Items_Fabric.NEKKER_HEART);
                                        //-> Cyclops drop
                                        entries.accept(TCOTS_Items_Fabric.CAVE_TROLL_LIVER);

                                        //Mutagens
                                        {
                                            entries.accept(TCOTS_Items_Fabric.FOGLET_MUTAGEN);
                                            entries.accept(TCOTS_Items_Fabric.WATER_HAG_MUTAGEN);
                                            entries.accept(TCOTS_Items_Fabric.GRAVE_HAG_MUTAGEN);
                                            entries.accept(TCOTS_Items_Fabric.NEKKER_WARRIOR_MUTAGEN);
                                            entries.accept(TCOTS_Items_Fabric.TROLL_MUTAGEN);
                                        }
                                    }

                                    //Potions
                                    {
                                        entries.accept(TCOTS_Items_Fabric.SWALLOW_POTION.getDefaultInstance());
                                        entries.accept(TCOTS_Items_Fabric.SWALLOW_POTION_ENHANCED.getDefaultInstance());
                                        entries.accept(TCOTS_Items_Fabric.SWALLOW_POTION_SUPERIOR.getDefaultInstance());
                                        entries.accept(TCOTS_Items_Fabric.SWALLOW_SPLASH.getDefaultInstance());

                                        entries.accept(TCOTS_Items_Fabric.CAT_POTION.getDefaultInstance());
                                        entries.accept(TCOTS_Items_Fabric.CAT_POTION_ENHANCED.getDefaultInstance());
                                        entries.accept(TCOTS_Items_Fabric.CAT_POTION_SUPERIOR.getDefaultInstance());

                                        entries.accept(TCOTS_Items_Fabric.WHITE_RAFFARDS_DECOCTION.getDefaultInstance());
                                        entries.accept(TCOTS_Items_Fabric.WHITE_RAFFARDS_DECOCTION_ENHANCED.getDefaultInstance());
                                        entries.accept(TCOTS_Items_Fabric.WHITE_RAFFARDS_DECOCTION_SUPERIOR.getDefaultInstance());
                                        entries.accept(TCOTS_Items_Fabric.WHITE_RAFFARDS_DECOCTION_SPLASH.getDefaultInstance());

                                        entries.accept(TCOTS_Items_Fabric.KILLER_WHALE_POTION.getDefaultInstance());
                                        entries.accept(TCOTS_Items_Fabric.KILLER_WHALE_SPLASH.getDefaultInstance());

                                        entries.accept(TCOTS_Items_Fabric.BLACK_BLOOD_POTION.getDefaultInstance());
                                        entries.accept(TCOTS_Items_Fabric.BLACK_BLOOD_POTION_ENHANCED.getDefaultInstance());
                                        entries.accept(TCOTS_Items_Fabric.BLACK_BLOOD_POTION_SUPERIOR.getDefaultInstance());

                                        entries.accept(TCOTS_Items_Fabric.MARIBOR_FOREST_POTION.getDefaultInstance());
                                        entries.accept(TCOTS_Items_Fabric.MARIBOR_FOREST_POTION_ENHANCED.getDefaultInstance());
                                        entries.accept(TCOTS_Items_Fabric.MARIBOR_FOREST_POTION_SUPERIOR.getDefaultInstance());

                                        //W1
                                        entries.accept(TCOTS_Items_Fabric.WOLF_POTION.getDefaultInstance());
                                        entries.accept(TCOTS_Items_Fabric.WOLF_POTION_ENHANCED.getDefaultInstance());
                                        entries.accept(TCOTS_Items_Fabric.WOLF_POTION_SUPERIOR.getDefaultInstance());


                                        //W2
                                        entries.accept(TCOTS_Items_Fabric.ROOK_POTION.getDefaultInstance());
                                        entries.accept(TCOTS_Items_Fabric.ROOK_POTION_ENHANCED.getDefaultInstance());
                                        entries.accept(TCOTS_Items_Fabric.ROOK_POTION_SUPERIOR.getDefaultInstance());


                                        entries.accept(TCOTS_Items_Fabric.WHITE_HONEY_POTION.getDefaultInstance());
                                        entries.accept(TCOTS_Items_Fabric.WHITE_HONEY_POTION_ENHANCED.getDefaultInstance());
                                        entries.accept(TCOTS_Items_Fabric.WHITE_HONEY_POTION_SUPERIOR.getDefaultInstance());

                                        //Decoctions
                                        {
                                            entries.accept(TCOTS_Items_Fabric.WATER_HAG_DECOCTION);
                                            entries.accept(TCOTS_Items_Fabric.GRAVE_HAG_DECOCTION);
                                            entries.accept(TCOTS_Items_Fabric.ALGHOUL_DECOCTION);
                                            entries.accept(TCOTS_Items_Fabric.FOGLET_DECOCTION);
                                            entries.accept(TCOTS_Items_Fabric.NEKKER_WARRIOR_DECOCTION);
                                            entries.accept(TCOTS_Items_Fabric.TROLL_DECOCTION);
                                        }
                                    }

                                    //Bombs
                                    {
                                        entries.accept(TCOTS_Items_Fabric.GRAPESHOT);
                                        entries.accept(TCOTS_Items_Fabric.GRAPESHOT_ENHANCED);
                                        entries.accept(TCOTS_Items_Fabric.GRAPESHOT_SUPERIOR);

                                        entries.accept(TCOTS_Items_Fabric.SAMUM);
                                        entries.accept(TCOTS_Items_Fabric.SAMUM_ENHANCED);
                                        entries.accept(TCOTS_Items_Fabric.SAMUM_SUPERIOR);

                                        entries.accept(TCOTS_Items_Fabric.DANCING_STAR);
                                        entries.accept(TCOTS_Items_Fabric.DANCING_STAR_ENHANCED);
                                        entries.accept(TCOTS_Items_Fabric.DANCING_STAR_SUPERIOR);

                                        entries.accept(TCOTS_Items_Fabric.DEVILS_PUFFBALL);
                                        entries.accept(TCOTS_Items_Fabric.DEVILS_PUFFBALL_ENHANCED);
                                        entries.accept(TCOTS_Items_Fabric.DEVILS_PUFFBALL_SUPERIOR);

                                        entries.accept(TCOTS_Items_Fabric.DRAGONS_DREAM);
                                        entries.accept(TCOTS_Items_Fabric.DRAGONS_DREAM_ENHANCED);
                                        entries.accept(TCOTS_Items_Fabric.DRAGONS_DREAM_SUPERIOR);

                                        entries.accept(TCOTS_Items_Fabric.NORTHERN_WIND);
                                        entries.accept(TCOTS_Items_Fabric.NORTHERN_WIND_ENHANCED);
                                        entries.accept(TCOTS_Items_Fabric.NORTHERN_WIND_SUPERIOR);

                                        entries.accept(TCOTS_Items_Fabric.DIMERITIUM_BOMB);
                                        entries.accept(TCOTS_Items_Fabric.DIMERITIUM_BOMB_ENHANCED);
                                        entries.accept(TCOTS_Items_Fabric.DIMERITIUM_BOMB_SUPERIOR);

                                        entries.accept(TCOTS_Items_Fabric.MOON_DUST);
                                        entries.accept(TCOTS_Items_Fabric.MOON_DUST_ENHANCED);
                                        entries.accept(TCOTS_Items_Fabric.MOON_DUST_SUPERIOR);
                                    }

                                    //Monster Oils
                                    {
                                        entries.accept(TCOTS_Items_Fabric.NECROPHAGE_OIL);
                                        entries.accept(TCOTS_Items_Fabric.ENHANCED_NECROPHAGE_OIL);
                                        entries.accept(TCOTS_Items_Fabric.SUPERIOR_NECROPHAGE_OIL);

                                        entries.accept(TCOTS_Items_Fabric.OGROID_OIL);
                                        entries.accept(TCOTS_Items_Fabric.ENHANCED_OGROID_OIL);
                                        entries.accept(TCOTS_Items_Fabric.SUPERIOR_OGROID_OIL);

                                        entries.accept(TCOTS_Items_Fabric.BEAST_OIL);
                                        entries.accept(TCOTS_Items_Fabric.ENHANCED_BEAST_OIL);
                                        entries.accept(TCOTS_Items_Fabric.SUPERIOR_BEAST_OIL);

                                        entries.accept(TCOTS_Items_Fabric.HANGED_OIL);
                                        entries.accept(TCOTS_Items_Fabric.ENHANCED_HANGED_OIL);
                                        entries.accept(TCOTS_Items_Fabric.SUPERIOR_HANGED_OIL);
                                    }
                                }
                            }

                            //Formulae
                            addFormulaeEntries(entries);
                        })
                        .build());
    }

//    public static void registerGroupItems() {
//        owoItemGroup  = OwoItemGroup.builder(ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "main"), () -> Icon.of(TCOTS_Items.WITCHER_BESTIARY))
//                        .initializer(
//                                owoItemGroup -> {
//
//                                    owoItemGroup.addCustomTab(Icon.of(TCOTS_Items.GVALCHIR), "combat", (context, entries) ->
//                                            {
//                                                //Book
//                                                entries.accept(TCOTS_Items.WITCHER_BESTIARY);
//
//                                                //Spawn Eggs
//                                                {
//                                                    entries.accept(TCOTS_Items.DROWNER_SPAWN_EGG);
//                                                    entries.accept(TCOTS_Items.GHOUL_SPAWN_EGG);
//                                                    entries.accept(TCOTS_Items.ALGHOUL_SPAWN_EGG);
//                                                    entries.accept(TCOTS_Items.ROTFIEND_SPAWN_EGG);
//                                                    entries.accept(TCOTS_Items.FOGLET_SPAWN_EGG);
//                                                    entries.accept(TCOTS_Items.WATER_HAG_SPAWN_EGG);
//                                                    entries.accept(TCOTS_Items.GRAVE_HAG_SPAWN_EGG);
//                                                    entries.accept(TCOTS_Items.SCURVER_SPAWN_EGG);
//                                                    entries.accept(TCOTS_Items.DEVOURER_SPAWN_EGG);
//                                                    entries.accept(TCOTS_Items.GRAVEIR_SPAWN_EGG);
//                                                    entries.accept(TCOTS_Items.BULLVORE_SPAWN_EGG);
//
//                                                    entries.accept(TCOTS_Items.NEKKER_SPAWN_EGG);
//                                                    entries.accept(TCOTS_Items.NEKKER_WARRIOR_SPAWN_EGG);
//                                                    entries.accept(TCOTS_Items.CYCLOPS_SPAWN_EGG);
//                                                    entries.accept(TCOTS_Items.ROCK_TROLL_SPAWN_EGG);
//                                                    entries.accept(TCOTS_Items.ICE_TROLL_SPAWN_EGG);
//                                                    entries.accept(TCOTS_Items.FOREST_TROLL_SPAWN_EGG);
//                                                    entries.accept(TCOTS_Items.ICE_GIANT_SPAWN_EGG);
//
//                                                    entries.accept(TCOTS_Items.GIANT_ANCHOR_BLOCK_ITEM);
//                                                    entries.accept(TCOTS_Items.GIANT_ANCHOR);
//                                                }
//
//                                                //Other Ingredients
//                                                {
//                                                    entries.accept(TCOTS_Items.CURED_MONSTER_LEATHER);
//                                                }
//
//                                                //Weapons
//                                                {
//                                                    entries.accept(TCOTS_Items.GVALCHIR);
//                                                    entries.accept(TCOTS_Items.MOONBLADE);
//                                                    entries.accept(TCOTS_Items.DYAEBL);
//                                                    entries.accept(TCOTS_Items.ARDAENYE);
//                                                    entries.accept(TCOTS_Items.WINTERS_BLADE);
//
//
//                                                    entries.accept(TCOTS_Items.KNIGHT_CROSSBOW);
//                                                    entries.accept(TCOTS_Items.BASE_BOLT);
//                                                    entries.accept(TCOTS_Items.BLUNT_BOLT);
//                                                    entries.accept(TCOTS_Items.PRECISION_BOLT);
//                                                    entries.accept(TCOTS_Items.EXPLODING_BOLT);
//                                                    entries.accept(TCOTS_Items.BROADHEAD_BOLT);
//                                                }
//
//                                                //Armors
//                                                {
//                                                    entries.accept(TCOTS_Items.WARRIORS_LEATHER_JACKET);
//                                                    entries.accept(TCOTS_Items.WARRIORS_LEATHER_TROUSERS);
//                                                    entries.accept(TCOTS_Items.WARRIORS_LEATHER_BOOTS);
//
//                                                    entries.accept(TCOTS_Items.MANTICORE_ARMOR);
//                                                    entries.accept(TCOTS_Items.MANTICORE_TROUSERS);
//                                                    entries.accept(TCOTS_Items.MANTICORE_BOOTS);
//
//                                                    entries.accept(TCOTS_Items.RAVENS_ARMOR);
//                                                    entries.accept(TCOTS_Items.RAVENS_TROUSERS);
//                                                    entries.accept(TCOTS_Items.RAVENS_BOOTS);
//
//                                                    //Horse Armors
//                                                    entries.accept(TCOTS_Items.TUNDRA_HORSE_ARMOR);
//                                                    entries.accept(TCOTS_Items.KNIGHT_ERRANTS_HORSE_ARMOR);
//                                                }
//
//                                                //Misc/Blocks
//                                                {
//                                                    entries.accept(TCOTS_Items.NEST_SLAB_ITEM);
//                                                    entries.accept(TCOTS_Items.NEST_SKULL_ITEM);
//                                                    entries.accept(TCOTS_Items.MONSTER_NEST_ITEM);
//                                                    entries.accept(TCOTS_Items.WINTERS_BLADE_SKELETON_ITEM);
//                                                    entries.accept(TCOTS_Items.SKELETON_BLOCK_ITEM);
//                                                }
//
//                                            },
//                                            true);
//
//                                    owoItemGroup.addCustomTab(Icon.of(TCOTS_Items.ALCHEMY_BOOK), "alchemy", (context, entries) ->
//                                            {
//                                                //Book
//                                                entries.accept(TCOTS_Items.ALCHEMY_BOOK);
//
//                                                //Alchemy
//                                                {
//                                                    //Ingredients
//                                                    {
//                                                        entries.accept(TCOTS_Items.ALCHEMY_TABLE_ITEM);
//                                                        entries.accept(TCOTS_Items.HERBAL_TABLE_ITEM);
//                                                        entries.accept(TCOTS_Items.HERBAL_MIXTURE);
//
//                                                        entries.accept(TCOTS_Items.ALLSPICE);
//                                                        entries.accept(TCOTS_Items.ARENARIA);
//                                                        entries.accept(TCOTS_Items.CELANDINE);
//                                                        entries.accept(TCOTS_Items.BRYONIA);
//                                                        entries.accept(TCOTS_Items.CROWS_EYE);
//                                                        entries.accept(TCOTS_Items.VERBENA);
//                                                        entries.accept(TCOTS_Items.HAN_FIBER);
//                                                        entries.accept(TCOTS_Items.PUFFBALL);
//                                                        entries.accept(TCOTS_Items.SEWANT_MUSHROOMS);
//
//                                                        entries.accept(TCOTS_Items.ERGOT_SEEDS);
//
//                                                        entries.accept(TCOTS_Items.PUFFBALL_MUSHROOM_BLOCK_ITEM);
//                                                        entries.accept(TCOTS_Items.SEWANT_MUSHROOM_BLOCK_ITEM);
//                                                        entries.accept(TCOTS_Items.SEWANT_MUSHROOM_STEM_ITEM);
//
//
//                                                        entries.accept(TCOTS_Items.ICY_SPIRIT.getDefaultInstance());
//                                                        entries.accept(TCOTS_Items.CHERRY_CORDIAL.getDefaultInstance());
//                                                        entries.accept(TCOTS_Items.VILLAGE_HERBAL.getDefaultInstance());
//                                                        entries.accept(TCOTS_Items.MANDRAKE_CORDIAL.getDefaultInstance());
//                                                        entries.accept(TCOTS_Items.DWARVEN_SPIRIT.getDefaultInstance());
//                                                        entries.accept(TCOTS_Items.ALCOHEST.getDefaultInstance());
//                                                        entries.accept(TCOTS_Items.WHITE_GULL.getDefaultInstance());
//
//                                                        entries.accept(TCOTS_Items.MONSTER_FAT);
//                                                        entries.accept(TCOTS_Items.ALCHEMY_PASTE);
//                                                        entries.accept(TCOTS_Items.STAMMELFORDS_DUST);
//                                                        entries.accept(TCOTS_Items.ALCHEMISTS_POWDER);
//
//                                                        entries.accept(TCOTS_Items.AETHER);
//                                                        entries.accept(TCOTS_Items.HYDRAGENUM);
//                                                        entries.accept(TCOTS_Items.NIGREDO);
//                                                        entries.accept(TCOTS_Items.QUEBRITH);
//                                                        entries.accept(TCOTS_Items.REBIS);
//                                                        entries.accept(TCOTS_Items.RUBEDO);
//                                                        entries.accept(TCOTS_Items.VERMILION);
//                                                        entries.accept(TCOTS_Items.VITRIOL);
//                                                    }
//
//                                                    //Drops
//                                                    {
//                                                        //Necrophages
//                                                        entries.accept(TCOTS_Items.DROWNER_TONGUE);
//                                                        entries.accept(TCOTS_Items.DROWNER_BRAIN);
//                                                        entries.accept(TCOTS_Items.GHOUL_BLOOD);
//                                                        entries.accept(TCOTS_Items.ALGHOUL_BONE_MARROW);
//                                                        entries.accept(TCOTS_Items.ROTFIEND_BLOOD);
//                                                        entries.accept(TCOTS_Items.FOGLET_TEETH);
//                                                        entries.accept(TCOTS_Items.WATER_ESSENCE);
//                                                        entries.accept(TCOTS_Items.WATER_HAG_MUD_BALL);
//                                                        entries.accept(TCOTS_Items.SCURVER_SPINE);
//                                                        entries.accept(TCOTS_Items.DEVOURER_TEETH);
//                                                        entries.accept(TCOTS_Items.CADAVERINE);
//                                                        entries.accept(TCOTS_Items.GRAVEIR_BONE);
//                                                        entries.accept(TCOTS_Items.BULLVORE_HORN_FRAGMENT);
//
//                                                        //Ogroids
//                                                        entries.accept(TCOTS_Items.NEKKER_EYE);
//                                                        entries.accept(TCOTS_Items.NEKKER_HEART);
//                                                        //-> Cyclops drop
//                                                        entries.accept(TCOTS_Items.CAVE_TROLL_LIVER);
//
//                                                        //Mutagens
//                                                        {
//                                                            entries.accept(TCOTS_Items.FOGLET_MUTAGEN);
//                                                            entries.accept(TCOTS_Items.WATER_HAG_MUTAGEN);
//                                                            entries.accept(TCOTS_Items.GRAVE_HAG_MUTAGEN);
//                                                            entries.accept(TCOTS_Items.NEKKER_WARRIOR_MUTAGEN);
//                                                            entries.accept(TCOTS_Items.TROLL_MUTAGEN);
//                                                        }
//                                                    }
//
//                                                    //Potions
//                                                    {
//                                                        entries.accept(TCOTS_Items.SWALLOW_POTION.getDefaultInstance());
//                                                        entries.accept(TCOTS_Items.SWALLOW_POTION_ENHANCED.getDefaultInstance());
//                                                        entries.accept(TCOTS_Items.SWALLOW_POTION_SUPERIOR.getDefaultInstance());
//                                                        entries.accept(TCOTS_Items.SWALLOW_SPLASH.getDefaultInstance());
//
//                                                        entries.accept(TCOTS_Items.CAT_POTION.getDefaultInstance());
//                                                        entries.accept(TCOTS_Items.CAT_POTION_ENHANCED.getDefaultInstance());
//                                                        entries.accept(TCOTS_Items.CAT_POTION_SUPERIOR.getDefaultInstance());
//
//                                                        entries.accept(TCOTS_Items.WHITE_RAFFARDS_DECOCTION.getDefaultInstance());
//                                                        entries.accept(TCOTS_Items.WHITE_RAFFARDS_DECOCTION_ENHANCED.getDefaultInstance());
//                                                        entries.accept(TCOTS_Items.WHITE_RAFFARDS_DECOCTION_SUPERIOR.getDefaultInstance());
//                                                        entries.accept(TCOTS_Items.WHITE_RAFFARDS_DECOCTION_SPLASH.getDefaultInstance());
//
//                                                        entries.accept(TCOTS_Items.KILLER_WHALE_POTION.getDefaultInstance());
//                                                        entries.accept(TCOTS_Items.KILLER_WHALE_SPLASH.getDefaultInstance());
//
//                                                        entries.accept(TCOTS_Items.BLACK_BLOOD_POTION.getDefaultInstance());
//                                                        entries.accept(TCOTS_Items.BLACK_BLOOD_POTION_ENHANCED.getDefaultInstance());
//                                                        entries.accept(TCOTS_Items.BLACK_BLOOD_POTION_SUPERIOR.getDefaultInstance());
//
//                                                        entries.accept(TCOTS_Items.MARIBOR_FOREST_POTION.getDefaultInstance());
//                                                        entries.accept(TCOTS_Items.MARIBOR_FOREST_POTION_ENHANCED.getDefaultInstance());
//                                                        entries.accept(TCOTS_Items.MARIBOR_FOREST_POTION_SUPERIOR.getDefaultInstance());
//
//                                                        //W1
//                                                        entries.accept(TCOTS_Items.WOLF_POTION.getDefaultInstance());
//                                                        entries.accept(TCOTS_Items.WOLF_POTION_ENHANCED.getDefaultInstance());
//                                                        entries.accept(TCOTS_Items.WOLF_POTION_SUPERIOR.getDefaultInstance());
//
//
//                                                        //W2
//                                                        entries.accept(TCOTS_Items.ROOK_POTION.getDefaultInstance());
//                                                        entries.accept(TCOTS_Items.ROOK_POTION_ENHANCED.getDefaultInstance());
//                                                        entries.accept(TCOTS_Items.ROOK_POTION_SUPERIOR.getDefaultInstance());
//
//
//                                                        entries.accept(TCOTS_Items.WHITE_HONEY_POTION.getDefaultInstance());
//                                                        entries.accept(TCOTS_Items.WHITE_HONEY_POTION_ENHANCED.getDefaultInstance());
//                                                        entries.accept(TCOTS_Items.WHITE_HONEY_POTION_SUPERIOR.getDefaultInstance());
//
//                                                        //Decoctions
//                                                        {
//                                                            entries.accept(TCOTS_Items.WATER_HAG_DECOCTION);
//                                                            entries.accept(TCOTS_Items.GRAVE_HAG_DECOCTION);
//                                                            entries.accept(TCOTS_Items.ALGHOUL_DECOCTION);
//                                                            entries.accept(TCOTS_Items.FOGLET_DECOCTION);
//                                                            entries.accept(TCOTS_Items.NEKKER_WARRIOR_DECOCTION);
//                                                            entries.accept(TCOTS_Items.TROLL_DECOCTION);
//                                                        }
//                                                    }
//
//                                                    //Bombs
//                                                    {
//                                                        entries.accept(TCOTS_Items.GRAPESHOT);
//                                                        entries.accept(TCOTS_Items.GRAPESHOT_ENHANCED);
//                                                        entries.accept(TCOTS_Items.GRAPESHOT_SUPERIOR);
//
//                                                        entries.accept(TCOTS_Items.SAMUM);
//                                                        entries.accept(TCOTS_Items.SAMUM_ENHANCED);
//                                                        entries.accept(TCOTS_Items.SAMUM_SUPERIOR);
//
//                                                        entries.accept(TCOTS_Items.DANCING_STAR);
//                                                        entries.accept(TCOTS_Items.DANCING_STAR_ENHANCED);
//                                                        entries.accept(TCOTS_Items.DANCING_STAR_SUPERIOR);
//
//                                                        entries.accept(TCOTS_Items.DEVILS_PUFFBALL);
//                                                        entries.accept(TCOTS_Items.DEVILS_PUFFBALL_ENHANCED);
//                                                        entries.accept(TCOTS_Items.DEVILS_PUFFBALL_SUPERIOR);
//
//                                                        entries.accept(TCOTS_Items.DRAGONS_DREAM);
//                                                        entries.accept(TCOTS_Items.DRAGONS_DREAM_ENHANCED);
//                                                        entries.accept(TCOTS_Items.DRAGONS_DREAM_SUPERIOR);
//
//                                                        entries.accept(TCOTS_Items.NORTHERN_WIND);
//                                                        entries.accept(TCOTS_Items.NORTHERN_WIND_ENHANCED);
//                                                        entries.accept(TCOTS_Items.NORTHERN_WIND_SUPERIOR);
//
//                                                        entries.accept(TCOTS_Items.DIMERITIUM_BOMB);
//                                                        entries.accept(TCOTS_Items.DIMERITIUM_BOMB_ENHANCED);
//                                                        entries.accept(TCOTS_Items.DIMERITIUM_BOMB_SUPERIOR);
//
//                                                        entries.accept(TCOTS_Items.MOON_DUST);
//                                                        entries.accept(TCOTS_Items.MOON_DUST_ENHANCED);
//                                                        entries.accept(TCOTS_Items.MOON_DUST_SUPERIOR);
//                                                    }
//
//                                                    //Monster Oils
//                                                    {
//                                                        entries.accept(TCOTS_Items.NECROPHAGE_OIL);
//                                                        entries.accept(TCOTS_Items.ENHANCED_NECROPHAGE_OIL);
//                                                        entries.accept(TCOTS_Items.SUPERIOR_NECROPHAGE_OIL);
//
//                                                        entries.accept(TCOTS_Items.OGROID_OIL);
//                                                        entries.accept(TCOTS_Items.ENHANCED_OGROID_OIL);
//                                                        entries.accept(TCOTS_Items.SUPERIOR_OGROID_OIL);
//
//                                                        entries.accept(TCOTS_Items.BEAST_OIL);
//                                                        entries.accept(TCOTS_Items.ENHANCED_BEAST_OIL);
//                                                        entries.accept(TCOTS_Items.SUPERIOR_BEAST_OIL);
//
//                                                        entries.accept(TCOTS_Items.HANGED_OIL);
//                                                        entries.accept(TCOTS_Items.ENHANCED_HANGED_OIL);
//                                                        entries.accept(TCOTS_Items.SUPERIOR_HANGED_OIL);
//                                                    }
//                                                }
//                                            },
//                                            true);
//
//                                    owoItemGroup.addCustomTab(Icon.of(TCOTS_Items.ALCHEMY_FORMULA), "formulae", (context, entries) ->
//                                            {
//                                                //Formulae
//                                                addFormulaeEntries(entries);
//                                            },
//                                            true);
//
//                                    owoItemGroup.addButton(ItemGroupButton.curseforge(owoItemGroup, "https://www.curseforge.com/minecraft/mc-mods/the-conjunction-of-the-spheres"));
//                                    owoItemGroup.addButton(ItemGroupButton.modrinth(owoItemGroup, "https://modrinth.com/mod/the-conjunction-of-the-spheres"));
//                                    owoItemGroup.addButton(ItemGroupButton.github(owoItemGroup, "https://github.com/AngelDGr/The-Conjunction-of-the-Spheres"));
//                                })
//                        .build();
//
//        owoItemGroup.initialize();
//    }

    private static void addFormulaeEntries(CreativeModeTab.Output entries){
        List<Item> listPotions = new ArrayList<>();
        List<Item> listBombs = new ArrayList<>();
        List<Item> listOils = new ArrayList<>();

        List<Item> listMisc = new ArrayList<>();

        BuiltInRegistries.ITEM.forEach(item ->
                {
                    if(item instanceof WitcherPotions_Base && !(item instanceof WitcherAlcohol_Base))
                        listPotions.add(item);

                    if(item instanceof WitcherBombs_Base) listBombs.add(item);

                    if(item instanceof WitcherMonsterOil_Base) listOils.add(item);

                    if(AlchemyFormulaUtil.isMiscItem(item))
                        listMisc.add(item);
                }
                );


        for(Item potion: listPotions){
            if(potion instanceof WitcherPotions_Base witcherPotion && !(potion instanceof WitcherPotionsSplash_Base))
                entries.accept(AlchemyFormulaUtil.setFormula(BuiltInRegistries.ITEM.getKey(potion), witcherPotion.isDecoction()), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);

            if(potion instanceof WitcherPotionsSplash_Base)
                entries.accept(AlchemyFormulaUtil.setFormula(BuiltInRegistries.ITEM.getKey(potion)), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        }

        for(Item bomb: listBombs){
            entries.accept(AlchemyFormulaUtil.setFormula(BuiltInRegistries.ITEM.getKey(bomb)), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        }

        for(Item oil: listOils){
            entries.accept(AlchemyFormulaUtil.setFormula(BuiltInRegistries.ITEM.getKey(oil)), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        }

        for(Item misc: listMisc){
            entries.accept(AlchemyFormulaUtil.setFormula(BuiltInRegistries.ITEM.getKey(misc)), CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
        }

    }

}
