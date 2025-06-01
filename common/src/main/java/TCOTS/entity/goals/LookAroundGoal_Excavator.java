package TCOTS.entity.goals;

import TCOTS.entity.interfaces.ExcavatorMob;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;

public class LookAroundGoal_Excavator extends RandomLookAroundGoal {

    ExcavatorMob excavatorMob;

    public LookAroundGoal_Excavator(Mob mob) {
        super(mob);
        if (!(mob instanceof ExcavatorMob)) {
            throw new IllegalArgumentException("LookAroundGoal_InGround requires Mob implements ExcavatorMob");
        }
        this.excavatorMob = (ExcavatorMob) mob;
    }

    @Override
    public boolean canUse(){
        return super.canUse() && !excavatorMob.getInGround();
    }

    @Override
    public boolean canContinueToUse(){
        return super.canContinueToUse() && !excavatorMob.getInGround();
    }
}
