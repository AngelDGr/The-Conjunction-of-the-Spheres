package TCOTS.items;
import TCOTS.TCOTS_Main;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

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
                            entries.add(TCOTS_Items.ROTFIEND_SPAWN_EGG);
                            entries.add(TCOTS_Items.NEKKER_SPAWN_EGG);
                            entries.add(TCOTS_Items.GRAVE_HAG_SPAWN_EGG);
                            entries.add(TCOTS_Items.WATER_HAG_SPAWN_EGG);
                            entries.add(TCOTS_Items.FOGLET_SPAWN_EGG);


                            //Drops
                            entries.add(TCOTS_Items.DROWNER_TONGUE);
                            entries.add(TCOTS_Items.DROWNER_BRAIN);
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

                            //Ingredients
                            entries.add(TCOTS_Items.ALLSPICE);
                            entries.add(TCOTS_Items.ARENARIA);
                            entries.add(TCOTS_Items.CELANDINE);
                            entries.add(TCOTS_Items.BRYONIA);
                            entries.add(TCOTS_Items.ERGOT_SEEDS);
                            entries.add(TCOTS_Items.LILY_OF_THE_VALLEY_PETALS);
                            entries.add(TCOTS_Items.ALLIUM_PETALS);
                            entries.add(TCOTS_Items.POPPY_PETALS);
                            entries.add(TCOTS_Items.BUNCH_OF_LEAVES);
                            entries.add(TCOTS_Items.CROWS_EYE);
                            entries.add(TCOTS_Items.VERBENA);
                            entries.add(TCOTS_Items.HAN_FIBER);
                            entries.add(TCOTS_Items.PUFFBALL);
                            entries.add(TCOTS_Items.PUFFBALL_MUSHROOM_BLOCK_ITEM);

                            //Potions
                            entries.add(TCOTS_Items.ALCHEMY_TABLE_ITEM);

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

                            entries.add(TCOTS_Items.SWALLOW_POTION.getDefaultStack());
                            entries.add(TCOTS_Items.SWALLOW_POTION_ENHANCED.getDefaultStack());
                            entries.add(TCOTS_Items.SWALLOW_POTION_SUPERIOR.getDefaultStack());

                            entries.add(TCOTS_Items.WHITE_RAFFARDS_DECOCTION.getDefaultStack());
                            entries.add(TCOTS_Items.WHITE_RAFFARDS_DECOCTION_ENHANCED.getDefaultStack());
                            entries.add(TCOTS_Items.WHITE_RAFFARDS_DECOCTION_SUPERIOR.getDefaultStack());

                            entries.add(TCOTS_Items.CAT_POTION.getDefaultStack());
                            entries.add(TCOTS_Items.CAT_POTION_ENHANCED.getDefaultStack());
                            entries.add(TCOTS_Items.CAT_POTION_SUPERIOR.getDefaultStack());

                            entries.add(TCOTS_Items.KILLER_WHALE_POTION.getDefaultStack());

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
                            entries.add(TCOTS_Items.FOGLET_DECOCTION);


                            //Misc/Blocks
                            entries.add(TCOTS_Items.NEST_SLAB_ITEM);
                            entries.add(TCOTS_Items.NEST_SKULL_ITEM);
                            entries.add(TCOTS_Items.MONSTER_NEST_ITEM);


                        }).build());
    }

}
