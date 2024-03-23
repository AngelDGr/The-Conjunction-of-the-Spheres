package TCOTS.entity;

import TCOTS.TCOTS_Main;
import TCOTS.entity.misc.DrownerPuddleEntity;
import TCOTS.entity.misc.FoglingEntity;
import TCOTS.entity.misc.WaterHag_MudBallEntity;
import TCOTS.entity.necrophages.*;
import TCOTS.entity.ogroids.NekkerEntity;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.BiomeKeys;

public class TCOTS_Entities {

//    0.2.0- Necrophages & Ogroids
// Necrophages
    //xTODO: Drowner
    //xTODO: Rotfiend
    //xTODO: Grave Hag
    //xTODO: Water Hag
    //xTODO: Foglet
    //TODO: Ghoul
    //TODO: Wights
    //TODO: Scurvers
    //TODO: Devourer?
    //TODO: Graveir?

//  Ogroids
    //xTODO: Nekkers
    //TODO: Cyclopses
    //TODO: Rock troll
    //TODO: Ice troll
    //TODO: Ice Giant (Boss)

    public static final EntityGroup NECROPHAGES = new EntityGroup();
    public static final EntityGroup OGROIDS = new EntityGroup();
    public static final EntityGroup SPECTERS = new EntityGroup();
    public static final EntityGroup VAMPIRES = new EntityGroup();
    public static final EntityGroup INSECTOIDS = new EntityGroup();
    public static final EntityGroup BEASTS = new EntityGroup();
    public static final EntityGroup ELEMENTA = new EntityGroup();
    public static final EntityGroup CURSED_ONES = new EntityGroup();
    public static final EntityGroup HYBRIDS = new EntityGroup();
    public static final EntityGroup DRACONIDS = new EntityGroup();
    public static final EntityGroup RELICTS = new EntityGroup();

