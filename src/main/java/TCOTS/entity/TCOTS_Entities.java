package TCOTS.entity;

import TCOTS.TCOTS_Main;
import TCOTS.entity.misc.DrownerPuddleEntity;
import TCOTS.entity.necrophages.DrownerEntity;
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
    //TODO: Rotfiend
    //TODO: Water Hag
    //TODO: Grave Hag
    //TODO: Foglet
    //TODO: Ghoul
    //TODO: Wights
    //TODO: Scurvers
    //TODO: Devourer?
    //TODO: Graveir?

//  Ogroids
    //TODO: Nekkers
    //TODO: Cyclopses
    //TODO: Ice Giant
    //TODO: Rock troll
    //TODO: Ice troll

    public static final EntityGroup NECROPHAGES = new EntityGroup();

    public static final EntityGroup OGROIDS = new EntityGroup();

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




    public static void addSpawns() {
        //Drowners in swamps
        BiomeModifications.addSpawn(BiomeSelectors.includeByKey(BiomeKeys.SWAMP,
                                                                BiomeKeys.MANGROVE_SWAMP), SpawnGroup.MONSTER,
                DROWNER, 130, 3, 5);

        //Drowners in beaches
        BiomeModifications.addSpawn(BiomeSelectors.includeByKey(BiomeKeys.BEACH), SpawnGroup.MONSTER,
                DROWNER, 50, 2, 4);

        //Drowners swimming in oceans
        BiomeModifications.addSpawn(BiomeSelectors.includeByKey(BiomeKeys.OCEAN,BiomeKeys.DEEP_OCEAN,
                                                                BiomeKeys.LUKEWARM_OCEAN,BiomeKeys.DEEP_LUKEWARM_OCEAN,
                                                                BiomeKeys.COLD_OCEAN,BiomeKeys.DEEP_COLD_OCEAN,
                                                                BiomeKeys.RIVER), SpawnGroup.MONSTER,
                DROWNER, 20, 2, 3);

        SpawnRestriction.register(DROWNER, SpawnRestriction.Location.NO_RESTRICTIONS,
                Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, DrownerEntity::canSpawnDrowner);




    }
}
