package neoforge.TCOTS;


import TCOTS.TCOTS_Main;
import TCOTS.TCOTS_Tags;
import TCOTS.registry.TCOTS_Entities;
import TCOTS.registry.TCOTS_Items;
import TCOTS.registry.TCOTS_WorldGen;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.DataMapProvider;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.BiomeModifiers;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.neoforged.neoforge.registries.datamaps.builtin.Compostable;
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, modid = TCOTS_Main.MOD_ID)
public class TCOTS_DataGenerator {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        // Data generators may require some of these as constructor parameters.
        // See below for more details on each of these.
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        RegistrySetBuilder biomesModifiersSet = new RegistrySetBuilder();

        biomesModifiersSet.add(NeoForgeRegistries.Keys.BIOME_MODIFIERS, bootstrap -> {
            //Creates Vegetation
            {
                registerVegetation(bootstrap, "feature/celandine", TCOTS_Tags.CELANDINE_SPAWN, TCOTS_WorldGen.CELANDINE_PLANT_PLACED);
                registerVegetation(bootstrap, "feature/verbena", TCOTS_Tags.VERBENA_SPAWN, TCOTS_WorldGen.VERBENA_FLOWER_PLACED);
                registerVegetation(bootstrap, "feature/han_fiber", TCOTS_Tags.HAN_FIBER_SPAWN, TCOTS_WorldGen.HAN_FIBER_PLACED);
                registerVegetation(bootstrap, "feature/crows_eye", TCOTS_Tags.CROWS_EYE_SPAWN, TCOTS_WorldGen.CROWS_EYE_FERN_PLACED);
                registerVegetation(bootstrap, "feature/arenaria", TCOTS_Tags.ARENARIA_SPAWN, TCOTS_WorldGen.ARENARIA_BUSH_PLACED);

                registerVegetation(bootstrap, "feature/puffball_normal", TCOTS_Tags.PUFFBALL_SPAWN_NORMAL, TCOTS_WorldGen.PUFFBALL_NORMAL);
                registerVegetation(bootstrap, "feature/puffball_taiga", TCOTS_Tags.PUFFBALL_SPAWN_TAIGA, TCOTS_WorldGen.PUFFBALL_TAIGA);
                registerVegetation(bootstrap, "feature/puffball_swamp", TCOTS_Tags.PUFFBALL_SPAWN_SWAMP, TCOTS_WorldGen.PUFFBALL_SWAMP);
                registerVegetation(bootstrap, "feature/puffball_growth", TCOTS_Tags.MUSHROOM_SPAWN_OLD_GROWTH, TCOTS_WorldGen.PUFFBALL_OLD_GROWTH);

                registerVegetation(bootstrap, "feature/sewant_normal", TCOTS_Tags.SEWANT_SPAWN_NORMAL, TCOTS_WorldGen.SEWANT_MUSHROOMS_NORMAL);
                registerVegetation(bootstrap, "feature/sewant_taiga", TCOTS_Tags.SEWANT_SPAWN_TAIGA, TCOTS_WorldGen.SEWANT_MUSHROOMS_TAIGA);
                registerVegetation(bootstrap, "feature/sewant_dark", TCOTS_Tags.SEWANT_SPAWN_DARK, TCOTS_WorldGen.SEWANT_MUSHROOMS_DARK_FOREST);
                registerVegetation(bootstrap, "feature/sewant_growth", TCOTS_Tags.MUSHROOM_SPAWN_OLD_GROWTH, TCOTS_WorldGen.SEWANT_MUSHROOMS_OLD_GROWTH);

                registerVegetation(bootstrap, "feature/bryonia_surface", BiomeTags.IS_OVERWORLD, TCOTS_WorldGen.BRYONIA_SURFACE);
                registerVegetation(bootstrap, "feature/bryonia_underground", BiomeTags.IS_OVERWORLD, TCOTS_WorldGen.BRYONIA_UNDERGROUND);
            }

            //Sets monster spawns
            {
                //Necrophages
                {
                    //Drowners
                    {
                        registerMobSpawn(bootstrap, "spawn/drowner_swamp", TCOTS_Tags.DROWNER_SWAMP, TCOTS_Entities.Drowner(), 130, 3, 5);
                        registerMobSpawn(bootstrap, "spawn/drowner_beach", TCOTS_Tags.DROWNER_BEACH, TCOTS_Entities.Drowner(), 50, 2, 4);
                        registerMobSpawn(bootstrap, "spawn/drowner_water", TCOTS_Tags.DROWNER_WATER, TCOTS_Entities.Drowner(), 8, 2, 3);
                    }

                    //Rotfiends
                    {
                        registerMobSpawn(bootstrap, "spawn/rotfiend", TCOTS_Tags.ROTFIEND, TCOTS_Entities.Rotfiend(), 80, 4, 6);
                    }

                    //Foglets
                    {
                        registerMobSpawn(bootstrap, "spawn/foglet_swamp", TCOTS_Tags.FOGLET_SWAMP, TCOTS_Entities.Foglet(), 80, 1, 3);
                        registerMobSpawn(bootstrap, "spawn/foglet_dark", TCOTS_Tags.FOGLET_DARK, TCOTS_Entities.Foglet(), 120, 1, 2);
                        registerMobSpawn(bootstrap, "spawn/foglet_hills_forests", TCOTS_Tags.FOGLET_HILLS_FORESTS, TCOTS_Entities.Foglet(), 50, 1, 2);
                    }

                    //Water Hags
                    {
                        registerMobSpawn(bootstrap, "spawn/water_hag_swamp", TCOTS_Tags.WATER_HAG_SWAMP, TCOTS_Entities.WaterHag(), 80, 1, 2);
                        registerMobSpawn(bootstrap, "spawn/water_hag_river", TCOTS_Tags.WATER_HAG_RIVER, TCOTS_Entities.WaterHag(), 20, 1, 2);
                    }

                    //Grave Hags
                    {
                        registerMobSpawn(bootstrap, "spawn/grave_hag", TCOTS_Tags.GRAVE_HAG, TCOTS_Entities.GraveHag(), 80, 1, 2);
                    }

                    //Ghouls & Alghouls
                    {
                        registerMobSpawn(bootstrap, "spawn/ghoul", TCOTS_Tags.GHOUL, TCOTS_Entities.Ghoul(), 10, 3, 5);
                    }

                    //Scurvers
                    {
                        registerMobSpawn(bootstrap, "spawn/scurver", TCOTS_Tags.SCURVER, TCOTS_Entities.Scurver(), 40, 2, 3);
                    }

                    //Devourer
                    {
                        registerMobSpawn(bootstrap, "spawn/devourer", TCOTS_Tags.DEVOURER, TCOTS_Entities.Devourer(), 60, 3, 4);
                    }

                    //Graveir
                    {
                        registerMobSpawn(bootstrap, "spawn/graveir", TCOTS_Tags.GRAVEIR, TCOTS_Entities.Graveir(), 60, 1, 2);
                    }
                }

                //Ogroids
                {
                    //Nekkers
                    {
                        registerMobSpawn(bootstrap, "spawn/nekker", TCOTS_Tags.NEKKER, TCOTS_Entities.Nekker(), 5, 4, 6);
                    }

                    //Cyclops
                    {
                        registerMobSpawn(bootstrap, "spawn/cyclops", TCOTS_Tags.CYCLOPS, TCOTS_Entities.Cyclops(), 15, 1, 1);
                    }

                    //Rock Troll
                    {
                        registerMobSpawn(bootstrap, "spawn/rock_troll", TCOTS_Tags.ROCK_TROLL, TCOTS_Entities.RockTroll(), 5, 1, 1);
                    }

                    //Ice Troll
                    {
                        registerMobSpawn(bootstrap, "spawn/ice_troll", TCOTS_Tags.ICE_TROLL, TCOTS_Entities.IceTroll(), 2, 1, 1);
                    }

                    //Forest Troll
                    {
                        registerMobSpawn(bootstrap, "spawn/forest_troll", TCOTS_Tags.FOREST_TROLL, TCOTS_Entities.ForestTroll(), 5, 1, 1);
                    }
                }
            }
        });


