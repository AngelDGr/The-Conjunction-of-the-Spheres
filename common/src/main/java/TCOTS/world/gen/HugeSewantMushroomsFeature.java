package TCOTS.world.gen;

import com.mojang.serialization.Codec;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.HugeMushroomBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.AbstractHugeMushroomFeature;
import net.minecraft.world.level.levelgen.feature.configurations.HugeMushroomFeatureConfiguration;

public class HugeSewantMushroomsFeature extends AbstractHugeMushroomFeature {
    //xTODO: Fix shape
    public HugeSewantMushroomsFeature(final Codec<HugeMushroomFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    protected int getTreeRadiusForHeight(final int i, final int j, final int capSize, final int y) {
        return 0;
    }

    @Override
    protected void makeCap(final LevelAccessor world, final RandomSource random, final BlockPos start, final int y, final BlockPos.MutableBlockPos mutable, final HugeMushroomFeatureConfiguration config) {
        for (int i = y - 3; i <= y; ++i) {
            final int j = i < y ? config.foliageRadius : config.foliageRadius - 1;
            final int k = config.foliageRadius - 2;
            for (int l = -j; l <= j; ++l) {
                for (int m = -j; m <= j; ++m) {
                    final boolean bl6;
                    final boolean bl = l == -j;
                    final boolean bl2 = l == j;
                    final boolean bl3 = m == -j;
                    final boolean bl4 = m == j;
                    final boolean bl5 = bl || bl2;
                    bl6 = bl3 || bl4;
                    if (i < y && bl5 == bl6) continue;
                    mutable.setWithOffset(start, l, i, m);
                    if (world.getBlockState(mutable).isSolidRender(world, mutable)) continue;
                    BlockState blockState = config.capProvider.getState(random, start);
                    if (blockState.hasProperty(HugeMushroomBlock.WEST) && blockState.hasProperty(HugeMushroomBlock.EAST) && blockState.hasProperty(HugeMushroomBlock.NORTH) && blockState.hasProperty(HugeMushroomBlock.SOUTH) && blockState.hasProperty(HugeMushroomBlock.UP)) {
                        blockState = blockState.setValue(HugeMushroomBlock.UP, i >= y - 1).setValue(HugeMushroomBlock.WEST, l < -k).setValue(HugeMushroomBlock.EAST, l > k).setValue(HugeMushroomBlock.NORTH, m < -k).setValue(HugeMushroomBlock.SOUTH, m > k);
                    }
                    this.setBlock(world, mutable, blockState);
                }
            }
        }
    }



    @Override
    protected void placeTrunk(final LevelAccessor world, final RandomSource random, final BlockPos pos, final HugeMushroomFeatureConfiguration config, final int height, final BlockPos.MutableBlockPos mutablePos) {
        int lastGeneratedBranchHeight=0;
        Direction lastBranchDirection=Direction.UP;
        config.stemProvider.getState(random, pos).setValue(HugeMushroomBlock.UP, true).setValue(HugeMushroomBlock.DOWN, true);

        for (int i = 0; i < height; ++i) {
            mutablePos.set(pos).move(Direction.UP, i);

            if (world.getBlockState(mutablePos).isSolidRender(world, mutablePos)) continue;
            this.setBlock(world, mutablePos, config.stemProvider.getState(random, pos));

            //Generates Branch & little cap
            if(i > 0 && i < height-6 && random.nextInt(2)==0){

                //Select Length
                final int randomBranchLength = random.nextIntBetweenInclusive(3,4);
                //Select Direction
                final Direction direction = switch (random.nextInt(4)) {
                    case 0 -> Direction.EAST;
                    case 1 -> Direction.WEST;
                    case 2 -> Direction.NORTH;
                    default -> Direction.SOUTH;
                };

                //Generates Branch
                for(int j = 1; j < randomBranchLength;j++ ) {
                    //To not generate two branches in the same side near each other
                    if ((direction==lastBranchDirection && i < (lastGeneratedBranchHeight+4))) break;
                    mutablePos.set(pos).move(Direction.UP, i).move(direction, j);
                    if (world.getBlockState(mutablePos).isSolidRender(world, mutablePos)) continue;
                    this.setBlock(world, mutablePos, config.stemProvider.getState(random, pos).setValue(HugeMushroomBlock.UP, true).setValue(HugeMushroomBlock.DOWN, true));
                    //Generates tiny cap
                    if(j==randomBranchLength-1) {
                        generateBranchCap(world, random, mutablePos, mutablePos, config);
                    }
                }

                lastBranchDirection=direction;
                lastGeneratedBranchHeight=i;
            }
        }
    }

    private final List<Direction> directionList = List.of(Direction.EAST, Direction.NORTH, Direction.WEST, Direction.SOUTH);

    private void generateBranchCap(final LevelAccessor world, final RandomSource random, final BlockPos start, final BlockPos.MutableBlockPos mutablePos, final HugeMushroomFeatureConfiguration config){

        BlockPos extraBranch = start;
        if(random.nextInt()%2==0) {
            //Generates branch extra stem
            final BlockState blockStateStem = config.stemProvider.getState(random, start).setValue(HugeMushroomBlock.UP, true).setValue(HugeMushroomBlock.DOWN, true);
            extraBranch = mutablePos.set(start).move(Direction.UP, 1);
            if (world.getBlockState(extraBranch).isSolidRender(world, extraBranch)) return;
            this.setBlock(world, extraBranch, blockStateStem);
        }


        final BlockPos center = mutablePos.set(extraBranch).move(Direction.UP,1);
        final BlockPos.MutableBlockPos mutable=new BlockPos.MutableBlockPos();
        //Center block
        final BlockState blockState = config.capProvider.getState(random, start);
        if (world.getBlockState(mutablePos).isSolidRender(world, mutablePos)) return;
        this.setBlock(world, mutablePos, blockState);

        //Side blocks
        for(final Direction direction: directionList) {
            mutable.set(center).move(direction,1);
            if (world.getBlockState(mutable).isSolidRender(world, mutable)) continue;
            this.setBlock(world,mutable,blockState);
        }

        //Corner blocks
        mutable.set(center).move(Direction.SOUTH,1).move(Direction.EAST,1);
        if (world.getBlockState(mutable).isSolidRender(world, mutable)) return;
        this.setBlock(world,mutable,blockState);

        mutable.set(center).move(Direction.SOUTH,1).move(Direction.WEST,1);
        if (world.getBlockState(mutable).isSolidRender(world, mutable)) return;
        this.setBlock(world,mutable,blockState);

        mutable.set(center).move(Direction.NORTH,1).move(Direction.EAST,1);
        if (world.getBlockState(mutable).isSolidRender(world, mutable)) return;
        this.setBlock(world,mutable,blockState);

        mutable.set(center).move(Direction.NORTH,1).move(Direction.WEST,1);
        if (world.getBlockState(mutable).isSolidRender(world, mutable)) return;
        this.setBlock(world,mutable,blockState);
    }

    @Override
    protected int getTreeHeight(final RandomSource random) {
        int i = random.nextInt(3) + 8;
        if (random.nextInt(3) == 0) {
            i *= 2;
        }
        return i;
    }
}