    //Necrophages
    public static final EntityType<DrownerEntity> DROWNER = Registry.register(
            Registries.ENTITY_TYPE, new Identifier(TCOTS_Main.MOD_ID, "drowner"),
            FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, DrownerEntity::new)
                    //Hitbox
                    .dimensions(EntityDimensions.changing(0.78f, 1.9f)).build());
    public static final EntityType<DrownerPuddleEntity> DROWNER_PUDDLE = Registry.register(
            Registries.ENTITY_TYPE, new Identifier(TCOTS_Main.MOD_ID, "drowner_puddle"),
            FabricEntityTypeBuilder.<DrownerPuddleEntity>create(SpawnGroup.MISC, DrownerPuddleEntity::new
                    )
                    // Hitbox
                    .dimensions(EntityDimensions.changing(0.78f, 0.1f)).build());



    public static final EntityType<RotfiendEntity> ROTFIEND = Registry.register(
            Registries.ENTITY_TYPE, new Identifier(TCOTS_Main.MOD_ID, "rotfiend"),
            FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, RotfiendEntity::new)
                    //Hitbox
                    .dimensions(EntityDimensions.changing(0.78f, 1.9f)).build());



    public static final EntityType<GraveHagEntity> GRAVE_HAG = Registry.register(
            Registries.ENTITY_TYPE, new Identifier(TCOTS_Main.MOD_ID, "grave_hag"),
            FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, GraveHagEntity::new)
                    //Hitbox
                    .dimensions(EntityDimensions.changing(0.78f, 1.9f)).build());

    public static final EntityType<WaterHagEntity> WATER_HAG = Registry.register(
            Registries.ENTITY_TYPE, new Identifier(TCOTS_Main.MOD_ID, "water_hag"),
            FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, WaterHagEntity::new)
                    //Hitbox
                    .dimensions(EntityDimensions.changing(0.78f, 1.9f)).build());

    public static final EntityType<WaterHag_MudBallEntity> WATER_HAG_MUD_BALL = Registry.register(
            Registries.ENTITY_TYPE, new Identifier(TCOTS_Main.MOD_ID, "water_hag_mud_ball"),
            FabricEntityTypeBuilder.<WaterHag_MudBallEntity>create(SpawnGroup.MISC, WaterHag_MudBallEntity::new
                    )
                    // Hitbox
                    .dimensions(EntityDimensions.changing(0.25f, 0.25f)).build());


    public static final EntityType<FogletEntity> FOGLET = Registry.register(
            Registries.ENTITY_TYPE, new Identifier(TCOTS_Main.MOD_ID, "foglet"),
            FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, FogletEntity::new)
                    //Hitbox
                    .dimensions(EntityDimensions.changing(0.78f, 1.9f)).build());
    public static final EntityType<FoglingEntity> FOGLING = Registry.register(
            Registries.ENTITY_TYPE, new Identifier(TCOTS_Main.MOD_ID, "fogling"),
            FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, FoglingEntity::new)
                    //Hitbox
                    .dimensions(EntityDimensions.changing(0.78f, 1.9f)).build());


    //Ogroids
    public static final EntityType<NekkerEntity> NEKKER = Registry.register(
            Registries.ENTITY_TYPE, new Identifier(TCOTS_Main.MOD_ID, "nekker"),
            FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, NekkerEntity::new)
                    //Hitbox
                    .dimensions(EntityDimensions.changing(0.65f, 0.9f)).build());

    public static void addSpawns() {
        //Drowners
        SpawnRestriction.register(DROWNER, SpawnRestriction.Location.NO_RESTRICTIONS,
                Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, DrownerEntity::canSpawnDrowner);

            //In swamps
            BiomeModifications.addSpawn(BiomeSelectors.includeByKey(BiomeKeys.SWAMP,
                                                                    BiomeKeys.MANGROVE_SWAMP), SpawnGroup.MONSTER,
                    DROWNER, 130, 3, 5);

            //In beaches
            BiomeModifications.addSpawn(BiomeSelectors.includeByKey(BiomeKeys.BEACH), SpawnGroup.MONSTER,
                    DROWNER, 50, 2, 4);

            //Swimming in oceans/rivers
            BiomeModifications.addSpawn(BiomeSelectors.includeByKey(BiomeKeys.OCEAN,BiomeKeys.DEEP_OCEAN,
                                                                    BiomeKeys.LUKEWARM_OCEAN,BiomeKeys.DEEP_LUKEWARM_OCEAN,
                                                                    BiomeKeys.COLD_OCEAN,BiomeKeys.DEEP_COLD_OCEAN,
                                                                    BiomeKeys.RIVER), SpawnGroup.MONSTER,
                    DROWNER, 10, 2, 3);


        //Rotfiends
            SpawnRestriction.register(ROTFIEND, SpawnRestriction.Location.ON_GROUND,
                    Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, Necrophage_Base::canSpawnInDark);

            //In night
            BiomeModifications.addSpawn(BiomeSelectors.includeByKey(
                                                                    BiomeKeys.BIRCH_FOREST, BiomeKeys.FOREST, BiomeKeys.DARK_FOREST,
                                                                    BiomeKeys.DRIPSTONE_CAVES,
                                                                    BiomeKeys.OLD_GROWTH_BIRCH_FOREST, BiomeKeys.OLD_GROWTH_PINE_TAIGA, BiomeKeys.OLD_GROWTH_SPRUCE_TAIGA,
                                                                    BiomeKeys.PLAINS, BiomeKeys.SAVANNA, BiomeKeys.TAIGA), SpawnGroup.MONSTER,
                    ROTFIEND, 80, 4, 6);


        //Foglets
        SpawnRestriction.register(FOGLET, SpawnRestriction.Location.ON_GROUND,
                Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, Necrophage_Base::canSpawnInDark_NotCaves);

        //In swamps/rivers
        BiomeModifications.addSpawn(BiomeSelectors.includeByKey(BiomeKeys.SWAMP, BiomeKeys.MANGROVE_SWAMP,
                        BiomeKeys.RIVER
                ), SpawnGroup.MONSTER,
                FOGLET, 80, 1, 3);

        //In forests/mountains
        BiomeModifications.addSpawn(BiomeSelectors.includeByKey(BiomeKeys.OLD_GROWTH_SPRUCE_TAIGA, BiomeKeys.OLD_GROWTH_BIRCH_FOREST,
                        BiomeKeys.JAGGED_PEAKS, BiomeKeys.STONY_PEAKS, BiomeKeys.WINDSWEPT_HILLS, BiomeKeys.WINDSWEPT_FOREST, BiomeKeys.WINDSWEPT_GRAVELLY_HILLS
                ), SpawnGroup.MONSTER,
                FOGLET, 50, 1, 2);

        //In dark forests
        BiomeModifications.addSpawn(BiomeSelectors.includeByKey(BiomeKeys.DARK_FOREST
                ), SpawnGroup.MONSTER,
                FOGLET, 120, 1, 2);


        //Water Hags
        SpawnRestriction.register(WATER_HAG, SpawnRestriction.Location.ON_GROUND,
                Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, DrownerEntity::canSpawnDrowner);

        //In swamps
        BiomeModifications.addSpawn(BiomeSelectors.includeByKey(BiomeKeys.SWAMP,
                                                                BiomeKeys.MANGROVE_SWAMP), SpawnGroup.MONSTER,
                WATER_HAG, 80, 1, 2);

        //In rivers
        BiomeModifications.addSpawn(BiomeSelectors.includeByKey(BiomeKeys.RIVER), SpawnGroup.MONSTER,
                DROWNER, 20, 1, 2);



        //Grave Hags
        SpawnRestriction.register(GRAVE_HAG, SpawnRestriction.Location.ON_GROUND,
                Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, Necrophage_Base::canSpawnInDarkNotDeepDeepslate);

        //In night
        BiomeModifications.addSpawn(BiomeSelectors.includeByKey(
                        BiomeKeys.BIRCH_FOREST, BiomeKeys.FOREST, BiomeKeys.DARK_FOREST,
                        BiomeKeys.DRIPSTONE_CAVES,
                        BiomeKeys.OLD_GROWTH_BIRCH_FOREST, BiomeKeys.OLD_GROWTH_PINE_TAIGA, BiomeKeys.OLD_GROWTH_SPRUCE_TAIGA,
                        BiomeKeys.PLAINS, BiomeKeys.SAVANNA, BiomeKeys.TAIGA), SpawnGroup.MONSTER,
                GRAVE_HAG, 80, 1, 2);


        //Nekkers
        SpawnRestriction.register(NEKKER, SpawnRestriction.Location.ON_GROUND,
                Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, NekkerEntity::canSpawnNekker);

            //In night
            BiomeModifications.addSpawn(BiomeSelectors.includeByKey(
                                                                BiomeKeys.SAVANNA, BiomeKeys.PLAINS,
                                                                BiomeKeys.JUNGLE, BiomeKeys.SPARSE_JUNGLE,
                                                                BiomeKeys.TAIGA, BiomeKeys.OLD_GROWTH_PINE_TAIGA, BiomeKeys.OLD_GROWTH_SPRUCE_TAIGA,
                                                                BiomeKeys.FOREST, BiomeKeys.FLOWER_FOREST, BiomeKeys.BIRCH_FOREST, BiomeKeys. OLD_GROWTH_BIRCH_FOREST, BiomeKeys.DARK_FOREST), SpawnGroup.MONSTER,
                    NEKKER, 5, 4,6);

    }
}
