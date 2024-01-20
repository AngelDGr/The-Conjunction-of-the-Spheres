package TCOTS.items;
import TCOTS.TCOTS_Main;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;

import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class TCOTS_ItemsGroups {

    public static void registerModItem(ItemGroup.Entries entries,Item item){
        entries.add(item);
    }

    //ItemGroup
    public static final ItemGroup TheConjunctionOfTheSpheres = Registry.register(Registries.ITEM_GROUP,
            new Identifier(TCOTS_Main.MOD_ID, "tcots-witcher"),
            FabricItemGroup.builder().displayName(Text.translatable("itemgroup.tcots-witcher"))
                    .icon(() -> new ItemStack(TCOTS_Items.DROWNER_BRAIN))
                    .entries((displayContext, entries) -> {
                        //Drops
                        entries.add(TCOTS_Items.DROWNER_BRAIN);
                        entries.add(TCOTS_Items.DROWNER_TONGUE);


                        //Spawn Eggs
                        entries.add(TCOTS_Items.DROWNER_SPAWN_EGG);

                        //Potions
                        entries.add(TCOTS_Items.SWALLOW_POTION.getDefaultStack());
                        entries.add(TCOTS_Items.KILLER_WHALE_POTION.getDefaultStack());

                        entries.add(TCOTS_Items.SWALLOW_SPLASH.getDefaultStack());
                        entries.add(TCOTS_Items.KILLER_WHALE_SPLASH.getDefaultStack());


                    }).build());

    public static void registerGroupItems() {
        TCOTS_Main.LOGGER.info("Registering Item Groups for " + TCOTS_Main.MOD_ID);
    }

}
