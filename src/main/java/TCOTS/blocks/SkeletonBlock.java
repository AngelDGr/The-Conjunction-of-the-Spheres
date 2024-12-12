package TCOTS.blocks;

import TCOTS.blocks.entity.SkeletonBlockEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.enums.Instrument;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationPropertyHelper;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("deprecation")
public class SkeletonBlock extends BlockWithEntity {
    protected static VoxelShape LITTLE_CUBE = Block.createCuboidShape(1.0, 0.0, 1.0, 15.0, 14.0, 15.0);
    protected static VoxelShape LEGS = Block.createCuboidShape(1.0, 0.0, 1.0, 15.0, 5.0, 15.0);
    protected static VoxelShape HALF_BODY = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 6.0, 16.0);

    public static final int MAX_ROTATION_INDEX = RotationPropertyHelper.getMax();
    private static final int MAX_ROTATIONS = MAX_ROTATION_INDEX + 1;
    public static final IntProperty ROTATION = Properties.ROTATION;
    public static final IntProperty SHAPE = IntProperty.of("shape", 0, 5);
    public static final BooleanProperty HAS_ARMOR = BooleanProperty.of("armor");
    public static final BooleanProperty HIDE_HEAD = BooleanProperty.of("hide_head");

    protected SkeletonBlock(Settings settings) {
        super(settings);
    }
    public SkeletonBlock() {
        this(FabricBlockSettings.create().strength(2.0f).sounds(BlockSoundGroup.BONE).instrument(Instrument.XYLOPHONE).pistonBehavior(PistonBehavior.DESTROY).noCollision().nonOpaque());
        this.setDefaultState(this.getDefaultState().with(HAS_ARMOR, false)
                .with(ROTATION, RotationPropertyHelper.fromDirection(Direction.NORTH))
                .with(SHAPE, 0)
                .with(HIDE_HEAD, false));
    }

    private static VoxelShape getShape(BlockState state){
        return switch (state.get(SHAPE)){
            case 0, 3, 4, 5  -> HALF_BODY;
            case 1  -> LEGS;
            default -> LITTLE_CUBE;
        };
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return getShape(state);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(ROTATION, RotationPropertyHelper.fromYaw(ctx.getPlayerYaw()));
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(ROTATION, rotation.rotate(state.get(ROTATION), MAX_ROTATIONS));
    }

    @Override
    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.with(ROTATION, mirror.mirror(state.get(ROTATION), MAX_ROTATIONS));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(ROTATION);
        builder.add(HAS_ARMOR);
        builder.add(SHAPE);
        builder.add(HIDE_HEAD);
    }

    @Override
    public boolean hasSidedTransparency(BlockState state) {
        return true;
    }

    @Override
    public BlockRenderType getRenderType(BlockState state){
        return BlockRenderType.MODEL;
    }

    @Override
    public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
        if (type == NavigationType.AIR && !this.collidable) {
            return true;
        }
        return super.canPathfindThrough(state, world, pos, type);
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new SkeletonBlockEntity(pos, state);
    }
}

