package TCOTS.entity.necrophages;

import TCOTS.entity.TCOTS_Entities;
import TCOTS.sounds.TCOTS_Sounds;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.EntityType;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.entity.ai.control.YawAdjustingLookControl;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.ai.pathing.MobNavigation;
import net.minecraft.entity.ai.pathing.SwimNavigation;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;


import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class DrownerEntity extends HostileEntity implements GeoEntity {
    protected final SwimNavigation waterNavigation;
    protected final MobNavigation landNavigation;
    protected static final TrackedData<Boolean> SWIM = DataTracker.registerData(DrownerEntity.class, TrackedDataHandlerRegistry.BOOLEAN);;
//    static{
//        SWIM = DataTracker.registerData(DrownerEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
//    }
    public boolean swimming_Progress=false;
    public DrownerEntity(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
        this.moveControl = new DolphinOrLand_MoveControl(this, 85, 70, 100.0F, 0.1F, true);
        this.lookControl = new YawAdjustingLookControl(this, 10);
        this.waterNavigation = new SwimNavigation(this, world);
        this.landNavigation = new MobNavigation(this, world);
    }
    public static final RawAnimation IDLE = RawAnimation.begin().thenLoop("idle");
    public static final RawAnimation WATER_IDLE = RawAnimation.begin().thenLoop("idle.water");
    public static final RawAnimation RUNNING= RawAnimation.begin().thenLoop("move.running");
    public static final RawAnimation WALKING = RawAnimation.begin().thenLoop("move.walking");
    public static final RawAnimation SWIMMING= RawAnimation.begin().thenLoop("move.swimming");
    public static final RawAnimation ATTACK1 = RawAnimation.begin().thenPlay("attack.swing1");
    public static final RawAnimation ATTACK2 = RawAnimation.begin().thenPlay("attack.swing2");
    public static final RawAnimation WATER_ATTACK1 = RawAnimation.begin().thenPlay("attack.waterswing1");
    public static final RawAnimation WATER_ATTACK2 = RawAnimation.begin().thenPlay("attack.waterswing2");

    //MoveControl-
    private static class DolphinOrLand_MoveControl extends MoveControl {
        private final int pitchChange;
        private final int yawChange;
        private final float speedInWater;
        private final float speedInAir;
        private final boolean buoyant;
        public DolphinOrLand_MoveControl(DrownerEntity entity, int pitchChange, int yawChange, float speedInWater, float speedInAir, boolean buoyant) {
            super(entity);
            this.pitchChange = pitchChange;
            this.yawChange = yawChange;
            this.speedInWater = speedInWater;
            this.speedInAir = speedInAir;
            this.buoyant = buoyant;
        }

        @Override
        public void tick() {
            if(entity.isSubmergedInWater() || entity.isTouchingWater()) {

                if(entity.isSubmergedInWater()){entity.getDataTracker().set(SWIM, Boolean.TRUE);

                }
                if(!entity.isSubmergedInWater()){entity.getDataTracker().set(SWIM, Boolean.FALSE);}
                LivingEntity livingEntity = entity.getTarget();
                this.entity.setMovementSpeed(2.0f);
                if(livingEntity != null && livingEntity.getY() > this.entity.getY()){
                    //Makes it go up
                    this.entity.setVelocity(this.entity.getVelocity().add(0.0, 0.01, 0.0));
                }
                if(livingEntity != null && livingEntity.getY() < this.entity.getY()){
                    //Makes it go down
                    this.entity.setVelocity(this.entity.getVelocity().add(0.0, -0.005, 0.0));
                }
            }
            //It's on land, so return the normal MoveControl
            else{

                if(!entity.isSubmergedInWater()){entity.getDataTracker().set(SWIM, Boolean.FALSE);}

            super.tick();}
        }
        private static float method_45335(float f) {
            return 1.0F - MathHelper.clamp((f - 10.0F) / 50.0F, 0.0F, 1.0F);
        }
    }


    //Dynamic hitbox
    @Override
    protected Box calculateBoundingBox() {
        if (dataTracker.get(SWIM)) {
            // Tiny hit-box when swim
            return new Box(this.getX() - 0.6, this.getY()+1.7, this.getZ() - 0.6,
                    this.getX() + 0.6, this.getY()+0.6, this.getZ() + 0.6);
        } else {
            // Normal hit-box otherwise
            return super.calculateBoundingBox();
        }
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(SWIM, Boolean.FALSE);
    }
    @Override
    public void onTrackedDataSet(TrackedData<?> data) {
        if (dataTracker.get(SWIM)) {
            this.setBoundingBox(this.calculateBoundingBox());
        }
        super.onTrackedDataSet(data);
    }

    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);
    public static DefaultAttributeContainer.Builder setAttributes() {
        return AnimalEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 20.0D)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 4.0f) //Amount of health that hurts you