        generator.addProvider(
                // Only run datapack generation when server data is being generated
                event.includeServer(),
                // Create the provider
                new DatapackBuiltinEntriesProvider(
                        output,
                        event.getLookupProvider(),
                        // Our registry set builder to generate the data from.
                        biomesModifiersSet,
                        // A set of mod ids we are generating. Usually only your own mod id.
                        Set.of(TCOTS_Main.MOD_ID)
                )
        );

        generator.addProvider(event.includeServer(), new DataMapGenerator(output, lookupProvider));
    }

    public static void registerVegetation(BootstrapContext<BiomeModifier> bootstrap, String biomeModifier, TagKey<Biome> spawnTag, ResourceKey<PlacedFeature> placedFeature){
        registerVegetation(bootstrap, modifierFor(biomeModifier), spawnTag, placedFeature);
    }

    public static void registerVegetation(BootstrapContext<BiomeModifier> bootstrap, ResourceKey<BiomeModifier> biomeModifierResourceKey, TagKey<Biome> spawnTag, ResourceKey<PlacedFeature> placedFeature){
        registerFeature(bootstrap, biomeModifierResourceKey, spawnTag, placedFeature, GenerationStep.Decoration.VEGETAL_DECORATION);
    }

    public static void registerFeature(BootstrapContext<BiomeModifier> bootstrap, ResourceKey<BiomeModifier> biomeModifierResourceKey, TagKey<Biome> spawnTag, ResourceKey<PlacedFeature> placedFeature, GenerationStep.Decoration step){
        HolderGetter<Biome> biomes = bootstrap.lookup(Registries.BIOME);
        HolderGetter<PlacedFeature> placedFeatures = bootstrap.lookup(Registries.PLACED_FEATURE);

        // Register the biome modifiers.
        bootstrap.register(biomeModifierResourceKey,
                new BiomeModifiers.AddFeaturesBiomeModifier(
                        // The biome(s) to generate within
                        (biomes.getOrThrow(spawnTag)),
                        // The feature(s) to generate within the biomes
                        HolderSet.direct(placedFeatures.getOrThrow(placedFeature)),
                        // The generation step
                        step
                )
        );
    }

    public static void registerMobSpawn(BootstrapContext<BiomeModifier> bootstrap, String biomeModifier, TagKey<Biome> spawnTag, EntityType<?> mob, int weight, int minGroupSize, int maxGroupSize){
        registerMobSpawn(bootstrap, modifierFor(biomeModifier), spawnTag, mob, weight, minGroupSize, maxGroupSize);
    }

    public static void registerMobSpawn(BootstrapContext<BiomeModifier> bootstrap, ResourceKey<BiomeModifier> biomeModifierResourceKey, TagKey<Biome> spawnTag, EntityType<?> mob, int weight, int minGroupSize, int maxGroupSize){
        HolderGetter<Biome> biomes = bootstrap.lookup(Registries.BIOME);

        // Register the biome modifiers.
        bootstrap.register(biomeModifierResourceKey,
                new BiomeModifiers.AddSpawnsBiomeModifier(
                        // The biome(s) to spawn the mobs within
                        biomes.getOrThrow(spawnTag),
                        // The spawners of the entities to add
                        List.of(
                                new MobSpawnSettings.SpawnerData(mob, weight, minGroupSize, maxGroupSize)
                        )
                )
        );
    }

    public static ResourceKey<BiomeModifier> modifierFor(String id){
        return ResourceKey.create(
                NeoForgeRegistries.Keys.BIOME_MODIFIERS, // The registry this key is for
                ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, id) // The registry name
        );
    }

    public static class DataMapGenerator extends DataMapProvider {
        public DataMapGenerator(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> lookupProvider) {
            super(packOutput, lookupProvider);
        }

        @Override
        protected void gather() {
//            registerCompostableItems()
            {
                builder(NeoForgeDataMaps.COMPOSTABLES)
                        .add(TCOTS_Items.ARENARIA, new Compostable(0.65f), false)
                        .add(TCOTS_Items.ALLSPICE, new Compostable(0.3f), false)

                        .add(TCOTS_Items.BRYONIA, new Compostable(0.65f), false)

                        .add(TCOTS_Items.CELANDINE, new Compostable(0.65f), false)
                        .add(TCOTS_Items.CROWS_EYE, new Compostable(0.65f), false)
                        .add(TCOTS_Items.CADAVERINE, new Compostable(0.85f), false)

                        .add(TCOTS_Items.HAN_FIBER, new Compostable(0.65f), false)

                        .add(TCOTS_Items.PUFFBALL, new Compostable(0.65f), false)
                        .add(TCOTS_Items.PUFFBALL_MUSHROOM_BLOCK_ITEM, new Compostable(0.85f), false)

                        .add(TCOTS_Items.SEWANT_MUSHROOMS, new Compostable(0.65f), false)
                        .add(TCOTS_Items.SEWANT_MUSHROOM_STEM_ITEM, new Compostable(0.85f), false)
                        .add(TCOTS_Items.SEWANT_MUSHROOM_BLOCK_ITEM, new Compostable(0.85f), false)

                        .add(TCOTS_Items.VERBENA, new Compostable(0.65f), false);
            }
        }
    }


}
