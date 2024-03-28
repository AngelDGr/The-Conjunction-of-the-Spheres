package TCOTS.blocks.plants;

import TCOTS.blocks.TCOTS_Blocks;
import TCOTS.items.TCOTS_Items;
import TCOTS.sounds.TCOTS_Sounds;
import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.RavagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.*;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

public class CrowsEyeFern extends TallPlantBlock implements Fertilizable {

    public static final MapCodec<CrowsEyeFern> CODEC = CrowsEyeFern.createCodec(CrowsEyeFern::new);
    public static final IntProperty AGE = Properties.AGE_4;

    private static final VoxelShape GROWN_UPPER_OUTLINE_SHAPE = Block.createCuboidShape(3.0, 0.0, 3.0, 13.0, 10.0, 13.0);
    private static final VoxelShape GROWN_LOWER_OUTLINE_SHAPE =
            Block.createCuboidShape(3.0, 0.0, 3.0, 13.0, 16.0, 13.0);
    private static final VoxelShape AGE_0_SHAPE =
            Block.createCuboidShape(5.0, 0.0, 5.0, 11.0, 4.0, 11.0);
    private static final VoxelShape[] UPPER_OUTLINE_SHAPES = new VoxelShape[]{Block.createCuboidShape(3.0, 0.0, 3.0, 13.0, 5.0, 13.0), GROWN_UPPER_OUTLINE_SHAPE};
    private static final VoxelShape[] LOWER_OUTLINE_SHAPES = new VoxelShape[]{
            AGE_0_SHAPE,
            Block.createCuboidShape(3.0, 0.0, 3.0, 13.0, 9.0, 13.0),
            GROWN_LOWER_OUTLINE_SHAPE,
            GROWN_LOWER_OUTLINE_SHAPE,
            GROWN_LOWER_OUTLINE_SHAPE};


    public MapCodec<CrowsEyeFern> getCodec() {
        return CODEC;
    }

    public CrowsEyeFern(Settings settings) {
        super(settings);
    }

    @Override
    public ItemStack getPickStack(WorldView world, BlockPos pos, BlockState state) {
        return new ItemStack(TCOTS_Items.CROWS_EYE);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return state.get(HALF) == DoubleBlockHalf.UPPER ?
                UPPER_OUTLINE_SHAPES[Math.min(Math.abs(4 - (state.get(AGE) + 1)),
                        UPPER_OUTLINE_SHAPES.length - 1)] :
                LOWER_OUTLINE_SHAPES[state.get(AGE)];
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (isDoubleTallAtAge(state.get(AGE))) {
            return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
        }
        return state.canPlaceAt(world, pos) ? state : Blocks.AIR.getDefaultState();
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(AGE);
        super.appendProperties(builder);
    }

