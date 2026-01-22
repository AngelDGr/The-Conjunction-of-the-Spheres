package mors.tcots.block.skull;

import mors.tcots.block.entity.NestSkullBlockEntity;
import mors.tcots.client.geo.renderer.block.NestSkullBlockRenderer;
import mors.tcots.registry.TCOTS_Blocks;
import mors.tcots.utils.GeckoAnimationsUtil;
import com.mojang.serialization.MapCodec;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Equipable;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.RotationSegment;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.Map;
import java.util.function.Consumer;


public class NestSkullBlock extends BaseEntityBlock implements Equipable {

    public static final MapCodec<NestSkullBlock> CODEC = NestSkullBlock.simpleCodec(NestSkullBlock::new);
    //Skull
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

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

    public static class NestSkullItem extends BlockItem implements GeoItem {
        private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

        protected final Block wallBlock;
        private final Direction verticalAttachmentDirection;
        public NestSkullItem(final Block block, final Block wallBlock, final Properties settings, final Direction verticalAttachmentDirection) {
            super(block, settings);
            SingletonGeoAnimatable.registerSyncedAnimatable(this);
            this.wallBlock = wallBlock;
            this.verticalAttachmentDirection = verticalAttachmentDirection;
        }

        @Override
        public void createGeoRenderer(final Consumer<GeoRenderProvider> consumer) {
            consumer.accept(new GeoRenderProvider() {

                private final NestSkullBlockRenderer.Item renderer = new NestSkullBlockRenderer.Item();

                @Override
                public @NotNull BlockEntityWithoutLevelRenderer getGeoItemRenderer() {
                    return this.renderer;
                }
            });
        }

        @Override
        public void registerControllers(final AnimatableManager.ControllerRegistrar controllers) {
            controllers.add(GeckoAnimationsUtil.genericIdleController(this));
        }

        @Override
        public AnimatableInstanceCache getAnimatableInstanceCache() {
            return cache;
        }
        protected boolean canPlaceAt(final LevelReader world, final BlockState state, final BlockPos pos) {
            return state.canSurvive(world, pos);
        }

        @Override
        @Nullable
        protected BlockState getPlacementState(@NotNull final BlockPlaceContext context) {
            final BlockState blockState = this.wallBlock.getStateForPlacement(context);
            BlockState blockState2 = null;
            final Level worldView = context.getLevel();
            final BlockPos blockPos = context.getClickedPos();
            for (final Direction direction : context.getNearestLookingDirections()) {
                final BlockState blockState3;
                if (direction == this.verticalAttachmentDirection.getOpposite()) continue;
                blockState3 = direction == this.verticalAttachmentDirection ? this.getBlock().getStateForPlacement(context) : blockState;
                if (blockState3 == null || !this.canPlaceAt(worldView, blockState3, blockPos)) continue;
                blockState2 = blockState3;
                break;
            }
            return blockState2 != null && worldView.isUnobstructed(blockState2, blockPos, CollisionContext.empty()) ? blockState2 : null;
        }

        @Override
        public void registerBlocks(@NotNull final Map<Block, Item> map, @NotNull final Item item) {
            super.registerBlocks(map, item);
            map.put(this.wallBlock, item);
        }


    }
}
