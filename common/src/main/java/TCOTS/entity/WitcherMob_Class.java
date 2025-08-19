package TCOTS.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animation.RawAnimation;

@SuppressWarnings({"unused"})
public class WitcherMob_Class extends Monster implements Enemy {
    protected WitcherMob_Class(final EntityType<? extends WitcherMob_Class> entityType, final Level world) {
        super(entityType, world);
        this.xpReward = 5;
    }

    public static final RawAnimation IDLE = RawAnimation.begin().thenLoop("idle");
    public static final RawAnimation RUNNING = RawAnimation.begin().thenLoop("move.running");
    public static final RawAnimation WALKING = RawAnimation.begin().thenLoop("move.walking");

    @Override
    public float getWalkTargetValue(final @NotNull BlockPos pos, final @NotNull LevelReader world) {
        return 0.0f;
    }

    protected SoundEvent getStepSound() {
        return null;
    }

    @Override
    protected void playStepSound(final @NotNull BlockPos pos, final @NotNull BlockState state) {
        if(getStepSound()!=null){
            this.playSound(this.getStepSound(), 0.15F, 1.0F);}
        else {
            super.playStepSound(pos,state);
        }
    }

    @Override
    public boolean doHurtTarget(final @NotNull Entity target) {
        if(this.getAttackSound() != null){
            this.playSound(this.getAttackSound(), 1.0F, 1.0F);
        }
        return super.doHurtTarget(target);
    }

    protected SoundEvent getAttackSound(){
        return null;
    }

    public static boolean canSpawnInDarkW(final EntityType<? extends WitcherMob_Class> type, final ServerLevelAccessor world, final MobSpawnType spawnReason, final BlockPos pos, final RandomSource random) {
        return world.getDifficulty() != Difficulty.PEACEFUL && Monster.isDarkEnoughToSpawn(world, pos, random) && Monster.checkMobSpawnRules(type, world, spawnReason, pos, random);
    }

    public static boolean canSpawnInDarkNotSurface(final EntityType<? extends WitcherMob_Class> type, final ServerLevelAccessor world, final MobSpawnType spawnReason, final BlockPos pos, final RandomSource random) {
        if(spawnReason == MobSpawnType.SPAWNER){
           return world.getDifficulty() != Difficulty.PEACEFUL && Monster.isDarkEnoughToSpawn(world, pos, random) && Monster.checkMobSpawnRules(type, world, spawnReason, pos, random);
        } else {
        return world.getDifficulty() != Difficulty.PEACEFUL && Monster.isDarkEnoughToSpawn(world, pos, random) && Monster.checkMobSpawnRules(type, world, spawnReason, pos, random) &&
                pos.getY() <= 50;
        }
    }

    public static boolean canSpawnInDark_NotCaves(final EntityType<? extends WitcherMob_Class> type, final ServerLevelAccessor world, final MobSpawnType spawnReason, final BlockPos pos, final RandomSource random) {
        if(spawnReason==MobSpawnType.SPAWNER){
            return world.getDifficulty() != Difficulty.PEACEFUL && Monster.isDarkEnoughToSpawn(world, pos, random) && Monster.checkMobSpawnRules(type, world, spawnReason, pos, random);
        } else {
            return world.getDifficulty() != Difficulty.PEACEFUL && Monster.isDarkEnoughToSpawn(world, pos, random) && Monster.checkMobSpawnRules(type, world, spawnReason, pos, random) &&
                    pos.getY() >= 50;
        }
    }

    public static boolean canSpawnInDarkNotBelowDeepslate(final EntityType<? extends WitcherMob_Class> type, final ServerLevelAccessor world, final MobSpawnType spawnReason, final BlockPos pos, final RandomSource random) {
        if(spawnReason==MobSpawnType.SPAWNER){
            return world.getDifficulty() != Difficulty.PEACEFUL && Monster.isDarkEnoughToSpawn(world, pos, random) && Monster.checkMobSpawnRules(type, world, spawnReason, pos, random);
        } else {
            return world.getDifficulty() != Difficulty.PEACEFUL && Monster.isDarkEnoughToSpawn(world, pos, random) && Monster.checkMobSpawnRules(type, world, spawnReason, pos, random) &&
                    pos.getY() >= -20;
        }
    }

}
