package TCOTS.entity.necrophages;

import TCOTS.sounds.TCOTS_Sounds;
import net.minecraft.block.BlockState;
import net.minecraft.command.argument.EntityAnchorArgumentType;
import net.minecraft.entity.*;

import net.minecraft.entity.ai.brain.task.LookTargetUtil;
import net.minecraft.entity.ai.control.LookControl;
import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.ai.pathing.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;


import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;

import net.minecraft.entity.raid.RaiderEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;


import java.util.Objects;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class DrownerEntity extends Necrophage_Base implements GeoEntity {
    protected final SwimNavigation waterNavigation;
    protected final MobNavigation landNavigation;
    public static final RawAnimation IDLE = RawAnimation.begin().thenLoop("idle");
    public static final RawAnimation WATER_IDLE = RawAnimation.begin().thenLoop("idle.water");
    public static final RawAnimation RUNNING= RawAnimation.begin().thenLoop("move.running");
    public static final RawAnimation WALKING = RawAnimation.begin().thenLoop("move.walking");
    public static final RawAnimation SWIMMING= RawAnimation.begin().thenLoop("move.swimming");
    public static final RawAnimation ATTACK1 = RawAnimation.begin().thenPlay("attack.swing1");
    public static final RawAnimation ATTACK2 = RawAnimation.begin().thenPlay("attack.swing2");
    public static final RawAnimation LUNGE = RawAnimation.begin().thenPlay("attack.lunge");
    public static final RawAnimation WATER_ATTACK1 = RawAnimation.begin().thenPlay("attack.waterswing1");
    public static final RawAnimation WATER_ATTACK2 = RawAnimation.begin().thenPlay("attack.waterswing2");
    protected static final TrackedData<Boolean> SWIM = DataTracker.registerData(DrownerEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    protected static final TrackedData<Boolean> LUGGING = DataTracker.registerData(DrownerEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    public boolean IsLugging=false;



    public DrownerEntity(EntityType<? extends DrownerEntity> entityType, World world) {
        super(entityType, world);
        this.setPathfindingPenalty(PathNodeType.WATER, 0.0F);
        this.moveControl = new WaterOrLand_MoveControl(this, 0.07F);
        this.lookControl = new DrownerLookControl(this, 90);
        this.waterNavigation = new SwimNavigation(this, world);
        this.landNavigation = new MobNavigation(this, world);
    }


    @Override
    protected void initGoals()
    {
        //Water attack, lunge and flee
        this.goalSelector.add(0, new Drowner_FleeFromTarget(this,1.0, 1));
        this.goalSelector.add(1, new Drowner_MeleeAttackGoal(this, 1.2D, false, 60, 40));

        this.goalSelector.add(2, new SwimAroundGoal(this, 1.0, 10));
        this.goalSelector.add(3, new WanderAroundFarGoal(this, 0.75f, 1));

        this.goalSelector.add(4, new LookAroundGoal(this));

        //Returns to ground

        //Objectives
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, PlayerEntity.class, true));
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, MerchantEntity.class, true));
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, IronGolemEntity.class, true));
        this.targetSelector.add(3, new ActiveTargetGoal<>(this, AxolotlEntity.class, true));
        this.targetSelector.add(3, new ActiveTargetGoal<>(this, DolphinEntity.class, true));
        this.targetSelector.add(4, new ActiveTargetGoal<>(this, FishEntity.class, true));
    }

    //Conditions if Drowner is swimming
    public boolean canDrownerUnderwaterAttackTarget(@Nullable LivingEntity target) {
        if (target != null) {
            return target.isTouchingWater();
        } else {
            return false;}
    }
    //Updates the data tracker and the navigation
    @Override
    public void updateSwimming() {
        if (!this.getWorld().isClient) {
            if (this.canMoveVoluntarily() && this.isSubmergedInWater()) {
                this.navigation = this.waterNavigation;
                if(this.isAlive()){
                    this.setSwimmingDataTracker(true);}
                this.setSwimming(true);
            } else {

                if (this.isTouchingWater() && this.getSwimmingDataTracker()) {
                    this.setSwimmingDataTracker(true);
                } else {
                    if (this.isAlive()) {
                        this.setSwimmingDataTracker(false);
                    }
                    this.setSwimming(false);
                }
                this.navigation = this.landNavigation;
            }
        }
    }
    //MoveControl
    private static class WaterOrLand_MoveControl extends MoveControl {
        private final float waterSpeed; // Velocidad de movimiento en el agua
        private final DrownerEntity drowner;
        public WaterOrLand_MoveControl(DrownerEntity entity, float waterSpeed) {
            super(entity);
            this.waterSpeed = waterSpeed;
            this.drowner=entity;
        }

        private static float method_45335(float f) {
            return 1.0F - MathHelper.clamp((f - 10.0F) / 50.0F, 0.0F, 1.0F);
        }

        public void handleUnderwaterMovement(){
            LivingEntity target = this.entity.getTarget();
            double dXtoTarget = this.targetX - this.entity.getX();
            double dYtoTarget = this.targetY - this.entity.getY();
            double dZtoTarget = this.targetZ - this.entity.getZ();
            double length = Math.sqrt(dXtoTarget * dXtoTarget + dYtoTarget * dYtoTarget + dZtoTarget * dZtoTarget);

            //Movement Vector
            Vec3d vec3D_movementUnderwater = new Vec3d((double)(dXtoTarget / length) * waterSpeed,
                                                       (double)(dYtoTarget / length) * waterSpeed,
                                                       (double)(dZtoTarget / length) * waterSpeed);

            //Makes it go up
            if(target != null
                    && target.getY() > this.entity.getY()
                    //If the target it's attackable
                    && drowner.canDrownerUnderwaterAttackTarget(target)
                    //Target it's not submerged
                    && !target.isSubmergedInWater())
            {this.entity.setVelocity(this.entity.getVelocity().add(0.0, 0.01, 0.0));}

            //It's not underwater but it's not in land
            if(!entity.isOnGround() && entity.isTouchingWater() && !entity.isSubmergedInWater()) {
                //It's attacking in the water, but it's not submerged
                if (drowner.canDrownerUnderwaterAttackTarget(target)) {
                    this.entity.setVelocity(this.entity.getVelocity().add(0.0, -0.1, 0.0));
                }
                if (!entity.isAttacking()) {
                    //It's on water, not underwater, but it doesn't see any target
                    this.entity.setVelocity(this.entity.getVelocity().add(0.0, -0.008, 0.0));
                }
            }

            //It's moving (no idle)
            if (this.state == State.MOVE_TO && !this.entity.getNavigation().isIdle()) {
                //Calculate what angle the entity needs to see
                //Calculate the angle up and down (Y)
                float hipoY = (float) (MathHelper.atan2(dZtoTarget, dXtoTarget) * 57.2957763671875) - 90.0F;
                this.entity.setYaw(this.wrapDegrees(this.entity.getYaw(),hipoY,90));

                //Applies the 3D Movement Vector
                entity.limbAnimator.setSpeed(1);
                entity.move(MovementType.SELF, new Vec3d(vec3D_movementUnderwater.x, vec3D_movementUnderwater.y, vec3D_movementUnderwater.z));
//                    System.out.println("3DVector applied");

//                    this.moveTo(this.targetX, this.targetY, this.targetZ, 0.5);
                //Keep it underwater
                if(drowner.canDrownerUnderwaterAttackTarget(target)) {
                    assert target != null;
                    if (!target.isSubmergedInWater()) {
                        this.entity.setVelocity(this.entity.getVelocity().add(0.0, -0.001, 0.0));
                    }
                }
            }
            else {
                this.entity.setMovementSpeed(0.0F);
            }
        }

        @Override
        public void tick() {
            //It's swimming, so applies the water control
            if(drowner.getSwimmingDataTracker()) {
                handleUnderwaterMovement();
            }
            //It's not underwater
            else{
                //It's on land, so return the normal MoveControl
                    super.tick();

            }
        }
    }
    //LookControl
    private static class DrownerLookControl extends LookControl {
        private final int yawAdjustThreshold;
        private static final int ADDED_PITCH = 0;
        private static final int ADDED_YAW = 0;
        private final DrownerEntity drowner;

        public DrownerLookControl(DrownerEntity entity, int yawAdjustThreshold) {
            super(entity);
            this.yawAdjustThreshold = yawAdjustThreshold;
            this.drowner = entity;
        }

        @Override
        public void tick() {
            if (drowner.getSwimmingDataTracker()){
                if (this.lookAtTimer > 0) {
                    --this.lookAtTimer;
                    this.getTargetYaw().ifPresent((yaw) -> {
                        this.entity.headYaw = this.changeAngle(this.entity.headYaw, yaw + ADDED_YAW, this.maxYawChange);
                    });
                    this.getTargetPitch().ifPresent((pitch) -> {
                        this.entity.setPitch(this.changeAngle(this.entity.getPitch(), pitch + ADDED_PITCH, this.maxPitchChange));
                    });
                } else {
                    if (this.entity.getNavigation().isIdle()) {
                        this.entity.setPitch(this.changeAngle(this.entity.getPitch(), 0.0F, 5.0F));
                    }
                    this.entity.headYaw = this.changeAngle(this.entity.headYaw, this.entity.bodyYaw, this.maxYawChange);
                }

                //WrapDegrees = Rotates an angle from one point to another
                float f = MathHelper.wrapDegrees(this.entity.headYaw - this.entity.bodyYaw);
                MobEntity entity;
                if (f < (float)(-this.yawAdjustThreshold)) {
                    entity = this.entity;
                    entity.bodyYaw -= 4.0F;
                } else if (f > (float)this.yawAdjustThreshold) {
                    entity = this.entity;
                    entity.bodyYaw += 4.0F;
                }
            }
            else{
                super.tick();
            }
        }
    }
    public boolean hasAttackedOnWater;
    private int cooldownSwimmingAttackTicks;
    //To manage attacks in and outside water
    private class Drowner_MeleeAttackGoal extends MeleeAttackGoal {
        private final DrownerEntity drowner;
        private final int cooldownBetweenWaterAttacks;
        private final int cooldownBetweenLungesAttacks;
        int LungeTicks;
        boolean cooldownBetweenLunges=false;
        public Drowner_MeleeAttackGoal(DrownerEntity mob, double speed, boolean pauseWhenMobIdle, int cooldownBetweenWaterAttacks, int cooldownBetweenLungesAttacks) {
            super(mob, speed, pauseWhenMobIdle);
            this.drowner = mob;
            this.cooldownBetweenWaterAttacks = cooldownBetweenWaterAttacks;
            this.cooldownBetweenLungesAttacks = cooldownBetweenLungesAttacks;
//            this.LuggingBoolean=IsLugging;
        }

        @Override
        public boolean canStart() {
            // If the Drowner it's swimming only attacks creatures on water
            if (drowner.isTouchingWater()) {
                return super.canStart() && this.drowner.canDrownerUnderwaterAttackTarget(this.drowner.getTarget()) && !drowner.hasAttackedOnWater;
            } else {
                return super.canStart();
            }
        }

        @Override
        public boolean shouldContinue() {
            if(drowner.isTouchingWater()){
                return super.shouldContinue() && this.drowner.canDrownerUnderwaterAttackTarget(this.drowner.getTarget()) && !drowner.hasAttackedOnWater;
            }
            else{
                return super.shouldContinue();
            }
        }

        Vec3d vec3D_lunge;
        float bodyYawPrev;


        @Override
        public void tick(){
            //Start the counter for the Lunge attack
            super.tick();
            if (LungeTicks > 0) {
                DrownerEntity.this.setIsLugging(false);
                drowner.setBodyYaw(90);
                --LungeTicks;
            } else {
                cooldownBetweenLunges = false;
            }

//            if(this.drowner.getIsLugging()){
//            }
        }

        @Override
        protected void attack(LivingEntity target, double squaredDistance) {
            //Special logic for water attack
            if (drowner.isTouchingWater()) {
                if(target!=null){
                WaterAttackLogic(target, squaredDistance);}
            } else {
                //Lunge and land attack
                if(target!=null){
                LungeAttack(target, squaredDistance);
                }
            }
        }



        @NotNull
        private Vec3d getVec3d(LivingEntity target) {
            double dXtoTarget = target.getX() - this.mob.getX();
            double dYtoTarget = target.getY() - this.mob.getY();
            double dZtoTarget = target.getZ() - this.mob.getZ();
            double length = Math.sqrt(dXtoTarget * dXtoTarget + dYtoTarget * dYtoTarget + dZtoTarget * dZtoTarget);

            //Movement Vector
            return new Vec3d((double)(dXtoTarget / length) * 0.8,
                             (double)(dYtoTarget / length) * 0.8,
                             (double)(dZtoTarget / length)* 0.8) ;
        }

        //Logic for the lunge attack and attacks on land
        private void LungeAttack(LivingEntity target, double squaredDistance){
            double d = this.getSquaredMaxAttackDistance(target);
            //Check if it can do a lunge
            if(!cooldownBetweenLunges){
                //Makes the lunge
                //5 square distance like 1.5 blocks aprox
                //I want 7.5 blocks aprox
                //So 7.5/1.5=5
                if(this.mob.squaredDistanceTo(target) > 5 && this.mob.squaredDistanceTo(target) < 25 && (Objects.requireNonNull(this.mob.getTarget()).getY() - this.mob.getY()) < 10) {

                    vec3D_lunge = getVec3d(target);

                    DrownerEntity.this.setIsLugging(true);

                    bodyYawPrev = drowner.getBodyYaw();
                    this.mob.setVelocity(this.mob.getVelocity().add(vec3D_lunge.x, 0.35, vec3D_lunge.z));
                    drowner.setBodyYaw(bodyYawPrev);
                    DrownerEntity.this.playSound(TCOTS_Sounds.DROWNER_LUNGE, 1.0F, 1.0F);


                    //Put the cooldown
                    LungeTicks = cooldownBetweenLungesAttacks;
                    cooldownBetweenLunges = true;
                }
                else{
                    //If it's not far enough, normal attack
                    if (squaredDistance <= d && super.getCooldown() <= 0) {
                        this.resetCooldown();
                        this.mob.swingHand(Hand.MAIN_HAND);
                        this.mob.tryAttack(target);
                    }
                }
            }
            else {
                //If it has cooldown, normal attack
                if (squaredDistance <= d && super.getCooldown() <= 0) {
                    this.resetCooldown();
                    this.mob.swingHand(Hand.MAIN_HAND);
                    this.mob.tryAttack(target);
                }
            }
        }

        //Method for water attack
        private void WaterAttackLogic(LivingEntity target, double squaredDistance) {
            if(!drowner.hasAttackedOnWater) {
                double d = this.getSquaredMaxAttackDistance(target);
                if (squaredDistance <= d && super.getCooldown() <= 0) {
                    this.resetCooldown();
                    this.mob.swingHand(Hand.MAIN_HAND);
                    //Makes the target go down on water
                    target.setVelocity(target.getVelocity().add(0.0, -0.3, 0.0));
                    this.mob.tryAttack(target);
                    drowner.cooldownSwimmingAttackTicks = cooldownBetweenWaterAttacks;
                    drowner.hasAttackedOnWater = true;
                }
            }
        }
    }

    //Makes the drowner flee after a hit that it make
    private static class Drowner_FleeFromTarget extends SwimAroundGoal{
        private final DrownerEntity drowner;
        private Drowner_FleeFromTarget(DrownerEntity drowner, double speed, int probability) {
            super(drowner, speed, probability);
            this.drowner = drowner;
        }

        @Override
        public boolean canStart() {
            return super.canStart() && drowner.hasAttackedOnWater;
        }
        @Override
        public boolean shouldContinue(){
            return super.shouldContinue() && drowner.hasAttackedOnWater;
        }
        @Override
        public void tick(){
            if (drowner.cooldownSwimmingAttackTicks > 0) {
                --drowner.cooldownSwimmingAttackTicks;
            } else {
                drowner.hasAttackedOnWater = false;
            }
        }
        @Override
        @Nullable
        protected Vec3d getWanderTarget() {
            return LookTargetUtil.find(this.mob, 4, 3);
        }
    }

    //This way don't reset every time it reenter the world, causing clipping
    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putBoolean("Swimming", this.dataTracker.get(SWIM));
