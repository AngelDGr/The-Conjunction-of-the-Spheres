package TCOTS.utils;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.AbstractDecorationEntity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.VehicleEntity;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;

import java.util.List;

public class EntitiesUtil {

    /**
    Util method to push and damage enemies, disable player shield and destroy End Crystals, Vehicles and Item Frames
     */
    public static void pushAndDamageEntities(MobEntity mob, float damage, double lateralExpansion, double yExpansion, double knockbackStrength, Class<?>... classException){

        List<Entity> listMobs= mob.getWorld().getEntitiesByClass(Entity.class, mob.getBoundingBox().expand(lateralExpansion,yExpansion,lateralExpansion),
                entity -> {
                    for (Class<?> class_ : classException) {
                        if (class_.isAssignableFrom(entity.getClass()))
                            return false;
                    }
                    return true;
                }
        );

        for (Entity entity : listMobs){
            double d = mob.getX() - entity.getX();
            double e = mob.getZ() - entity.getZ();
            if(entity instanceof LivingEntity livingEntity) {
                livingEntity.takeKnockback(knockbackStrength, d, e);
                //Push the player
                if (entity instanceof ServerPlayerEntity && !((ServerPlayerEntity) entity).isCreative()) {
                    ((ServerPlayerEntity) entity).networkHandler.send(new EntityVelocityUpdateS2CPacket(entity), null);
                }
                //Removes the shield
                if (livingEntity.isBlocking() && entity instanceof PlayerEntity) {
                    ((PlayerEntity) entity).disableShield(true);
                }
                //Checks if the entity it's blocking, to block the damage
                else if (!livingEntity.isBlocking()) {
                    entity.damage(mob.getDamageSources().mobAttack(mob), damage);
                }

                //Destroys other no-living entities
            } else if(entity instanceof VehicleEntity || entity instanceof EndCrystalEntity || entity instanceof AbstractDecorationEntity) {
                entity.damage(mob.getDamageSources().mobAttack(mob), 50.0f);
            } else {
                return;
            }
        }
    }

}

