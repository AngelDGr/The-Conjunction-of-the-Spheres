package TCOTS.world;

import TCOTS.TCOTS_Main;
import TCOTS.blocks.TCOTS_Blocks_Fabric;
import TCOTS.blocks.plants.*;
import com.google.common.collect.ImmutableList;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.VineBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.levelgen.structure.templatesystem.AlwaysTrueTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.ProcessorRule;
import net.minecraft.world.level.levelgen.structure.templatesystem.RandomBlockMatchTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.RandomBlockStateMatchTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorList;
import java.util.List;

public class TCOTS_ProcessorList {
    public static final ResourceKey<StructureProcessorList> RANDOM_TROLL_CAVE = registerKey("random_troll_cave");

    public static final ResourceKey<StructureProcessorList> RANDOM_HERBALIST_HERBS_PLAINS = registerKey("random_herbalist_herbs_plains");

    public static final ResourceKey<StructureProcessorList> RANDOM_HERBALIST_HERBS_TAIGA = registerKey("random_herbalist_herbs_taiga");

    public static final ResourceKey<StructureProcessorList> RANDOM_HERBALIST_HERBS_SNOWY = registerKey("random_herbalist_herbs_snowy");

    public static final ResourceKey<StructureProcessorList> RANDOM_HERBALIST_HERBS_SAVANNA = registerKey("random_herbalist_herbs_savanna");

    public static final ResourceKey<StructureProcessorList> RANDOM_HERBALIST_HERBS_DESERT = registerKey("random_herbalist_herbs_desert");

