package TCOTS.world;

import TCOTS.TCOTS_Main;
import TCOTS.world.gen.BryoniaPatchFeature;
import TCOTS.world.gen.BryoniaPatchFeatureConfig;
import TCOTS.world.gen.HugePuffballMushroomFeature;
import TCOTS.world.gen.HugeSewantMushroomsFeature;
import net.minecraft.registry.*;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.*;

public class TCOTS_Features {

    public static Feature<HugeMushroomFeatureConfig> HUGE_PUFFBALL_MUSHROOM = TCOTS_Features.registerFeature("huge_puffball_mushroom", new HugePuffballMushroomFeature(HugeMushroomFeatureConfig.CODEC));

    public static Feature<HugeMushroomFeatureConfig> HUGE_SEWANT_MUSHROOMS = TCOTS_Features.registerFeature("huge_sewant_mushrooms", new HugeSewantMushroomsFeature(HugeMushroomFeatureConfig.CODEC));

    public static Feature<BryoniaPatchFeatureConfig> BRYONIA_PATCH_FEATURE = TCOTS_Features.registerFeature("bryonia_patch", new BryoniaPatchFeature(BryoniaPatchFeatureConfig.CODEC));


    private static <C extends FeatureConfig, F extends Feature<C>> F registerFeature(String name, F feature) {
        return Registry.register(Registries.FEATURE, Identifier.of(TCOTS_Main.MOD_ID, name) , feature);
    }

    public static void registerFeatures(){
    }


    //Flora
    public static final TagKey<Biome> CELANDINE_SPAWN = createBiomeTag("has_flora/celandine_spawn");
    public static final TagKey<Biome> VERBENA_SPAWN = createBiomeTag("has_flora/verbena_spawn");
    public static final TagKey<Biome> HAN_FIBER_SPAWN = createBiomeTag("has_flora/han_fiber_spawn");
    public static final TagKey<Biome> CROWS_EYE_SPAWN = createBiomeTag("has_flora/crows_eye_spawn");
    public static final TagKey<Biome> ARENARIA_SPAWN = createBiomeTag("has_flora/arenaria_spawn");

    public static final TagKey<Biome> PUFFBALL_SPAWN_NORMAL = createBiomeTag("has_flora/puffball_spawn_normal");
    public static final TagKey<Biome> PUFFBALL_SPAWN_TAIGA = createBiomeTag("has_flora/puffball_spawn_taiga");
    public static final TagKey<Biome> PUFFBALL_SPAWN_SWAMP = createBiomeTag("has_flora/puffball_spawn_swamp");

    public static final TagKey<Biome> SEWANT_SPAWN_NORMAL = createBiomeTag("has_flora/sewant_spawn_normal");
    public static final TagKey<Biome> SEWANT_SPAWN_TAIGA = createBiomeTag("has_flora/sewant_spawn_taiga");
    public static final TagKey<Biome> SEWANT_SPAWN_DARK = createBiomeTag("has_flora/sewant_spawn_dark");

    public static final TagKey<Biome> MUSHROOM_SPAWN_OLD_GROWTH = createBiomeTag("has_flora/mushroom_spawn_old_growth");

    //Monsters
    public static final TagKey<Biome> DROWNER_SWAMP = createBiomeTag("has_monster/drowner_swamp");
    public static final TagKey<Biome> DROWNER_BEACH = createBiomeTag("has_monster/drowner_beach");
    public static final TagKey<Biome> DROWNER_WATER = createBiomeTag("has_monster/drowner_water");

    public static final TagKey<Biome> ROTFIEND = createBiomeTag("has_monster/rotfiend");

    public static final TagKey<Biome> FOGLET_SWAMP = createBiomeTag("has_monster/foglet_swamp");
    public static final TagKey<Biome> FOGLET_DARK = createBiomeTag("has_monster/foglet_dark");
    public static final TagKey<Biome> FOGLET_HILLS_FORESTS = createBiomeTag("has_monster/foglet_hills_forests");

    public static final TagKey<Biome> WATER_HAG_SWAMP = createBiomeTag("has_monster/water_hag_swamp");
    public static final TagKey<Biome> WATER_HAG_RIVER = createBiomeTag("has_monster/water_hag_river");

    public static final TagKey<Biome> GRAVE_HAG = createBiomeTag("has_monster/grave_hag");

    public static final TagKey<Biome> GHOUL = createBiomeTag("has_monster/ghoul");

    public static final TagKey<Biome> SCURVER = createBiomeTag("has_monster/scurver");

    public static final TagKey<Biome> DEVOURER = createBiomeTag("has_monster/devourer");

    public static final TagKey<Biome> GRAVEIR = createBiomeTag("has_monster/graveir");


    public static final TagKey<Biome> NEKKER = createBiomeTag("has_monster/nekker");

    public static final TagKey<Biome> CYCLOPS = createBiomeTag("has_monster/cyclops");

    public static final TagKey<Biome> ROCK_TROLL = createBiomeTag("has_monster/rock_troll");

    public static final TagKey<Biome> ICE_TROLL = createBiomeTag("has_monster/ice_troll");

    public static final TagKey<Biome> FOREST_TROLL = createBiomeTag("has_monster/forest_troll");

    public static TagKey<Biome> createBiomeTag(String name){
        return TagKey.of(RegistryKeys.BIOME, Identifier.of(TCOTS_Main.MOD_ID,name));
    }
}
