package TCOTS.blocks;

import TCOTS.blocks.entity.MonsterNestBlockEntity;
import TCOTS.blocks.entity.NestSkullBlockEntity;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.MobSpawnerBlockEntity;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.context.LootContextTypes;
import net.minecraft.loot.entry.LootTableEntry;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.registry.Registries;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public class MonsterNestBlock extends BlockWithEntity {

    public static final DirectionProperty FACING = HorizontalFacingBlock.FACING;
    protected MonsterNestBlock(Settings settings) {
        super(settings);
        this.setDefaultState((BlockState)((BlockState)((BlockState)this.stateManager.getDefaultState()).with(FACING, Direction.NORTH)));
    }

    protected static final VoxelShape SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 15.0, 16.0);

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new MonsterNestBlockEntity(pos, state);
    }
    @Override
    public BlockRenderType getRenderType(BlockState state){
        return BlockRenderType.MODEL;
    }
    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return SHAPE;
    }

    @Override
    public float calcBlockBreakingDelta(BlockState state, PlayerEntity player, BlockView world, BlockPos pos) {
        if (
                player.getMainHandStack().getItem() instanceof ShovelItem &&
                EnchantmentHelper.getEfficiency(player) >= 3
        ) {

            return super.calcBlockBreakingDelta(state, player, world, pos);
        }
        else{
            return 0.0f;
        }
    }

    @Override
    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return MonsterNestBlock.checkType(type, TCOTS_Blocks.MONSTER_NEST_ENTITY, world.isClient ? MonsterNestBlockEntity::clientTick : MonsterNestBlockEntity::serverTick);
    }


    @Override
    public void appendTooltip(ItemStack stack, @Nullable BlockView world, List<Text> tooltip, TooltipContext options) {
        super.appendTooltip(stack, world, tooltip, options);
        Optional<Text> optional = this.getEntityNameForTooltip(stack);
        if (optional.isPresent()) {
            tooltip.add(optional.get());
        } else {
            tooltip.add(ScreenTexts.EMPTY);
            tooltip.add(Text.translatable("block.minecraft.spawner.desc1").formatted(Formatting.GRAY));
            tooltip.add(ScreenTexts.space().append(Text.translatable("block.minecraft.spawner.desc2").formatted(Formatting.BLUE)));
        }
    }

    private Optional<Text> getEntityNameForTooltip(ItemStack stack) {
        String string;
        Identifier identifier;
        NbtCompound nbtCompound = BlockItem.getBlockEntityNbt(stack);
        if (nbtCompound != null && nbtCompound.contains("SpawnData", NbtElement.COMPOUND_TYPE) && (identifier = Identifier.tryParse(string = nbtCompound.getCompound("SpawnData").getCompound("entity").getString("id"))) != null) {
            return Registries.ENTITY_TYPE.getOrEmpty(identifier).map(entityType -> Text.translatable(entityType.getTranslationKey()).formatted(Formatting.GRAY));
        }
        return Optional.empty();
    }

    @Override
    public void onStacksDropped(BlockState state, ServerWorld world, BlockPos pos, ItemStack tool, boolean dropExperience) {
        super.onStacksDropped(state, world, pos, tool, dropExperience);
        if (dropExperience) {
            int i = 15 + world.random.nextInt(15) + world.random.nextInt(15);
            this.dropExperience(world, pos, i);
        }

    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return (BlockState)this.getDefaultState().with(FACING, ctx.getHorizontalPlayerFacing().getOpposite());
    }

    @Override
    public BlockState rotate(BlockState state, BlockRotation rotation) {
        return (BlockState)state.with(FACING, rotation.rotate(state.get(FACING)));
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
