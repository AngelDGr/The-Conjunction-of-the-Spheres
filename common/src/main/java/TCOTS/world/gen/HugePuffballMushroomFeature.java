package TCOTS.world.gen;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.HugeMushroomBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.AbstractHugeMushroomFeature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.HugeMushroomFeatureConfiguration;

public class HugePuffballMushroomFeature extends AbstractHugeMushroomFeature {
    public HugePuffballMushroomFeature(Codec<HugeMushroomFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    protected int getTreeRadiusForHeight(int i, int j, int capSize, int y) {
        int k = 0;
        if (y < j && y >= j - 3) {
            k = capSize;
        } else if (y == j) {
            k = capSize;
        }
        return k;
    }

    @Override
    protected void makeCap(LevelAccessor world, RandomSource random, BlockPos start, int height, BlockPos.MutableBlockPos mutable, HugeMushroomFeatureConfiguration config) {
        // Loop through the height-coordinates from (height - 3) to height
        for (int i = height - 4; i <= height; ++i) {
            int j;
            int k;
            // Determine the foliage radius based on the current height-coordinate
            if (i < height) {
                j = config.foliageRadius;
            } else {
                j = config.foliageRadius - 1;
            }

            k = config.foliageRadius - 2;

            // Loop through the x and z coordinates
            for (int l = -j; l <= j; ++l) {
                for (int m = -j; m <= j; ++m) {
                    // Determine if the current position is at the edge of the cap
                    boolean bl = l == -j;
                    boolean bl2 = l == j;
                    boolean bl3 = m == -j;
                    boolean bl4 = m == j;
                    boolean bl5 = bl || bl2;
                    boolean bl6 = bl3 || bl4;

                    // Skip if not at the edge and not below the cap
                    if (i < height && bl5 == bl6) continue;

                    // Set the position to the current coordinates
                    mutable.setWithOffset(start, l, i, m);

                    // Skip if the block at the position is solid
                    if (world.getBlockState(mutable).isSolidRender(world, mutable)) continue;

                    // Get the block state for the cap
                    BlockState blockState = config.capProvider.getState(random, start);

                    // Adjust block state based on position and orientation
                    if (blockState.hasProperty(HugeMushroomBlock.WEST) && blockState.hasProperty(HugeMushroomBlock.EAST) && blockState.hasProperty(HugeMushroomBlock.NORTH) && blockState.hasProperty(HugeMushroomBlock.SOUTH) && blockState.hasProperty(HugeMushroomBlock.UP)) {
                        blockState = blockState
                                .setValue(HugeMushroomBlock.UP, i >= height - 1)
                                .setValue(HugeMushroomBlock.WEST, l < -k)
                                .setValue(HugeMushroomBlock.EAST, l > k)
                                .setValue(HugeMushroomBlock.NORTH, m < -k)
                                .setValue(HugeMushroomBlock.SOUTH, m > k);
                    }

                    // Set the block state at the position
                    this.setBlock(world, mutable, blockState);
                }
            }
        }
    }

    @Override
    public boolean place(FeaturePlaceContext<HugeMushroomFeatureConfiguration> context) {
        BlockPos.MutableBlockPos mutable;
        WorldGenLevel structureWorldAccess = context.level();
        BlockPos blockPos = context.origin();
        RandomSource random = context.random();
        HugeMushroomFeatureConfiguration hugeMushroomFeatureConfig = context.config();
        int i = this.getTreeHeight(random);
        if (!this.isValidPosition(structureWorldAccess, blockPos, i, mutable = new BlockPos.MutableBlockPos(), hugeMushroomFeatureConfig)) {
            return false;
        }
        this.makeCap(structureWorldAccess, random, blockPos, i, mutable, hugeMushroomFeatureConfig);
        this.placeTrunk(structureWorldAccess, random, blockPos, hugeMushroomFeatureConfig, i, mutable);
        return true;
    }

    @Override
    protected boolean isValidPosition(LevelAccessor world, BlockPos pos, int height, BlockPos.MutableBlockPos mutablePos, HugeMushroomFeatureConfiguration config) {
        return super.isValidPosition(world, pos, height, mutablePos, config);
    }

    @Override
    protected int getTreeHeight(RandomSource random) {
        return random.nextInt(2) + 5;
    }
}
