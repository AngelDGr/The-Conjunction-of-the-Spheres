package TCOTS.entity.ogroids;

import TCOTS.entity.geo.renderer.ogroids.NekkerRenderer;
import TCOTS.entity.necrophages.RotfiendEntity;
import TCOTS.sounds.TCOTS_Sounds;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

import java.util.EnumSet;

public class NekkerEntity extends Ogroid_Base implements GeoEntity {

    //TODO: Add spawn
    //xTODO: Add drops

    public static final RawAnimation IDLE = RawAnimation.begin().thenLoop("idle");
    public static final RawAnimation RUNNING = RawAnimation.begin().thenLoop("move.running");
    public static final RawAnimation WALKING = RawAnimation.begin().thenLoop("move.walking");
    public static final RawAnimation ATTACK1 = RawAnimation.begin().thenPlay("attack.swing1");
    public static final RawAnimation ATTACK2 = RawAnimation.begin().thenPlay("attack.swing2");
    public static final RawAnimation ATTACK3 = RawAnimation.begin().thenPlay("attack.swing3");
    public static final RawAnimation LUNGE = RawAnimation.begin().thenPlay("attack.lunge");

    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    protected static final TrackedData<Boolean> LUGGING = DataTracker.registerData(NekkerEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    public NekkerEntity(EntityType<? extends NekkerEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void initGoals() {
        //Attack

        //Emerge from ground
//        this.goalSelector.add(0, new DrownerEntity.Drowner_EmergeFromGround(this));
//        this.goalSelector.add(0, new RotfiendEntity.Rotfiend_EmergeFromGround(this));
        this.goalSelector.add(1, new SwimGoal(this));


        this.goalSelector.add(2, new NekkerEntity.Attack_Lunge(this, 200, 1.2));

        //Returns to ground
//        this.goalSelector.add(3, new RotfiendEntity.Rotfiend_ReturnToGround(this));


        this.goalSelector.add(4, new MeleeAttackGoal(this, 1.2D, false));


        this.goalSelector.add(5, new WanderAroundGoal(this, 0.75f, 20));

        this.goalSelector.add(6, new LookAroundGoal(this));

        //Objectives
        this.targetSelector.add(1, new RevengeGoal(this, new Class[0]).setGroupRevenge(new Class[0]));
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, PlayerEntity.class, true));
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, MerchantEntity.class, true));
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, IronGolemEntity.class, true));
    }

    public boolean cooldownBetweenLunges = false;
    public int LungeTicks;

    private class Attack_Lunge extends Goal {
        private final NekkerEntity mob;
        private final int cooldownBetweenLungesAttacks;
        private final double SpeedLungeMultiplier;

        private Attack_Lunge(NekkerEntity mob, int cooldownBetweenLungesAttacks, double lungeImpulse) {
            this.mob = mob;
            this.cooldownBetweenLungesAttacks = cooldownBetweenLungesAttacks;
            this.setControls(EnumSet.of(Control.MOVE, Control.JUMP));
            this.SpeedLungeMultiplier = lungeImpulse;
        }

        @Override
        public boolean canStart() {
            LivingEntity target = this.mob.getTarget();
            if (target != null) {
                //5 square distance like 1.5 blocks approx
                //I want 7.5 blocks approx
                //So 7.5/1.5=5
                return !NekkerEntity.this.cooldownBetweenLunges && this.mob.isAttacking()
                        && this.mob.squaredDistanceTo(target) > 5 && this.mob.squaredDistanceTo(target) < 25
                        && (this.mob.getTarget().getY() - this.mob.getY()) <= 1
//                        && !this.mob.getIsEmerging()
//                        && !this.mob.getInGroundDataTracker()
                        ;
            } else {
                return false;
            }
        }

        @Override
        public boolean shouldContinue() {
            LivingEntity target = this.mob.getTarget();
            if (target != null) {
                //5 square distance like 1.5 blocks approx
                //I want 7.5 blocks approx
                //So 7.5/1.5=5
                return !NekkerEntity.this.cooldownBetweenLunges && this.mob.isAttacking()
                        && this.mob.squaredDistanceTo(target) > 5 && this.mob.squaredDistanceTo(target) < 25
                        && (this.mob.getTarget().getY() - this.mob.getY()) <= 1;
            } else {
                return false;
            }
        }

        Vec3d vec3D_lunge;
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
//                double d = this.mob.getSquaredDistanceToAttackPosOf(livingEntity);
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
            if (!NekkerEntity.this.cooldownBetweenLunges) {
                //Makes the lunge
                //Extra random ticks in cooldown
                randomExtra = NekkerEntity.this.random.nextInt(51);
                //0.35 Y default
                vec3D_lunge = getVec3d(target).normalize();
                NekkerEntity.this.setIsLugging(true);

                NekkerEntity.this.setVelocity(NekkerEntity.this.getVelocity().add(vec3D_lunge.x, 0.35, vec3D_lunge.z));
                this.mob.getLookControl().lookAt(target, 30.0F, 30.0F);

                NekkerEntity.this.playSound(TCOTS_Sounds.NEKKER_LUNGE, 1.0F, 1.0F);

                //Put the cooldown
                LungeTicks = cooldownBetweenLungesAttacks + randomExtra;
                NekkerEntity.this.cooldownBetweenLunges = true;
            }

        }

    }

    public static DefaultAttributeContainer.Builder setAttributes() {
        return AnimalEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 14.0D)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 2.0f) //Amount of health that hurts you
                .add(EntityAttributes.GENERIC_ATTACK_SPEED, 0.5f)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.26f)

                .add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK, 0.2f)
                .add(EntityAttributes.GENERIC_ARMOR,2f);

    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        //Walk/Idle Controller
        controllerRegistrar.add(new AnimationController<>(this, "Idle/Walk", 5, state -> {

            //If it's aggressive and it is moving
            if (this.isAttacking() && state.isMoving()) {
                return state.setAndContinue(RUNNING);
            }
            //It's not attacking and/or it's no moving
            else {
                //If it's attacking but NO moving
                if (isAttacking()) {
                    return state.setAndContinue(RUNNING);
                } else {
                    //If it's just moving
                    if (state.isMoving()) {
                        return state.setAndContinue(WALKING);
                    }
                    //Anything else
                    else {

                        return state.setAndContinue(IDLE);
                    }
                }
            }
        }
        ));

        //Attack Controller
        controllerRegistrar.add(
                new AnimationController<>(this, "AttackController", 1, state -> {
                    state.getController().forceAnimationReset();
                    // Random instance
                    // Generates three random numbers
                    if (this.handSwinging) {
                        int r = NekkerEntity.this.random.nextInt(3);
                        switch (r) {
                            case 0:
                                return state.setAndContinue(ATTACK1);

                            case 1:
                                return state.setAndContinue(ATTACK2);

                            case 2:
                                return state.setAndContinue(ATTACK3);
                        }
                    }
                    return PlayState.CONTINUE;
                })
        );

        //Lunge Controller
        controllerRegistrar.add(
                new AnimationController<>(this, "LungeController", 1, state -> {
                    if (this.getIsLugging()) {
                        state.setAnimation(LUNGE);
                        return PlayState.CONTINUE;
                    }

                    state.getController().forceAnimationReset();
                    return PlayState.CONTINUE;
                })
        );
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(LUGGING, Boolean.FALSE);
//        this.dataTracker.startTracking(TRIGGER_EXPLOSION, Boolean.FALSE);
//        this.dataTracker.startTracking(InGROUND, Boolean.FALSE);
//        this.dataTracker.startTracking(EMERGING, Boolean.FALSE);
    }


    public final boolean getIsLugging() {
        return this.dataTracker.get(LUGGING);
    }

    public final void setIsLugging(boolean wasLugging) {
        this.dataTracker.set(LUGGING, wasLugging);
    }


    @Override
    public void tick() {
        //Start the counter for the Lunge attack
        if (NekkerEntity.this.LungeTicks > 0) {
            NekkerEntity.this.setIsLugging(false);
            --NekkerEntity.this.LungeTicks;
        } else {
            NekkerEntity.this.cooldownBetweenLunges = false;
        }

//        //Particles when return to ground
//        if(this.AnimationParticlesTicks > 0 && this.getInGroundDataTracker()){
//            this.spawnGroundParticles();
//            --this.AnimationParticlesTicks;
//        }
//
//        //Particles when emerges from ground
//        if(this.getIsEmerging()){
//            this.spawnGroundParticles();
//        }
        super.tick();
    }

    //Sounds
    @Override
    protected SoundEvent getAmbientSound() {
        return TCOTS_Sounds.NEKKER_IDLE;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return TCOTS_Sounds.NEKKER_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return TCOTS_Sounds.NEKKER_DEATH;
    }

    //Attack Sound
    @Override
    public boolean tryAttack(Entity target) {
        this.playSound(TCOTS_Sounds.NEKKER_ATTACK, 1.0F, 1.0F);
        return super.tryAttack(target);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}