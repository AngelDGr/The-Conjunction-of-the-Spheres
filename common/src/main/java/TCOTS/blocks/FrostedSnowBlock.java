package TCOTS.blocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.MultifaceBlock;
import net.minecraft.world.level.block.MultifaceSpreader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.jetbrains.annotations.NotNull;

public class FrostedSnowBlock extends MultifaceBlock {
    public static final MapCodec<FrostedSnowBlock> CODEC = FrostedSnowBlock.simpleCodec(FrostedSnowBlock::new);

    public static final IntegerProperty AGE = BlockStateProperties.AGE_3;
    private final FrostedSnowGrower grower = new FrostedSnowGrower(this);

    public FrostedSnowBlock(final Properties settings) {
        super(settings);
        this.registerDefaultState(MultifaceBlock.getDefaultMultifaceState(this.stateDefinition).setValue(AGE,0));
    }

    @Override
    protected @NotNull MapCodec<? extends MultifaceBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(final StateDefinition.@NotNull Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(AGE);
    }

    @Override
    public @NotNull MultifaceSpreader getSpreader() {
        return grower;
    }

    @Override
    public @NotNull ItemStack getCloneItemStack(@NotNull final LevelReader world, @NotNull final BlockPos pos, @NotNull final BlockState state) {
        return ItemStack.EMPTY;
    }

    @Override
    public void randomTick(@NotNull final BlockState state, @NotNull final ServerLevel world, @NotNull final BlockPos pos, @NotNull final RandomSource random) {
//        super.randomTick(state, world, pos, random);
        this.tick(state, world, pos, random);
    }

    @Override
    public void tick(@NotNull final BlockState state, @NotNull final ServerLevel world, @NotNull final BlockPos pos, final RandomSource random) {
        if ((random.nextInt(3) == 0 || this.canMelt(world, pos, 4))
//                && world.getLightLevel(pos) > - state.get(AGE) - state.getOpacity(world, pos)
                && this.increaseAge(state, world, pos)) {
            final BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
            for (final Direction direction : Direction.values()) {
                mutable.setWithOffset(pos, direction);
                final BlockState blockState = world.getBlockState(mutable);
                if (!blockState.is(this) || this.increaseAge(blockState, world, mutable)) continue;
                world.scheduleTick(mutable, this, Mth.nextInt(random, 20, 40));
            }
            return;
        }
        world.scheduleTick(pos, this, Mth.nextInt(random, 20, 40));
    }

    private boolean canMelt(final BlockGetter world, final BlockPos pos, final int maxNeighbors) {
        int i = 0;
        final BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
        for (final Direction direction : Direction.values()) {
            mutable.setWithOffset(pos, direction);
            if (!world.getBlockState(mutable).is(this) || ++i < maxNeighbors) continue;
            return false;
        }
        return true;
    }

    private boolean increaseAge(final BlockState state, final Level world, final BlockPos pos) {
        final int i = state.getValue(AGE);
        if (i < 3) {
            world.setBlock(pos, state.setValue(AGE, i + 1), Block.UPDATE_CLIENTS);
            return false;
        }
        this.melt(state, world, pos);
        return true;
    }

    @Override
    public void neighborChanged(@NotNull final BlockState state, @NotNull final Level world, @NotNull final BlockPos pos, final Block sourceBlock, @NotNull final BlockPos sourcePos, final boolean notify) {
        if (sourceBlock.defaultBlockState().is(this) && this.canMelt(world, pos, 2)) {
            this.melt(state, world, pos);
        }
        super.neighborChanged(state, world, pos, sourceBlock, sourcePos, notify);
    }

    @SuppressWarnings("unused")
    protected void melt(final BlockState state, final Level world, final BlockPos pos) {
        if (world.dimensionType().ultraWarm()) {
            world.removeBlock(pos, false);
            return;
        }
        world.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
        world.neighborChanged(pos, Blocks.AIR.defaultBlockState().getBlock(), pos);
    }

}
