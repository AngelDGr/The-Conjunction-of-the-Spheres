package TCOTS.blocks.plants;

import TCOTS.registry.TCOTS_Items;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class CelandinePlant extends BushBlock implements BonemealableBlock {

    public static final MapCodec<CelandinePlant> CODEC = CelandinePlant.simpleCodec(CelandinePlant::new);

    private static final VoxelShape[] AGE_TO_SHAPE = new VoxelShape[]{Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0), Block.box(0.0, 0.0, 0.0, 16.0, 4.0, 16.0), Block.box(0.0, 0.0, 0.0, 16.0, 6.0, 16.0), Block.box(0.0, 0.0, 0.0, 16.0, 8.0, 16.0)};

    public static final IntegerProperty AGE = BlockStateProperties.AGE_3;

    protected IntegerProperty getAgeProperty() {
        return AGE;
    }

    public int getAge(final BlockState state) {
        return state.getValue(this.getAgeProperty());
    }

    @Override
    protected @NotNull MapCodec<? extends BushBlock> codec() {
        return CODEC;
    }

    public CelandinePlant(final Properties settings) {
        super(settings);
        this.registerDefaultState(this.stateDefinition.any().setValue(AGE, 0));
    }

    @Override
    public @NotNull ItemStack getCloneItemStack(@NotNull final LevelReader world, @NotNull final BlockPos pos, @NotNull final BlockState state) {
        return new ItemStack(TCOTS_Items.CELANDINE);
    }

    @Override
    public @NotNull VoxelShape getShape(@NotNull final BlockState state, @NotNull final BlockGetter world, @NotNull final BlockPos pos, @NotNull final CollisionContext context) {
        return AGE_TO_SHAPE[this.getAge(state)];
    }

    @Override
    protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AGE);
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
