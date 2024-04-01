package TCOTS.world;

import TCOTS.TCOTS_Main;
import TCOTS.blocks.TCOTS_Blocks;
import net.minecraft.block.Blocks;
import net.minecraft.block.MushroomBlock;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;

public class TCOTS_ConfiguredFeatures {

    public static final RegistryKey<ConfiguredFeature<?, ?>> HUGE_PUFFBALL_MUSHROOM = registerKey("huge_puffball_mushroom");

    public static void boostrap(Registerable<ConfiguredFeature<?, ?>> context) {


        TCOTS_ConfiguredFeatures.register(context, HUGE_PUFFBALL_MUSHROOM, TCOTS_Features.HUGE_PUFFBALL_MUSHROOM, new HugeMushroomFeatureConfig(
                BlockStateProvider.of(TCOTS_Blocks.PUFFBALL_MUSHROOM_BLOCK.getDefaultState().with(MushroomBlock.DOWN, false)),

                BlockStateProvider.of((Blocks.MUSHROOM_STEM.getDefaultState().with(MushroomBlock.UP, false)).with(MushroomBlock.DOWN, false)),

                2
                ));
    }

    public static RegistryKey<ConfiguredFeature<?, ?>> registerKey(String name) {
        return RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, new Identifier(TCOTS_Main.MOD_ID, name));
    }

    private static <FC extends FeatureConfig, F extends Feature<FC>> void register(Registerable<ConfiguredFeature<?, ?>> context, RegistryKey<ConfiguredFeature<?, ?>> key, F feature, FC configuration) {
        context.register(key, new ConfiguredFeature<>(feature, configuration));
    }
}
