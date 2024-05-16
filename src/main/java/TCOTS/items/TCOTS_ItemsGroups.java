package TCOTS.items;
import TCOTS.TCOTS_Main;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

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
                            entries.add(TCOTS_Items.WITCHER_BESTIARY);
                            entries.add(TCOTS_Items.ALCHEMY_BOOK);

                            //Spawn Eggs
                            entries.add(TCOTS_Items.DROWNER_SPAWN_EGG);
                            entries.add(TCOTS_Items.GHOUL_SPAWN_EGG);
                            entries.add(TCOTS_Items.ALGHOUL_SPAWN_EGG);
                            entries.add(TCOTS_Items.ROTFIEND_SPAWN_EGG);
                            entries.add(TCOTS_Items.NEKKER_SPAWN_EGG);
                            entries.add(TCOTS_Items.GRAVE_HAG_SPAWN_EGG);
                            entries.add(TCOTS_Items.WATER_HAG_SPAWN_EGG);
                            entries.add(TCOTS_Items.FOGLET_SPAWN_EGG);


                            //Drops
                            entries.add(TCOTS_Items.DROWNER_TONGUE);
                            entries.add(TCOTS_Items.DROWNER_BRAIN);
                            entries.add(TCOTS_Items.GHOUL_BLOOD);
                            entries.add(TCOTS_Items.ALGHOUL_BONE_MARROW);
                            entries.add(TCOTS_Items.ROTFIEND_BLOOD);
                            entries.add(TCOTS_Items.NEKKER_EYE);
                            entries.add(TCOTS_Items.NEKKER_HEART);
                            entries.add(TCOTS_Items.WATER_HAG_MUD_BALL);
                            entries.add(TCOTS_Items.WATER_ESSENCE);
                            entries.add(TCOTS_Items.FOGLET_TEETH);

                            //Mutagens
                            entries.add(TCOTS_Items.GRAVE_HAG_MUTAGEN);
                            entries.add(TCOTS_Items.WATER_HAG_MUTAGEN);
                            entries.add(TCOTS_Items.FOGLET_MUTAGEN);

                            //Potions
                            //Ingredients
                            entries.add(TCOTS_Items.ALCHEMY_TABLE_ITEM);
                            entries.add(TCOTS_Items.HERBAL_TABLE_ITEM);
                            entries.add(TCOTS_Items.ORGANIC_PASTE);

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
                            entries.add(TCOTS_Items.LILY_OF_THE_VALLEY_PETALS);
                            entries.add(TCOTS_Items.ALLIUM_PETALS);
                            entries.add(TCOTS_Items.POPPY_PETALS);
                            entries.add(TCOTS_Items.PEONY_PETALS);
                            entries.add(TCOTS_Items.AZURE_BLUET_PETALS);
                            entries.add(TCOTS_Items.OXEYE_DAISY_PETALS);
                            entries.add(TCOTS_Items.BUNCH_OF_LEAVES);

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
                            entries.add(TCOTS_Items.AETHER);
                            entries.add(TCOTS_Items.VITRIOL);
                            entries.add(TCOTS_Items.VERMILION);
                            entries.add(TCOTS_Items.HYDRAGENUM);
                            entries.add(TCOTS_Items.QUEBRITH);
                            entries.add(TCOTS_Items.RUBEDO);
                            entries.add(TCOTS_Items.REBIS);
                            entries.add(TCOTS_Items.NIGREDO);
                            entries.add(TCOTS_Items.ALCHEMY_PASTE);
                            entries.add(TCOTS_Items.MONSTER_FAT);
                            entries.add(TCOTS_Items.STAMMELFORDS_DUST);
                            entries.add(TCOTS_Items.ALCHEMISTS_POWDER);


                            entries.add(TCOTS_Items.SWALLOW_POTION.getDefaultStack());
                            entries.add(TCOTS_Items.SWALLOW_POTION_ENHANCED.getDefaultStack());
                            entries.add(TCOTS_Items.SWALLOW_POTION_SUPERIOR.getDefaultStack());

                            entries.add(TCOTS_Items.WHITE_RAFFARDS_DECOCTION.getDefaultStack());
                            entries.add(TCOTS_Items.WHITE_RAFFARDS_DECOCTION_ENHANCED.getDefaultStack());
                            entries.add(TCOTS_Items.WHITE_RAFFARDS_DECOCTION_SUPERIOR.getDefaultStack());

                            entries.add(TCOTS_Items.KILLER_WHALE_POTION.getDefaultStack());

                            entries.add(TCOTS_Items.CAT_POTION.getDefaultStack());
                            entries.add(TCOTS_Items.CAT_POTION_ENHANCED.getDefaultStack());
                            entries.add(TCOTS_Items.CAT_POTION_SUPERIOR.getDefaultStack());

                            entries.add(TCOTS_Items.BLACK_BLOOD_POTION.getDefaultStack());
                            entries.add(TCOTS_Items.BLACK_BLOOD_POTION_ENHANCED.getDefaultStack());
                            entries.add(TCOTS_Items.BLACK_BLOOD_POTION_SUPERIOR.getDefaultStack());

                            entries.add(TCOTS_Items.MARIBOR_FOREST_POTION.getDefaultStack());
                            entries.add(TCOTS_Items.MARIBOR_FOREST_POTION_ENHANCED.getDefaultStack());
                            entries.add(TCOTS_Items.MARIBOR_FOREST_POTION_SUPERIOR.getDefaultStack());

                            entries.add(TCOTS_Items.WHITE_HONEY_POTION.getDefaultStack());
                            entries.add(TCOTS_Items.WHITE_HONEY_POTION_ENHANCED.getDefaultStack());
                            entries.add(TCOTS_Items.WHITE_HONEY_POTION_SUPERIOR.getDefaultStack());

                            entries.add(TCOTS_Items.SWALLOW_SPLASH.getDefaultStack());
                            entries.add(TCOTS_Items.WHITE_RAFFARDS_DECOCTION_SPLASH.getDefaultStack());
                            entries.add(TCOTS_Items.KILLER_WHALE_SPLASH.getDefaultStack());


                            //Monster Oils
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


                            //Decoctions
                            entries.add(TCOTS_Items.GRAVE_HAG_DECOCTION);
                            entries.add(TCOTS_Items.WATER_HAG_DECOCTION);
                            entries.add(TCOTS_Items.ALGHOUL_DECOCTION);
                            entries.add(TCOTS_Items.FOGLET_DECOCTION);

                            //Bombs
                            entries.add(TCOTS_Items.GRAPESHOT);
                            entries.add(TCOTS_Items.ENHANCED_GRAPESHOT);
                            entries.add(TCOTS_Items.SUPERIOR_GRAPESHOT);
                            entries.add(TCOTS_Items.DANCING_STAR);
                            entries.add(TCOTS_Items.DANCING_STAR_ENHANCED);
                            entries.add(TCOTS_Items.DANCING_STAR_SUPERIOR);
                            entries.add(TCOTS_Items.DRAGONS_DREAM);
                            entries.add(TCOTS_Items.DRAGONS_DREAM_ENHANCED);
                            entries.add(TCOTS_Items.DRAGONS_DREAM_SUPERIOR);
                            entries.add(TCOTS_Items.DEVILS_PUFFBALL);
                            entries.add(TCOTS_Items.DEVILS_PUFFBALL_ENHANCED);
                            entries.add(TCOTS_Items.DEVILS_PUFFBALL_SUPERIOR);
                            entries.add(TCOTS_Items.SAMUM);
                            entries.add(TCOTS_Items.SAMUM_ENHANCED);
                            entries.add(TCOTS_Items.SAMUM_SUPERIOR);
                            entries.add(TCOTS_Items.NORTHERN_WIND);
                            entries.add(TCOTS_Items.NORTHERN_WIND_ENHANCED);
                            entries.add(TCOTS_Items.NORTHERN_WIND_SUPERIOR);
                            entries.add(TCOTS_Items.DIMERITIUM_BOMB);
                            entries.add(TCOTS_Items.DIMERITIUM_BOMB_ENHANCED);
                            entries.add(TCOTS_Items.DIMERITIUM_BOMB_SUPERIOR);
                            entries.add(TCOTS_Items.MOON_DUST);
                            entries.add(TCOTS_Items.MOON_DUST_ENHANCED);
                            entries.add(TCOTS_Items.MOON_DUST_SUPERIOR);

                            //Misc/Blocks
                            entries.add(TCOTS_Items.NEST_SLAB_ITEM);
                            entries.add(TCOTS_Items.NEST_SKULL_ITEM);
                            entries.add(TCOTS_Items.MONSTER_NEST_ITEM);


                        }).build());
    }

}
