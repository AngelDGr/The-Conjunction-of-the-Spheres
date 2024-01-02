package TCOTS.entity.necrophages;

import TCOTS.entity.TCOTS_Entities;
import TCOTS.sounds.TCOTS_Sounds;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;

import net.minecraft.entity.ai.control.LookControl;
import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.ai.pathing.MobNavigation;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.ai.pathing.SwimNavigation;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;


import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.AxolotlEntity;
import net.minecraft.entity.passive.DolphinEntity;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.thrown.PotionEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;


import java.util.EnumSet;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class DrownerEntity extends HostileEntity implements GeoEntity {
    protected final SwimNavigation waterNavigation;
    protected final MobNavigation landNavigation;
    public static final RawAnimation IDLE = RawAnimation.begin().thenLoop("idle");
    public static final RawAnimation WATER_IDLE = RawAnimation.begin().thenLoop("idle.water");
    public static final RawAnimation RUNNING= RawAnimation.begin().thenLoop("move.running");
    public static final RawAnimation WALKING = RawAnimation.begin().thenLoop("move.walking");
    public static final RawAnimation SWIMMING= RawAnimation.begin().thenLoop("move.swimming");
    public static final RawAnimation ATTACK1 = RawAnimation.begin().thenPlay("attack.swing1");
    public static final RawAnimation ATTACK2 = RawAnimation.begin().thenPlay("attack.swing2");
    public static final RawAnimation WATER_ATTACK1 = RawAnimation.begin().thenPlay("attack.waterswing1");
    public static final RawAnimation WATER_ATTACK2 = RawAnimation.begin().thenPlay("attack.waterswing2");
    protected static final TrackedData<Boolean> SWIM = DataTracker.registerData(DrownerEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    public DrownerEntity(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
        this.setPathfindingPenalty(PathNodeType.WATER, 0.0F);
        this.moveControl = new WaterOrLand_MoveControl(this, 0.07F);
        this.lookControl = new DrownerLookControl(this, 90);

        this.waterNavigation = new SwimNavigation(this, world);
        this.landNavigation = new MobNavigation(this, world);
    }

    @Override
    protected void initGoals() {
//        this.goalSelector.add(1, new SwimGoal(this));
        this.goalSelector.add(1, new Drowner_MeleeAttackGoal(this, 1.2D, false));

        this.goalSelector.add(2, new SwimAroundGoal(this, 1.0, 10));
        this.goalSelector.add(3, new WanderAroundFarGoal(this, 0.75f, 1));
        this.goalSelector.add(4, new LookAroundGoal(this));

        this.targetSelector.add(2, new ActiveTargetGoal<>(this, PlayerEntity.class, true));
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, MerchantEntity.class, true));
        this.targetSelector.add(3, new ActiveTargetGoal<>(this, AxolotlEntity.class, true));
        this.targetSelector.add(3, new ActiveTargetGoal<>(this, DolphinEntity.class, true));
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
                this.getDataTracker().set(SWIM, Boolean.TRUE);
//                this.setSwimming(true);
            } else {
                this.navigation = this.landNavigation;
                this.getDataTracker().set(SWIM, Boolean.FALSE);
//                this.setSwimming(false);
            }
        }
    }

    //MoveControl
    private static class WaterOrLand_MoveControl extends MoveControl {
        private final float waterSpeed; // Velocidad de movimiento en el agua
        double swimmingSpeed=0.8;
        private final DrownerEntity drowner;
        public WaterOrLand_MoveControl(DrownerEntity entity, float waterSpeed) {
            super(entity);
            this.waterSpeed = waterSpeed;
            this.drowner=entity;
        }

        @Override
        public void tick() {
            LivingEntity target = this.entity.getTarget();
            double dXtoTarget = this.targetX - this.entity.getX();
            double dYtoTarget = this.targetY - this.entity.getY();
            double dZtoTarget = this.targetZ - this.entity.getZ();
            double length = Math.sqrt(dXtoTarget * dXtoTarget + dYtoTarget * dYtoTarget + dZtoTarget * dZtoTarget);

            //Movement Vector
            Vec3d vec3D_movementUnderwater = new Vec3d((double)(dXtoTarget / length) * waterSpeed,
                                                       (double)(dYtoTarget / length) * waterSpeed,
                                                       (double)(dZtoTarget / length) * waterSpeed);

            Vec3d vec3D_movementAboveWater = new Vec3d((double)(dXtoTarget / length) * swimmingSpeed,
                                                       (double)(dYtoTarget / length) * swimmingSpeed,
                                                       (double)(dZtoTarget / length) * swimmingSpeed);

            if(target != null && target.getY() > this.entity.getY() && entity.isTouchingWater() && drowner.canDrownerUnderwaterAttackTarget(target)){
                //Makes it go up
                this.entity.setVelocity(this.entity.getVelocity().add(0.0, 0.01, 0.0));}

            if(drowner.getSwimmingDataTracker()) {
                if (this.state == State.MOVE_TO && !this.entity.getNavigation().isIdle()) {
                    //Calculate what angle the entity needs to see
                    //Calculate the angle up and down (Y)
                    float hipoY = (float) (MathHelper.atan2(dZtoTarget, dXtoTarget) * 57.2957763671875) - 90.0F;
                    this.entity.setYaw(this.wrapDegrees(this.entity.getYaw(),hipoY,90));
                    this.entity.bodyYaw = this.entity.getYaw();
                    this.entity.headYaw = this.entity.getYaw();


                    entity.move(MovementType.SELF, new Vec3d(vec3D_movementUnderwater.x, vec3D_movementUnderwater.y, vec3D_movementUnderwater.z));
                }
                else {
                    this.entity.setMovementSpeed(0.0F);
                }
            }
            //It's not underwater
            else{

                //It's not underwater but it's touching water
                if(entity.isTouchingWater()){
                    if(target!=null){
                    entity.getLookControl().lookAt(target);
//                    entity.setPitch();
                    entity.move(MovementType.SELF, new Vec3d(vec3D_movementAboveWater.x, 0, vec3D_movementAboveWater.z));
                    }
                    if(!entity.isAttacking()){
                        //It's on water, not underwater, but it doesn't see any target
                        this.entity.setVelocity(this.entity.getVelocity().add(0.0, -0.008, 0.0));
                    }
                }
                //It's on land, so return the normal MoveControl
                else{super.tick();}
            }
        }

//        @Override
//        public void tick() {
//            LivingEntity target = this.entity.getTarget();
//
//            if (target != null) {
//                    double dXtoTarget = this.targetX - this.entity.getX();
//                    double dYtoTarget = this.targetY - this.entity.getY();
//                    double dZtoTarget = this.targetZ - this.entity.getZ();
//                    double length = Math.sqrt(dXtoTarget * dXtoTarget + dYtoTarget * dYtoTarget + dZtoTarget * dZtoTarget);
//
//                    Vec3d lookAt = new Vec3d(dXtoTarget / length, dYtoTarget / length, dZtoTarget / length);
//                    this.entity.getLookControl().lookAt(target, 30.0F, 30.0F);
//
//                    // Movement Vector
//                    Vec3d vec3D_movementUnderwater = new Vec3d(lookAt.x * waterSpeed, lookAt.y * waterSpeed, lookAt.z * waterSpeed);
//
//                    if (target.getY() > this.entity.getY() && entity.isTouchingWater() && drowner.canDrownerUnderwaterAttackTarget(target)) {
//                        // Makes it go up
//                        this.entity.setVelocity(this.entity.getVelocity().add(0.0, 0.01, 0.0));
//                    }
//
//                    if (drowner.getSwimmingDataTracker()) {
//                        if (this.state == State.MOVE_TO && !this.entity.getNavigation().isIdle()) {
//                                entity.move(MovementType.SELF, new Vec3d(vec3D_movementUnderwater.x, vec3D_movementUnderwater.y, vec3D_movementUnderwater.z));
//                        } else {
//                                this.entity.setMovementSpeed(0.0F);
//                        }
//                    } else {
//                        if (entity.isTouchingWater()) {
//                            super.tick();
//                        if (!entity.isAttacking()) {
//                            // It's on water, not underwater, but it doesn't see any target
//                            this.entity.setVelocity(this.entity.getVelocity().add(0.0, -0.008, 0.0));
//                        }
//                        } else {
//                            // It's on land, so return the normal MoveControl
//                            super.tick();
//                        }
//                    }
//            }
//        }
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

                //WrapDegrees = Rota un angulo de un punto a otro
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

    //To manage attacks in and outside water
    private static class Drowner_MeleeAttackGoal extends MeleeAttackGoal {
        private final DrownerEntity drowner;
        public Drowner_MeleeAttackGoal(DrownerEntity mob, double speed, boolean pauseWhenMobIdle) {
            super(mob, speed, pauseWhenMobIdle);
            this.drowner= mob ;
        }
        public boolean canStart() {
            //If drowner it's swimming only attacks underwater creatures
            if(drowner.isTouchingWater()){
                return super.canStart() && this.drowner.canDrownerUnderwaterAttackTarget(this.drowner.getTarget());
            }
            else{
                return super.canStart();
            }
        }
        public boolean shouldContinue() {
            if(drowner.isTouchingWater()){
                return super.shouldContinue() && this.drowner.canDrownerUnderwaterAttackTarget(this.drowner.getTarget());
            }
            else{
                return super.shouldContinue();
            }
        }
    }

    //This way don't reset every time it reenter the world, causing clipping
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putBoolean("Swimming", this.dataTracker.get(SWIM));
    }
    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        this.setSwimmingDataTracker(nbt.getBoolean("Swimming"));
        super.readCustomDataFromNbt(nbt);
    }
    public boolean getSwimmingDataTracker() {
        return this.dataTracker.get(SWIM);
    }
    public void setSwimmingDataTracker(boolean wasSwimming) {
        this.dataTracker.set(SWIM, wasSwimming);
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
                    //If it's aggressive and it is moving
                    if(this.isAttacking() && state.isMoving()){
                        return state.setAndContinue(RUNNING);
                    }
                    //It's not attacking and/or it's no moving
                    else{
                        //If it's attacking but NO moving
                        if(isAttacking()){
                            return state.setAndContinue(RUNNING);
                        }
                        else {
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
    //Water creature things
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

    //To kill it in one shot with a crossbow arrow
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
