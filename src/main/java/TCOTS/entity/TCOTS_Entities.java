package TCOTS.entity;

import TCOTS.TCOTS_Main;
import TCOTS.entity.misc.*;
import TCOTS.entity.misc.bolts.*;
import TCOTS.entity.necrophages.*;
import TCOTS.entity.ogroids.*;
import TCOTS.world.TCOTS_Features;
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


    public static final TagKey<EntityType<?>> IGNITING_ENTITIES = TagKey.of(RegistryKeys.ENTITY_TYPE, new Identifier(TCOTS_Main.MOD_ID,"igniting_entities"));
    public static final TagKey<EntityType<?>> DIMERITIUM_REMOVAL = TagKey.of(RegistryKeys.ENTITY_TYPE, new Identifier(TCOTS_Main.MOD_ID,"dimeritium_removal"));
    public static final TagKey<EntityType<?>> DIMERITIUM_DAMAGE = TagKey.of(RegistryKeys.ENTITY_TYPE, new Identifier(TCOTS_Main.MOD_ID,"dimeritium_damage"));
    public static final TagKey<EntityType<?>> BOSS_TAG = TagKey.of(RegistryKeys.ENTITY_TYPE, new Identifier("c", "bosses"));


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

    public static final EntityType<BullvoreEntity> BULLVORE = Registry.register(
            Registries.ENTITY_TYPE, new Identifier(TCOTS_Main.MOD_ID, "bullvore"),
            FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, BullvoreEntity::new)
                    //Hitbox
                    .dimensions(EntityDimensions.changing(1.9975f, 3.3f)).build());

    //Ogroids
    public static final EntityType<NekkerEntity> NEKKER = Registry.register(
            Registries.ENTITY_TYPE, new Identifier(TCOTS_Main.MOD_ID, "nekker"),
            FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, NekkerEntity::new)
                    //Hitbox
                    .dimensions(EntityDimensions.changing(0.65f, 0.975f)).build());

    public static final EntityType<NekkerWarriorEntity> NEKKER_WARRIOR = Registry.register(
            Registries.ENTITY_TYPE, new Identifier(TCOTS_Main.MOD_ID, "nekker_warrior"),
            FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, NekkerWarriorEntity::new)
                    //Hitbox
                    .dimensions(EntityDimensions.changing(0.7f, 1.3f)).build());

    public static final EntityType<CyclopsEntity> CYCLOPS = Registry.register(
            Registries.ENTITY_TYPE, new Identifier(TCOTS_Main.MOD_ID, "cyclops"),
            FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, CyclopsEntity::new)
                    //Hitbox
                    .dimensions(EntityDimensions.changing(1.9975f, 5.0f)).build());

    public static final EntityType<RockTrollEntity> ROCK_TROLL = Registry.register(
            Registries.ENTITY_TYPE, new Identifier(TCOTS_Main.MOD_ID, "rock_troll"),
            FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, RockTrollEntity::new)
                    //Hitbox
                    .dimensions(EntityDimensions.changing(1.8f, 2.7f)).build());
    public static final EntityType<Troll_RockProjectileEntity> TROLL_ROCK_PROJECTILE = Registry.register(
            Registries.ENTITY_TYPE, new Identifier(TCOTS_Main.MOD_ID, "troll_projectile"),
            FabricEntityTypeBuilder.<Troll_RockProjectileEntity>create(SpawnGroup.MISC, Troll_RockProjectileEntity::new
                    )
                    // Hitbox
                    .dimensions(EntityDimensions.changing(0.5f, 0.5f)).build());

    public static final EntityType<IceTrollEntity> ICE_TROLL = Registry.register(
            Registries.ENTITY_TYPE, new Identifier(TCOTS_Main.MOD_ID, "ice_troll"),
            FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, IceTrollEntity::new)
                    //Hitbox
                    .dimensions(EntityDimensions.changing(1.8f, 2.7f)).build());

    public static final EntityType<ForestTrollEntity> FOREST_TROLL = Registry.register(
            Registries.ENTITY_TYPE, new Identifier(TCOTS_Main.MOD_ID, "forest_troll"),
            FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, ForestTrollEntity::new)
                    //Hitbox
                    .dimensions(EntityDimensions.changing(1.8f, 2.8f)).build());


    public static final EntityType<IceGiantEntity> ICE_GIANT = Registry.register(
            Registries.ENTITY_TYPE, new Identifier(TCOTS_Main.MOD_ID, "ice_giant"),
            FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, IceGiantEntity::new)
                    //Hitbox
                    .dimensions(EntityDimensions.changing(1.9975f, 4.4f)).build());
    public static final EntityType<AnchorProjectileEntity> ANCHOR_PROJECTILE =
            Registry.register(Registries.ENTITY_TYPE, new Identifier(TCOTS_Main.MOD_ID, "anchor_projectile"),
                    FabricEntityTypeBuilder.<AnchorProjectileEntity>create(SpawnGroup.MISC, AnchorProjectileEntity::new)
                            .dimensions(EntityDimensions.fixed(1.25f, 1.8f))
                            .trackRangeBlocks(8)
                            .trackedUpdateRate(20)
                            .fireImmune().build());

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

    public static void registerSpawnPlacements() {
        //Necrophages
        {
            //Drowners
            SpawnRestriction.register(TCOTS_Entities.DROWNER, SpawnRestriction.Location.NO_RESTRICTIONS,
                    Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, DrownerEntity::canSpawnDrowner);


            //Rotfiends
            SpawnRestriction.register(TCOTS_Entities.ROTFIEND, SpawnRestriction.Location.ON_GROUND,
                    Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, RotfiendEntity::canSpawnInDarkW);


            //Foglets
            SpawnRestriction.register(TCOTS_Entities.FOGLET, SpawnRestriction.Location.ON_GROUND,
                    Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, FogletEntity::canSpawnInDark_NotCaves);


            //Water Hags
            SpawnRestriction.register(TCOTS_Entities.WATER_HAG, SpawnRestriction.Location.ON_GROUND,
                    Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, DrownerEntity::canSpawnDrowner);


            //Grave Hags
            SpawnRestriction.register(TCOTS_Entities.GRAVE_HAG, SpawnRestriction.Location.ON_GROUND,
                    Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, GraveHagEntity::canSpawnInDarkNotBelowDeepslate);


            //Ghouls & Alghouls
            SpawnRestriction.register(TCOTS_Entities.GHOUL, SpawnRestriction.Location.ON_GROUND,
                    Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, GhoulEntity::canSpawnGhoul);


            //Scurvers
            SpawnRestriction.register(TCOTS_Entities.SCURVER, SpawnRestriction.Location.ON_GROUND,
                    Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, ScurverEntity::canSpawnInDarkW);


            //Devourer
            SpawnRestriction.register(TCOTS_Entities.DEVOURER, SpawnRestriction.Location.ON_GROUND,
                    Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, DevourerEntity::canSpawnInDarkW);


            //Graveir
            SpawnRestriction.register(TCOTS_Entities.GRAVEIR, SpawnRestriction.Location.ON_GROUND,
                    Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, GraveirEntity::canSpawnGraveir);


            //Bullvore
            SpawnRestriction.register(TCOTS_Entities.BULLVORE, SpawnRestriction.Location.ON_GROUND,
                    Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, BullvoreEntity::canSpawnInDarkW);

        }

        //Ogroids
        {
            //Nekkers
            SpawnRestriction.register(TCOTS_Entities.NEKKER, SpawnRestriction.Location.ON_GROUND,
                    Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, NekkerEntity::canSpawnNekker);


            //Cyclops
            SpawnRestriction.register(TCOTS_Entities.CYCLOPS, SpawnRestriction.Location.ON_GROUND,
                    Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, CyclopsEntity::canCyclopsSpawn);


            //Rock Troll
            SpawnRestriction.register(TCOTS_Entities.ROCK_TROLL, SpawnRestriction.Location.ON_GROUND,
                    Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, RockTrollEntity::canSpawnInDarkNotBelowDeepslate);


            //Ice Troll
            SpawnRestriction.register(TCOTS_Entities.ICE_TROLL, SpawnRestriction.Location.ON_GROUND,
                    Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, IceTrollEntity::canSpawnInDarkNotBelowDeepslate);



            //Forest Troll
            SpawnRestriction.register(TCOTS_Entities.FOREST_TROLL, SpawnRestriction.Location.ON_GROUND,
                    Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, ForestTrollEntity::canSpawnInDarkNotBelowDeepslate);

        }
    }

    public static void registerBiomeModificationSpawn(){
        //Necrophages
        {
            //Drowners
            {
                //In swamps
                BiomeModifications.addSpawn(BiomeSelectors.tag(TCOTS_Features.DROWNER_SWAMP), SpawnGroup.MONSTER,
                        TCOTS_Entities.DROWNER, 130, 3, 5);

                //In beaches
                BiomeModifications.addSpawn(BiomeSelectors.tag(TCOTS_Features.DROWNER_BEACH), SpawnGroup.MONSTER,
                        TCOTS_Entities.DROWNER, 50, 2, 4);

                //Swimming in oceans/rivers
                BiomeModifications.addSpawn(BiomeSelectors.tag(TCOTS_Features.DROWNER_WATER), SpawnGroup.MONSTER,
                        TCOTS_Entities.DROWNER, 8, 2, 3);
            }

            //Rotfiends
            {
                //In night
                BiomeModifications.addSpawn(BiomeSelectors.tag(TCOTS_Features.ROTFIEND), SpawnGroup.MONSTER,
                        TCOTS_Entities.ROTFIEND, 80, 4, 6);
            }

            //Foglets
            {
                //In swamps/rivers
                BiomeModifications.addSpawn(BiomeSelectors.tag(TCOTS_Features.FOGLET_SWAMP), SpawnGroup.MONSTER,
                        TCOTS_Entities.FOGLET, 80, 1, 3);

                //In dark forests
                BiomeModifications.addSpawn(BiomeSelectors.tag(TCOTS_Features.FOGLET_DARK), SpawnGroup.MONSTER,
                        TCOTS_Entities.FOGLET, 120, 1, 2);

                //In forests/mountains
                BiomeModifications.addSpawn(BiomeSelectors.tag(TCOTS_Features.FOGLET_HILLS_FORESTS), SpawnGroup.MONSTER,
                        TCOTS_Entities.FOGLET, 50, 1, 2);
            }

            //Water Hags
            {
                //In swamps
                BiomeModifications.addSpawn(BiomeSelectors.tag(TCOTS_Features.WATER_HAG_SWAMP), SpawnGroup.MONSTER,
                        TCOTS_Entities.WATER_HAG, 80, 1, 2);

                //In rivers
                BiomeModifications.addSpawn(BiomeSelectors.tag(TCOTS_Features.WATER_HAG_RIVER), SpawnGroup.MONSTER,
                        TCOTS_Entities.WATER_HAG, 20, 1, 2);
            }

            //Grave Hags
            {
                //In night
                BiomeModifications.addSpawn(BiomeSelectors.tag(TCOTS_Features.GRAVE_HAG), SpawnGroup.MONSTER,
                        TCOTS_Entities.GRAVE_HAG, 80, 1, 2);
            }

            //Ghouls & Alghouls
            {
                //In night
                BiomeModifications.addSpawn(BiomeSelectors.tag(TCOTS_Features.GHOUL), SpawnGroup.MONSTER,
                        TCOTS_Entities.GHOUL, 10, 3, 5);
            }

            //Scurvers
            {
                //In night
                BiomeModifications.addSpawn(BiomeSelectors.tag(TCOTS_Features.SCURVER), SpawnGroup.MONSTER,
                        TCOTS_Entities.SCURVER, 40, 2, 3);
            }

            //Devourer
            {
                //In night
                BiomeModifications.addSpawn(BiomeSelectors.tag(TCOTS_Features.DEVOURER), SpawnGroup.MONSTER,
                        TCOTS_Entities.DEVOURER, 60, 3, 4);
            }

            //Graveir
            {
                //In Caves
                BiomeModifications.addSpawn(BiomeSelectors.tag(TCOTS_Features.GRAVEIR), SpawnGroup.MONSTER,
                        TCOTS_Entities.GRAVEIR, 60, 1, 2);
            }
        }

        //Ogroids
        {
            //Nekkers
            {
                //In night
                BiomeModifications.addSpawn(BiomeSelectors.tag(TCOTS_Features.NEKKER), SpawnGroup.MONSTER,
                        TCOTS_Entities.NEKKER, 5, 4, 6);
            }

            //Cyclops
            {
                //In snowy plains/mountains/taigas
                BiomeModifications.addSpawn(BiomeSelectors.tag(TCOTS_Features.CYCLOPS), SpawnGroup.MONSTER,
                        TCOTS_Entities.CYCLOPS, 15, 1, 1);
            }

            //Rock Troll
            {
                //In mountains/taigas
                BiomeModifications.addSpawn(BiomeSelectors.tag(TCOTS_Features.ROCK_TROLL), SpawnGroup.MONSTER,
                        TCOTS_Entities.ROCK_TROLL, 5, 1, 1);
            }

            //Ice Troll
            {
                //In snowy plains/mountains/taigas
                BiomeModifications.addSpawn(BiomeSelectors.tag(TCOTS_Features.ICE_TROLL), SpawnGroup.MONSTER,
                        TCOTS_Entities.ICE_TROLL, 2, 1, 1);
            }


            //Forest Troll
            {
                //In forests
                BiomeModifications.addSpawn(BiomeSelectors.tag(TCOTS_Features.FOREST_TROLL), SpawnGroup.MONSTER,
                        TCOTS_Entities.FOREST_TROLL, 5, 1, 1);
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
