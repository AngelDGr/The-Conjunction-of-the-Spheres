package mors.tcots.block;

import mors.tcots.client.geo.renderer.block.HerbalTableRenderer;
import mors.tcots.registry.TCOTS_Blocks;
import mors.tcots.block.entity.HerbalTableBlockEntity;
import mors.tcots.client.screen.HerbalTableScreenHandler;
import mors.tcots.utils.GeckoAnimationsUtil;
import com.mojang.serialization.MapCodec;
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;
import java.util.function.Consumer;

public class HerbalTableBlock extends BaseEntityBlock implements EntityBlock {

    public static final MapCodec<HerbalTableBlock> CODEC = HerbalTableBlock.simpleCodec(HerbalTableBlock::new);
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    protected static final VoxelShape SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 9.0, 16.0);
    private static final Component TITLE = Component.translatable("gui.tcots_witcher.herbal_table.title");

    @Override
    protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    public HerbalTableBlock(final Properties settings) {
        super(settings);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Nullable
    @Override
    public MenuProvider getMenuProvider(@NotNull final BlockState state, @NotNull final Level world, @NotNull final BlockPos pos) {
        return new SimpleMenuProvider((syncId, inventory, player) -> new HerbalTableScreenHandler(syncId, inventory, ContainerLevelAccess.create(world, pos)), TITLE);
    }

    @Override
    protected @NotNull InteractionResult useWithoutItem(@NotNull final BlockState state, final Level world, @NotNull final BlockPos pos, @NotNull final Player player, @NotNull final BlockHitResult hit) {
        if (world.isClientSide) {
            return InteractionResult.SUCCESS;
        }
        player.openMenu(state.getMenuProvider(world, pos));
        return InteractionResult.CONSUME;
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
    public void appendHoverText(@NotNull final ItemStack stack, final net.minecraft.world.item.Item.@NotNull TooltipContext context, @NotNull final List<Component> tooltip, @NotNull final TooltipFlag options) {
        super.appendHoverText(stack, context, tooltip, options);
        tooltip.add(Component.translatable("block.tcots_witcher.herbal_table.tooltip").withStyle(ChatFormatting.GRAY));
    }

    @Override
    public BlockState getStateForPlacement(final BlockPlaceContext ctx) {
        return this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite());
    }

    @Override
    public @NotNull BlockState rotate(final BlockState state, final Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public @NotNull BlockState mirror(final BlockState state, final Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull final BlockPos pos, @NotNull final BlockState state) {
        return new HerbalTableBlockEntity(TCOTS_Blocks.HerbalTableEntity(), pos, state);
    }

    public static class Item extends BlockItem implements GeoItem {

        private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

        public Item(final Block block, final Properties settings) {
            super(block, settings);
        }

        @Override
        public void createGeoRenderer(final Consumer<GeoRenderProvider> consumer) {
            consumer.accept(new GeoRenderProvider() {

                private final HerbalTableRenderer.Item renderer = new HerbalTableRenderer.Item();

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
    }
}
