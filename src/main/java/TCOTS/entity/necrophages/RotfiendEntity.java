package TCOTS.entity.necrophages;

import TCOTS.sounds.TCOTS_Sounds;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

import java.util.EnumSet;

public class RotfiendEntity extends Necrophage_Base implements GeoEntity {

    //TODO: Add Emerging animation (And digging?)
    //TODO: Add drops
    //TODO: Add spawning

    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    public static final RawAnimation IDLE = RawAnimation.begin().thenLoop("idle");
    public static final RawAnimation RUNNING = RawAnimation.begin().thenLoop("move.running");
    public static final RawAnimation WALKING = RawAnimation.begin().thenLoop("move.walking");
    public static final RawAnimation ATTACK1 = RawAnimation.begin().thenPlay("attack.swing1");
    public static final RawAnimation ATTACK2 = RawAnimation.begin().thenPlay("attack.swing2");
    public static final RawAnimation ATTACK3 = RawAnimation.begin().thenPlay("attack.swing3");
    public static final RawAnimation LUNGE = RawAnimation.begin().thenPlay("attack.lunge");

    public static final RawAnimation EXPLOSION = RawAnimation.begin().thenPlayAndHold("special.explosion");
    public static final RawAnimation DIGGING_OUT = RawAnimation.begin().thenPlayAndHold("special.diggingOut");
    public static final RawAnimation DIGGING_IN = RawAnimation.begin().thenPlayAndHold("special.diggingIn");

    protected static final TrackedData<Boolean> LUGGING = DataTracker.registerData(RotfiendEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    protected static final TrackedData<Boolean> EXPLODING = DataTracker.registerData(RotfiendEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    protected static final TrackedData<Boolean> TRIGGER_EXPLOSION = DataTracker.registerData(RotfiendEntity.class, TrackedDataHandlerRegistry.BOOLEAN);



    public RotfiendEntity(EntityType<? extends RotfiendEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void initGoals() {
        //Attack

        //Emerge from ground
//        this.goalSelector.add(0, new DrownerEntity.Drowner_EmergeFromGround(this));
        this.goalSelector.add(0, new Rotfiend_Explosion(this));
        this.goalSelector.add(1, new SwimGoal(this));


        this.goalSelector.add(2, new RotfiendEntity.Attack_Lunge(this, 100, 0.6));

        //Returns to ground
//        this.goalSelector.add(2, new DrownerEntity.Drowner_ReturnToGround(this));

        this.goalSelector.add(3, new MeleeAttackGoal(this, 1.2D, false));


        this.goalSelector.add(4, new WanderAroundGoal(this, 0.75f, 20));

        this.goalSelector.add(5, new LookAroundGoal(this));

        //Objectives
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, PlayerEntity.class, true));
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, MerchantEntity.class, true));
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, IronGolemEntity.class, true));
    }

    public boolean cooldownBetweenLunges = false;
    public int LungeTicks;

    private class Attack_Lunge extends Goal {
        private final RotfiendEntity mob;
        private final int cooldownBetweenLungesAttacks;
        private final double SpeedLungeMultiplier;

        private Attack_Lunge(RotfiendEntity mob, int cooldownBetweenLungesAttacks, double lungeImpulse) {
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
                return !RotfiendEntity.this.cooldownBetweenLunges && this.mob.isAttacking()
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
                return !RotfiendEntity.this.cooldownBetweenLunges && this.mob.isAttacking()
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
            if (!RotfiendEntity.this.cooldownBetweenLunges) {
                //Makes the lunge
                //Extra random ticks in cooldown
                randomExtra = RotfiendEntity.this.random.nextInt(51);
                //0.35 Y default
                vec3D_lunge = getVec3d(target).normalize();
                RotfiendEntity.this.setIsLugging(true);

                RotfiendEntity.this.setVelocity(RotfiendEntity.this.getVelocity().add(vec3D_lunge.x, 0.35, vec3D_lunge.z));
                this.mob.getLookControl().lookAt(target, 30.0F, 30.0F);

                RotfiendEntity.this.playSound(TCOTS_Sounds.ROTFIEND_LUNGE, 1.0F, 1.0F);

                //Put the cooldown
                LungeTicks = cooldownBetweenLungesAttacks + randomExtra;
                RotfiendEntity.this.cooldownBetweenLunges = true;

            }

        }

    }

    private class Rotfiend_Explosion extends Goal {
        int AnimationTicks = 30;

        private final RotfiendEntity rotfiend;

        private Rotfiend_Explosion(RotfiendEntity mob) {
            this.rotfiend = mob;
            this.setControls(EnumSet.of(Goal.Control.MOVE));
        }

        @Override
        public boolean canStart() {
            return (RotfiendEntity.this.getHealth() < (RotfiendEntity.this.getMaxHealth() * 0.5) && !rotfiend.isOnFire());
        }

        @Override
        public void start() {
            this.rotfiend.playSound(TCOTS_Sounds.ROTFIEND_EXPLODING, 1.0F, 1.0F);
            this.rotfiend.setIsExploding(true);
            rotfiend.getNavigation().stop();
            rotfiend.getLookControl().lookAt(0, 0, 0);
            AnimationTicks = 30;
        }

        @Override
        public void tick() {
            if (AnimationTicks > 0) {
//                System.out.println("AnimationTicks: " + AnimationTicks);
                --AnimationTicks;
            } else {
                stop();
            }
        }

        @Override
        public void stop() {
            rotfiend.setIsTriggerExplosion(true);
        }
    }

    public static DefaultAttributeContainer.Builder setAttributes() {
        return AnimalEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 20.0D)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 3.0f) //Amount of health that hurts you
