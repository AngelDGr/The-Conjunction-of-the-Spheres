package TCOTS.entity.goals;

import TCOTS.entity.interfaces.ExcavatorMob;
import net.minecraft.entity.ai.goal.LookAroundGoal;
import net.minecraft.entity.mob.MobEntity;

public class LookAroundGoal_Excavator extends LookAroundGoal {

    ExcavatorMob excavatorMob;

    public LookAroundGoal_Excavator(MobEntity mob) {
        super(mob);
        if (!(mob instanceof ExcavatorMob)) {
            throw new IllegalArgumentException("LookAroundGoal_InGround requires Mob implements ExcavatorMob");
        }
        this.excavatorMob = (ExcavatorMob) mob;
    }

    @Override
    public boolean canStart(){
        return super.canStart() && !excavatorMob.getInGround();
    }

    @Override
    public boolean shouldContinue(){
        return super.shouldContinue() && !excavatorMob.getInGround();
    }
}
