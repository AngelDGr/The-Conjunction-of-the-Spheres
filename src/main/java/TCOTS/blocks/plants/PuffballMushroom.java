package TCOTS.blocks.plants;

import TCOTS.items.TCOTS_Items;
import net.minecraft.block.BlockState;
import net.minecraft.block.MushroomPlantBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.gen.feature.ConfiguredFeature;

public class PuffballMushroom extends MushroomPlantBlock {

    public PuffballMushroom(RegistryKey<ConfiguredFeature<?, ?>> featureKey, Settings settings) {
        super(settings, featureKey);
    }

    @Override
    public ItemStack getPickStack(BlockView world, BlockPos pos, BlockState state) {
        return new ItemStack(TCOTS_Items.PUFFBALL);
    }
}