    @Override
    public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
        if (entity instanceof RavagerEntity && world.getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING)) {
            world.breakBlock(pos, true, entity);
        }
        super.onEntityCollision(state, world, pos, entity);
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
    }

    @Override
    public boolean hasRandomTicks(BlockState state) {
        return state.get(HALF) == DoubleBlockHalf.LOWER && !this.isFullyGrown(state);
    }

    private boolean isFullyGrown(BlockState state) {
        return state.get(AGE) >= 4;
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        int i = state.get(AGE);
        if (i < 3 && random.nextInt(5) == 0 && world.getBaseLightLevel(pos.up(), 0) >= 9) {
            BlockState blockState = state.with(AGE, i + 1);
            world.setBlockState(pos, blockState, Block.NOTIFY_LISTENERS);
            world.emitGameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Emitter.of(blockState));
        }
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        boolean bl;
        int i = state.get(AGE);
        bl = i == 4;
        if (!bl && player.getStackInHand(hand).isOf(Items.BONE_MEAL)) {
            return ActionResult.PASS;
        }
        if (i > 3) {
            int j = 1 + world.random.nextInt(4);
            SweetBerryBushBlock.dropStack(world, pos, new ItemStack(TCOTS_Items.CROWS_EYE, j));

            world.playSound(null, pos, TCOTS_Sounds.INGREDIENT_POPS, SoundCategory.BLOCKS, 1.0f, 0.8f + world.random.nextFloat() * 0.4f);

            BlockState ageState = state.with(AGE, 3);

            //Changes itself
            world.setBlockState(pos, ageState, Block.NOTIFY_LISTENERS);
            world.emitGameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Emitter.of(player, ageState));

            //It's the bottom half, so also change the top half
            if(isLowerHalf(state)){
                world.setBlockState(pos.up(), ageState.with(HALF, DoubleBlockHalf.UPPER), Block.NOTIFY_ALL);
                world.emitGameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Emitter.of(player, ageState));
            }
            //It's the top, so also change the bottom half
            else {
                world.setBlockState(pos.down(), ageState.with(HALF, DoubleBlockHalf.LOWER), Block.NOTIFY_LISTENERS);
                world.emitGameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Emitter.of(player, ageState));
            }

            return ActionResult.success(world.isClient);
        }
        return super.onUse(state, world, pos, player, hand, hit);
    }

    @Override
    public boolean isFertilizable(WorldView world, BlockPos pos, BlockState state) {
        LowerHalfContext lowerHalfContext = this.getLowerHalfContext(world, pos, state);
        if (lowerHalfContext == null) {
            return false;
        }
        return this.canGrow(world, lowerHalfContext.pos, lowerHalfContext.state, lowerHalfContext.state.get(AGE) + 1);
    }

    private static boolean canGrowAt(WorldView world, BlockPos pos) {
        BlockState blockState = world.getBlockState(pos);
        return blockState.isAir() || blockState.isOf(TCOTS_Blocks.CROWS_EYE_FERN);
    }

//    private static boolean canPlaceAt(WorldView world, BlockPos pos) {
//        return CropBlock.hasEnoughLightAt(world, pos);
//    }

    private boolean canGrow(WorldView world, BlockPos pos, BlockState state, int age) {
        return !this.isFullyGrown(state)
//                && canPlaceAt(world, pos)
                && (!isDoubleTallAtAge(age) || canGrowAt(world, pos.up()));
    }

    @Override
    public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
        return true;
    }

    private static boolean isDoubleTallAtAge(int age) {
        return age >= 3;
    }


    private static boolean isLowerHalf(BlockState state) {
        return state.isOf(TCOTS_Blocks.CROWS_EYE_FERN) && state.get(HALF) == DoubleBlockHalf.LOWER;
    }

    @Nullable
    private LowerHalfContext getLowerHalfContext(WorldView world, BlockPos pos, BlockState state) {
        if (isLowerHalf(state)) {
            return new LowerHalfContext(pos, state);
        }
        BlockPos blockPos = pos.down();
        BlockState blockState = world.getBlockState(blockPos);
        if (isLowerHalf(blockState)) {
            return new LowerHalfContext(blockPos, blockState);
        }
        return null;
    }

    @Override
    public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
        LowerHalfContext lowerHalfContext = this.getLowerHalfContext(world, pos, state);
        if (lowerHalfContext == null) {
            return;
        }
        this.tryGrow(world, lowerHalfContext.state, lowerHalfContext.pos, 1);
    }

    private void tryGrow(ServerWorld world, BlockState state, BlockPos pos, int amount) {
        int i = Math.min(state.get(AGE) + amount, 4);
        if (!this.canGrow(world, pos, state, i)) {
            return;
        }
        BlockState blockState = state.with(AGE, i);
        world.setBlockState(pos, blockState, Block.NOTIFY_LISTENERS);

        //Place the upper half
        if (isDoubleTallAtAge(i)) {
            world.setBlockState(pos.up(), blockState.with(HALF, DoubleBlockHalf.UPPER), Block.NOTIFY_ALL);
        }
    }

    record LowerHalfContext(BlockPos pos, BlockState state) {
    }
}
