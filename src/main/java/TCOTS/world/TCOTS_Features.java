package TCOTS.world;

import TCOTS.TCOTS_Main;
import TCOTS.world.gen.BryoniaPatchFeature;
import TCOTS.world.gen.BryoniaPatchFeatureConfig;
import TCOTS.world.gen.HugePuffballMushroomFeature;
import TCOTS.world.gen.HugeSewantMushroomsFeature;
import net.minecraft.registry.*;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.feature.*;

public class TCOTS_Features {

    public static Feature<HugeMushroomFeatureConfig> HUGE_PUFFBALL_MUSHROOM = TCOTS_Features.registerFeature("huge_puffball_mushroom", new HugePuffballMushroomFeature(HugeMushroomFeatureConfig.CODEC));

    public static Feature<HugeMushroomFeatureConfig> HUGE_SEWANT_MUSHROOMS = TCOTS_Features.registerFeature("huge_sewant_mushrooms", new HugeSewantMushroomsFeature(HugeMushroomFeatureConfig.CODEC));

    public static Feature<BryoniaPatchFeatureConfig> BRYONIA_PATCH_FEATURE = TCOTS_Features.registerFeature("bryonia_patch", new BryoniaPatchFeature(BryoniaPatchFeatureConfig.CODEC));


    private static <C extends FeatureConfig, F extends Feature<C>> F registerFeature(String name, F feature) {
        return Registry.register(Registries.FEATURE, new Identifier(TCOTS_Main.MOD_ID, name) , feature);
    }

    public static void registerFeatures(){
    }
}
