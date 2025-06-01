package TCOTS.registry.fabric;

import TCOTS.TCOTS_Main;
import TCOTS.registry.TCOTS_Blocks;
import net.fabricmc.fabric.api.object.builder.v1.world.poi.PointOfInterestHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.level.block.Block;

@SuppressWarnings("unused")
public class TCOTS_VillagersImpl {

    public static void registerVillagers() {
        registerPoi("herbal_poi", TCOTS_Blocks.HERBAL_TABLE.get());
    }

    @SuppressWarnings("all")
    private static PoiType registerPoi(String name, Block block) {
        return PointOfInterestHelper.register(ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, name), 1, 1, block);
    }
}
