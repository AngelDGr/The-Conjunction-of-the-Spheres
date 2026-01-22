package mors.tcots.entity.goal;

import mors.tcots.entity.interfaces.ExcavatorMob;
import java.util.List;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;

public class EmergeFromGroundGoal_Excavator extends Goal {
    private final ExcavatorMob excavatorMob;
    protected final PathfinderMob mob;

    private final int returnTicks;
    private final boolean generatesPuddle;

    private final int maxAnimationDuration;
    private int animationDuration =36;

    public EmergeFromGroundGoal_Excavator(final PathfinderMob mob, final int returnTicks) {
        this(mob,returnTicks,36);
    }

    public EmergeFromGroundGoal_Excavator(final PathfinderMob mob, final int returnTicks, int maxAnimationDuration) {
        this(mob,returnTicks,false, maxAnimationDuration);
    }

    public EmergeFromGroundGoal_Excavator(final PathfinderMob mob, final int returnTicks, final boolean generatesPuddle) {
        this(mob, returnTicks, generatesPuddle, 36);
    }

    public EmergeFromGroundGoal_Excavator(final PathfinderMob mob, final int returnTicks, final boolean generatesPuddle, int maxAnimationDuration) {
        if (!(mob instanceof ExcavatorMob)) {
            throw new IllegalArgumentException("EmergeFromGroundGoal requires Mob implements ExcavatorMob");
        }
        this.excavatorMob = (ExcavatorMob) mob;
        this.mob = mob;
        this.returnTicks=returnTicks;
        this.generatesPuddle =generatesPuddle;
        this.maxAnimationDuration=maxAnimationDuration;
    }

    @Override
    public boolean canUse() {
        return (canStartO() || detectedBySomeone()) && excavatorMob.getInGround();
    }
    public boolean canStartO(){
        final LivingEntity livingEntity = this.mob.getTarget();
        //If it doesn't have target
        if (livingEntity == null) {
            return false;
        }
        //If it's the target dead
        else if (!livingEntity.isAlive()) {
            return false;
        }
        else {
            return this.mob.distanceToSqr(livingEntity.getX(), livingEntity.getY(), livingEntity.getZ()) <= 80;
        }
    }

    public boolean detectedBySomeone(){
        final List<Mob> enemiesList =
        this.mob.level().getEntitiesOfClass(Mob.class, this.mob.getBoundingBox().inflate(10,5,10),
                entity -> entity.getTarget() == this.mob);

        return !enemiesList.isEmpty();
    }

    @Override
    public void start(){
        if(excavatorMob.getInvisibleData()){
            excavatorMob.setInvisibleData(false);
        }

        this.mob.playSound(excavatorMob.getEmergingSound(), 1.0F, 1.0F);

        if(excavatorMob.getPuddle()!=null){
            excavatorMob.getPuddle().setDespawnPuddle(true);
        }

        this.excavatorMob.spawnGroundParticles(mob);
        animationDuration = maxAnimationDuration;
        excavatorMob.setIsEmerging(true);
    }

    @Override
    public boolean canContinueToUse(){
        return (shouldContinueO() || detectedBySomeone()) && excavatorMob.getInGround();
    }
    public boolean shouldContinueO(){
        final LivingEntity livingEntity = this.mob.getTarget();
        if (livingEntity == null) {
            return false;
        } else if (!livingEntity.isAlive()) {
            return false;
        } else {
            return !(livingEntity instanceof Player) || !livingEntity.isSpectator() && !((Player)livingEntity).isCreative();
        }
    }

    @Override
    public void tick(){
        if (animationDuration > 0) {
            --animationDuration;
        }else {
            stop();
        }
    }

    @Override
    public void stop(){
        excavatorMob.setIsEmerging(false);
        if(generatesPuddle) {
            if (excavatorMob.getPuddle() != null) {
                destroyPuddle();
            }
        }

        if(excavatorMob.getInGround()){
            excavatorMob.setReturnToGround_Ticks(returnTicks);
            excavatorMob.setInGround(false);}
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    public void destroyPuddle(){
        excavatorMob.getPuddle().discard();
        excavatorMob.setSpawnedPuddleDataTracker(false);
    }
}
