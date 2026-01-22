package mors.tcots.entity.goal;

import software.bernie.geckolib.animatable.GeoEntity;

import java.util.EnumSet;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.pathfinder.Path;

public class MeleeAttackGoal_Animated extends Goal {
    protected final PathfinderMob mob;
    public final double speed;
    protected final boolean pauseWhenMobIdle;
    private Path path;
    protected double targetX;
    protected double targetY;
    protected double targetZ;
    protected int updateCountdownTicks;
    protected int cooldown;
    private long lastUpdateTime;
    private final int attackAnimationsNumber;

    public MeleeAttackGoal_Animated(final PathfinderMob mob, final double speed, final boolean pauseWhenMobIdle) {
        this(mob, speed, pauseWhenMobIdle, 3);
    }

    public MeleeAttackGoal_Animated(final PathfinderMob mob, final double speed, final boolean pauseWhenMobIdle, final int attackAnimationsNumber) {
        if (!(mob instanceof GeoEntity)) {
            throw new IllegalArgumentException("MeleeAttackGoal_Animated requires Mob implements GeoEntity");
        }
        this.mob = mob;
        this.speed = speed;
        this.pauseWhenMobIdle = pauseWhenMobIdle;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        this.attackAnimationsNumber=attackAnimationsNumber;
    }

    @Override
    public boolean canUse() {
        final long l = this.mob.level().getGameTime();
        if (l - this.lastUpdateTime < 20L) {
            return false;
        }
        this.lastUpdateTime = l;
        final LivingEntity livingEntity = this.mob.getTarget();
        if (livingEntity == null) {
            return false;
        }
        if (!livingEntity.isAlive()) {
            return false;
        }
        this.path = this.mob.getNavigation().createPath(livingEntity, 0);
        if (this.path != null) {
            return true;
        }
        return this.mob.isWithinMeleeAttackRange(livingEntity);
    }

    @Override
    public boolean canContinueToUse() {
        final LivingEntity livingEntity = this.mob.getTarget();
        if (livingEntity == null) {
            return false;
        }
        if (!livingEntity.isAlive()) {
            return false;
        }
        if (!this.pauseWhenMobIdle) {
            return !this.mob.getNavigation().isDone();
        }
        if (!this.mob.isWithinRestriction(livingEntity.blockPosition())) {
            return false;
        }
        return !(livingEntity instanceof Player) || !livingEntity.isSpectator() && !((Player)livingEntity).isCreative();
    }

    @Override
    public void start() {
        this.mob.getNavigation().moveTo(this.path, this.speed);
        this.mob.setAggressive(true);
        this.updateCountdownTicks = 0;
        this.cooldown = 0;
    }

    @Override
    public void stop() {
        final LivingEntity livingEntity = this.mob.getTarget();
        if (!EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(livingEntity)) {
            this.mob.setTarget(null);
        }
        this.mob.setAggressive(false);
        this.mob.getNavigation().stop();
    }

    @Override
    public boolean requiresUpdateEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        final LivingEntity livingEntity = this.mob.getTarget();
        if (livingEntity == null) {
            return;
        }
        this.mob.getLookControl().setLookAt(livingEntity, 30.0f, 30.0f);
        this.updateCountdownTicks = Math.max(this.updateCountdownTicks - 1, 0);
        if ((this.pauseWhenMobIdle || this.mob.getSensing().hasLineOfSight(livingEntity)) && this.updateCountdownTicks <= 0 && (this.targetX == 0.0 && this.targetY == 0.0 && this.targetZ == 0.0 || livingEntity.distanceToSqr(this.targetX, this.targetY, this.targetZ) >= 1.0 || this.mob.getRandom().nextFloat() < 0.05f)) {
            this.targetX = livingEntity.getX();
            this.targetY = livingEntity.getY();
            this.targetZ = livingEntity.getZ();
            this.updateCountdownTicks = 4 + this.mob.getRandom().nextInt(7);
            final double d = this.mob.distanceToSqr(livingEntity);
            if (d > 1024.0) {
                this.updateCountdownTicks += 10;
            } else if (d > 256.0) {
                this.updateCountdownTicks += 5;
            }
            if (!this.mob.getNavigation().moveTo(livingEntity, this.speed)) {
                this.updateCountdownTicks += 15;
            }
            this.updateCountdownTicks = this.adjustedTickDelay(this.updateCountdownTicks);
        }
        this.cooldown = Math.max(this.cooldown - 1, 0);
        this.attack(livingEntity);
    }

    protected void attack(final LivingEntity target) {
        if (this.canAttack(target)) {
            this.resetCooldown();
            this.mob.swing(InteractionHand.MAIN_HAND);
            this.mob.getRandom().nextIntBetweenInclusive(0,1);

            //Triggers the Animation
            if(this.mob instanceof final GeoEntity geo) {
                final int randomAttack;
                switch (this.attackAnimationsNumber){
                    //Two attack animations
                    case 2:
                        randomAttack = this.mob.getRandom().nextIntBetweenInclusive(0, 1);
                        if (randomAttack == 0) {
                            geo.triggerAnim("base_controller", "attack1");
                        } else {
                            geo.triggerAnim("base_controller", "attack2");
                        }

                        break;

                    //Three attack animations
                    case 3:
                        randomAttack = this.mob.getRandom().nextIntBetweenInclusive(0, 2);
                        if (randomAttack == 0) {
                            geo.triggerAnim("base_controller", "attack1");
                        } else if (randomAttack == 1) {
                            geo.triggerAnim("base_controller", "attack2");
                        } else {
                            geo.triggerAnim("base_controller", "attack3");
                        }

                        break;

                    default:
                        break;
                }
            }

            this.mob.doHurtTarget(target);
        }
    }

    protected void resetCooldown() {
        this.cooldown = this.adjustedTickDelay(20);
    }

    protected boolean isCooledDown() {
        return this.cooldown <= 0;
    }

    protected boolean canAttack(final LivingEntity target) {
        return this.isCooledDown() && this.mob.isWithinMeleeAttackRange(target) && this.mob.getSensing().hasLineOfSight(target);
    }

}
