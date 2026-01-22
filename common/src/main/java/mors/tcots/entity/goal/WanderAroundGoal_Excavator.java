package mors.tcots.entity.goal;

import mors.tcots.entity.interfaces.ExcavatorMob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;

public class WanderAroundGoal_Excavator extends RandomStrollGoal {

    ExcavatorMob excavatorMob;

    public WanderAroundGoal_Excavator(final PathfinderMob mob, final double speed, final int chance) {
        super(mob, speed, chance);
        if (!(mob instanceof ExcavatorMob)) {
            throw new IllegalArgumentException("WanderAroundGoal_Excavator requires Mob implements ExcavatorMob");
        }
        excavatorMob=(ExcavatorMob)mob;
    }


    @Override
    public boolean canUse() {
        return super.canUse() && !excavatorMob.getInGround();
    }

    @Override
    public boolean canContinueToUse() {
        return super.canContinueToUse() && !excavatorMob.getInGround();
    }
}
