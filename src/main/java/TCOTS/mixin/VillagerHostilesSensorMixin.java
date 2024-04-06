package TCOTS.mixin;

import TCOTS.entity.TCOTS_Entities;
import com.google.common.collect.ImmutableMap;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.brain.sensor.VillagerHostilesSensor;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@SuppressWarnings({"deprecation", "unused"})
@Mixin(VillagerHostilesSensor.class)
public class VillagerHostilesSensorMixin {
    @Unique
    private static final float distanceDanger=12.0f;

    @Shadow @Final private static ImmutableMap<Object, Object> SQUARED_DISTANCES_FOR_DANGER =
            ImmutableMap.builder()
                    .put(EntityType.DROWNED, 8.0f)
                    .put(EntityType.EVOKER, 12.0f)
                    .put(EntityType.HUSK, 8.0f)
                    .put(EntityType.ILLUSIONER, 12.0f)
                    .put(EntityType.PILLAGER, 15.0f)
                    .put(EntityType.RAVAGER, 12.0f)
                    .put(EntityType.VEX, 8.0f)
                    .put(EntityType.VINDICATOR, 10.0f)
                    .put(EntityType.ZOGLIN, 10.0f)
                    .put(EntityType.ZOMBIE, 8.0f)
                    .put(EntityType.ZOMBIE_VILLAGER, 8.0f)


                    .put(TCOTS_Entities.DROWNER, distanceDanger)
                    .put(TCOTS_Entities.ROTFIEND, distanceDanger)
                    .put(TCOTS_Entities.FOGLET, distanceDanger)
                    .put(TCOTS_Entities.WATER_HAG, distanceDanger)
                    .put(TCOTS_Entities.GRAVE_HAG, distanceDanger)


                    .put(TCOTS_Entities.NEKKER, distanceDanger)
                    .build();

    @Unique
    private ImmutableMap<EntityType<?>, Float> insertWitcherMobs(ImmutableMap<EntityType<?>, Float> immutableMap){
        //Necrophages
        immutableMap.put(TCOTS_Entities.DROWNER, distanceDanger);
        immutableMap.put(TCOTS_Entities.ROTFIEND, distanceDanger);
        immutableMap.put(TCOTS_Entities.FOGLET, distanceDanger);
        immutableMap.put(TCOTS_Entities.FOGLING, distanceDanger);
        immutableMap.put(TCOTS_Entities.WATER_HAG, distanceDanger);
        immutableMap.put(TCOTS_Entities.GRAVE_HAG, distanceDanger);

        //Ogroids
        immutableMap.put(TCOTS_Entities.NEKKER, distanceDanger);

        return immutableMap;
    }


//    @Redirect(method = "isHostile", at = @At(value = "FIELD",
//            target = "Lnet/minecraft/entity/ai/brain/sensor/VillagerHostilesSensor;SQUARED_DISTANCES_FOR_DANGER:Lcom/google/common/collect/ImmutableMap;"
//
////            ,opcode = Opcodes.PUTFIELD
//    ))
//    private ImmutableMap<EntityType<?>, Float> injectedRedirectHostile() {
//        return insertWitcherMobs(SQUARED_DISTANCES_FOR_DANGER);
//    }
//
//    @Redirect(method = "isCloseEnoughForDanger", at = @At(value = "FIELD",
//            target = "Lnet/minecraft/entity/ai/brain/sensor/VillagerHostilesSensor;SQUARED_DISTANCES_FOR_DANGER:Lcom/google/common/collect/ImmutableMap;"
//
////            ,opcode = Opcodes.PUTFIELD
//    ))
//    private ImmutableMap<EntityType<?>, Float> injectedRedirectDistance() {
//        return insertWitcherMobs(SQUARED_DISTANCES_FOR_DANGER);
//    }
//
//    @Redirect(method = "isHostile",
//            at = @At(value = "FIELD",
//            target = "Lnet/minecraft/entity/ai/brain/sensor/VillagerHostilesSensor;SQUARED_DISTANCES_FOR_DANGER:Lcom/google/common/collect/ImmutableMap;"
//
//            ,opcode = Opcodes.PUTFIELD
//    ))
//    private void a() {
//
//    }

}
