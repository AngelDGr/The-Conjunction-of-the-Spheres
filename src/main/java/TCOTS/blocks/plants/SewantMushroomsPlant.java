package TCOTS.blocks.plants;

import TCOTS.items.TCOTS_Items;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.MushroomPlantBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.event.GameEvent;
import net.minecraft.world.gen.feature.ConfiguredFeature;

import java.util.function.BiFunction;

@SuppressWarnings("deprecation")
public class SewantMushroomsPlant extends MushroomPlantBlock {

    public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;
    public static final IntProperty MUSHROOM_AMOUNT = IntProperty.of("mushroom_amount", 1, 4);
    private static final BiFunction<Direction, Integer, VoxelShape> FACING_AND_AMOUNT_TO_SHAPE = Util.memoize((facing, flowerAmount) -> {
        VoxelShape[] voxelShapes = new VoxelShape[]{
                Block.createCuboidShape(8.0, 0.0, 8.0, 16.0, 3.0, 16.0),
                Block.createCuboidShape(8.0, 0.0, 0.0, 16.0, 5.0, 8.0),
                Block.createCuboidShape(0.0, 0.0, 0.0, 8.0, 7.0, 8.0),
                Block.createCuboidShape(0.0, 0.0, 8.0, 8.0, 7.0, 16.0)};
        VoxelShape voxelShape = VoxelShapes.empty();
        for (int i = 0; i < flowerAmount; ++i) {
            int j = Math.floorMod(i - facing.getHorizontal(), 4);
            voxelShape = VoxelShapes.union(voxelShape, voxelShapes[j]);
        }
        return voxelShape.asCuboid();
    });


    public SewantMushroomsPlant(RegistryKey<ConfiguredFeature<?, ?>> featureKey, Settings settings) {
        super(featureKey, settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH).with(MUSHROOM_AMOUNT, 1));
    }

    @Override
    public ItemStack getPickStack(WorldView world, BlockPos pos, BlockState state) {
        return new ItemStack(TCOTS_Items.SEWANT_MUSHROOMS);
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation(state.get(FACING)));
    }

    @Override
    public boolean canReplace(BlockState state, ItemPlacementContext context) {
        if (!context.shouldCancelInteraction() && context.getStack().isOf(this.asItem()) && state.get(MUSHROOM_AMOUNT) < 4) {
            return true;
        }
        return super.canReplace(state, context);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return FACING_AND_AMOUNT_TO_SHAPE.apply(state.get(FACING), state.get(MUSHROOM_AMOUNT));
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockState blockState = ctx.getWorld().getBlockState(ctx.getBlockPos());
        if (blockState.isOf(this)) {
            return blockState.with(MUSHROOM_AMOUNT, Math.min(4, blockState.get(MUSHROOM_AMOUNT) + 1));
        }
        return this.getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing().getOpposite());
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, MUSHROOM_AMOUNT);
    }

    @Override
    public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
        if(state.get(MUSHROOM_AMOUNT)<4){
            return true;
        }
        return (double)random.nextFloat() < 0.4;
    }

    @Override
    public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
        int i = state.get(MUSHROOM_AMOUNT);
        if (i < 4) {
            world.setBlockState(pos, state.with(MUSHROOM_AMOUNT, i + 1), Block.NOTIFY_LISTENERS);
        } else {
            this.trySpawningBigMushroom(world, pos, state, random);
        }
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        int amount = state.get(MUSHROOM_AMOUNT);

        if (amount < 4 && random.nextInt(10) == 0 &&
                (world.getBaseLightLevel(pos, 0) < 13
                || world.getBlockState(pos.down()).isIn(BlockTags.MUSHROOM_GROW_BLOCK)))
        {
            BlockState blockState = state.with(MUSHROOM_AMOUNT, amount + 1);
            world.setBlockState(pos, blockState, Block.NOTIFY_LISTENERS);
            world.emitGameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Emitter.of(blockState));
        }

        if (random.nextInt(25) == 0) {
            int i = 5;
            for (BlockPos blockPos : BlockPos.iterate(pos.add(-4, -1, -4), pos.add(4, 1, 4))) {
                if (!world.getBlockState(blockPos).isOf(this) || --i > 0) continue;
                return;
            }
            BlockPos blockPos2 = pos.add(random.nextInt(3) - 1, random.nextInt(2) - random.nextInt(2), random.nextInt(3) - 1);
            for (int k = 0; k < 4; ++k) {
                if (world.isAir(blockPos2) && state.canPlaceAt(world, blockPos2)) {
                    pos = blockPos2;
                }
                blockPos2 = pos.add(random.nextInt(3) - 1, random.nextInt(2) - random.nextInt(2), random.nextInt(3) - 1);
            }
            if (world.isAir(blockPos2) && state.canPlaceAt(world, blockPos2)) {
                world.setBlockState(blockPos2, state.with(MUSHROOM_AMOUNT,1), Block.NOTIFY_LISTENERS);
            }
        }
    }
}
