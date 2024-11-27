package TCOTS.world;

import TCOTS.TCOTS_Main;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.structure.Structure;


public class TCOTS_Structures {
    public static final TagKey<Structure> ON_ICE_GIANT_MAP = generateTag("on_ice_giant_map");
    public static final RegistryKey<Structure> ICE_GIANT_CAVE = generateStructureRegistry("ice_giant_cave");

    private static TagKey<Structure> generateTag(String id) {
        return TagKey.of(RegistryKeys.STRUCTURE, Identifier.of(TCOTS_Main.MOD_ID,id));
    }

    private static RegistryKey<Structure> generateStructureRegistry(String id) {
        return RegistryKey.of(RegistryKeys.STRUCTURE, Identifier.of(TCOTS_Main.MOD_ID,id));
    }

    public static void registerStructureTags(){

    }
}
