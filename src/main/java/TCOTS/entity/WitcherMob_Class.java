package TCOTS.entity;

import TCOTS.entity.necrophages.Necrophage_Base;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Difficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;


public class WitcherMob_Class extends PathAwareEntity implements Monster {
    protected WitcherMob_Class(EntityType<? extends PathAwareEntity> entityType, World world) {
        super(entityType, world);
        this.experiencePoints = 5;
    }

    @Override
    public void tickMovement() {
        this.tickHandSwing();
        this.updateDespawnCounter();
        super.tickMovement();
    }

    protected void updateDespawnCounter() {
        float f = this.getBrightnessAtEyes();
        if (f > 0.5F) {
            this.despawnCounter += 2;
        }

    }

    public boolean isAngryAt(PlayerEntity player) {
        return true;
    }

    @Override
    protected boolean isDisallowedInPeaceful() {
        return true;
    }

    public static boolean canSpawnInDark(EntityType<? extends Necrophage_Base> type, ServerWorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
        return world.getDifficulty() != Difficulty.PEACEFUL && HostileEntity.isSpawnDark(world, pos, random) && HostileEntity.canMobSpawn(type, world, spawnReason, pos, random);
    }

    public static boolean canSpawnInDarkNotDeepslate(EntityType<? extends Necrophage_Base> type, ServerWorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
        return world.getDifficulty() != Difficulty.PEACEFUL && HostileEntity.isSpawnDark(world, pos, random) && HostileEntity.canMobSpawn(type, world, spawnReason, pos, random) &&
                pos.getY() >= 0;
    }

    public static boolean canSpawnInDarkNotDeepDeepslate(EntityType<? extends Necrophage_Base> type, ServerWorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
        return world.getDifficulty() != Difficulty.PEACEFUL && HostileEntity.isSpawnDark(world, pos, random) && HostileEntity.canMobSpawn(type, world, spawnReason, pos, random) &&
                pos.getY() >= -20;
    }

    @Override
    public SoundCategory getSoundCategory() {
        return SoundCategory.HOSTILE;
    }
}
