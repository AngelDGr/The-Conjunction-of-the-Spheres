package TCOTS.entity.necrophages;

import TCOTS.blocks.TCOTS_Blocks;
import TCOTS.entity.goals.*;
import TCOTS.entity.interfaces.ExcavatorMob;
import TCOTS.items.concoctions.TCOTS_Effects;
import TCOTS.sounds.TCOTS_Sounds;
import TCOTS.utils.EntitiesUtil;
import TCOTS.utils.GeoControllersUtil;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.RevengeGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

import java.util.List;

public class DevourerEntity extends NecrophageMonster implements GeoEntity, ExcavatorMob {

    //xTODO: Finish bestiary
    //xTODO: Make the water cancel the jump
    //xTODO: Fix the jump particles
    //xTODO: Add jumping animation
    //xTODO: Add natural spawn
    //xTODO: Add drop

    private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    private static final byte FALLING_PARTICLES = 42;
    public static final RawAnimation JUMP = RawAnimation.begin().thenPlay("special.jumping");

    protected static final TrackedData<Boolean> InGROUND = DataTracker.registerData(DevourerEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    protected static final TrackedData<Boolean> EMERGING = DataTracker.registerData(DevourerEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    protected static final TrackedData<Boolean> INVISIBLE = DataTracker.registerData(DevourerEntity.class, TrackedDataHandlerRegistry.BOOLEAN);


    protected static final TrackedData<Boolean> FALLING = DataTracker.registerData(DevourerEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    public DevourerEntity(EntityType<? extends PathAwareEntity> entityType, World world) {
        super(entityType, world);
        experiencePoints=10;
    }

    @Override
    public int getMaxHeadRotation() {
        return 43;
    }

    public static DefaultAttributeContainer.Builder setAttributes() {
        return AnimalEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 30.0D)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 5.0f) //Amount of health that hurts you
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.22f)

                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 0.5);
    }

    @Override
    protected void initGoals() {
        //Emerge from ground
        this.goalSelector.add(0, new EmergeFromGroundGoal_Excavator(this, 500));
        this.goalSelector.add(1, new SwimGoal(this));

        //Returns to ground
        this.goalSelector.add(2, new ReturnToGroundGoal_Excavator(this));

        this.goalSelector.add(3, new DevourerJumpAttack(this,160));

        this.goalSelector.add(4, new Devourer_MeleeAttackGoal(this, 1.2D, false, 3600));

        this.goalSelector.add(5, new WanderAroundGoal_Excavator(this, 0.75f, 20));

        this.goalSelector.add(6, new LookAroundGoal_Excavator(this));

        //Objectives
        this.targetSelector.add(1, new RevengeGoal(this, DevourerEntity.class));
        this.targetSelector.add(1, new ActiveTargetGoal<>(this, PlayerEntity.class, true));
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, MerchantEntity.class, true));
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, IronGolemEntity.class, true));
    }

    private static class DevourerJumpAttack extends Goal {

        private final DevourerEntity devourer;
        private final int jumpCooldown;

        public DevourerJumpAttack(DevourerEntity devourer, int jumpCooldown) {
            this.devourer=devourer;
            this.jumpCooldown=jumpCooldown;
        }

        @Override
        public boolean canStart() {
            LivingEntity target = this.devourer.getTarget();

            if (target != null) {
                //5 square distance like 1.5 blocks approx
                //I want 7.5 blocks approx
                //So 7.5/1.5=5
                return !this.devourer.cooldownBetweenJumps
                        && this.devourer.isAttacking()
                        && !this.devourer.isInFluid()
                        && !this.devourer.hasVehicle()
                        && !this.devourer.getWorld().getBlockState(this.devourer.getBlockPos()).isOf(Blocks.HONEY_BLOCK)
                        && !this.devourer.getWorld().getBlockState(this.devourer.getBlockPos()).isOf(Blocks.COBWEB)
                        && this.devourer.squaredDistanceTo(target) < 5;
            } else {
                return false;
            }
        }

        @Override
        public void tick() {
            LivingEntity target = this.devourer.getTarget();

            if(target!=null){
                jumpAttack();
            }
        }

        private void jumpAttack(){
            devourer.jump();
            devourer.setIsFalling(true);
            devourer.cooldownBetweenJumps=true;
            devourer.jumpTicks=jumpCooldown;
        }
    }

    @Override
    protected int computeFallDamage(float fallDistance, float damageMultiplier) {
        return super.computeFallDamage(fallDistance, damageMultiplier) - 8;
    }

    @Override
    protected void jump() {
        this.playSound(TCOTS_Sounds.DEVOURER_JUMP, 1.0f, 1.0f);
        this.triggerAnim("JumpController","jump");
        super.jump();
    }

    private static class Devourer_MeleeAttackGoal extends MeleeAttackGoal_Excavator{
        private final DevourerEntity devourer;

        public Devourer_MeleeAttackGoal(DevourerEntity mob, double speed, boolean pauseWhenMobIdle, int ticksBeforeToGround) {
            super(mob, speed, pauseWhenMobIdle, ticksBeforeToGround);
            this.devourer=mob;
        }

        @Override
        public boolean canStart() {
            return super.canStart() && !devourer.isFalling();
        }

        @Override
        public boolean shouldContinue() {
            return super.shouldContinue() && !devourer.isFalling();
        }
    }

    @Override
    public void onLanding() {
        if(isFalling()){
            if(this.getWorld().getBlockState(this.getBlockPos().down()).getFluidState().isOf(Fluids.EMPTY)
                    && !this.getWorld().getBlockState(this.getBlockPos()).isIn(TCOTS_Blocks.NEGATES_DEVOURER_JUMP)
                    && !this.getWorld().getBlockState(this.getBlockPos().down()).isIn(TCOTS_Blocks.NEGATES_DEVOURER_JUMP))
            {
                EntitiesUtil.pushAndDamageEntities(this, 2 + (fallDistance), 1.5 + (fallDistance * 0.2f), 2, 1.2, DevourerEntity.class);
                this.playSound(SoundEvents.ENTITY_HOSTILE_BIG_FALL, 1.0f, 1.0f);
                this.getWorld().sendEntityStatus(this, FALLING_PARTICLES);
            }

            setIsFalling(false);
        }
        super.onLanding();
    }


    private int counter=0;

    @Override
    public void handleStatus(byte status) {
        if(status==FALLING_PARTICLES){
            spawnSmokeParticles(counter);
        } else{
            super.handleStatus(status);
        }
    }

    private void spawnSmokeParticles(int counter){
        while (counter < 40) {
            double d = this.random.nextGaussian() * 0.2;
            double e = this.random.nextGaussian() * 0.2;
            double f = this.random.nextGaussian() * 0.2;
            this.getWorld().addParticle(ParticleTypes.WHITE_SMOKE, this.getX(), this.getY()-0.5f, this.getZ(), d, e, f);
            ++counter;
        }
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(InGROUND, Boolean.FALSE);
        this.dataTracker.startTracking(EMERGING, Boolean.FALSE);
        this.dataTracker.startTracking(INVISIBLE, Boolean.FALSE);
        this.dataTracker.startTracking(FALLING, Boolean.FALSE);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        //Walk/Idle Controller
        controllerRegistrar.add(new AnimationController<>(this, "Idle/Walk", 5, GeoControllersUtil::idleWalkRunController)
        );

        //Attack Controller
        controllerRegistrar.add(
                new AnimationController<>(this, "AttackController", 2, state -> PlayState.STOP)
                        .triggerableAnim("attack1", GeoControllersUtil.ATTACK1)
                        .triggerableAnim("attack2", GeoControllersUtil.ATTACK2)
                        .triggerableAnim("attack3", GeoControllersUtil.ATTACK3)
        );

        //DiggingIn Controller
        controllerRegistrar.add(
                new AnimationController<>(this,"DiggingController",1, this::animationDiggingPredicate)
        );

        //DiggingOut Controller
        controllerRegistrar.add(
                new AnimationController<>(this, "EmergingController", 1, this::animationEmergingPredicate)
        );

        controllerRegistrar.add(
                new AnimationController<>(this, "JumpController", 1, state -> PlayState.STOP)
                        .triggerableAnim("jump", JUMP)
        );
    }


    @Override
    public boolean canHaveStatusEffect(StatusEffectInstance effect) {
        return effect.getEffectType() != TCOTS_Effects.SAMUM_EFFECT && super.canHaveStatusEffect(effect);
    }

    public boolean cooldownBetweenJumps=false;
    private int jumpTicks;

    private void tickJump(){
        if(counter>0){
            counter=0;
        }

        if (jumpTicks > 0) {
            --jumpTicks;
        } else {
            cooldownBetweenJumps=false;
        }
    }


    public void setIsFalling(boolean isFalling) {
        this.dataTracker.set(FALLING, isFalling);
    }

    public boolean isFalling() {
        return this.dataTracker.get(FALLING);
    }

    //Excavator Common
    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putBoolean("InGround", this.dataTracker.get(InGROUND));
        nbt.putInt("ReturnToGroundTicks", this.ReturnToGround_Ticks);
        nbt.putBoolean("Invisible", this.dataTracker.get(INVISIBLE));
        nbt.putInt("JumpCooldown", this.jumpTicks);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        this.setInGroundDataTracker(nbt.getBoolean("InGround"));
        this.ReturnToGround_Ticks = nbt.getInt("ReturnToGroundTicks");
        this.setInvisibleData(nbt.getBoolean("Invisible"));
        this.jumpTicks=nbt.getInt("JumpCooldown");
        super.readCustomDataFromNbt(nbt);
    }

    @Override
    public void tick() {
        //Counter for particles
        this.tickExcavator(this);
        //Tick for jumps
        this.tickJump();

        super.tick();
    }

    @Override
    protected void mobTick() {
        mobTickExcavator(
                List.of(BlockTags.DIRT, BlockTags.STONE_ORE_REPLACEABLES, BlockTags.DEEPSLATE_ORE_REPLACEABLES),
                List.of(Blocks.SAND),
                this
        );

        this.setInvisible(this.getInvisibleData());
        super.mobTick();
    }

    @Override
    protected Box calculateBoundingBox() {
        if (getInGroundDataTracker()) {
            return groundBox(this);
        }
        else{
            // Normal hit-box otherwise
            return super.calculateBoundingBox();
        }
    }

    @Override
    public boolean isPushable() {
        return !this.getIsEmerging() && !this.getInGroundDataTracker();
    }

    @Override
    public boolean isInvulnerableTo(DamageSource damageSource) {
        return this.getIsEmerging() || this.getInGroundDataTracker() || super.isInvulnerableTo(damageSource) || this.isFalling();
    }

    int AnimationParticlesTicks=36;

    @Override
    public int getAnimationParticlesTicks() {
        return AnimationParticlesTicks;
    }

    @Override
    public void setAnimationParticlesTicks(int animationParticlesTicks) {
        AnimationParticlesTicks=animationParticlesTicks;
    }

    @Override
    public boolean getInGroundDataTracker() {
        return this.dataTracker.get(InGROUND);
    }

    @Override
    public void setInGroundDataTracker(boolean wasInGround) {
        this.dataTracker.set(InGROUND, wasInGround);
    }

    @Override
    public boolean getIsEmerging() {
        return this.dataTracker.get(EMERGING);
    }

    @Override
    public void setIsEmerging(boolean wasEmerging) {
        this.dataTracker.set(EMERGING, wasEmerging);
    }
    public int ReturnToGround_Ticks=20;
    @Override
    public int getReturnToGround_Ticks() {
        return ReturnToGround_Ticks;
    }

    @Override
    public void setReturnToGround_Ticks(int returnToGround_Ticks) {
        ReturnToGround_Ticks=returnToGround_Ticks;
    }

    @Override
    public boolean getInvisibleData() {
        return this.dataTracker.get(INVISIBLE);
    }

    @Override
    public void setInvisibleData(boolean isInvisible) {
        this.dataTracker.set(INVISIBLE, isInvisible);
    }


    //Sounds
    @Override
    protected SoundEvent getAmbientSound() {
        if (!this.getInGroundDataTracker()) {
            return TCOTS_Sounds.DEVOURER_IDLE;
        } else {
            return null;
        }
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return TCOTS_Sounds.DEVOURER_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return TCOTS_Sounds.DEVOURER_DEATH;
    }

    @Override
    protected SoundEvent getAttackSound() {
        return TCOTS_Sounds.DEVOURER_ATTACK;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
