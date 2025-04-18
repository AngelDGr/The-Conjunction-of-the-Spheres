package TCOTS.items.maps;

import TCOTS.TCOTS_Main;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.saveddata.maps.MapDecorationType;


public class TCOTS_MapIcons {
    public static final Holder<MapDecorationType> GIANT_CAVE = register("giant_cave", "giant_cave", true, 9615870, false, true);
    @SuppressWarnings("all")
    private static Holder<MapDecorationType> register(String id, String assetId, boolean showOnItemFrame, int mapColor, boolean trackCount, boolean explorationMapElement) {
        ResourceKey<MapDecorationType> registryKey = ResourceKey.create(Registries.MAP_DECORATION_TYPE, ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, id));
        MapDecorationType mapDecorationType = new MapDecorationType(ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, assetId), showOnItemFrame, mapColor, explorationMapElement, trackCount);
        return Registry.registerForHolder(BuiltInRegistries.MAP_DECORATION_TYPE, registryKey, mapDecorationType);
    }
}
