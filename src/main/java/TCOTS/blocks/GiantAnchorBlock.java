package TCOTS.blocks;

import TCOTS.blocks.entity.GiantAnchorBlockEntity;
import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("deprecation")
public class GiantAnchorBlock extends BlockWithEntity {
    public static final MapCodec<GiantAnchorBlock> CODEC = GiantAnchorBlock.createCodec(GiantAnchorBlock::new);
    public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;
    protected static final VoxelShape BOTTOM_SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 8.0, 16.0);
    protected static final VoxelShape TOP_NORTH = Block.createCuboidShape(0.0, 8.0, 8.0, 16.0, 16.0, 16.0);
    protected static final VoxelShape TOP_SOUTH = Block.createCuboidShape(0.0, 8.0, 0.0, 16.0, 16.0, 8.0);
    protected static final VoxelShape TOP_EAST = Block.createCuboidShape(0.0, 8.0, 0.0, 8.0, 16.0, 16.0);
    protected static final VoxelShape TOP_WEST = Block.createCuboidShape(8.0, 8.0, 0.0, 16.0, 16.0, 16.0);

    private static VoxelShape composeShape(Direction direction){
        return switch (direction){
            case DOWN, NORTH, UP -> putTopPart(TOP_NORTH);

            case SOUTH -> putTopPart(TOP_SOUTH);
            case WEST -> putTopPart(TOP_WEST);
            case EAST -> putTopPart(TOP_EAST);
        };
    }

    private static VoxelShape putTopPart(VoxelShape topPart){
        return VoxelShapes.union(BOTTOM_SHAPE, topPart);
    }


    protected GiantAnchorBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH));
    }


    @Override
    public BlockRenderType getRenderType(BlockState state){
        return BlockRenderType.MODEL;
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return composeShape(state.get(FACING));
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing().getOpposite());
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
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return CODEC;
    }

    @Override
    public boolean hasSidedTransparency(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new GiantAnchorBlockEntity(pos, state);
    }
}
