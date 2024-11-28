package TCOTS.blocks;

import TCOTS.advancements.TCOTS_Criteria;
import TCOTS.blocks.entity.MonsterNestBlockEntity;
import TCOTS.entity.misc.WitcherBombEntity;
import TCOTS.utils.MiscUtil;
import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.Spawner;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShovelItem;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.Stats;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.text.Text;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.function.BiConsumer;

public class MonsterNestBlock extends BlockWithEntity {
    public static final MapCodec<MonsterNestBlock> CODEC = MonsterNestBlock.createCodec(MonsterNestBlock::new);
    public MapCodec<MonsterNestBlock> getCodec() {
        return CODEC;
    }

    protected MonsterNestBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH));
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new MonsterNestBlockEntity(pos, state);
    }

    @Override
    public void afterBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack tool) {
        super.afterBreak(world, player, pos, state, blockEntity, tool);
        if(player instanceof ServerPlayerEntity serverPlayer){
            TCOTS_Criteria.DESTROY_MULTIPLE_MONSTER_NEST.trigger(serverPlayer, serverPlayer.getStatHandler().getStat(Stats.MINED.getOrCreateStat(this)));
        }
    }

    @Override
    public void onExploded(BlockState state, ServerWorld world, BlockPos pos, Explosion explosion, BiConsumer<ItemStack, BlockPos> stackMerger) {
        if(explosion.getEntity()!=null && explosion.getEntity() instanceof WitcherBombEntity bomb && bomb.getOwner() instanceof PlayerEntity player){
            if(player instanceof ServerPlayerEntity serverPlayer){
                TCOTS_Criteria.DESTROY_MONSTER_NEST.trigger(serverPlayer);

                serverPlayer.incrementStat(Stats.MINED.getOrCreateStat(this));

                TCOTS_Criteria.DESTROY_MULTIPLE_MONSTER_NEST.trigger(serverPlayer, serverPlayer.getStatHandler().getStat(Stats.MINED.getOrCreateStat(this)));
            }
        }

        world.breakBlock(pos, true);
    }

    @Override
    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return MonsterNestBlock.validateTicker(type, TCOTS_Blocks.MONSTER_NEST_ENTITY, world.isClient ? MonsterNestBlockEntity::clientTick : MonsterNestBlockEntity::serverTick);
    }

    @Override
    public BlockRenderType getRenderType(BlockState state){
        return BlockRenderType.MODEL;
    }

    protected static final VoxelShape SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 15.0, 16.0);
    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }



    @Override
    public float calcBlockBreakingDelta(BlockState state, PlayerEntity player, BlockView world, BlockPos pos) {
        ItemStack shovel = player.getMainHandStack();
        if(shovel.getItem() instanceof ShovelItem && MiscUtil.getEnchantmentLevel(Enchantments.EFFICIENCY, shovel) >= 3){
            return super.calcBlockBreakingDelta(state, player, world, pos);
        } else {
            return 0.0f;
        }
    }

    @Override
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType options) {
        super.appendTooltip(stack, context, tooltip, options);
        Spawner.appendSpawnDataToTooltip(stack, tooltip, "SpawnData");
    }

    @Override
    public void onStacksDropped(BlockState state, ServerWorld world, BlockPos pos, ItemStack tool, boolean dropExperience) {
        super.onStacksDropped(state, world, pos, tool, dropExperience);
        if (dropExperience) {
            int i = 15 + world.random.nextInt(15) + world.random.nextInt(15);
            this.dropExperience(world, pos, i);
        }
    }

    public static final EnumProperty<Direction> FACING = HorizontalFacingBlock.FACING;
    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing().getOpposite());
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return state.with(FACING, rotation.rotate(state.get(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, BlockMirror mirror) {
        return state.rotate(mirror.getRotation(state.get(FACING)));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }
}
