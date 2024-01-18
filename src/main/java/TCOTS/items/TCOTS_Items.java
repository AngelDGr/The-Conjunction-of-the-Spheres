package TCOTS.items;

import TCOTS.TCOTS_Main;
import TCOTS.entity.TCOTS_Entities;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class TCOTS_Items {

    //TODO: Add use to the items
        //TODO: Drowner Tongue usable for water breathing potion, maybe a shorter one?
        //TODO: Drowner Brain usable for a new potion, Swallow:
            //TODO: Add the Swallow effect, works like regeneration, it's a lot slower but the potion it's more durable


    public static final Item DROWNER_SPAWN_EGG = registerItem("drowner_spawn_egg",
            new SpawnEggItem(TCOTS_Entities.DROWNER, 0x8db1c0, 0x9fa3ae,
                    new FabricItemSettings()));

    public static final Item DROWNER_TONGUE = registerItem("drowner_tongue",
            new Item(
                    new FabricItemSettings().maxCount(64)));

    public static final Item DROWNER_BRAIN = registerItem("drowner_brain",
            new Item(
                    new FabricItemSettings().maxCount(16)));

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, new Identifier(TCOTS_Main.MOD_ID, name), item);
    }

    public static void registerModItems() {
        TCOTS_Main.LOGGER.info("Registering Mod Items for " + TCOTS_Main.MOD_ID);
    }
}
