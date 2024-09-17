package TCOTS.world;

import TCOTS.TCOTS_Main;
import TCOTS.blocks.TCOTS_Blocks;
import TCOTS.blocks.plants.*;
import com.google.common.collect.ImmutableList;
import net.minecraft.block.*;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.structure.processor.RuleStructureProcessor;
import net.minecraft.structure.processor.StructureProcessor;
import net.minecraft.structure.processor.StructureProcessorList;
import net.minecraft.structure.processor.StructureProcessorRule;
import net.minecraft.structure.rule.AlwaysTrueRuleTest;
import net.minecraft.structure.rule.RandomBlockMatchRuleTest;
import net.minecraft.structure.rule.RandomBlockStateMatchRuleTest;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

import java.util.List;

public class TCOTS_ProcessorList {
    public static final RegistryKey<StructureProcessorList> RANDOM_TROLL_CAVE = registerKey("random_troll_cave");

    public static final RegistryKey<StructureProcessorList> RANDOM_HERBALIST_HERBS_PLAINS = registerKey("random_herbalist_herbs_plains");

    public static final RegistryKey<StructureProcessorList> RANDOM_HERBALIST_HERBS_TAIGA = registerKey("random_herbalist_herbs_taiga");

    public static final RegistryKey<StructureProcessorList> RANDOM_HERBALIST_HERBS_SNOWY = registerKey("random_herbalist_herbs_snowy");

    public static final RegistryKey<StructureProcessorList> RANDOM_HERBALIST_HERBS_SAVANNA = registerKey("random_herbalist_herbs_savanna");

    public static final RegistryKey<StructureProcessorList> RANDOM_HERBALIST_HERBS_DESERT = registerKey("random_herbalist_herbs_desert");

    public static RegistryKey<StructureProcessorList> registerKey(String name) {
        return RegistryKey.of(RegistryKeys.PROCESSOR_LIST, new Identifier(TCOTS_Main.MOD_ID, name));
    }

