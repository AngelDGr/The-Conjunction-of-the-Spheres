package TCOTS.mixin;

import TCOTS.entity.TCOTS_Entities_Fabric;
import com.google.common.collect.ImmutableMap;
import net.minecraft.world.entity.ai.sensing.VillagerHostilesSensor;
import org.spongepowered.asm.mixin.*;

@Mixin(VillagerHostilesSensor.class)
public class VillagerHostilesSensorMixin {
    @Unique
    private static final float distanceDanger = 12.0f;
    @Unique
    private static final float distanceMediumDanger = 16.0f;
    @Unique
    private static final float distanceExtremeDanger = 24.0f;


    @Shadow @Final @Mutable
    private static ImmutableMap<Object, Object> ACCEPTABLE_DISTANCE_FROM_HOSTILES;

    static {
                ACCEPTABLE_DISTANCE_FROM_HOSTILES = ImmutableMap.builder()
                .putAll(ACCEPTABLE_DISTANCE_FROM_HOSTILES.entrySet())

                        //Necrophages
                        .put(TCOTS_Entities_Fabric.DROWNER, distanceDanger)
                        .put(TCOTS_Entities_Fabric.ROTFIEND, distanceDanger)
                        .put(TCOTS_Entities_Fabric.FOGLET, distanceDanger)
                        .put(TCOTS_Entities_Fabric.WATER_HAG, distanceDanger)
                        .put(TCOTS_Entities_Fabric.GRAVE_HAG, distanceDanger)
                        .put(TCOTS_Entities_Fabric.GHOUL, distanceDanger)
                        .put(TCOTS_Entities_Fabric.ALGHOUL, distanceDanger)
                        .put(TCOTS_Entities_Fabric.SCURVER, distanceMediumDanger)
                        .put(TCOTS_Entities_Fabric.DEVOURER, distanceMediumDanger)
                        .put(TCOTS_Entities_Fabric.GRAVEIR, distanceMediumDanger)
                        .put(TCOTS_Entities_Fabric.BULLVORE, distanceExtremeDanger)


                        //Ogroids
                        .put(TCOTS_Entities_Fabric.NEKKER, distanceDanger)
                        .put(TCOTS_Entities_Fabric.NEKKER_WARRIOR, distanceMediumDanger)
                        .put(TCOTS_Entities_Fabric.CYCLOPS, distanceMediumDanger)
                        .put(TCOTS_Entities_Fabric.ICE_TROLL, distanceMediumDanger)
                        .put(TCOTS_Entities_Fabric.ICE_GIANT, distanceExtremeDanger)

                        .build();
    }

}
