package mors.tcots.entity.monsters.necrophages;

import mors.tcots.client.geo.animation.entity.necrophage.DevourerAnimations;
import mors.tcots.entity.goal.*;
import mors.tcots.registry.TCOTS_Sounds;
import mors.tcots.registry.TCOTS_Tags;
import mors.tcots.entity.interfaces.ExcavatorMob;
import mors.tcots.registry.TCOTS_Effects;
import mors.tcots.utils.TCOTS_EntitiesUtil;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.AABB;

public class DevourerEntity extends NecrophageMonster implements GeoEntity, ExcavatorMob {

    //xTODO: Finish bestiary
    //xTODO: Make the water cancel the jump
    //xTODO: Fix the jump particles
    //xTODO: Add jumping animation
    //xTODO: Add natural spawn
    //xTODO: Add drop

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    private static final byte FALLING_PARTICLES = 42;
    public static final RawAnimation JUMP = RawAnimation.begin().thenPlayAndHold("special.jumping");
    public static final RawAnimation LANDING = RawAnimation.begin().thenPlay("special.landing");

    protected static final EntityDataAccessor<Boolean> InGROUND = SynchedEntityData.defineId(DevourerEntity.class, EntityDataSerializers.BOOLEAN);
    protected static final EntityDataAccessor<Boolean> EMERGING = SynchedEntityData.defineId(DevourerEntity.class, EntityDataSerializers.BOOLEAN);
    protected static final EntityDataAccessor<Boolean> INVISIBLE = SynchedEntityData.defineId(DevourerEntity.class, EntityDataSerializers.BOOLEAN);


    protected static final EntityDataAccessor<Boolean> FALLING = SynchedEntityData.defineId(DevourerEntity.class, EntityDataSerializers.BOOLEAN);
    protected static final EntityDataAccessor<Float> FALLING_DISTANCE = SynchedEntityData.defineId(DevourerEntity.class, EntityDataSerializers.FLOAT);


    public DevourerEntity(final EntityType<? extends DevourerEntity> entityType, final Level world) {
        super(entityType, world);
        xpReward=10;
    }

    @Override
    public int getMaxHeadYRot() {
        return 43;
    }

    public static AttributeSupplier.Builder setAttributes() {
        return Animal.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 30.0D)
                .add(Attributes.ATTACK_DAMAGE, 5.0f) //Amount of health that hurts you
                .add(Attributes.MOVEMENT_SPEED, 0.22f)

