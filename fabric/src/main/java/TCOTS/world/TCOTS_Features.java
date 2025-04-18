package TCOTS.world;

import TCOTS.TCOTS_Main;
import TCOTS.world.gen.BryoniaPatchFeature;
import TCOTS.world.gen.BryoniaPatchFeatureConfig;
import TCOTS.world.gen.HugePuffballMushroomFeature;
import TCOTS.world.gen.HugeSewantMushroomsFeature;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.HugeMushroomFeatureConfiguration;

public class TCOTS_Features {

    public static Feature<HugeMushroomFeatureConfiguration> HUGE_PUFFBALL_MUSHROOM = TCOTS_Features.registerFeature("huge_puffball_mushroom", new HugePuffballMushroomFeature(HugeMushroomFeatureConfiguration.CODEC));

    public static Feature<HugeMushroomFeatureConfiguration> HUGE_SEWANT_MUSHROOMS = TCOTS_Features.registerFeature("huge_sewant_mushrooms", new HugeSewantMushroomsFeature(HugeMushroomFeatureConfiguration.CODEC));

    public static Feature<BryoniaPatchFeatureConfig> BRYONIA_PATCH_FEATURE = TCOTS_Features.registerFeature("bryonia_patch", new BryoniaPatchFeature(BryoniaPatchFeatureConfig.CODEC));


    private static <C extends FeatureConfiguration, F extends Feature<C>> F registerFeature(String name, F feature) {
        return Registry.register(BuiltInRegistries.FEATURE, ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, name) , feature);
    }

    public static void registerFeatures(){
    }
}
