package TCOTS.blocks.plants;

import TCOTS.registry.TCOTS_Items;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class HanFiberPlant extends VerbenaFlower {

    public static final MapCodec<VerbenaFlower> CODEC = VerbenaFlower.simpleCodec(VerbenaFlower::new);

    @Override
    protected @NotNull MapCodec<? extends BushBlock> codec() {
        return CODEC;
    }

    private static final VoxelShape[] AGE_TO_SHAPE = new VoxelShape[]{
            Block.box(5.0, 0.0, 5.0, 12, 4.0, 12),
            Block.box(5.0, 0.0, 5.0, 12, 10.0, 12),
            Block.box(5.0, 0.0, 5.0, 12, 14.0, 12),
            Block.box(5.0, 0.0, 5.0, 12, 16.0, 12)};

    @Override
    public @NotNull VoxelShape getShape(final BlockState state, @NotNull final BlockGetter world, @NotNull final BlockPos pos, @NotNull final CollisionContext context) {
        final Vec3 vec3d = state.getOffset(world, pos);
        return AGE_TO_SHAPE[this.getAge(state)].move(vec3d.x, vec3d.y, vec3d.z);
    }

    public HanFiberPlant(final Properties settings) {
        super(settings);
    }

    @Override
    public @NotNull ItemStack getCloneItemStack(@NotNull final LevelReader world, @NotNull final BlockPos pos, @NotNull final BlockState state) {
        return new ItemStack(TCOTS_Items.HAN_FIBER.get());
    }

}
