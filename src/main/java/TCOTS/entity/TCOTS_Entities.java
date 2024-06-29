package TCOTS.entity;

import TCOTS.TCOTS_Main;
import TCOTS.entity.misc.*;
import TCOTS.entity.misc.bolts.*;
import TCOTS.entity.necrophages.*;
import TCOTS.entity.ogroids.NekkerEntity;
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

//    0.2.0- Necrophages & Ogroids
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
    //TODO: Bullvore
    //W1
    //xTODO: Devourer
    //TODO: Graveir


//  Ogroids
    //W3
    //xTODO: Nekkers
    //TODO: Nekker Warriors
    //TODO: Cyclopses
    //TODO: Rock troll
    //TODO: Ice troll
    //TODO: Ice Giant (Boss)
    //W2
    //TODO: Troll (Forest)


    public static final TagKey<EntityType<?>> IGNITING_ENTITIES = TagKey.of(RegistryKeys.ENTITY_TYPE, new Identifier(TCOTS_Main.MOD_ID,"igniting_entities"));
    public static final TagKey<EntityType<?>> DIMERITIUM_REMOVAL = TagKey.of(RegistryKeys.ENTITY_TYPE, new Identifier(TCOTS_Main.MOD_ID,"dimeritium_removal"));
    public static final TagKey<EntityType<?>> DIMERITIUM_DAMAGE = TagKey.of(RegistryKeys.ENTITY_TYPE, new Identifier(TCOTS_Main.MOD_ID,"dimeritium_damage"));

    public static final WitcherGroup NECROPHAGES = new WitcherGroup("necrophages",0);
    public static final WitcherGroup OGROIDS = new WitcherGroup("ogroids",1);
    public static final WitcherGroup SPECTERS = new WitcherGroup("specters",2);
    public static final WitcherGroup VAMPIRES = new WitcherGroup("vampires",3);
    public static final WitcherGroup INSECTOIDS = new WitcherGroup("insectoids",4);
    public static final WitcherGroup BEASTS = new WitcherGroup("beasts",5);
    public static final WitcherGroup ELEMENTA = new WitcherGroup("elementa",6);
    public static final WitcherGroup CURSED_ONES = new WitcherGroup("cursed_ones",7);
    public static final WitcherGroup HYBRIDS = new WitcherGroup("hybrids",8);
    public static final WitcherGroup DRACONIDS = new WitcherGroup("draconids",9);
    public static final WitcherGroup RELICTS = new WitcherGroup("relicts",10);

    //Necrophages
    public static final EntityType<DrownerEntity> DROWNER = Registry.register(
            Registries.ENTITY_TYPE, new Identifier(TCOTS_Main.MOD_ID, "drowner"),
            FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, DrownerEntity::new)
                    //Hitbox
                    .dimensions(EntityDimensions.changing(0.78f, 1.9f)).build());
    public static final EntityType<DrownerPuddleEntity> DROWNER_PUDDLE = Registry.register(
            Registries.ENTITY_TYPE, new Identifier(TCOTS_Main.MOD_ID, "drowner_puddle"),
            FabricEntityTypeBuilder.<DrownerPuddleEntity>create(SpawnGroup.MISC, DrownerPuddleEntity::new
                    ).fireImmune()
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


    public static final EntityType<GhoulEntity> GHOUL = Registry.register(
            Registries.ENTITY_TYPE, new Identifier(TCOTS_Main.MOD_ID, "ghoul"),
            FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, GhoulEntity::new)
                    //Hitbox
                    .dimensions(EntityDimensions.changing(1.4f, 0.9f)).build());

    public static final EntityType<AlghoulEntity> ALGHOUL = Registry.register(
            Registries.ENTITY_TYPE, new Identifier(TCOTS_Main.MOD_ID, "alghoul"),
            FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, AlghoulEntity::new)
                    //Hitbox
                    .dimensions(EntityDimensions.changing(1.8f, 1.2f)).build());

    public static final EntityType<ScurverEntity> SCURVER = Registry.register(
            Registries.ENTITY_TYPE, new Identifier(TCOTS_Main.MOD_ID, "scurver"),
            FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, ScurverEntity::new)
                    //Hitbox
                    .dimensions(EntityDimensions.changing(0.78f, 1.9f)).build());
    public static final EntityType<ScurverSpineEntity> SCURVER_SPINE =
            Registry.register(Registries.ENTITY_TYPE, new Identifier(TCOTS_Main.MOD_ID, "scurver_spike"),
                    FabricEntityTypeBuilder.<ScurverSpineEntity>create(SpawnGroup.MISC, ScurverSpineEntity::new)
                            .dimensions(EntityDimensions.fixed(0.25f, 0.25f))
                            .trackRangeBlocks(4).trackedUpdateRate(20).build());


    public static final EntityType<DevourerEntity> DEVOURER = Registry.register(
            Registries.ENTITY_TYPE, new Identifier(TCOTS_Main.MOD_ID, "devourer"),
            FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, DevourerEntity::new)
                    //Hitbox
                    .dimensions(EntityDimensions.changing(0.85f, 1.8f)).build());

    public static final EntityType<GraveirEntity> GRAVEIR = Registry.register(
            Registries.ENTITY_TYPE, new Identifier(TCOTS_Main.MOD_ID, "graveir"),
            FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, GraveirEntity::new)
                    //Hitbox
                    .dimensions(EntityDimensions.changing(1.4f, 2.65f)).build());

    //Ogroids
    public static final EntityType<NekkerEntity> NEKKER = Registry.register(
            Registries.ENTITY_TYPE, new Identifier(TCOTS_Main.MOD_ID, "nekker"),
            FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, NekkerEntity::new)
                    //Hitbox
                    .dimensions(EntityDimensions.changing(0.65f, 0.9f)).build());

    //Misc
    public static final EntityType<WitcherBombEntity> WITCHER_BOMB = Registry.register(
            Registries.ENTITY_TYPE, new Identifier(TCOTS_Main.MOD_ID, "witcher_bomb"),
            FabricEntityTypeBuilder.<WitcherBombEntity>create(SpawnGroup.MISC, WitcherBombEntity::new)
                    // Hitbox
                    .dimensions(EntityDimensions.changing(0.25f, 0.25f)).build());

    public static final EntityType<AreaEffectCloudEntity> AREA_EFFECT_CLOUD = Registry.register(
            Registries.ENTITY_TYPE, new Identifier(TCOTS_Main.MOD_ID, "dragons_dream_cloud"),
            FabricEntityTypeBuilder.<AreaEffectCloudEntity>create(SpawnGroup.MISC, DragonsDreamCloud::new).fireImmune()
                    .dimensions(EntityDimensions.changing(6.0f, 1.5f)).build()
            );

    public static final EntityType<BaseBoltProjectile> BASE_BOLT =
            Registry.register(Registries.ENTITY_TYPE, new Identifier(TCOTS_Main.MOD_ID, "base_bolt"),
                    FabricEntityTypeBuilder.<BaseBoltProjectile>create(SpawnGroup.MISC, BaseBoltProjectile::new)
                            .dimensions(EntityDimensions.fixed(0.5f, 0.5f))
                            .trackRangeBlocks(4).trackedUpdateRate(20).build());

    public static final EntityType<BluntBoltProjectile> BLUNT_BOLT =
            Registry.register(Registries.ENTITY_TYPE, new Identifier(TCOTS_Main.MOD_ID, "blunt_bolt"),
                    FabricEntityTypeBuilder.<BluntBoltProjectile>create(SpawnGroup.MISC, BluntBoltProjectile::new)
                            .dimensions(EntityDimensions.fixed(0.5f, 0.5f))
                            .trackRangeBlocks(4).trackedUpdateRate(20).build());

    public static final EntityType<PrecisionBoltProjectile> PRECISION_BOLT =
            Registry.register(Registries.ENTITY_TYPE, new Identifier(TCOTS_Main.MOD_ID, "precision_bolt"),
                    FabricEntityTypeBuilder.<PrecisionBoltProjectile>create(SpawnGroup.MISC, PrecisionBoltProjectile::new)
                            .dimensions(EntityDimensions.fixed(0.5f, 0.5f))
                            .trackRangeBlocks(4).trackedUpdateRate(20).build());

    public static final EntityType<ExplodingBoltProjectile> EXPLODING_BOLT =
            Registry.register(Registries.ENTITY_TYPE, new Identifier(TCOTS_Main.MOD_ID, "exploding_bolt"),
                    FabricEntityTypeBuilder.<ExplodingBoltProjectile>create(SpawnGroup.MISC, ExplodingBoltProjectile::new)
                            .dimensions(EntityDimensions.fixed(0.5f, 0.5f))
                            .trackRangeBlocks(4).trackedUpdateRate(20).build());

    public static final EntityType<BroadheadBoltProjectile> BROADHEAD_BOLT =
            Registry.register(Registries.ENTITY_TYPE, new Identifier(TCOTS_Main.MOD_ID, "broadhead_bolt"),
                    FabricEntityTypeBuilder.<BroadheadBoltProjectile>create(SpawnGroup.MISC, BroadheadBoltProjectile::new)
                            .dimensions(EntityDimensions.fixed(0.5f, 0.5f))
                            .trackRangeBlocks(4).trackedUpdateRate(20).build());

    public static void addSpawns() {
        //Necrophages
        {
            //Drowners
            {
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
                BiomeModifications.addSpawn(BiomeSelectors.includeByKey(BiomeKeys.OCEAN, BiomeKeys.DEEP_OCEAN,
                                BiomeKeys.LUKEWARM_OCEAN, BiomeKeys.DEEP_LUKEWARM_OCEAN,
                                BiomeKeys.COLD_OCEAN, BiomeKeys.DEEP_COLD_OCEAN,
                                BiomeKeys.RIVER), SpawnGroup.MONSTER,
                        DROWNER, 10, 2, 3);
            }

            //Rotfiends
            {
                SpawnRestriction.register(ROTFIEND, SpawnRestriction.Location.ON_GROUND,
                        Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, NecrophageMonster::canSpawnInDark);

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
                SpawnRestriction.register(FOGLET, SpawnRestriction.Location.ON_GROUND,
                        Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, NecrophageMonster::canSpawnInDark_NotCaves);

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
                SpawnRestriction.register(WATER_HAG, SpawnRestriction.Location.ON_GROUND,
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
                SpawnRestriction.register(GRAVE_HAG, SpawnRestriction.Location.ON_GROUND,
                        Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, NecrophageMonster::canSpawnInDarkNotBelowDeepslate);

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
                SpawnRestriction.register(GHOUL, SpawnRestriction.Location.ON_GROUND,
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
                SpawnRestriction.register(SCURVER, SpawnRestriction.Location.ON_GROUND,
                        Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, NecrophageMonster::canSpawnInDark);

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
                SpawnRestriction.register(DEVOURER, SpawnRestriction.Location.ON_GROUND,
                        Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, NecrophageMonster::canSpawnInDark);

                //In night
                BiomeModifications.addSpawn(BiomeSelectors.includeByKey(
                                BiomeKeys.BIRCH_FOREST, BiomeKeys.FOREST,
                                BiomeKeys.SWAMP, BiomeKeys.RIVER,
                                BiomeKeys.PLAINS), SpawnGroup.MONSTER,
                        DEVOURER, 60, 3, 4);
            }

            //Graveir
            {
                SpawnRestriction.register(GRAVEIR, SpawnRestriction.Location.ON_GROUND,
                        Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, GraveirEntity::canSpawnGraveir);

                //In Caves
                BiomeModifications.addSpawn(
                        BiomeSelectors.foundInOverworld()
                                .and(BiomeSelectors.excludeByKey(BiomeKeys.DEEP_DARK, BiomeKeys.LUSH_CAVES, BiomeKeys.MUSHROOM_FIELDS)
                                ), SpawnGroup.MONSTER,
                        GRAVEIR, 60, 1, 2);
            }
        }

        //Ogroids
        {
            {
                //Nekkers
                SpawnRestriction.register(NEKKER, SpawnRestriction.Location.ON_GROUND,
                        Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, NekkerEntity::canSpawnNekker);

                //In night
                BiomeModifications.addSpawn(BiomeSelectors.includeByKey(
                                BiomeKeys.SAVANNA, BiomeKeys.PLAINS,
                                BiomeKeys.JUNGLE, BiomeKeys.SPARSE_JUNGLE,
                                BiomeKeys.TAIGA, BiomeKeys.OLD_GROWTH_PINE_TAIGA, BiomeKeys.OLD_GROWTH_SPRUCE_TAIGA,
                                BiomeKeys.FOREST, BiomeKeys.FLOWER_FOREST, BiomeKeys.BIRCH_FOREST, BiomeKeys.OLD_GROWTH_BIRCH_FOREST, BiomeKeys.DARK_FOREST), SpawnGroup.MONSTER,
                        NEKKER, 5, 4, 6);
            }
        }
    }

    public static void addAttributes(){
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

        //Nekker
        FabricDefaultAttributeRegistry.register(TCOTS_Entities.NEKKER, NekkerEntity.setAttributes());
    }
}
