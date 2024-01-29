package TCOTS.blocks.skull;

import net.minecraft.block.BlockState;
import net.minecraft.block.SkullBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.SkullBlockEntity;
import net.minecraft.util.math.BlockPos;

public class NestSkullBlock extends SkullBlock {
    public NestSkullBlock(SkullType skullType, Settings settings) {
        super(skullType, settings);
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new SkullBlockEntity(pos, state);
    }
}
