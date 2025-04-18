package TCOTS.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import net.minecraft.world.entity.decoration.HangingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.VehicleEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class EntitiesUtil {
    /**
    Util method to push and damage enemies, disable player shield and destroy End Crystals, Vehicles and Item Frames
     */
    public static void pushAndDamageEntities(Mob pusherEntity, float damage, double lateralExpansion, double yExpansion, double knockbackStrength, Class<?>... classException){
        pushAndDamageEntities(pusherEntity, damage, lateralExpansion, yExpansion, knockbackStrength, pusherEntity.damageSources().mobAttack(pusherEntity), classException);
    }

    /**
     Util method to push and damage enemies, disable player shield and destroy End Crystals, Vehicles and Item Frames, for entities that aren't mobs
     */
    public static void pushAndDamageEntities(Entity pusherEntity, float damage, double lateralExpansion, double yExpansion, double knockbackStrength, DamageSource damageSource, Class<?>... classException){
        List<Entity> listMobs= pusherEntity.level().getEntitiesOfClass(Entity.class, pusherEntity.getBoundingBox().inflate(lateralExpansion,yExpansion,lateralExpansion),
                entity -> {
                    for (Class<?> class_ : classException) {
                        if (class_.isAssignableFrom(entity.getClass()))
                            return false;
                    }
                    return entity != pusherEntity;
                }
        );


        for (Entity entity : listMobs){
            double d = pusherEntity.getX() - entity.getX();
            double e = pusherEntity.getZ() - entity.getZ();
            if(entity instanceof LivingEntity livingEntity) {

                livingEntity.knockback(knockbackStrength, d, e);
                //Push the player
                if (entity instanceof ServerPlayer && !((ServerPlayer) entity).isCreative()) {
                    ((ServerPlayer) entity).connection.send(new ClientboundSetEntityMotionPacket(entity), null);
                }
                //Removes the shield
                if (livingEntity.isBlocking() && entity instanceof Player) {
                    ((Player) entity).disableShield();
                }
                //Checks if the entity it's blocking, to block the damage
                else if (!livingEntity.isBlocking()) {
                    entity.hurt(damageSource, damage);
                }

                //Destroys other no-living entities
            } else if(entity instanceof VehicleEntity || entity instanceof EndCrystal || entity instanceof HangingEntity) {
                entity.hurt(damageSource, 50.0f);
            } else {
                return;
            }
        }
    }

    public static void spawnImpactParticles(Entity entity, double radius, double fallingDistance){
        spawnImpactParticles(entity, radius, fallingDistance, Math.max(80 + fallingDistance, 2 * Math.PI * radius));
    }

    /**
    Spawns particles with the texture of the block where it impacts, in a circle shape
     */
    public static void spawnImpactParticles(Entity entity, double radius, double fallingDistance, double pQuantity) {

        // To get the ground position
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos(entity.getOnPos().getX(), entity.getOnPos().getY(), entity.getOnPos().getZ());
        while (entity.level().getBlockState(pos).isAir() && pos.getY()>-64) {
            pos.setY(pos.getY() - 1);
        }

        pos.setY(pos.getY() + 1);

        if(entity.isInWater()) pos.set(entity.getOnPos());

        // Fill the circle with particles
        double stepSize = radius / 10.0;  // Adjust the step size for more or fewer particles inside the circle
        for (double r = 0; r <= radius; r += stepSize) {
            double particlesInRing = Math.max(pQuantity, 2 * Math.PI * r);
            for (int i = 0; i < particlesInRing; i++) {
                double angle = (2 * Math.PI) * i / particlesInRing;
                double offsetX = r * Math.cos(angle);
                double offsetZ = r * Math.sin(angle);

                // Add some vertical randomness for particle height
                double d = entity.level().getRandom().nextGaussian() * 0.5;
                double e = entity.level().getRandom().nextGaussian() * 0.5;
                double f = entity.level().getRandom().nextGaussian() * 0.5;



                // Use a block particle for the interior
                BlockState blockState = entity.level().getBlockState(
                        new BlockPos(
                                (int) (entity.getX()+offsetX),
                                pos.below().getY(),
                                (int) (entity.getZ()+offsetZ)));
                if(blockState.getRenderShape() != RenderShape.INVISIBLE) {
                    // Select the particle type
                    ParticleOptions particleType = new BlockParticleOption(ParticleTypes.BLOCK, blockState);

                    // Spawn the particle at the calculated position
                    entity.level().addParticle(particleType,
                            entity.getX() + offsetX,
                            pos.getY(),
                            entity.getZ() + offsetZ,
                            d, e, f);
                }



            }
        }
    }

    /**
    Gets an ItemStack from an ItemEntity
    @param stack The ItemEntity
     */
    public static ItemStack getItemFromStack(ItemEntity stack) {
        ItemStack itemStack = stack.getItem();
        ItemStack itemStack2 = itemStack.split(1);
        if (itemStack.isEmpty()) {
            stack.discard();
        } else {
            stack.setItem(itemStack);
        }
        return itemStack2;
    }

    /**
    Adds particles when running
     */
    public static void spawnGroundParticles(PathfinderMob entity) {
        BlockState blockState = entity.getBlockStateOn();
        if (blockState.getRenderShape() != RenderShape.INVISIBLE) {
            for (int i = 0; i < 8; ++i) {
                double d = entity.getX() + (double) Mth.randomBetween(entity.getRandom(), -0.7F, 0.7F);
                double e = entity.getY();
                double f = entity.getZ() + (double) Mth.randomBetween(entity.getRandom(), -0.7F, 0.7F);

                entity.level().addParticle(new BlockParticleOption(ParticleTypes.BLOCK, blockState), d, e, f, 0.0, 0.0, 0.0);
            }
        }
    }

    /**
    Damages the equipment of an entity
     @param entity The entity that uses the armor
     @param source The DamageSource
     @param amount The amount of damage
     @param slots The slots to damage
     */
    public static void damageEquipment(LivingEntity entity,DamageSource source, float amount, EquipmentSlot... slots) {
        if (!(amount <= 0.0F)) {
            int i = (int)Math.max(1.0F, amount / 4.0F);

            for (EquipmentSlot equipmentSlot : slots) {
                ItemStack itemStack = entity.getItemBySlot(equipmentSlot);
                if (itemStack.getItem() instanceof ArmorItem && itemStack.canBeHurtBy(source)) {
                    itemStack.hurtAndBreak(i, entity, equipmentSlot);
                }
            }
        }
    }
}
