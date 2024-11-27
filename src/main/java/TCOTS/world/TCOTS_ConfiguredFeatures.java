package TCOTS.world;

import TCOTS.TCOTS_Main;
import TCOTS.blocks.TCOTS_Blocks;
import TCOTS.blocks.plants.*;
import TCOTS.world.gen.BryoniaPatchFeatureConfig;
import net.minecraft.block.Blocks;
import net.minecraft.block.MultifaceGrowthBlock;
import net.minecraft.block.MushroomBlock;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;

public class TCOTS_ConfiguredFeatures {
    public static final RegistryKey<ConfiguredFeature<?, ?>> HUGE_PUFFBALL_MUSHROOM = registerKey("huge_puffball_mushroom");
    public static final RegistryKey<ConfiguredFeature<?, ?>> HUGE_SEWANT_MUSHROOMS = registerKey("huge_sewant_mushrooms");

    //Flowers
    public static RegistryKey<ConfiguredFeature<?, ?>> CELANDINE_PLANT = registerKey("celandine_patch");
    public static RegistryKey<ConfiguredFeature<?, ?>> VERBENA_FLOWER = registerKey("verbena_patch");
    public static RegistryKey<ConfiguredFeature<?, ?>> HAN_FIBER_PLANT = registerKey("han_fiber_patch");

    //Fern
    public static RegistryKey<ConfiguredFeature<?, ?>> CROWS_EYE_FERN = registerKey("crows_eye_patch");
    //Sweet Berries
    public static RegistryKey<ConfiguredFeature<?, ?>> ARENARIA_BUSH = registerKey("arenaria_patch");


    //Others
    public static RegistryKey<ConfiguredFeature<?, ?>> PUFFBALL_MUSHROOM = registerKey("puffball_patch");
    public static RegistryKey<ConfiguredFeature<?, ?>> SEWANT_MUSHROOMS = registerKey("sewant_mushrooms_patch");
    public static RegistryKey<ConfiguredFeature<?, ?>> BRYONIA_VINE_UNDERGROUND = registerKey("bryonia_patch_underground");
    public static RegistryKey<ConfiguredFeature<?, ?>> BRYONIA_VINE_SURFACE = registerKey("bryonia_patch_surface");



