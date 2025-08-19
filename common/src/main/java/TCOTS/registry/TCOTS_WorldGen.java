package TCOTS.registry;

import TCOTS.TCOTS_Main;
import TCOTS.TCOTS_Registries;
import TCOTS.blocks.plants.*;
import TCOTS.world.gen.BryoniaPatchFeature;
import TCOTS.world.gen.BryoniaPatchFeatureConfig;
import TCOTS.world.gen.HugePuffballMushroomFeature;
import TCOTS.world.gen.HugeSewantMushroomsFeature;
import com.google.common.collect.ImmutableList;
import dev.architectury.hooks.level.biome.BiomeProperties;
import dev.architectury.registry.level.biome.BiomeModifications;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.HugeMushroomFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraft.world.level.levelgen.structure.templatesystem.*;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.Supplier;

public class TCOTS_WorldGen {
//    Seed for testing
//    -3075228940648816938

    //Placed Features
    public static final ResourceKey<PlacedFeature> CELANDINE_PLANT_PLACED = registerPlacedFeatureKey("celandine_placed");
    public static final ResourceKey<PlacedFeature> VERBENA_FLOWER_PLACED = registerPlacedFeatureKey("verbena_placed");
    public static final ResourceKey<PlacedFeature> HAN_FIBER_PLACED = registerPlacedFeatureKey("han_fiber_placed");
    public static final ResourceKey<PlacedFeature> CROWS_EYE_FERN_PLACED = registerPlacedFeatureKey("crows_eye_placed");
    public static final ResourceKey<PlacedFeature> ARENARIA_BUSH_PLACED = registerPlacedFeatureKey("arenaria_placed");
    public static final ResourceKey<PlacedFeature> PUFFBALL_TAIGA = registerPlacedFeatureKey("puffball_taiga");
    public static final ResourceKey<PlacedFeature> PUFFBALL_OLD_GROWTH = registerPlacedFeatureKey("puffball_old_growth");
    public static final ResourceKey<PlacedFeature> PUFFBALL_NORMAL = registerPlacedFeatureKey("puffball_normal");
    public static final ResourceKey<PlacedFeature> PUFFBALL_SWAMP = registerPlacedFeatureKey("puffball_swamp");
    public static final ResourceKey<PlacedFeature> SEWANT_MUSHROOMS_NORMAL = registerPlacedFeatureKey("sewant_mushrooms_normal");
    public static final ResourceKey<PlacedFeature> SEWANT_MUSHROOMS_TAIGA = registerPlacedFeatureKey("sewant_mushrooms_taiga");
    public static final ResourceKey<PlacedFeature> SEWANT_MUSHROOMS_OLD_GROWTH = registerPlacedFeatureKey("sewant_mushrooms_growth");
    public static final ResourceKey<PlacedFeature> SEWANT_MUSHROOMS_DARK_FOREST = registerPlacedFeatureKey("sewant_mushrooms_dark_forest");
    public static final ResourceKey<PlacedFeature> BRYONIA_UNDERGROUND = registerPlacedFeatureKey("bryonia_underground");
    public static final ResourceKey<PlacedFeature> BRYONIA_SURFACE = registerPlacedFeatureKey("bryonia_surface");

    public static final ResourceKey<StructureProcessorList> RANDOM_TROLL_CAVE = registerStructureProcessorKey("random_troll_cave");
    public static final ResourceKey<StructureProcessorList> RANDOM_HERBALIST_HERBS_PLAINS = registerStructureProcessorKey("random_herbalist_herbs_plains");
    public static final ResourceKey<StructureProcessorList> RANDOM_HERBALIST_HERBS_TAIGA = registerStructureProcessorKey("random_herbalist_herbs_taiga");
    public static final ResourceKey<StructureProcessorList> RANDOM_HERBALIST_HERBS_SNOWY = registerStructureProcessorKey("random_herbalist_herbs_snowy");
    public static final ResourceKey<StructureProcessorList> RANDOM_HERBALIST_HERBS_SAVANNA = registerStructureProcessorKey("random_herbalist_herbs_savanna");
    public static final ResourceKey<StructureProcessorList> RANDOM_HERBALIST_HERBS_DESERT = registerStructureProcessorKey("random_herbalist_herbs_desert");

    public static RegistrySupplier<Feature<HugeMushroomFeatureConfiguration>> HUGE_PUFFBALL_MUSHROOM_F = registerFeature("huge_puffball_mushroom", ()-> new HugePuffballMushroomFeature(HugeMushroomFeatureConfiguration.CODEC));
    public static RegistrySupplier<Feature<HugeMushroomFeatureConfiguration>> HUGE_SEWANT_MUSHROOMS_F = registerFeature("huge_sewant_mushrooms", ()-> new HugeSewantMushroomsFeature(HugeMushroomFeatureConfiguration.CODEC));
    public static RegistrySupplier<Feature<BryoniaPatchFeatureConfig>> BRYONIA_PATCH_FEATURE = registerFeature("bryonia_patch", ()-> new BryoniaPatchFeature(BryoniaPatchFeatureConfig.CODEC));

    //Configured Features
    public static final ResourceKey<ConfiguredFeature<?, ?>> HUGE_PUFFBALL_MUSHROOM_CF = registerKey("huge_puffball_mushroom");
    public static final ResourceKey<ConfiguredFeature<?, ?>> HUGE_SEWANT_MUSHROOMS_CF = registerKey("huge_sewant_mushrooms");

    public static void initFeatures(){

    }

