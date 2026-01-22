package mors.tcots.block.plants;

import mors.tcots.registry.TCOTS_Items;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.MushroomBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import org.jetbrains.annotations.NotNull;

public class PuffballMushroomPlant extends MushroomBlock {

    public PuffballMushroomPlant(final ResourceKey<ConfiguredFeature<?, ?>> featureKey, final Properties settings) {
        super(featureKey, settings);
    }

    @Override
    public @NotNull ItemStack getCloneItemStack(@NotNull final LevelReader world, @NotNull final BlockPos pos, @NotNull final BlockState state) {
        return new ItemStack(TCOTS_Items.PUFFBALL);
    }
}
