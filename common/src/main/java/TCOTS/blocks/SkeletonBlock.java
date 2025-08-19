package TCOTS.blocks;

import TCOTS.blocks.entity.SkeletonBlockEntity;
import TCOTS.registry.TCOTS_Blocks;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.block.state.properties.RotationSegment;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SkeletonBlock extends BaseEntityBlock {
    public static final MapCodec<SkeletonBlock> CODEC = SkeletonBlock.simpleCodec(SkeletonBlock::new);
    protected static VoxelShape LITTLE_CUBE = Block.box(1.0, 0.0, 1.0, 15.0, 14.0, 15.0);
    protected static VoxelShape LEGS = Block.box(1.0, 0.0, 1.0, 15.0, 5.0, 15.0);
    protected static VoxelShape HALF_BODY = Block.box(0.0, 0.0, 0.0, 16.0, 6.0, 16.0);

    public static final int MAX_ROTATION_INDEX = RotationSegment.getMaxSegmentIndex();
    private static final int MAX_ROTATIONS = MAX_ROTATION_INDEX + 1;

    protected SkeletonBlock(final Properties settings) {
        super(settings);
    }
    public SkeletonBlock() {
        this(Properties.of().strength(2.0f).sound(SoundType.BONE_BLOCK).instrument(NoteBlockInstrument.XYLOPHONE).pushReaction(PushReaction.DESTROY).noCollission().noOcclusion());
        this.registerDefaultState(this.defaultBlockState().setValue(TCOTS_Blocks.HAS_ARMOR, false)
                .setValue(TCOTS_Blocks.ROTATION, RotationSegment.convertToSegment(Direction.NORTH))
                .setValue(TCOTS_Blocks.SHAPE, 0)
                .setValue(TCOTS_Blocks.HIDE_HEAD, false));
    }

    private static VoxelShape getShape(final BlockState state){
        return switch (state.getValue(TCOTS_Blocks.SHAPE)){
            case 0, 3, 4, 5  -> HALF_BODY;
            case 1  -> LEGS;
            default -> LITTLE_CUBE;
        };
    }

    @Override
    public @NotNull VoxelShape getShape(@NotNull final BlockState state, @NotNull final BlockGetter world, @NotNull final BlockPos pos, @NotNull final CollisionContext context) {
        return getShape(state);
    }

    @Override
    public BlockState getStateForPlacement(final BlockPlaceContext ctx) {
        return this.defaultBlockState().setValue(TCOTS_Blocks.ROTATION, RotationSegment.convertToSegment(ctx.getRotation()));
    }

    @Override
    public @NotNull BlockState rotate(final BlockState state, final Rotation rotation) {
        return state.setValue(TCOTS_Blocks.ROTATION, rotation.rotate(state.getValue(TCOTS_Blocks.ROTATION), MAX_ROTATIONS));
    }

    @Override
    public @NotNull BlockState mirror(final BlockState state, final Mirror mirror) {
        return state.setValue(TCOTS_Blocks.ROTATION, mirror.mirror(state.getValue(TCOTS_Blocks.ROTATION), MAX_ROTATIONS));
    }

    @Override
    protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(TCOTS_Blocks.ROTATION);
        builder.add(TCOTS_Blocks.HAS_ARMOR);
        builder.add(TCOTS_Blocks.SHAPE);
        builder.add(TCOTS_Blocks.HIDE_HEAD);
    }

    @Override
    public boolean useShapeForLightOcclusion(@NotNull final BlockState state) {
        return true;
    }

    @Override
    protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public @NotNull RenderShape getRenderShape(@NotNull final BlockState state){
        return RenderShape.MODEL;
    }

    @Override
    protected boolean isPathfindable(@NotNull final BlockState state, @NotNull final PathComputationType type) {
        if (type == PathComputationType.AIR && !this.hasCollision) {
            return true;
        }
        return super.isPathfindable(state, type);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull final BlockPos pos, @NotNull final BlockState state) {
        return new SkeletonBlockEntity(TCOTS_Blocks.SkeletonBlockEntity(), pos, state);
    }
}

