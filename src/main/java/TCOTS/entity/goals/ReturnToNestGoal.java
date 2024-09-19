package TCOTS.entity.goals;

import TCOTS.entity.interfaces.ExcavatorMob;
import TCOTS.entity.interfaces.GuardNestMob;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.util.math.BlockPos;

public class ReturnToNestGoal extends Goal {

    private final PathAwareEntity mob;
    private final GuardNestMob guardMob;
    private final double speed;
    private final double distanceBeforeReturning;


    public ReturnToNestGoal(PathAwareEntity mob, double speed, double distanceBeforeReturning){
        this.mob = mob;

        if (!(mob instanceof GuardNestMob)) {
            throw new IllegalArgumentException("LungeAttackGoal requires Mob implements GuardNestMob");
        }

        this.guardMob = (GuardNestMob) mob;

        this.speed=speed;
        this.distanceBeforeReturning=distanceBeforeReturning;
    }

    public ReturnToNestGoal(PathAwareEntity mob, double speed){
        this(mob, speed, 100);
    }

    @Override
    public boolean canStart() {
        return mob.getTarget()==null
                && guardMob.getNestPos()!= BlockPos.ORIGIN
                && mob.getSteppingPos().getSquaredDistance(
                        guardMob.getNestPos().getX(),
                        guardMob.getNestPos().getY(),
                        guardMob.getNestPos().getZ())

                > distanceBeforeReturning
                && this.isExcavator()
                && guardMob.getExtraReasonNotGoToNest();
    }

    private boolean isExcavator(){
        if(this.mob instanceof ExcavatorMob excavatorMob){
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

    public void startMovingTo(EntityNavigation navigation, int x, int y, int z, double speed) {
        navigation.startMovingAlong(navigation.findPathTo(x, y, z, 2), speed);
    }

}