    public static void boostrap(Registerable<ConfiguredFeature<?, ?>> context) {

        TCOTS_ConfiguredFeatures.register(context, HUGE_PUFFBALL_MUSHROOM, TCOTS_Features.HUGE_PUFFBALL_MUSHROOM, new HugeMushroomFeatureConfig(
                BlockStateProvider.of(TCOTS_Blocks.PUFFBALL_MUSHROOM_BLOCK.getDefaultState().with(MushroomBlock.DOWN, false)),
                BlockStateProvider.of((Blocks.MUSHROOM_STEM.getDefaultState().with(MushroomBlock.UP, false)).with(MushroomBlock.DOWN, false)),
                3
                ));

        TCOTS_ConfiguredFeatures.register(context, HUGE_SEWANT_MUSHROOMS, TCOTS_Features.HUGE_SEWANT_MUSHROOMS, new HugeMushroomFeatureConfig(
                BlockStateProvider.of(TCOTS_Blocks.SEWANT_MUSHROOM_BLOCK.getDefaultState().with(MushroomBlock.DOWN, false)),
                BlockStateProvider.of((TCOTS_Blocks.SEWANT_MUSHROOM_STEM.getDefaultState().with(MushroomBlock.UP, false)).with(MushroomBlock.DOWN, false)),
                2
        ));


        //Size of a flower patch
        TCOTS_ConfiguredFeatures.register(context, CELANDINE_PLANT, Feature.FLOWER,
                new RandomPatchFeatureConfig(12,7,3, PlacedFeatures.createEntry(Feature.SIMPLE_BLOCK,
                        new SimpleBlockFeatureConfig(BlockStateProvider.of(TCOTS_Blocks.CELANDINE_PLANT.getDefaultState().with(CelandinePlant.AGE, 3))))));

        TCOTS_ConfiguredFeatures.register(context, VERBENA_FLOWER, Feature.FLOWER,
                new RandomPatchFeatureConfig(20,7,3, PlacedFeatures.createEntry(Feature.SIMPLE_BLOCK,
                        new SimpleBlockFeatureConfig(BlockStateProvider.of(TCOTS_Blocks.VERBENA_FLOWER.getDefaultState().with(VerbenaFlower.AGE, 3))))));

        TCOTS_ConfiguredFeatures.register(context, HAN_FIBER_PLANT, Feature.FLOWER,
                new RandomPatchFeatureConfig(50,4,3, PlacedFeatures.createEntry(Feature.SIMPLE_BLOCK,
                        new SimpleBlockFeatureConfig(BlockStateProvider.of(TCOTS_Blocks.HAN_FIBER_PLANT.getDefaultState().with(HanFiberPlant.AGE, 3))))));

        TCOTS_ConfiguredFeatures.register(context, CROWS_EYE_FERN, Feature.RANDOM_PATCH,
                new RandomPatchFeatureConfig(40,7,3, PlacedFeatures.createEntry(Feature.SIMPLE_BLOCK,
                        new SimpleBlockFeatureConfig(BlockStateProvider.of(TCOTS_Blocks.CROWS_EYE_FERN.getDefaultState().with(CrowsEyeFern.AGE, 4))))));

        TCOTS_ConfiguredFeatures.register(context, ARENARIA_BUSH, Feature.RANDOM_PATCH,
                new RandomPatchFeatureConfig(30,7,3, PlacedFeatures.createEntry(Feature.SIMPLE_BLOCK,
                        new SimpleBlockFeatureConfig(BlockStateProvider.of(TCOTS_Blocks.ARENARIA_BUSH.getDefaultState().with(ArenariaBush.AGE, 1))))));

        TCOTS_ConfiguredFeatures.register(context, PUFFBALL_MUSHROOM, Feature.RANDOM_PATCH,
                new RandomPatchFeatureConfig(96,7,3, PlacedFeatures.createEntry(Feature.SIMPLE_BLOCK,
                        new SimpleBlockFeatureConfig(BlockStateProvider.of(TCOTS_Blocks.PUFFBALL_MUSHROOM.getDefaultState())))));

        TCOTS_ConfiguredFeatures.register(context, SEWANT_MUSHROOMS, Feature.RANDOM_PATCH,
                new RandomPatchFeatureConfig(96,7,3, PlacedFeatures.createEntry(Feature.SIMPLE_BLOCK,
                        new SimpleBlockFeatureConfig(BlockStateProvider.of(TCOTS_Blocks.SEWANT_MUSHROOMS_PLANT.getDefaultState()
                                .with(SewantMushroomsPlant.MUSHROOM_AMOUNT, 3)
                                .with(SewantMushroomsPlant.FACING, Direction.NORTH))))));


        TCOTS_ConfiguredFeatures.register(context, BRYONIA_VINE_UNDERGROUND,
                TCOTS_Features.BRYONIA_PATCH_FEATURE,
                new BryoniaPatchFeatureConfig(
                ((MultifaceGrowthBlock)TCOTS_Blocks.BRYONIA_VINE),
                        20,
                        false, false, true,
                        20,
                        50
                ));

        TCOTS_ConfiguredFeatures.register(context, BRYONIA_VINE_SURFACE,
                TCOTS_Features.BRYONIA_PATCH_FEATURE,
                new BryoniaPatchFeatureConfig(
                        ((MultifaceGrowthBlock)TCOTS_Blocks.BRYONIA_VINE),
                        20,
                        false, false, true,
                        50,
                        200
                ));

    }

    public static RegistryKey<ConfiguredFeature<?, ?>> registerKey(String name) {
        return RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, Identifier.of(TCOTS_Main.MOD_ID, name));
    }

    private static <FC extends FeatureConfig, F extends Feature<FC>> void register(Registerable<ConfiguredFeature<?, ?>> context, RegistryKey<ConfiguredFeature<?, ?>> key, F feature, FC configuration) {
        context.register(key, new ConfiguredFeature<>(feature, configuration));
    }
}