                .add(Attributes.KNOCKBACK_RESISTANCE, 0.5);
    }

    @Override
    protected void registerGoals() {
        //Emerge from ground
        this.goalSelector.addGoal(0, new EmergeFromGroundGoal_Excavator(this, 500));
        this.goalSelector.addGoal(1, new FloatGoal(this));

        //Returns to ground
        this.goalSelector.addGoal(2, new ReturnToGroundGoal_Excavator(this));

        this.goalSelector.addGoal(3, new DevourerJumpAttack(this,140, 60));

        this.goalSelector.addGoal(4, new Devourer_MeleeAttackGoal(this, 1.2D, false, 3600));

        this.goalSelector.addGoal(5, new WanderAroundGoal_Excavator(this, 0.75f, 20));

        this.goalSelector.addGoal(6, new LookAroundGoal_Excavator(this));

        //Objectives
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this, DevourerEntity.class));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, AbstractVillager.class, true));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
    }

    private static class DevourerJumpAttack extends Goal {

        private final DevourerEntity devourer;
        private final int minJumpCooldown;
        private final int maxJumpCooldown;

        public DevourerJumpAttack(final DevourerEntity devourer, final int minJumpCooldown, final int maxExtraRandomCooldown) {
            this.devourer=devourer;
            this.minJumpCooldown = minJumpCooldown;
            this.maxJumpCooldown = maxExtraRandomCooldown;
        }

        @Override
        public boolean canUse() {
            final LivingEntity target = this.devourer.getTarget();

            if (target != null) {

                return !this.devourer.cooldownBetweenJumps
                        && this.devourer.isAggressive()
                        && !this.devourer.isInLiquid()
                        && !this.devourer.isPassenger()
                        && !this.devourer.level().getBlockState(this.devourer.blockPosition()).is(Blocks.HONEY_BLOCK)
                        && !this.devourer.level().getBlockState(this.devourer.blockPosition()).is(Blocks.COBWEB)
                        && this.devourer.distanceToSqr(target) < 4;
            } else {
                return false;
            }
        }

        @Override
        public void tick() {
            final LivingEntity target = this.devourer.getTarget();

            if(target!=null){
                jumpAttack();
            }
        }

        private void jumpAttack(){
            devourer.jumpFromGround();
            devourer.setIsFalling(true);
            devourer.cooldownBetweenJumps=true;
            devourer.jumpTicks= minJumpCooldown + devourer.random.nextIntBetweenInclusive(0,maxJumpCooldown);
        }
    }

    @Override
    protected int calculateFallDamage(final float fallDistance, final float damageMultiplier) {
        return super.calculateFallDamage(fallDistance, damageMultiplier) - 8;
    }

    @Override
    public void jumpFromGround() {
        this.playSound(TCOTS_Sounds.getSoundEvent("devourer_jump"), 1.0f, 1.0f);
        super.jumpFromGround();
    }

    private static class Devourer_MeleeAttackGoal extends MeleeAttackGoal_Excavator {
        private final DevourerEntity devourer;

        public Devourer_MeleeAttackGoal(final DevourerEntity mob, final double speed, final boolean pauseWhenMobIdle, final int ticksBeforeToGround) {
            super(mob, speed, pauseWhenMobIdle, ticksBeforeToGround, true);
            this.devourer=mob;
        }

        @Override
        public boolean canUse() {
            return super.canUse() && !devourer.isFalling();
        }

        @Override
        public boolean canContinueToUse() {
            return super.canContinueToUse() && !devourer.isFalling();
        }
    }

    @Override
    public void resetFallDistance() {
        if(isFalling()){
            setIsFalling(false);
            if(this.level().getBlockState(this.blockPosition().below()).getFluidState().is(Fluids.EMPTY)
                    && !this.level().getBlockState(this.blockPosition()).is(TCOTS_Tags.Block.NEGATES_DEVOURER_JUMP)
                    && !this.level().getBlockState(this.blockPosition().below()).is(TCOTS_Tags.Block.NEGATES_DEVOURER_JUMP))
            {
                this.triggerAnim("base_controller","landing");
                TCOTS_EntitiesUtil.pushAndDamageEntities(this, 2 + (fallDistance), 1.5 + (fallDistance * 0.2f), 2, 1.2, DevourerEntity.class);
                this.playSound(SoundEvents.HOSTILE_BIG_FALL, 1.0f, 1.0f);
                this.level().broadcastEntityEvent(this, FALLING_PARTICLES);
            }
        }
        super.resetFallDistance();
    }

    @Override
    public void handleEntityEvent(final byte status) {
        if(status==FALLING_PARTICLES){
            TCOTS_EntitiesUtil.spawnImpactParticles(this, 0.85f + (1.5 + (getFallingDistance() * 0.2f)), this.getFallingDistance());
        } else{
            super.handleEntityEvent(status);
        }
    }

    @Override
    protected void defineSynchedData(final SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(InGROUND, Boolean.FALSE);
        builder.define(EMERGING, Boolean.FALSE);
        builder.define(INVISIBLE, Boolean.FALSE);
        builder.define(FALLING, Boolean.FALSE);
        builder.define(FALLING_DISTANCE, fallDistance);
    }

    @Override
    public void registerControllers(final AnimatableManager.ControllerRegistrar controllerRegistrar) {
        //Walk/Idle Controller
        controllerRegistrar.add(DevourerAnimations.mainController(this));

        //DiggingIn Controller
        controllerRegistrar.add(
                new AnimationController<>(this,"DiggingController",1, this::animationDiggingPredicate)
        );

        //DiggingOut Controller
        controllerRegistrar.add(
                new AnimationController<>(this, "EmergingController", 1, this::animationEmergingPredicate)
        );

        //Jumping
        controllerRegistrar.add(
                new AnimationController<>(this, "JumpController", 1, state -> {
                    if(this.isFalling()){
                        state.setAnimation(JUMP);
                        return PlayState.CONTINUE;
                    } else {
                        state.getController().forceAnimationReset();
                        return PlayState.STOP;
                    }
                }));
    }


    @Override
    public boolean canBeAffected(final MobEffectInstance effect) {
        return effect.getEffect() != TCOTS_Effects.SamumEffect() && super.canBeAffected(effect);
    }

    public boolean cooldownBetweenJumps=false;
    private int jumpTicks;

    private void tickJump(){

        if (jumpTicks > 0) {
            --jumpTicks;
        } else {
            cooldownBetweenJumps=false;
        }
    }


    public void setIsFalling(final boolean isFalling) {
        this.entityData.set(FALLING, isFalling);
    }

    public boolean isFalling() {
        return this.entityData.get(FALLING);
    }

    public void setFallingDistance(final float fallingDistance) {
        this.entityData.set(FALLING_DISTANCE, fallingDistance);
    }

    public double getFallingDistance() {
        return this.entityData.get(FALLING_DISTANCE);
    }

    //Excavator Common
    @Override
    public void addAdditionalSaveData(@NotNull final CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putBoolean("InGround", this.entityData.get(InGROUND));
        nbt.putInt("ReturnToGroundTicks", this.ReturnToGround_Ticks);
        nbt.putBoolean("Invisible", this.entityData.get(INVISIBLE));
        nbt.putInt("JumpCooldown", this.jumpTicks);
    }

    @Override
    public void readAdditionalSaveData(final CompoundTag nbt) {
        this.setInGround(nbt.getBoolean("InGround"));
        this.ReturnToGround_Ticks = nbt.getInt("ReturnToGroundTicks");
        this.setInvisibleData(nbt.getBoolean("Invisible"));
        this.jumpTicks=nbt.getInt("JumpCooldown");
        super.readAdditionalSaveData(nbt);
    }

    @Override
    public void tick() {
        //Counter for particles
        this.tickExcavator(this);

        //To sync fallDistance with the client
        setFallingDistance(fallDistance);

        //Tick for jumps
        this.tickJump();

        super.tick();
    }

    @Override
    protected void customServerAiStep() {
        mobTickExcavator(
                List.of(BlockTags.DIRT, BlockTags.STONE_ORE_REPLACEABLES, BlockTags.DEEPSLATE_ORE_REPLACEABLES),
                List.of(Blocks.SAND),
                this
        );

        this.setInvisible(this.getInvisibleData());
        super.customServerAiStep();
    }

    @Override
    protected @NotNull AABB makeBoundingBox() {
        if (getInGround()) {
            return groundBox(this);
        }
        else{
            // Normal hit-box otherwise
            return super.makeBoundingBox();
        }
    }

    @Override
    public boolean isInvulnerableTo(@NotNull final DamageSource damageSource) {
        return this.getIsEmerging() || this.getInGround() || super.isInvulnerableTo(damageSource) || this.isFalling();
    }

    @Override
    public boolean isPushable() {
        return super.isPushable() && !this.getIsEmerging() && !this.getInGround();
    }

    @Override
    public boolean fireImmune() {
        return super.fireImmune() || this.getInGround();
    }

    int AnimationParticlesTicks=36;

    @Override
    public int getAnimationParticlesTicks() {
        return AnimationParticlesTicks;
    }

    @Override
    public void setAnimationParticlesTicks(final int animationParticlesTicks) {
        AnimationParticlesTicks=animationParticlesTicks;
    }

    @Override
    public boolean getInGround() {
        return this.entityData.get(InGROUND);
    }

    @Override
    public void setInGround(final boolean wasInGround) {
        this.entityData.set(InGROUND, wasInGround);
    }

    @Override
    public boolean getIsEmerging() {
        return this.entityData.get(EMERGING);
    }

    @Override
    public void setIsEmerging(final boolean wasEmerging) {
        this.entityData.set(EMERGING, wasEmerging);
    }
    public int ReturnToGround_Ticks=20;
    @Override
    public int getReturnToGround_Ticks() {
        return ReturnToGround_Ticks;
    }

    @Override
    public void setReturnToGround_Ticks(final int returnToGround_Ticks) {
        ReturnToGround_Ticks=returnToGround_Ticks;
    }

    @Override
    public boolean getInvisibleData() {
        return this.entityData.get(INVISIBLE);
    }

    @Override
    public void setInvisibleData(final boolean isInvisible) {
        this.entityData.set(INVISIBLE, isInvisible);
    }

    @Override
    protected @NotNull EntityDimensions getDefaultDimensions(@NotNull final Pose pose) {
        return this.getInGround()? this.getType().getDimensions().withEyeHeight(0.1f): super.getDefaultDimensions(pose);
    }

    @Override
    public void onSyncedDataUpdated(@NotNull final EntityDataAccessor<?> data) {
        super.onSyncedDataUpdated(data);
        if (!this.getInGround() || this.getInGround()) {
            this.setBoundingBox(this.makeBoundingBox());
            this.refreshDimensions();
        }
    }


    //Sounds
    @Override
    protected SoundEvent getAmbientSound() {
        if (!this.getInGround()) {
            return TCOTS_Sounds.getSoundEvent("devourer_idle");
        } else {
            return null;
        }
    }

    @Override
    protected @NotNull SoundEvent getHurtSound(@NotNull final DamageSource source) {
        return TCOTS_Sounds.getSoundEvent("devourer_hurt");
    }

    @Override
    protected @NotNull SoundEvent getDeathSound() {
        return TCOTS_Sounds.getSoundEvent("devourer_death");
    }

    @Override
    protected SoundEvent getAttackSound() {
        return TCOTS_Sounds.getSoundEvent("devourer_attack");
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
