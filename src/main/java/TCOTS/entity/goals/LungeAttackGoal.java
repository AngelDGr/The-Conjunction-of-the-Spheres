package TCOTS.entity.goals;

import TCOTS.entity.interfaces.ExcavatorMob;
import TCOTS.entity.interfaces.LungeMob;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoEntity;

import java.util.EnumSet;

public class LungeAttackGoal extends Goal {
    private final PathAwareEntity mob;
    private final LungeMob lungeMob;
    private final ExcavatorMob excavatorMob;
    private final int cooldownBetweenLungesAttacks;
    private final double SpeedLungeMultiplier;
    private final int minDistance;
    private final int maxDistance;

    public LungeAttackGoal(PathAwareEntity mob, int cooldownBetweenLungesAttacks, double lungeImpulse, int minDistance, int maxDistance) {
        this.mob = mob;
        if(mob instanceof ExcavatorMob){
            this.excavatorMob= (ExcavatorMob) mob;
        }
        else {this.excavatorMob=null;}

        if (!(mob instanceof LungeMob)) {
            throw new IllegalArgumentException("LungeAttackGoal requires Mob implements LungeMob");
        }

        this.lungeMob= (LungeMob) mob;

        this.cooldownBetweenLungesAttacks = cooldownBetweenLungesAttacks;
        this.setControls(EnumSet.of(Control.MOVE, Control.JUMP));
        this.SpeedLungeMultiplier = lungeImpulse;
        this.minDistance=minDistance;
        this.maxDistance=maxDistance;
    }

    @Override
    public boolean canStart() {
        LivingEntity target = this.mob.getTarget();
        if (target != null) {
            //5 square distance like 1.5 blocks approx
            //I want 7.5 blocks approx
            //So 7.5/1.5=5
            return lungeMob.getNotCooldownBetweenLunges() && this.mob.isAttacking()
                    && this.mob.squaredDistanceTo(target) > minDistance && this.mob.squaredDistanceTo(target) < maxDistance
                    && (this.mob.getTarget().getY() - this.mob.getY()) <= 1
                    && isExcavator();
        } else {
            return false;
        }
    }

    private boolean isExcavator(){
        if(excavatorMob != null){
            return !this.excavatorMob.getIsEmerging() && !this.excavatorMob.getInGround();
        }

        return true;
    }
    int randomExtra;

    @Override
    public boolean shouldRunEveryTick() {
        return true;
    }

    @Override
    public void start() {
        this.mob.getNavigation().stop();
    }

    @Override
    public void tick() {
        LivingEntity livingEntity = this.mob.getTarget();

        if (livingEntity != null) {
            LungeAttack(livingEntity);
        }
    }

    @NotNull
    private Vec3d getVec3d(LivingEntity target) {
        double dXtoTarget = target.getX() - this.mob.getEyePos().x;
        double dYtoTarget = target.getY() - this.mob.getEyePos().y;
        double dZtoTarget = target.getZ() - this.mob.getEyePos().z;
        double length = Math.sqrt(dXtoTarget * dXtoTarget + dYtoTarget * dYtoTarget + dZtoTarget * dZtoTarget);

        //Movement Vector
        return new Vec3d((dXtoTarget / length) * SpeedLungeMultiplier,
                (dYtoTarget / length),
                (dZtoTarget / length) * SpeedLungeMultiplier);
    }

    private void LungeAttack(LivingEntity target) {
        //Check if it can do a lunge
        if (lungeMob.getNotCooldownBetweenLunges()) {
            //Makes the lunge
            //Extra random ticks in cooldown
            randomExtra = mob.getRandom().nextInt(51);

            Vec3d vec3D_lunge = getVec3d(target).normalize();
            if(lungeMob instanceof GeoEntity){
                ((GeoEntity) lungeMob).triggerAnim("LungeController","lunge");
            }

            //Vanilla way
//            Vec3d vec3d = this.mob.getVelocity();
//            Vec3d vec3d2 = new Vec3d(target.getX() - this.mob.getX(), 0.0, target.getZ() - this.mob.getZ());
//            if (vec3d2.lengthSquared() > 1.0E-7) {
//                vec3d2 = vec3d2.normalize().multiply(0.4).add(vec3d.multiply(SpeedLungeMultiplier));
//            }
//            this.mob.setVelocity(vec3d2.x, 0.35, vec3d2.z);

            //0.35 Y default
            mob.setVelocity(mob.getVelocity().add(vec3D_lunge.x, 0.35, vec3D_lunge.z));
            this.mob.getLookControl().lookAt(target, 30.0F, 30.0F);

            if(lungeMob.getLungeSound() != null) {
                mob.playSound(lungeMob.getLungeSound(), 1.0F, 1.0F);
            }
            //Put the cooldown
            lungeMob.setLungeTicks(cooldownBetweenLungesAttacks + randomExtra);
            lungeMob.setCooldownBetweenLunges(true);
        }

    }
}
