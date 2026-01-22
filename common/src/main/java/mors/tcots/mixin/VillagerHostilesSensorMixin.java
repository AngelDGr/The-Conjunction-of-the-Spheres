package mors.tcots.mixin;

import mors.tcots.registry.TCOTS_Entities;
import com.google.common.collect.ImmutableMap;
import net.minecraft.world.entity.ai.sensing.VillagerHostilesSensor;
import org.spongepowered.asm.mixin.*;

@Mixin(VillagerHostilesSensor.class)
public class VillagerHostilesSensorMixin {
    @Unique
    private static final float tcots$distanceDanger = 12.0f;
    @Unique
    private static final float tcots$distanceMediumDanger = 16.0f;
    @Unique
    private static final float tcots$distanceExtremeDanger = 24.0f;


    @Shadow @Final @Mutable
    private static ImmutableMap<Object, Object> ACCEPTABLE_DISTANCE_FROM_HOSTILES;

    static {
                ACCEPTABLE_DISTANCE_FROM_HOSTILES = ImmutableMap.builder()
                .putAll(ACCEPTABLE_DISTANCE_FROM_HOSTILES.entrySet())

                        //Necrophages
                        .put(TCOTS_Entities.Drowner(), tcots$distanceDanger)
                        .put(TCOTS_Entities.Rotfiend(), tcots$distanceDanger)
                        .put(TCOTS_Entities.Foglet(), tcots$distanceDanger)
                        .put(TCOTS_Entities.WaterHag(), tcots$distanceDanger)
                        .put(TCOTS_Entities.GraveHag(), tcots$distanceDanger)
                        .put(TCOTS_Entities.Ghoul(), tcots$distanceDanger)
                        .put(TCOTS_Entities.Alghoul(), tcots$distanceDanger)
                        .put(TCOTS_Entities.Scurver(), tcots$distanceMediumDanger)
                        .put(TCOTS_Entities.Devourer(), tcots$distanceMediumDanger)
                        .put(TCOTS_Entities.Bloedzuiger(), tcots$distanceMediumDanger)
                        .put(TCOTS_Entities.Graveir(), tcots$distanceMediumDanger)
                        .put(TCOTS_Entities.Bullvore(), tcots$distanceExtremeDanger)

                        //Ogroids
                        .put(TCOTS_Entities.Nekker(), tcots$distanceDanger)
                        .put(TCOTS_Entities.NekkerWarrior(), tcots$distanceMediumDanger)
                        .put(TCOTS_Entities.Cyclops(), tcots$distanceMediumDanger)
                        .put(TCOTS_Entities.IceTroll(), tcots$distanceMediumDanger)
                        .put(TCOTS_Entities.IceGiant(), tcots$distanceExtremeDanger)

                        .build();
    }

}
