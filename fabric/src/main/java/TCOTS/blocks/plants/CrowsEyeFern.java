package TCOTS.blocks.plants;

import TCOTS.blocks.TCOTS_Blocks_Fabric;
import TCOTS.items.TCOTS_Items_Fabric;
import TCOTS.sounds.TCOTS_Sounds;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.*;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Ravager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.SweetBerryBushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class CrowsEyeFern extends DoublePlantBlock implements BonemealableBlock {

    public static final MapCodec<CrowsEyeFern> CODEC = CrowsEyeFern.simpleCodec(CrowsEyeFern::new);
    public static final IntegerProperty AGE = BlockStateProperties.AGE_4;

    private static final VoxelShape GROWN_UPPER_OUTLINE_SHAPE = Block.box(3.0, 0.0, 3.0, 13.0, 10.0, 13.0);
    private static final VoxelShape GROWN_LOWER_OUTLINE_SHAPE =
            Block.box(3.0, 0.0, 3.0, 13.0, 16.0, 13.0);
    private static final VoxelShape AGE_0_SHAPE =
            Block.box(5.0, 0.0, 5.0, 11.0, 4.0, 11.0);
    private static final VoxelShape[] UPPER_OUTLINE_SHAPES = new VoxelShape[]{Block.box(3.0, 0.0, 3.0, 13.0, 5.0, 13.0), GROWN_UPPER_OUTLINE_SHAPE};
    private static final VoxelShape[] LOWER_OUTLINE_SHAPES = new VoxelShape[]{
            AGE_0_SHAPE,
            Block.box(3.0, 0.0, 3.0, 13.0, 9.0, 13.0),
            GROWN_LOWER_OUTLINE_SHAPE,
            GROWN_LOWER_OUTLINE_SHAPE,
            GROWN_LOWER_OUTLINE_SHAPE};


    public @NotNull MapCodec<CrowsEyeFern> codec() {
        return CODEC;
    }

    public CrowsEyeFern(Properties settings) {
        super(settings);
        this.registerDefaultState(this.stateDefinition.any().setValue(AGE, 0).setValue(HALF, DoubleBlockHalf.LOWER));
    }

    @Override
    public @NotNull ItemStack getCloneItemStack(@NotNull LevelReader world, @NotNull BlockPos pos, @NotNull BlockState state) {
        return new ItemStack(TCOTS_Items_Fabric.CROWS_EYE);
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState state, @NotNull BlockGetter world, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        Vec3 offset = state.getOffset(world, pos);

        return state.getValue(HALF) == DoubleBlockHalf.UPPER ?
                UPPER_OUTLINE_SHAPES[Math.min(Math.abs(4 - (state.getValue(AGE) + 1)), UPPER_OUTLINE_SHAPES.length - 1)].move(offset.x, offset.y, offset.z) :
                LOWER_OUTLINE_SHAPES[state.getValue(AGE)].move(offset.x, offset.y, offset.z);
    }

    @Override
    public @NotNull BlockState updateShape(BlockState state, @NotNull Direction direction, @NotNull BlockState neighborState, @NotNull LevelAccessor world, @NotNull BlockPos pos, @NotNull BlockPos neighborPos) {
        if (isDoubleTallAtAge(state.getValue(AGE))) {
            return super.updateShape(state, direction, neighborState, world, pos, neighborPos);
        }
        return state.canSurvive(world, pos) ? state : Blocks.AIR.defaultBlockState();
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AGE);
        super.createBlockStateDefinition(builder);
    }

    @Override
    public void entityInside(@NotNull BlockState state, @NotNull Level world, @NotNull BlockPos pos, @NotNull Entity entity) {
        if (entity instanceof Ravager && world.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
            world.destroyBlock(pos, true, entity);
        }
        super.entityInside(state, world, pos, entity);
    }

    @Override
    public void setPlacedBy(@NotNull Level world, @NotNull BlockPos pos, @NotNull BlockState state, @NotNull LivingEntity placer, @NotNull ItemStack itemStack) {
    }

    @Override
    public boolean isRandomlyTicking(BlockState state) {
        return state.getValue(HALF) == DoubleBlockHalf.LOWER && !this.isFullyGrown(state);
    }

    @SuppressWarnings("all")
    private boolean isFullyGrown(BlockState state) {
        return state.getValue(AGE) >= 4;
    }

    @Override
    public void randomTick(BlockState state, @NotNull ServerLevel world, @NotNull BlockPos pos, @NotNull RandomSource random) {
        int i = state.getValue(AGE);
        if (i < 3 && random.nextInt(5) == 0 && world.getRawBrightness(pos.above(), 0) >= 9) {
            BlockState blockState = state.setValue(AGE, i + 1);
            world.setBlock(pos, blockState, Block.UPDATE_CLIENTS);
            world.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(blockState));
        }
    }

    @Override
    protected @NotNull ItemInteractionResult useItemOn(@NotNull ItemStack stack, BlockState state, @NotNull Level world, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hit) {
        int i = state.getValue(AGE);
        boolean bl = i == 4;
        return !bl && stack.is(Items.BONE_MEAL)
                ? ItemInteractionResult.SKIP_DEFAULT_BLOCK_INTERACTION
                : super.useItemOn(stack, state, world, pos, player, hand, hit);
    }

    @Override
    public @NotNull InteractionResult useWithoutItem(BlockState state, @NotNull Level world, @NotNull BlockPos pos, @NotNull Player player, @NotNull BlockHitResult hit) {
        int i = state.getValue(AGE);
        if (i > 3) {
            int j = 1 + world.random.nextInt(4);
            SweetBerryBushBlock.popResource(world, pos, new ItemStack(TCOTS_Items_Fabric.CROWS_EYE, j));

            world.playSound(null, pos, TCOTS_Sounds.INGREDIENT_POPS, SoundSource.BLOCKS, 1.0f, 0.8f + world.random.nextFloat() * 0.4f);

            BlockState ageState = state.setValue(AGE, 3);

            //Changes itself
            world.setBlock(pos, ageState, Block.UPDATE_CLIENTS);
            world.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(player, ageState));

            //It's the bottom half, so also change the top half
            if(isLowerHalf(state)){
                world.setBlock(pos.above(), ageState.setValue(HALF, DoubleBlockHalf.UPPER), Block.UPDATE_ALL);
                world.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(player, ageState));
            }
            //It's the top, so also change the bottom half
            else {
                world.setBlock(pos.below(), ageState.setValue(HALF, DoubleBlockHalf.LOWER), Block.UPDATE_CLIENTS);
                world.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(player, ageState));
            }

            return InteractionResult.sidedSuccess(world.isClientSide);
        }
        return super.useWithoutItem(state, world, pos, player, hit);
    }

    @Override
    public boolean isValidBonemealTarget(@NotNull LevelReader world, @NotNull BlockPos pos, @NotNull BlockState state) {
        LowerHalfContext lowerHalfContext = this.getLowerHalfContext(world, pos, state);
        if (lowerHalfContext == null) {
            return false;
        }
        return this.canGrow(world, lowerHalfContext.pos, lowerHalfContext.state, lowerHalfContext.state.getValue(AGE) + 1);
    }

    private static boolean canGrowAt(LevelReader world, BlockPos pos) {
        BlockState blockState = world.getBlockState(pos);
        return blockState.isAir() || blockState.is(TCOTS_Blocks_Fabric.CROWS_EYE_FERN);
    }

    private boolean canGrow(LevelReader world, BlockPos pos, BlockState state, int age) {
        return !this.isFullyGrown(state)
                && canSurvive(state, world, pos)
                && (!isDoubleTallAtAge(age) || canGrowAt(world, pos.above()));
    }

    @Override
    public boolean isBonemealSuccess(@NotNull Level world, @NotNull RandomSource random, @NotNull BlockPos pos, @NotNull BlockState state) {
        return true;
    }

    private static boolean isDoubleTallAtAge(int age) {
        return age >= 3;
    }


    private static boolean isLowerHalf(BlockState state) {
        return state.is(TCOTS_Blocks_Fabric.CROWS_EYE_FERN) && state.getValue(HALF) == DoubleBlockHalf.LOWER;
    }

    @Nullable
    private LowerHalfContext getLowerHalfContext(LevelReader world, BlockPos pos, BlockState state) {
        if (isLowerHalf(state)) {
            return new LowerHalfContext(pos, state);
        }
        BlockPos blockPos = pos.below();
        BlockState blockState = world.getBlockState(blockPos);
        if (isLowerHalf(blockState)) {
            return new LowerHalfContext(blockPos, blockState);
        }
        return null;
    }

    @Override
    public void performBonemeal(@NotNull ServerLevel world, @NotNull RandomSource random, @NotNull BlockPos pos, @NotNull BlockState state) {
        LowerHalfContext lowerHalfContext = this.getLowerHalfContext(world, pos, state);
        if (lowerHalfContext == null) {
            return;
        }
        this.tryGrow(world, lowerHalfContext.state, lowerHalfContext.pos, 1);
    }

    @SuppressWarnings("all")
    private void tryGrow(ServerLevel world, BlockState state, BlockPos pos, int amount) {
        int i = Math.min(state.getValue(AGE) + amount, 4);
        if (!this.canGrow(world, pos, state, i)) {
            return;
        }
        BlockState blockState = state.setValue(AGE, i);
        world.setBlock(pos, blockState, Block.UPDATE_CLIENTS);

        //Place the upper half
        if (isDoubleTallAtAge(i)) {
            world.setBlock(pos.above(), blockState.setValue(HALF, DoubleBlockHalf.UPPER), Block.UPDATE_ALL);
        }
    }



    record LowerHalfContext(BlockPos pos, BlockState state) {
    }
}
