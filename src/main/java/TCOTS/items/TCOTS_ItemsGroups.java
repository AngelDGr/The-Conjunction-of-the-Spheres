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

    //ItemGroup
    public static final ItemGroup TheConjunctionOfTheSpheres = Registry.register(Registries.ITEM_GROUP,
            new Identifier(TCOTS_Main.MOD_ID, "tcots-witcher"),
            FabricItemGroup.builder().displayName(Text.translatable("itemgroup.tcots-witcher"))
                    .icon(() -> new ItemStack(TCOTS_Items.DROWNER_BRAIN))
                    .entries((displayContext, entries) -> {
                        //Drops
                        entries.add(TCOTS_Items.DROWNER_TONGUE);
                        entries.add(TCOTS_Items.DROWNER_BRAIN);
                        entries.add(TCOTS_Items.ROTFIEND_BLOOD);
                        entries.add(TCOTS_Items.NEKKER_EYE);
                        entries.add(TCOTS_Items.NEKKER_HEART);


                        //Spawn Eggs
                        entries.add(TCOTS_Items.DROWNER_SPAWN_EGG);
                        entries.add(TCOTS_Items.ROTFIEND_SPAWN_EGG);
                        entries.add(TCOTS_Items.NEKKER_SPAWN_EGG);
                        entries.add(TCOTS_Items.GRAVE_HAG_SPAWN_EGG);

                        //Potions
                        entries.add(TCOTS_Items.ALCHEMY_TABLE_ITEM);

                        entries.add(TCOTS_Items.DWARVEN_SPIRIT.getDefaultStack());
                        entries.add(TCOTS_Items.ALCOHEST.getDefaultStack());
                        entries.add(TCOTS_Items.AETHER);

                        entries.add(TCOTS_Items.SWALLOW_POTION.getDefaultStack());
                        entries.add(TCOTS_Items.SWALLOW_POTION_ENHANCED.getDefaultStack());
                        entries.add(TCOTS_Items.SWALLOW_POTION_SUPERIOR.getDefaultStack());

                        entries.add(TCOTS_Items.WHITE_RAFFARDS_DECOCTION.getDefaultStack());
                        entries.add(TCOTS_Items.WHITE_RAFFARDS_DECOCTION_ENHANCED.getDefaultStack());
                        entries.add(TCOTS_Items.WHITE_RAFFARDS_DECOCTION_SUPERIOR.getDefaultStack());

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


                        //Misc/Blocks
                        entries.add(TCOTS_Items.NEST_SLAB_ITEM);
                        entries.add(TCOTS_Items.NEST_SKULL_ITEM);
                        entries.add(TCOTS_Items.MONSTER_NEST_ITEM);


                    }).build());

    public static void registerGroupItems() {
        TCOTS_Main.LOGGER.info("Registering Item Groups for " + TCOTS_Main.MOD_ID);
    }

}