//                .add(EntityAttributes.GENERIC_ATTACK_SPEED, 2.0f)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.27f);
    }
    @Override
    protected void initGoals() {
//        this.goalSelector.add(1, new SwimGoal(this));

//        this.goalSelector.add(1,new SwimAroundGoal(this, 1.0,10));
//        this.goalSelector.add(1, new MoveIntoWaterGoal(this));
//this.getDimensions().scaled().scaled()
        this.goalSelector.add(1, new MeleeAttackGoal(this, 1.2D, false));

        this.goalSelector.add(2, new SwimAroundGoal(this, 1.0, 10));
        this.goalSelector.add(3, new WanderAroundFarGoal(this, 0.75f, 1));


        this.goalSelector.add(4, new LookAroundGoal(this));

        this.targetSelector.add(2, new ActiveTargetGoal<>(this, PlayerEntity.class, true));
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, MerchantEntity.class, true));
//        this.targetSelector.add(3, new ActiveTargetGoal<>(this, ChickenEntity.class, true));
    }

    //Each Control can only play one animation at the time
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "Idle/Walk", 5,
                state->{

                    if(this.isSubmergedInWater()){
                        if(state.isMoving()){
                            state.setAndContinue(SWIMMING);
                        }
                        else{
                            state.setAndContinue(WATER_IDLE);
                        }
                    }
                    else{
                    //If it's aggressive and it is moving
                    if(this.isAttacking() && state.isMoving()){
                        state.setAndContinue(RUNNING);
                    }
                    //It's not attacking and/or it's no moving
                    else{
                        //If it's attacking but NO moving
                        if(isAttacking()){
                            state.setAndContinue(RUNNING);
                        }
                        else {
                            //If it's just moving
                            if (state.isMoving()) {
                                state.setAndContinue(WALKING);
                            }
                            //Anything else
                            else {
                                state.setAndContinue(IDLE);
                            }
                        }
                    }
                    return PlayState.CONTINUE;
                    }
                    return PlayState.CONTINUE;
                }
        ));

        //Attack Controller
        controllerRegistrar.add(
         new AnimationController<>(this, "AttackController", 2, state -> {
            if (this.handSwinging){
                // Random instance
                Random random = new Random();
                // Generates two random numbers
                int r = ThreadLocalRandom.current().nextInt(1, 3);
                if(this.isSubmergedInWater()){
                    if (r == 1) {
                        state.setAndContinue(WATER_ATTACK1);
                    } else {
                        state.setAndContinue(WATER_ATTACK2);
                    }
                }
                else {
                    if (r == 1) {
                        state.setAndContinue(ATTACK1);
                    } else {
                        state.setAndContinue(ATTACK2);
                    }
                }
                state.getController().forceAnimationReset();
                return PlayState.CONTINUE;
            }
             return PlayState.CONTINUE;
            })
        );
    }

    //Sounds
    @Override
    protected SoundEvent getAmbientSound() {
        return TCOTS_Sounds.DROWNER_IDLE;
    }
    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return TCOTS_Sounds.DROWNER_HURT;
    }
    @Override
    protected SoundEvent getDeathSound() {
        return TCOTS_Sounds.DROWNER_DEATH;
    }

        //Footsteps sounds
        protected SoundEvent getStepSound() {
            return TCOTS_Sounds.DROWNER_FOOTSTEP;
        }
        protected void playStepSound(BlockPos pos, BlockState state) {
            this.playSound(this.getStepSound(), 0.15F, 1.0F);
        }

    //Attack Sound
    @Override
    public boolean tryAttack(Entity target) {
        this.playSound(TCOTS_Sounds.DROWNER_ATTACK, 1.0F, 1.0F);
        return super.tryAttack(target);
    }

    //Water creature
    @Override
    public boolean canBreatheInWater() {
        return true;
    }
    @Override
    public boolean isPushedByFluids() {
        return false;
    }
    @Override
    public EntityGroup getGroup() {
        return TCOTS_Entities.NECROPHAGES;
    }
    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
