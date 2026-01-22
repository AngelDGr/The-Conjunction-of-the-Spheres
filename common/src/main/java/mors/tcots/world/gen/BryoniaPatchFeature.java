package mors.tcots.world.gen;

import mors.tcots.block.plants.BryoniaVine;
import com.mojang.serialization.Codec;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

public class BryoniaPatchFeature extends Feature<BryoniaPatchFeatureConfig> {
    public BryoniaPatchFeature(final Codec<BryoniaPatchFeatureConfig> configCodec) {
        super(configCodec);
    }

    @Override
    public boolean place(final FeaturePlaceContext<BryoniaPatchFeatureConfig> context) {
        final WorldGenLevel structureWorldAccess = context.level();
        final BlockPos blockPos = context.origin();
        final RandomSource random = context.random();
        final BryoniaPatchFeatureConfig bryoniaGrowthFeatureConfig = context.config();

        if(blockPos.getY() > bryoniaGrowthFeatureConfig.YMax || blockPos.getY() < bryoniaGrowthFeatureConfig.YMin){
           return false;
        }

        if ( (isNotAir(structureWorldAccess.getBlockState(blockPos))) || (isNotAir(structureWorldAccess.getBlockState(blockPos.below())) && isNotAir(structureWorldAccess.getBlockState(blockPos.below().below())) )  ) {
            return false;
        }

        final List<Direction> list = bryoniaGrowthFeatureConfig.shuffleDirections(random);
        return generate(structureWorldAccess, blockPos, structureWorldAccess.getBlockState(blockPos), bryoniaGrowthFeatureConfig, random, list);
    }

    public static boolean generate(final WorldGenLevel world, final BlockPos pos, final BlockState state, final BryoniaPatchFeatureConfig config, final RandomSource random, final List<Direction> directions) {
        final BlockPos.MutableBlockPos mutable = pos.mutable();
        for (final Direction direction : directions) {
            final BlockState blockState = world.getBlockState(mutable.setWithOffset(pos, direction));

            if(isNotInBlock(blockState)) continue;

            final BlockState blockState2 = config.lichen.getStateForPlacement(state, world, pos, direction);

            if (blockState2 == null) {
                return false;
            }

            world.setBlock(pos, blockState2.setValue(BryoniaVine.AGE,random.nextInt(4)), Block.UPDATE_ALL);
            world.getChunk(pos).markPosForPostprocessing(pos);

            return true;
        }
        return false;
    }

    private static boolean isNotAir(final BlockState state) {
        return !state.isAir();
    }

    private static boolean isNotInBlock(final BlockState state) {
        return !(state.is(BlockTags.LOGS_THAT_BURN) || state.is(BlockTags.STONE_BRICKS) || state.is(BlockTags.TERRACOTTA)
                || state.is(BlockTags.PLANKS) || state.is(BlockTags.STONE_ORE_REPLACEABLES) || state.is(BlockTags.PLANKS)
                || state.is(Blocks.MOSSY_COBBLESTONE)) || state.is(Blocks.MOSS_BLOCK);
    }

}
