package TCOTS.entity;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Difficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import software.bernie.geckolib.core.animation.RawAnimation;


@SuppressWarnings({"unused"})
public class WitcherMob_Class extends HostileEntity implements Monster {
    protected WitcherMob_Class(EntityType<? extends WitcherMob_Class> entityType, World world) {
        super(entityType, world);
        this.experiencePoints = 5;
    }

    public static final RawAnimation IDLE = RawAnimation.begin().thenLoop("idle");
    public static final RawAnimation RUNNING = RawAnimation.begin().thenLoop("move.running");
    public static final RawAnimation WALKING = RawAnimation.begin().thenLoop("move.walking");

    @Override
    public float getPathfindingFavor(BlockPos pos, WorldView world) {
        return 0.0f;
    }

    protected SoundEvent getStepSound() {
        return null;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        if(getStepSound()!=null){
            this.playSound(this.getStepSound(), 0.15F, 1.0F);}
        else {
            super.playStepSound(pos,state);
        }
    }

    @Override
    public boolean tryAttack(Entity target) {
        if(this.getAttackSound() != null){
            this.playSound(this.getAttackSound(), 1.0F, 1.0F);
        }
        return super.tryAttack(target);
    }

    protected SoundEvent getAttackSound(){
        return null;
    }

    public static boolean canSpawnInDarkW(EntityType<? extends WitcherMob_Class> type, ServerWorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
        return world.getDifficulty() != Difficulty.PEACEFUL && HostileEntity.isSpawnDark(world, pos, random) && HostileEntity.canMobSpawn(type, world, spawnReason, pos, random);
    }

    public static boolean canSpawnInDarkNotSurface(EntityType<? extends WitcherMob_Class> type, ServerWorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
        if(spawnReason == SpawnReason.SPAWNER){
           return world.getDifficulty() != Difficulty.PEACEFUL && HostileEntity.isSpawnDark(world, pos, random) && HostileEntity.canMobSpawn(type, world, spawnReason, pos, random);
        } else {
        return world.getDifficulty() != Difficulty.PEACEFUL && HostileEntity.isSpawnDark(world, pos, random) && HostileEntity.canMobSpawn(type, world, spawnReason, pos, random) &&
                pos.getY() <= 50;
        }
    }

    public static boolean canSpawnInDark_NotCaves(EntityType<? extends WitcherMob_Class> type, ServerWorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
        if(spawnReason==SpawnReason.SPAWNER){
            return world.getDifficulty() != Difficulty.PEACEFUL && HostileEntity.isSpawnDark(world, pos, random) && HostileEntity.canMobSpawn(type, world, spawnReason, pos, random);
        } else {
            return world.getDifficulty() != Difficulty.PEACEFUL && HostileEntity.isSpawnDark(world, pos, random) && HostileEntity.canMobSpawn(type, world, spawnReason, pos, random) &&
                    pos.getY() >= 50;
        }
    }

    public static boolean canSpawnInDarkNotBelowDeepslate(EntityType<? extends WitcherMob_Class> type, ServerWorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
        if(spawnReason==SpawnReason.SPAWNER){
            return world.getDifficulty() != Difficulty.PEACEFUL && HostileEntity.isSpawnDark(world, pos, random) && HostileEntity.canMobSpawn(type, world, spawnReason, pos, random);
        } else {
            return world.getDifficulty() != Difficulty.PEACEFUL && HostileEntity.isSpawnDark(world, pos, random) && HostileEntity.canMobSpawn(type, world, spawnReason, pos, random) &&
                    pos.getY() >= -20;
        }
    }

}
