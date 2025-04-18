package TCOTS.blocks.plants;

import TCOTS.items.TCOTS_Items_Fabric;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.MushroomBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import org.jetbrains.annotations.NotNull;

public class PuffballMushroom extends MushroomBlock {

    public PuffballMushroom(ResourceKey<ConfiguredFeature<?, ?>> featureKey, Properties settings) {
        super(featureKey, settings);
    }

    @Override
    public @NotNull ItemStack getCloneItemStack(@NotNull LevelReader world, @NotNull BlockPos pos, @NotNull BlockState state) {
        return new ItemStack(TCOTS_Items_Fabric.PUFFBALL);
    }
}
