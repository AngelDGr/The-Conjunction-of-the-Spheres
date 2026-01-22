package mors.tcots.block;

import mors.tcots.client.geo.renderer.block.MonsterNestRenderer;
import mors.tcots.registry.TCOTS_Blocks;
import mors.tcots.block.entity.MonsterNestBlockEntity;
import mors.tcots.entity.misc.WitcherBombEntity;
import mors.tcots.registry.TCOTS_Criteria;
import mors.tcots.utils.GeckoAnimationsUtil;
import mors.tcots.utils.TCOTS_Util;
import com.mojang.serialization.MapCodec;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.Spawner;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class MonsterNestBlock extends BaseEntityBlock {
    public static final MapCodec<MonsterNestBlock> CODEC = MonsterNestBlock.simpleCodec(MonsterNestBlock::new);
    public @NotNull MapCodec<MonsterNestBlock> codec() {
        return CODEC;
    }

    public MonsterNestBlock(final Properties settings) {
        super(settings);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull final BlockPos pos, @NotNull final BlockState state) {
        return new MonsterNestBlockEntity(TCOTS_Blocks.MonsterNestBlockEntity(), pos, state);
    }

    @Override
    public void playerDestroy(@NotNull final Level world, @NotNull final Player player, @NotNull final BlockPos pos, @NotNull final BlockState state, @Nullable final BlockEntity blockEntity, @NotNull final ItemStack tool) {
        super.playerDestroy(world, player, pos, state, blockEntity, tool);
        if(player instanceof final ServerPlayer serverPlayer){
            TCOTS_Criteria.DestroyMultipleMonsterNest().trigger(serverPlayer, serverPlayer.getStats().getValue(Stats.BLOCK_MINED.get(this)));
        }
    }

    @Override
    public void onExplosionHit(@NotNull final BlockState state, @NotNull final Level world, @NotNull final BlockPos pos, final Explosion explosion, @NotNull final BiConsumer<ItemStack, BlockPos> stackMerger) {
        if(explosion.getDirectSourceEntity()!=null && explosion.getDirectSourceEntity() instanceof final WitcherBombEntity bomb && bomb.getOwner() instanceof final Player player){
            if(player instanceof final ServerPlayer serverPlayer){
                TCOTS_Criteria.DestroyMonsterNest().trigger(serverPlayer);

                serverPlayer.awardStat(Stats.BLOCK_MINED.get(this));

                TCOTS_Criteria.DestroyMultipleMonsterNest().trigger(serverPlayer, serverPlayer.getStats().getValue(Stats.BLOCK_MINED.get(this)));
            }
        }

        world.destroyBlock(pos, true);
    }

    @Override
    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(final Level world, @NotNull final BlockState state, @NotNull final BlockEntityType<T> type) {
        return MonsterNestBlock.createTickerHelper(type, TCOTS_Blocks.MonsterNestBlockEntity(), world.isClientSide ? MonsterNestBlockEntity::clientTick : MonsterNestBlockEntity::serverTick);
    }

    @Override
    public @NotNull RenderShape getRenderShape(@NotNull final BlockState state){
        return RenderShape.MODEL;
    }

    protected static final VoxelShape SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 15.0, 16.0);
    @Override
    public @NotNull VoxelShape getShape(@NotNull final BlockState state, @NotNull final BlockGetter world, @NotNull final BlockPos pos, @NotNull final CollisionContext context) {
        return SHAPE;
    }



    @Override
    public float getDestroyProgress(@NotNull final BlockState state, final Player player, @NotNull final BlockGetter world, @NotNull final BlockPos pos) {
        final ItemStack shovel = player.getMainHandItem();
        if(shovel.getItem() instanceof ShovelItem && TCOTS_Util.getEnchantmentLevel(Enchantments.EFFICIENCY, shovel) >= 3){
            return super.getDestroyProgress(state, player, world, pos);
        } else {
            return 0.0f;
        }
    }

    @Override
    public void appendHoverText(@NotNull final ItemStack stack, final net.minecraft.world.item.Item.@NotNull TooltipContext context, @NotNull final List<Component> tooltip, @NotNull final TooltipFlag options) {
        super.appendHoverText(stack, context, tooltip, options);
        Spawner.appendHoverText(stack, tooltip, "SpawnData");
    }

    @Override
    public void spawnAfterBreak(@NotNull final BlockState state, @NotNull final ServerLevel world, @NotNull final BlockPos pos, @NotNull final ItemStack tool, final boolean dropExperience) {
        super.spawnAfterBreak(state, world, pos, tool, dropExperience);
        if (dropExperience) {
            final int i = 15 + world.random.nextInt(15) + world.random.nextInt(15);
            this.popExperience(world, pos, i);
        }
    }

    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
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

    public static class Item extends BlockItem implements GeoItem {

        private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

        public Item(final Block block, final Properties settings) {
            super(block, settings);
            SingletonGeoAnimatable.registerSyncedAnimatable(this);
        }

        @Override
        public void createGeoRenderer(final Consumer<GeoRenderProvider> consumer) {
            consumer.accept(new GeoRenderProvider() {

                private final MonsterNestRenderer.Item renderer = new MonsterNestRenderer.Item();


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