    public static void boostrap(Registerable<StructureProcessorList> processorListRegisterable) {


        register(processorListRegisterable, RANDOM_HERBALIST_HERBS_PLAINS,
                ImmutableList.of(
                        //Processor
                        new RuleStructureProcessor(
                                //Rules
                                ImmutableList.of(
                                //Potted
                                        addPotReplaceable(Blocks.POTTED_DANDELION, 0.3f),
                                        addPotReplaceable(TCOTS_Blocks.POTTED_CELANDINE_FLOWER, 0.3f),
                                        addPotReplaceable(Blocks.POTTED_POPPY, 0.3f),
                                        addPotReplaceable(Blocks.POTTED_AZURE_BLUET, 0.2f),
                                        addPotReplaceable(Blocks.POTTED_FLOWERING_AZALEA_BUSH, 0.3f),
                                        addPotReplaceable(TCOTS_Blocks.POTTED_VERBENA_FLOWER, 0.3f),
                                        addPotReplaceable(Blocks.POTTED_OXEYE_DAISY, 0.3f),
                                        addPotReplaceable(Blocks.POTTED_LILY_OF_THE_VALLEY, 0.2f),
                                        addPotReplaceable(TCOTS_Blocks.POTTED_SEWANT_MUSHROOMS, 0.1f),

                                        //FlowerBlock
                                        //Replace Plants
                                        addFlowerReplaceable(Blocks.DANDELION, 0.3f),
                                        addFlowerReplaceable(Blocks.AZURE_BLUET, 0.3f),
                                        addFlowerReplaceable(TCOTS_Blocks.CELANDINE_PLANT.getDefaultState().with(CelandinePlant.AGE, 3), 0.3f),
                                        addFlowerReplaceable(Blocks.FLOWERING_AZALEA, 0.2f),
                                        addFlowerReplaceable(TCOTS_Blocks.VERBENA_FLOWER.getDefaultState().with(VerbenaFlower.AGE, 3), 0.2f),
                                        addFlowerReplaceable(Blocks.LILY_OF_THE_VALLEY, 0.2f),
                                        addFlowerReplaceable(TCOTS_Blocks.ARENARIA_BUSH.getDefaultState().with(ArenariaBush.AGE, 2), 0.1f),

                                        //Replace Vines
                                        addBlockStateReplaceable(Blocks.VINE.getDefaultState().with(VineBlock.NORTH, true),
                                                TCOTS_Blocks.BRYONIA_VINE.getDefaultState().with(VineBlock.NORTH, true).with(BryoniaVine.AGE, 3),
                                                0.3f),

                                        addBlockStateReplaceable(Blocks.VINE.getDefaultState().with(VineBlock.SOUTH, true),
                                                TCOTS_Blocks.BRYONIA_VINE.getDefaultState().with(VineBlock.SOUTH, true).with(BryoniaVine.AGE, 3),
                                                0.3f),

                                        addBlockStateReplaceable(Blocks.VINE.getDefaultState().with(VineBlock.WEST, true),
                                                TCOTS_Blocks.BRYONIA_VINE.getDefaultState().with(VineBlock.WEST, true).with(BryoniaVine.AGE, 3),
                                                0.3f),

                                        addBlockStateReplaceable(Blocks.VINE.getDefaultState().with(VineBlock.EAST, true),
                                                TCOTS_Blocks.BRYONIA_VINE.getDefaultState().with(VineBlock.EAST, true).with(BryoniaVine.AGE, 3),
                                                0.3f),

                                        addBlockStateReplaceable(Blocks.VINE.getDefaultState().with(VineBlock.NORTH, true),
                                                Blocks.GLOW_LICHEN.getDefaultState().with(VineBlock.NORTH, true),
                                                0.1f),

                                        addBlockStateReplaceable(Blocks.VINE.getDefaultState().with(VineBlock.SOUTH, true),
                                                Blocks.GLOW_LICHEN.getDefaultState().with(VineBlock.SOUTH, true),
                                                0.1f),

                                        addBlockStateReplaceable(Blocks.VINE.getDefaultState().with(VineBlock.EAST, true),
                                                Blocks.GLOW_LICHEN.getDefaultState().with(VineBlock.EAST, true),
                                                0.1f),

                                        addBlockStateReplaceable(Blocks.VINE.getDefaultState().with(VineBlock.WEST, true),
                                                Blocks.GLOW_LICHEN.getDefaultState().with(VineBlock.WEST, true),
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
                        new RuleStructureProcessor(
                                //Rules
                                ImmutableList.of(
                                        //Potted
                                        addPotReplaceable(Blocks.POTTED_FERN, 0.3f),
                                        addPotReplaceable(Blocks.POTTED_BROWN_MUSHROOM, 0.3f),
                                        addPotReplaceable(Blocks.POTTED_RED_MUSHROOM, 0.2f),
                                        addPotReplaceable(Blocks.POTTED_SPRUCE_SAPLING, 0.2f),
                                        addPotReplaceable(TCOTS_Blocks.POTTED_PUFFBALL_MUSHROOM, 0.2f),
                                        addPotReplaceable(TCOTS_Blocks.POTTED_SEWANT_MUSHROOMS, 0.2f),
                                        addPotReplaceable(Blocks.POTTED_AZURE_BLUET, 0.2f),
                                        addPotReplaceable(Blocks.POTTED_CORNFLOWER, 0.2f),
                                        addPotReplaceable(Blocks.POTTED_ALLIUM, 0.2f),

                                        //FlowerBlock
                                        //Replace Plants
                                        addFlowerReplaceable(Blocks.DANDELION, 0.3f),
                                        addFlowerReplaceable(Blocks.SWEET_BERRY_BUSH.getDefaultState().with(VerbenaFlower.AGE, 2), 0.3f),
                                        addFlowerReplaceable(Blocks.FERN, 0.3f),
                                        addFlowerReplaceable(Blocks.AZURE_BLUET, 0.2f),
                                        addFlowerReplaceable(TCOTS_Blocks.ARENARIA_BUSH.getDefaultState().with(ArenariaBush.AGE, 2), 0.3f),

                                        addBlockStateReplaceable(Blocks.FERN.getDefaultState(),
                                                TCOTS_Blocks.CROWS_EYE_FERN.getDefaultState().with(TallPlantBlock.HALF, DoubleBlockHalf.LOWER).with(CrowsEyeFern.AGE, 2),
                                                0.4f),

                                        addFlowerReplaceable(TCOTS_Blocks.VERBENA_FLOWER.getDefaultState().with(VerbenaFlower.AGE, 3), 0.2f),

                                        addFlowerReplaceable(TCOTS_Blocks.SEWANT_MUSHROOMS_PLANT.getDefaultState().with(SewantMushroomsPlant.MUSHROOM_AMOUNT, 2)
                                                .with(SewantMushroomsPlant.FACING, Direction.NORTH), 0.2f),

                                        addFlowerReplaceable(TCOTS_Blocks.SEWANT_MUSHROOMS_PLANT.getDefaultState().with(SewantMushroomsPlant.MUSHROOM_AMOUNT, 2)
                                                .with(SewantMushroomsPlant.FACING, Direction.SOUTH), 0.2f),


                                        addFlowerReplaceable(Blocks.ALLIUM, 0.2f),

                                        //Replace Vines
                                        addBlockStateReplaceable(Blocks.VINE.getDefaultState().with(VineBlock.NORTH, true),
                                                TCOTS_Blocks.BRYONIA_VINE.getDefaultState().with(VineBlock.NORTH, true).with(BryoniaVine.AGE, 3),
                                                0.2f),

                                        addBlockStateReplaceable(Blocks.VINE.getDefaultState().with(VineBlock.SOUTH, true),
                                                TCOTS_Blocks.BRYONIA_VINE.getDefaultState().with(VineBlock.SOUTH, true).with(BryoniaVine.AGE, 3),
                                                0.2f),

                                        addBlockStateReplaceable(Blocks.VINE.getDefaultState().with(VineBlock.WEST, true),
                                                TCOTS_Blocks.BRYONIA_VINE.getDefaultState().with(VineBlock.WEST, true).with(BryoniaVine.AGE, 3),
                                                0.2f),

                                        addBlockStateReplaceable(Blocks.VINE.getDefaultState().with(VineBlock.EAST, true),
                                                TCOTS_Blocks.BRYONIA_VINE.getDefaultState().with(VineBlock.EAST, true).with(BryoniaVine.AGE, 3),
                                                0.2f),

                                        addBlockStateReplaceable(Blocks.VINE.getDefaultState().with(VineBlock.NORTH, true),
                                                Blocks.GLOW_LICHEN.getDefaultState().with(VineBlock.NORTH, true),
                                                0.1f),

                                        addBlockStateReplaceable(Blocks.VINE.getDefaultState().with(VineBlock.SOUTH, true),
                                                Blocks.GLOW_LICHEN.getDefaultState().with(VineBlock.SOUTH, true),
                                                0.1f),

                                        addBlockStateReplaceable(Blocks.VINE.getDefaultState().with(VineBlock.EAST, true),
                                                Blocks.GLOW_LICHEN.getDefaultState().with(VineBlock.EAST, true),
                                                0.1f),

                                        addBlockStateReplaceable(Blocks.VINE.getDefaultState().with(VineBlock.WEST, true),
                                                Blocks.GLOW_LICHEN.getDefaultState().with(VineBlock.WEST, true),
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
                        new RuleStructureProcessor(
                                //Rules
                                ImmutableList.of(
                                        //Potted
                                        addPotReplaceable(TCOTS_Blocks.POTTED_BRYONIA_FLOWER, 0.2f),
                                        addPotReplaceable(Blocks.POTTED_BROWN_MUSHROOM, 0.3f),
                                        addPotReplaceable(Blocks.POTTED_RED_MUSHROOM, 0.2f),
                                        addPotReplaceable(Blocks.POTTED_SPRUCE_SAPLING, 0.2f),
                                        addPotReplaceable(TCOTS_Blocks.POTTED_PUFFBALL_MUSHROOM, 0.2f),
                                        addPotReplaceable(TCOTS_Blocks.POTTED_VERBENA_FLOWER, 0.3f),
                                        addPotReplaceable(Blocks.POTTED_AZURE_BLUET, 0.2f),
                                        addPotReplaceable(Blocks.POTTED_OXEYE_DAISY, 0.3f),

                                        //FlowerBlock
                                        //Replace Plants
                                        addFlowerReplaceable(TCOTS_Blocks.ARENARIA_BUSH.getDefaultState().with(ArenariaBush.AGE, 2), 0.3f),
                                        addBlockStateReplaceable(Blocks.FERN.getDefaultState(),
                                                TCOTS_Blocks.CROWS_EYE_FERN.getDefaultState().with(TallPlantBlock.HALF, DoubleBlockHalf.LOWER).with(CrowsEyeFern.AGE, 2),
                                                0.3f),
                                        addFlowerReplaceable(Blocks.SWEET_BERRY_BUSH.getDefaultState().with(VerbenaFlower.AGE, 2), 0.3f),
                                        addFlowerReplaceable(Blocks.BROWN_MUSHROOM, 0.3f),
                                        addFlowerReplaceable(TCOTS_Blocks.PUFFBALL_MUSHROOM, 0.3f),
                                        addFlowerReplaceable(Blocks.AZURE_BLUET, 0.3f),
                                        addFlowerReplaceable(TCOTS_Blocks.VERBENA_FLOWER.getDefaultState().with(VerbenaFlower.AGE, 3), 0.2f),
                                        addFlowerReplaceable(Blocks.DANDELION, 0.2f),

                                        //Replace Vines
                                        addBlockStateReplaceable(Blocks.VINE.getDefaultState().with(VineBlock.NORTH, true),
                                                TCOTS_Blocks.BRYONIA_VINE.getDefaultState().with(VineBlock.NORTH, true).with(BryoniaVine.AGE, 3),
                                                0.3f),

                                        addBlockStateReplaceable(Blocks.VINE.getDefaultState().with(VineBlock.SOUTH, true),
                                                TCOTS_Blocks.BRYONIA_VINE.getDefaultState().with(VineBlock.SOUTH, true).with(BryoniaVine.AGE, 3),
                                                0.3f),

                                        addBlockStateReplaceable(Blocks.VINE.getDefaultState().with(VineBlock.WEST, true),
                                                TCOTS_Blocks.BRYONIA_VINE.getDefaultState().with(VineBlock.WEST, true).with(BryoniaVine.AGE, 3),
                                                0.3f),

                                        addBlockStateReplaceable(Blocks.VINE.getDefaultState().with(VineBlock.EAST, true),
                                                TCOTS_Blocks.BRYONIA_VINE.getDefaultState().with(VineBlock.EAST, true).with(BryoniaVine.AGE, 3),
                                                0.3f),

                                        addBlockStateReplaceable(Blocks.VINE.getDefaultState().with(VineBlock.NORTH, true),
                                                Blocks.GLOW_LICHEN.getDefaultState().with(VineBlock.NORTH, true),
                                                0.1f),

                                        addBlockStateReplaceable(Blocks.VINE.getDefaultState().with(VineBlock.SOUTH, true),
                                                Blocks.GLOW_LICHEN.getDefaultState().with(VineBlock.SOUTH, true),
                                                0.1f),

                                        addBlockStateReplaceable(Blocks.VINE.getDefaultState().with(VineBlock.EAST, true),
                                                Blocks.GLOW_LICHEN.getDefaultState().with(VineBlock.EAST, true),
                                                0.1f),

                                        addBlockStateReplaceable(Blocks.VINE.getDefaultState().with(VineBlock.WEST, true),
                                                Blocks.GLOW_LICHEN.getDefaultState().with(VineBlock.WEST, true),
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
                        new RuleStructureProcessor(
                                //Rules
                                ImmutableList.of(
                                        //Potted
                                        addPotReplaceable(Blocks.POTTED_CACTUS, 0.2f),
                                        addPotReplaceable(Blocks.POTTED_DEAD_BUSH, 0.2f),
                                        addPotReplaceable(TCOTS_Blocks.POTTED_HAN_FIBER, 0.3f),
                                        addPotReplaceable(Blocks.POTTED_BROWN_MUSHROOM, 0.2f),
                                        addPotReplaceable(Blocks.POTTED_RED_MUSHROOM, 0.2f),
                                        addPotReplaceable(TCOTS_Blocks.POTTED_PUFFBALL_MUSHROOM, 0.2f),
                                        addPotReplaceable(Blocks.POTTED_LILY_OF_THE_VALLEY, 0.2f),
                                        addPotReplaceable(TCOTS_Blocks.POTTED_BRYONIA_FLOWER, 0.2f),


                                        //FlowerBlock
                                        //Replace Plants
                                        addFlowerReplaceable(Blocks.CACTUS, 0.3f),
                                        addFlowerReplaceable(Blocks.DEAD_BUSH, 0.3f),
                                        addFlowerReplaceable(TCOTS_Blocks.HAN_FIBER_PLANT.getDefaultState().with(HanFiberPlant.AGE, 3), 0.2f),
                                        addFlowerReplaceable(Blocks.AZURE_BLUET, 0.3f),
                                        addFlowerReplaceable(Blocks.LILY_OF_THE_VALLEY, 0.2f),
                                        addFlowerReplaceable(TCOTS_Blocks.VERBENA_FLOWER.getDefaultState().with(VerbenaFlower.AGE, 3), 0.2f),

                                        //Replace Vines
                                        addBlockStateReplaceable(Blocks.VINE.getDefaultState().with(VineBlock.NORTH, true),
                                                TCOTS_Blocks.BRYONIA_VINE.getDefaultState().with(VineBlock.NORTH, true).with(BryoniaVine.AGE, 3),
                                                0.3f),

                                        addBlockStateReplaceable(Blocks.VINE.getDefaultState().with(VineBlock.SOUTH, true),
                                                TCOTS_Blocks.BRYONIA_VINE.getDefaultState().with(VineBlock.SOUTH, true).with(BryoniaVine.AGE, 3),
                                                0.3f),

                                        addBlockStateReplaceable(Blocks.VINE.getDefaultState().with(VineBlock.WEST, true),
                                                TCOTS_Blocks.BRYONIA_VINE.getDefaultState().with(VineBlock.WEST, true).with(BryoniaVine.AGE, 3),
                                                0.3f),

                                        addBlockStateReplaceable(Blocks.VINE.getDefaultState().with(VineBlock.EAST, true),
                                                TCOTS_Blocks.BRYONIA_VINE.getDefaultState().with(VineBlock.EAST, true).with(BryoniaVine.AGE, 3),
                                                0.3f),

                                        addBlockStateReplaceable(Blocks.VINE.getDefaultState().with(VineBlock.NORTH, true),
                                                Blocks.GLOW_LICHEN.getDefaultState().with(VineBlock.NORTH, true),
                                                0.1f),

                                        addBlockStateReplaceable(Blocks.VINE.getDefaultState().with(VineBlock.SOUTH, true),
                                                Blocks.GLOW_LICHEN.getDefaultState().with(VineBlock.SOUTH, true),
                                                0.1f),

                                        addBlockStateReplaceable(Blocks.VINE.getDefaultState().with(VineBlock.EAST, true),
                                                Blocks.GLOW_LICHEN.getDefaultState().with(VineBlock.EAST, true),
                                                0.1f),

                                        addBlockStateReplaceable(Blocks.VINE.getDefaultState().with(VineBlock.WEST, true),
                                                Blocks.GLOW_LICHEN.getDefaultState().with(VineBlock.WEST, true),
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
                        new RuleStructureProcessor(
                                //Rules
                                ImmutableList.of(

                                        addPotReplaceable(Blocks.POTTED_DANDELION, 0.2f),
                                        addPotReplaceable(TCOTS_Blocks.POTTED_HAN_FIBER, 0.3f),
                                        addPotReplaceable(Blocks.POTTED_ACACIA_SAPLING, 0.2f),
                                        addPotReplaceable(Blocks.POTTED_BROWN_MUSHROOM, 0.2f),
                                        addPotReplaceable(Blocks.POTTED_RED_MUSHROOM, 0.2f),
                                        addPotReplaceable(Blocks.POTTED_AZURE_BLUET, 0.2f),
                                        addPotReplaceable(Blocks.POTTED_LILY_OF_THE_VALLEY, 0.2f),
                                        addPotReplaceable(TCOTS_Blocks.POTTED_BRYONIA_FLOWER, 0.2f),


                                        //FlowerBlock
                                        //Replace Plants

                                        addFlowerReplaceable(TCOTS_Blocks.HAN_FIBER_PLANT.getDefaultState().with(HanFiberPlant.AGE, 3), 0.2f),
                                        addFlowerReplaceable(Blocks.SHORT_GRASS, 0.3f),
                                        addFlowerReplaceable(Blocks.DANDELION, 0.3f),
                                        addFlowerReplaceable(Blocks.ACACIA_SAPLING, 0.3f),
                                        addFlowerReplaceable(Blocks.MOSS_CARPET, 0.2f),
                                        addFlowerReplaceable(Blocks.AZURE_BLUET, 0.3f),
                                        addFlowerReplaceable(Blocks.LILY_OF_THE_VALLEY, 0.2f),

                                        //Replace Vines
                                        addBlockStateReplaceable(Blocks.VINE.getDefaultState().with(VineBlock.NORTH, true),
                                                TCOTS_Blocks.BRYONIA_VINE.getDefaultState().with(VineBlock.NORTH, true).with(BryoniaVine.AGE, 3),
                                                0.2f),

                                        addBlockStateReplaceable(Blocks.VINE.getDefaultState().with(VineBlock.SOUTH, true),
                                                TCOTS_Blocks.BRYONIA_VINE.getDefaultState().with(VineBlock.SOUTH, true).with(BryoniaVine.AGE, 3),
                                                0.2f),

                                        addBlockStateReplaceable(Blocks.VINE.getDefaultState().with(VineBlock.WEST, true),
                                                TCOTS_Blocks.BRYONIA_VINE.getDefaultState().with(VineBlock.WEST, true).with(BryoniaVine.AGE, 3),
                                                0.2f),

                                        addBlockStateReplaceable(Blocks.VINE.getDefaultState().with(VineBlock.EAST, true),
                                                TCOTS_Blocks.BRYONIA_VINE.getDefaultState().with(VineBlock.EAST, true).with(BryoniaVine.AGE, 3),
                                                0.2f),

                                        addBlockStateReplaceable(Blocks.VINE.getDefaultState().with(VineBlock.NORTH, true),
                                                Blocks.GLOW_LICHEN.getDefaultState().with(VineBlock.NORTH, true),
                                                0.1f),

                                        addBlockStateReplaceable(Blocks.VINE.getDefaultState().with(VineBlock.SOUTH, true),
                                                Blocks.GLOW_LICHEN.getDefaultState().with(VineBlock.SOUTH, true),
                                                0.1f),

                                        addBlockStateReplaceable(Blocks.VINE.getDefaultState().with(VineBlock.EAST, true),
                                                Blocks.GLOW_LICHEN.getDefaultState().with(VineBlock.EAST, true),
                                                0.1f),

                                        addBlockStateReplaceable(Blocks.VINE.getDefaultState().with(VineBlock.WEST, true),
                                                Blocks.GLOW_LICHEN.getDefaultState().with(VineBlock.WEST, true),
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
                        new RuleStructureProcessor(
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

    private static StructureProcessorRule addPotReplaceable(Block block, float probability){
        return addBlockReplaceable(Blocks.FLOWER_POT, block, probability);
    }

    private static StructureProcessorRule addBlockStateReplaceable(BlockState stateOriginal, BlockState stateReplace, float probability){
        return new StructureProcessorRule(
                new RandomBlockStateMatchRuleTest(stateOriginal, probability),
                AlwaysTrueRuleTest.INSTANCE,
                stateReplace
        );
    }

    private static StructureProcessorRule addFlowerReplaceable(Block block, float probability){
        return addBlockReplaceable(Blocks.POPPY, block, probability);
    }

    private static StructureProcessorRule addFlowerReplaceable(BlockState state, float probability){
        return addBlockReplaceable(state, probability);
    }

    private static StructureProcessorRule addBlockReplaceable(Block replace, Block block, float probability){
        return new StructureProcessorRule(
                new RandomBlockMatchRuleTest(replace, probability),
                AlwaysTrueRuleTest.INSTANCE,
                block.getDefaultState()
        );
    }


    private static StructureProcessorRule addBlockReplaceable(BlockState state, float probability){
        return new StructureProcessorRule(
                new RandomBlockMatchRuleTest(Blocks.POPPY, probability),
                AlwaysTrueRuleTest.INSTANCE,
                state
        );
    }
    private static void register(Registerable<StructureProcessorList> processorListRegisterable, RegistryKey<StructureProcessorList> key, List<StructureProcessor> processors) {
        processorListRegisterable.register(key, new StructureProcessorList(processors));
    }
}
