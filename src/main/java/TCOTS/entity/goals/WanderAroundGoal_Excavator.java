package TCOTS.entity.goals;

import TCOTS.entity.interfaces.ExcavatorMob;
import net.minecraft.entity.ai.goal.WanderAroundGoal;
import net.minecraft.entity.mob.PathAwareEntity;

public class WanderAroundGoal_Excavator extends WanderAroundGoal {

    ExcavatorMob excavatorMob;

    public WanderAroundGoal_Excavator(PathAwareEntity mob, double speed, int chance) {
        super(mob, speed, chance);
        if (!(mob instanceof ExcavatorMob)) {
            throw new IllegalArgumentException("WanderAroundGoal_Excavator requires Mob implements ExcavatorMob");
        }
        excavatorMob=(ExcavatorMob)mob;
    }


    @Override
    public boolean canStart() {
        return super.canStart() && !excavatorMob.getInGround();
    }

    @Override
    public boolean shouldContinue() {
        return super.shouldContinue() && !excavatorMob.getInGround();
    }
}
