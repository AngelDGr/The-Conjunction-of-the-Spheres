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
import net.minecraft.entity.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.BiomeKeys;

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


    public static final TagKey<EntityType<?>> IGNITING_ENTITIES = createTag("igniting_entities");
    public static final TagKey<EntityType<?>> DIMERITIUM_REMOVAL = createTag("dimeritium_removal");
    public static final TagKey<EntityType<?>> DIMERITIUM_DAMAGE = createTag("dimeritium_damage");
    public static final TagKey<EntityType<?>> BOSS_TAG = TagKey.of(RegistryKeys.ENTITY_TYPE, Identifier.of("c", "bosses"));



    public static final TagKey<EntityType<?>> NECROPHAGES = createTag("necrophages");
    public static final TagKey<EntityType<?>> OGROIDS = createTag("ogroids");
    public static final TagKey<EntityType<?>> SPECTERS = createTag("specters");
    public static final TagKey<EntityType<?>> VAMPIRES = createTag("vampires");
    public static final TagKey<EntityType<?>> INSECTOIDS = createTag("insectoid");
    public static final TagKey<EntityType<?>> BEASTS = createTag("beasts");
    public static final TagKey<EntityType<?>> ELEMENTA = createTag("elementa");
    public static final TagKey<EntityType<?>> HYBRIDS = createTag("hybrids");
    public static final TagKey<EntityType<?>> CURSED_ONES = createTag("cursed_ones");
    public static final TagKey<EntityType<?>> DRACONIDS = createTag("draconids");
    public static final TagKey<EntityType<?>> RELICTS = createTag("relicts");
    //Necrophages
    public static final EntityType<DrownerEntity> DROWNER = Registry.register(
            Registries.ENTITY_TYPE, Identifier.of(TCOTS_Main.MOD_ID, "drowner"),
            FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, DrownerEntity::new)
                    //Hitbox
                    .dimensions(EntityDimensions.changing(0.78f, 1.9f)).build());
    public static final EntityType<DrownerPuddleEntity> DROWNER_PUDDLE = Registry.register(
            Registries.ENTITY_TYPE, Identifier.of(TCOTS_Main.MOD_ID, "drowner_puddle"),
            FabricEntityTypeBuilder.<DrownerPuddleEntity>create(SpawnGroup.MISC, DrownerPuddleEntity::new
                    ).fireImmune()
                    // Hitbox
                    .dimensions(EntityDimensions.changing(0.78f, 0.1f)).build());


    public static final EntityType<RotfiendEntity> ROTFIEND = Registry.register(
            Registries.ENTITY_TYPE, Identifier.of(TCOTS_Main.MOD_ID, "rotfiend"),
            FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, RotfiendEntity::new)
                    //Hitbox
                    .dimensions(EntityDimensions.changing(0.78f, 1.9f)).build());


    public static final EntityType<GraveHagEntity> GRAVE_HAG = Registry.register(
            Registries.ENTITY_TYPE, Identifier.of(TCOTS_Main.MOD_ID, "grave_hag"),
            FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, GraveHagEntity::new)
                    //Hitbox
                    .dimensions(EntityDimensions.changing(0.78f, 1.9f)).build());

    public static final EntityType<WaterHagEntity> WATER_HAG = Registry.register(
            Registries.ENTITY_TYPE, Identifier.of(TCOTS_Main.MOD_ID, "water_hag"),
            FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, WaterHagEntity::new)
                    //Hitbox
                    .dimensions(EntityDimensions.changing(0.78f, 1.9f)).build());
    public static final EntityType<WaterHag_MudBallEntity> WATER_HAG_MUD_BALL = Registry.register(
            Registries.ENTITY_TYPE, Identifier.of(TCOTS_Main.MOD_ID, "water_hag_mud_ball"),
            FabricEntityTypeBuilder.<WaterHag_MudBallEntity>create(SpawnGroup.MISC, WaterHag_MudBallEntity::new
                    )
                    // Hitbox
                    .dimensions(EntityDimensions.changing(0.25f, 0.25f)).build());

    public static final EntityType<FogletEntity> FOGLET = Registry.register(
            Registries.ENTITY_TYPE, Identifier.of(TCOTS_Main.MOD_ID, "foglet"),
            FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, FogletEntity::new)
                    //Hitbox
                    .dimensions(EntityDimensions.changing(0.78f, 1.9f)).build());
    public static final EntityType<FoglingEntity> FOGLING = Registry.register(
            Registries.ENTITY_TYPE, Identifier.of(TCOTS_Main.MOD_ID, "fogling"),
            FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, FoglingEntity::new)
                    //Hitbox
                    .dimensions(EntityDimensions.changing(0.78f, 1.9f)).build());


    public static final EntityType<GhoulEntity> GHOUL = Registry.register(
            Registries.ENTITY_TYPE, Identifier.of(TCOTS_Main.MOD_ID, "ghoul"),
            EntityType.Builder.create(GhoulEntity::new, SpawnGroup.MONSTER)
                    //Hitbox
                    .dimensions(1.4f, 0.9f)
                    .eyeHeight(0.62f)
                    .build());

    public static final EntityType<AlghoulEntity> ALGHOUL = Registry.register(
            Registries.ENTITY_TYPE, Identifier.of(TCOTS_Main.MOD_ID, "alghoul"),
            EntityType.Builder.create(AlghoulEntity::new, SpawnGroup.MONSTER)
                    //Hitbox
                    .dimensions(1.8f, 1.2f)
                    .eyeHeight(0.62f)
                    .build());

    public static final EntityType<ScurverEntity> SCURVER = Registry.register(
            Registries.ENTITY_TYPE, Identifier.of(TCOTS_Main.MOD_ID, "scurver"),
            FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, ScurverEntity::new)
                    //Hitbox
                    .dimensions(EntityDimensions.changing(0.78f, 1.9f)).build());
    public static final EntityType<ScurverSpineEntity> SCURVER_SPINE =
            Registry.register(Registries.ENTITY_TYPE, Identifier.of(TCOTS_Main.MOD_ID, "scurver_spike"),
                    FabricEntityTypeBuilder.<ScurverSpineEntity>create(SpawnGroup.MISC, ScurverSpineEntity::new)
                            .dimensions(EntityDimensions.fixed(0.25f, 0.25f))
                            .trackRangeBlocks(4).trackedUpdateRate(20).build());


    public static final EntityType<DevourerEntity> DEVOURER = Registry.register(
            Registries.ENTITY_TYPE, Identifier.of(TCOTS_Main.MOD_ID, "devourer"),
            FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, DevourerEntity::new)
                    //Hitbox
                    .dimensions(EntityDimensions.changing(0.85f, 1.8f)).build());

    public static final EntityType<GraveirEntity> GRAVEIR = Registry.register(
            Registries.ENTITY_TYPE, Identifier.of(TCOTS_Main.MOD_ID, "graveir"),
            FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, GraveirEntity::new)
                    //Hitbox
                    .dimensions(EntityDimensions.changing(1.4f, 2.65f)).build());

    public static final EntityType<BullvoreEntity> BULLVORE = Registry.register(
            Registries.ENTITY_TYPE, Identifier.of(TCOTS_Main.MOD_ID, "bullvore"),
            EntityType.Builder.create(BullvoreEntity::new, SpawnGroup.MONSTER)
                    //Hitbox
                    .dimensions(1.9975f, 3.3f)
                    .build());

    //Ogroids
    public static final EntityType<NekkerEntity> NEKKER = Registry.register(
            Registries.ENTITY_TYPE, Identifier.of(TCOTS_Main.MOD_ID, "nekker"),
            FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, NekkerEntity::new)
                    //Hitbox
                    .dimensions(EntityDimensions.changing(0.65f, 0.975f)).build());

    public static final EntityType<NekkerWarriorEntity> NEKKER_WARRIOR = Registry.register(
            Registries.ENTITY_TYPE, Identifier.of(TCOTS_Main.MOD_ID, "nekker_warrior"),
            FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, NekkerWarriorEntity::new)
                    //Hitbox
                    .dimensions(EntityDimensions.changing(0.7f, 1.3f)).build());

    public static final EntityType<CyclopsEntity> CYCLOPS = Registry.register(
            Registries.ENTITY_TYPE, Identifier.of(TCOTS_Main.MOD_ID, "cyclops"),
            FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, CyclopsEntity::new)
                    //Hitbox
                    .dimensions(EntityDimensions.changing(1.9975f, 5.0f)).build());

    public static final EntityType<RockTrollEntity> ROCK_TROLL = Registry.register(
            Registries.ENTITY_TYPE, Identifier.of(TCOTS_Main.MOD_ID, "rock_troll"),
            FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, RockTrollEntity::new)
                    //Hitbox
                    .dimensions(EntityDimensions.changing(1.8f, 2.7f)).build());
    public static final EntityType<Troll_RockProjectileEntity> TROLL_ROCK_PROJECTILE = Registry.register(
            Registries.ENTITY_TYPE, Identifier.of(TCOTS_Main.MOD_ID, "troll_projectile"),
            FabricEntityTypeBuilder.<Troll_RockProjectileEntity>create(SpawnGroup.MISC, Troll_RockProjectileEntity::new
                    )
                    // Hitbox
                    .dimensions(EntityDimensions.changing(0.5f, 0.5f)).build());

    public static final EntityType<IceTrollEntity> ICE_TROLL = Registry.register(
            Registries.ENTITY_TYPE, Identifier.of(TCOTS_Main.MOD_ID, "ice_troll"),
            FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, IceTrollEntity::new)
                    //Hitbox
                    .dimensions(EntityDimensions.changing(1.8f, 2.7f)).build());

    public static final EntityType<ForestTrollEntity> FOREST_TROLL = Registry.register(
            Registries.ENTITY_TYPE, Identifier.of(TCOTS_Main.MOD_ID, "forest_troll"),
            FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, ForestTrollEntity::new)
                    //Hitbox
                    .dimensions(EntityDimensions.changing(1.8f, 2.8f)).build());


    public static final EntityType<IceGiantEntity> ICE_GIANT = Registry.register(
            Registries.ENTITY_TYPE, Identifier.of(TCOTS_Main.MOD_ID, "ice_giant"),
            FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, IceGiantEntity::new)
                    //Hitbox
                    .dimensions(EntityDimensions.changing(1.9975f, 4.4f)).build());
    public static final EntityType<AnchorProjectileEntity> ANCHOR_PROJECTILE =
            Registry.register(Registries.ENTITY_TYPE, Identifier.of(TCOTS_Main.MOD_ID, "anchor_projectile"),
                    EntityType.Builder.<AnchorProjectileEntity>create(AnchorProjectileEntity::new, SpawnGroup.MISC)
                            .dimensions(1.25f, 1.8f)
                            .eyeHeight(0.13F)
                            .maxTrackingRange(8)
                            .trackingTickInterval(20)
                            .makeFireImmune()
                            .build());

    //Misc
    public static final EntityType<WitcherBombEntity> WITCHER_BOMB = Registry.register(
            Registries.ENTITY_TYPE, Identifier.of(TCOTS_Main.MOD_ID, "witcher_bomb"),
            FabricEntityTypeBuilder.<WitcherBombEntity>create(SpawnGroup.MISC, WitcherBombEntity::new)
                    // Hitbox
                    .dimensions(EntityDimensions.changing(0.25f, 0.25f)).build());

    public static final EntityType<AreaEffectCloudEntity> AREA_EFFECT_CLOUD = Registry.register(
            Registries.ENTITY_TYPE, Identifier.of(TCOTS_Main.MOD_ID, "dragons_dream_cloud"),
            FabricEntityTypeBuilder.<AreaEffectCloudEntity>create(SpawnGroup.MISC, DragonsDreamCloud::new).fireImmune()
                    .dimensions(EntityDimensions.changing(6.0f, 1.5f)).build()
            );

    public static final EntityType<BaseBoltProjectile> BASE_BOLT =
            Registry.register(Registries.ENTITY_TYPE, Identifier.of(TCOTS_Main.MOD_ID, "base_bolt"),
                    FabricEntityTypeBuilder.<BaseBoltProjectile>create(SpawnGroup.MISC, BaseBoltProjectile::new)
                            .dimensions(EntityDimensions.fixed(0.5f, 0.5f))
                            .trackRangeBlocks(4).trackedUpdateRate(20).build());

    public static final EntityType<BluntBoltProjectile> BLUNT_BOLT =
            Registry.register(Registries.ENTITY_TYPE, Identifier.of(TCOTS_Main.MOD_ID, "blunt_bolt"),
                    FabricEntityTypeBuilder.<BluntBoltProjectile>create(SpawnGroup.MISC, BluntBoltProjectile::new)
                            .dimensions(EntityDimensions.fixed(0.5f, 0.5f))
                            .trackRangeBlocks(4).trackedUpdateRate(20).build());

    public static final EntityType<PrecisionBoltProjectile> PRECISION_BOLT =
            Registry.register(Registries.ENTITY_TYPE, Identifier.of(TCOTS_Main.MOD_ID, "precision_bolt"),
                    FabricEntityTypeBuilder.<PrecisionBoltProjectile>create(SpawnGroup.MISC, PrecisionBoltProjectile::new)
                            .dimensions(EntityDimensions.fixed(0.5f, 0.5f))
                            .trackRangeBlocks(4).trackedUpdateRate(20).build());

    public static final EntityType<ExplodingBoltProjectile> EXPLODING_BOLT =
            Registry.register(Registries.ENTITY_TYPE, Identifier.of(TCOTS_Main.MOD_ID, "exploding_bolt"),
                    FabricEntityTypeBuilder.<ExplodingBoltProjectile>create(SpawnGroup.MISC, ExplodingBoltProjectile::new)
                            .dimensions(EntityDimensions.fixed(0.5f, 0.5f))
                            .trackRangeBlocks(4).trackedUpdateRate(20).build());

    public static final EntityType<BroadheadBoltProjectile> BROADHEAD_BOLT =
            Registry.register(Registries.ENTITY_TYPE, Identifier.of(TCOTS_Main.MOD_ID, "broadhead_bolt"),
                    FabricEntityTypeBuilder.<BroadheadBoltProjectile>create(SpawnGroup.MISC, BroadheadBoltProjectile::new)
                            .dimensions(EntityDimensions.fixed(0.5f, 0.5f))
                            .trackRangeBlocks(4).trackedUpdateRate(20).build());

    public static void addSpawns() {
        //Necrophages
        {
            //Drowners
            {
                SpawnRestriction.register(DROWNER, SpawnLocationTypes.UNRESTRICTED,
                        Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, DrownerEntity::canSpawnDrowner);

                //In swamps
                BiomeModifications.addSpawn(BiomeSelectors.includeByKey(BiomeKeys.SWAMP,
                                BiomeKeys.MANGROVE_SWAMP), SpawnGroup.MONSTER,
                        DROWNER, 130, 3, 5);

                //In beaches
                BiomeModifications.addSpawn(BiomeSelectors.includeByKey(BiomeKeys.BEACH), SpawnGroup.MONSTER,
                        DROWNER, 50, 2, 4);

                //Swimming in oceans/rivers
                BiomeModifications.addSpawn(BiomeSelectors.includeByKey(BiomeKeys.OCEAN, BiomeKeys.DEEP_OCEAN,
                                BiomeKeys.LUKEWARM_OCEAN, BiomeKeys.DEEP_LUKEWARM_OCEAN,
                                BiomeKeys.COLD_OCEAN, BiomeKeys.DEEP_COLD_OCEAN,
                                BiomeKeys.RIVER), SpawnGroup.MONSTER,
                        DROWNER, 8, 2, 3);
            }

            //Rotfiends
            {
                SpawnRestriction.register(ROTFIEND, SpawnLocationTypes.ON_GROUND,
                        Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, RotfiendEntity::canSpawnInDarkW);

                //In night
                BiomeModifications.addSpawn(BiomeSelectors.includeByKey(
                                BiomeKeys.BIRCH_FOREST, BiomeKeys.FOREST, BiomeKeys.DARK_FOREST,
                                BiomeKeys.DRIPSTONE_CAVES,
                                BiomeKeys.OLD_GROWTH_BIRCH_FOREST, BiomeKeys.OLD_GROWTH_PINE_TAIGA, BiomeKeys.OLD_GROWTH_SPRUCE_TAIGA,
                                BiomeKeys.PLAINS, BiomeKeys.TAIGA), SpawnGroup.MONSTER,
                        ROTFIEND, 80, 4, 6);
            }

            //Foglets
            {
                SpawnRestriction.register(FOGLET, SpawnLocationTypes.ON_GROUND,
                        Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, FogletEntity::canSpawnInDark_NotCaves);

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
            }

            //Water Hags
            {
                SpawnRestriction.register(WATER_HAG, SpawnLocationTypes.ON_GROUND,
                        Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, DrownerEntity::canSpawnDrowner);

                //In swamps
                BiomeModifications.addSpawn(BiomeSelectors.includeByKey(BiomeKeys.SWAMP,
                                BiomeKeys.MANGROVE_SWAMP), SpawnGroup.MONSTER,
                        WATER_HAG, 80, 1, 2);

                //In rivers
                BiomeModifications.addSpawn(BiomeSelectors.includeByKey(BiomeKeys.RIVER), SpawnGroup.MONSTER,
                        DROWNER, 20, 1, 2);
            }

            //Grave Hags
            {
                SpawnRestriction.register(GRAVE_HAG, SpawnLocationTypes.ON_GROUND,
                        Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, GraveHagEntity::canSpawnInDarkNotBelowDeepslate);

                //In night
                BiomeModifications.addSpawn(BiomeSelectors.includeByKey(
                                BiomeKeys.BIRCH_FOREST, BiomeKeys.FOREST, BiomeKeys.DARK_FOREST,
                                BiomeKeys.DRIPSTONE_CAVES,
                                BiomeKeys.OLD_GROWTH_BIRCH_FOREST, BiomeKeys.OLD_GROWTH_PINE_TAIGA, BiomeKeys.OLD_GROWTH_SPRUCE_TAIGA,
                                BiomeKeys.PLAINS, BiomeKeys.SAVANNA, BiomeKeys.TAIGA), SpawnGroup.MONSTER,
                        GRAVE_HAG, 80, 1, 2);
            }

            //Ghouls & Alghouls
            {
                SpawnRestriction.register(GHOUL, SpawnLocationTypes.ON_GROUND,
                        Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, GhoulEntity::canSpawnGhoul);

                //In night
                BiomeModifications.addSpawn(BiomeSelectors.includeByKey(
                                BiomeKeys.SAVANNA, BiomeKeys.PLAINS,
                                BiomeKeys.TAIGA, BiomeKeys.OLD_GROWTH_PINE_TAIGA, BiomeKeys.OLD_GROWTH_SPRUCE_TAIGA,
                                BiomeKeys.FOREST, BiomeKeys.FLOWER_FOREST, BiomeKeys.BIRCH_FOREST, BiomeKeys.OLD_GROWTH_BIRCH_FOREST, BiomeKeys.DARK_FOREST), SpawnGroup.MONSTER,
                        GHOUL, 10, 3, 5);
            }

            //Scurvers
            {
                SpawnRestriction.register(SCURVER, SpawnLocationTypes.ON_GROUND,
                        Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, ScurverEntity::canSpawnInDarkW);

                //In night
                BiomeModifications.addSpawn(BiomeSelectors.includeByKey(
                                BiomeKeys.BIRCH_FOREST, BiomeKeys.FOREST, BiomeKeys.DARK_FOREST,
                                BiomeKeys.OLD_GROWTH_BIRCH_FOREST,
                                BiomeKeys.PLAINS, BiomeKeys.SAVANNA,
                                BiomeKeys.JUNGLE, BiomeKeys.SPARSE_JUNGLE, BiomeKeys.BAMBOO_JUNGLE), SpawnGroup.MONSTER,
                        SCURVER, 40, 2, 3);
            }

            //Devourer
            {
                SpawnRestriction.register(DEVOURER, SpawnLocationTypes.ON_GROUND,
                        Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, DevourerEntity::canSpawnInDarkW);

                //In night
                BiomeModifications.addSpawn(BiomeSelectors.includeByKey(
                                BiomeKeys.BIRCH_FOREST, BiomeKeys.FOREST,
                                BiomeKeys.SWAMP, BiomeKeys.RIVER,
                                BiomeKeys.PLAINS), SpawnGroup.MONSTER,
                        DEVOURER, 60, 3, 4);
            }

            //Graveir
            {
                SpawnRestriction.register(GRAVEIR, SpawnLocationTypes.ON_GROUND,
                        Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, GraveirEntity::canSpawnGraveir);

                //In Caves
                BiomeModifications.addSpawn(
                        BiomeSelectors.foundInOverworld()
                                .and(BiomeSelectors.excludeByKey(BiomeKeys.DEEP_DARK, BiomeKeys.LUSH_CAVES, BiomeKeys.MUSHROOM_FIELDS)
                                ), SpawnGroup.MONSTER,
                        GRAVEIR, 60, 1, 2);
            }

            //Bullvore
            {
                SpawnRestriction.register(BULLVORE, SpawnLocationTypes.ON_GROUND,
                        Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, BullvoreEntity::canSpawnInDarkW);
            }
        }

        //Ogroids
        {
            //Nekkers
            {
                SpawnRestriction.register(NEKKER, SpawnLocationTypes.ON_GROUND,
                        Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, NekkerEntity::canSpawnNekker);

                //In night
                BiomeModifications.addSpawn(BiomeSelectors.includeByKey(
                                BiomeKeys.SAVANNA, BiomeKeys.PLAINS,
                                BiomeKeys.JUNGLE, BiomeKeys.SPARSE_JUNGLE,
                                BiomeKeys.TAIGA, BiomeKeys.OLD_GROWTH_PINE_TAIGA, BiomeKeys.OLD_GROWTH_SPRUCE_TAIGA,
                                BiomeKeys.FOREST, BiomeKeys.FLOWER_FOREST, BiomeKeys.BIRCH_FOREST, BiomeKeys.OLD_GROWTH_BIRCH_FOREST, BiomeKeys.DARK_FOREST), SpawnGroup.MONSTER,
                        NEKKER, 5, 4, 6);
            }

            //Cyclops
            {
                SpawnRestriction.register(CYCLOPS, SpawnLocationTypes.ON_GROUND,
                        Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, CyclopsEntity::canCyclopsSpawn);

                //In snowy plains/mountains/taigas
                BiomeModifications.addSpawn(BiomeSelectors.includeByKey(
                                //Snowy Plains
                                BiomeKeys.SNOWY_PLAINS, BiomeKeys.STONY_SHORE,
                                //Is_Hill
                                BiomeKeys.WINDSWEPT_HILLS,BiomeKeys.WINDSWEPT_FOREST,BiomeKeys.WINDSWEPT_GRAVELLY_HILLS,
                                //Is_Mountain
                                BiomeKeys.MEADOW,BiomeKeys.FROZEN_PEAKS,BiomeKeys.JAGGED_PEAKS,BiomeKeys.STONY_PEAKS,BiomeKeys.SNOWY_SLOPES,
                                //Is_Taiga
                                BiomeKeys.OLD_GROWTH_PINE_TAIGA,BiomeKeys.OLD_GROWTH_SPRUCE_TAIGA), SpawnGroup.MONSTER,
                        CYCLOPS, 15, 1, 1);
            }

            //Rock Troll
            {
                SpawnRestriction.register(ROCK_TROLL, SpawnLocationTypes.ON_GROUND,
                        Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, RockTrollEntity::canSpawnInDarkNotBelowDeepslate);

                //In mountains/taigas
                BiomeModifications.addSpawn(BiomeSelectors.includeByKey(
                                //Mountains
                                BiomeKeys.STONY_PEAKS, BiomeKeys.MEADOW, BiomeKeys.WINDSWEPT_HILLS, BiomeKeys.WINDSWEPT_GRAVELLY_HILLS, BiomeKeys.WINDSWEPT_FOREST,
                                //Taiga
                                BiomeKeys.TAIGA, BiomeKeys.OLD_GROWTH_SPRUCE_TAIGA, BiomeKeys.OLD_GROWTH_PINE_TAIGA,
                                //Caves/Shore
                                BiomeKeys.DRIPSTONE_CAVES, BiomeKeys.STONY_SHORE), SpawnGroup.MONSTER,
                        ROCK_TROLL, 5, 1, 1);
            }

            //Ice Troll
            {
                SpawnRestriction.register(ICE_TROLL, SpawnLocationTypes.ON_GROUND,
                        Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, IceTrollEntity::canSpawnInDarkNotBelowDeepslate);
                //In snowy plains/mountains/taigas
                BiomeModifications.addSpawn(BiomeSelectors.includeByKey(
                                //Mountains
                                BiomeKeys.JAGGED_PEAKS, BiomeKeys.SNOWY_SLOPES, BiomeKeys.GROVE,
                                //Snowy Plains
                                BiomeKeys.SNOWY_PLAINS, BiomeKeys.ICE_SPIKES), SpawnGroup.MONSTER,
                        ICE_TROLL, 2, 1, 1);
            }


            //Forest Troll
            {
                SpawnRestriction.register(FOREST_TROLL, SpawnLocationTypes.ON_GROUND,
                        Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, ForestTrollEntity::canSpawnInDarkNotBelowDeepslate);
                //In forests
                BiomeModifications.addSpawn(BiomeSelectors.includeByKey(
                                //Forest
                                BiomeKeys.FOREST, BiomeKeys.FLOWER_FOREST, BiomeKeys.BIRCH_FOREST, BiomeKeys.OLD_GROWTH_BIRCH_FOREST,
                                //Dark Forest
                                BiomeKeys.DARK_FOREST), SpawnGroup.MONSTER,
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

    public static TagKey<EntityType<?>> createTag(String name){
        return TagKey.of(RegistryKeys.ENTITY_TYPE, Identifier.of(TCOTS_Main.MOD_ID,name));
    }
}
