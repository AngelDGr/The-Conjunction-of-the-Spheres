package TCOTS.blocks;

import TCOTS.blocks.entity.GiantAnchorBlockEntity;
import TCOTS.registry.TCOTS_Blocks;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class GiantAnchorBlock extends BaseEntityBlock {
    public static final MapCodec<GiantAnchorBlock> CODEC = GiantAnchorBlock.simpleCodec(GiantAnchorBlock::new);

    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    protected static final VoxelShape BOTTOM_SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 8.0, 16.0);
    protected static final VoxelShape TOP_NORTH = Block.box(0.0, 8.0, 8.0, 16.0, 16.0, 16.0);
    protected static final VoxelShape TOP_SOUTH = Block.box(0.0, 8.0, 0.0, 16.0, 16.0, 8.0);
    protected static final VoxelShape TOP_EAST = Block.box(0.0, 8.0, 0.0, 8.0, 16.0, 16.0);
    protected static final VoxelShape TOP_WEST = Block.box(8.0, 8.0, 0.0, 16.0, 16.0, 16.0);

    private static VoxelShape composeShape(Direction direction){
        return switch (direction){
            case DOWN, NORTH, UP -> putTopPart(TOP_NORTH);

            case SOUTH -> putTopPart(TOP_SOUTH);
            case WEST -> putTopPart(TOP_WEST);
            case EAST -> putTopPart(TOP_EAST);
        };
    }

    private static VoxelShape putTopPart(VoxelShape topPart){
        return Shapes.or(BOTTOM_SHAPE, topPart);
    }

    public GiantAnchorBlock(Properties settings) {
        super(settings);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    public @NotNull RenderShape getRenderShape(@NotNull BlockState state){
        return RenderShape.MODEL;
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState state, @NotNull BlockGetter world, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return composeShape(state.getValue(FACING));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite());
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
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public boolean useShapeForLightOcclusion(@NotNull BlockState state) {
        return true;
    }

    @Override
    protected boolean isPathfindable(@NotNull BlockState state, @NotNull PathComputationType type) {
        return false;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new GiantAnchorBlockEntity(TCOTS_Blocks.GiantAnchorEntity(), pos, state);
    }
}
