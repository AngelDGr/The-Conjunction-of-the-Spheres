package TCOTS.blocks;

import TCOTS.registry.TCOTS_Blocks;
import TCOTS.blocks.entity.MonsterNestBlockEntity;
import TCOTS.entity.misc.WitcherBombEntity;
import TCOTS.registry.TCOTS_Criteria;
import TCOTS.utils.MiscUtil;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.TooltipFlag;
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

import java.util.List;
import java.util.function.BiConsumer;

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
        if(shovel.getItem() instanceof ShovelItem && MiscUtil.getEnchantmentLevel(Enchantments.EFFICIENCY, shovel) >= 3){
            return super.getDestroyProgress(state, player, world, pos);
        } else {
            return 0.0f;
        }
    }

    @Override
    public void appendHoverText(@NotNull final ItemStack stack, final Item.@NotNull TooltipContext context, @NotNull final List<Component> tooltip, @NotNull final TooltipFlag options) {
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
}
