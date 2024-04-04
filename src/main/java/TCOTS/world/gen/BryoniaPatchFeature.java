package TCOTS.world.gen;

import TCOTS.blocks.plants.BryoniaVine;
import com.mojang.serialization.Codec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.util.FeatureContext;

import java.util.List;

public class BryoniaPatchFeature extends Feature<BryoniaPatchFeatureConfig> {
    public BryoniaPatchFeature(Codec<BryoniaPatchFeatureConfig> configCodec) {
        super(configCodec);
    }

    @Override
    public boolean generate(FeatureContext<BryoniaPatchFeatureConfig> context) {
        StructureWorldAccess structureWorldAccess = context.getWorld();
        BlockPos blockPos = context.getOrigin();
        Random random = context.getRandom();
        BryoniaPatchFeatureConfig bryoniaGrowthFeatureConfig = context.getConfig();

        if(blockPos.getY() > bryoniaGrowthFeatureConfig.YMax || blockPos.getY() < bryoniaGrowthFeatureConfig.YMin){
           return false;
        }

        if ( (isNotAir(structureWorldAccess.getBlockState(blockPos))) || (isNotAir(structureWorldAccess.getBlockState(blockPos.down())) && isNotAir(structureWorldAccess.getBlockState(blockPos.down().down())) )  ) {
            return false;
        }

        List<Direction> list = bryoniaGrowthFeatureConfig.shuffleDirections(random);
        if (generate(structureWorldAccess, blockPos, structureWorldAccess.getBlockState(blockPos), bryoniaGrowthFeatureConfig, random, list)) {
            return true;
        }

        return false;
    }

    public static boolean generate(StructureWorldAccess world, BlockPos pos, BlockState state, BryoniaPatchFeatureConfig config, Random random, List<Direction> directions) {
        BlockPos.Mutable mutable = pos.mutableCopy();
        for (Direction direction : directions) {
            BlockState blockState = world.getBlockState(mutable.set(pos, direction));

            if(isNotInBlock(blockState)) continue;

            BlockState blockState2 = config.lichen.withDirection(state, world, pos, direction);

            if (blockState2 == null) {
                return false;
            }

            world.setBlockState(pos, blockState2.with(BryoniaVine.AGE,random.nextInt(4)), Block.NOTIFY_ALL);
            world.getChunk(pos).markBlockForPostProcessing(pos);

            return true;
        }
        return false;
    }

    private static boolean isNotAir(BlockState state) {
        return !state.isAir();
    }

    private static boolean isNotInBlock(BlockState state) {
        return !(state.isIn(BlockTags.LOGS_THAT_BURN) || state.isIn(BlockTags.STONE_BRICKS) || state.isIn(BlockTags.TERRACOTTA)
                || state.isIn(BlockTags.PLANKS) || state.isIn(BlockTags.STONE_ORE_REPLACEABLES) || state.isIn(BlockTags.PLANKS)
                || state.isOf(Blocks.MOSSY_COBBLESTONE)) || state.isOf(Blocks.MOSS_BLOCK);
    }

}
