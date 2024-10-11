package TCOTS.items;

import TCOTS.TCOTS_Main;
import TCOTS.items.concoctions.*;
import TCOTS.utils.AlchemyFormulaUtil;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class TCOTS_ItemsGroups {


    //ItemGroups
    public static ItemGroup TheConjunctionOfTheSpheres;
    public static ItemGroup TheConjunctionOfTheSpheres_Alchemy;

    public static void registerGroupItems() {

        TheConjunctionOfTheSpheres = Registry.register(Registries.ITEM_GROUP,
                new Identifier(TCOTS_Main.MOD_ID, "tcots-witcher"),
                FabricItemGroup.builder().displayName(Text.translatable("itemgroup.tcots-witcher"))
                        .icon(() -> new ItemStack(TCOTS_Items.WITCHER_BESTIARY))
                        .entries((displayContext, entries) -> {
                            //Books
                            {
                                entries.add(TCOTS_Items.WITCHER_BESTIARY);
                                entries.add(TCOTS_Items.ALCHEMY_BOOK);
                            }

                            //Spawn Eggs
                            {
                                entries.add(TCOTS_Items.DROWNER_SPAWN_EGG);
                                entries.add(TCOTS_Items.GHOUL_SPAWN_EGG);
                                entries.add(TCOTS_Items.ALGHOUL_SPAWN_EGG);
                                entries.add(TCOTS_Items.ROTFIEND_SPAWN_EGG);
                                entries.add(TCOTS_Items.FOGLET_SPAWN_EGG);
                                entries.add(TCOTS_Items.WATER_HAG_SPAWN_EGG);
                                entries.add(TCOTS_Items.GRAVE_HAG_SPAWN_EGG);
                                entries.add(TCOTS_Items.SCURVER_SPAWN_EGG);
                                entries.add(TCOTS_Items.DEVOURER_SPAWN_EGG);
                                entries.add(TCOTS_Items.GRAVEIR_SPAWN_EGG);
                                entries.add(TCOTS_Items.BULLVORE_SPAWN_EGG);

                                entries.add(TCOTS_Items.NEKKER_SPAWN_EGG);
                                entries.add(TCOTS_Items.NEKKER_WARRIOR_SPAWN_EGG);
                                entries.add(TCOTS_Items.CYCLOPS_SPAWN_EGG);
                                entries.add(TCOTS_Items.ROCK_TROLL_SPAWN_EGG);
                                entries.add(TCOTS_Items.ICE_TROLL_SPAWN_EGG);
                                entries.add(TCOTS_Items.FOREST_TROLL_SPAWN_EGG);
                            }


                            //Drops
                            {
                                //Necrophages
                                entries.add(TCOTS_Items.DROWNER_TONGUE);
                                entries.add(TCOTS_Items.DROWNER_BRAIN);
                                entries.add(TCOTS_Items.GHOUL_BLOOD);
                                entries.add(TCOTS_Items.ALGHOUL_BONE_MARROW);
                                entries.add(TCOTS_Items.ROTFIEND_BLOOD);
                                entries.add(TCOTS_Items.FOGLET_TEETH);
                                entries.add(TCOTS_Items.WATER_ESSENCE);
                                entries.add(TCOTS_Items.WATER_HAG_MUD_BALL);
                                entries.add(TCOTS_Items.SCURVER_SPINE);
                                entries.add(TCOTS_Items.DEVOURER_TEETH);
                                entries.add(TCOTS_Items.CADAVERINE);
                                entries.add(TCOTS_Items.GRAVEIR_BONE);
                                entries.add(TCOTS_Items.BULLVORE_HORN_FRAGMENT);

                                //Ogroids
                                entries.add(TCOTS_Items.NEKKER_EYE);
                                entries.add(TCOTS_Items.NEKKER_HEART);
                                //-> Cyclops drop
                                entries.add(TCOTS_Items.CAVE_TROLL_LIVER);

                                //Mutagens
                                {
                                    entries.add(TCOTS_Items.FOGLET_MUTAGEN);
                                    entries.add(TCOTS_Items.WATER_HAG_MUTAGEN);
                                    entries.add(TCOTS_Items.GRAVE_HAG_MUTAGEN);
                                    entries.add(TCOTS_Items.NEKKER_WARRIOR_MUTAGEN);
                                    entries.add(TCOTS_Items.TROLL_MUTAGEN);
                                }
                            }

                            //Other Ingredients
                            {
                                entries.add(TCOTS_Items.CURED_MONSTER_LEATHER);
                            }


                            //Weapons
                            {
                                entries.add(TCOTS_Items.GVALCHIR);
                                entries.add(TCOTS_Items.MOONBLADE);
                                entries.add(TCOTS_Items.DYAEBL);
                                entries.add(TCOTS_Items.WINTERS_BLADE);
                                entries.add(TCOTS_Items.ARDAENYE);


                                entries.add(TCOTS_Items.KNIGHT_CROSSBOW);
                                entries.add(TCOTS_Items.BASE_BOLT);
                                entries.add(TCOTS_Items.BLUNT_BOLT);
                                entries.add(TCOTS_Items.PRECISION_BOLT);
                                entries.add(TCOTS_Items.EXPLODING_BOLT);
                                entries.add(TCOTS_Items.BROADHEAD_BOLT);
                            }

                            //Armors
                            {
                                entries.add(TCOTS_Items.WARRIORS_LEATHER_JACKET);
                                entries.add(TCOTS_Items.WARRIORS_LEATHER_TROUSERS);
                                entries.add(TCOTS_Items.WARRIORS_LEATHER_BOOTS);

                                entries.add(TCOTS_Items.MANTICORE_ARMOR);
                                entries.add(TCOTS_Items.MANTICORE_TROUSERS);
                                entries.add(TCOTS_Items.MANTICORE_BOOTS);

                                entries.add(TCOTS_Items.RAVENS_ARMOR);
                                entries.add(TCOTS_Items.RAVENS_TROUSERS);
                                entries.add(TCOTS_Items.RAVENS_BOOTS);
                            }


                            //Concoctions
                            {
                                //Ingredients
                                {
                                    entries.add(TCOTS_Items.ALCHEMY_TABLE_ITEM);
                                    entries.add(TCOTS_Items.HERBAL_TABLE_ITEM);
                                    entries.add(TCOTS_Items.HERBAL_MIXTURE);

                                    entries.add(TCOTS_Items.ALLSPICE);
                                    entries.add(TCOTS_Items.ARENARIA);
                                    entries.add(TCOTS_Items.CELANDINE);
                                    entries.add(TCOTS_Items.BRYONIA);
                                    entries.add(TCOTS_Items.CROWS_EYE);
                                    entries.add(TCOTS_Items.VERBENA);
                                    entries.add(TCOTS_Items.HAN_FIBER);
                                    entries.add(TCOTS_Items.PUFFBALL);
                                    entries.add(TCOTS_Items.SEWANT_MUSHROOMS);

                                    entries.add(TCOTS_Items.ERGOT_SEEDS);

                                    entries.add(TCOTS_Items.PUFFBALL_MUSHROOM_BLOCK_ITEM);
                                    entries.add(TCOTS_Items.SEWANT_MUSHROOM_BLOCK_ITEM);
                                    entries.add(TCOTS_Items.SEWANT_MUSHROOM_STEM_ITEM);


                                    entries.add(TCOTS_Items.ICY_SPIRIT.getDefaultStack());
                                    entries.add(TCOTS_Items.CHERRY_CORDIAL.getDefaultStack());
                                    entries.add(TCOTS_Items.VILLAGE_HERBAL.getDefaultStack());
                                    entries.add(TCOTS_Items.MANDRAKE_CORDIAL.getDefaultStack());
                                    entries.add(TCOTS_Items.DWARVEN_SPIRIT.getDefaultStack());
                                    entries.add(TCOTS_Items.ALCOHEST.getDefaultStack());
                                    entries.add(TCOTS_Items.WHITE_GULL.getDefaultStack());

                                    entries.add(TCOTS_Items.MONSTER_FAT);
                                    entries.add(TCOTS_Items.ALCHEMY_PASTE);
                                    entries.add(TCOTS_Items.STAMMELFORDS_DUST);
                                    entries.add(TCOTS_Items.ALCHEMISTS_POWDER);

                                    entries.add(TCOTS_Items.AETHER);
                                    entries.add(TCOTS_Items.HYDRAGENUM);
                                    entries.add(TCOTS_Items.NIGREDO);
                                    entries.add(TCOTS_Items.QUEBRITH);
                                    entries.add(TCOTS_Items.REBIS);
                                    entries.add(TCOTS_Items.RUBEDO);
                                    entries.add(TCOTS_Items.VERMILION);
                                    entries.add(TCOTS_Items.VITRIOL);

                                }

                                //Potions
                                {
                                    entries.add(TCOTS_Items.SWALLOW_POTION.getDefaultStack());
                                    entries.add(TCOTS_Items.SWALLOW_POTION_ENHANCED.getDefaultStack());
                                    entries.add(TCOTS_Items.SWALLOW_POTION_SUPERIOR.getDefaultStack());
                                    entries.add(TCOTS_Items.SWALLOW_SPLASH.getDefaultStack());

                                    entries.add(TCOTS_Items.CAT_POTION.getDefaultStack());
                                    entries.add(TCOTS_Items.CAT_POTION_ENHANCED.getDefaultStack());
                                    entries.add(TCOTS_Items.CAT_POTION_SUPERIOR.getDefaultStack());

                                    entries.add(TCOTS_Items.WHITE_RAFFARDS_DECOCTION.getDefaultStack());
                                    entries.add(TCOTS_Items.WHITE_RAFFARDS_DECOCTION_ENHANCED.getDefaultStack());
                                    entries.add(TCOTS_Items.WHITE_RAFFARDS_DECOCTION_SUPERIOR.getDefaultStack());
                                    entries.add(TCOTS_Items.WHITE_RAFFARDS_DECOCTION_SPLASH.getDefaultStack());

                                    entries.add(TCOTS_Items.KILLER_WHALE_POTION.getDefaultStack());
                                    entries.add(TCOTS_Items.KILLER_WHALE_SPLASH.getDefaultStack());

                                    entries.add(TCOTS_Items.BLACK_BLOOD_POTION.getDefaultStack());
                                    entries.add(TCOTS_Items.BLACK_BLOOD_POTION_ENHANCED.getDefaultStack());
                                    entries.add(TCOTS_Items.BLACK_BLOOD_POTION_SUPERIOR.getDefaultStack());

                                    entries.add(TCOTS_Items.MARIBOR_FOREST_POTION.getDefaultStack());
                                    entries.add(TCOTS_Items.MARIBOR_FOREST_POTION_ENHANCED.getDefaultStack());
                                    entries.add(TCOTS_Items.MARIBOR_FOREST_POTION_SUPERIOR.getDefaultStack());

                                    //W1
                                    entries.add(TCOTS_Items.WOLF_POTION.getDefaultStack());
                                    entries.add(TCOTS_Items.WOLF_POTION_ENHANCED.getDefaultStack());
                                    entries.add(TCOTS_Items.WOLF_POTION_SUPERIOR.getDefaultStack());


                                    //W2
                                    entries.add(TCOTS_Items.ROOK_POTION.getDefaultStack());
                                    entries.add(TCOTS_Items.ROOK_POTION_ENHANCED.getDefaultStack());
                                    entries.add(TCOTS_Items.ROOK_POTION_SUPERIOR.getDefaultStack());


                                    entries.add(TCOTS_Items.WHITE_HONEY_POTION.getDefaultStack());
                                    entries.add(TCOTS_Items.WHITE_HONEY_POTION_ENHANCED.getDefaultStack());
                                    entries.add(TCOTS_Items.WHITE_HONEY_POTION_SUPERIOR.getDefaultStack());

                                }

                                //Bombs
                                {
                                    entries.add(TCOTS_Items.GRAPESHOT);
                                    entries.add(TCOTS_Items.GRAPESHOT_ENHANCED);
                                    entries.add(TCOTS_Items.GRAPESHOT_SUPERIOR);

                                    entries.add(TCOTS_Items.SAMUM);
                                    entries.add(TCOTS_Items.SAMUM_ENHANCED);
                                    entries.add(TCOTS_Items.SAMUM_SUPERIOR);

                                    entries.add(TCOTS_Items.DANCING_STAR);
                                    entries.add(TCOTS_Items.DANCING_STAR_ENHANCED);
                                    entries.add(TCOTS_Items.DANCING_STAR_SUPERIOR);

                                    entries.add(TCOTS_Items.DEVILS_PUFFBALL);
                                    entries.add(TCOTS_Items.DEVILS_PUFFBALL_ENHANCED);
                                    entries.add(TCOTS_Items.DEVILS_PUFFBALL_SUPERIOR);

                                    entries.add(TCOTS_Items.DRAGONS_DREAM);
                                    entries.add(TCOTS_Items.DRAGONS_DREAM_ENHANCED);
                                    entries.add(TCOTS_Items.DRAGONS_DREAM_SUPERIOR);

                                    entries.add(TCOTS_Items.NORTHERN_WIND);
                                    entries.add(TCOTS_Items.NORTHERN_WIND_ENHANCED);
                                    entries.add(TCOTS_Items.NORTHERN_WIND_SUPERIOR);

                                    entries.add(TCOTS_Items.DIMERITIUM_BOMB);
                                    entries.add(TCOTS_Items.DIMERITIUM_BOMB_ENHANCED);
                                    entries.add(TCOTS_Items.DIMERITIUM_BOMB_SUPERIOR);

                                    entries.add(TCOTS_Items.MOON_DUST);
                                    entries.add(TCOTS_Items.MOON_DUST_ENHANCED);
                                    entries.add(TCOTS_Items.MOON_DUST_SUPERIOR);
                                }

                                //Monster Oils
                                {
                                    entries.add(TCOTS_Items.NECROPHAGE_OIL);
                                    entries.add(TCOTS_Items.ENHANCED_NECROPHAGE_OIL);
                                    entries.add(TCOTS_Items.SUPERIOR_NECROPHAGE_OIL);

                                    entries.add(TCOTS_Items.OGROID_OIL);
                                    entries.add(TCOTS_Items.ENHANCED_OGROID_OIL);
                                    entries.add(TCOTS_Items.SUPERIOR_OGROID_OIL);

                                    entries.add(TCOTS_Items.BEAST_OIL);
                                    entries.add(TCOTS_Items.ENHANCED_BEAST_OIL);
                                    entries.add(TCOTS_Items.SUPERIOR_BEAST_OIL);

                                    entries.add(TCOTS_Items.HANGED_OIL);
                                    entries.add(TCOTS_Items.ENHANCED_HANGED_OIL);
                                    entries.add(TCOTS_Items.SUPERIOR_HANGED_OIL);
                                }

                                //Decoctions
                                {
                                    entries.add(TCOTS_Items.WATER_HAG_DECOCTION);
                                    entries.add(TCOTS_Items.GRAVE_HAG_DECOCTION);
                                    entries.add(TCOTS_Items.ALGHOUL_DECOCTION);
                                    entries.add(TCOTS_Items.FOGLET_DECOCTION);
                                    entries.add(TCOTS_Items.NEKKER_WARRIOR_DECOCTION);
                                    entries.add(TCOTS_Items.TROLL_DECOCTION);
                                }

                            }


                            //Misc/Blocks
                            {
                                entries.add(TCOTS_Items.NEST_SLAB_ITEM);
                                entries.add(TCOTS_Items.NEST_SKULL_ITEM);
                                entries.add(TCOTS_Items.MONSTER_NEST_ITEM);
                            }


                            //Formulae
                            addFormulaeEntries(entries);

                        }).build());
    }

    private static void addFormulaeEntries(ItemGroup.Entries entries){
        List<Item> listPotions = new ArrayList<>();
        List<Item> listBombs = new ArrayList<>();
        List<Item> listOils = new ArrayList<>();

        List<Item> listMisc = new ArrayList<>();

        Registries.ITEM.forEach(item ->
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
                entries.add(AlchemyFormulaUtil.setFormula(Registries.ITEM.getId(potion), witcherPotion.isDecoction()), ItemGroup.StackVisibility.PARENT_AND_SEARCH_TABS);

            if(potion instanceof WitcherPotionsSplash_Base)
                entries.add(AlchemyFormulaUtil.setFormula(Registries.ITEM.getId(potion)), ItemGroup.StackVisibility.PARENT_AND_SEARCH_TABS);
        }

        for(Item bomb: listBombs){
            entries.add(AlchemyFormulaUtil.setFormula(Registries.ITEM.getId(bomb)), ItemGroup.StackVisibility.PARENT_AND_SEARCH_TABS);
        }

        for(Item oil: listOils){
            entries.add(AlchemyFormulaUtil.setFormula(Registries.ITEM.getId(oil)), ItemGroup.StackVisibility.PARENT_AND_SEARCH_TABS);
        }

        for(Item misc: listMisc){
            entries.add(AlchemyFormulaUtil.setFormula(Registries.ITEM.getId(misc)), ItemGroup.StackVisibility.PARENT_AND_SEARCH_TABS);
        }

    }


}
