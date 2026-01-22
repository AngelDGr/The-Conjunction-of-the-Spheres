package mors.tcots.entity.goal;

import mors.tcots.entity.interfaces.ExcavatorMob;
import mors.tcots.entity.interfaces.GuardNestMob;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;

public class ReturnToNestGoal extends Goal {

    private final PathfinderMob mob;
    private final GuardNestMob guardMob;
    private final double speed;
    private final double distanceBeforeReturning;


    public ReturnToNestGoal(final PathfinderMob mob, final double speed, final double distanceBeforeReturning){
        this.mob = mob;

        if (!(mob instanceof GuardNestMob)) {
            throw new IllegalArgumentException("LungeAttackGoal requires Mob implements GuardNestMob");
        }

        this.guardMob = (GuardNestMob) mob;

        this.speed=speed;
        this.distanceBeforeReturning=distanceBeforeReturning;
    }

    public ReturnToNestGoal(final PathfinderMob mob, final double speed){
        this(mob, speed, 100);
    }

    @Override
    public boolean canUse() {
        return mob.getTarget()==null
                && guardMob.getNestPos()!= BlockPos.ZERO
                && mob.getOnPos().distToLowCornerSqr(
                        guardMob.getNestPos().getX(),
                        guardMob.getNestPos().getY(),
                        guardMob.getNestPos().getZ())

                > distanceBeforeReturning
                && this.isExcavator()
                && guardMob.getExtraReasonToNotGoToNest();
    }

    private boolean isExcavator(){
        if(this.mob instanceof final ExcavatorMob excavatorMob){
            return !excavatorMob.getIsEmerging() && !excavatorMob.getInGround();
        }

        return true;
    }

    @Override
    public void start() {
        this.startMovingTo(mob.getNavigation(), guardMob.getNestPos().getX(), guardMob.getNestPos().getY(), guardMob.getNestPos().getZ(), speed);
    }

    @Override
    public void tick() {
        this.startMovingTo(mob.getNavigation(), guardMob.getNestPos().getX(), guardMob.getNestPos().getY(), guardMob.getNestPos().getZ(), speed);
    }

    public void startMovingTo(final PathNavigation navigation, final int x, final int y, final int z, final double speed) {
        navigation.moveTo(navigation.createPath(x, y, z, 2), speed);
    }

}
