package TCOTS.utils;

import TCOTS.TCOTS_Main;
import TCOTS.entity.TCOTS_Entities;
import TCOTS.items.TCOTS_Items;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.decoration.AbstractDecorationEntity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.mob.AbstractPiglinEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.passive.GolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.VehicleEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.Registries;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.MathHelper;

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

    /**
     Util method to push and damage enemies, disable player shield and destroy End Crystals, Vehicles and Item Frames. None class has immunity
     */
    public static void pushAndDamageEntities(MobEntity mob, float damage, double lateralExpansion, double yExpansion, double knockbackStrength){

        List<Entity> listMobs= mob.getWorld().getEntitiesByClass(Entity.class, mob.getBoundingBox().expand(lateralExpansion,yExpansion,lateralExpansion),
                entity -> entity != mob
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

    /**
    Gets an ItemStack from an ItemEntity
    @param stack The ItemEntity
     */
    public static ItemStack getItemFromStack(ItemEntity stack) {
        ItemStack itemStack = stack.getStack();
        ItemStack itemStack2 = itemStack.split(1);
        if (itemStack.isEmpty()) {
            stack.discard();
        } else {
            stack.setStack(itemStack);
        }
        return itemStack2;
    }

    /**
    Adds particles when running
     */
    public static void spawnGroundParticles(PathAwareEntity entity) {
        BlockState blockState = entity.getSteppingBlockState();
        if (blockState.getRenderType() != BlockRenderType.INVISIBLE) {
            for (int i = 0; i < 8; ++i) {
                double d = entity.getX() + (double) MathHelper.nextBetween(entity.getRandom(), -0.7F, 0.7F);
                double e = entity.getY();
                double f = entity.getZ() + (double) MathHelper.nextBetween(entity.getRandom(), -0.7F, 0.7F);

                entity.getWorld().addParticle(new BlockStateParticleEffect(ParticleTypes.BLOCK, blockState), d, e, f, 0.0, 0.0, 0.0);
            }
        }
    }

    /**
     Checks if the player it's wearing the full Manticore Armor
     */
    public static boolean isWearingManticoreArmor(LivingEntity player){
        return player.getEquippedStack(EquipmentSlot.CHEST).isOf(TCOTS_Items.MANTICORE_ARMOR)
                && player.getEquippedStack(EquipmentSlot.LEGS).isOf(TCOTS_Items.MANTICORE_TROUSERS)
                && player.getEquippedStack(EquipmentSlot.FEET).isOf(TCOTS_Items.MANTICORE_BOOTS);
    }

    /**
     Checks if the player it's wearing the full Warrior's Leather Armor
     */
    public static boolean isWearingWarriorsLeatherArmor(LivingEntity player){
        return player.getEquippedStack(EquipmentSlot.CHEST).isOf(TCOTS_Items.WARRIORS_LEATHER_JACKET)
                && player.getEquippedStack(EquipmentSlot.LEGS).isOf(TCOTS_Items.WARRIORS_LEATHER_TROUSERS)
                && player.getEquippedStack(EquipmentSlot.FEET).isOf(TCOTS_Items.WARRIORS_LEATHER_BOOTS);
    }


    /**
     Checks if the player it's wearing the full Raven's Leather Armor
     */
    public static boolean isWearingRavensArmor(LivingEntity player){
        return player.getEquippedStack(EquipmentSlot.CHEST).isOf(TCOTS_Items.RAVENS_ARMOR)
                && player.getEquippedStack(EquipmentSlot.LEGS).isOf(TCOTS_Items.RAVENS_TROUSERS)
                && player.getEquippedStack(EquipmentSlot.FEET).isOf(TCOTS_Items.RAVENS_BOOTS);
    }

    /**
    Checks if the entity it's a magical monster
     @param livingTarget The entity to check
     */

    public static boolean isMonster(LivingEntity livingTarget){
        return
                //Necrophage
                (livingTarget.getGroup() == TCOTS_Entities.NECROPHAGES || livingTarget.getGroup() == EntityGroup.UNDEAD || TCOTS_Main.CONFIG.monsters.Necrophages().contains(Registries.ENTITY_TYPE.getId(livingTarget.getType()).toString())) ||
                //Ogroid
                (livingTarget.getGroup() == TCOTS_Entities.OGROIDS || livingTarget instanceof AbstractPiglinEntity || TCOTS_Main.CONFIG.monsters.Ogroids().contains(Registries.ENTITY_TYPE.getId(livingTarget.getType()).toString())) ||
                //Specter
                (livingTarget.getGroup() == TCOTS_Entities.SPECTERS) ||
                //Vampires
                (livingTarget.getGroup() == TCOTS_Entities.VAMPIRES) ||
                //Insectoid
                (livingTarget.getGroup() == TCOTS_Entities.INSECTOIDS || livingTarget.getGroup() == EntityGroup.ARTHROPOD) ||
                //Elementa
                (livingTarget.getGroup() == TCOTS_Entities.ELEMENTA || livingTarget instanceof GolemEntity) ||
                //Cursed One
                (livingTarget.getGroup() == TCOTS_Entities.CURSED_ONES) ||
                //Hybrid
                (livingTarget.getGroup() == TCOTS_Entities.HYBRIDS) ||
                //Draconid
                (livingTarget.getGroup() == TCOTS_Entities.DRACONIDS) ||
                //Relict
                (livingTarget.getGroup() == TCOTS_Entities.CURSED_ONES)
                ;
    }

}

