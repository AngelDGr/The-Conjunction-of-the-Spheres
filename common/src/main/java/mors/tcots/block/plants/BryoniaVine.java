package mors.tcots.block.plants;

import mors.tcots.registry.TCOTS_Sounds;
import mors.tcots.registry.TCOTS_Items;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.MultifaceBlock;
import net.minecraft.world.level.block.MultifaceSpreader;
import net.minecraft.world.level.block.SweetBerryBushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

public class BryoniaVine extends MultifaceBlock implements BonemealableBlock {
    public static final MapCodec<BryoniaVine> CODEC = BryoniaVine.simpleCodec(BryoniaVine::new);
    private final MultifaceSpreader grower = new MultifaceSpreader(this);
    public static final IntegerProperty AGE = BlockStateProperties.AGE_3;

    @Override
    protected @NotNull MapCodec<? extends MultifaceBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AGE);
        super.createBlockStateDefinition(builder);
    }

    @Override
    public @NotNull ItemStack getCloneItemStack(@NotNull final LevelReader world, @NotNull final BlockPos pos, @NotNull final BlockState state) {
        return new ItemStack(TCOTS_Items.BRYONIA);
    }

    @Override
    public @NotNull MultifaceSpreader getSpreader() {
        return grower;
    }

    public BryoniaVine(final Properties settings) {
        super(settings);
        this.registerDefaultState(MultifaceBlock.getDefaultMultifaceState(this.stateDefinition).setValue(AGE,0));
    }

    @Override
    public boolean isRandomlyTicking(final BlockState state) {
        return state.getValue(AGE) < 3;
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
        final boolean bl = i == 3;
        return !bl && stack.is(Items.BONE_MEAL)
                ? ItemInteractionResult.SKIP_DEFAULT_BLOCK_INTERACTION
                : super.useItemOn(stack, state, world, pos, player, hand, hit);
    }

    @Override
    public @NotNull InteractionResult useWithoutItem(final BlockState state, @NotNull final Level world, @NotNull final BlockPos pos, @NotNull final Player player, @NotNull final BlockHitResult hit) {
        final int age = state.getValue(AGE);
        int j=0;
        if (age > 2) {
            for(final Direction direction : DIRECTIONS){
                if(state.getValue(MultifaceBlock.getFaceProperty(direction))){
                    j=j+1;
                }
            }

            SweetBerryBushBlock.popResource(world, pos, new ItemStack(TCOTS_Items.BRYONIA, j));

            world.playSound(null, pos, TCOTS_Sounds.getSoundEvent("ingredient_pops"), SoundSource.BLOCKS, 1.0f, 0.8f + world.random.nextFloat() * 0.4f);

            final BlockState blockState = state.setValue(AGE, 2);
            world.setBlock(pos, blockState, Block.UPDATE_CLIENTS);
            world.gameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Context.of(player, blockState));
            return InteractionResult.sidedSuccess(world.isClientSide);
        }
        return super.useWithoutItem(state, world, pos, player, hit);
    }

    @Override
    public boolean isValidBonemealTarget(@NotNull final LevelReader world, @NotNull final BlockPos pos, final BlockState state) {
        return state.getValue(AGE) < 3;
    }

    @Override
    public boolean isBonemealSuccess(@NotNull final Level world, @NotNull final RandomSource random, @NotNull final BlockPos pos, @NotNull final BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(final ServerLevel world, @NotNull final RandomSource random, @NotNull final BlockPos pos, final BlockState state) {
        final int i = Math.min(3, state.getValue(AGE) + 1);
        world.setBlock(pos, state.setValue(AGE, i), Block.UPDATE_CLIENTS);
    }
}