    //Doesn't work in NeoForge, data biome_modifiers used instead
    public static void initVegetation(){
        BiomeModifications.addProperties(

                ((biomeContext, mutable) -> {
                    if(biomeContext.hasTag(BiomeTags.IS_OVERWORLD)){
                        //Celandine
                        if(biomeContext.hasTag(TCOTS_Tags.CELANDINE_SPAWN)){

                            addFeature(mutable, GenerationStep.Decoration.VEGETAL_DECORATION, CELANDINE_PLANT_PLACED);
                        }

                        //Verbena
                        if(biomeContext.hasTag(TCOTS_Tags.VERBENA_SPAWN)){

                            addFeature(mutable, GenerationStep.Decoration.VEGETAL_DECORATION, VERBENA_FLOWER_PLACED);
                        }

                        // Han Fiber
                        if (biomeContext.hasTag(TCOTS_Tags.HAN_FIBER_SPAWN)) {

                            addFeature(mutable, GenerationStep.Decoration.VEGETAL_DECORATION, HAN_FIBER_PLACED);
                        }

                        // Crow's Eye
                        if (biomeContext.hasTag(TCOTS_Tags.CROWS_EYE_SPAWN)) {

                            addFeature(mutable, GenerationStep.Decoration.VEGETAL_DECORATION, CROWS_EYE_FERN_PLACED);
                        }

                        // Arenaria
                        if (biomeContext.hasTag(TCOTS_Tags.ARENARIA_SPAWN)) {

                            addFeature(mutable, GenerationStep.Decoration.VEGETAL_DECORATION, ARENARIA_BUSH_PLACED);
                        }

                        // Puffball
                        if (biomeContext.hasTag(TCOTS_Tags.PUFFBALL_SPAWN_NORMAL)) {

                            addFeature(mutable, GenerationStep.Decoration.VEGETAL_DECORATION, PUFFBALL_NORMAL);
                        }

                        if (biomeContext.hasTag(TCOTS_Tags.MUSHROOM_SPAWN_OLD_GROWTH)) {

                            addFeature(mutable, GenerationStep.Decoration.VEGETAL_DECORATION, PUFFBALL_OLD_GROWTH);
                        }

                        if (biomeContext.hasTag(TCOTS_Tags.PUFFBALL_SPAWN_SWAMP)) {

                            addFeature(mutable, GenerationStep.Decoration.VEGETAL_DECORATION, PUFFBALL_SWAMP);
                        }

                        if (biomeContext.hasTag(TCOTS_Tags.PUFFBALL_SPAWN_TAIGA)) {

                            addFeature(mutable, GenerationStep.Decoration.VEGETAL_DECORATION, PUFFBALL_TAIGA);
                        }


                        // Sewant Mushrooms
                        if (biomeContext.hasTag(TCOTS_Tags.SEWANT_SPAWN_NORMAL)) {

                            addFeature(mutable, GenerationStep.Decoration.VEGETAL_DECORATION, SEWANT_MUSHROOMS_NORMAL);
                        }

                        if (biomeContext.hasTag(TCOTS_Tags.SEWANT_SPAWN_TAIGA)) {

                            addFeature(mutable, GenerationStep.Decoration.VEGETAL_DECORATION, SEWANT_MUSHROOMS_TAIGA);
                        }

                        if (biomeContext.hasTag(TCOTS_Tags.MUSHROOM_SPAWN_OLD_GROWTH)) {

                            addFeature(mutable, GenerationStep.Decoration.VEGETAL_DECORATION, SEWANT_MUSHROOMS_OLD_GROWTH);
                        }

                        if (biomeContext.hasTag(TCOTS_Tags.SEWANT_SPAWN_DARK)) {

                            addFeature(mutable, GenerationStep.Decoration.VEGETAL_DECORATION, SEWANT_MUSHROOMS_DARK_FOREST);
                        }

                        // Bryonia
                        addFeature(mutable, GenerationStep.Decoration.VEGETAL_DECORATION, BRYONIA_UNDERGROUND);
                        addFeature(mutable, GenerationStep.Decoration.VEGETAL_DECORATION, BRYONIA_SURFACE);
                    }
        }) );
    }

    //Needs to be used the same amount the times as existent biome_modifiers as data in NeoForge
    @SuppressWarnings("all")
    public static void addFeature(BiomeProperties.Mutable mutable, GenerationStep.Decoration decoration, ResourceKey<PlacedFeature> feature){
        mutable.getGenerationProperties().addFeature(decoration, feature);
    }


    //Flowers
    public static ResourceKey<ConfiguredFeature<?, ?>> CELANDINE_PLANT = registerKey("celandine_patch");
    public static ResourceKey<ConfiguredFeature<?, ?>> VERBENA_FLOWER = registerKey("verbena_patch");
    public static ResourceKey<ConfiguredFeature<?, ?>> HAN_FIBER_PLANT = registerKey("han_fiber_patch");

    //Fern
    public static ResourceKey<ConfiguredFeature<?, ?>> CROWS_EYE_FERN = registerKey("crows_eye_patch");
    //Sweet Berries
    public static ResourceKey<ConfiguredFeature<?, ?>> ARENARIA_BUSH = registerKey("arenaria_patch");

    //Others
    public static ResourceKey<ConfiguredFeature<?, ?>> PUFFBALL_MUSHROOM = registerKey("puffball_patch");
    public static ResourceKey<ConfiguredFeature<?, ?>> SEWANT_MUSHROOMS = registerKey("sewant_mushrooms_patch");
    public static ResourceKey<ConfiguredFeature<?, ?>> BRYONIA_VINE_UNDERGROUND = registerKey("bryonia_patch_underground");
    public static ResourceKey<ConfiguredFeature<?, ?>> BRYONIA_VINE_SURFACE = registerKey("bryonia_patch_surface");

