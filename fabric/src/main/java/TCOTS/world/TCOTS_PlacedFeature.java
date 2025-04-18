package TCOTS.world;

import TCOTS.TCOTS_Main;
import com.google.common.collect.ImmutableList;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.RarityFilter;
import net.minecraft.world.level.levelgen.placement.SurfaceRelativeThresholdFilter;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class TCOTS_PlacedFeature {

    public static final ResourceKey<PlacedFeature> CELANDINE_PLANT_PLACED = registerKey("celandine_placed");
    public static final ResourceKey<PlacedFeature> VERBENA_FLOWER_PLACED = registerKey("verbena_placed");

    public static final ResourceKey<PlacedFeature> HAN_FIBER_PLACED = registerKey("han_fiber_placed");

    public static final ResourceKey<PlacedFeature> CROWS_EYE_FERN_PLACED = registerKey("crows_eye_placed");
    public static final ResourceKey<PlacedFeature> ARENARIA_BUSH_PLACED = registerKey("arenaria_placed");

    public static final ResourceKey<PlacedFeature> PUFFBALL_TAIGA = registerKey("puffball_taiga");
    public static final ResourceKey<PlacedFeature> PUFFBALL_OLD_GROWTH = registerKey("puffball_old_growth");
    public static final ResourceKey<PlacedFeature> PUFFBALL_NORMAL = registerKey("puffball_normal");
    public static final ResourceKey<PlacedFeature> PUFFBALL_SWAMP = registerKey("puffball_swamp");

    public static final ResourceKey<PlacedFeature> SEWANT_MUSHROOMS_NORMAL = registerKey("sewant_mushrooms_normal");
    public static final ResourceKey<PlacedFeature> SEWANT_MUSHROOMS_TAIGA = registerKey("sewant_mushrooms_taiga");
    public static final ResourceKey<PlacedFeature> SEWANT_MUSHROOMS_OLD_GROWTH = registerKey("sewant_mushrooms_growth");
    public static final ResourceKey<PlacedFeature> SEWANT_MUSHROOMS_DARK_FOREST = registerKey("sewant_mushrooms_dark_forest");

    public static final ResourceKey<PlacedFeature> BRYONIA_UNDERGROUND = registerKey("bryonia_underground");
    public static final ResourceKey<PlacedFeature> BRYONIA_SURFACE = registerKey("bryonia_surface");

    public static void boostrap(BootstrapContext<PlacedFeature> context) {
        HolderGetter<ConfiguredFeature<?, ?>> registryEntryLookup = context.lookup(Registries.CONFIGURED_FEATURE);
        Holder.Reference<ConfiguredFeature<?, ?>> puffballEntry = registryEntryLookup.getOrThrow(TCOTS_ConfiguredFeatures.PUFFBALL_MUSHROOM);
        Holder.Reference<ConfiguredFeature<?, ?>> sewantMushroomsEntry = registryEntryLookup.getOrThrow(TCOTS_ConfiguredFeatures.SEWANT_MUSHROOMS);


        PlacementUtils.register(context, CELANDINE_PLANT_PLACED, registryEntryLookup.getOrThrow(TCOTS_ConfiguredFeatures.CELANDINE_PLANT),
                RarityFilter.onAverageOnceEvery(120), InSquarePlacement.spread(),
                PlacementUtils.HEIGHTMAP, BiomeFilter.biome());

        PlacementUtils.register(context, VERBENA_FLOWER_PLACED, registryEntryLookup.getOrThrow(TCOTS_ConfiguredFeatures.VERBENA_FLOWER),
                RarityFilter.onAverageOnceEvery(140), InSquarePlacement.spread(),
                PlacementUtils.HEIGHTMAP, BiomeFilter.biome());

        PlacementUtils.register(context, HAN_FIBER_PLACED, registryEntryLookup.getOrThrow(TCOTS_ConfiguredFeatures.HAN_FIBER_PLANT),
                RarityFilter.onAverageOnceEvery(25), InSquarePlacement.spread(),
                PlacementUtils.HEIGHTMAP, BiomeFilter.biome());

        PlacementUtils.register(context, CROWS_EYE_FERN_PLACED, registryEntryLookup.getOrThrow(TCOTS_ConfiguredFeatures.CROWS_EYE_FERN),
                RarityFilter.onAverageOnceEvery(10), InSquarePlacement.spread(),
                PlacementUtils.HEIGHTMAP, BiomeFilter.biome());

        PlacementUtils.register(context, ARENARIA_BUSH_PLACED, registryEntryLookup.getOrThrow(TCOTS_ConfiguredFeatures.ARENARIA_BUSH),
                RarityFilter.onAverageOnceEvery(40), InSquarePlacement.spread(),
                PlacementUtils.HEIGHTMAP, BiomeFilter.biome());

        //Puffball
        PlacementUtils.register(context, PUFFBALL_NORMAL, puffballEntry, mushroomModifiers(320, null));
        PlacementUtils.register(context, PUFFBALL_TAIGA, puffballEntry, mushroomModifiers(120, null));
        PlacementUtils.register(context, PUFFBALL_OLD_GROWTH, puffballEntry, mushroomModifiers(56, null));
        PlacementUtils.register(context, PUFFBALL_SWAMP, puffballEntry, mushroomModifiers(16, null));

        PlacementUtils.register(context, SEWANT_MUSHROOMS_NORMAL, sewantMushroomsEntry, mushroomModifiers(300, null));
        PlacementUtils.register(context, SEWANT_MUSHROOMS_TAIGA, sewantMushroomsEntry, mushroomModifiers(100, null));
        PlacementUtils.register(context, SEWANT_MUSHROOMS_OLD_GROWTH, sewantMushroomsEntry, mushroomModifiers(42, null));
        PlacementUtils.register(context, SEWANT_MUSHROOMS_DARK_FOREST, sewantMushroomsEntry, mushroomModifiers(64, null));

        //SewantMushrooms

        //Bryonia
        PlacementUtils.register(context, BRYONIA_UNDERGROUND, registryEntryLookup.getOrThrow(TCOTS_ConfiguredFeatures.BRYONIA_VINE_UNDERGROUND),

                CountPlacement.of(UniformInt.of(80, 120)), PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT, InSquarePlacement.spread(), SurfaceRelativeThresholdFilter.of(Heightmap.Types.OCEAN_FLOOR_WG, Integer.MIN_VALUE, -13), BiomeFilter.biome()
        );

        PlacementUtils.register(context, BRYONIA_SURFACE, registryEntryLookup.getOrThrow(TCOTS_ConfiguredFeatures.BRYONIA_VINE_SURFACE),
                CountPlacement.of(UniformInt.of(80, 120)), PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT, InSquarePlacement.spread(), SurfaceRelativeThresholdFilter.of(Heightmap.Types.OCEAN_FLOOR_WG, Integer.MIN_VALUE, -13), BiomeFilter.biome()
        );
    }


    @SuppressWarnings({"unchecked", "rawtypes"})
    private static List<PlacementModifier> mushroomModifiers(int chance, @Nullable PlacementModifier modifier) {
        ImmutableList.Builder builder = ImmutableList.builder();
        if (modifier != null) {
            builder.add(modifier);
        }
        if (chance != 0) {
            builder.add(RarityFilter.onAverageOnceEvery(chance));
        }
        builder.add(InSquarePlacement.spread());
        builder.add(PlacementUtils.HEIGHTMAP);
        builder.add(BiomeFilter.biome());
        return builder.build();
    }

    public static void generateVegetation() {
        //Celandine
        BiomeModifications.addFeature(BiomeSelectors.includeByKey(Biomes.PLAINS,  Biomes.MEADOW,
                        Biomes.BIRCH_FOREST, Biomes.FOREST, Biomes.OLD_GROWTH_BIRCH_FOREST, Biomes.DARK_FOREST),
                GenerationStep.Decoration.VEGETAL_DECORATION, CELANDINE_PLANT_PLACED);

        //Verbena
        BiomeModifications.addFeature(BiomeSelectors.includeByKey(Biomes.PLAINS,  Biomes.MEADOW,
                        Biomes.BIRCH_FOREST, Biomes.FOREST, Biomes.OLD_GROWTH_BIRCH_FOREST, Biomes.DARK_FOREST,
                        Biomes.FLOWER_FOREST),
                GenerationStep.Decoration.VEGETAL_DECORATION, VERBENA_FLOWER_PLACED);

        //Han Fiber
        BiomeModifications.addFeature(BiomeSelectors.includeByKey(Biomes.JUNGLE, Biomes.SPARSE_JUNGLE,
                        Biomes.MANGROVE_SWAMP, Biomes.SWAMP, Biomes.SAVANNA
                        ),
                GenerationStep.Decoration.VEGETAL_DECORATION, HAN_FIBER_PLACED);

        //Crow's Eye
        BiomeModifications.addFeature(BiomeSelectors.includeByKey(Biomes.TAIGA, Biomes.OLD_GROWTH_SPRUCE_TAIGA, Biomes.OLD_GROWTH_PINE_TAIGA,
                        Biomes.SNOWY_TAIGA, Biomes.GROVE
                ),
                GenerationStep.Decoration.VEGETAL_DECORATION, CROWS_EYE_FERN_PLACED);

        //Arenaria
        BiomeModifications.addFeature(BiomeSelectors.includeByKey(Biomes.TAIGA, Biomes.OLD_GROWTH_SPRUCE_TAIGA, Biomes.OLD_GROWTH_PINE_TAIGA,
                        Biomes.SNOWY_TAIGA, Biomes.FLOWER_FOREST
                ),
                GenerationStep.Decoration.VEGETAL_DECORATION, ARENARIA_BUSH_PLACED);

        //Puffball
        BiomeModifications.addFeature(
                BiomeSelectors.excludeByKey(
                        Biomes.MUSHROOM_FIELDS,
                        Biomes.TAIGA, Biomes.MANGROVE_SWAMP, Biomes.THE_VOID,
                        Biomes.MEADOW, Biomes.FROZEN_PEAKS, Biomes.JAGGED_PEAKS, Biomes.STONY_PEAKS, Biomes.SNOWY_SLOPES, Biomes.GROVE,
                        Biomes.LUSH_CAVES),
                GenerationStep.Decoration.VEGETAL_DECORATION, PUFFBALL_NORMAL);

        BiomeModifications.addFeature(BiomeSelectors.includeByKey(Biomes.TAIGA, Biomes.SNOWY_TAIGA),
                GenerationStep.Decoration.VEGETAL_DECORATION, PUFFBALL_TAIGA);

        BiomeModifications.addFeature(BiomeSelectors.includeByKey(Biomes.SWAMP),
                GenerationStep.Decoration.VEGETAL_DECORATION, PUFFBALL_SWAMP);

        BiomeModifications.addFeature(BiomeSelectors.includeByKey(Biomes.OLD_GROWTH_SPRUCE_TAIGA, Biomes.OLD_GROWTH_PINE_TAIGA),
                GenerationStep.Decoration.VEGETAL_DECORATION, PUFFBALL_OLD_GROWTH);

        //Sewant Mushrooms
        BiomeModifications.addFeature(
                BiomeSelectors.excludeByKey(
                        Biomes.TAIGA, Biomes.MANGROVE_SWAMP, Biomes.THE_VOID,
                        Biomes.MEADOW, Biomes.FROZEN_PEAKS, Biomes.JAGGED_PEAKS, Biomes.STONY_PEAKS, Biomes.SNOWY_SLOPES, Biomes.GROVE,
                        Biomes.LUSH_CAVES),
                GenerationStep.Decoration.VEGETAL_DECORATION, SEWANT_MUSHROOMS_NORMAL);

        BiomeModifications.addFeature(BiomeSelectors.includeByKey(Biomes.TAIGA),
                GenerationStep.Decoration.VEGETAL_DECORATION, SEWANT_MUSHROOMS_TAIGA);

        BiomeModifications.addFeature(BiomeSelectors.includeByKey(Biomes.OLD_GROWTH_SPRUCE_TAIGA, Biomes.OLD_GROWTH_PINE_TAIGA),
                GenerationStep.Decoration.VEGETAL_DECORATION, SEWANT_MUSHROOMS_OLD_GROWTH);

        BiomeModifications.addFeature(BiomeSelectors.includeByKey(Biomes.DARK_FOREST),
                GenerationStep.Decoration.VEGETAL_DECORATION, SEWANT_MUSHROOMS_DARK_FOREST);

        //Bryonia
        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Decoration.VEGETAL_DECORATION, BRYONIA_UNDERGROUND);
        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Decoration.VEGETAL_DECORATION, BRYONIA_SURFACE);
    }

    public static ResourceKey<PlacedFeature> registerKey(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, name));
    }
}
