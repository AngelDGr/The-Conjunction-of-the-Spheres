package fabric.TCOTS.world;

import TCOTS.TCOTS_Main;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.levelgen.structure.Structure;


public class TCOTS_Structures {
    public static final TagKey<Structure> ON_ICE_GIANT_MAP = generateTag("on_ice_giant_map");
    public static final ResourceKey<Structure> ICE_GIANT_CAVE = generateStructureRegistry("ice_giant_cave");

    private static TagKey<Structure> generateTag(String id) {
        return TagKey.create(Registries.STRUCTURE, ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID,id));
    }

    private static ResourceKey<Structure> generateStructureRegistry(String id) {
        return ResourceKey.create(Registries.STRUCTURE, ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID,id));
    }

    public static void registerStructureTags(){

    }
}
