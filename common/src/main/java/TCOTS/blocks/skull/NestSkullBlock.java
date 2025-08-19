package TCOTS.blocks.skull;

import TCOTS.blocks.entity.NestSkullBlockEntity;
import TCOTS.registry.TCOTS_Blocks;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Equipable;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.RotationSegment;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class NestSkullBlock extends BaseEntityBlock implements Equipable {

    public static final MapCodec<NestSkullBlock> CODEC = NestSkullBlock.simpleCodec(NestSkullBlock::new);

    public @NotNull MapCodec<NestSkullBlock> codec() {
        return CODEC;
    }

    public static final int MAX_ROTATION_INDEX = RotationSegment.getMaxSegmentIndex();
    private static final int MAX_ROTATIONS = MAX_ROTATION_INDEX + 1;
    public static final IntegerProperty ROTATION = BlockStateProperties.ROTATION_16;
    protected static final VoxelShape SHAPE = Block.box(4.0, 0.0, 4.0, 12.0, 8.0, 12.0);
    public NestSkullBlock(final Properties settings) {
        super(settings);
        this.registerDefaultState(this.stateDefinition.any().setValue(ROTATION, 0));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull final BlockPos pos, @NotNull final BlockState state) {
        return new NestSkullBlockEntity(TCOTS_Blocks.SkullNestEntity(), pos, state);
    }


    @Override
    public @NotNull RenderShape getRenderShape(@NotNull final BlockState state){
        return RenderShape.MODEL;
    }

    @Override
    public @NotNull VoxelShape getShape(@NotNull final BlockState state, @NotNull final BlockGetter world, @NotNull final BlockPos pos, @NotNull final CollisionContext context) {
        return SHAPE;
    }

    @Override
    public @NotNull VoxelShape getOcclusionShape(@NotNull final BlockState state, @NotNull final BlockGetter world, @NotNull final BlockPos pos) {
        return Shapes.empty();
    }

    @Override
    public BlockState getStateForPlacement(final BlockPlaceContext ctx) {
        return this.defaultBlockState().setValue(ROTATION, RotationSegment.convertToSegment(ctx.getRotation()));
    }

    @Override
    public @NotNull BlockState rotate(final BlockState state, final Rotation rotation) {
        return state.setValue(ROTATION, rotation.rotate(state.getValue(ROTATION), MAX_ROTATIONS));
    }

    @Override
    public @NotNull BlockState mirror(final BlockState state, final Mirror mirror) {
        return state.setValue(ROTATION, mirror.mirror(state.getValue(ROTATION), MAX_ROTATIONS));
    }

    @Override
    protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(ROTATION);
    }


    @Override
    public float getDestroyProgress(@NotNull final BlockState state, final Player player, @NotNull final BlockGetter world, @NotNull final BlockPos pos) {
        if (player.getMainHandItem().getItem() instanceof SwordItem) {
            return 0.8f;
        }
        return super.getDestroyProgress(state, player, world, pos);
    }

    @Override
    protected boolean isPathfindable(@NotNull final BlockState state, @NotNull final PathComputationType type) {
        return false;
    }

    @Override
    public @NotNull EquipmentSlot getEquipmentSlot() {
        return EquipmentSlot.HEAD;
    }
}
