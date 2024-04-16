package TCOTS.world.gen;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.block.MushroomBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.gen.feature.HugeMushroomFeature;
import net.minecraft.world.gen.feature.HugeMushroomFeatureConfig;

import java.util.List;

public class HugeSewantMushroomsFeature extends HugeMushroomFeature {
    //xTODO: Fix shape
    public HugeSewantMushroomsFeature(Codec<HugeMushroomFeatureConfig> codec) {
        super(codec);
    }

    @Override
    protected int getCapSize(int i, int j, int capSize, int y) {
        return 0;
    }

    @Override
    protected void generateCap(WorldAccess world, Random random, BlockPos start, int y, BlockPos.Mutable mutable, HugeMushroomFeatureConfig config) {
        for (int i = y - 3; i <= y; ++i) {
            int j = i < y ? config.foliageRadius : config.foliageRadius - 1;
            int k = config.foliageRadius - 2;
            for (int l = -j; l <= j; ++l) {
                for (int m = -j; m <= j; ++m) {
                    boolean bl6;
                    boolean bl = l == -j;
                    boolean bl2 = l == j;
                    boolean bl3 = m == -j;
                    boolean bl4 = m == j;
                    boolean bl5 = bl || bl2;
                    bl6 = bl3 || bl4;
                    if (i < y && bl5 == bl6) continue;
                    mutable.set(start, l, i, m);
                    if (world.getBlockState(mutable).isOpaqueFullCube(world, mutable)) continue;
                    BlockState blockState = config.capProvider.get(random, start);
                    if (blockState.contains(MushroomBlock.WEST) && blockState.contains(MushroomBlock.EAST) && blockState.contains(MushroomBlock.NORTH) && blockState.contains(MushroomBlock.SOUTH) && blockState.contains(MushroomBlock.UP)) {
                        blockState = blockState.with(MushroomBlock.UP, i >= y - 1).with(MushroomBlock.WEST, l < -k).with(MushroomBlock.EAST, l > k).with(MushroomBlock.NORTH, m < -k).with(MushroomBlock.SOUTH, m > k);
                    }
                    this.setBlockState(world, mutable, blockState);
                }
            }
        }
    }



    @Override
    protected void generateStem(WorldAccess world, Random random, BlockPos pos, HugeMushroomFeatureConfig config, int height, BlockPos.Mutable mutablePos) {
        int lastGeneratedBranchHeight=0;
        Direction lastBranchDirection=Direction.UP;
        config.stemProvider.get(random, pos).with(MushroomBlock.UP, true).with(MushroomBlock.DOWN, true);

        for (int i = 0; i < height; ++i) {
            mutablePos.set(pos).move(Direction.UP, i);

            if (world.getBlockState(mutablePos).isOpaqueFullCube(world, mutablePos)) continue;
            this.setBlockState(world, mutablePos, config.stemProvider.get(random, pos));

            //Generates Branch & little cap
            if(i > 0 && i < height-6 && random.nextInt(2)==0){

                //Select Length
                int randomBranchLength = random.nextBetween(3,4);
                //Select Direction
                Direction direction = switch (random.nextInt(4)) {
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
                    if (world.getBlockState(mutablePos).isOpaqueFullCube(world, mutablePos)) continue;
                    this.setBlockState(world, mutablePos, config.stemProvider.get(random, pos).with(MushroomBlock.UP, true).with(MushroomBlock.DOWN, true));
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

    private void generateBranchCap(WorldAccess world, Random random, BlockPos start, BlockPos.Mutable mutablePos, HugeMushroomFeatureConfig config){

        BlockPos extraBranch = start;
        if(random.nextInt()%2==0) {
            //Generates branch extra stem
            BlockState blockStateStem = config.stemProvider.get(random, start).with(MushroomBlock.UP, true).with(MushroomBlock.DOWN, true);
            extraBranch = mutablePos.set(start).move(Direction.UP, 1);
            if (world.getBlockState(extraBranch).isOpaqueFullCube(world, extraBranch)) return;
            this.setBlockState(world, extraBranch, blockStateStem);
        }


        BlockPos center = mutablePos.set(extraBranch).move(Direction.UP,1);
        BlockPos.Mutable mutable=new BlockPos.Mutable();
        //Center block
        BlockState blockState = config.capProvider.get(random, start);
        if (world.getBlockState(mutablePos).isOpaqueFullCube(world, mutablePos)) return;
        this.setBlockState(world, mutablePos, blockState);

        //Side blocks
        for(Direction direction: directionList) {
            mutable.set(center).move(direction,1);
            if (world.getBlockState(mutable).isOpaqueFullCube(world, mutable)) continue;
            this.setBlockState(world,mutable,blockState);
        }

        //Corner blocks
        mutable.set(center).move(Direction.SOUTH,1).move(Direction.EAST,1);
        if (world.getBlockState(mutable).isOpaqueFullCube(world, mutable)) return;
        this.setBlockState(world,mutable,blockState);

        mutable.set(center).move(Direction.SOUTH,1).move(Direction.WEST,1);
        if (world.getBlockState(mutable).isOpaqueFullCube(world, mutable)) return;
        this.setBlockState(world,mutable,blockState);

        mutable.set(center).move(Direction.NORTH,1).move(Direction.EAST,1);
        if (world.getBlockState(mutable).isOpaqueFullCube(world, mutable)) return;
        this.setBlockState(world,mutable,blockState);

        mutable.set(center).move(Direction.NORTH,1).move(Direction.WEST,1);
        if (world.getBlockState(mutable).isOpaqueFullCube(world, mutable)) return;
        this.setBlockState(world,mutable,blockState);
    }

    @Override
    protected int getHeight(Random random) {
        int i = random.nextInt(3) + 8;
        if (random.nextInt(3) == 0) {
            i *= 2;
        }
        return i;
    }
}
