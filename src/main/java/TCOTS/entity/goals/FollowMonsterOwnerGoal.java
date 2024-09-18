package TCOTS.entity.goals;

import TCOTS.entity.interfaces.ExcavatorMob;
import net.minecraft.entity.Ownable;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.PathAwareEntity;
import org.jetbrains.annotations.Nullable;

public class FollowMonsterOwnerGoal extends Goal {
    private final PathAwareEntity mob;
    @Nullable
    private PathAwareEntity owner;
    private final double speed;
    private int delay;

    public FollowMonsterOwnerGoal(PathAwareEntity mob, double speed) {
        if (!(mob instanceof Ownable)) {
            throw new IllegalArgumentException("FollowOwnerGoal requires Mob implements Ownable");
        }
        this.mob = mob;
        this.speed = speed;
    }

    @Override
    public boolean canStart()
    {
        if (((Ownable)(this.mob)).getOwner() == null) {
            return false;
        }

        this.owner= (PathAwareEntity) ((Ownable)(this.mob)).getOwner();
        double d = this.mob.squaredDistanceTo(this.owner);

        return !(d < 9.0) && !(d > 256.0) && this.isExcavating() && mob.getTarget()==null;
    }

    @Override
    public boolean shouldContinue() {
        if (this.owner == null) {
            return false;
        }

        if (!this.owner.isAlive()) {
            return false;
        }
        double d = this.mob.squaredDistanceTo(this.owner);
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
        this.delay = this.getTickCount(10);
        this.mob.getNavigation().startMovingTo(this.owner, this.speed);
    }

    private boolean isExcavating(){
        if(mob instanceof ExcavatorMob excavator){
            return !excavator.getInGround() && !excavator.getIsEmerging();
        }

        return true;
    }
}
