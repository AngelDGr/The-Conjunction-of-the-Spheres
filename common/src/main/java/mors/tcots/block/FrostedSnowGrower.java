package mors.tcots.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.MultifaceBlock;
import net.minecraft.world.level.block.MultifaceSpreader;
import net.minecraft.world.level.block.state.BlockState;

public class FrostedSnowGrower extends MultifaceSpreader {
    public FrostedSnowGrower(final MultifaceBlock lichen) {
        super(new FrostedSnowGrowChecker(lichen));
    }

    public static class FrostedSnowGrowChecker extends DefaultSpreaderConfig {
        public FrostedSnowGrowChecker(final MultifaceBlock lichen) {
            super(lichen);
        }
        @Override
        protected boolean stateCanBeReplaced(final BlockGetter world, final BlockPos pos, final BlockPos growPos, final Direction direction, final BlockState state) {
            return state.isAir() || state.is(this.block);
        }
    }
}
