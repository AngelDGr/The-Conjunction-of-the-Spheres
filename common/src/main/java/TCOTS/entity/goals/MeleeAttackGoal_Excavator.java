package TCOTS.entity.goals;

import TCOTS.entity.interfaces.ExcavatorMob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;

public class MeleeAttackGoal_Excavator extends MeleeAttackGoal_Animated {

    private final ExcavatorMob excavatorMob;
    private final int ticksBeforeToGround;

    //For returning mobs
    public MeleeAttackGoal_Excavator(final PathfinderMob mob, final double speed, final boolean pauseWhenMobIdle, final int ticksBeforeToGround) {
        super(mob, speed, pauseWhenMobIdle);
        if (!(mob instanceof ExcavatorMob)) {
            throw new IllegalArgumentException("MeleeAttackGoal_Excavator requires Mob implements ExcavatorMob");
        }
        this.excavatorMob = (ExcavatorMob) mob;
        this.ticksBeforeToGround = ticksBeforeToGround;
    }

    @SuppressWarnings("unused")
    public MeleeAttackGoal_Excavator(final PathfinderMob mob, final double speed, final boolean pauseWhenMobIdle, final int ticksBeforeToGround, final boolean twoAttacks) {
        super(mob, speed, pauseWhenMobIdle, 2);
        if (!(mob instanceof ExcavatorMob)) {
            throw new IllegalArgumentException("MeleeAttackGoal_Excavator requires Mob implements ExcavatorMob");
        }
        this.excavatorMob = (ExcavatorMob) mob;
        this.ticksBeforeToGround = ticksBeforeToGround;
    }

    //For no returning mobs
    public MeleeAttackGoal_Excavator(final PathfinderMob mob, final double speed, final boolean pauseWhenMobIdle) {
        this(mob, speed, pauseWhenMobIdle,0);
    }

    @Override
    public boolean canUse() {
        return super.canUse()
                && !this.excavatorMob.getIsEmerging()
                && !this.excavatorMob.getInGround();
    }

    @Override
    public void start() {
        if(ticksBeforeToGround > 0){
            final int randomExtra = mob.getRandom().nextIntBetweenInclusive(1,51);
            excavatorMob.setReturnToGround_Ticks(this.ticksBeforeToGround + randomExtra);
        } else if (ticksBeforeToGround == 0) {
            excavatorMob.setReturnToGround_Ticks(5000);
        }

        super.start();
    }

    @Override
    protected void attack(final LivingEntity target) {
        if(target!=null && ticksBeforeToGround > 0){
            final int randomExtra = mob.getRandom().nextIntBetweenInclusive(1,51);

            excavatorMob.setReturnToGround_Ticks(this.ticksBeforeToGround + randomExtra);

            super.attack(target);
        } else {
            super.attack(target);
        }
    }
}
