package TCOTS.blocks.plants;

import TCOTS.registry.TCOTS_Sounds;
import TCOTS.registry.TCOTS_Items;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.SweetBerryBushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class ArenariaBush extends BushBlock implements BonemealableBlock {
    public static final MapCodec<ArenariaBush> CODEC = ArenariaBush.simpleCodec(ArenariaBush::new);
    public static final IntegerProperty AGE = BlockStateProperties.AGE_2;

    private static final VoxelShape SMALL_SHAPE = Block.box(3.0, 0.0, 3.0, 13.0, 8.0, 13.0);
    private static final VoxelShape LARGE_SHAPE = Block.box(1.0, 0.0, 1.0, 15.0, 16.0, 15.0);

    @Override
    protected @NotNull MapCodec<? extends BushBlock> codec() {
        return CODEC;
    }

    public ArenariaBush(final Properties settings) {
        super(settings);
        this.registerDefaultState(this.stateDefinition.any().setValue(AGE, 0));
    }

    @Override
    public @NotNull ItemStack getCloneItemStack(@NotNull final LevelReader world, @NotNull final BlockPos pos, @NotNull final BlockState state) {
        return new ItemStack(TCOTS_Items.ARENARIA);
    }

    @Override
    public @NotNull VoxelShape getShape(final BlockState state, @NotNull final BlockGetter world, @NotNull final BlockPos pos, @NotNull final CollisionContext context) {
        if (state.getValue(AGE) == 0) {
            return SMALL_SHAPE;
        }
        if (state.getValue(AGE) < 3) {
            return LARGE_SHAPE;
        }
        return super.getShape(state, world, pos, context);
    }

    @Override
    protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AGE);
    }

    @Override
    public boolean isRandomlyTicking(final BlockState state) {
        return state.getValue(AGE) < 2;
    }

    @Override
    public void randomTick(final BlockState state, @NotNull final ServerLevel world, @NotNull final BlockPos pos, @NotNull final RandomSource random) {
        final int i = state.getValue(AGE);
        if (i < 3 && random.nextInt(5) == 0 && world.getRawBrightness(pos.above(), 0) >= 9) {
            final BlockState blockState = state.setValue(AGE, i + 1);
            world.setBlock(pos, blockState, Block.UPDATE_CLIENTS);
            world.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(blockState));
        }
    }

    @Override
    protected @NotNull ItemInteractionResult useItemOn(@NotNull final ItemStack stack, final BlockState state, @NotNull final Level world, @NotNull final BlockPos pos, @NotNull final Player player, @NotNull final InteractionHand hand, @NotNull final BlockHitResult hit) {
        final int i = state.getValue(AGE);
        final boolean bl = i == 2;
        return !bl && stack.is(Items.BONE_MEAL)
                ? ItemInteractionResult.SKIP_DEFAULT_BLOCK_INTERACTION
                : super.useItemOn(stack, state, world, pos, player, hand, hit);
    }

    @Override
    public @NotNull InteractionResult useWithoutItem(final BlockState state, @NotNull final Level world, @NotNull final BlockPos pos, @NotNull final Player player, @NotNull final BlockHitResult hit) {
        final int i = state.getValue(AGE);
        if (i > 1) {
            final int j = 1 + world.random.nextInt(3);
            SweetBerryBushBlock.popResource(world, pos, new ItemStack(TCOTS_Items.ARENARIA, j));

            world.playSound(null, pos, TCOTS_Sounds.getSoundEvent("ingredient_pops"), SoundSource.BLOCKS, 1.0f, 0.8f + world.random.nextFloat() * 0.4f);

            final BlockState blockState = state.setValue(AGE, 1);
            world.setBlock(pos, blockState, Block.UPDATE_CLIENTS);
            world.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(player, blockState));
            return InteractionResult.sidedSuccess(world.isClientSide);
        }
        return super.useWithoutItem(state, world, pos, player, hit);
    }

    @Override
    public boolean isValidBonemealTarget(@NotNull final LevelReader world, @NotNull final BlockPos pos, final BlockState state) {
        return state.getValue(AGE) < 2;
    }

    @Override
    public boolean isBonemealSuccess(@NotNull final Level world, @NotNull final RandomSource random, @NotNull final BlockPos pos, @NotNull final BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(final ServerLevel world, @NotNull final RandomSource random, @NotNull final BlockPos pos, final BlockState state) {
        final int i = Math.min(2, state.getValue(AGE) + 1);
        world.setBlock(pos, state.setValue(AGE, i), Block.UPDATE_CLIENTS);
    }
}
