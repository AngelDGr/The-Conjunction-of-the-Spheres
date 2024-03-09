package TCOTS.entity.goals;

import TCOTS.entity.interfaces.ExcavatorMob;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;

public class EmergeFromGroundGoal_Excavator extends Goal {
    private final ExcavatorMob excavatorMob;
    protected final PathAwareEntity mob;

    private final int returnTicks;
    private final boolean generatesPuddle;

    int AnimationTicks=36;

    public EmergeFromGroundGoal_Excavator(PathAwareEntity mob, int returnTicks) {
        this(mob,returnTicks,false);
    }

    public EmergeFromGroundGoal_Excavator(PathAwareEntity mob, int returnTicks, boolean generatesPuddle) {
        if (!(mob instanceof ExcavatorMob)) {
            throw new IllegalArgumentException("EmergeFromGroundGoal requires Mob implements ExcavatorMob");
        }
        this.excavatorMob = (ExcavatorMob) mob;
        this.mob = mob;
        this.returnTicks=returnTicks;
        this.generatesPuddle =generatesPuddle;
    }

    @Override
    public boolean canStart() {
        return canStartO() && excavatorMob.getInGroundDataTracker();
    }
    public boolean canStartO(){
        LivingEntity livingEntity = this.mob.getTarget();
        //If it doesn't have target
        if (livingEntity == null) {
            return false;
        }
        //If it's the target dead
        else if (!livingEntity.isAlive()) {
            return false;
        }
        else {
            return this.mob.squaredDistanceTo(livingEntity.getX(), livingEntity.getY(), livingEntity.getZ()) <= 80;
        }
    }

    @Override
    public void start(){
        this.mob.playSound(excavatorMob.getEmergingSound(), 1.0F, 1.0F);

        if(excavatorMob.getPuddle()!=null){
            excavatorMob.getPuddle().setSpawnControlDataTracker(false);
        }

        this.excavatorMob.spawnGroundParticles();
        AnimationTicks=36;
        excavatorMob.setIsEmerging(true);
    }

    @Override
    public boolean shouldContinue(){
        return shouldContinueO() && excavatorMob.getInGroundDataTracker();
    }
    public boolean shouldContinueO(){
        LivingEntity livingEntity = this.mob.getTarget();
        if (livingEntity == null) {
            return false;
        } else if (!livingEntity.isAlive()) {
            return false;
        } else {
            return !(livingEntity instanceof PlayerEntity) || !livingEntity.isSpectator() && !((PlayerEntity)livingEntity).isCreative();
        }
    }

    @Override
    public void tick(){
        if (AnimationTicks > 0) {
            --AnimationTicks;
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

        if(excavatorMob.getInGroundDataTracker()){
            excavatorMob.setReturnToGround_Ticks(returnTicks);
            excavatorMob.setInGroundDataTracker(false);}
    }

    @Override
    public boolean shouldRunEveryTick() {
        return true;
    }

    public void destroyPuddle(){
        excavatorMob.getPuddle().discard();
        excavatorMob.setSpawnedPuddleDataTracker(false);
    }
}
