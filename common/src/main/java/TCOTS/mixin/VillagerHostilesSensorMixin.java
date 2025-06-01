package TCOTS.mixin;

import TCOTS.registry.TCOTS_Entities;
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
                        .put(TCOTS_Entities.Drowner(), distanceDanger)
                        .put(TCOTS_Entities.Rotfiend(), distanceDanger)
                        .put(TCOTS_Entities.Foglet(), distanceDanger)
                        .put(TCOTS_Entities.WaterHag(), distanceDanger)
                        .put(TCOTS_Entities.GraveHag(), distanceDanger)
                        .put(TCOTS_Entities.Ghoul(), distanceDanger)
                        .put(TCOTS_Entities.Alghoul(), distanceDanger)
                        .put(TCOTS_Entities.Scurver(), distanceMediumDanger)
                        .put(TCOTS_Entities.Devourer(), distanceMediumDanger)
                        .put(TCOTS_Entities.Graveir(), distanceMediumDanger)
                        .put(TCOTS_Entities.Bullvore(), distanceExtremeDanger)


                        //Ogroids
                        .put(TCOTS_Entities.Nekker(), distanceDanger)
                        .put(TCOTS_Entities.NekkerWarrior(), distanceMediumDanger)
                        .put(TCOTS_Entities.Cyclops(), distanceMediumDanger)
                        .put(TCOTS_Entities.IceTroll(), distanceMediumDanger)
                        .put(TCOTS_Entities.IceGiant(), distanceExtremeDanger)

                        .build();
    }

}
