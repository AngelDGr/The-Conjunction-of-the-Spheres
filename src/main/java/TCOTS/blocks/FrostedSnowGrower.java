package TCOTS.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.block.LichenGrower;
import net.minecraft.block.MultifaceGrowthBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;

public class FrostedSnowGrower extends LichenGrower {
    public FrostedSnowGrower(MultifaceGrowthBlock lichen) {
        super(new FrostedSnowGrowChecker(lichen));
    }

    public static class FrostedSnowGrowChecker extends LichenGrowChecker {
        public FrostedSnowGrowChecker(MultifaceGrowthBlock lichen) {
            super(lichen);
        }
        @Override
        protected boolean canGrow(BlockView world, BlockPos pos, BlockPos growPos, Direction direction, BlockState state) {
            return state.isAir() || state.isOf(this.lichen);
        }
    }
}
