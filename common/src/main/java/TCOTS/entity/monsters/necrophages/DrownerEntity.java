package TCOTS.entity.monsters.necrophages;

import TCOTS.registry.TCOTS_Sounds;
import TCOTS.entity.goals.*;
import TCOTS.entity.interfaces.ExcavatorMob;
import TCOTS.entity.interfaces.LungeMob;
import TCOTS.utils.GeoControllersUtil;
import TCOTS.entity.misc.DrownerPuddleEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.*;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.RandomSwimmingGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.entity.animal.AbstractFish;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Dolphin;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.animal.Squid;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;

@SuppressWarnings({"deprecation", "unused"})
public class DrownerEntity extends NecrophageMonster implements GeoEntity, ExcavatorMob, LungeMob {
//xTODO: Make it faster when walking in water
//xTODO: Make it that it don't sink in water
//xTODO: Fix the data tracker
//xTODO: Add the digging in ground goal
//xTODO: Add sounds when digging
//xTODO: Make the puddle entity spawn when digging
//xTODO: Add drops (Add loot tables)
//xTODO: Add spawn
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    protected final WaterBoundPathNavigation waterNavigation;
    protected final GroundPathNavigation landNavigation;

    public static final RawAnimation WATER_IDLE = RawAnimation.begin().thenLoop("idle.water");
    public static final RawAnimation SWIMMING= RawAnimation.begin().thenLoop("move.swimming");
    public static final RawAnimation LUNGE = RawAnimation.begin().thenPlay("attack.lunge");
    public static final RawAnimation WATER_ATTACK1 = RawAnimation.begin().thenPlay("attack.waterswing1");
    public static final RawAnimation WATER_ATTACK2 = RawAnimation.begin().thenPlay("attack.waterswing2");
    protected static final EntityDataAccessor<Boolean> SWIM = SynchedEntityData.defineId(DrownerEntity.class, EntityDataSerializers.BOOLEAN);
    protected static final EntityDataAccessor<Boolean> InGROUND = SynchedEntityData.defineId(DrownerEntity.class, EntityDataSerializers.BOOLEAN);
    protected static final EntityDataAccessor<Boolean> EMERGING = SynchedEntityData.defineId(DrownerEntity.class, EntityDataSerializers.BOOLEAN);
    protected static final EntityDataAccessor<Boolean> LUGGING = SynchedEntityData.defineId(DrownerEntity.class, EntityDataSerializers.BOOLEAN);
    protected static final EntityDataAccessor<Boolean> SPAWNED_PUDDLE = SynchedEntityData.defineId(DrownerEntity.class, EntityDataSerializers.BOOLEAN);
    protected static final EntityDataAccessor<Boolean> INVISIBLE = SynchedEntityData.defineId(DrownerEntity.class, EntityDataSerializers.BOOLEAN);

    public DrownerEntity(final EntityType<? extends DrownerEntity> entityType, final Level world) {
        super(entityType, world);

        this.setPathfindingMalus(PathType.WATER, 0.0F);
        this.moveControl = new WaterOrLand_MoveControl(this, 0.07F);
        this.lookControl = new DrownerLookControl(this, 90);
        this.waterNavigation = new WaterBoundPathNavigation(this, world);
        this.landNavigation = new GroundPathNavigation(this, world);
    }

