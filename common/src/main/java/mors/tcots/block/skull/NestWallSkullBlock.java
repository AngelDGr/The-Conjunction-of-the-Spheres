package mors.tcots.block.skull;

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
    public NestWallSkullBlock(final Properties settings) {
        super(settings);
        this.registerDefaultState(this.stateDefinition.any().setValue(NestSkullBlock.FACING, Direction.NORTH));
    }
    @Override
    public @NotNull String getDescriptionId() {
        return this.asItem().getDescriptionId();
    }

    @Override
    public @NotNull VoxelShape getShape(@NotNull final BlockState state, @NotNull final BlockGetter world, @NotNull final BlockPos pos, @NotNull final CollisionContext context) {
        return FACING_TO_SHAPE.get(state.getValue(NestSkullBlock.FACING));
    }

    @Override
    public BlockState getStateForPlacement(final BlockPlaceContext ctx) {
        BlockState blockState = this.defaultBlockState();
        final Level blockView = ctx.getLevel();
        final BlockPos blockPos = ctx.getClickedPos();
        for (final Direction direction : ctx.getNearestLookingDirections()) {
            if (!direction.getAxis().isHorizontal()) continue;
            final Direction direction2 = direction.getOpposite();
            blockState = blockState.setValue(NestSkullBlock.FACING, direction2);
            if (blockView.getBlockState(blockPos.relative(direction)).canBeReplaced(ctx)) continue;
            return blockState;
        }
        return null;
    }

    @Override
    public @NotNull BlockState rotate(final BlockState state, final Rotation rotation) {
        return state.setValue(NestSkullBlock.FACING, rotation.rotate(state.getValue(NestSkullBlock.FACING)));
    }

    @Override
    public @NotNull BlockState mirror(final BlockState state, final Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(NestSkullBlock.FACING)));
    }

    @Override
    protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(NestSkullBlock.FACING);
    }

}