//                .add(EntityAttributes.GENERIC_ATTACK_SPEED, 2.0f)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.28f);
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
//                            //It's in the ground and not emerging
//                            if(this.getInGroundDataTracker() && !this.getIsEmerging()){
//                                state.setAnimation(DIGGING_IN);
//                                return PlayState.CONTINUE;
//                            }else{
                        return state.setAndContinue(IDLE);
//                            }
                    }
                }
            }
//                        }

        }
        ));

        //Attack Controller
        controllerRegistrar.add(
                new AnimationController<>(this, "AttackController", 1, state -> {
                    state.getController().forceAnimationReset();
                    // Random instance
                    // Generates three random numbers
                    if (this.handSwinging) {
                        int r = RotfiendEntity.this.random.nextInt(3);
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

        //Explosion Controller
        controllerRegistrar.add(
                new AnimationController<>(this, "ExplosionController", 1, state -> {
                    if (this.getIsExploding()) {
                        state.setAnimation(EXPLOSION);
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
        this.dataTracker.startTracking(EXPLODING, Boolean.FALSE);
        this.dataTracker.startTracking(TRIGGER_EXPLOSION, Boolean.FALSE);
//        this.dataTracker.startTracking(InGROUND, Boolean.FALSE);
//        this.dataTracker.startTracking(EMERGING, Boolean.FALSE);

    }

    public final boolean getIsLugging() {
        return this.dataTracker.get(LUGGING);
    }

    public final void setIsLugging(boolean wasLugging) {
        this.dataTracker.set(LUGGING, wasLugging);
    }

    public final boolean getIsExploding() {
        return this.dataTracker.get(EXPLODING);
    }

    public final void setIsExploding(boolean wasExploding) {
        this.dataTracker.set(EXPLODING, wasExploding);
    }

    public final boolean getIsTriggerExplosion() {
        return this.dataTracker.get(TRIGGER_EXPLOSION);
    }

    public final void setIsTriggerExplosion(boolean wasExplosion) {
        this.dataTracker.set(TRIGGER_EXPLOSION, wasExplosion);
    }

    @Override
    public void onTrackedDataSet(TrackedData<?> data) {
        super.onTrackedDataSet(data);
    }

    @Override
    public void tick() {
        //Start the counter for the Lunge attack
        if (RotfiendEntity.this.LungeTicks > 0) {
            RotfiendEntity.this.setIsLugging(false);
            --RotfiendEntity.this.LungeTicks;
        } else {
            RotfiendEntity.this.cooldownBetweenLunges = false;
        }

        //Particles when return to ground
//        if(this.AnimationParticlesTicks > 0 && this.getInGroundDataTracker()){
//            this.spawnGroundParticles();
//            --this.AnimationParticlesTicks;
//        }

//        if(!this.getInGroundDataTracker() && !this.getIsEmerging()){
//            AnimationParticlesTicks=36;
//        }

        //Particles when emerges from ground
//        if(this.getIsEmerging()){
//            this.spawnGroundParticles();
//        }
        if (this.getIsTriggerExplosion()) {
            this.explode();
        }
        super.tick();
    }

//    @Override
//    public void mobTick(){
//
//        super.mobTick();
//    }


    private void explode() {
        if (!this.getWorld().isClient) {
            this.dead = true;
            this.getWorld().createExplosion(this, this.getX(), this.getY(), this.getZ(), (float) 3, World.ExplosionSourceType.MOB);
            this.discard();
        }
    }

    public int ticksSinceDeath=60;
    @Override
    protected void updatePostDeath() {
        if(!this.isOnFire()) {
            if (ticksSinceDeath == 60) {
                this.playSound(TCOTS_Sounds.ROTFIEND_EXPLODING, 1.0F, 1.0F);
                this.setIsExploding(true);
                this.getNavigation().stop();
                this.getLookControl().lookAt(0, 0, 0);
            }
            if (ticksSinceDeath > 0) {
                --this.ticksSinceDeath;
            } else {
                this.setIsTriggerExplosion(true);
            }
        }
        else {
            super.updatePostDeath();
        }
    }

    private void spawnGroundParticles() {
        Random random = this.getRandom();
        BlockState blockState = this.getSteppingBlockState();
        if (blockState.getRenderType() != BlockRenderType.INVISIBLE) {
            for (int i = 0; i < 11; ++i) {
                double d = this.getX() + (double) MathHelper.nextBetween(random, -0.7F, 0.7F);
                double e = this.getY();
                double f = this.getZ() + (double) MathHelper.nextBetween(random, -0.7F, 0.7F);

                if (i == 5
                ) {
                    this.getWorld().addParticle(ParticleTypes.SPLASH, d, e, f, 0.0, 0.0, 0.0);
                } else {
                    this.getWorld().addParticle(new BlockStateParticleEffect(ParticleTypes.BLOCK, blockState), d, e, f, 0.0, 0.0, 0.0);

                }

            }
        }
    }


    @Override
    public boolean canExplosionDestroyBlock(Explosion explosion, BlockView world, BlockPos pos, BlockState state, float explosionPower) {
        return false;
    }

    //Sounds
    @Override
    protected SoundEvent getAmbientSound() {
        if (!this.getIsExploding()) {
            return TCOTS_Sounds.ROTFIEND_IDLE;
        } else {
            return null;
        }
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return TCOTS_Sounds.ROTFIEND_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        if (!this.isOnFire()) {
            return null;
        } else {
            return TCOTS_Sounds.ROTFIEND_DEATH;
        }
    }

    //Attack Sound
    @Override
    public boolean tryAttack(Entity target) {
        this.playSound(TCOTS_Sounds.ROTFIEND_ATTACK, 1.0F, 1.0F);
        return super.tryAttack(target);
    }

    @Override
    public boolean isInvulnerableTo(DamageSource damageSource) {
        return this.getIsExploding() || super.isInvulnerableTo(damageSource);
    }

    @Override
    public boolean isPushable() {
        return !this.getIsExploding();
    }

    boolean killedWithoutFire = true;



    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
