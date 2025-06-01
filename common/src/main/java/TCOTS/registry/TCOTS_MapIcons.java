package TCOTS.registry;

import TCOTS.TCOTS_Main;
import TCOTS.TCOTS_Registries;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.saveddata.maps.MapDecorationType;

public class TCOTS_MapIcons {

    public static ResourceLocation GIANT_CAVE = ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "giant_cave");


    public static void initMapIcons(){
        register("giant_cave", "giant_cave", true, 9615870, false, true);
    }

    public static Holder<MapDecorationType> GiantCave(){ return getHolder(GIANT_CAVE);}

    @SuppressWarnings("all")
    private static void register(String id, String assetId, boolean showOnItemFrame, int mapColor, boolean trackCount, boolean explorationMapElement) {
        TCOTS_Registries.MAP_ICONS.register(id, ()-> new MapDecorationType(ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, assetId), showOnItemFrame, mapColor, explorationMapElement, trackCount));
    }

    public static Holder<MapDecorationType> getHolder(ResourceLocation id) {
        Holder<MapDecorationType> holder = TCOTS_Registries.MAP_ICONS.getRegistrar().getHolder(id);

        if (holder == null) {
            throw new IllegalArgumentException("MapIcon with id " + id + " does not exist");
        }
        return holder;
    }
}
