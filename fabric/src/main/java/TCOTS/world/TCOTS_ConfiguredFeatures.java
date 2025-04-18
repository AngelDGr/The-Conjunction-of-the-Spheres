package TCOTS.world;

import TCOTS.TCOTS_Main;
import TCOTS.blocks.TCOTS_Blocks_Fabric;
import TCOTS.blocks.plants.*;
import TCOTS.world.gen.BryoniaPatchFeatureConfig;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.HugeMushroomBlock;
import net.minecraft.world.level.block.MultifaceBlock;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.HugeMushroomFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

public class TCOTS_ConfiguredFeatures {
    public static final ResourceKey<ConfiguredFeature<?, ?>> HUGE_PUFFBALL_MUSHROOM = registerKey("huge_puffball_mushroom");
    public static final ResourceKey<ConfiguredFeature<?, ?>> HUGE_SEWANT_MUSHROOMS = registerKey("huge_sewant_mushrooms");

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



    public static void boostrap(BootstrapContext<ConfiguredFeature<?, ?>> context) {

        TCOTS_ConfiguredFeatures.register(context, HUGE_PUFFBALL_MUSHROOM, TCOTS_Features.HUGE_PUFFBALL_MUSHROOM, new HugeMushroomFeatureConfiguration(
                BlockStateProvider.simple(TCOTS_Blocks_Fabric.PUFFBALL_MUSHROOM_BLOCK.defaultBlockState().setValue(HugeMushroomBlock.DOWN, false)),
                BlockStateProvider.simple((Blocks.MUSHROOM_STEM.defaultBlockState().setValue(HugeMushroomBlock.UP, false)).setValue(HugeMushroomBlock.DOWN, false)),
                3
                ));

        TCOTS_ConfiguredFeatures.register(context, HUGE_SEWANT_MUSHROOMS, TCOTS_Features.HUGE_SEWANT_MUSHROOMS, new HugeMushroomFeatureConfiguration(
                BlockStateProvider.simple(TCOTS_Blocks_Fabric.SEWANT_MUSHROOM_BLOCK.defaultBlockState().setValue(HugeMushroomBlock.DOWN, false)),
                BlockStateProvider.simple((TCOTS_Blocks_Fabric.SEWANT_MUSHROOM_STEM.defaultBlockState().setValue(HugeMushroomBlock.UP, false)).setValue(HugeMushroomBlock.DOWN, false)),
                2
        ));


        //Size of a flower patch
        TCOTS_ConfiguredFeatures.register(context, CELANDINE_PLANT, Feature.FLOWER,
                new RandomPatchConfiguration(12,7,3, PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK,
                        new SimpleBlockConfiguration(BlockStateProvider.simple(TCOTS_Blocks_Fabric.CELANDINE_PLANT.defaultBlockState().setValue(CelandinePlant.AGE, 3))))));

        TCOTS_ConfiguredFeatures.register(context, VERBENA_FLOWER, Feature.FLOWER,
                new RandomPatchConfiguration(20,7,3, PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK,
                        new SimpleBlockConfiguration(BlockStateProvider.simple(TCOTS_Blocks_Fabric.VERBENA_FLOWER.defaultBlockState().setValue(VerbenaFlower.AGE, 3))))));

        TCOTS_ConfiguredFeatures.register(context, HAN_FIBER_PLANT, Feature.FLOWER,
                new RandomPatchConfiguration(50,4,3, PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK,
                        new SimpleBlockConfiguration(BlockStateProvider.simple(TCOTS_Blocks_Fabric.HAN_FIBER_PLANT.defaultBlockState().setValue(HanFiberPlant.AGE, 3))))));

        TCOTS_ConfiguredFeatures.register(context, CROWS_EYE_FERN, Feature.RANDOM_PATCH,
                new RandomPatchConfiguration(40,7,3, PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK,
                        new SimpleBlockConfiguration(BlockStateProvider.simple(TCOTS_Blocks_Fabric.CROWS_EYE_FERN.defaultBlockState().setValue(CrowsEyeFern.AGE, 4))))));

        TCOTS_ConfiguredFeatures.register(context, ARENARIA_BUSH, Feature.RANDOM_PATCH,
                new RandomPatchConfiguration(30,7,3, PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK,
                        new SimpleBlockConfiguration(BlockStateProvider.simple(TCOTS_Blocks_Fabric.ARENARIA_BUSH.defaultBlockState().setValue(ArenariaBush.AGE, 1))))));

        TCOTS_ConfiguredFeatures.register(context, PUFFBALL_MUSHROOM, Feature.RANDOM_PATCH,
                new RandomPatchConfiguration(96,7,3, PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK,
                        new SimpleBlockConfiguration(BlockStateProvider.simple(TCOTS_Blocks_Fabric.PUFFBALL_MUSHROOM.defaultBlockState())))));

        TCOTS_ConfiguredFeatures.register(context, SEWANT_MUSHROOMS, Feature.RANDOM_PATCH,
                new RandomPatchConfiguration(96,7,3, PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK,
                        new SimpleBlockConfiguration(BlockStateProvider.simple(TCOTS_Blocks_Fabric.SEWANT_MUSHROOMS_PLANT.defaultBlockState()
                                .setValue(SewantMushroomsPlant.MUSHROOM_AMOUNT, 3)
                                .setValue(SewantMushroomsPlant.FACING, Direction.NORTH))))));


        TCOTS_ConfiguredFeatures.register(context, BRYONIA_VINE_UNDERGROUND,
                TCOTS_Features.BRYONIA_PATCH_FEATURE,
                new BryoniaPatchFeatureConfig(
                ((MultifaceBlock) TCOTS_Blocks_Fabric.BRYONIA_VINE),
                        20,
                        false, false, true,
                        20,
                        50
                ));

        TCOTS_ConfiguredFeatures.register(context, BRYONIA_VINE_SURFACE,
                TCOTS_Features.BRYONIA_PATCH_FEATURE,
                new BryoniaPatchFeatureConfig(
                        ((MultifaceBlock) TCOTS_Blocks_Fabric.BRYONIA_VINE),
                        20,
                        false, false, true,
                        50,
                        200
                ));

    }

    public static ResourceKey<ConfiguredFeature<?, ?>> registerKey(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, name));
    }

    private static <FC extends FeatureConfiguration, F extends Feature<FC>> void register(BootstrapContext<ConfiguredFeature<?, ?>> context, ResourceKey<ConfiguredFeature<?, ?>> key, F feature, FC configuration) {
        context.register(key, new ConfiguredFeature<>(feature, configuration));
    }
}
