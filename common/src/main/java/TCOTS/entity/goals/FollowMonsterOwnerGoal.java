package TCOTS.entity.goals;

import TCOTS.entity.interfaces.ExcavatorMob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.TraceableEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import org.jetbrains.annotations.Nullable;

public class FollowMonsterOwnerGoal extends Goal {
    private final PathfinderMob mob;
    @Nullable
    private PathfinderMob owner;
    private final double speed;
    private int delay;

    public FollowMonsterOwnerGoal(PathfinderMob mob, double speed) {
        if (!(mob instanceof TraceableEntity)) {
            throw new IllegalArgumentException("FollowOwnerGoal requires Mob implements Ownable");
        }
        this.mob = mob;
        this.speed = speed;
    }

    @Override
    public boolean canUse()
    {
        if (((TraceableEntity)(this.mob)).getOwner() == null) {
            return false;
        }

        this.owner= (PathfinderMob) ((TraceableEntity)(this.mob)).getOwner();
        double d = this.mob.distanceToSqr(this.owner);

        return !(d < 9.0) && !(d > 256.0) && this.isExcavating() && mob.getTarget()==null;
    }

    @Override
    public boolean canContinueToUse() {
        if (this.owner == null) {
            return false;
        }

        if (!this.owner.isAlive()) {
            return false;
        }
        double d = this.mob.distanceToSqr(this.owner);
        return !(d < 9.0) && !(d > 256.0) && this.isExcavating();
    }

    @Override
    public void start() {
        this.delay = 0;
    }

    @Override
    public void stop() {
        this.owner = null;
    }

    @Override
    public void tick() {
        if (--this.delay > 0) {
            return;
        }
        this.delay = this.adjustedTickDelay(10);
        this.mob.getNavigation().moveTo(this.owner, this.speed);
    }

    private boolean isExcavating(){
        if(mob instanceof ExcavatorMob excavator){
            return !excavator.getInGround() && !excavator.getIsEmerging();
        }

        return true;
    }
}
