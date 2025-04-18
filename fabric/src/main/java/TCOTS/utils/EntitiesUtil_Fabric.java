package TCOTS.utils;

import TCOTS.TCOTS_Main;
import TCOTS.TCOTS_Tags;
import TCOTS.items.TCOTS_Items_Fabric;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.allay.Allay;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.monster.Blaze;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.entity.monster.Guardian;
import net.minecraft.world.entity.monster.Ravager;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.monster.Vex;
import net.minecraft.world.entity.monster.Witch;
import net.minecraft.world.entity.monster.breeze.Breeze;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;

public class EntitiesUtil_Fabric {

    /**
     Checks if the player it's wearing the full Manticore Armor
     */
    public static boolean isWearingManticoreArmor(LivingEntity player){
        return player.getItemBySlot(EquipmentSlot.CHEST).is(TCOTS_Items_Fabric.MANTICORE_ARMOR)
                && player.getItemBySlot(EquipmentSlot.LEGS).is(TCOTS_Items_Fabric.MANTICORE_TROUSERS)
                && player.getItemBySlot(EquipmentSlot.FEET).is(TCOTS_Items_Fabric.MANTICORE_BOOTS);
    }

    /**
     Checks if the player it's wearing the full Warrior's Leather Armor
     */
    public static boolean isWearingWarriorsLeatherArmor(LivingEntity player){
        return player.getItemBySlot(EquipmentSlot.CHEST).is(TCOTS_Items_Fabric.WARRIORS_LEATHER_JACKET)
                && player.getItemBySlot(EquipmentSlot.LEGS).is(TCOTS_Items_Fabric.WARRIORS_LEATHER_TROUSERS)
                && player.getItemBySlot(EquipmentSlot.FEET).is(TCOTS_Items_Fabric.WARRIORS_LEATHER_BOOTS);
    }

    /**
     Checks if the player it's wearing the full Raven's Armor
     */
    public static boolean isWearingRavensArmor(LivingEntity player){
        return player.getItemBySlot(EquipmentSlot.CHEST).is(TCOTS_Items_Fabric.RAVENS_ARMOR)
                && player.getItemBySlot(EquipmentSlot.LEGS).is(TCOTS_Items_Fabric.RAVENS_TROUSERS)
                && player.getItemBySlot(EquipmentSlot.FEET).is(TCOTS_Items_Fabric.RAVENS_BOOTS);
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
        return entity.getType().is(TCOTS_Tags.NECROPHAGES) ||
                entity.getType().is(EntityTypeTags.UNDEAD) ||
                TCOTS_Main.CONFIG.monsters.Necrophages().contains(BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType()).toString());
    }

    public static boolean isOgroid(LivingEntity entity){
        return entity.getType().is(TCOTS_Tags.OGROIDS) ||
                entity instanceof AbstractPiglin ||
                TCOTS_Main.CONFIG.monsters.Ogroids().contains(BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType()).toString());
    }

    public static boolean isSpecter(LivingEntity entity){
        return entity.getType().is(TCOTS_Tags.SPECTERS) ||
                entity instanceof Ghast ||
                TCOTS_Main.CONFIG.monsters.Specters().contains(BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType()).toString());
    }

    public static boolean isVampire(LivingEntity entity){
        return entity.getType().is(TCOTS_Tags.VAMPIRES) ||
                TCOTS_Main.CONFIG.monsters.Vampires().contains(BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType()).toString());
    }

    public static boolean isInsectoid(LivingEntity entity){
        return entity.getType().is(TCOTS_Tags.INSECTOIDS) ||
                entity.getType().is(EntityTypeTags.ARTHROPOD) ||
                TCOTS_Main.CONFIG.monsters.Insectoids().contains(BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType()).toString());
    }

    public static boolean isBeast(LivingEntity entity){
        return entity.getType().is(TCOTS_Tags.BEASTS) ||
                entity instanceof Animal ||
                TCOTS_Main.CONFIG.monsters.Beasts().contains(BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType()).toString());
    }

    public static boolean isElementa(LivingEntity entity){
        return entity.getType().is(TCOTS_Tags.ELEMENTA) ||
                entity instanceof Allay  ||
                entity instanceof AbstractGolem  ||
                entity instanceof Blaze  ||
                entity instanceof Breeze ||
                entity instanceof Slime  ||
                entity instanceof Vex ||
                TCOTS_Main.CONFIG.monsters.Elementa().contains(BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType()).toString());
    }

    public static boolean isHybrid(LivingEntity entity){
        return entity.getType().is(TCOTS_Tags.HYBRIDS) ||
                TCOTS_Main.CONFIG.monsters.Hybrids().contains(BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType()).toString());
    }

    public static boolean isCursedOne(LivingEntity entity){
        return entity.getType().is(TCOTS_Tags.CURSED_ONES) ||
                entity instanceof Creeper ||
                entity instanceof Ravager ||
                TCOTS_Main.CONFIG.monsters.Cursed_Ones().contains(BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType()).toString());
    }

    public static boolean isDraconid(LivingEntity entity){
        return entity.getType().is(TCOTS_Tags.DRACONIDS) ||
                entity instanceof EnderDragon ||
                TCOTS_Main.CONFIG.monsters.Draconids().contains(BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType()).toString());
    }

    public static boolean isRelict(LivingEntity entity){
        return entity.getType().is(TCOTS_Tags.RELICTS) ||
                entity instanceof EnderMan ||
                entity instanceof Guardian ||
                entity instanceof Warden   ||
                TCOTS_Main.CONFIG.monsters.Relicts().contains(BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType()).toString());
    }

    public static boolean isHumanoid(LivingEntity entity){
        return entity.getType().is(EntityTypeTags.ILLAGER) ||
                entity instanceof AbstractVillager ||
                entity instanceof Witch ||
                entity instanceof Player ||
                TCOTS_Main.CONFIG.monsters.Humanoids().contains(BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType()).toString());
    }

}

