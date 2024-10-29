package TCOTS.world;

import TCOTS.TCOTS_Main;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.registry.tag.BiomeTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.structure.PillagerOutpostGenerator;
import net.minecraft.structure.pool.StructurePool;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.Pool;
import net.minecraft.world.Heightmap;
import net.minecraft.world.StructureSpawns;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.StructureTerrainAdaptation;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.heightprovider.ConstantHeightProvider;
import net.minecraft.world.gen.heightprovider.UniformHeightProvider;
import net.minecraft.world.gen.structure.JigsawStructure;
import net.minecraft.world.gen.structure.Structure;
import net.minecraft.world.gen.structure.StructureKeys;
import net.minecraft.world.gen.structure.Structures;

import java.util.Map;

@SuppressWarnings("all")
public class TCOTS_Structures {
    public static final TagKey<Structure> ON_ICE_GIANT_MAP = generateTag("on_ice_giant_map");
    public static final RegistryKey<Structure> ICE_GIANT_CAVE = generateStructureRegistry("ice_giant_cave");

    private static TagKey<Structure> generateTag(String id) {
        return TagKey.of(RegistryKeys.STRUCTURE, new Identifier(TCOTS_Main.MOD_ID,id));
    }

    private static RegistryKey<Structure> generateStructureRegistry(String id) {
        return RegistryKey.of(RegistryKeys.STRUCTURE, new Identifier(TCOTS_Main.MOD_ID,id));
    }

    public static void registerStructureTags(){

    }

    public static void bootstrap(Registerable<Structure> structureRegisterable) {
        RegistryEntryLookup<Biome> registryEntryLookup = structureRegisterable.getRegistryLookup(RegistryKeys.BIOME);
        RegistryEntryLookup<StructurePool> registryEntryLookup2 = structureRegisterable.getRegistryLookup(RegistryKeys.TEMPLATE_POOL);
        structureRegisterable.register(ICE_GIANT_CAVE,

                new JigsawStructure(
                        createConfig(registryEntryLookup.getOrThrow(BiomeTags.VILLAGE_SNOWY_HAS_STRUCTURE), StructureTerrainAdaptation.NONE),
                        registryEntryLookup2.getOrThrow(PillagerOutpostGenerator.STRUCTURE_POOLS),
                        1,
                        UniformHeightProvider.create(
                                YOffset.fixed(300),
                                YOffset.fixed(62)
                        ),
                        false,
                        Heightmap.Type.WORLD_SURFACE_WG
                        ))
        ;

        structureRegisterable.register(StructureKeys.PILLAGER_OUTPOST,
                new JigsawStructure(
                        Structures.createConfig(
                            registryEntryLookup.getOrThrow(BiomeTags.PILLAGER_OUTPOST_HAS_STRUCTURE),
                            Map.of(SpawnGroup.MONSTER, new StructureSpawns(StructureSpawns.BoundingBox.STRUCTURE,
                                    Pool.of(new SpawnSettings.SpawnEntry[]{new SpawnSettings.SpawnEntry(EntityType.PILLAGER, 1, 1, 1)}))),
                            GenerationStep.Feature.SURFACE_STRUCTURES, StructureTerrainAdaptation.BEARD_THIN),
                        registryEntryLookup2.getOrThrow(PillagerOutpostGenerator.STRUCTURE_POOLS),
                        7,
                        ConstantHeightProvider.create(YOffset.fixed(0)),
                        true,
                        Heightmap.Type.WORLD_SURFACE_WG));


    }

    private static Structure.Config createConfig(RegistryEntryList<Biome> biomes, StructureTerrainAdaptation terrainAdaptation) {
        return Structures.createConfig(biomes, Map.of(), GenerationStep.Feature.SURFACE_STRUCTURES, terrainAdaptation);
    }
}
