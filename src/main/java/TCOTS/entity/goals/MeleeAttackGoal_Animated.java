package TCOTS.entity.goals;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.util.Hand;
import software.bernie.geckolib.animatable.GeoEntity;

import java.util.EnumSet;

public class MeleeAttackGoal_Animated extends Goal {
    protected final PathAwareEntity mob;
    public final double speed;
    private final boolean pauseWhenMobIdle;
    private Path path;
    private double targetX;
    private double targetY;
    private double targetZ;
    private int updateCountdownTicks;
    private int cooldown;
    private long lastUpdateTime;
    private final int attackAnimationsNumber;

    public MeleeAttackGoal_Animated(PathAwareEntity mob, double speed, boolean pauseWhenMobIdle) {
        this(mob, speed, pauseWhenMobIdle, 3);
    }

    public MeleeAttackGoal_Animated(PathAwareEntity mob, double speed, boolean pauseWhenMobIdle, int attackAnimationsNumber) {
        if (!(mob instanceof GeoEntity)) {
            throw new IllegalArgumentException("MeleeAttackGoal_Animated requires Mob implements GeoEntity");
        }
        this.mob = mob;
        this.speed = speed;
        this.pauseWhenMobIdle = pauseWhenMobIdle;
        this.setControls(EnumSet.of(Goal.Control.MOVE, Goal.Control.LOOK));
        this.attackAnimationsNumber=attackAnimationsNumber;
    }

    @Override
    public boolean canStart() {
        long l = this.mob.getWorld().getTime();
        if (l - this.lastUpdateTime < 20L) {
            return false;
        }
        this.lastUpdateTime = l;
        LivingEntity livingEntity = this.mob.getTarget();
        if (livingEntity == null) {
            return false;
        }
        if (!livingEntity.isAlive()) {
            return false;
        }
        this.path = this.mob.getNavigation().findPathTo(livingEntity, 0);
        if (this.path != null) {
            return true;
        }
        return this.mob.isInAttackRange(livingEntity);
    }

    @Override
    public boolean shouldContinue() {
        LivingEntity livingEntity = this.mob.getTarget();
        if (livingEntity == null) {
            return false;
        }
        if (!livingEntity.isAlive()) {
            return false;
        }
        if (!this.pauseWhenMobIdle) {
            return !this.mob.getNavigation().isIdle();
        }
        if (!this.mob.isInWalkTargetRange(livingEntity.getBlockPos())) {
            return false;
        }
        return !(livingEntity instanceof PlayerEntity) || !livingEntity.isSpectator() && !((PlayerEntity)livingEntity).isCreative();
    }

    @Override
    public void start() {
        this.mob.getNavigation().startMovingAlong(this.path, this.speed);
        this.mob.setAttacking(true);
        this.updateCountdownTicks = 0;
        this.cooldown = 0;
    }

    @Override
    public void stop() {
        LivingEntity livingEntity = this.mob.getTarget();
        if (!EntityPredicates.EXCEPT_CREATIVE_OR_SPECTATOR.test(livingEntity)) {
            this.mob.setTarget(null);
        }
        this.mob.setAttacking(false);
        this.mob.getNavigation().stop();
    }

    @Override
    public boolean shouldRunEveryTick() {
        return true;
    }

    @Override
    public void tick() {
        LivingEntity livingEntity = this.mob.getTarget();
        if (livingEntity == null) {
            return;
        }
        this.mob.getLookControl().lookAt(livingEntity, 30.0f, 30.0f);
        this.updateCountdownTicks = Math.max(this.updateCountdownTicks - 1, 0);
        if ((this.pauseWhenMobIdle || this.mob.getVisibilityCache().canSee(livingEntity)) && this.updateCountdownTicks <= 0 && (this.targetX == 0.0 && this.targetY == 0.0 && this.targetZ == 0.0 || livingEntity.squaredDistanceTo(this.targetX, this.targetY, this.targetZ) >= 1.0 || this.mob.getRandom().nextFloat() < 0.05f)) {
            this.targetX = livingEntity.getX();
            this.targetY = livingEntity.getY();
            this.targetZ = livingEntity.getZ();
            this.updateCountdownTicks = 4 + this.mob.getRandom().nextInt(7);
            double d = this.mob.squaredDistanceTo(livingEntity);
            if (d > 1024.0) {
                this.updateCountdownTicks += 10;
            } else if (d > 256.0) {
                this.updateCountdownTicks += 5;
            }
            if (!this.mob.getNavigation().startMovingTo(livingEntity, this.speed)) {
                this.updateCountdownTicks += 15;
            }
            this.updateCountdownTicks = this.getTickCount(this.updateCountdownTicks);
        }
        this.cooldown = Math.max(this.cooldown - 1, 0);
        this.attack(livingEntity);
    }

    protected void attack(LivingEntity target) {
        if (this.canAttack(target)) {
            this.resetCooldown();
            this.mob.swingHand(Hand.MAIN_HAND);
            this.mob.getRandom().nextBetween(0,1);

            //Triggers the Animation
            if(this.mob instanceof GeoEntity geo) {
                int randomAttack;
                switch (this.attackAnimationsNumber){
                    //Two attack animations
                    case 2:
                        randomAttack = this.mob.getRandom().nextBetween(0, 1);
                        if (randomAttack == 0) {
                            geo.triggerAnim("AttackController", "attack1");
                        } else {
                            geo.triggerAnim("AttackController", "attack2");
                        }

                        break;

                    //Three attack animations
                    case 3:
                        randomAttack = this.mob.getRandom().nextBetween(0, 2);
                        if (randomAttack == 0) {
                            geo.triggerAnim("AttackController", "attack1");
                        } else if (randomAttack == 1) {
                            geo.triggerAnim("AttackController", "attack2");
                        } else {
                            geo.triggerAnim("AttackController", "attack3");
                        }

                        break;

                    default:
                        break;
                }
            }

            this.mob.tryAttack(target);
        }
    }

    protected void resetCooldown() {
        this.cooldown = this.getTickCount(20);
    }

    protected boolean isCooledDown() {
        return this.cooldown <= 0;
    }

    protected boolean canAttack(LivingEntity target) {
        return this.isCooledDown() && this.mob.isInAttackRange(target) && this.mob.getVisibilityCache().canSee(target);
    }

}
