package TCOTS.entity;

import TCOTS.TCOTS_Main;
import TCOTS.entity.misc.DrownerPuddleEntity;
import TCOTS.entity.necrophages.DrownerEntity;
import TCOTS.entity.necrophages.Necrophage_Base;
import TCOTS.entity.necrophages.RotfiendEntity;
import TCOTS.entity.ogroids.NekkerEntity;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.tag.BiomeTags;
import net.minecraft.util.Identifier;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.BiomeKeys;

public class TCOTS_Entities {

//    0.2.0- Necrophages & Ogroids
// Necrophages
    //xTODO: Drowner
    //xTODO: Rotfiend
    //TODO: Water Hag
    //TODO: Grave Hag
    //TODO: Foglet
    //TODO: Ghoul
    //TODO: Wights
    //TODO: Scurvers
    //TODO: Devourer?
    //TODO: Graveir?

//  Ogroids
    //xTODO: Nekkers
    //TODO: Cyclopses
    //TODO: Ice Giant
    //TODO: Rock troll
    //TODO: Ice troll

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
                    ROTFIEND, 80, 3, 3);


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