    public static ResourceKey<StructureProcessorList> registerKey(String name) {
        return ResourceKey.create(Registries.PROCESSOR_LIST, ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, name));
    }

    public static void boostrap(BootstrapContext<StructureProcessorList> processorListRegisterable) {


        register(processorListRegisterable, RANDOM_HERBALIST_HERBS_PLAINS,
                ImmutableList.of(
                        //Processor
                        new RuleProcessor(
                                //Rules
                                ImmutableList.of(
                                //Potted
                                        addPotReplaceable(Blocks.POTTED_DANDELION, 0.3f),
                                        addPotReplaceable(TCOTS_Blocks_Fabric.POTTED_CELANDINE_FLOWER, 0.3f),
                                        addPotReplaceable(Blocks.POTTED_POPPY, 0.3f),
                                        addPotReplaceable(Blocks.POTTED_AZURE_BLUET, 0.2f),
                                        addPotReplaceable(Blocks.POTTED_FLOWERING_AZALEA, 0.3f),
                                        addPotReplaceable(TCOTS_Blocks_Fabric.POTTED_VERBENA_FLOWER, 0.3f),
                                        addPotReplaceable(Blocks.POTTED_OXEYE_DAISY, 0.3f),
                                        addPotReplaceable(Blocks.POTTED_LILY_OF_THE_VALLEY, 0.2f),
                                        addPotReplaceable(TCOTS_Blocks_Fabric.POTTED_SEWANT_MUSHROOMS, 0.1f),

                                        //FlowerBlock
                                        //Replace Plants
                                        addFlowerReplaceable(Blocks.DANDELION, 0.3f),
                                        addFlowerReplaceable(Blocks.AZURE_BLUET, 0.3f),
                                        addFlowerReplaceable(TCOTS_Blocks_Fabric.CELANDINE_PLANT.defaultBlockState().setValue(CelandinePlant.AGE, 3), 0.3f),
                                        addFlowerReplaceable(Blocks.FLOWERING_AZALEA, 0.2f),
                                        addFlowerReplaceable(TCOTS_Blocks_Fabric.VERBENA_FLOWER.defaultBlockState().setValue(VerbenaFlower.AGE, 3), 0.2f),
                                        addFlowerReplaceable(Blocks.LILY_OF_THE_VALLEY, 0.2f),
                                        addFlowerReplaceable(TCOTS_Blocks_Fabric.ARENARIA_BUSH.defaultBlockState().setValue(ArenariaBush.AGE, 2), 0.1f),

                                        //Replace Vines
                                        addBlockStateReplaceable(Blocks.VINE.defaultBlockState().setValue(VineBlock.NORTH, true),
                                                TCOTS_Blocks_Fabric.BRYONIA_VINE.defaultBlockState().setValue(VineBlock.NORTH, true).setValue(BryoniaVine.AGE, 3),
                                                0.3f),

                                        addBlockStateReplaceable(Blocks.VINE.defaultBlockState().setValue(VineBlock.SOUTH, true),
                                                TCOTS_Blocks_Fabric.BRYONIA_VINE.defaultBlockState().setValue(VineBlock.SOUTH, true).setValue(BryoniaVine.AGE, 3),
                                                0.3f),

                                        addBlockStateReplaceable(Blocks.VINE.defaultBlockState().setValue(VineBlock.WEST, true),
                                                TCOTS_Blocks_Fabric.BRYONIA_VINE.defaultBlockState().setValue(VineBlock.WEST, true).setValue(BryoniaVine.AGE, 3),
                                                0.3f),

                                        addBlockStateReplaceable(Blocks.VINE.defaultBlockState().setValue(VineBlock.EAST, true),
                                                TCOTS_Blocks_Fabric.BRYONIA_VINE.defaultBlockState().setValue(VineBlock.EAST, true).setValue(BryoniaVine.AGE, 3),
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

        register(processorListRegisterable, RANDOM_HERBALIST_HERBS_TAIGA,
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
                                        addPotReplaceable(TCOTS_Blocks_Fabric.POTTED_PUFFBALL_MUSHROOM, 0.2f),
                                        addPotReplaceable(TCOTS_Blocks_Fabric.POTTED_SEWANT_MUSHROOMS, 0.2f),
                                        addPotReplaceable(Blocks.POTTED_AZURE_BLUET, 0.2f),
                                        addPotReplaceable(Blocks.POTTED_CORNFLOWER, 0.2f),
                                        addPotReplaceable(Blocks.POTTED_ALLIUM, 0.2f),

                                        //FlowerBlock
                                        //Replace Plants
                                        addFlowerReplaceable(Blocks.DANDELION, 0.3f),
                                        addFlowerReplaceable(Blocks.SWEET_BERRY_BUSH.defaultBlockState().setValue(VerbenaFlower.AGE, 2), 0.3f),
                                        addFlowerReplaceable(Blocks.FERN, 0.3f),
                                        addFlowerReplaceable(Blocks.AZURE_BLUET, 0.2f),
                                        addFlowerReplaceable(TCOTS_Blocks_Fabric.ARENARIA_BUSH.defaultBlockState().setValue(ArenariaBush.AGE, 2), 0.3f),

                                        addBlockStateReplaceable(Blocks.FERN.defaultBlockState(),
                                                TCOTS_Blocks_Fabric.CROWS_EYE_FERN.defaultBlockState().setValue(DoublePlantBlock.HALF, DoubleBlockHalf.LOWER).setValue(CrowsEyeFern.AGE, 2),
                                                0.4f),

                                        addFlowerReplaceable(TCOTS_Blocks_Fabric.VERBENA_FLOWER.defaultBlockState().setValue(VerbenaFlower.AGE, 3), 0.2f),

                                        addFlowerReplaceable(TCOTS_Blocks_Fabric.SEWANT_MUSHROOMS_PLANT.defaultBlockState().setValue(SewantMushroomsPlant.MUSHROOM_AMOUNT, 2)
                                                .setValue(SewantMushroomsPlant.FACING, Direction.NORTH), 0.2f),

                                        addFlowerReplaceable(TCOTS_Blocks_Fabric.SEWANT_MUSHROOMS_PLANT.defaultBlockState().setValue(SewantMushroomsPlant.MUSHROOM_AMOUNT, 2)
                                                .setValue(SewantMushroomsPlant.FACING, Direction.SOUTH), 0.2f),


                                        addFlowerReplaceable(Blocks.ALLIUM, 0.2f),

                                        //Replace Vines
                                        addBlockStateReplaceable(Blocks.VINE.defaultBlockState().setValue(VineBlock.NORTH, true),
                                                TCOTS_Blocks_Fabric.BRYONIA_VINE.defaultBlockState().setValue(VineBlock.NORTH, true).setValue(BryoniaVine.AGE, 3),
                                                0.2f),

                                        addBlockStateReplaceable(Blocks.VINE.defaultBlockState().setValue(VineBlock.SOUTH, true),
                                                TCOTS_Blocks_Fabric.BRYONIA_VINE.defaultBlockState().setValue(VineBlock.SOUTH, true).setValue(BryoniaVine.AGE, 3),
                                                0.2f),

                                        addBlockStateReplaceable(Blocks.VINE.defaultBlockState().setValue(VineBlock.WEST, true),
                                                TCOTS_Blocks_Fabric.BRYONIA_VINE.defaultBlockState().setValue(VineBlock.WEST, true).setValue(BryoniaVine.AGE, 3),
                                                0.2f),

                                        addBlockStateReplaceable(Blocks.VINE.defaultBlockState().setValue(VineBlock.EAST, true),
                                                TCOTS_Blocks_Fabric.BRYONIA_VINE.defaultBlockState().setValue(VineBlock.EAST, true).setValue(BryoniaVine.AGE, 3),
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

        register(processorListRegisterable, RANDOM_HERBALIST_HERBS_SNOWY,
                ImmutableList.of(
                        //Processor
                        new RuleProcessor(
                                //Rules
                                ImmutableList.of(
                                        //Potted
                                        addPotReplaceable(TCOTS_Blocks_Fabric.POTTED_BRYONIA_FLOWER, 0.2f),
                                        addPotReplaceable(Blocks.POTTED_BROWN_MUSHROOM, 0.3f),
                                        addPotReplaceable(Blocks.POTTED_RED_MUSHROOM, 0.2f),
                                        addPotReplaceable(Blocks.POTTED_SPRUCE_SAPLING, 0.2f),
                                        addPotReplaceable(TCOTS_Blocks_Fabric.POTTED_PUFFBALL_MUSHROOM, 0.2f),
                                        addPotReplaceable(TCOTS_Blocks_Fabric.POTTED_VERBENA_FLOWER, 0.3f),
                                        addPotReplaceable(Blocks.POTTED_AZURE_BLUET, 0.2f),
                                        addPotReplaceable(Blocks.POTTED_OXEYE_DAISY, 0.3f),

                                        //FlowerBlock
                                        //Replace Plants
                                        addFlowerReplaceable(TCOTS_Blocks_Fabric.ARENARIA_BUSH.defaultBlockState().setValue(ArenariaBush.AGE, 2), 0.3f),
                                        addBlockStateReplaceable(Blocks.FERN.defaultBlockState(),
                                                TCOTS_Blocks_Fabric.CROWS_EYE_FERN.defaultBlockState().setValue(DoublePlantBlock.HALF, DoubleBlockHalf.LOWER).setValue(CrowsEyeFern.AGE, 2),
                                                0.3f),
                                        addFlowerReplaceable(Blocks.SWEET_BERRY_BUSH.defaultBlockState().setValue(VerbenaFlower.AGE, 2), 0.3f),
                                        addFlowerReplaceable(Blocks.BROWN_MUSHROOM, 0.3f),
                                        addFlowerReplaceable(TCOTS_Blocks_Fabric.PUFFBALL_MUSHROOM, 0.3f),
                                        addFlowerReplaceable(Blocks.AZURE_BLUET, 0.3f),
                                        addFlowerReplaceable(TCOTS_Blocks_Fabric.VERBENA_FLOWER.defaultBlockState().setValue(VerbenaFlower.AGE, 3), 0.2f),
                                        addFlowerReplaceable(Blocks.DANDELION, 0.2f),

                                        //Replace Vines
                                        addBlockStateReplaceable(Blocks.VINE.defaultBlockState().setValue(VineBlock.NORTH, true),
                                                TCOTS_Blocks_Fabric.BRYONIA_VINE.defaultBlockState().setValue(VineBlock.NORTH, true).setValue(BryoniaVine.AGE, 3),
                                                0.3f),

                                        addBlockStateReplaceable(Blocks.VINE.defaultBlockState().setValue(VineBlock.SOUTH, true),
                                                TCOTS_Blocks_Fabric.BRYONIA_VINE.defaultBlockState().setValue(VineBlock.SOUTH, true).setValue(BryoniaVine.AGE, 3),
                                                0.3f),

                                        addBlockStateReplaceable(Blocks.VINE.defaultBlockState().setValue(VineBlock.WEST, true),
                                                TCOTS_Blocks_Fabric.BRYONIA_VINE.defaultBlockState().setValue(VineBlock.WEST, true).setValue(BryoniaVine.AGE, 3),
                                                0.3f),

                                        addBlockStateReplaceable(Blocks.VINE.defaultBlockState().setValue(VineBlock.EAST, true),
                                                TCOTS_Blocks_Fabric.BRYONIA_VINE.defaultBlockState().setValue(VineBlock.EAST, true).setValue(BryoniaVine.AGE, 3),
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

        register(processorListRegisterable, RANDOM_HERBALIST_HERBS_DESERT,
                ImmutableList.of(
                        //Processor
                        new RuleProcessor(
                                //Rules
                                ImmutableList.of(
                                        //Potted
                                        addPotReplaceable(Blocks.POTTED_CACTUS, 0.2f),
                                        addPotReplaceable(Blocks.POTTED_DEAD_BUSH, 0.2f),
                                        addPotReplaceable(TCOTS_Blocks_Fabric.POTTED_HAN_FIBER, 0.3f),
                                        addPotReplaceable(Blocks.POTTED_BROWN_MUSHROOM, 0.2f),
                                        addPotReplaceable(Blocks.POTTED_RED_MUSHROOM, 0.2f),
                                        addPotReplaceable(TCOTS_Blocks_Fabric.POTTED_PUFFBALL_MUSHROOM, 0.2f),
                                        addPotReplaceable(Blocks.POTTED_LILY_OF_THE_VALLEY, 0.2f),
                                        addPotReplaceable(TCOTS_Blocks_Fabric.POTTED_BRYONIA_FLOWER, 0.2f),


                                        //FlowerBlock
                                        //Replace Plants
                                        addFlowerReplaceable(Blocks.CACTUS, 0.3f),
                                        addFlowerReplaceable(Blocks.DEAD_BUSH, 0.3f),
                                        addFlowerReplaceable(TCOTS_Blocks_Fabric.HAN_FIBER_PLANT.defaultBlockState().setValue(HanFiberPlant.AGE, 3), 0.2f),
                                        addFlowerReplaceable(Blocks.AZURE_BLUET, 0.3f),
                                        addFlowerReplaceable(Blocks.LILY_OF_THE_VALLEY, 0.2f),
                                        addFlowerReplaceable(TCOTS_Blocks_Fabric.VERBENA_FLOWER.defaultBlockState().setValue(VerbenaFlower.AGE, 3), 0.2f),

                                        //Replace Vines
                                        addBlockStateReplaceable(Blocks.VINE.defaultBlockState().setValue(VineBlock.NORTH, true),
                                                TCOTS_Blocks_Fabric.BRYONIA_VINE.defaultBlockState().setValue(VineBlock.NORTH, true).setValue(BryoniaVine.AGE, 3),
                                                0.3f),

                                        addBlockStateReplaceable(Blocks.VINE.defaultBlockState().setValue(VineBlock.SOUTH, true),
                                                TCOTS_Blocks_Fabric.BRYONIA_VINE.defaultBlockState().setValue(VineBlock.SOUTH, true).setValue(BryoniaVine.AGE, 3),
                                                0.3f),

                                        addBlockStateReplaceable(Blocks.VINE.defaultBlockState().setValue(VineBlock.WEST, true),
                                                TCOTS_Blocks_Fabric.BRYONIA_VINE.defaultBlockState().setValue(VineBlock.WEST, true).setValue(BryoniaVine.AGE, 3),
                                                0.3f),

                                        addBlockStateReplaceable(Blocks.VINE.defaultBlockState().setValue(VineBlock.EAST, true),
                                                TCOTS_Blocks_Fabric.BRYONIA_VINE.defaultBlockState().setValue(VineBlock.EAST, true).setValue(BryoniaVine.AGE, 3),
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

        register(processorListRegisterable, RANDOM_HERBALIST_HERBS_SAVANNA,
                ImmutableList.of(
                        //Processor
                        new RuleProcessor(
                                //Rules
                                ImmutableList.of(

                                        addPotReplaceable(Blocks.POTTED_DANDELION, 0.2f),
                                        addPotReplaceable(TCOTS_Blocks_Fabric.POTTED_HAN_FIBER, 0.3f),
                                        addPotReplaceable(Blocks.POTTED_ACACIA_SAPLING, 0.2f),
                                        addPotReplaceable(Blocks.POTTED_BROWN_MUSHROOM, 0.2f),
                                        addPotReplaceable(Blocks.POTTED_RED_MUSHROOM, 0.2f),
                                        addPotReplaceable(Blocks.POTTED_AZURE_BLUET, 0.2f),
                                        addPotReplaceable(Blocks.POTTED_LILY_OF_THE_VALLEY, 0.2f),
                                        addPotReplaceable(TCOTS_Blocks_Fabric.POTTED_BRYONIA_FLOWER, 0.2f),


                                        //FlowerBlock
                                        //Replace Plants

                                        addFlowerReplaceable(TCOTS_Blocks_Fabric.HAN_FIBER_PLANT.defaultBlockState().setValue(HanFiberPlant.AGE, 3), 0.2f),
                                        addFlowerReplaceable(Blocks.SHORT_GRASS, 0.3f),
                                        addFlowerReplaceable(Blocks.DANDELION, 0.3f),
                                        addFlowerReplaceable(Blocks.ACACIA_SAPLING, 0.3f),
                                        addFlowerReplaceable(Blocks.MOSS_CARPET, 0.2f),
                                        addFlowerReplaceable(Blocks.AZURE_BLUET, 0.3f),
                                        addFlowerReplaceable(Blocks.LILY_OF_THE_VALLEY, 0.2f),

                                        //Replace Vines
                                        addBlockStateReplaceable(Blocks.VINE.defaultBlockState().setValue(VineBlock.NORTH, true),
                                                TCOTS_Blocks_Fabric.BRYONIA_VINE.defaultBlockState().setValue(VineBlock.NORTH, true).setValue(BryoniaVine.AGE, 3),
                                                0.2f),

                                        addBlockStateReplaceable(Blocks.VINE.defaultBlockState().setValue(VineBlock.SOUTH, true),
                                                TCOTS_Blocks_Fabric.BRYONIA_VINE.defaultBlockState().setValue(VineBlock.SOUTH, true).setValue(BryoniaVine.AGE, 3),
                                                0.2f),

                                        addBlockStateReplaceable(Blocks.VINE.defaultBlockState().setValue(VineBlock.WEST, true),
                                                TCOTS_Blocks_Fabric.BRYONIA_VINE.defaultBlockState().setValue(VineBlock.WEST, true).setValue(BryoniaVine.AGE, 3),
                                                0.2f),

                                        addBlockStateReplaceable(Blocks.VINE.defaultBlockState().setValue(VineBlock.EAST, true),
                                                TCOTS_Blocks_Fabric.BRYONIA_VINE.defaultBlockState().setValue(VineBlock.EAST, true).setValue(BryoniaVine.AGE, 3),
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


        register(processorListRegisterable, RANDOM_TROLL_CAVE,
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

    private static ProcessorRule addPotReplaceable(Block block, float probability){
        return addBlockReplaceable(Blocks.FLOWER_POT, block, probability);
    }

    private static ProcessorRule addBlockStateReplaceable(BlockState stateOriginal, BlockState stateReplace, float probability){
        return new ProcessorRule(
                new RandomBlockStateMatchTest(stateOriginal, probability),
                AlwaysTrueTest.INSTANCE,
                stateReplace
        );
    }

    private static ProcessorRule addFlowerReplaceable(Block block, float probability){
        return addBlockReplaceable(Blocks.POPPY, block, probability);
    }

    private static ProcessorRule addFlowerReplaceable(BlockState state, float probability){
        return addBlockReplaceable(state, probability);
    }

    private static ProcessorRule addBlockReplaceable(Block replace, Block block, float probability){
        return new ProcessorRule(
                new RandomBlockMatchTest(replace, probability),
                AlwaysTrueTest.INSTANCE,
                block.defaultBlockState()
        );
    }


    private static ProcessorRule addBlockReplaceable(BlockState state, float probability){
        return new ProcessorRule(
                new RandomBlockMatchTest(Blocks.POPPY, probability),
                AlwaysTrueTest.INSTANCE,
                state
        );
    }
    private static void register(BootstrapContext<StructureProcessorList> processorListRegisterable, ResourceKey<StructureProcessorList> key, List<StructureProcessor> processors) {
        processorListRegisterable.register(key, new StructureProcessorList(processors));
    }
}