//        nbt.putBoolean("Lugging", this.dataTracker.get(LUGGING));
    }
    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        this.setSwimmingDataTracker(nbt.getBoolean("Swimming"));
//        this.setIsLugging(nbt.getBoolean("Lugging"));
        super.readCustomDataFromNbt(nbt);
    }
    public boolean getSwimmingDataTracker() {
        return this.dataTracker.get(SWIM);
    }
    public void setSwimmingDataTracker(boolean wasSwimming) {
        this.dataTracker.set(SWIM, wasSwimming);
    }
    public final boolean getIsLugging(){
        return this.dataTracker.get(LUGGING);
    }
    public final void setIsLugging(boolean wasLugging){
        this.dataTracker.set(LUGGING, wasLugging);
    }

    //Dynamic hitbox
    @Override
    protected Box calculateBoundingBox() {
        if (dataTracker.get(SWIM)) {
            // Tiny hit-box when swim
            return new Box(this.getX() - 0.39, this.getY()+1.7, this.getZ() - 0.39,
                    this.getX() + 0.39, this.getY()+0.6, this.getZ() + 0.39);
        } else {
            // Normal hit-box otherwise
            return super.calculateBoundingBox();
        }
    }
    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(SWIM, Boolean.FALSE);
        this.dataTracker.startTracking(LUGGING, Boolean.FALSE);
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

    //Each Control can only play one animation at the time
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {

        //Walk/Idle Controller
        controllerRegistrar.add(new AnimationController<>(this, "Idle/Walk", 5,
                state->{
                    if(this.getSwimmingDataTracker()){
                        if(state.isMoving()){
                            return state.setAndContinue(SWIMMING);
                        }
                        else{
                            return state.setAndContinue(WATER_IDLE);
                        }
                    }
                    else{
//                        if(this.getIsLugging()) {
//                            return PlayState.STOP;
//                        }
//                        else{
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
//                        }
                    }
                }
        ));

        //Attack Controller
        controllerRegistrar.add(
                new AnimationController<>(this, "AttackController", 1, state -> {
                    state.getController().forceAnimationReset();
                    // Random instance
                    Random random = new Random();
                    // Generates two random numbers
                    int r = ThreadLocalRandom.current().nextInt(1, 3);
                    if (this.handSwinging){
                        if(this.getSwimmingDataTracker()){
                            if (r == 1) {
                                return state.setAndContinue(WATER_ATTACK1);
                            } else {
                                return state.setAndContinue(WATER_ATTACK2);
                            }
                        }
                        else {
                            if (r == 1) {
                                return state.setAndContinue(ATTACK1);
                            } else {
                                return state.setAndContinue(ATTACK2);
                            }
                        }
                    }
                    return PlayState.CONTINUE;
                })
        );

        //Lunge Controller
        controllerRegistrar.add(
                new AnimationController<>(this, "LungeController", 1, state -> {
                    if (this.getIsLugging()){
                        state.setAnimation(LUNGE);
                        return PlayState.CONTINUE;
                    }

                    state.getController().forceAnimationReset();
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
    protected SoundEvent getSwimSound() {
        return SoundEvents.ENTITY_PLAYER_SWIM;
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
    //Water creature things
    @Override
    public boolean canBreatheInWater() {
        return true;
    }
    @Override
    public boolean isPushedByFluids() {
        return false;
    }

    //To kill Drowner in one shot with a crossbow arrow
    @Override
    public boolean damage(DamageSource source, float amount) {
        //If it's a projectile
        if (source.getSource() instanceof PersistentProjectileEntity arrow) {
            Entity shooter = source.getAttacker();
            //If the shooter it's on the water, the arrow was shot from a crossbow and the Drowner it's swimming
            if (shooter != null && arrow.isShotFromCrossbow() && shooter.isTouchingWater() && this.getSwimmingDataTracker()) {
                return super.damage(source, 200);
            }
        }
        return super.damage(source, amount);
    }
    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}