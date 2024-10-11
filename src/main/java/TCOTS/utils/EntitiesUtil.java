package TCOTS.utils;

import TCOTS.TCOTS_Main;
import TCOTS.entity.TCOTS_Entities;
import TCOTS.items.TCOTS_Items;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.decoration.AbstractDecorationEntity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.passive.AllayEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.GolemEntity;
import net.minecraft.entity.passive.MerchantEntity;
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
     Checks if the player it's wearing the full Raven's Armor
     */
    public static boolean isWearingRavensArmor(LivingEntity player){
        return player.getEquippedStack(EquipmentSlot.CHEST).isOf(TCOTS_Items.RAVENS_ARMOR)
                && player.getEquippedStack(EquipmentSlot.LEGS).isOf(TCOTS_Items.RAVENS_TROUSERS)
                && player.getEquippedStack(EquipmentSlot.FEET).isOf(TCOTS_Items.RAVENS_BOOTS);
    }

    /**
    Checks if the entity it's a magical monster
     @param entity The entity to check
     */

    public static boolean isMonster(LivingEntity entity){
        return
                isNecrophage(entity) ||
                isOgroid(entity)||
                isSpecter(entity) ||
                isVampire(entity) ||
                isInsectoid(entity) ||
                isElementa(entity) ||
                isCursedOne(entity) ||
                isHybrid(entity) ||
                isDraconid(entity) ||
                isRelict(entity);
    }

    public static boolean isNecrophage(LivingEntity entity){
        return entity.getGroup() == TCOTS_Entities.NECROPHAGES ||
                entity.getGroup() == EntityGroup.UNDEAD ||
                TCOTS_Main.CONFIG.monsters.Necrophages().contains(Registries.ENTITY_TYPE.getId(entity.getType()).toString());
    }

    public static boolean isOgroid(LivingEntity entity){
        return entity.getGroup() == TCOTS_Entities.OGROIDS ||
                entity instanceof AbstractPiglinEntity ||
                TCOTS_Main.CONFIG.monsters.Ogroids().contains(Registries.ENTITY_TYPE.getId(entity.getType()).toString());
    }

    public static boolean isSpecter(LivingEntity entity){
        return entity.getGroup() == TCOTS_Entities.SPECTERS ||
                entity instanceof GhastEntity ||
                TCOTS_Main.CONFIG.monsters.Specters().contains(Registries.ENTITY_TYPE.getId(entity.getType()).toString());
    }

    public static boolean isVampire(LivingEntity entity){
        return entity.getGroup() == TCOTS_Entities.VAMPIRES ||
                TCOTS_Main.CONFIG.monsters.Vampires().contains(Registries.ENTITY_TYPE.getId(entity.getType()).toString());
    }

    public static boolean isInsectoid(LivingEntity entity){
        return entity.getGroup() == TCOTS_Entities.INSECTOIDS ||
                entity.getGroup() == EntityGroup.ARTHROPOD ||
                TCOTS_Main.CONFIG.monsters.Insectoids().contains(Registries.ENTITY_TYPE.getId(entity.getType()).toString());
    }

    public static boolean isBeast(LivingEntity entity){
        return entity.getGroup() == TCOTS_Entities.BEASTS ||
                entity instanceof AnimalEntity ||
                TCOTS_Main.CONFIG.monsters.Beasts().contains(Registries.ENTITY_TYPE.getId(entity.getType()).toString());
    }

    public static boolean isElementa(LivingEntity entity){
        return entity.getGroup() == TCOTS_Entities.ELEMENTA ||
                entity instanceof AllayEntity  ||
                entity instanceof GolemEntity  ||
                entity instanceof BlazeEntity  ||
                entity instanceof BreezeEntity ||
                entity instanceof SlimeEntity  ||
                entity instanceof VexEntity ||
                TCOTS_Main.CONFIG.monsters.Elementa().contains(Registries.ENTITY_TYPE.getId(entity.getType()).toString());
    }

    public static boolean isHybrid(LivingEntity entity){
        return entity.getGroup() == TCOTS_Entities.HYBRIDS ||
                TCOTS_Main.CONFIG.monsters.Hybrids().contains(Registries.ENTITY_TYPE.getId(entity.getType()).toString());
    }

    public static boolean isCursedOne(LivingEntity entity){
        return entity.getGroup() == TCOTS_Entities.CURSED_ONES ||
                entity instanceof CreeperEntity ||
                entity instanceof RavagerEntity ||
                TCOTS_Main.CONFIG.monsters.Cursed_Ones().contains(Registries.ENTITY_TYPE.getId(entity.getType()).toString());
    }

    public static boolean isDraconid(LivingEntity entity){
        return entity.getGroup() == TCOTS_Entities.DRACONIDS ||
                entity instanceof EnderDragonEntity ||
                TCOTS_Main.CONFIG.monsters.Draconids().contains(Registries.ENTITY_TYPE.getId(entity.getType()).toString());
    }

    public static boolean isRelict(LivingEntity entity){
        return entity.getGroup() == TCOTS_Entities.RELICTS ||
                entity instanceof EndermanEntity ||
                entity instanceof GuardianEntity ||
                entity instanceof WardenEntity   ||
                TCOTS_Main.CONFIG.monsters.Relicts().contains(Registries.ENTITY_TYPE.getId(entity.getType()).toString());
    }

    public static boolean isHumanoid(LivingEntity entity){
        return entity.getGroup() == EntityGroup.ILLAGER ||
                entity instanceof MerchantEntity ||
                entity instanceof WitchEntity ||
                entity instanceof PlayerEntity ||
                TCOTS_Main.CONFIG.monsters.Humanoids().contains(Registries.ENTITY_TYPE.getId(entity.getType()).toString());
    }

}

