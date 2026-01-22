package mors.tcots.block;

import mors.tcots.block.entity.SkeletonBlockEntity;
import mors.tcots.client.geo.renderer.block.SkeletonBlockRenderer;
import mors.tcots.registry.TCOTS_Blocks;
import com.mojang.serialization.MapCodec;
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
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
import net.minecraft.world.level.block.state.properties.*;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;
import java.util.function.Consumer;

public class SkeletonBlock extends BaseEntityBlock {
    public static final MapCodec<SkeletonBlock> CODEC = SkeletonBlock.simpleCodec(SkeletonBlock::new);
    //Skeleton Body
    public static final IntegerProperty ROTATION = BlockStateProperties.ROTATION_16;
    public static final IntegerProperty SHAPE = IntegerProperty.create("shape", 0, 5);
    public static final BooleanProperty HAS_ARMOR = BooleanProperty.create("armor");
    public static final BooleanProperty HIDE_HEAD = BooleanProperty.create("hide_head");
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
        this.registerDefaultState(this.defaultBlockState().setValue(HAS_ARMOR, false)
                .setValue(ROTATION, RotationSegment.convertToSegment(Direction.NORTH))
                .setValue(SHAPE, 0)
                .setValue(HIDE_HEAD, false));
    }

    private static VoxelShape getShape(final BlockState state){
        return switch (state.getValue(SHAPE)){
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
        builder.add(HAS_ARMOR);
        builder.add(SHAPE);
        builder.add(HIDE_HEAD);
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

    public static class Item extends BlockItem implements GeoItem {
        private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
        public Item(final Block block, final Properties settings) {
            super(block, settings);
        }

        @Override
        public void createGeoRenderer(final Consumer<GeoRenderProvider> consumer) {
            consumer.accept(new GeoRenderProvider() {
    
                private final SkeletonBlockRenderer.Item renderer = new SkeletonBlockRenderer.Item();

                @Override
                public @NotNull BlockEntityWithoutLevelRenderer getGeoItemRenderer() {
                    return this.renderer;
                }

            });
        }

        @Override
        public void appendHoverText(@NotNull final ItemStack stack, @NotNull final TooltipContext context, final List<Component> tooltip, @NotNull final TooltipFlag type) {
            tooltip.add(Component.translatable("block.tcots_witcher.skeleton_block.tooltip1").withStyle(ChatFormatting.DARK_PURPLE, ChatFormatting.ITALIC));
            tooltip.add(Component.translatable("block.tcots_witcher.skeleton_block.tooltip2").withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
        }
        public static final RawAnimation SITTING = RawAnimation.begin().thenPlayAndHold("pose.sitting");
        @Override
        public void registerControllers(final AnimatableManager.ControllerRegistrar controllers) {
            controllers.add(new AnimationController<>(this, 0, state -> {
                        state.setControllerSpeed(1);
                        state.getController().transitionLength(0);

                        return state.setAndContinue(SITTING);
                    }
                    )
            );
        }

        @Override
        public AnimatableInstanceCache getAnimatableInstanceCache() {
            return cache;
        }
    }
}