    @Override
    protected void registerGoals()
    {
        //Flee, lunge, emerging and Water/land attack
        this.goalSelector.addGoal(0, new Drowner_FleeFromTarget(this,1.0, 1));

        //Emerge from ground
        this.goalSelector.addGoal(0, new EmergeFromGroundGoal_Excavator(this, 100, true));

        this.goalSelector.addGoal(1, new Drowner_Attack_Lunge(this,100, 0.9));

        //Returns to ground
        this.goalSelector.addGoal(2, new ReturnToGroundGoal_Excavator(this, true));

        this.goalSelector.addGoal(3, new Drowner_LandWaterAttackGoal(this, 1.2D, false, 40, 3000));

//        this.goalSelector.add(4, new FollowWaterHag(this, 0.75, 3,7));
        this.goalSelector.addGoal(5, new Drowner_SwimAroundGoal(this, 0.75f, 10));
        this.goalSelector.addGoal(6, new WanderAroundGoal_Excavator(this, 0.75f, 20));
//        this.goalSelector.add(4, new FollowWaterHag(this, 0.75, 3,7));
        this.goalSelector.addGoal(7, new LookAroundGoal_Excavator(this));



        //Objectives
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, AbstractVillager.class, true));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Axolotl.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Dolphin.class, true));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, AbstractFish.class, true));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, Squid.class, true));
    }

    //Conditions if Drowner is swimming
    public boolean canDrownerUnderwaterAttackTarget(@Nullable final LivingEntity target) {
        if (target != null) {
            return target.isInWater();
        } else {
            return false;}
    }
    //Updates the data tracker and the navigation
    @Override
    public void updateSwimming() {
        if (!this.level().isClientSide) {
            if (this.isEffectiveAi() && this.isUnderWater()) {
                this.navigation = this.waterNavigation;
                if(this.isAlive()){
                    this.setSwimmingDataTracker(true);}
                this.setSwimming(true);
            } else {

                if (this.isInWater() && this.getSwimmingDataTracker()) {
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
        public WaterOrLand_MoveControl(final DrownerEntity entity, final float waterSpeed) {
            super(entity);
            this.waterSpeed = waterSpeed;
            this.drowner=entity;
        }

        public void handleUnderwaterMovement(){
            final LivingEntity target = this.mob.getTarget();
            final double dXtoTarget = this.wantedX - this.mob.getX();
            final double dYtoTarget = this.wantedY - this.mob.getY();
            final double dZtoTarget = this.wantedZ - this.mob.getZ();
            final double length = Math.sqrt(dXtoTarget * dXtoTarget + dYtoTarget * dYtoTarget + dZtoTarget * dZtoTarget);

            //Movement Vector
            final Vec3 vec3D_movementUnderwater = new Vec3((dXtoTarget / length) * waterSpeed,
                                                       (dYtoTarget / length) * waterSpeed,
                                                       (dZtoTarget / length) * waterSpeed);

            //Makes it go up if it's attacking
            if(target != null
                    && target.getY() > this.mob.getY()
                    //If the target it's attackable
                    && drowner.canDrownerUnderwaterAttackTarget(target)
                    //Target it's not submerged
                    && !target.isUnderWater())
            {this.mob.setDeltaMovement(this.mob.getDeltaMovement().add(0.0, 0.02, 0.0));}

            //It's not underwater, it's touching water, but it's not in the ground
            if(!mob.onGround() && mob.isInWater() && !mob.isUnderWater()) {
                //It's attacking in the water, but it's not submerged
                if (drowner.canDrownerUnderwaterAttackTarget(target)) {
                    this.mob.setDeltaMovement(this.mob.getDeltaMovement().add(0.0, -0.1, 0.0));
                }
            }

            if(mob.onGround() && drowner.getSwimmingDataTracker() && !mob.isAggressive()){
//                if (drowner.canDrownerUnderwaterAttackTarget(target)) {
                    this.mob.setDeltaMovement(this.mob.getDeltaMovement().add(0.0, 0.08, 0.0));
//                }
            }

            //It's moving (no idle)
            if (this.operation == Operation.MOVE_TO && !this.mob.getNavigation().isDone()) {
                //Calculate what angle the entity needs to see
                //Calculate the angle up and down (Y)
                final float hipoY = (float) (Mth.atan2(dZtoTarget, dXtoTarget) * 57.2957763671875) - 90.0F;
                this.mob.setYRot(this.rotlerp(this.mob.getYRot(),hipoY,90));

                //Applies the 3D Movement Vector
                mob.walkAnimation.setSpeed(1);
                mob.walkAnimation.setSpeed(1);
                mob.move(MoverType.SELF, new Vec3(vec3D_movementUnderwater.x, vec3D_movementUnderwater.y, vec3D_movementUnderwater.z));

                //Keep it underwater
                if(drowner.canDrownerUnderwaterAttackTarget(target)) {
                    assert target != null;
                    if (!target.isUnderWater()) {
                        this.mob.setDeltaMovement(this.mob.getDeltaMovement().add(0.0, -0.001, 0.0));
                    }
                }
            }
            else {
                this.mob.setSpeed(0.0F);
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

        public DrownerLookControl(final DrownerEntity entity, final int yawAdjustThreshold) {
            super(entity);
            this.yawAdjustThreshold = yawAdjustThreshold;
            this.drowner = entity;
        }

        @Override
        public void tick() {
            if (drowner.getSwimmingDataTracker()){
                if (this.lookAtCooldown > 0) {
                    --this.lookAtCooldown;
                    this.getYRotD().ifPresent((yaw) ->
                        this.mob.yHeadRot = this.rotateTowards(this.mob.yHeadRot, yaw + ADDED_YAW, this.yMaxRotSpeed)
                    );
                    this.getXRotD().ifPresent((pitch) ->
                        this.mob.setXRot(this.rotateTowards(this.mob.getXRot(), pitch + ADDED_PITCH, this.xMaxRotAngle))
                    );
                } else {
                    if (this.mob.getNavigation().isDone()) {
                        this.mob.setXRot(this.rotateTowards(this.mob.getXRot(), 0.0F, 5.0F));
                    }
                    this.mob.yHeadRot = this.rotateTowards(this.mob.yHeadRot, this.mob.yBodyRot, this.yMaxRotSpeed);
                }

                //WrapDegrees = Rotates an angle from one point to another
                final float f = Mth.wrapDegrees(this.mob.yHeadRot - this.mob.yBodyRot);
                final Mob entity;
                if (f < (float)(-this.yawAdjustThreshold)) {
                    entity = this.mob;
                    entity.yBodyRot -= 4.0F;
                } else if (f > (float)this.yawAdjustThreshold) {
                    entity = this.mob;
                    entity.yBodyRot += 4.0F;
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
    @Override
    public int getLungeTicks() {
        return LungeTicks;
    }

    @Override
    public void setLungeTicks(final int lungeTicks) {
        LungeTicks = lungeTicks;
    }
    public int ReturnToGround_Ticks=20;

    public int getReturnToGround_Ticks() {
        return ReturnToGround_Ticks;
    }

    public void setReturnToGround_Ticks(final int returnToGround_Ticks) {
        ReturnToGround_Ticks = returnToGround_Ticks;
    }
    public boolean cooldownBetweenLunges=false;
    @Override
    public boolean getNotCooldownBetweenLunges() {
        return !cooldownBetweenLunges;
    }

    @Override
    public void setCooldownBetweenLunges(final boolean cooldownBetweenLunges) {
        this.cooldownBetweenLunges = cooldownBetweenLunges;
    }

    //To manage attacks in and outside water
    private class Drowner_LandWaterAttackGoal extends MeleeAttackGoal_Animated {
        private final DrownerEntity drowner;
        private final int cooldownBetweenWaterAttacks;
        private final int ticksBeforeToGround;

        public Drowner_LandWaterAttackGoal(final DrownerEntity mob, final double speed, final boolean pauseWhenMobIdle, final int cooldownBetweenWaterAttacks, final int ticksBeforeToGround) {
            super(mob, speed, pauseWhenMobIdle, 2);
            this.drowner = mob;
            this.cooldownBetweenWaterAttacks = cooldownBetweenWaterAttacks;
            this.ticksBeforeToGround = ticksBeforeToGround;
        }

        @Override
        public boolean canUse() {
            // If the Drowner it's swimming only attacks creatures on water
            if (drowner.getSwimmingDataTracker()) {
                return super.canUse() && this.drowner.canDrownerUnderwaterAttackTarget(this.drowner.getTarget()) && !drowner.hasAttackedOnWater;
            } else {
                return super.canUse()
                        && !this.drowner.getIsEmerging()
                        && !this.drowner.getInGround();
            }
        }
        @Override
        public boolean canContinueToUse() {
            if(drowner.getSwimmingDataTracker()){
                return super.canContinueToUse() && this.drowner.canDrownerUnderwaterAttackTarget(this.drowner.getTarget()) && !drowner.hasAttackedOnWater;
            }
            else{
                return super.canContinueToUse();
            }
        }

        @Override
        protected void attack(final LivingEntity target) {
            //Special logic for water attack
            if (drowner.getSwimmingDataTracker()) {
                if(target!=null){
                WaterAttackLogic(target);}
            } else {
                //Lunge and land attack
                if(target!=null){
                    final int randomExtra = DrownerEntity.this.random.nextIntBetweenInclusive(1,51);

                        DrownerEntity.this.ReturnToGround_Ticks=this.ticksBeforeToGround + randomExtra;

                    super.attack(target);
                }
            }
        }

        //Method for water attack
        private void WaterAttackLogic(final LivingEntity target) {
            if(!drowner.hasAttackedOnWater) {

                if (this.canAttack(target)) {
                    this.resetCooldown();
                    this.mob.swing(InteractionHand.MAIN_HAND);

                    //Triggers attack animation
                    final int randomAttack = this.mob.getRandom().nextIntBetweenInclusive(0, 1);
                    if (randomAttack == 0) {
                        drowner.triggerAnim("AttackController", "waterAttack1");
                    } else {
                        drowner.triggerAnim("AttackController", "waterAttack2");
                    }


                    //Makes the target go down on water
                    target.setDeltaMovement(target.getDeltaMovement().add(0.0, -0.3, 0.0));
                    this.mob.doHurtTarget(target);
                    drowner.cooldownSwimmingAttackTicks = cooldownBetweenWaterAttacks;
                    drowner.hasAttackedOnWater = true;
                }
            }
        }
    }
    //To manage the lunge attack
    private class Drowner_Attack_Lunge extends LungeAttackGoal {

        public Drowner_Attack_Lunge(final PathfinderMob mob, final int cooldownBetweenLungesAttacks, final double lungeImpulse) {
            super(mob, cooldownBetweenLungesAttacks, lungeImpulse,5,25);
        }


        @Override
        public boolean canUse() {
            return super.canUse() && !DrownerEntity.this.getSwimmingDataTracker();
        }

        @Override
        public boolean canContinueToUse(){
            return super.canContinueToUse() && !DrownerEntity.this.getSwimmingDataTracker();
        }
    }
    //Makes the drowner flee after a hit that it make
    private static class Drowner_FleeFromTarget extends RandomSwimmingGoal{
        private final DrownerEntity drowner;
        private Drowner_FleeFromTarget(final DrownerEntity drowner, final double speed, final int probability) {
            super(drowner, speed, probability);
            this.drowner = drowner;
        }

        @Override
        public boolean canUse() {
            return super.canUse() && drowner.hasAttackedOnWater;
        }
        @Override
        public boolean canContinueToUse(){
            return super.canContinueToUse() && drowner.hasAttackedOnWater;
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
        protected Vec3 getPosition() {
            return BehaviorUtils.getRandomSwimmablePos(this.mob, 4, 3);
        }
    }

    private DrownerPuddleEntity puddle;

    public DrownerPuddleEntity getPuddle() {
        return puddle;
    }

    public void setPuddle(final DrownerPuddleEntity puddle) {
        this.puddle = puddle;
    }

    @Override
    public boolean isInvulnerableTo(@NotNull final DamageSource damageSource) {
        return this.getIsEmerging() || this.getInGround() || super.isInvulnerableTo(damageSource);
    }
    @Override
    public boolean isPushable() {
        return super.isPushable() && !this.getIsEmerging() && !this.getInGround();
    }

    @Override
    public boolean fireImmune() {
        return super.fireImmune() || this.getInGround();
    }

    private class Drowner_SwimAroundGoal extends RandomSwimmingGoal{

        public Drowner_SwimAroundGoal(final PathfinderMob pathAwareEntity, final double d, final int i) {
            super(pathAwareEntity, d, i);
        }

        @Override
        public boolean canUse(){
            return super.canUse() && !DrownerEntity.this.getInGround();
        }

        @Override
        public boolean canContinueToUse(){
            return super.canContinueToUse() && !DrownerEntity.this.getInGround();
        }
    }

    //This way don't reset every time it reenter the world, causing clipping
    @Override
    public void addAdditionalSaveData(@NotNull final CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putBoolean("Swimming", this.entityData.get(SWIM));
        nbt.putBoolean("InGround", this.entityData.get(InGROUND));
        nbt.putInt("ReturnToGroundTicks", this.ReturnToGround_Ticks);
        nbt.putBoolean("PuddleSpawned",this.entityData.get(SPAWNED_PUDDLE));
        nbt.putBoolean("Invisible",this.entityData.get(INVISIBLE));
    }
    @Override
    public void readAdditionalSaveData(final CompoundTag nbt) {
        this.setSwimmingDataTracker(nbt.getBoolean("Swimming"));
        this.setInGround(nbt.getBoolean("InGround"));
        this.ReturnToGround_Ticks = nbt.getInt("ReturnToGroundTicks");
        this.setSpawnedPuddleDataTracker(nbt.getBoolean("PuddleSpawned"));
        this.setInvisibleData(nbt.getBoolean("Invisible"));
        super.readAdditionalSaveData(nbt);
    }

    public boolean getSwimmingDataTracker() {
        return this.entityData.get(SWIM);
    }
    public void setSwimmingDataTracker(final boolean wasSwimming) {
        this.entityData.set(SWIM, wasSwimming);
    }

    public final boolean getIsEmerging(){
        return this.entityData.get(EMERGING);
    }
    public final void setIsEmerging(final boolean wasEmerging){
        this.entityData.set(EMERGING, wasEmerging);
    }

    public boolean getInGround() {
        return this.entityData.get(InGROUND);
    }
    public void setInGround(final boolean wasInGround) {
        this.entityData.set(InGROUND, wasInGround);
    }

    public boolean getSpawnedPuddleDataTracker() {
        return this.entityData.get(SPAWNED_PUDDLE);
    }
    public void setSpawnedPuddleDataTracker(final boolean puddleSpawned) {this.entityData.set(SPAWNED_PUDDLE, puddleSpawned);}

    public final boolean getInvisibleData() {
        return this.entityData.get(INVISIBLE);
    }
    public final void setInvisibleData(final boolean isInvisible) {
        this.entityData.set(INVISIBLE, isInvisible);
    }

    //Dynamic hitbox
    @Override
    protected @NotNull AABB makeBoundingBox() {
        if (entityData.get(SWIM)) {
            // Tiny hit-box when swim
            return new AABB(this.getX() - 0.39, this.getY()+1.65, this.getZ() - 0.39,
                    this.getX() + 0.39, this.getY()+0.6, this.getZ() + 0.39);
        } else {
            if (entityData.get(InGROUND)) {
                return groundBox(this);
            }
            else{
                // Normal hit-box otherwise
                return super.makeBoundingBox();
            }
        }
    }

    @Override
    protected void defineSynchedData(final SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(SWIM, Boolean.FALSE);
        builder.define(LUGGING, Boolean.FALSE);
        builder.define(InGROUND, Boolean.FALSE);
        builder.define(EMERGING, Boolean.FALSE);
        builder.define(SPAWNED_PUDDLE, Boolean.FALSE);
        builder.define(INVISIBLE, Boolean.FALSE);
    }

    @Override
    protected @NotNull EntityDimensions getDefaultDimensions(@NotNull final Pose pose) {
        return this.getInGround()? this.getType().getDimensions().withEyeHeight(0.1f): super.getDefaultDimensions(pose);
    }

    @Override
    public void onSyncedDataUpdated(@NotNull final EntityDataAccessor<?> data) {
        super.onSyncedDataUpdated(data);
        if (entityData.get(SWIM) || !entityData.get(SWIM) || !this.getInGround() || this.getInGround()) {
            this.setBoundingBox(this.makeBoundingBox());
            this.refreshDimensions();
        }
    }

    public static AttributeSupplier.Builder setAttributes() {
        return Animal.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.ATTACK_DAMAGE, 4.0f) //Amount of health that hurts you
                .add(Attributes.MOVEMENT_SPEED, 0.27f);
    }

    //Each Control can only play one animation at the time
    @Override
    public void registerControllers(final AnimatableManager.ControllerRegistrar controllerRegistrar) {

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
                            if (this.isAggressive() && state.isMoving()) {
                                return state.setAndContinue(RUNNING);
                            }
                            //It's not attacking and/or it's no moving
                            else {
                                //If it's attacking but NO moving
                                if (isAggressive()) {
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
        }
        ));

        //Attack Controller
        controllerRegistrar.add(
                new AnimationController<>(this, "AttackController", 1, state -> PlayState.STOP)
                        .triggerableAnim("attack1", GeoControllersUtil.ATTACK1)
                        .triggerableAnim("attack2", GeoControllersUtil.ATTACK2)
                        .triggerableAnim("waterAttack1", WATER_ATTACK1)
                        .triggerableAnim("waterAttack2", WATER_ATTACK2)
        );

        //Lunge Controller
        lungeAnimationController(this, controllerRegistrar);

        //Digging Controller
        controllerRegistrar.add(
                new AnimationController<>(this,"DiggingController",1, this::animationDiggingPredicate)
        );

        //Emerging Controller
        controllerRegistrar.add(
                new AnimationController<>(this, "EmergingController", 1, this::animationEmergingPredicate)
        );
    }

    //Water creature things
    @Override
    public boolean isPushedByFluid() {
        return false;
    }
    //Speed on water
    @Override
    protected float getWaterSlowDown() {
        return 0.93F;
    }

    //To kill Drowner in one shot with a crossbow arrow
    @Override
    public boolean hurt(final DamageSource source, final float amount) {
        //If it's a projectile
        if (source.getDirectEntity() instanceof final AbstractArrow arrow) {
            final Entity shooter = source.getEntity();
            //If the shooter it's on the water, the arrow was shot from a crossbow and the Drowner it's swimming
            if (shooter != null && arrow.shotFromCrossbow() && shooter.isInWater() && this.getSwimmingDataTracker()) {
                return super.hurt(source, 200);
            }
        }
        return super.hurt(source, amount);
    }

    @Override
    public void spawnGroundParticles(@NotNull final LivingEntity entity) {
        final RandomSource random = entity.getRandom();
        final BlockState blockState = entity.getBlockStateOn();
        if (blockState.getRenderShape() != RenderShape.INVISIBLE) {
            for(int i = 0; i < 11; ++i) {
                final double d = entity.getX() + (double)Mth.randomBetween(random, -0.7F, 0.7F);
                final double e = entity.getY();
                final double f = entity.getZ() + (double)Mth.randomBetween(random, -0.7F, 0.7F);

                if(i==5
                ){
                    entity.level().addParticle(ParticleTypes.SPLASH, d, e, f, 0.0, 0.0, 0.0);
                }
                else{
                    entity.level().addParticle(new BlockParticleOption(ParticleTypes.BLOCK, blockState), d, e, f, 0.0, 0.0, 0.0);
                }

            }
        }
    }

    //Timers
    int AnimationParticlesTicks=36;

    public int getAnimationParticlesTicks() {
        return AnimationParticlesTicks;
    }

    public void setAnimationParticlesTicks(final int animationParticlesTicks) {
        AnimationParticlesTicks = animationParticlesTicks;
    }

    @Override
    public void tick(){
        //Detect own puddle
        tickPuddle(this);

        //Counter for Lunge attack
        this.tickLunge();
        //Counter for particles
        this.tickExcavator(this);

        super.tick();
    }


    @Override
    public void customServerAiStep(){
        //Only timer for return to ground if it's not swimming
        if(!DrownerEntity.this.getSwimmingDataTracker()) {
            mobTickExcavator(
                    List.of(BlockTags.DIRT),
                    List.of(Blocks.SAND),
                    this
            );
        }
        else{
            if(this.getInGround()){
                this.setInGround(false);}
        }

        this.setInvisible(this.getInvisibleData());
        super.customServerAiStep();
    }

    @Override
    public boolean checkSpawnObstruction(@NotNull final LevelReader world) {
        return world.isUnobstructed(this);
    }


    //Natural Spawn
    public static boolean canSpawnDrowner(final EntityType<? extends NecrophageMonster> type, final ServerLevelAccessor world, final MobSpawnType spawnReason, final BlockPos pos, final RandomSource random) {
        //If it's in ocean or river
        if(
                world.getBiome(pos).is(BiomeTags.IS_OCEAN)
                || world.getBiome(pos).is(BiomeTags.IS_DEEP_OCEAN)
                || world.getBiome(pos).is(BiomeTags.IS_RIVER))

        {
            final int seaLevel = world.getSeaLevel();
            final int underSeaLevel = seaLevel - 13;

            return spawnReason == MobSpawnType.SPAWNER ||
                    (world.getDifficulty() != Difficulty.PEACEFUL
                            && pos.getY() >= underSeaLevel
                            && pos.getY() <= seaLevel
                            && world.getFluidState(pos.below()).is(FluidTags.WATER)
                            && world.getBlockState(pos.above()).is(Blocks.WATER));
        }
        //If it's in another spawneable biome
        else{
            return
                    spawnReason == MobSpawnType.SPAWNER ||(
                    world.getDifficulty() != Difficulty.PEACEFUL &&
                    pos.getY() >= world.getSeaLevel() - 10 &&

                    (world.getBlockState(pos.east(20).below(5)).is(Blocks.WATER) ||
                     world.getBlockState(pos.west(20).below(5)).is(Blocks.WATER) ||
                     world.getBlockState(pos.north(20).below(5)).is(Blocks.WATER)||
                     world.getBlockState(pos.south(20).below(5)).is(Blocks.WATER)) &&


                    (  world.getBlockState(pos.below()).is(Blocks.SAND)
                    || world.getBlockState(pos.below()).is(Blocks.DIRT)
                    || world.getBlockState(pos.below()).is(BlockTags.FROGS_SPAWNABLE_ON))
                    );
        }
    }

    //Sounds
    @Override
    protected SoundEvent getAmbientSound() {
        if(!this.getInGround()){
            return TCOTS_Sounds.getSoundEvent("drowner_idle");
        }
        else{
            return null;
        }
    }
    @Override
    protected @NotNull SoundEvent getSwimSound() {
        return SoundEvents.PLAYER_SWIM;
    }
    @Override
    protected @NotNull SoundEvent getHurtSound(@NotNull final DamageSource source) {
        return TCOTS_Sounds.getSoundEvent("drowner_hurt");
    }
    @Override
    protected @NotNull SoundEvent getDeathSound() {
        return TCOTS_Sounds.getSoundEvent("drowner_death");
    }

    @Override
    public SoundEvent getEmergingSound() {
        return TCOTS_Sounds.getSoundEvent("drowner_emerging");
    }

    @Override
    public SoundEvent getDiggingSound() {
        return TCOTS_Sounds.getSoundEvent("drowner_digging");
    }

    @Override
    public SoundEvent getLungeSound() {
        return TCOTS_Sounds.getSoundEvent("drowner_lunge");
    }

    //Footsteps sounds
    protected SoundEvent getStepSound() {
        return TCOTS_Sounds.getSoundEvent("drowner_footstep");
    }

    //Attack Sound
    @Override
    protected SoundEvent getAttackSound() {
        return TCOTS_Sounds.getSoundEvent("drowner_attack");
    }

    @Override
    public void checkDespawn() {
        if (this.level().getDifficulty() == Difficulty.PEACEFUL && this.shouldDespawnInPeaceful()) {
            if(DrownerEntity.this.puddle!=null){
                DrownerEntity.this.puddle.discard();
            }
            this.discard();
        } else if (!this.isPersistenceRequired() && !this.requiresCustomPersistence()) {
            final Entity entity = this.level().getNearestPlayer(this, -1.0);
            if (entity != null) {
                final double d = entity.distanceToSqr(this);
                final int i = this.getType().getCategory().getDespawnDistance();
                final int j = i * i;
                if (d > (double)j && this.removeWhenFarAway(d)) {
                    if(DrownerEntity.this.puddle!=null){
                        DrownerEntity.this.puddle.discard();
                    }
                    this.discard();
                }

                final int k = this.getType().getCategory().getNoDespawnDistance();
                final int l = k * k;
                if (this.noActionTime > 600 && this.random.nextInt(800) == 0 && d > (double)l && this.removeWhenFarAway(d)) {
                    if(DrownerEntity.this.puddle!=null){
                        DrownerEntity.this.puddle.discard();
                    }
                    this.discard();
                } else if (d < (double)l) {
                    this.noActionTime = 0;
                }
            }

        } else {
            this.noActionTime = 0;
        }
    }


    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}

