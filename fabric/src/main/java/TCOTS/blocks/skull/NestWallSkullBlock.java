package TCOTS.blocks.skull;

import TCOTS.blocks.TCOTS_Blocks;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import java.util.Map;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class NestWallSkullBlock extends NestSkullBlock {

    private static final Map<Direction, VoxelShape> FACING_TO_SHAPE = Maps.newEnumMap(ImmutableMap.of(Direction.NORTH, Block.box(4.0, 4.0, 8.0, 12.0, 12.0, 16.0), Direction.SOUTH, Block.box(4.0, 4.0, 0.0, 12.0, 12.0, 8.0), Direction.EAST, Block.box(0.0, 4.0, 4.0, 8.0, 12.0, 12.0), Direction.WEST, Block.box(8.0, 4.0, 4.0, 16.0, 12.0, 12.0)));
    public NestWallSkullBlock(Properties settings) {
        super(settings);
        this.registerDefaultState(this.stateDefinition.any().setValue(TCOTS_Blocks.FACING, Direction.NORTH));
    }
    @Override
    public @NotNull String getDescriptionId() {
        return this.asItem().getDescriptionId();
    }

    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter world, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return FACING_TO_SHAPE.get(state.getValue(TCOTS_Blocks.FACING));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        BlockState blockState = this.defaultBlockState();
        Level blockView = ctx.getLevel();
        BlockPos blockPos = ctx.getClickedPos();
        for (Direction direction : ctx.getNearestLookingDirections()) {
            if (!direction.getAxis().isHorizontal()) continue;
            Direction direction2 = direction.getOpposite();
            blockState = blockState.setValue(TCOTS_Blocks.FACING, direction2);
            if (blockView.getBlockState(blockPos.relative(direction)).canBeReplaced(ctx)) continue;
            return blockState;
        }
        return null;
    }

    @Override
    public @NotNull BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(TCOTS_Blocks.FACING, rotation.rotate(state.getValue(TCOTS_Blocks.FACING)));
    }

    @Override
    public @NotNull BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(TCOTS_Blocks.FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(TCOTS_Blocks.FACING);
    }

}
