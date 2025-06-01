package TCOTS.blocks.plants;

import TCOTS.registry.TCOTS_Items;

import java.util.function.BiFunction;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.MushroomBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class SewantMushroomsPlant extends MushroomBlock {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public static final IntegerProperty MUSHROOM_AMOUNT = IntegerProperty.create("mushroom_amount", 1, 4);
    private static final BiFunction<Direction, Integer, VoxelShape> FACING_AND_AMOUNT_TO_SHAPE = Util.memoize((facing, flowerAmount) -> {
        VoxelShape[] voxelShapes = new VoxelShape[]{
                Block.box(8.0, 0.0, 8.0, 16.0, 3.0, 16.0),
                Block.box(8.0, 0.0, 0.0, 16.0, 5.0, 8.0),
                Block.box(0.0, 0.0, 0.0, 8.0, 7.0, 8.0),
                Block.box(0.0, 0.0, 8.0, 8.0, 7.0, 16.0)};
        VoxelShape voxelShape = Shapes.empty();
        for (int i = 0; i < flowerAmount; ++i) {
            int j = Math.floorMod(i - facing.get2DDataValue(), 4);
            voxelShape = Shapes.or(voxelShape, voxelShapes[j]);
        }
        return voxelShape.singleEncompassing();
    });


    public SewantMushroomsPlant(ResourceKey<ConfiguredFeature<?, ?>> featureKey, Properties settings) {
        super(featureKey, settings);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(MUSHROOM_AMOUNT, 1));
    }

    @Override
    public @NotNull ItemStack getCloneItemStack(@NotNull LevelReader world, @NotNull BlockPos pos, @NotNull BlockState state) {
        return new ItemStack(TCOTS_Items.SEWANT_MUSHROOMS);
    }

    @Override
    public @NotNull BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public @NotNull BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    public boolean canBeReplaced(@NotNull BlockState state, BlockPlaceContext context) {
        if (!context.isSecondaryUseActive() && context.getItemInHand().is(this.asItem()) && state.getValue(MUSHROOM_AMOUNT) < 4) {
            return true;
        }
        return super.canBeReplaced(state, context);
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState state, @NotNull BlockGetter world, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return FACING_AND_AMOUNT_TO_SHAPE.apply(state.getValue(FACING), state.getValue(MUSHROOM_AMOUNT));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        BlockState blockState = ctx.getLevel().getBlockState(ctx.getClickedPos());
        if (blockState.is(this)) {
            return blockState.setValue(MUSHROOM_AMOUNT, Math.min(4, blockState.getValue(MUSHROOM_AMOUNT) + 1));
        }
        return this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, MUSHROOM_AMOUNT);
    }

    @Override
    public boolean isBonemealSuccess(@NotNull Level world, @NotNull RandomSource random, @NotNull BlockPos pos, BlockState state) {
        if(state.getValue(MUSHROOM_AMOUNT)<4){
            return true;
        }
        return (double)random.nextFloat() < 0.4;
    }

    @Override
    public void performBonemeal(@NotNull ServerLevel world, @NotNull RandomSource random, @NotNull BlockPos pos, BlockState state) {
        int i = state.getValue(MUSHROOM_AMOUNT);
        if (i < 4) {
            world.setBlock(pos, state.setValue(MUSHROOM_AMOUNT, i + 1), Block.UPDATE_CLIENTS);
        } else {
            this.growMushroom(world, pos, state, random);
        }
    }

    @Override
    public void randomTick(BlockState state, @NotNull ServerLevel world, @NotNull BlockPos pos, @NotNull RandomSource random) {
        int amount = state.getValue(MUSHROOM_AMOUNT);

        if (amount < 4 && random.nextInt(10) == 0 &&
                (world.getRawBrightness(pos, 0) < 13
                || world.getBlockState(pos.below()).is(BlockTags.MUSHROOM_GROW_BLOCK)))
        {
            BlockState blockState = state.setValue(MUSHROOM_AMOUNT, amount + 1);
            world.setBlock(pos, blockState, Block.UPDATE_CLIENTS);
            world.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(blockState));
        }

        if (random.nextInt(25) == 0) {
            int i = 5;
            for (BlockPos blockPos : BlockPos.betweenClosed(pos.offset(-4, -1, -4), pos.offset(4, 1, 4))) {
                if (!world.getBlockState(blockPos).is(this) || --i > 0) continue;
                return;
            }
            BlockPos blockPos2 = pos.offset(random.nextInt(3) - 1, random.nextInt(2) - random.nextInt(2), random.nextInt(3) - 1);
            for (int k = 0; k < 4; ++k) {
                if (world.isEmptyBlock(blockPos2) && state.canSurvive(world, blockPos2)) {
                    pos = blockPos2;
                }
                blockPos2 = pos.offset(random.nextInt(3) - 1, random.nextInt(2) - random.nextInt(2), random.nextInt(3) - 1);
            }
            if (world.isEmptyBlock(blockPos2) && state.canSurvive(world, blockPos2)) {
                world.setBlock(blockPos2, state.setValue(MUSHROOM_AMOUNT,1), Block.UPDATE_CLIENTS);
            }
        }
    }
}