    public static ResourceKey<ConfiguredFeature<?, ?>> registerKey(final String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, name));
    }

    public static ResourceKey<PlacedFeature> registerPlacedFeatureKey(final String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, name));
    }

    public static ResourceKey<StructureProcessorList> registerStructureProcessorKey(final String name) {
        return ResourceKey.create(Registries.PROCESSOR_LIST, ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, name));
    }

    private static <C extends FeatureConfiguration, F extends Feature<C>> RegistrySupplier<F> registerFeature(final String name, final Supplier<F> feature) {
        return TCOTS_Registries.FEATURE.register(name,  feature);
    }

    private static <FC extends FeatureConfiguration, F extends Feature<FC>> void registerFeature(final BootstrapContext<ConfiguredFeature<?, ?>> context, final ResourceKey<ConfiguredFeature<?, ?>> key, final F feature, final FC configuration) {
        context.register(key, new ConfiguredFeature<>(feature, configuration));
    }

    public static void boostrapConfiguredFeature(final BootstrapContext<ConfiguredFeature<?, ?>> context) {

        registerFeature(context, TCOTS_WorldGen.HUGE_PUFFBALL_MUSHROOM_CF, HUGE_PUFFBALL_MUSHROOM_F.get(), new HugeMushroomFeatureConfiguration(
                BlockStateProvider.simple(TCOTS_Blocks.PuffballMushroomBlock().defaultBlockState().setValue(HugeMushroomBlock.DOWN, false)),
                BlockStateProvider.simple((Blocks.MUSHROOM_STEM.defaultBlockState().setValue(HugeMushroomBlock.UP, false)).setValue(HugeMushroomBlock.DOWN, false)),
                3
        ));

        registerFeature(context, TCOTS_WorldGen.HUGE_SEWANT_MUSHROOMS_CF, HUGE_SEWANT_MUSHROOMS_F.get(), new HugeMushroomFeatureConfiguration(
                BlockStateProvider.simple(TCOTS_Blocks.SewantMushroomBlock().defaultBlockState().setValue(HugeMushroomBlock.DOWN, false)),
                BlockStateProvider.simple((TCOTS_Blocks.SewantMushroomStem().defaultBlockState().setValue(HugeMushroomBlock.UP, false)).setValue(HugeMushroomBlock.DOWN, false)),
                2
        ));


        //Size of a flower patch
        registerFeature(context, CELANDINE_PLANT, Feature.FLOWER,
                new RandomPatchConfiguration(12,7,3, PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK,
                        new SimpleBlockConfiguration(BlockStateProvider.simple(TCOTS_Blocks.CelandinePlant().defaultBlockState().setValue(CelandinePlant.AGE, 3))))));

        registerFeature(context, VERBENA_FLOWER, Feature.FLOWER,
                new RandomPatchConfiguration(20,7,3, PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK,
                        new SimpleBlockConfiguration(BlockStateProvider.simple(TCOTS_Blocks.VerbenaFlower().defaultBlockState().setValue(VerbenaFlower.AGE, 3))))));

        registerFeature(context, HAN_FIBER_PLANT, Feature.FLOWER,
                new RandomPatchConfiguration(50,4,3, PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK,
                        new SimpleBlockConfiguration(BlockStateProvider.simple(TCOTS_Blocks.HanFiberPlant().defaultBlockState().setValue(HanFiberPlant.AGE, 3))))));

        registerFeature(context, CROWS_EYE_FERN, Feature.RANDOM_PATCH,
                new RandomPatchConfiguration(40,7,3, PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK,
                        new SimpleBlockConfiguration(BlockStateProvider.simple(TCOTS_Blocks.CrowsEyeFern().defaultBlockState().setValue(CrowsEyeFern.AGE, 4))))));

        registerFeature(context, ARENARIA_BUSH, Feature.RANDOM_PATCH,
                new RandomPatchConfiguration(30,7,3, PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK,
                        new SimpleBlockConfiguration(BlockStateProvider.simple(TCOTS_Blocks.ArenariaBush().defaultBlockState().setValue(ArenariaBush.AGE, 1))))));

        registerFeature(context, PUFFBALL_MUSHROOM, Feature.RANDOM_PATCH,
                new RandomPatchConfiguration(96,7,3, PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK,
                        new SimpleBlockConfiguration(BlockStateProvider.simple(TCOTS_Blocks.PuffballMushroom().defaultBlockState())))));

        registerFeature(context, SEWANT_MUSHROOMS, Feature.RANDOM_PATCH,
                new RandomPatchConfiguration(96,7,3, PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK,
                        new SimpleBlockConfiguration(BlockStateProvider.simple(TCOTS_Blocks.SewantMushroomsPlant().defaultBlockState()
                                .setValue(SewantMushroomsPlant.MUSHROOM_AMOUNT, 3)
                                .setValue(SewantMushroomsPlant.FACING, Direction.NORTH))))));


        registerFeature(context, BRYONIA_VINE_UNDERGROUND,
                BRYONIA_PATCH_FEATURE.get(),
                new BryoniaPatchFeatureConfig(
                        ((MultifaceBlock) TCOTS_Blocks.BryoniaVine()),
                        20,
                        false, false, true,
                        20,
                        50
                ));

        registerFeature(context, BRYONIA_VINE_SURFACE,
                BRYONIA_PATCH_FEATURE.get(),
                new BryoniaPatchFeatureConfig(
                        ((MultifaceBlock) TCOTS_Blocks.BryoniaVine()),
                        20,
                        false, false, true,
                        50,
                        200
                ));
    }

    public static void boostrapPlacedFeature(final BootstrapContext<PlacedFeature> context) {
        final HolderGetter<ConfiguredFeature<?, ?>> registryEntryLookup = context.lookup(Registries.CONFIGURED_FEATURE);
        final Holder.Reference<ConfiguredFeature<?, ?>> puffballEntry = registryEntryLookup.getOrThrow(PUFFBALL_MUSHROOM);
        final Holder.Reference<ConfiguredFeature<?, ?>> sewantMushroomsEntry = registryEntryLookup.getOrThrow(SEWANT_MUSHROOMS);


        PlacementUtils.register(context, CELANDINE_PLANT_PLACED, registryEntryLookup.getOrThrow(CELANDINE_PLANT),
                RarityFilter.onAverageOnceEvery(120), InSquarePlacement.spread(),
                PlacementUtils.HEIGHTMAP, BiomeFilter.biome());

        PlacementUtils.register(context, VERBENA_FLOWER_PLACED, registryEntryLookup.getOrThrow(VERBENA_FLOWER),
                RarityFilter.onAverageOnceEvery(140), InSquarePlacement.spread(),
                PlacementUtils.HEIGHTMAP, BiomeFilter.biome());

        PlacementUtils.register(context, HAN_FIBER_PLACED, registryEntryLookup.getOrThrow(HAN_FIBER_PLANT),
                RarityFilter.onAverageOnceEvery(25), InSquarePlacement.spread(),
                PlacementUtils.HEIGHTMAP, BiomeFilter.biome());

        PlacementUtils.register(context, CROWS_EYE_FERN_PLACED, registryEntryLookup.getOrThrow(CROWS_EYE_FERN),
                RarityFilter.onAverageOnceEvery(10), InSquarePlacement.spread(),
                PlacementUtils.HEIGHTMAP, BiomeFilter.biome());

        PlacementUtils.register(context, ARENARIA_BUSH_PLACED, registryEntryLookup.getOrThrow(ARENARIA_BUSH),
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
        PlacementUtils.register(context, BRYONIA_UNDERGROUND, registryEntryLookup.getOrThrow(BRYONIA_VINE_UNDERGROUND),

                CountPlacement.of(UniformInt.of(80, 120)), PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT, InSquarePlacement.spread(), SurfaceRelativeThresholdFilter.of(Heightmap.Types.OCEAN_FLOOR_WG, Integer.MIN_VALUE, -13), BiomeFilter.biome()
        );

        PlacementUtils.register(context, BRYONIA_SURFACE, registryEntryLookup.getOrThrow(BRYONIA_VINE_SURFACE),
                CountPlacement.of(UniformInt.of(80, 120)), PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT, InSquarePlacement.spread(), SurfaceRelativeThresholdFilter.of(Heightmap.Types.OCEAN_FLOOR_WG, Integer.MIN_VALUE, -13), BiomeFilter.biome()
        );
    }

    @SuppressWarnings({"unchecked", "rawtypes", "all"})
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

    public static void boostrapProcessorList(final BootstrapContext<StructureProcessorList> processorListRegisterable) {

        register(processorListRegisterable, TCOTS_WorldGen.RANDOM_HERBALIST_HERBS_PLAINS,
                ImmutableList.of(
                        //Processor
                        new RuleProcessor(
                                //Rules
                                ImmutableList.of(
                                        //Potted
                                        addPotReplaceable(Blocks.POTTED_DANDELION, 0.3f),
                                        addPotReplaceable(TCOTS_Blocks.PottedCelandineFlower(), 0.3f),
                                        addPotReplaceable(Blocks.POTTED_POPPY, 0.3f),
                                        addPotReplaceable(Blocks.POTTED_AZURE_BLUET, 0.2f),
                                        addPotReplaceable(Blocks.POTTED_FLOWERING_AZALEA, 0.3f),
                                        addPotReplaceable(TCOTS_Blocks.PottedVerbenaFlower(), 0.3f),
                                        addPotReplaceable(Blocks.POTTED_OXEYE_DAISY, 0.3f),
                                        addPotReplaceable(Blocks.POTTED_LILY_OF_THE_VALLEY, 0.2f),
                                        addPotReplaceable(TCOTS_Blocks.PottedSewantMushrooms(), 0.1f),

                                        //FlowerBlock
                                        //Replace Plants
                                        addFlowerReplaceable(Blocks.DANDELION, 0.3f),
                                        addFlowerReplaceable(Blocks.AZURE_BLUET, 0.3f),
                                        addFlowerReplaceable(TCOTS_Blocks.CelandinePlant().defaultBlockState().setValue(CelandinePlant.AGE, 3), 0.3f),
                                        addFlowerReplaceable(Blocks.FLOWERING_AZALEA, 0.2f),
                                        addFlowerReplaceable(TCOTS_Blocks.VerbenaFlower(), 0.2f),
                                        addFlowerReplaceable(TCOTS_Blocks.ArenariaBush().defaultBlockState().setValue(ArenariaBush.AGE, 2), 0.1f),

                                        //Replace Vines
                                        addBlockStateReplaceable(Blocks.VINE.defaultBlockState().setValue(VineBlock.NORTH, true),
                                                TCOTS_Blocks.BryoniaVine().defaultBlockState().setValue(VineBlock.NORTH, true).setValue(BryoniaVine.AGE, 3),
                                                0.3f),

                                        addBlockStateReplaceable(Blocks.VINE.defaultBlockState().setValue(VineBlock.SOUTH, true),
                                                TCOTS_Blocks.BryoniaVine().defaultBlockState().setValue(VineBlock.SOUTH, true).setValue(BryoniaVine.AGE, 3),
                                                0.3f),

                                        addBlockStateReplaceable(Blocks.VINE.defaultBlockState().setValue(VineBlock.WEST, true),
                                                TCOTS_Blocks.BryoniaVine().defaultBlockState().setValue(VineBlock.WEST, true).setValue(BryoniaVine.AGE, 3),
                                                0.3f),

                                        addBlockStateReplaceable(Blocks.VINE.defaultBlockState().setValue(VineBlock.EAST, true),
                                                TCOTS_Blocks.BryoniaVine().defaultBlockState().setValue(VineBlock.EAST, true).setValue(BryoniaVine.AGE, 3),
                                                0.3f),

                                        addBlockStateReplaceable(Blocks.VINE.defaultBlockState().setValue(VineBlock.NORTH, true),
                                                Blocks.GLOW_LICHEN.defaultBlockState().setValue(VineBlock.NORTH, true),
                                                0.1f),

                                        addBlockStateReplaceable(Blocks.VINE.defaultBlockState().setValue(VineBlock.SOUTH, true),
                                                Blocks.GLOW_LICHEN.defaultBlockState().setValue(VineBlock.SOUTH, true),
                                                0.1f),

                                        addBlockStateReplaceable(Blocks.VINE.defaultBlockState().setValue(VineBlock.EAST, true),
                                                Blocks.GLOW_LICHEN.defaultBlockState().setValue(VineBlock.EAST, true),
                                                0.1f),

                                        addBlockStateReplaceable(Blocks.VINE.defaultBlockState().setValue(VineBlock.WEST, true),
                                                Blocks.GLOW_LICHEN.defaultBlockState().setValue(VineBlock.WEST, true),
                                                0.1f),

                                        //Moss
                                        addBlockReplaceable(Blocks.COBBLESTONE, Blocks.MOSSY_COBBLESTONE,0.2f)
                                )
                        )
                )
        );

        register(processorListRegisterable, TCOTS_WorldGen.RANDOM_HERBALIST_HERBS_TAIGA,
                ImmutableList.of(
                        //Processor
                        new RuleProcessor(
                                //Rules
                                ImmutableList.of(
                                        //Potted
                                        addPotReplaceable(Blocks.POTTED_FERN, 0.3f),
                                        addPotReplaceable(Blocks.POTTED_BROWN_MUSHROOM, 0.3f),
                                        addPotReplaceable(Blocks.POTTED_RED_MUSHROOM, 0.2f),
                                        addPotReplaceable(Blocks.POTTED_SPRUCE_SAPLING, 0.2f),
                                        addPotReplaceable(TCOTS_Blocks.PottedPuffballMushroom(), 0.2f),
                                        addPotReplaceable(TCOTS_Blocks.PottedSewantMushrooms(), 0.2f),
                                        addPotReplaceable(Blocks.POTTED_AZURE_BLUET, 0.2f),
                                        addPotReplaceable(Blocks.POTTED_CORNFLOWER, 0.2f),
                                        addPotReplaceable(Blocks.POTTED_ALLIUM, 0.2f),

                                        //FlowerBlock
                                        //Replace Plants
                                        addFlowerReplaceable(Blocks.DANDELION, 0.3f),
                                        addFlowerReplaceable(Blocks.SWEET_BERRY_BUSH.defaultBlockState().setValue(VerbenaFlower.AGE, 2), 0.3f),
                                        addFlowerReplaceable(Blocks.FERN, 0.3f),
                                        addFlowerReplaceable(Blocks.AZURE_BLUET, 0.2f),
                                        addFlowerReplaceable(TCOTS_Blocks.ArenariaBush().defaultBlockState().setValue(ArenariaBush.AGE, 2), 0.3f),

                                        addBlockStateReplaceable(Blocks.FERN.defaultBlockState(),
                                                TCOTS_Blocks.CrowsEyeFern().defaultBlockState().setValue(DoublePlantBlock.HALF, DoubleBlockHalf.LOWER).setValue(CrowsEyeFern.AGE, 2),
                                                0.4f),

                                        addFlowerReplaceable(TCOTS_Blocks.VerbenaFlower().defaultBlockState().setValue(VerbenaFlower.AGE, 3), 0.2f),

                                        addFlowerReplaceable(TCOTS_Blocks.SewantMushroomsPlant().defaultBlockState().setValue(SewantMushroomsPlant.MUSHROOM_AMOUNT, 2)
                                                .setValue(SewantMushroomsPlant.FACING, Direction.NORTH), 0.2f),

                                        addFlowerReplaceable(TCOTS_Blocks.SewantMushroomsPlant().defaultBlockState().setValue(SewantMushroomsPlant.MUSHROOM_AMOUNT, 2)
                                                .setValue(SewantMushroomsPlant.FACING, Direction.SOUTH), 0.2f),


                                        addFlowerReplaceable(Blocks.ALLIUM, 0.2f),

                                        //Replace Vines
                                        addBlockStateReplaceable(Blocks.VINE.defaultBlockState().setValue(VineBlock.NORTH, true),
                                                TCOTS_Blocks.BryoniaVine().defaultBlockState().setValue(VineBlock.NORTH, true).setValue(BryoniaVine.AGE, 3),
                                                0.2f),

                                        addBlockStateReplaceable(Blocks.VINE.defaultBlockState().setValue(VineBlock.SOUTH, true),
                                                TCOTS_Blocks.BryoniaVine().defaultBlockState().setValue(VineBlock.SOUTH, true).setValue(BryoniaVine.AGE, 3),
                                                0.2f),

                                        addBlockStateReplaceable(Blocks.VINE.defaultBlockState().setValue(VineBlock.WEST, true),
                                                TCOTS_Blocks.BryoniaVine().defaultBlockState().setValue(VineBlock.WEST, true).setValue(BryoniaVine.AGE, 3),
                                                0.2f),

                                        addBlockStateReplaceable(Blocks.VINE.defaultBlockState().setValue(VineBlock.EAST, true),
                                                TCOTS_Blocks.BryoniaVine().defaultBlockState().setValue(VineBlock.EAST, true).setValue(BryoniaVine.AGE, 3),
                                                0.2f),

                                        addBlockStateReplaceable(Blocks.VINE.defaultBlockState().setValue(VineBlock.NORTH, true),
                                                Blocks.GLOW_LICHEN.defaultBlockState().setValue(VineBlock.NORTH, true),
                                                0.1f),

                                        addBlockStateReplaceable(Blocks.VINE.defaultBlockState().setValue(VineBlock.SOUTH, true),
                                                Blocks.GLOW_LICHEN.defaultBlockState().setValue(VineBlock.SOUTH, true),
                                                0.1f),

                                        addBlockStateReplaceable(Blocks.VINE.defaultBlockState().setValue(VineBlock.EAST, true),
                                                Blocks.GLOW_LICHEN.defaultBlockState().setValue(VineBlock.EAST, true),
                                                0.1f),

                                        addBlockStateReplaceable(Blocks.VINE.defaultBlockState().setValue(VineBlock.WEST, true),
                                                Blocks.GLOW_LICHEN.defaultBlockState().setValue(VineBlock.WEST, true),
                                                0.1f),

                                        //Moss
                                        addBlockReplaceable(Blocks.COBBLESTONE, Blocks.MOSSY_COBBLESTONE,0.2f),
                                        addBlockReplaceable(Blocks.DIRT, Blocks.DIRT_PATH,1f)

                                )
                        )
                )
        );

        register(processorListRegisterable, TCOTS_WorldGen.RANDOM_HERBALIST_HERBS_SNOWY,
                ImmutableList.of(
                        //Processor
                        new RuleProcessor(
                                //Rules
                                ImmutableList.of(
                                        //Potted
                                        addPotReplaceable(TCOTS_Blocks.PottedBryoniaFlower(), 0.2f),
                                        addPotReplaceable(Blocks.POTTED_BROWN_MUSHROOM, 0.3f),
                                        addPotReplaceable(Blocks.POTTED_RED_MUSHROOM, 0.2f),
                                        addPotReplaceable(Blocks.POTTED_SPRUCE_SAPLING, 0.2f),
                                        addPotReplaceable(TCOTS_Blocks.PottedPuffballMushroom(), 0.2f),
                                        addPotReplaceable(TCOTS_Blocks.PottedVerbenaFlower(), 0.3f),
                                        addPotReplaceable(Blocks.POTTED_AZURE_BLUET, 0.2f),
                                        addPotReplaceable(Blocks.POTTED_OXEYE_DAISY, 0.3f),

                                        //FlowerBlock
                                        //Replace Plants
                                        addFlowerReplaceable(TCOTS_Blocks.ArenariaBush().defaultBlockState().setValue(ArenariaBush.AGE, 2), 0.3f),
                                        addBlockStateReplaceable(Blocks.FERN.defaultBlockState(),
                                                TCOTS_Blocks.CrowsEyeFern().defaultBlockState().setValue(DoublePlantBlock.HALF, DoubleBlockHalf.LOWER).setValue(CrowsEyeFern.AGE, 2),
                                                0.3f),
                                        addFlowerReplaceable(Blocks.SWEET_BERRY_BUSH.defaultBlockState().setValue(VerbenaFlower.AGE, 2), 0.3f),
                                        addFlowerReplaceable(Blocks.BROWN_MUSHROOM, 0.3f),
                                        addFlowerReplaceable(TCOTS_Blocks.PuffballMushroom(), 0.3f),
                                        addFlowerReplaceable(Blocks.AZURE_BLUET, 0.3f),
                                        addFlowerReplaceable(TCOTS_Blocks.VerbenaFlower().defaultBlockState().setValue(VerbenaFlower.AGE, 3), 0.2f),
                                        addFlowerReplaceable(Blocks.DANDELION, 0.2f),

                                        //Replace Vines
                                        addBlockStateReplaceable(Blocks.VINE.defaultBlockState().setValue(VineBlock.NORTH, true),
                                                TCOTS_Blocks.BryoniaVine().defaultBlockState().setValue(VineBlock.NORTH, true).setValue(BryoniaVine.AGE, 3),
                                                0.3f),

                                        addBlockStateReplaceable(Blocks.VINE.defaultBlockState().setValue(VineBlock.SOUTH, true),
                                                TCOTS_Blocks.BryoniaVine().defaultBlockState().setValue(VineBlock.SOUTH, true).setValue(BryoniaVine.AGE, 3),
                                                0.3f),

                                        addBlockStateReplaceable(Blocks.VINE.defaultBlockState().setValue(VineBlock.WEST, true),
                                                TCOTS_Blocks.BryoniaVine().defaultBlockState().setValue(VineBlock.WEST, true).setValue(BryoniaVine.AGE, 3),
                                                0.3f),

                                        addBlockStateReplaceable(Blocks.VINE.defaultBlockState().setValue(VineBlock.EAST, true),
                                                TCOTS_Blocks.BryoniaVine().defaultBlockState().setValue(VineBlock.EAST, true).setValue(BryoniaVine.AGE, 3),
                                                0.3f),

                                        addBlockStateReplaceable(Blocks.VINE.defaultBlockState().setValue(VineBlock.NORTH, true),
                                                Blocks.GLOW_LICHEN.defaultBlockState().setValue(VineBlock.NORTH, true),
                                                0.1f),

                                        addBlockStateReplaceable(Blocks.VINE.defaultBlockState().setValue(VineBlock.SOUTH, true),
                                                Blocks.GLOW_LICHEN.defaultBlockState().setValue(VineBlock.SOUTH, true),
                                                0.1f),

                                        addBlockStateReplaceable(Blocks.VINE.defaultBlockState().setValue(VineBlock.EAST, true),
                                                Blocks.GLOW_LICHEN.defaultBlockState().setValue(VineBlock.EAST, true),
                                                0.1f),

                                        addBlockStateReplaceable(Blocks.VINE.defaultBlockState().setValue(VineBlock.WEST, true),
                                                Blocks.GLOW_LICHEN.defaultBlockState().setValue(VineBlock.WEST, true),
                                                0.1f),

                                        //Moss
                                        addBlockReplaceable(Blocks.COBBLESTONE, Blocks.MOSSY_COBBLESTONE,0.2f)
                                )
                        )
                )
        );

        register(processorListRegisterable, TCOTS_WorldGen.RANDOM_HERBALIST_HERBS_DESERT,
                ImmutableList.of(
                        //Processor
                        new RuleProcessor(
                                //Rules
                                ImmutableList.of(
                                        //Potted
                                        addPotReplaceable(Blocks.POTTED_CACTUS, 0.2f),
                                        addPotReplaceable(Blocks.POTTED_DEAD_BUSH, 0.2f),
                                        addPotReplaceable(TCOTS_Blocks.PottedHanFiber(), 0.3f),
                                        addPotReplaceable(Blocks.POTTED_BROWN_MUSHROOM, 0.2f),
                                        addPotReplaceable(Blocks.POTTED_RED_MUSHROOM, 0.2f),
                                        addPotReplaceable(TCOTS_Blocks.PottedPuffballMushroom(), 0.2f),
                                        addPotReplaceable(Blocks.POTTED_LILY_OF_THE_VALLEY, 0.2f),
                                        addPotReplaceable(TCOTS_Blocks.PottedBryoniaFlower(), 0.2f),


                                        //FlowerBlock
                                        //Replace Plants
                                        addFlowerReplaceable(Blocks.CACTUS, 0.3f),
                                        addFlowerReplaceable(Blocks.DEAD_BUSH, 0.3f),
                                        addFlowerReplaceable(TCOTS_Blocks.HanFiberPlant().defaultBlockState().setValue(HanFiberPlant.AGE, 3), 0.2f),
                                        addFlowerReplaceable(Blocks.AZURE_BLUET, 0.3f),
                                        addFlowerReplaceable(Blocks.LILY_OF_THE_VALLEY, 0.2f),
                                        addFlowerReplaceable(TCOTS_Blocks.VerbenaFlower().defaultBlockState().setValue(VerbenaFlower.AGE, 3), 0.2f),

                                        //Replace Vines
                                        addBlockStateReplaceable(Blocks.VINE.defaultBlockState().setValue(VineBlock.NORTH, true),
                                                TCOTS_Blocks.BryoniaVine().defaultBlockState().setValue(VineBlock.NORTH, true).setValue(BryoniaVine.AGE, 3),
                                                0.3f),

                                        addBlockStateReplaceable(Blocks.VINE.defaultBlockState().setValue(VineBlock.SOUTH, true),
                                                TCOTS_Blocks.BryoniaVine().defaultBlockState().setValue(VineBlock.SOUTH, true).setValue(BryoniaVine.AGE, 3),
                                                0.3f),

                                        addBlockStateReplaceable(Blocks.VINE.defaultBlockState().setValue(VineBlock.WEST, true),
                                                TCOTS_Blocks.BryoniaVine().defaultBlockState().setValue(VineBlock.WEST, true).setValue(BryoniaVine.AGE, 3),
                                                0.3f),

                                        addBlockStateReplaceable(Blocks.VINE.defaultBlockState().setValue(VineBlock.EAST, true),
                                                TCOTS_Blocks.BryoniaVine().defaultBlockState().setValue(VineBlock.EAST, true).setValue(BryoniaVine.AGE, 3),
                                                0.3f),

                                        addBlockStateReplaceable(Blocks.VINE.defaultBlockState().setValue(VineBlock.NORTH, true),
                                                Blocks.GLOW_LICHEN.defaultBlockState().setValue(VineBlock.NORTH, true),
                                                0.1f),

                                        addBlockStateReplaceable(Blocks.VINE.defaultBlockState().setValue(VineBlock.SOUTH, true),
                                                Blocks.GLOW_LICHEN.defaultBlockState().setValue(VineBlock.SOUTH, true),
                                                0.1f),

                                        addBlockStateReplaceable(Blocks.VINE.defaultBlockState().setValue(VineBlock.EAST, true),
                                                Blocks.GLOW_LICHEN.defaultBlockState().setValue(VineBlock.EAST, true),
                                                0.1f),

                                        addBlockStateReplaceable(Blocks.VINE.defaultBlockState().setValue(VineBlock.WEST, true),
                                                Blocks.GLOW_LICHEN.defaultBlockState().setValue(VineBlock.WEST, true),
                                                0.1f),

                                        //Moss
                                        addBlockReplaceable(Blocks.COBBLESTONE, Blocks.MOSSY_COBBLESTONE,0.2f)
                                )
                        )
                )
        );

        register(processorListRegisterable, TCOTS_WorldGen.RANDOM_HERBALIST_HERBS_SAVANNA,
                ImmutableList.of(
                        //Processor
                        new RuleProcessor(
                                //Rules
                                ImmutableList.of(

                                        addPotReplaceable(Blocks.POTTED_DANDELION, 0.2f),
                                        addPotReplaceable(TCOTS_Blocks.PottedHanFiber(), 0.3f),
                                        addPotReplaceable(Blocks.POTTED_ACACIA_SAPLING, 0.2f),
                                        addPotReplaceable(Blocks.POTTED_BROWN_MUSHROOM, 0.2f),
                                        addPotReplaceable(Blocks.POTTED_RED_MUSHROOM, 0.2f),
                                        addPotReplaceable(Blocks.POTTED_AZURE_BLUET, 0.2f),
                                        addPotReplaceable(Blocks.POTTED_LILY_OF_THE_VALLEY, 0.2f),
                                        addPotReplaceable(TCOTS_Blocks.PottedBryoniaFlower(), 0.2f),


                                        //FlowerBlock
                                        //Replace Plants

                                        addFlowerReplaceable(TCOTS_Blocks.HanFiberPlant().defaultBlockState().setValue(HanFiberPlant.AGE, 3), 0.2f),
                                        addFlowerReplaceable(Blocks.SHORT_GRASS, 0.3f),
                                        addFlowerReplaceable(Blocks.DANDELION, 0.3f),
                                        addFlowerReplaceable(Blocks.ACACIA_SAPLING, 0.3f),
                                        addFlowerReplaceable(Blocks.MOSS_CARPET, 0.2f),
                                        addFlowerReplaceable(Blocks.AZURE_BLUET, 0.3f),
                                        addFlowerReplaceable(Blocks.LILY_OF_THE_VALLEY, 0.2f),

                                        //Replace Vines
                                        addBlockStateReplaceable(Blocks.VINE.defaultBlockState().setValue(VineBlock.NORTH, true),
                                                TCOTS_Blocks.BryoniaVine().defaultBlockState().setValue(VineBlock.NORTH, true).setValue(BryoniaVine.AGE, 3),
                                                0.2f),

                                        addBlockStateReplaceable(Blocks.VINE.defaultBlockState().setValue(VineBlock.SOUTH, true),
                                                TCOTS_Blocks.BryoniaVine().defaultBlockState().setValue(VineBlock.SOUTH, true).setValue(BryoniaVine.AGE, 3),
                                                0.2f),

                                        addBlockStateReplaceable(Blocks.VINE.defaultBlockState().setValue(VineBlock.WEST, true),
                                                TCOTS_Blocks.BryoniaVine().defaultBlockState().setValue(VineBlock.WEST, true).setValue(BryoniaVine.AGE, 3),
                                                0.2f),

                                        addBlockStateReplaceable(Blocks.VINE.defaultBlockState().setValue(VineBlock.EAST, true),
                                                TCOTS_Blocks.BryoniaVine().defaultBlockState().setValue(VineBlock.EAST, true).setValue(BryoniaVine.AGE, 3),
                                                0.2f),

                                        addBlockStateReplaceable(Blocks.VINE.defaultBlockState().setValue(VineBlock.NORTH, true),
                                                Blocks.GLOW_LICHEN.defaultBlockState().setValue(VineBlock.NORTH, true),
                                                0.1f),

                                        addBlockStateReplaceable(Blocks.VINE.defaultBlockState().setValue(VineBlock.SOUTH, true),
                                                Blocks.GLOW_LICHEN.defaultBlockState().setValue(VineBlock.SOUTH, true),
                                                0.1f),

                                        addBlockStateReplaceable(Blocks.VINE.defaultBlockState().setValue(VineBlock.EAST, true),
                                                Blocks.GLOW_LICHEN.defaultBlockState().setValue(VineBlock.EAST, true),
                                                0.1f),

                                        addBlockStateReplaceable(Blocks.VINE.defaultBlockState().setValue(VineBlock.WEST, true),
                                                Blocks.GLOW_LICHEN.defaultBlockState().setValue(VineBlock.WEST, true),
                                                0.1f),

                                        //Moss
                                        addBlockReplaceable(Blocks.COBBLESTONE, Blocks.MOSSY_COBBLESTONE,0.2f)
                                )
                        )
                )
        );


        register(processorListRegisterable, TCOTS_WorldGen.RANDOM_TROLL_CAVE,
                ImmutableList.of(
                        //Processor
                        new RuleProcessor(
                                //Rules
                                ImmutableList.of(
                                        //Moss
                                        addBlockReplaceable(Blocks.ANDESITE, Blocks.TUFF,0.2f),
                                        addBlockReplaceable(Blocks.ANDESITE, Blocks.IRON_ORE,0.2f),

                                        addBlockReplaceable(Blocks.IRON_ORE, Blocks.COAL_ORE,0.2f),
                                        addBlockReplaceable(Blocks.IRON_ORE, Blocks.GOLD_ORE,0.2f),
                                        addBlockReplaceable(Blocks.IRON_ORE, Blocks.COPPER_ORE,0.2f)

                                )
                        )
                )
        );

    }

    private static ProcessorRule addPotReplaceable(final Block block, final float probability){
        return addBlockReplaceable(Blocks.FLOWER_POT, block, probability);
    }

    private static ProcessorRule addBlockStateReplaceable(final BlockState stateOriginal, final BlockState stateReplace, final float probability){
        return new ProcessorRule(
                new RandomBlockStateMatchTest(stateOriginal, probability),
                AlwaysTrueTest.INSTANCE,
                stateReplace
        );
    }

    private static ProcessorRule addFlowerReplaceable(final Block block, final float probability){
        return addBlockReplaceable(Blocks.POPPY, block, probability);
    }

    private static ProcessorRule addFlowerReplaceable(final BlockState state, final float probability){
        return addBlockReplaceable(state, probability);
    }

    private static ProcessorRule addBlockReplaceable(final Block replace, final Block block, final float probability){
        return new ProcessorRule(
                new RandomBlockMatchTest(replace, probability),
                AlwaysTrueTest.INSTANCE,
                block.defaultBlockState()
        );
    }

    private static ProcessorRule addBlockReplaceable(final BlockState state, final float probability){
        return new ProcessorRule(
                new RandomBlockMatchTest(Blocks.POPPY, probability),
                AlwaysTrueTest.INSTANCE,
                state
        );
    }

    private static void register(final BootstrapContext<StructureProcessorList> processorListRegisterable, final ResourceKey<StructureProcessorList> key, final List<StructureProcessor> processors) {
        processorListRegisterable.register(key, new StructureProcessorList(processors));
    }
}
