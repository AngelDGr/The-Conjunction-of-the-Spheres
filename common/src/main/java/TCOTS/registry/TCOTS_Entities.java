package TCOTS.registry;

import TCOTS.TCOTS_Main;
import TCOTS.TCOTS_Registries;
import TCOTS.entity.misc.DrownerPuddleEntity;
import TCOTS.entity.misc.FoglingEntity;
import TCOTS.entity.misc.ScurverSpineEntity;
import TCOTS.entity.misc.WaterHag_MudBallEntity;
import TCOTS.entity.monsters.necrophages.*;
import TCOTS.entity.monsters.ogroids.*;
import TCOTS.entity.misc.*;
import TCOTS.entity.misc.bolts.*;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.storage.loot.LootTable;

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
    //TODO: Bloedzuiger
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
    public static final RegistrySupplier<EntityType<DrownerEntity>> DROWNER = TCOTS_Registries.ENTITY_TYPES.register(
            "drowner",
            ()-> EntityType.Builder.of(DrownerEntity::new, MobCategory.MONSTER)
                    //Hitbox
                    .sized(0.78f, 1.9f).build("drowner"));
    public static final RegistrySupplier<EntityType<DrownerPuddleEntity>> DROWNER_PUDDLE = TCOTS_Registries.ENTITY_TYPES.register(
            "drowner_puddle",
            () -> EntityType.Builder.<DrownerPuddleEntity>of(DrownerPuddleEntity::new, MobCategory.MISC)
                    .fireImmune()
                    .sized(0.78f, 0.1f).build("drowner_puddle"));

    public static final RegistrySupplier<EntityType<RotfiendEntity>> ROTFIEND = TCOTS_Registries.ENTITY_TYPES.register(
            "rotfiend",
            () -> EntityType.Builder.of(RotfiendEntity::new, MobCategory.MONSTER)
                    .sized(0.78f, 1.9f).build("rotfiend"));

    public static final RegistrySupplier<EntityType<GraveHagEntity>> GRAVE_HAG = TCOTS_Registries.ENTITY_TYPES.register(
            "grave_hag",
            () -> EntityType.Builder.of(GraveHagEntity::new, MobCategory.MONSTER)
                    .sized(0.78f, 1.9f).build("grave_hag"));

    public static final RegistrySupplier<EntityType<WaterHagEntity>> WATER_HAG = TCOTS_Registries.ENTITY_TYPES.register(
            "water_hag",
            () -> EntityType.Builder.of(WaterHagEntity::new, MobCategory.MONSTER)
                    .sized(0.78f, 1.9f).build("water_hag"));

    public static final RegistrySupplier<EntityType<WaterHag_MudBallEntity>> WATER_HAG_MUD_BALL = TCOTS_Registries.ENTITY_TYPES.register(
            "water_hag_mud_ball",
            () -> EntityType.Builder.<WaterHag_MudBallEntity>of(WaterHag_MudBallEntity::new, MobCategory.MISC)
                    .sized(0.25f, 0.25f).build("water_hag_mud_ball"));

    public static final RegistrySupplier<EntityType<FogletEntity>> FOGLET = TCOTS_Registries.ENTITY_TYPES.register(
            "foglet",
            () -> EntityType.Builder.of(FogletEntity::new, MobCategory.MONSTER)
                    .sized(0.78f, 1.9f).build("foglet"));

    public static final RegistrySupplier<EntityType<FoglingEntity>> FOGLING = TCOTS_Registries.ENTITY_TYPES.register(
            "fogling",
            () -> EntityType.Builder.of(FoglingEntity::new, MobCategory.MONSTER)
                    .sized(0.78f, 1.9f).build("fogling"));

    public static final RegistrySupplier<EntityType<GhoulEntity>> GHOUL = TCOTS_Registries.ENTITY_TYPES.register(
            "ghoul",
            () -> EntityType.Builder.of(GhoulEntity::new, MobCategory.MONSTER)
                    .sized(1.4f, 0.9f)
                    .eyeHeight(0.62f)
                    .build("ghoul"));

    public static final RegistrySupplier<EntityType<AlghoulEntity>> ALGHOUL = TCOTS_Registries.ENTITY_TYPES.register(
            "alghoul",
            () -> EntityType.Builder.of(AlghoulEntity::new, MobCategory.MONSTER)
                    .sized(1.8f, 1.2f)
                    .eyeHeight(0.62f)
                    .build("alghoul"));

    public static final RegistrySupplier<EntityType<ScurverEntity>> SCURVER = TCOTS_Registries.ENTITY_TYPES.register(
            "scurver",
            () -> EntityType.Builder.of(ScurverEntity::new, MobCategory.MONSTER)
                    .sized(0.78f, 1.9f).build("scurver"));

    public static final RegistrySupplier<EntityType<ScurverSpineEntity>> SCURVER_SPINE = TCOTS_Registries.ENTITY_TYPES.register(
            "scurver_spike",
            () -> EntityType.Builder.<ScurverSpineEntity>of(ScurverSpineEntity::new, MobCategory.MISC)
                    .sized(0.25f, 0.25f)
                    .clientTrackingRange(4).updateInterval(20).build("scurver_spike"));

    public static final RegistrySupplier<EntityType<DevourerEntity>> DEVOURER = TCOTS_Registries.ENTITY_TYPES.register(
            "devourer",
            () -> EntityType.Builder.of(DevourerEntity::new, MobCategory.MONSTER)
                    .sized(0.85f, 1.8f).build("devourer"));

    public static final RegistrySupplier<EntityType<BloedzuigerEntity>> BLOEDZUIGER = TCOTS_Registries.ENTITY_TYPES.register(
            "bloedzuiger",
            () -> EntityType.Builder.of(BloedzuigerEntity::new, MobCategory.MONSTER)
                    .sized(1.0f, 2.0f).build("bloedzuiger"));

    public static final RegistrySupplier<EntityType<GraveirEntity>> GRAVEIR = TCOTS_Registries.ENTITY_TYPES.register(
            "graveir",
            () -> EntityType.Builder.of(GraveirEntity::new, MobCategory.MONSTER)
                    .sized(1.4f, 2.65f).build("graveir"));

    public static final RegistrySupplier<EntityType<BullvoreEntity>> BULLVORE = TCOTS_Registries.ENTITY_TYPES.register(
            "bullvore",
            () -> EntityType.Builder.of(BullvoreEntity::new, MobCategory.MONSTER)
                    .sized(1.9975f, 3.3f).build("bullvore"));

    public static final RegistrySupplier<EntityType<NekkerEntity>> NEKKER = TCOTS_Registries.ENTITY_TYPES.register(
            "nekker",
            () -> EntityType.Builder.of(NekkerEntity::new, MobCategory.MONSTER)
                    .sized(0.65f, 0.975f).build("nekker"));

    public static final RegistrySupplier<EntityType<NekkerWarriorEntity>> NEKKER_WARRIOR = TCOTS_Registries.ENTITY_TYPES.register(
            "nekker_warrior",
            () -> EntityType.Builder.of(NekkerWarriorEntity::new, MobCategory.MONSTER)
                    .sized(0.7f, 1.3f).build("nekker_warrior"));

    public static final RegistrySupplier<EntityType<CyclopsEntity>> CYCLOPS = TCOTS_Registries.ENTITY_TYPES.register(
            "cyclops",
            () -> EntityType.Builder.of(CyclopsEntity::new, MobCategory.MONSTER)
                    .sized(1.9975f, 5.0f).build("cyclops"));

    public static final RegistrySupplier<EntityType<RockTrollEntity>> ROCK_TROLL = TCOTS_Registries.ENTITY_TYPES.register(
            "rock_troll",
            () -> EntityType.Builder.of(RockTrollEntity::new, MobCategory.MONSTER)
                    .sized(1.8f, 2.7f).build("rock_troll"));

    public static final RegistrySupplier<EntityType<Troll_RockProjectileEntity>> TROLL_ROCK_PROJECTILE = TCOTS_Registries.ENTITY_TYPES.register(
            "troll_projectile",
            () -> EntityType.Builder.<Troll_RockProjectileEntity>of(Troll_RockProjectileEntity::new, MobCategory.MISC)
                    .sized(0.5f, 0.5f).build("troll_projectile"));

    public static final RegistrySupplier<EntityType<IceTrollEntity>> ICE_TROLL = TCOTS_Registries.ENTITY_TYPES.register(
            "ice_troll",
            () -> EntityType.Builder.of(IceTrollEntity::new, MobCategory.MONSTER)
                    .sized(1.8f, 2.7f).build("ice_troll"));

    public static final RegistrySupplier<EntityType<ForestTrollEntity>> FOREST_TROLL = TCOTS_Registries.ENTITY_TYPES.register(
            "forest_troll",
            () -> EntityType.Builder.of(ForestTrollEntity::new, MobCategory.MONSTER)
                    .sized(1.8f, 2.8f).build("forest_troll"));

    public static final RegistrySupplier<EntityType<IceGiantEntity>> ICE_GIANT = TCOTS_Registries.ENTITY_TYPES.register(
            "ice_giant",
            () -> EntityType.Builder.of(IceGiantEntity::new, MobCategory.MONSTER)
                    .sized(1.9975f, 4.4f).build("ice_giant"));

    public static final RegistrySupplier<EntityType<AnchorProjectileEntity>> ANCHOR_PROJECTILE = TCOTS_Registries.ENTITY_TYPES.register(
            "anchor_projectile",
            () -> EntityType.Builder.<AnchorProjectileEntity>of(AnchorProjectileEntity::new, MobCategory.MISC)
                    .sized(1.25f, 1.8f)
                    .eyeHeight(0.13F)
                    .clientTrackingRange(8)
                    .updateInterval(20)
                    .fireImmune()
                    .build("anchor_projectile"));

    public static final RegistrySupplier<EntityType<WitcherBombEntity>> WITCHER_BOMB = TCOTS_Registries.ENTITY_TYPES.register(
            "witcher_bomb",
            () -> EntityType.Builder.<WitcherBombEntity>of(WitcherBombEntity::new, MobCategory.MISC)
                    .sized(0.25f, 0.25f).build("witcher_bomb"));

    public static final RegistrySupplier<EntityType<AreaEffectCloud>> AREA_EFFECT_CLOUD = TCOTS_Registries.ENTITY_TYPES.register(
                    "dragons_dream_cloud",
                    () -> EntityType.Builder.<AreaEffectCloud>of(DragonsDreamCloud::new, MobCategory.MISC)
                            .fireImmune()
                            .sized(6.0f, 1.5f)
                            .build("dragons_dream_cloud")
            );

    public static final RegistrySupplier<EntityType<BaseBoltProjectile>> BASE_BOLT = TCOTS_Registries.ENTITY_TYPES.register(
                    "base_bolt",
                    () -> EntityType.Builder.<BaseBoltProjectile>of(BaseBoltProjectile::new, MobCategory.MISC)
                            .sized(0.5f, 0.5f)
                            .clientTrackingRange(4)
                            .updateInterval(20)
                            .build("base_bolt")
            );

    public static final RegistrySupplier<EntityType<BluntBoltProjectile>> BLUNT_BOLT =
            TCOTS_Registries.ENTITY_TYPES.register(
                    "blunt_bolt",
                    () -> EntityType.Builder.<BluntBoltProjectile>of(BluntBoltProjectile::new, MobCategory.MISC)
                            .sized(0.5f, 0.5f)
                            .clientTrackingRange(4)
                            .updateInterval(20)
                            .build("blunt_bolt")
            );

    public static final RegistrySupplier<EntityType<PrecisionBoltProjectile>> PRECISION_BOLT =
            TCOTS_Registries.ENTITY_TYPES.register(
                    "precision_bolt",
                    () -> EntityType.Builder.<PrecisionBoltProjectile>of(PrecisionBoltProjectile::new, MobCategory.MISC)
                            .sized(0.5f, 0.5f)
                            .clientTrackingRange(4)
                            .updateInterval(20)
                            .build("precision_bolt")
            );

    public static final RegistrySupplier<EntityType<ExplodingBoltProjectile>> EXPLODING_BOLT =
            TCOTS_Registries.ENTITY_TYPES.register(
                    "exploding_bolt",
                    () -> EntityType.Builder.<ExplodingBoltProjectile>of(ExplodingBoltProjectile::new, MobCategory.MISC)
                            .sized(0.5f, 0.5f)
                            .clientTrackingRange(4)
                            .updateInterval(20)
                            .build("exploding_bolt")
            );

    public static final RegistrySupplier<EntityType<BroadheadBoltProjectile>> BROADHEAD_BOLT =
            TCOTS_Registries.ENTITY_TYPES.register(
                    "broadhead_bolt",
                    () -> EntityType.Builder.<BroadheadBoltProjectile>of(BroadheadBoltProjectile::new, MobCategory.MISC)
                            .sized(0.5f, 0.5f)
                            .clientTrackingRange(4)
                            .updateInterval(20)
                            .build("broadhead_bolt")
            );


    public static void initEntities(){}

    // Necrophages
    public static EntityType<DrownerEntity> Drowner() { return DROWNER.get(); }
    public static EntityType<DrownerPuddleEntity> DrownerPuddle() { return DROWNER_PUDDLE.get(); }
    public static EntityType<RotfiendEntity> Rotfiend() { return ROTFIEND.get(); }
    public static EntityType<GraveHagEntity> GraveHag() { return GRAVE_HAG.get(); }
    public static EntityType<WaterHagEntity> WaterHag() { return WATER_HAG.get(); }
    public static EntityType<WaterHag_MudBallEntity> WaterHagMudBall() { return WATER_HAG_MUD_BALL.get(); }
    public static EntityType<FogletEntity> Foglet() { return FOGLET.get(); }
    public static EntityType<FoglingEntity> Fogling() { return FOGLING.get(); }
    public static EntityType<GhoulEntity> Ghoul() { return GHOUL.get(); }
    public static EntityType<AlghoulEntity> Alghoul() { return ALGHOUL.get(); }
    public static EntityType<ScurverEntity> Scurver() { return SCURVER.get(); }
    public static EntityType<ScurverSpineEntity> ScurverSpine() { return SCURVER_SPINE.get(); }
    public static EntityType<DevourerEntity> Devourer() { return DEVOURER.get(); }
    public static EntityType<BloedzuigerEntity> Bloedzuiger() { return BLOEDZUIGER.get(); }
    public static EntityType<GraveirEntity> Graveir() { return GRAVEIR.get(); }
    public static EntityType<BullvoreEntity> Bullvore() { return BULLVORE.get(); }

    // Ogroids
    public static EntityType<NekkerEntity> Nekker() { return NEKKER.get(); }
    public static EntityType<NekkerWarriorEntity> NekkerWarrior() { return NEKKER_WARRIOR.get(); }
    public static EntityType<CyclopsEntity> Cyclops() { return CYCLOPS.get(); }
    public static EntityType<RockTrollEntity> RockTroll() { return ROCK_TROLL.get(); }
    public static EntityType<Troll_RockProjectileEntity> TrollRockProjectile() { return TROLL_ROCK_PROJECTILE.get(); }
    public static EntityType<IceTrollEntity> IceTroll() { return ICE_TROLL.get(); }
    public static EntityType<ForestTrollEntity> ForestTroll() { return FOREST_TROLL.get(); }
    public static EntityType<IceGiantEntity> IceGiant() { return ICE_GIANT.get(); }
    public static EntityType<AnchorProjectileEntity> AnchorProjectile() { return ANCHOR_PROJECTILE.get(); }

    // Misc
    public static EntityType<WitcherBombEntity> WitcherBomb() { return WITCHER_BOMB.get(); }
    public static EntityType<AreaEffectCloud> AreaEffectCloud() { return AREA_EFFECT_CLOUD.get(); }
    public static EntityType<BaseBoltProjectile> BaseBolt() { return BASE_BOLT.get(); }
    public static EntityType<BluntBoltProjectile> BluntBolt() { return BLUNT_BOLT.get(); }
    public static EntityType<PrecisionBoltProjectile> PrecisionBolt() { return PRECISION_BOLT.get(); }
    public static EntityType<ExplodingBoltProjectile> ExplodingBolt() { return EXPLODING_BOLT.get(); }
    public static EntityType<BroadheadBoltProjectile> BroadheadBolt() { return BROADHEAD_BOLT.get(); }

    public static ResourceKey<LootTable> FOREST_TROLL_BARTERING = ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID,"gameplay/forest_troll_bartering"));
    public static ResourceKey<LootTable> ROCK_TROLL_BARTERING = ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID,"gameplay/rock_troll_bartering"));
    public static ResourceKey<LootTable> ICE_TROLL_BARTERING = ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID,"gameplay/ice_troll_bartering"));

}
