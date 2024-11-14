package TCOTS.world;

import TCOTS.TCOTS_Main;
import com.google.common.collect.ImmutableList;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.feature.PlacedFeatures;
import net.minecraft.world.gen.placementmodifier.*;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TCOTS_PlacedFeature {

    public static final RegistryKey<PlacedFeature> CELANDINE_PLANT_PLACED = registerKey("celandine_placed");
    public static final RegistryKey<PlacedFeature> VERBENA_FLOWER_PLACED = registerKey("verbena_placed");

    public static final RegistryKey<PlacedFeature> HAN_FIBER_PLACED = registerKey("han_fiber_placed");

    public static final RegistryKey<PlacedFeature> CROWS_EYE_FERN_PLACED = registerKey("crows_eye_placed");
    public static final RegistryKey<PlacedFeature> ARENARIA_BUSH_PLACED = registerKey("arenaria_placed");

    public static final RegistryKey<PlacedFeature> PUFFBALL_TAIGA = registerKey("puffball_taiga");
    public static final RegistryKey<PlacedFeature> PUFFBALL_OLD_GROWTH = registerKey("puffball_old_growth");
    public static final RegistryKey<PlacedFeature> PUFFBALL_NORMAL = registerKey("puffball_normal");
    public static final RegistryKey<PlacedFeature> PUFFBALL_SWAMP = registerKey("puffball_swamp");

    public static final RegistryKey<PlacedFeature> SEWANT_MUSHROOMS_NORMAL = registerKey("sewant_mushrooms_normal");
    public static final RegistryKey<PlacedFeature> SEWANT_MUSHROOMS_TAIGA = registerKey("sewant_mushrooms_taiga");
    public static final RegistryKey<PlacedFeature> SEWANT_MUSHROOMS_OLD_GROWTH = registerKey("sewant_mushrooms_growth");
    public static final RegistryKey<PlacedFeature> SEWANT_MUSHROOMS_DARK_FOREST = registerKey("sewant_mushrooms_dark_forest");

    public static final RegistryKey<PlacedFeature> BRYONIA_UNDERGROUND = registerKey("bryonia_underground");
    public static final RegistryKey<PlacedFeature> BRYONIA_SURFACE = registerKey("bryonia_surface");

    public static void boostrap(Registerable<PlacedFeature> context) {
        RegistryEntryLookup<ConfiguredFeature<?, ?>> registryEntryLookup = context.getRegistryLookup(RegistryKeys.CONFIGURED_FEATURE);
        RegistryEntry.Reference<ConfiguredFeature<?, ?>> puffballEntry = registryEntryLookup.getOrThrow(TCOTS_ConfiguredFeatures.PUFFBALL_MUSHROOM);
        RegistryEntry.Reference<ConfiguredFeature<?, ?>> sewantMushroomsEntry = registryEntryLookup.getOrThrow(TCOTS_ConfiguredFeatures.SEWANT_MUSHROOMS);


        PlacedFeatures.register(context, CELANDINE_PLANT_PLACED, registryEntryLookup.getOrThrow(TCOTS_ConfiguredFeatures.CELANDINE_PLANT),
                RarityFilterPlacementModifier.of(120), SquarePlacementModifier.of(),
                PlacedFeatures.MOTION_BLOCKING_HEIGHTMAP, BiomePlacementModifier.of());

        PlacedFeatures.register(context, VERBENA_FLOWER_PLACED, registryEntryLookup.getOrThrow(TCOTS_ConfiguredFeatures.VERBENA_FLOWER),
                RarityFilterPlacementModifier.of(140), SquarePlacementModifier.of(),
                PlacedFeatures.MOTION_BLOCKING_HEIGHTMAP, BiomePlacementModifier.of());

        PlacedFeatures.register(context, HAN_FIBER_PLACED, registryEntryLookup.getOrThrow(TCOTS_ConfiguredFeatures.HAN_FIBER_PLANT),
                RarityFilterPlacementModifier.of(25), SquarePlacementModifier.of(),
                PlacedFeatures.MOTION_BLOCKING_HEIGHTMAP, BiomePlacementModifier.of());

        PlacedFeatures.register(context, CROWS_EYE_FERN_PLACED, registryEntryLookup.getOrThrow(TCOTS_ConfiguredFeatures.CROWS_EYE_FERN),
                RarityFilterPlacementModifier.of(10), SquarePlacementModifier.of(),
                PlacedFeatures.MOTION_BLOCKING_HEIGHTMAP, BiomePlacementModifier.of());

        PlacedFeatures.register(context, ARENARIA_BUSH_PLACED, registryEntryLookup.getOrThrow(TCOTS_ConfiguredFeatures.ARENARIA_BUSH),
                RarityFilterPlacementModifier.of(40), SquarePlacementModifier.of(),
                PlacedFeatures.MOTION_BLOCKING_HEIGHTMAP, BiomePlacementModifier.of());

        //Puffball
        PlacedFeatures.register(context, PUFFBALL_NORMAL, puffballEntry, mushroomModifiers(320, null));
        PlacedFeatures.register(context, PUFFBALL_TAIGA, puffballEntry, mushroomModifiers(120, null));
        PlacedFeatures.register(context, PUFFBALL_OLD_GROWTH, puffballEntry, mushroomModifiers(56, null));
        PlacedFeatures.register(context, PUFFBALL_SWAMP, puffballEntry, mushroomModifiers(16, null));

        PlacedFeatures.register(context, SEWANT_MUSHROOMS_NORMAL, sewantMushroomsEntry, mushroomModifiers(300, null));
        PlacedFeatures.register(context, SEWANT_MUSHROOMS_TAIGA, sewantMushroomsEntry, mushroomModifiers(100, null));
        PlacedFeatures.register(context, SEWANT_MUSHROOMS_OLD_GROWTH, sewantMushroomsEntry, mushroomModifiers(42, null));
        PlacedFeatures.register(context, SEWANT_MUSHROOMS_DARK_FOREST, sewantMushroomsEntry, mushroomModifiers(64, null));

        //SewantMushrooms

        //Bryonia
        PlacedFeatures.register(context, BRYONIA_UNDERGROUND, registryEntryLookup.getOrThrow(TCOTS_ConfiguredFeatures.BRYONIA_VINE_UNDERGROUND),

                CountPlacementModifier.of(UniformIntProvider.create(80, 120)), PlacedFeatures.BOTTOM_TO_120_RANGE, SquarePlacementModifier.of(), SurfaceThresholdFilterPlacementModifier.of(Heightmap.Type.OCEAN_FLOOR_WG, Integer.MIN_VALUE, -13), BiomePlacementModifier.of()
        );

        PlacedFeatures.register(context, BRYONIA_SURFACE, registryEntryLookup.getOrThrow(TCOTS_ConfiguredFeatures.BRYONIA_VINE_SURFACE),
                CountPlacementModifier.of(UniformIntProvider.create(80, 120)), PlacedFeatures.BOTTOM_TO_120_RANGE, SquarePlacementModifier.of(), SurfaceThresholdFilterPlacementModifier.of(Heightmap.Type.OCEAN_FLOOR_WG, Integer.MIN_VALUE, -13), BiomePlacementModifier.of()
        );
    }


    @SuppressWarnings({"unchecked", "rawtypes"})
    private static List<PlacementModifier> mushroomModifiers(int chance, @Nullable PlacementModifier modifier) {
        ImmutableList.Builder builder = ImmutableList.builder();
        if (modifier != null) {
            builder.add(modifier);
        }
        if (chance != 0) {
            builder.add(RarityFilterPlacementModifier.of(chance));
        }
        builder.add(SquarePlacementModifier.of());
        builder.add(PlacedFeatures.MOTION_BLOCKING_HEIGHTMAP);
        builder.add(BiomePlacementModifier.of());
        return builder.build();
    }

    public static void generateVegetation() {
        //Celandine
        BiomeModifications.addFeature(BiomeSelectors.includeByKey(BiomeKeys.PLAINS,  BiomeKeys.MEADOW,
                        BiomeKeys.BIRCH_FOREST, BiomeKeys.FOREST, BiomeKeys.OLD_GROWTH_BIRCH_FOREST, BiomeKeys.DARK_FOREST),
                GenerationStep.Feature.VEGETAL_DECORATION, CELANDINE_PLANT_PLACED);

        //Verbena
        BiomeModifications.addFeature(BiomeSelectors.includeByKey(BiomeKeys.PLAINS,  BiomeKeys.MEADOW,
                        BiomeKeys.BIRCH_FOREST, BiomeKeys.FOREST, BiomeKeys.OLD_GROWTH_BIRCH_FOREST, BiomeKeys.DARK_FOREST,
                        BiomeKeys.FLOWER_FOREST),
                GenerationStep.Feature.VEGETAL_DECORATION, VERBENA_FLOWER_PLACED);

        //Han Fiber
        BiomeModifications.addFeature(BiomeSelectors.includeByKey(BiomeKeys.JUNGLE, BiomeKeys.SPARSE_JUNGLE,
                        BiomeKeys.MANGROVE_SWAMP, BiomeKeys.SWAMP, BiomeKeys.SAVANNA
                        ),
                GenerationStep.Feature.VEGETAL_DECORATION, HAN_FIBER_PLACED);

        //Crow's Eye
        BiomeModifications.addFeature(BiomeSelectors.includeByKey(BiomeKeys.TAIGA, BiomeKeys.OLD_GROWTH_SPRUCE_TAIGA, BiomeKeys.OLD_GROWTH_PINE_TAIGA,
                        BiomeKeys.SNOWY_TAIGA, BiomeKeys.GROVE
                ),
                GenerationStep.Feature.VEGETAL_DECORATION, CROWS_EYE_FERN_PLACED);

        //Arenaria
        BiomeModifications.addFeature(BiomeSelectors.includeByKey(BiomeKeys.TAIGA, BiomeKeys.OLD_GROWTH_SPRUCE_TAIGA, BiomeKeys.OLD_GROWTH_PINE_TAIGA,
                        BiomeKeys.SNOWY_TAIGA, BiomeKeys.FLOWER_FOREST
                ),
                GenerationStep.Feature.VEGETAL_DECORATION, ARENARIA_BUSH_PLACED);

        //Puffball
        BiomeModifications.addFeature(
                BiomeSelectors.excludeByKey(
                        BiomeKeys.MUSHROOM_FIELDS,
                        BiomeKeys.TAIGA, BiomeKeys.MANGROVE_SWAMP, BiomeKeys.THE_VOID,
                        BiomeKeys.MEADOW, BiomeKeys.FROZEN_PEAKS, BiomeKeys.JAGGED_PEAKS, BiomeKeys.STONY_PEAKS, BiomeKeys.SNOWY_SLOPES, BiomeKeys.GROVE,
                        BiomeKeys.LUSH_CAVES),
                GenerationStep.Feature.VEGETAL_DECORATION, PUFFBALL_NORMAL);

        BiomeModifications.addFeature(BiomeSelectors.includeByKey(BiomeKeys.TAIGA, BiomeKeys.SNOWY_TAIGA),
                GenerationStep.Feature.VEGETAL_DECORATION, PUFFBALL_TAIGA);

        BiomeModifications.addFeature(BiomeSelectors.includeByKey(BiomeKeys.SWAMP),
                GenerationStep.Feature.VEGETAL_DECORATION, PUFFBALL_SWAMP);

        BiomeModifications.addFeature(BiomeSelectors.includeByKey(BiomeKeys.OLD_GROWTH_SPRUCE_TAIGA, BiomeKeys.OLD_GROWTH_PINE_TAIGA),
                GenerationStep.Feature.VEGETAL_DECORATION, PUFFBALL_OLD_GROWTH);

        //Sewant Mushrooms
        BiomeModifications.addFeature(
                BiomeSelectors.excludeByKey(
                        BiomeKeys.TAIGA, BiomeKeys.MANGROVE_SWAMP, BiomeKeys.THE_VOID,
                        BiomeKeys.MEADOW, BiomeKeys.FROZEN_PEAKS, BiomeKeys.JAGGED_PEAKS, BiomeKeys.STONY_PEAKS, BiomeKeys.SNOWY_SLOPES, BiomeKeys.GROVE,
                        BiomeKeys.LUSH_CAVES),
                GenerationStep.Feature.VEGETAL_DECORATION, SEWANT_MUSHROOMS_NORMAL);

        BiomeModifications.addFeature(BiomeSelectors.includeByKey(BiomeKeys.TAIGA),
                GenerationStep.Feature.VEGETAL_DECORATION, SEWANT_MUSHROOMS_TAIGA);

        BiomeModifications.addFeature(BiomeSelectors.includeByKey(BiomeKeys.OLD_GROWTH_SPRUCE_TAIGA, BiomeKeys.OLD_GROWTH_PINE_TAIGA),
                GenerationStep.Feature.VEGETAL_DECORATION, SEWANT_MUSHROOMS_OLD_GROWTH);

        BiomeModifications.addFeature(BiomeSelectors.includeByKey(BiomeKeys.DARK_FOREST),
                GenerationStep.Feature.VEGETAL_DECORATION, SEWANT_MUSHROOMS_DARK_FOREST);

        //Bryonia
        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Feature.VEGETAL_DECORATION, BRYONIA_UNDERGROUND);
        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Feature.VEGETAL_DECORATION, BRYONIA_SURFACE);
    }

    public static RegistryKey<PlacedFeature> registerKey(String name) {
        return RegistryKey.of(RegistryKeys.PLACED_FEATURE, Identifier.of(TCOTS_Main.MOD_ID, name));
    }
}
