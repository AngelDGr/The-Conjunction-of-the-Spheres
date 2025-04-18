package TCOTS.blocks;

import TCOTS.advancements.TCOTS_Criteria;
import TCOTS.blocks.entity.MonsterNestBlockEntity;
import TCOTS.entity.misc.WitcherBombEntity;
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

    protected MonsterNestBlock(Properties settings) {
        super(settings);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new MonsterNestBlockEntity(TCOTS_Blocks_Fabric.MONSTER_NEST_ENTITY, pos, state);
    }

    @Override
    public void playerDestroy(@NotNull Level world, @NotNull Player player, @NotNull BlockPos pos, @NotNull BlockState state, @Nullable BlockEntity blockEntity, @NotNull ItemStack tool) {
        super.playerDestroy(world, player, pos, state, blockEntity, tool);
        if(player instanceof ServerPlayer serverPlayer){
            TCOTS_Criteria.DESTROY_MULTIPLE_MONSTER_NEST.trigger(serverPlayer, serverPlayer.getStats().getValue(Stats.BLOCK_MINED.get(this)));
        }
    }

    @Override
    public void onExplosionHit(@NotNull BlockState state, @NotNull Level world, @NotNull BlockPos pos, Explosion explosion, @NotNull BiConsumer<ItemStack, BlockPos> stackMerger) {
        if(explosion.getDirectSourceEntity()!=null && explosion.getDirectSourceEntity() instanceof WitcherBombEntity bomb && bomb.getOwner() instanceof Player player){
            if(player instanceof ServerPlayer serverPlayer){
                TCOTS_Criteria.DESTROY_MONSTER_NEST.trigger(serverPlayer);

                serverPlayer.awardStat(Stats.BLOCK_MINED.get(this));

                TCOTS_Criteria.DESTROY_MULTIPLE_MONSTER_NEST.trigger(serverPlayer, serverPlayer.getStats().getValue(Stats.BLOCK_MINED.get(this)));
            }
        }

        world.destroyBlock(pos, true);
    }

    @Override
    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, @NotNull BlockState state, @NotNull BlockEntityType<T> type) {
        return MonsterNestBlock.createTickerHelper(type, TCOTS_Blocks_Fabric.MONSTER_NEST_ENTITY, world.isClientSide ? MonsterNestBlockEntity::clientTick : MonsterNestBlockEntity::serverTick);
    }

    @Override
    public @NotNull RenderShape getRenderShape(@NotNull BlockState state){
        return RenderShape.MODEL;
    }

    protected static final VoxelShape SHAPE = Block.box(0.0, 0.0, 0.0, 16.0, 15.0, 16.0);
    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter world, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return SHAPE;
    }



    @Override
    public float getDestroyProgress(@NotNull BlockState state, Player player, @NotNull BlockGetter world, @NotNull BlockPos pos) {
        ItemStack shovel = player.getMainHandItem();
        if(shovel.getItem() instanceof ShovelItem && MiscUtil.getEnchantmentLevel(Enchantments.EFFICIENCY, shovel) >= 3){
            return super.getDestroyProgress(state, player, world, pos);
        } else {
            return 0.0f;
        }
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, Item.@NotNull TooltipContext context, @NotNull List<Component> tooltip, @NotNull TooltipFlag options) {
        super.appendHoverText(stack, context, tooltip, options);
        Spawner.appendHoverText(stack, tooltip, "SpawnData");
    }

    @Override
    public void spawnAfterBreak(@NotNull BlockState state, @NotNull ServerLevel world, @NotNull BlockPos pos, @NotNull ItemStack tool, boolean dropExperience) {
        super.spawnAfterBreak(state, world, pos, tool, dropExperience);
        if (dropExperience) {
            int i = 15 + world.random.nextInt(15) + world.random.nextInt(15);
            this.popExperience(world, pos, i);
        }
    }

    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite());
    }

    @Override
    public @NotNull BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public @NotNull BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }
}
