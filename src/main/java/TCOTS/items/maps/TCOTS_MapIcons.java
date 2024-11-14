package TCOTS.items.maps;

import TCOTS.TCOTS_Main;
import net.minecraft.item.map.MapDecorationType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;


public class TCOTS_MapIcons {
    public static final RegistryEntry<MapDecorationType> GIANT_CAVE = register("giant_cave", "giant_cave", true, 9615870, false, true);
    @SuppressWarnings("all")
    private static RegistryEntry<MapDecorationType> register(String id, String assetId, boolean showOnItemFrame, int mapColor, boolean trackCount, boolean explorationMapElement) {
        RegistryKey<MapDecorationType> registryKey = RegistryKey.of(RegistryKeys.MAP_DECORATION_TYPE, Identifier.of(TCOTS_Main.MOD_ID, id));
        MapDecorationType mapDecorationType = new MapDecorationType(Identifier.of(TCOTS_Main.MOD_ID, assetId), showOnItemFrame, mapColor, explorationMapElement, trackCount);
        return Registry.registerReference(Registries.MAP_DECORATION_TYPE, registryKey, mapDecorationType);
    }
}
