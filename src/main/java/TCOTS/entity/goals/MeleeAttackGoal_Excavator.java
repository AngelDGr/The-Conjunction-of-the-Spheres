package TCOTS.entity.goals;

import TCOTS.entity.interfaces.ExcavatorMob;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.mob.PathAwareEntity;

public class MeleeAttackGoal_Excavator extends MeleeAttackGoal {

    private final ExcavatorMob excavatorMob;
    private final int ticksBeforeToGround;

    //For returning mobs
    public MeleeAttackGoal_Excavator(PathAwareEntity mob, double speed, boolean pauseWhenMobIdle, int ticksBeforeToGround) {
        super(mob, speed, pauseWhenMobIdle);
        if (!(mob instanceof ExcavatorMob)) {
            throw new IllegalArgumentException("MeleeAttackGoal_Excavator requires Mob implements ExcavatorMob");
        }
        this.excavatorMob = (ExcavatorMob) mob;
        this.ticksBeforeToGround = ticksBeforeToGround;
    }

    //For no returning mobs
    public MeleeAttackGoal_Excavator(PathAwareEntity mob, double speed, boolean pauseWhenMobIdle) {
        this(mob, speed, pauseWhenMobIdle,0);
    }

    @Override
    public boolean canStart() {
        return super.canStart()
                && !this.excavatorMob.getIsEmerging()
                && !this.excavatorMob.getInGroundDataTracker();
    }

    @Override
    protected void attack(LivingEntity target, double squaredDistance) {
        if(target!=null && ticksBeforeToGround > 0){
            int randomExtra = mob.getRandom().nextBetween(1,51);

            excavatorMob.setReturnToGround_Ticks(this.ticksBeforeToGround + randomExtra);

            super.attack(target,squaredDistance);
        } else {
            super.attack(target,squaredDistance);
        }
    }
}