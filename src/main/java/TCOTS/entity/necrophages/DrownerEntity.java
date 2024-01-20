package TCOTS.entity.necrophages;

import TCOTS.entity.misc.DrownerPuddleEntity;
import TCOTS.sounds.TCOTS_Sounds;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
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
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;

import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.tag.BiomeTags;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Difficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;


import java.util.EnumSet;
import java.util.List;

public class DrownerEntity extends Necrophage_Base implements GeoEntity {
//xTODO: Make it faster when walking in water
//xTODO: Make it that it don't sink in water
//xTODO: Fix the data tracker
//xTODO: Add the digging in ground goal
//xTODO: Add sounds when digging
//xTODO: Make the puddle entity spawn when digging

//xTODO: Add drops (Add loot tables)
//xTODO: Add spawn

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
    public static final RawAnimation DIGGING_OUT = RawAnimation.begin().thenPlayAndHold("special.diggingOut");
    public static final RawAnimation DIGGING_IN = RawAnimation.begin().thenPlayAndHold("special.diggingIn");

    protected static final TrackedData<Boolean> SWIM = DataTracker.registerData(DrownerEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    protected static final TrackedData<Boolean> InGROUND = DataTracker.registerData(DrownerEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    protected static final TrackedData<Boolean> EMERGING = DataTracker.registerData(DrownerEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    protected static final TrackedData<Boolean> LUGGING = DataTracker.registerData(DrownerEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    protected static final TrackedData<Boolean> SPAWNED_PUDDLE = DataTracker.registerData(DrownerEntity.class, TrackedDataHandlerRegistry.BOOLEAN);


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
        //Flee, lunge, emerging and Water/land attack
        this.goalSelector.add(0, new Drowner_FleeFromTarget(this,1.0, 1));

        //Emerge from ground
        this.goalSelector.add(0, new Drowner_EmergeFromGround(this));

        this.goalSelector.add(1, new Drowner_Lunge(this,100, 0.9));

        //Returns to ground
        this.goalSelector.add(2, new Drowner_ReturnToGround(this));

        this.goalSelector.add(3, new Drowner_LandWaterAttackGoal(this, 1.2D, false, 40, 3000));


        this.goalSelector.add(4, new Drowner_SwimAroundGoal(this, 1.0, 10));
        this.goalSelector.add(5, new Drowner_WanderAroundGoal(this, 0.75f, 20));

        this.goalSelector.add(6, new Drowner_LookAroundGoal(this));

        //Objectives
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, PlayerEntity.class, true));
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, MerchantEntity.class, true));
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, IronGolemEntity.class, true));
        this.targetSelector.add(3, new ActiveTargetGoal<>(this, AxolotlEntity.class, true));
        this.targetSelector.add(3, new ActiveTargetGoal<>(this, DolphinEntity.class, true));
        this.targetSelector.add(4, new ActiveTargetGoal<>(this, FishEntity.class, true));
        this.targetSelector.add(4, new ActiveTargetGoal<>(this, SquidEntity.class, true));
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
        private final float waterSpeed;
        private final DrownerEntity drowner;
        public WaterOrLand_MoveControl(DrownerEntity entity, float waterSpeed) {
            super(entity);
            this.waterSpeed = waterSpeed;
            this.drowner=entity;
        }

        public void handleUnderwaterMovement(){
            LivingEntity target = this.entity.getTarget();
            double dXtoTarget = this.targetX - this.entity.getX();
            double dYtoTarget = this.targetY - this.entity.getY();
            double dZtoTarget = this.targetZ - this.entity.getZ();
            double length = Math.sqrt(dXtoTarget * dXtoTarget + dYtoTarget * dYtoTarget + dZtoTarget * dZtoTarget);

            //Movement Vector
            Vec3d vec3D_movementUnderwater = new Vec3d((dXtoTarget / length) * waterSpeed,
                                                       (dYtoTarget / length) * waterSpeed,
                                                       (dZtoTarget / length) * waterSpeed);

            //Makes it go up if it's attacking
            if(target != null
                    && target.getY() > this.entity.getY()
                    //If the target it's attackable
                    && drowner.canDrownerUnderwaterAttackTarget(target)
                    //Target it's not submerged
                    && !target.isSubmergedInWater())
            {this.entity.setVelocity(this.entity.getVelocity().add(0.0, 0.02, 0.0));}

            //It's not underwater, it's touching water, but it's not in the ground
            if(!entity.isOnGround() && entity.isTouchingWater() && !entity.isSubmergedInWater()) {
                //It's attacking in the water, but it's not submerged
                if (drowner.canDrownerUnderwaterAttackTarget(target)) {
                    this.entity.setVelocity(this.entity.getVelocity().add(0.0, -0.1, 0.0));
                }
            }

            if(entity.isOnGround() && drowner.getSwimmingDataTracker() && !entity.isAttacking()){
//                if (drowner.canDrownerUnderwaterAttackTarget(target)) {
                    this.entity.setVelocity(this.entity.getVelocity().add(0.0, 0.08, 0.0));
//                }
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
    public int LungeTicks;
    public int ReturnToGround_Ticks=20;
    public boolean cooldownBetweenLunges=false;

    //To manage attacks in and outside water
    private class Drowner_LandWaterAttackGoal extends MeleeAttackGoal {
        private final DrownerEntity drowner;
        private final int cooldownBetweenWaterAttacks;
        private final int ticksBeforeToGround;

        public Drowner_LandWaterAttackGoal(DrownerEntity mob, double speed, boolean pauseWhenMobIdle, int cooldownBetweenWaterAttacks, int ticksBeforeToGround) {
            super(mob, speed, pauseWhenMobIdle);
            this.drowner = mob;
            this.cooldownBetweenWaterAttacks = cooldownBetweenWaterAttacks;
            this.ticksBeforeToGround = ticksBeforeToGround;
        }

        @Override
        public boolean canStart() {
            // If the Drowner it's swimming only attacks creatures on water
            if (drowner.getSwimmingDataTracker()) {
                return super.canStart() && this.drowner.canDrownerUnderwaterAttackTarget(this.drowner.getTarget()) && !drowner.hasAttackedOnWater;
            } else {
                return super.canStart()
                        && !this.drowner.getIsEmerging()
                        && !this.drowner.getInGroundDataTracker();
            }
        }
        @Override
        public boolean shouldContinue() {
            if(drowner.getSwimmingDataTracker()){
                return super.shouldContinue() && this.drowner.canDrownerUnderwaterAttackTarget(this.drowner.getTarget()) && !drowner.hasAttackedOnWater;
            }
            else{
                return super.shouldContinue();
            }
        }

        @Override
        protected void attack(LivingEntity target, double squaredDistance) {
            //Special logic for water attack
            if (drowner.getSwimmingDataTracker()) {
                if(target!=null){
                WaterAttackLogic(target, squaredDistance);}
            } else {
                //Lunge and land attack
                if(target!=null){
                    int randomExtra = DrownerEntity.this.random.nextBetween(1,51);

                        DrownerEntity.this.ReturnToGround_Ticks=this.ticksBeforeToGround + randomExtra;

                    super.attack(target,squaredDistance);
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
    //To manage the lunge attack
    private class Drowner_Lunge extends Goal {
        private final DrownerEntity mob;
        private final int cooldownBetweenLungesAttacks;
        private final double SpeedLungeMultiplier;

        private Drowner_Lunge(DrownerEntity mob, int cooldownBetweenLungesAttacks, double lungeImpulse) {
            this.mob = mob;
            this.cooldownBetweenLungesAttacks=cooldownBetweenLungesAttacks;
            this.setControls(EnumSet.of(Control.MOVE, Control.JUMP));
            this.SpeedLungeMultiplier=lungeImpulse;
        }

        @Override
        public boolean canStart() {
            LivingEntity target = this.mob.getTarget();
            if(target!=null){
                //5 square distance like 1.5 blocks approx
                //I want 7.5 blocks approx
                //So 7.5/1.5=5
            return !DrownerEntity.this.cooldownBetweenLunges && this.mob.isAttacking() && !this.mob.getSwimmingDataTracker()
                    && this.mob.squaredDistanceTo(target) > 5 && this.mob.squaredDistanceTo(target) < 25
                    && (this.mob.getTarget().getY() - this.mob.getY()) <= 1
                    && !this.mob.getIsEmerging()
                    && !this.mob.getInGroundDataTracker()
                    ;}
            else {
                return false;
            }
        }

        @Override
        public boolean shouldContinue(){
            LivingEntity target = this.mob.getTarget();
            if(target!=null){
                //5 square distance like 1.5 blocks approx
                //I want 7.5 blocks approx
                //So 7.5/1.5=5
                return !DrownerEntity.this.cooldownBetweenLunges && this.mob.isAttacking() && !this.mob.getSwimmingDataTracker()
                        && this.mob.squaredDistanceTo(target) > 5 && this.mob.squaredDistanceTo(target) < 25
                        && (this.mob.getTarget().getY() - this.mob.getY()) <= 1;}
            else {
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
        public void start(){
            this.mob.getNavigation().stop();
        }

        @Override
        public void stop(){
//            Drowner_MeleeAttackGoal.start();
        }

        @Override
        public void tick(){
            LivingEntity livingEntity = this.mob.getTarget();

            if(livingEntity!=null){
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

        private void LungeAttack(LivingEntity target){
            //Check if it can do a lunge
            if(!DrownerEntity.this.cooldownBetweenLunges){
                //Makes the lunge
                    //Extra random ticks in cooldown
                    randomExtra = DrownerEntity.this.random.nextInt(51);
                    //0.35 Y default
                    vec3D_lunge = getVec3d(target).normalize();
                    DrownerEntity.this.setIsLugging(true);

                    DrownerEntity.this.setVelocity(DrownerEntity.this.getVelocity().add(vec3D_lunge.x, 0.35, vec3D_lunge.z));
                    this.mob.getLookControl().lookAt(target, 30.0F, 30.0F);

                    DrownerEntity.this.playSound(TCOTS_Sounds.DROWNER_LUNGE, 1.0F, 1.0F);

                    //Put the cooldown
                    LungeTicks = cooldownBetweenLungesAttacks + randomExtra;
                    DrownerEntity.this.cooldownBetweenLunges = true;

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

    DrownerPuddleEntity puddle;
    //Makes the drowner occult in ground
    private class Drowner_ReturnToGround extends Goal {
        private final DrownerEntity drowner;
        int ticks=35;
        private Drowner_ReturnToGround(DrownerEntity mob) {
            this.drowner = mob;
        }
        @Override
        public boolean canStart() {
            return drowner.getInGroundDataTracker();
        }
        @Override
        public boolean shouldContinue(){
            return drowner.getInGroundDataTracker();
        }
        @Override
        public void start(){
            ticks=35;
            if(!drowner.getSpawnedPuddleDataTracker() && !drowner.isTouchingWater()){
                spawnPuddle(drowner.getWorld(),drowner);
            }
            drowner.playSound(TCOTS_Sounds.DROWNER_DIGGING,1.0F,1.0F);
            drowner.getNavigation().stop();
            drowner.getLookControl().lookAt(0,0,0);
        }

        @Override
        public void stop(){

        }

        @Override
        public void tick(){

        }

        public void spawnPuddle(World world, LivingEntity entity){
            float yaw=DrownerEntity.this.random.nextInt(361);

            DrownerEntity.this.puddle = new DrownerPuddleEntity(world, entity.getX(), entity.getY(), entity.getZ(), DrownerEntity.this);

            if (!world.isClient) {
                world.spawnEntity(DrownerEntity.this.puddle);

                DrownerEntity.this.puddle.setSpawnControlDataTracker(true);
                drowner.setSpawnedPuddleDataTracker(true);
            }

        }


    }
    //Makes the drowner emerge from ground
    private class Drowner_EmergeFromGround extends Goal{

        private final DrownerEntity drowner;

        protected final PathAwareEntity mob;

        int AnimationTicks=36;

        private Drowner_EmergeFromGround(DrownerEntity mob) {
            this.drowner = mob;
            this.mob = mob;

        }

        @Override
        public boolean canStart() {
            return canStartO() && drowner.getInGroundDataTracker();
        }
        public boolean canStartO(){

                LivingEntity livingEntity = this.mob.getTarget();
                //If it doesn't have target
                if (livingEntity == null) {
                    return false;
                }
                //If it's the target dead
                else if (!livingEntity.isAlive()) {
                    return false;
                }
                else {
                    return this.mob.squaredDistanceTo(livingEntity.getX(), livingEntity.getY(), livingEntity.getZ()) <= 80;
                }
        }

        @Override
        public void start(){
            this.drowner.playSound(TCOTS_Sounds.DROWNER_EMERGING, 1.0F, 1.0F);
            if(DrownerEntity.this.puddle!=null){
                DrownerEntity.this.puddle.setSpawnControlDataTracker(false);
            }

            this.drowner.spawnGroundParticles();
            AnimationTicks=36;
            drowner.setIsEmerging(true);
        }

        @Override
        public boolean shouldContinue(){
            return shouldContinueO() && drowner.getInGroundDataTracker();
        }
        public boolean shouldContinueO(){
            LivingEntity livingEntity = this.mob.getTarget();
            if (livingEntity == null) {
                return false;
            } else if (!livingEntity.isAlive()) {
                return false;
            } else {
                return !(livingEntity instanceof PlayerEntity) || !livingEntity.isSpectator() && !((PlayerEntity)livingEntity).isCreative();
            }
        }

        @Override
        public void tick(){
            if (AnimationTicks > 0) {
//                System.out.println("AnimationTicks"+AnimationTicks);
                --AnimationTicks;
            }else {
                  stop();
            }
        }

        @Override
        public void stop(){
            drowner.setIsEmerging(false);
            if(DrownerEntity.this.puddle!=null){
                destroyPuddle();
            }

            if(DrownerEntity.this.getInGroundDataTracker()){
                DrownerEntity.this.ReturnToGround_Ticks=100;
                DrownerEntity.this.setInGroundDataTracker(false);}
        }

        @Override
        public boolean shouldRunEveryTick() {
            return true;
        }

        public void destroyPuddle(){
            DrownerEntity.this.puddle.discard();
            drowner.setSpawnedPuddleDataTracker(false);
        }
    }

    public DrownerPuddleEntity DetectOwnPuddle() {
        List<DrownerPuddleEntity> list = this.getWorld().getEntitiesByClass(DrownerPuddleEntity.class,
                new Box(this.getX() + 2, this.getY() + 2, this.getZ() + 2,
                        this.getX() - 2, this.getY() - 2, this.getZ() - 2),
                (T) -> true);

        //Detect
        if (!list.isEmpty()) {
            for (DrownerPuddleEntity puddleEntity : list) {
                if (puddleEntity.getOwnerUUID() != null && puddleEntity.getOwnerUUID().equals(this.getUuid())) {
                    return puddleEntity;
                }
            }
        }

        return null;
    }

    @Override
    public boolean isInvulnerableTo(DamageSource damageSource) {
        return this.getIsEmerging() || this.getInGroundDataTracker() || super.isInvulnerableTo(damageSource);
    }
    @Override
    public boolean isPushable() {
        return !this.getIsEmerging() && !this.getInGroundDataTracker();
    }

    private class Drowner_WanderAroundGoal extends WanderAroundGoal{

        public Drowner_WanderAroundGoal(PathAwareEntity mob, double speed, int chance) {
            super(mob, speed, chance);
        }

        @Override
        public boolean canStart(){
            return super.canStart() && !DrownerEntity.this.getInGroundDataTracker();
        }

        @Override
        public boolean shouldContinue(){
            return super.shouldContinue() && !DrownerEntity.this.getInGroundDataTracker();
        }
    }
    private class Drowner_LookAroundGoal extends  LookAroundGoal{

        public Drowner_LookAroundGoal(MobEntity mob) {
            super(mob);
        }

        @Override
        public boolean canStart(){
            return super.canStart() && !DrownerEntity.this.getInGroundDataTracker();
        }

        @Override
        public boolean shouldContinue(){
            return super.shouldContinue() && !DrownerEntity.this.getInGroundDataTracker();
        }

    }
    private class Drowner_SwimAroundGoal extends SwimAroundGoal{

        public Drowner_SwimAroundGoal(PathAwareEntity pathAwareEntity, double d, int i) {
            super(pathAwareEntity, d, i);
        }

        @Override
        public boolean canStart(){
            return super.canStart() && !DrownerEntity.this.getInGroundDataTracker();
        }

        @Override
        public boolean shouldContinue(){
            return super.shouldContinue() && !DrownerEntity.this.getInGroundDataTracker();
        }
    }


    //This way don't reset every time it reenter the world, causing clipping
    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putBoolean("Swimming", this.dataTracker.get(SWIM));
        nbt.putBoolean("InGround", this.dataTracker.get(InGROUND));
        nbt.putBoolean("PuddleSpawned",this.dataTracker.get(SPAWNED_PUDDLE));
    }
    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        this.setSwimmingDataTracker(nbt.getBoolean("Swimming"));
        this.setInGroundDataTracker(nbt.getBoolean("InGround"));
        this.setSpawnedPuddleDataTracker(nbt.getBoolean("PuddleSpawned"));
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

    public final boolean getIsEmerging(){
        return this.dataTracker.get(EMERGING);
    }
    public final void setIsEmerging(boolean wasEmerging){
        this.dataTracker.set(EMERGING, wasEmerging);
    }

    public boolean getInGroundDataTracker() {
        return this.dataTracker.get(InGROUND);
    }
    public void setInGroundDataTracker(boolean wasInGround) {
        this.dataTracker.set(InGROUND, wasInGround);
    }

    public boolean getSpawnedPuddleDataTracker() {
        return this.dataTracker.get(SPAWNED_PUDDLE);
    }
    public void setSpawnedPuddleDataTracker(boolean puddleSpawned) {this.dataTracker.set(SPAWNED_PUDDLE, puddleSpawned);}

    //Dynamic hitbox
    @Override
    protected Box calculateBoundingBox() {
        if (dataTracker.get(SWIM)) {
            // Tiny hit-box when swim
            return new Box(this.getX() - 0.39, this.getY()+1.65, this.getZ() - 0.39,
                    this.getX() + 0.39, this.getY()+0.6, this.getZ() + 0.39);
        } else {

            if (dataTracker.get(InGROUND)) {
                return new Box(this.getX() - 0.39, this.getY() + 0.1, this.getZ() - 0.39,
                        this.getX() + 0.39, this.getY(), this.getZ() + 0.39);
            }
            else{
                // Normal hit-box otherwise
                return super.calculateBoundingBox();
            }
        }
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(SWIM, Boolean.FALSE);
        this.dataTracker.startTracking(LUGGING, Boolean.FALSE);
        this.dataTracker.startTracking(InGROUND, Boolean.FALSE);
        this.dataTracker.startTracking(EMERGING, Boolean.FALSE);
        this.dataTracker.startTracking(SPAWNED_PUDDLE, Boolean.FALSE);
    }
    @Override
    public void onTrackedDataSet(TrackedData<?> data) {
        super.onTrackedDataSet(data);
        if (dataTracker.get(SWIM) || !dataTracker.get(InGROUND) || dataTracker.get(InGROUND)) {
//            System.out.println("InsideOnTrackedDataSet: "+DrownerEntity.this.getInGroundDataTracker());
            this.setBoundingBox(this.calculateBoundingBox());
        }

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
        controllerRegistrar.add(new AnimationController<>(this, "Idle/Walk", 5, state->{
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
                                        //It's in the ground and not emerging
                                        if(this.getInGroundDataTracker() && !this.getIsEmerging()){
                                            state.setAnimation(DIGGING_IN);
                                            return PlayState.CONTINUE;
                                        }else{
                                            return state.setAndContinue(IDLE);
                                        }
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
                    // Generates two random numbers
                    if (this.handSwinging){
                        int r = DrownerEntity.this.random.nextInt(2);
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

        //DiggingOut Controller
        controllerRegistrar.add(
                new AnimationController<>(this, "DiggingOutController", 1, state -> {
//                    state.getController().forceAnimationReset();
                    if (this.getIsEmerging()){
                        state.setAnimation(DIGGING_OUT);
                        return PlayState.CONTINUE;
                    }
                    else{
                        state.getController().forceAnimationReset();
                        return PlayState.STOP;
                    }
                })
        );
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
    //Speed on water
    @Override
    protected float getBaseMovementSpeedMultiplier() {
        return 0.93F;
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


    private void spawnGroundParticles() {
        Random random = this.getRandom();
        BlockState blockState = this.getSteppingBlockState();
        if (blockState.getRenderType() != BlockRenderType.INVISIBLE) {
            for(int i = 0; i < 11; ++i) {
                double d = this.getX() + (double)MathHelper.nextBetween(random, -0.7F, 0.7F);
                double e = this.getY();
                double f = this.getZ() + (double)MathHelper.nextBetween(random, -0.7F, 0.7F);

                if(i==5
                ){
                    this.getWorld().addParticle(ParticleTypes.SPLASH, d, e, f, 0.0, 0.0, 0.0);
                }
                else{
                    this.getWorld().addParticle(new BlockStateParticleEffect(ParticleTypes.BLOCK, blockState), d, e, f, 0.0, 0.0, 0.0);

                }

            }
        }
    }

    //Timers
    int AnimationParticlesTicks=36;
    @Override
    public void tick(){
        if(this.puddle==null){
            this.puddle=DetectOwnPuddle();
        }

        //Start the counter for the Lunge attack
        if (DrownerEntity.this.LungeTicks > 0) {
            DrownerEntity.this.setIsLugging(false);
            --DrownerEntity.this.LungeTicks;
        }else {
            DrownerEntity.this.cooldownBetweenLunges = false;
        }

        //Particles when return to ground
        if(this.AnimationParticlesTicks > 0 && this.getInGroundDataTracker()){
            this.spawnGroundParticles();
            --this.AnimationParticlesTicks;
        }

        if(!this.getInGroundDataTracker() && !this.getIsEmerging()){
            AnimationParticlesTicks=36;
        }

        //Particles when emerges from ground
        if(this.getIsEmerging()){
            this.spawnGroundParticles();
        }

        super.tick();
    }


    @Override
    public void mobTick(){
        //Only timer for return to ground if it's not swimming
        if(!DrownerEntity.this.getSwimmingDataTracker()) {
            if (DrownerEntity.this.ReturnToGround_Ticks > 0
            && !this.getIsEmerging()
            && !this.isAttacking()
            ) {
                --DrownerEntity.this.ReturnToGround_Ticks;
            }else{
                BlockPos entityPos = new BlockPos((int)this.getX(), (int)this.getY(), (int)this.getZ());

                //Makes the Drowner return to the dirt
                if(DrownerEntity.this.ReturnToGround_Ticks==0 && (
                this.getWorld().getBlockState(entityPos.down()).isIn(BlockTags.DIRT) ||
                this.getWorld().getBlockState(entityPos.down()).isOf(Blocks.SAND)
                )
                ){
                    this.setInGroundDataTracker(true);
                }
            }
        }
        else{
            if(this.getInGroundDataTracker()){
                this.setInGroundDataTracker(false);}
        }
        super.mobTick();
    }

    //Natural Spawn
    public static boolean canSpawnDrowner(EntityType<? extends DrownerEntity> type, ServerWorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
        //If it's in ocean or river
        if(
                world.getBiome(pos).isIn(BiomeTags.IS_OCEAN)
                || world.getBiome(pos).isIn(BiomeTags.IS_DEEP_OCEAN)
                || world.getBiome(pos).isIn(BiomeTags.IS_RIVER))

        {
            return
                    spawnReason == SpawnReason.SPAWNER ||(
                    world.getDifficulty() != Difficulty.PEACEFUL &&
                    world.getFluidState(pos.down()).isIn(FluidTags.WATER)
                    )
            ;
                }
        //If it's in another spawneable biome
        else{
            return
                    spawnReason == SpawnReason.SPAWNER ||(
                    world.getDifficulty() != Difficulty.PEACEFUL &&
                    pos.getY() >= world.getSeaLevel() - 10 &&

                    (world.getBlockState(pos.east(20).down(5)).isOf(Blocks.WATER) ||
                     world.getBlockState(pos.west(20).down(5)).isOf(Blocks.WATER) ||
                     world.getBlockState(pos.north(20).down(5)).isOf(Blocks.WATER)||
                     world.getBlockState(pos.south(20).down(5)).isOf(Blocks.WATER)) &&


                    (  world.getBlockState(pos.down()).isOf(Blocks.SAND)
                    || world.getBlockState(pos.down()).isOf(Blocks.DIRT)
                    || world.getBlockState(pos.down()).isIn(BlockTags.FROGS_SPAWNABLE_ON))
                    );
        }
    }

    @Override
    protected boolean isDisallowedInPeaceful() {
        return true;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    //Sounds
    @Override
    protected SoundEvent getAmbientSound() {
        if(!this.getInGroundDataTracker()){
            return TCOTS_Sounds.DROWNER_IDLE;
        }
        else{
            return null;
        }
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


    @Override
    public void checkDespawn() {
        if (this.getWorld().getDifficulty() == Difficulty.PEACEFUL && this.isDisallowedInPeaceful()) {
            if(DrownerEntity.this.puddle!=null){
                DrownerEntity.this.puddle.discard();
            }
            this.discard();
        } else if (!this.isPersistent() && !this.cannotDespawn()) {
            Entity entity = this.getWorld().getClosestPlayer(this, -1.0);
            if (entity != null) {
                double d = entity.squaredDistanceTo(this);
                int i = this.getType().getSpawnGroup().getImmediateDespawnRange();
                int j = i * i;
                if (d > (double)j && this.canImmediatelyDespawn(d)) {
                    if(DrownerEntity.this.puddle!=null){
                        DrownerEntity.this.puddle.discard();
                    }
                    this.discard();
                }

                int k = this.getType().getSpawnGroup().getDespawnStartRange();
                int l = k * k;
                if (this.despawnCounter > 600 && this.random.nextInt(800) == 0 && d > (double)l && this.canImmediatelyDespawn(d)) {
                    if(DrownerEntity.this.puddle!=null){
                        DrownerEntity.this.puddle.discard();
                    }
                    this.discard();
                } else if (d < (double)l) {
                    this.despawnCounter = 0;
                }
            }

        } else {
            this.despawnCounter = 0;
        }
    }

}
