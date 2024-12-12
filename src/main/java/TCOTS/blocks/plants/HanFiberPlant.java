package TCOTS.blocks.plants;

import TCOTS.items.TCOTS_Items;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;

public class HanFiberPlant extends VerbenaFlower {

    private static final VoxelShape[] AGE_TO_SHAPE = new VoxelShape[]{
            Block.createCuboidShape(5.0, 0.0, 5.0, 12, 4.0, 12),
            Block.createCuboidShape(5.0, 0.0, 5.0, 12, 10.0, 12),
            Block.createCuboidShape(5.0, 0.0, 5.0, 12, 14.0, 12),
            Block.createCuboidShape(5.0, 0.0, 5.0, 12, 16.0, 12)};

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        Vec3d vec3d = state.getModelOffset(world, pos);
        return AGE_TO_SHAPE[this.getAge(state)].offset(vec3d.x, vec3d.y, vec3d.z);
    }

    public HanFiberPlant(Settings settings) {
        super(settings);
    }

    @Override
    public ItemStack getPickStack(BlockView world, BlockPos pos, BlockState state) {
        return new ItemStack(TCOTS_Items.HAN_FIBER);
    }

}
