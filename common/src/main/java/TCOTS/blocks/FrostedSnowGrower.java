package TCOTS.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.MultifaceBlock;
import net.minecraft.world.level.block.MultifaceSpreader;
import net.minecraft.world.level.block.state.BlockState;

public class FrostedSnowGrower extends MultifaceSpreader {
    public FrostedSnowGrower(MultifaceBlock lichen) {
        super(new FrostedSnowGrowChecker(lichen));
    }

    public static class FrostedSnowGrowChecker extends DefaultSpreaderConfig {
        public FrostedSnowGrowChecker(MultifaceBlock lichen) {
            super(lichen);
        }
        @Override
        protected boolean stateCanBeReplaced(BlockGetter world, BlockPos pos, BlockPos growPos, Direction direction, BlockState state) {
            return state.isAir() || state.is(this.block);
        }
    }
}
