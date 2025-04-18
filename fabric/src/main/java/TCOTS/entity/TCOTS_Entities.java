package TCOTS.entity;

import TCOTS.TCOTS_Main;
import TCOTS.entity.misc.*;
import TCOTS.entity.misc.bolts.*;
import TCOTS.entity.necrophages.*;
import TCOTS.entity.ogroids.*;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.Heightmap;

@SuppressWarnings("all")
public class TCOTS_Entities {

//    1.0.0- Necrophages & Ogroids
// Necrophages
    //W3
    //xTODO: Drowner
    //xTODO: Rotfiend
    //xTODO: Grave Hag
    //xTODO: Water Hag
    //xTODO: Foglet
    //xTODO: Ghoul
    //xTODO: Alghoul
    //TODO: Wights - Next update
    //xTODO: Scurvers
    //W2
    //xTODO: Bullvore
    //W1
    //xTODO: Devourer
    //xTODO: Graveir


//  Ogroids
    //W3
    //xTODO: Nekkers
    //xTODO: Nekker Warriors
    //xTODO: Cyclopses
    //xTODO: Rock troll
    //xTODO: Ice troll
    //xTODO: Ice Giant (Boss)
    //W2
    //xTODO: Troll (Forest)


    //Necrophages
    public static final EntityType<DrownerEntity> DROWNER = Registry.register(
            BuiltInRegistries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "drowner"),
            FabricEntityTypeBuilder.create(MobCategory.MONSTER, DrownerEntity::new)
                    //Hitbox
                    .dimensions(EntityDimensions.scalable(0.78f, 1.9f)).build());
    public static final EntityType<DrownerPuddleEntity> DROWNER_PUDDLE = Registry.register(
            BuiltInRegistries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "drowner_puddle"),
            FabricEntityTypeBuilder.<DrownerPuddleEntity>create(MobCategory.MISC, DrownerPuddleEntity::new
                    ).fireImmune()
                    // Hitbox
                    .dimensions(EntityDimensions.scalable(0.78f, 0.1f)).build());


    public static final EntityType<RotfiendEntity> ROTFIEND = Registry.register(
            BuiltInRegistries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "rotfiend"),
            FabricEntityTypeBuilder.create(MobCategory.MONSTER, RotfiendEntity::new)
                    //Hitbox
                    .dimensions(EntityDimensions.scalable(0.78f, 1.9f)).build());


    public static final EntityType<GraveHagEntity> GRAVE_HAG = Registry.register(
            BuiltInRegistries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "grave_hag"),
            FabricEntityTypeBuilder.create(MobCategory.MONSTER, GraveHagEntity::new)
                    //Hitbox
                    .dimensions(EntityDimensions.scalable(0.78f, 1.9f)).build());

    public static final EntityType<WaterHagEntity> WATER_HAG = Registry.register(
            BuiltInRegistries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "water_hag"),
            FabricEntityTypeBuilder.create(MobCategory.MONSTER, WaterHagEntity::new)
                    //Hitbox
                    .dimensions(EntityDimensions.scalable(0.78f, 1.9f)).build());
    public static final EntityType<WaterHag_MudBallEntity> WATER_HAG_MUD_BALL = Registry.register(
            BuiltInRegistries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "water_hag_mud_ball"),
            FabricEntityTypeBuilder.<WaterHag_MudBallEntity>create(MobCategory.MISC, WaterHag_MudBallEntity::new
                    )
                    // Hitbox
                    .dimensions(EntityDimensions.scalable(0.25f, 0.25f)).build());

    public static final EntityType<FogletEntity> FOGLET = Registry.register(
            BuiltInRegistries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "foglet"),
            FabricEntityTypeBuilder.create(MobCategory.MONSTER, FogletEntity::new)
                    //Hitbox
                    .dimensions(EntityDimensions.scalable(0.78f, 1.9f)).build());
    public static final EntityType<FoglingEntity> FOGLING = Registry.register(
            BuiltInRegistries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "fogling"),
            FabricEntityTypeBuilder.create(MobCategory.MONSTER, FoglingEntity::new)
                    //Hitbox
                    .dimensions(EntityDimensions.scalable(0.78f, 1.9f)).build());


    public static final EntityType<GhoulEntity> GHOUL = Registry.register(
            BuiltInRegistries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "ghoul"),
            EntityType.Builder.of(GhoulEntity::new, MobCategory.MONSTER)
                    //Hitbox
                    .sized(1.4f, 0.9f)
                    .eyeHeight(0.62f)
                    .build());

    public static final EntityType<AlghoulEntity> ALGHOUL = Registry.register(
            BuiltInRegistries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "alghoul"),
            EntityType.Builder.of(AlghoulEntity::new, MobCategory.MONSTER)
                    //Hitbox
                    .sized(1.8f, 1.2f)
                    .eyeHeight(0.62f)
                    .build());

    public static final EntityType<ScurverEntity> SCURVER = Registry.register(
            BuiltInRegistries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "scurver"),
            FabricEntityTypeBuilder.create(MobCategory.MONSTER, ScurverEntity::new)
                    //Hitbox
                    .dimensions(EntityDimensions.scalable(0.78f, 1.9f)).build());
    public static final EntityType<ScurverSpineEntity> SCURVER_SPINE =
            Registry.register(BuiltInRegistries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "scurver_spike"),
                    FabricEntityTypeBuilder.<ScurverSpineEntity>create(MobCategory.MISC, ScurverSpineEntity::new)
                            .dimensions(EntityDimensions.fixed(0.25f, 0.25f))
                            .trackRangeBlocks(4).trackedUpdateRate(20).build());


    public static final EntityType<DevourerEntity> DEVOURER = Registry.register(
            BuiltInRegistries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "devourer"),
            FabricEntityTypeBuilder.create(MobCategory.MONSTER, DevourerEntity::new)
                    //Hitbox
                    .dimensions(EntityDimensions.scalable(0.85f, 1.8f)).build());

    public static final EntityType<GraveirEntity> GRAVEIR = Registry.register(
            BuiltInRegistries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "graveir"),
            FabricEntityTypeBuilder.create(MobCategory.MONSTER, GraveirEntity::new)
                    //Hitbox
                    .dimensions(EntityDimensions.scalable(1.4f, 2.65f)).build());

    public static final EntityType<BullvoreEntity> BULLVORE = Registry.register(
            BuiltInRegistries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "bullvore"),
            EntityType.Builder.of(BullvoreEntity::new, MobCategory.MONSTER)
                    //Hitbox
                    .sized(1.9975f, 3.3f)
                    .build());

    //Ogroids
    public static final EntityType<NekkerEntity> NEKKER = Registry.register(
            BuiltInRegistries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "nekker"),
            FabricEntityTypeBuilder.create(MobCategory.MONSTER, NekkerEntity::new)
                    //Hitbox
                    .dimensions(EntityDimensions.scalable(0.65f, 0.975f)).build());

    public static final EntityType<NekkerWarriorEntity> NEKKER_WARRIOR = Registry.register(
            BuiltInRegistries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "nekker_warrior"),
            FabricEntityTypeBuilder.create(MobCategory.MONSTER, NekkerWarriorEntity::new)
                    //Hitbox
                    .dimensions(EntityDimensions.scalable(0.7f, 1.3f)).build());

    public static final EntityType<CyclopsEntity> CYCLOPS = Registry.register(
            BuiltInRegistries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "cyclops"),
            FabricEntityTypeBuilder.create(MobCategory.MONSTER, CyclopsEntity::new)
                    //Hitbox
                    .dimensions(EntityDimensions.scalable(1.9975f, 5.0f)).build());

    public static final EntityType<RockTrollEntity> ROCK_TROLL = Registry.register(
            BuiltInRegistries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "rock_troll"),
            FabricEntityTypeBuilder.create(MobCategory.MONSTER, RockTrollEntity::new)
                    //Hitbox
                    .dimensions(EntityDimensions.scalable(1.8f, 2.7f)).build());
    public static final EntityType<Troll_RockProjectileEntity> TROLL_ROCK_PROJECTILE = Registry.register(
            BuiltInRegistries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "troll_projectile"),
            FabricEntityTypeBuilder.<Troll_RockProjectileEntity>create(MobCategory.MISC, Troll_RockProjectileEntity::new
                    )
                    // Hitbox
                    .dimensions(EntityDimensions.scalable(0.5f, 0.5f)).build());

    public static final EntityType<IceTrollEntity> ICE_TROLL = Registry.register(
            BuiltInRegistries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "ice_troll"),
            FabricEntityTypeBuilder.create(MobCategory.MONSTER, IceTrollEntity::new)
                    //Hitbox
                    .dimensions(EntityDimensions.scalable(1.8f, 2.7f)).build());

    public static final EntityType<ForestTrollEntity> FOREST_TROLL = Registry.register(
            BuiltInRegistries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "forest_troll"),
            FabricEntityTypeBuilder.create(MobCategory.MONSTER, ForestTrollEntity::new)
                    //Hitbox
                    .dimensions(EntityDimensions.scalable(1.8f, 2.8f)).build());


    public static final EntityType<IceGiantEntity> ICE_GIANT = Registry.register(
            BuiltInRegistries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "ice_giant"),
            FabricEntityTypeBuilder.create(MobCategory.MONSTER, IceGiantEntity::new)
                    //Hitbox
                    .dimensions(EntityDimensions.scalable(1.9975f, 4.4f)).build());
    public static final EntityType<AnchorProjectileEntity> ANCHOR_PROJECTILE =
            Registry.register(BuiltInRegistries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "anchor_projectile"),
                    EntityType.Builder.<AnchorProjectileEntity>of(AnchorProjectileEntity::new, MobCategory.MISC)
                            .sized(1.25f, 1.8f)
                            .eyeHeight(0.13F)
                            .clientTrackingRange(8)
                            .updateInterval(20)
                            .fireImmune()
                            .build());

    //Misc
    public static final EntityType<WitcherBombEntity> WITCHER_BOMB = Registry.register(
            BuiltInRegistries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "witcher_bomb"),
            FabricEntityTypeBuilder.<WitcherBombEntity>create(MobCategory.MISC, WitcherBombEntity::new)
                    // Hitbox
                    .dimensions(EntityDimensions.scalable(0.25f, 0.25f)).build());

    public static final EntityType<AreaEffectCloud> AREA_EFFECT_CLOUD = Registry.register(
            BuiltInRegistries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "dragons_dream_cloud"),
            FabricEntityTypeBuilder.<AreaEffectCloud>create(MobCategory.MISC, DragonsDreamCloud::new).fireImmune()
                    .dimensions(EntityDimensions.scalable(6.0f, 1.5f)).build()
            );

    public static final EntityType<BaseBoltProjectile> BASE_BOLT =
            Registry.register(BuiltInRegistries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "base_bolt"),
                    FabricEntityTypeBuilder.<BaseBoltProjectile>create(MobCategory.MISC, BaseBoltProjectile::new)
                            .dimensions(EntityDimensions.fixed(0.5f, 0.5f))
                            .trackRangeBlocks(4).trackedUpdateRate(20).build());

    public static final EntityType<BluntBoltProjectile> BLUNT_BOLT =
            Registry.register(BuiltInRegistries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "blunt_bolt"),
                    FabricEntityTypeBuilder.<BluntBoltProjectile>create(MobCategory.MISC, BluntBoltProjectile::new)
                            .dimensions(EntityDimensions.fixed(0.5f, 0.5f))
                            .trackRangeBlocks(4).trackedUpdateRate(20).build());

    public static final EntityType<PrecisionBoltProjectile> PRECISION_BOLT =
            Registry.register(BuiltInRegistries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "precision_bolt"),
                    FabricEntityTypeBuilder.<PrecisionBoltProjectile>create(MobCategory.MISC, PrecisionBoltProjectile::new)
                            .dimensions(EntityDimensions.fixed(0.5f, 0.5f))
                            .trackRangeBlocks(4).trackedUpdateRate(20).build());

    public static final EntityType<ExplodingBoltProjectile> EXPLODING_BOLT =
            Registry.register(BuiltInRegistries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "exploding_bolt"),
                    FabricEntityTypeBuilder.<ExplodingBoltProjectile>create(MobCategory.MISC, ExplodingBoltProjectile::new)
                            .dimensions(EntityDimensions.fixed(0.5f, 0.5f))
                            .trackRangeBlocks(4).trackedUpdateRate(20).build());

    public static final EntityType<BroadheadBoltProjectile> BROADHEAD_BOLT =
            Registry.register(BuiltInRegistries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "broadhead_bolt"),
                    FabricEntityTypeBuilder.<BroadheadBoltProjectile>create(MobCategory.MISC, BroadheadBoltProjectile::new)
                            .dimensions(EntityDimensions.fixed(0.5f, 0.5f))
                            .trackRangeBlocks(4).trackedUpdateRate(20).build());

    public static void addSpawns() {
        //Necrophages
        {
            //Drowners
            {
                SpawnPlacements.register(DROWNER, SpawnPlacementTypes.NO_RESTRICTIONS,
                        Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, DrownerEntity::canSpawnDrowner);

                //In swamps
                BiomeModifications.addSpawn(BiomeSelectors.includeByKey(Biomes.SWAMP,
                                Biomes.MANGROVE_SWAMP), MobCategory.MONSTER,
                        DROWNER, 130, 3, 5);

                //In beaches
                BiomeModifications.addSpawn(BiomeSelectors.includeByKey(Biomes.BEACH), MobCategory.MONSTER,
                        DROWNER, 50, 2, 4);

                //Swimming in oceans/rivers
                BiomeModifications.addSpawn(BiomeSelectors.includeByKey(Biomes.OCEAN, Biomes.DEEP_OCEAN,
                                Biomes.LUKEWARM_OCEAN, Biomes.DEEP_LUKEWARM_OCEAN,
                                Biomes.COLD_OCEAN, Biomes.DEEP_COLD_OCEAN,
                                Biomes.RIVER), MobCategory.MONSTER,
                        DROWNER, 8, 2, 3);
            }

            //Rotfiends
            {
                SpawnPlacements.register(ROTFIEND, SpawnPlacementTypes.ON_GROUND,
                        Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, RotfiendEntity::canSpawnInDarkW);

                //In night
                BiomeModifications.addSpawn(BiomeSelectors.includeByKey(
                                Biomes.BIRCH_FOREST, Biomes.FOREST, Biomes.DARK_FOREST,
                                Biomes.DRIPSTONE_CAVES,
                                Biomes.OLD_GROWTH_BIRCH_FOREST, Biomes.OLD_GROWTH_PINE_TAIGA, Biomes.OLD_GROWTH_SPRUCE_TAIGA,
                                Biomes.PLAINS, Biomes.TAIGA), MobCategory.MONSTER,
                        ROTFIEND, 80, 4, 6);
            }

            //Foglets
            {
                SpawnPlacements.register(FOGLET, SpawnPlacementTypes.ON_GROUND,
                        Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, FogletEntity::canSpawnInDark_NotCaves);

                //In swamps/rivers
                BiomeModifications.addSpawn(BiomeSelectors.includeByKey(Biomes.SWAMP, Biomes.MANGROVE_SWAMP,
                                Biomes.RIVER
                        ), MobCategory.MONSTER,
                        FOGLET, 80, 1, 3);

                //In forests/mountains
                BiomeModifications.addSpawn(BiomeSelectors.includeByKey(Biomes.OLD_GROWTH_SPRUCE_TAIGA, Biomes.OLD_GROWTH_BIRCH_FOREST,
                                Biomes.JAGGED_PEAKS, Biomes.STONY_PEAKS, Biomes.WINDSWEPT_HILLS, Biomes.WINDSWEPT_FOREST, Biomes.WINDSWEPT_GRAVELLY_HILLS
                        ), MobCategory.MONSTER,
                        FOGLET, 50, 1, 2);

                //In dark forests
                BiomeModifications.addSpawn(BiomeSelectors.includeByKey(Biomes.DARK_FOREST
                        ), MobCategory.MONSTER,
                        FOGLET, 120, 1, 2);
            }

            //Water Hags
            {
                SpawnPlacements.register(WATER_HAG, SpawnPlacementTypes.ON_GROUND,
                        Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, DrownerEntity::canSpawnDrowner);

                //In swamps
                BiomeModifications.addSpawn(BiomeSelectors.includeByKey(Biomes.SWAMP,
                                Biomes.MANGROVE_SWAMP), MobCategory.MONSTER,
                        WATER_HAG, 80, 1, 2);

                //In rivers
                BiomeModifications.addSpawn(BiomeSelectors.includeByKey(Biomes.RIVER), MobCategory.MONSTER,
                        DROWNER, 20, 1, 2);
            }

            //Grave Hags
            {
                SpawnPlacements.register(GRAVE_HAG, SpawnPlacementTypes.ON_GROUND,
                        Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, GraveHagEntity::canSpawnInDarkNotBelowDeepslate);

                //In night
                BiomeModifications.addSpawn(BiomeSelectors.includeByKey(
                                Biomes.BIRCH_FOREST, Biomes.FOREST, Biomes.DARK_FOREST,
                                Biomes.DRIPSTONE_CAVES,
                                Biomes.OLD_GROWTH_BIRCH_FOREST, Biomes.OLD_GROWTH_PINE_TAIGA, Biomes.OLD_GROWTH_SPRUCE_TAIGA,
                                Biomes.PLAINS, Biomes.SAVANNA, Biomes.TAIGA), MobCategory.MONSTER,
                        GRAVE_HAG, 80, 1, 2);
            }

            //Ghouls & Alghouls
            {
                SpawnPlacements.register(GHOUL, SpawnPlacementTypes.ON_GROUND,
                        Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, GhoulEntity::canSpawnGhoul);

                //In night
                BiomeModifications.addSpawn(BiomeSelectors.includeByKey(
                                Biomes.SAVANNA, Biomes.PLAINS,
                                Biomes.TAIGA, Biomes.OLD_GROWTH_PINE_TAIGA, Biomes.OLD_GROWTH_SPRUCE_TAIGA,
                                Biomes.FOREST, Biomes.FLOWER_FOREST, Biomes.BIRCH_FOREST, Biomes.OLD_GROWTH_BIRCH_FOREST, Biomes.DARK_FOREST), MobCategory.MONSTER,
                        GHOUL, 10, 3, 5);
            }

            //Scurvers
            {
                SpawnPlacements.register(SCURVER, SpawnPlacementTypes.ON_GROUND,
                        Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, ScurverEntity::canSpawnInDarkW);

                //In night
                BiomeModifications.addSpawn(BiomeSelectors.includeByKey(
                                Biomes.BIRCH_FOREST, Biomes.FOREST, Biomes.DARK_FOREST,
                                Biomes.OLD_GROWTH_BIRCH_FOREST,
                                Biomes.PLAINS, Biomes.SAVANNA,
                                Biomes.JUNGLE, Biomes.SPARSE_JUNGLE, Biomes.BAMBOO_JUNGLE), MobCategory.MONSTER,
                        SCURVER, 40, 2, 3);
            }

            //Devourer
            {
                SpawnPlacements.register(DEVOURER, SpawnPlacementTypes.ON_GROUND,
                        Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, DevourerEntity::canSpawnInDarkW);

                //In night
                BiomeModifications.addSpawn(BiomeSelectors.includeByKey(
                                Biomes.BIRCH_FOREST, Biomes.FOREST,
                                Biomes.SWAMP, Biomes.RIVER,
                                Biomes.PLAINS), MobCategory.MONSTER,
                        DEVOURER, 60, 3, 4);
            }

            //Graveir
            {
                SpawnPlacements.register(GRAVEIR, SpawnPlacementTypes.ON_GROUND,
                        Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, GraveirEntity::canSpawnGraveir);

                //In Caves
                BiomeModifications.addSpawn(
                        BiomeSelectors.foundInOverworld()
                                .and(BiomeSelectors.excludeByKey(Biomes.DEEP_DARK, Biomes.LUSH_CAVES, Biomes.MUSHROOM_FIELDS)
                                ), MobCategory.MONSTER,
                        GRAVEIR, 60, 1, 2);
            }

            //Bullvore
            {
                SpawnPlacements.register(BULLVORE, SpawnPlacementTypes.ON_GROUND,
                        Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, BullvoreEntity::canSpawnInDarkW);
            }
        }

        //Ogroids
        {
            //Nekkers
            {
                SpawnPlacements.register(NEKKER, SpawnPlacementTypes.ON_GROUND,
                        Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, NekkerEntity::canSpawnNekker);

                //In night
                BiomeModifications.addSpawn(BiomeSelectors.includeByKey(
                                Biomes.SAVANNA, Biomes.PLAINS,
                                Biomes.JUNGLE, Biomes.SPARSE_JUNGLE,
                                Biomes.TAIGA, Biomes.OLD_GROWTH_PINE_TAIGA, Biomes.OLD_GROWTH_SPRUCE_TAIGA,
                                Biomes.FOREST, Biomes.FLOWER_FOREST, Biomes.BIRCH_FOREST, Biomes.OLD_GROWTH_BIRCH_FOREST, Biomes.DARK_FOREST), MobCategory.MONSTER,
                        NEKKER, 5, 4, 6);
            }

            //Cyclops
            {
                SpawnPlacements.register(CYCLOPS, SpawnPlacementTypes.ON_GROUND,
                        Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, CyclopsEntity::canCyclopsSpawn);

                //In snowy plains/mountains/taigas
                BiomeModifications.addSpawn(BiomeSelectors.includeByKey(
                                //Snowy Plains
                                Biomes.SNOWY_PLAINS, Biomes.STONY_SHORE,
                                //Is_Hill
                                Biomes.WINDSWEPT_HILLS,Biomes.WINDSWEPT_FOREST,Biomes.WINDSWEPT_GRAVELLY_HILLS,
                                //Is_Mountain
                                Biomes.MEADOW,Biomes.FROZEN_PEAKS,Biomes.JAGGED_PEAKS,Biomes.STONY_PEAKS,Biomes.SNOWY_SLOPES,
                                //Is_Taiga
                                Biomes.OLD_GROWTH_PINE_TAIGA,Biomes.OLD_GROWTH_SPRUCE_TAIGA), MobCategory.MONSTER,
                        CYCLOPS, 15, 1, 1);
            }

            //Rock Troll
            {
                SpawnPlacements.register(ROCK_TROLL, SpawnPlacementTypes.ON_GROUND,
                        Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, RockTrollEntity::canSpawnInDarkNotBelowDeepslate);

                //In mountains/taigas
                BiomeModifications.addSpawn(BiomeSelectors.includeByKey(
                                //Mountains
                                Biomes.STONY_PEAKS, Biomes.MEADOW, Biomes.WINDSWEPT_HILLS, Biomes.WINDSWEPT_GRAVELLY_HILLS, Biomes.WINDSWEPT_FOREST,
                                //Taiga
                                Biomes.TAIGA, Biomes.OLD_GROWTH_SPRUCE_TAIGA, Biomes.OLD_GROWTH_PINE_TAIGA,
                                //Caves/Shore
                                Biomes.DRIPSTONE_CAVES, Biomes.STONY_SHORE), MobCategory.MONSTER,
                        ROCK_TROLL, 5, 1, 1);
            }

            //Ice Troll
            {
                SpawnPlacements.register(ICE_TROLL, SpawnPlacementTypes.ON_GROUND,
                        Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, IceTrollEntity::canSpawnInDarkNotBelowDeepslate);
                //In snowy plains/mountains/taigas
                BiomeModifications.addSpawn(BiomeSelectors.includeByKey(
                                //Mountains
                                Biomes.JAGGED_PEAKS, Biomes.SNOWY_SLOPES, Biomes.GROVE,
                                //Snowy Plains
                                Biomes.SNOWY_PLAINS, Biomes.ICE_SPIKES), MobCategory.MONSTER,
                        ICE_TROLL, 2, 1, 1);
            }


            //Forest Troll
            {
                SpawnPlacements.register(FOREST_TROLL, SpawnPlacementTypes.ON_GROUND,
                        Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, ForestTrollEntity::canSpawnInDarkNotBelowDeepslate);
                //In forests
                BiomeModifications.addSpawn(BiomeSelectors.includeByKey(
                                //Forest
                                Biomes.FOREST, Biomes.FLOWER_FOREST, Biomes.BIRCH_FOREST, Biomes.OLD_GROWTH_BIRCH_FOREST,
                                //Dark Forest
                                Biomes.DARK_FOREST), MobCategory.MONSTER,
                        FOREST_TROLL, 5, 1, 1);
            }
        }
    }

    public static void setEntitiesAttributes(){
        //Necrophages
        {
            //Drowner
            FabricDefaultAttributeRegistry.register(TCOTS_Entities.DROWNER, DrownerEntity.setAttributes());

            //Rotfiend
            FabricDefaultAttributeRegistry.register(TCOTS_Entities.ROTFIEND, RotfiendEntity.setAttributes());

            //Grave Hag
            FabricDefaultAttributeRegistry.register(TCOTS_Entities.GRAVE_HAG, GraveHagEntity.setAttributes());

            //Water Hag
            FabricDefaultAttributeRegistry.register(TCOTS_Entities.WATER_HAG, WaterHagEntity.setAttributes());

            //Foglet
            FabricDefaultAttributeRegistry.register(TCOTS_Entities.FOGLET, FogletEntity.setAttributes());
            FabricDefaultAttributeRegistry.register(TCOTS_Entities.FOGLING, FoglingEntity.setAttributes());

            //Ghoul
            FabricDefaultAttributeRegistry.register(TCOTS_Entities.GHOUL, GhoulEntity.setAttributes());

            //Alghoul
            FabricDefaultAttributeRegistry.register(TCOTS_Entities.ALGHOUL, AlghoulEntity.setAttributes());

            //Scurver
            FabricDefaultAttributeRegistry.register(TCOTS_Entities.SCURVER, ScurverEntity.setAttributes());

            //Devourer
            FabricDefaultAttributeRegistry.register(TCOTS_Entities.DEVOURER, DevourerEntity.setAttributes());

            //Graveir
            FabricDefaultAttributeRegistry.register(TCOTS_Entities.GRAVEIR, GraveirEntity.setAttributes());

            //Bullvore
            FabricDefaultAttributeRegistry.register(TCOTS_Entities.BULLVORE, BullvoreEntity.setAttributes());
        }

        //Ogroids
        {
            //Nekker
            FabricDefaultAttributeRegistry.register(TCOTS_Entities.NEKKER, NekkerEntity.setAttributes());

            //Nekker Warrior
            FabricDefaultAttributeRegistry.register(TCOTS_Entities.NEKKER_WARRIOR, NekkerWarriorEntity.setAttributes());

            //Cyclops
            FabricDefaultAttributeRegistry.register(TCOTS_Entities.CYCLOPS, CyclopsEntity.setAttributes());

            //Rock Troll
            FabricDefaultAttributeRegistry.register(TCOTS_Entities.ROCK_TROLL, RockTrollEntity.setAttributes());

            //Ice Troll
            FabricDefaultAttributeRegistry.register(TCOTS_Entities.ICE_TROLL, IceTrollEntity.setAttributes());

            //Forest Troll
            FabricDefaultAttributeRegistry.register(TCOTS_Entities.FOREST_TROLL, ForestTrollEntity.setAttributes());

            //Ice Giant
            FabricDefaultAttributeRegistry.register(TCOTS_Entities.ICE_GIANT, IceGiantEntity.setAttributes());

        }
    }

}
