package TCOTS.world.gen;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.block.MushroomBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.gen.feature.HugeMushroomFeature;
import net.minecraft.world.gen.feature.HugeMushroomFeatureConfig;
import net.minecraft.world.gen.feature.util.FeatureContext;

public class HugePuffballMushroomFeature extends HugeMushroomFeature {
    public HugePuffballMushroomFeature(Codec<HugeMushroomFeatureConfig> codec) {
        super(codec);
    }

    @Override
    protected int getCapSize(int i, int j, int capSize, int y) {
        int k = 0;
        if (y < j && y >= j - 3) {
            k = capSize;
        } else if (y == j) {
            k = capSize;
        }
        return k;
    }

    @Override
    protected void generateCap(WorldAccess world, Random random, BlockPos start, int height, BlockPos.Mutable mutable, HugeMushroomFeatureConfig config) {
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
                    mutable.set(start, l, i, m);

                    // Skip if the block at the position is solid
                    if (world.getBlockState(mutable).isOpaqueFullCube(world, mutable)) continue;

                    // Get the block state for the cap
                    BlockState blockState = config.capProvider.get(random, start);

                    // Adjust block state based on position and orientation
                    if (blockState.contains(MushroomBlock.WEST) && blockState.contains(MushroomBlock.EAST) && blockState.contains(MushroomBlock.NORTH) && blockState.contains(MushroomBlock.SOUTH) && blockState.contains(MushroomBlock.UP)) {
                        blockState = blockState
                                .with(MushroomBlock.UP, i >= height - 1)
                                .with(MushroomBlock.WEST, l < -k)
                                .with(MushroomBlock.EAST, l > k)
                                .with(MushroomBlock.NORTH, m < -k)
                                .with(MushroomBlock.SOUTH, m > k);
                    }

                    // Set the block state at the position
                    this.setBlockState(world, mutable, blockState);
                }
            }
        }
    }

    @Override
    public boolean generate(FeatureContext<HugeMushroomFeatureConfig> context) {
        BlockPos.Mutable mutable;
        StructureWorldAccess structureWorldAccess = context.getWorld();
        BlockPos blockPos = context.getOrigin();
        Random random = context.getRandom();
        HugeMushroomFeatureConfig hugeMushroomFeatureConfig = context.getConfig();
        int i = this.getHeight(random);
        if (!this.canGenerate(structureWorldAccess, blockPos, i, mutable = new BlockPos.Mutable(), hugeMushroomFeatureConfig)) {
            return false;
        }
        this.generateCap(structureWorldAccess, random, blockPos, i, mutable, hugeMushroomFeatureConfig);
        this.generateStem(structureWorldAccess, random, blockPos, hugeMushroomFeatureConfig, i, mutable);
        return true;
    }

    @Override
    protected boolean canGenerate(WorldAccess world, BlockPos pos, int height, BlockPos.Mutable mutablePos, HugeMushroomFeatureConfig config) {
        return super.canGenerate(world, pos, height, mutablePos, config);
    }

    @Override
    protected int getHeight(Random random) {
        return random.nextInt(2) + 5;
    }
}
