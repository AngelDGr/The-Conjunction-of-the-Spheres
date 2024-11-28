package TCOTS.blocks.plants;

import TCOTS.items.TCOTS_Items;
import TCOTS.sounds.TCOTS_Sounds;
import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.event.GameEvent;


public class BryoniaVine extends MultifaceGrowthBlock implements Fertilizable {
    public static final MapCodec<BryoniaVine> CODEC = BryoniaVine.createCodec(BryoniaVine::new);
    private final LichenGrower grower = new LichenGrower(this);
    public static final IntProperty AGE = Properties.AGE_3;

    @Override
    protected MapCodec<? extends MultifaceGrowthBlock> getCodec() {
        return CODEC;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(AGE);
        super.appendProperties(builder);
    }

    @Override
    public ItemStack getPickStack(WorldView world, BlockPos pos, BlockState state) {
        return new ItemStack(TCOTS_Items.BRYONIA);
    }

    @Override
    public LichenGrower getGrower() {
        return grower;
    }

    public BryoniaVine(Settings settings) {
        super(settings);
        this.setDefaultState(MultifaceGrowthBlock.withAllDirections(this.stateManager).with(AGE,0));
    }

    @Override
    public boolean hasRandomTicks(BlockState state) {
        return state.get(AGE) < 3;
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        int i = state.get(AGE);
        if (i < 3 && random.nextInt(5) == 0 && world.getBaseLightLevel(pos.up(), 0) >= 9) {
            BlockState blockState = state.with(AGE, i + 1);
            world.setBlockState(pos, blockState, Block.NOTIFY_LISTENERS);
            world.emitGameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Emitter.of(blockState));
        }
    }

    @Override
    protected ActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        int i = state.get(AGE);
        boolean bl = i == 3;
        return !bl && stack.isOf(Items.BONE_MEAL)
                ? ActionResult.PASS
                : super.onUseWithItem(stack, state, world, pos, player, hand, hit);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        int age = state.get(AGE);
        int j=0;
        if (age > 2) {
            for(Direction direction : DIRECTIONS){
                if(state.get(MultifaceGrowthBlock.getProperty(direction))){
                    j=j+1;
                }
            }

            SweetBerryBushBlock.dropStack(world, pos, new ItemStack(TCOTS_Items.BRYONIA, j));

            world.playSound(null, pos, TCOTS_Sounds.INGREDIENT_POPS, SoundCategory.BLOCKS, 1.0f, 0.8f + world.random.nextFloat() * 0.4f);

            BlockState blockState = state.with(AGE, 2);
            world.setBlockState(pos, blockState, Block.NOTIFY_LISTENERS);
            world.emitGameEvent(GameEvent.BLOCK_CHANGE, pos, GameEvent.Emitter.of(player, blockState));
            return ActionResult.SUCCESS;
        }
        return super.onUse(state, world, pos, player, hit);
    }

    @Override
    public boolean isFertilizable(WorldView world, BlockPos pos, BlockState state) {
        return state.get(AGE) < 3;
    }

    @Override
    public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
        return true;
    }

    @Override
    public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
        int i = Math.min(3, state.get(AGE) + 1);
        world.setBlockState(pos, state.with(AGE, i), Block.NOTIFY_LISTENERS);
    }
}
