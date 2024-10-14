package TCOTS.mixin;

import TCOTS.entity.TCOTS_Entities;
import com.google.common.collect.ImmutableMap;
import net.minecraft.entity.ai.brain.sensor.VillagerHostilesSensor;
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
    private static ImmutableMap<Object, Object> SQUARED_DISTANCES_FOR_DANGER;

    static {
                SQUARED_DISTANCES_FOR_DANGER = ImmutableMap.builder()
                .putAll(SQUARED_DISTANCES_FOR_DANGER.entrySet())

                        //Necrophages
                        .put(TCOTS_Entities.DROWNER, distanceDanger)
                        .put(TCOTS_Entities.ROTFIEND, distanceDanger)
                        .put(TCOTS_Entities.FOGLET, distanceDanger)
                        .put(TCOTS_Entities.WATER_HAG, distanceDanger)
                        .put(TCOTS_Entities.GRAVE_HAG, distanceDanger)
                        .put(TCOTS_Entities.GHOUL, distanceDanger)
                        .put(TCOTS_Entities.ALGHOUL, distanceDanger)
                        .put(TCOTS_Entities.SCURVER, distanceMediumDanger)
                        .put(TCOTS_Entities.DEVOURER, distanceMediumDanger)
                        .put(TCOTS_Entities.GRAVEIR, distanceMediumDanger)
                        .put(TCOTS_Entities.BULLVORE, distanceExtremeDanger)


                        //Ogroids
                        .put(TCOTS_Entities.NEKKER, distanceDanger)
                        .put(TCOTS_Entities.NEKKER_WARRIOR, distanceMediumDanger)
                        .put(TCOTS_Entities.CYCLOPS, distanceMediumDanger)
                        .put(TCOTS_Entities.ICE_TROLL, distanceMediumDanger)
                        .put(TCOTS_Entities.ICE_GIANT, distanceExtremeDanger)

                        .build();
    }

}
