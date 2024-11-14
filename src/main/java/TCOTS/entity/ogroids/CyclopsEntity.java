package TCOTS.entity.ogroids;

import TCOTS.blocks.TCOTS_Blocks;
import TCOTS.entity.goals.MeleeAttackGoal_Animated;
import TCOTS.sounds.TCOTS_Sounds;
import TCOTS.utils.EntitiesUtil;
import TCOTS.utils.GeoControllersUtil;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public class CyclopsEntity extends OgroidMonster implements GeoEntity {
    //xTODO: Add sounds
    //xTODO: Add new attacks
    ///      Hyper jump   -> Ultra knockback and a lot of damage
    ///         Fix particles y position
    ///         Spawn ground particles in the blocks inside the radius
    ///         Apply these changes to the Devourer Jump
    ///     Ground punch -> Medium knockback and medium damage???
    //xTODO: Add bestiary description
    //TODO: Add drops
    //  Add Cyclops Eye and add a use for it??
    //  Add some kind of special leather?
    //xTODO: Add spawn

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    public CyclopsEntity(EntityType<? extends CyclopsEntity> entityType, World world) {
        super(entityType, world);
        this.experiencePoints=10;
    }

    public static final RawAnimation JUMP = RawAnimation.begin().thenPlayAndHold("special.jumping");
    public static final RawAnimation LANDING = RawAnimation.begin().thenPlay("special.landing");

    protected static final TrackedData<Boolean> FALLING = DataTracker.registerData(CyclopsEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    protected static final TrackedData<Float> FALLING_DISTANCE = DataTracker.registerData(CyclopsEntity.class, TrackedDataHandlerRegistry.FLOAT);


    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(FALLING, Boolean.FALSE);
        builder.add(FALLING_DISTANCE, fallDistance);
    }

    public static DefaultAttributeContainer.Builder setAttributes() {
        return AnimalEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_STEP_HEIGHT, 1.5f)

                .add(EntityAttributes.GENERIC_MAX_HEALTH, 45.0D)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 8.0f) //Amount of health that hurts you
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.22f)

                .add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK, 2.0f)
                .add(EntityAttributes.GENERIC_ARMOR, 4f)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 0.8f)
                .add(EntityAttributes.GENERIC_ARMOR_TOUGHNESS, 2f)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 35.0);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));

        this.goalSelector.add(1, new CyclopsJumpAttack(this,200, 100));

        this.goalSelector.add(2, new CyclopsMeleeAttackGoal(this, 1.2D, false));

        this.goalSelector.add(3, new WanderAroundGoal(this, 0.75f, 20));

        this.goalSelector.add(4, new LookAroundGoal(this));

        this.targetSelector.add(0, new ActiveTargetGoal<>(this, PlayerEntity.class, true));
        this.targetSelector.add(1, new ActiveTargetGoal<>(this, MerchantEntity.class, true));
        this.targetSelector.add(2, new RevengeGoal(this));
        this.targetSelector.add(3, new ActiveTargetGoal<>(this, IronGolemEntity.class, true));
    }

    private static class CyclopsMeleeAttackGoal extends MeleeAttackGoal_Animated {

        private final CyclopsEntity cyclops;

        public CyclopsMeleeAttackGoal(PathAwareEntity mob, double speed, boolean pauseWhenMobIdle) {
            super(mob, speed, pauseWhenMobIdle, 2);
            this.cyclops =(CyclopsEntity) mob;
        }

        @Override
        public boolean canStart() {
            return super.canStart()
                    && !cyclops.isFalling();
        }

        @Override
        public boolean shouldContinue() {
            return super.shouldContinue()
                    && !cyclops.isFalling();
        }
    }

    private static class CyclopsJumpAttack extends Goal {

        private final CyclopsEntity cyclops;
        private final int minJumpCooldown;
        private final int maxJumpCooldown;

        public CyclopsJumpAttack(CyclopsEntity cyclops, int minJumpCooldown, int maxExtraRandomCooldown) {
            this.cyclops = cyclops;
            this.minJumpCooldown = minJumpCooldown;
            this.maxJumpCooldown = maxExtraRandomCooldown;
        }

        @Override
        public boolean canStart() {
            LivingEntity target = this.cyclops.getTarget();

            if (target != null) {
                //5 square distance like 1.5 blocks approx
                //I want 7.5 blocks approx
                //So 7.5/1.5=5
                return !this.cyclops.cooldownBetweenJumps
                        && this.cyclops.isAttacking()
                        && !this.cyclops.isInFluid()
                        && !this.cyclops.hasVehicle()
                        && !this.cyclops.getWorld().getBlockState(this.cyclops.getBlockPos()).isOf(Blocks.HONEY_BLOCK)
                        && !this.cyclops.getWorld().getBlockState(this.cyclops.getBlockPos()).isOf(Blocks.COBWEB)
                        //Max
                        && this.cyclops.distanceTo(target) < 6
                        //Min
                        && this.cyclops.distanceTo(target) > 2;

            } else {
                return false;
            }
        }

        @Override
        public void tick() {
            LivingEntity target = this.cyclops.getTarget();

            if(target!=null){
                jumpAttack();
            }
        }

        private void jumpAttack(){
            cyclops.jump();
            cyclops.setIsFalling(true);
            cyclops.cooldownBetweenJumps=true;
            cyclops.jumpTicks= minJumpCooldown +cyclops.random.nextBetween(0,maxJumpCooldown);
        }
    }

    @Override
    protected float getJumpVelocity() {
        return 0.7f * this.getJumpVelocityMultiplier() + this.getJumpBoostVelocityModifier();
    }


    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        //Walk/Idle Controller
        controllerRegistrar.add(new AnimationController<>(this, "Idle/Walk", 5, GeoControllersUtil::idleWalkRunController)
        );

        //Attack Controller
        controllerRegistrar.add(
                new AnimationController<>(this, "AttackController", 1, state -> PlayState.STOP)
                        .triggerableAnim("attack1", GeoControllersUtil.ATTACK1)
                        .triggerableAnim("attack2", GeoControllersUtil.ATTACK2)
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

        //Landing
        controllerRegistrar.add(
                new AnimationController<>(this, "LandingController", 1, state -> PlayState.STOP)
                        .triggerableAnim("landing", LANDING)
        );
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);

        nbt.putInt("JumpCooldown", this.jumpTicks);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);

        this.jumpTicks=nbt.getInt("JumpCooldown");
    }

    @Override
    public void tick() {
        super.tick();

        //To sync fallDistance with the client
        setFallingDistance(fallDistance);

        //Tick for jumps
        tickJump();
    }

    @Override
    protected int computeFallDamage(float fallDistance, float damageMultiplier) {
        return super.computeFallDamage(fallDistance, damageMultiplier) - 4;
    }

    @Override
    public void jump() {
        this.playSound(TCOTS_Sounds.CYCLOPS_ATTACK, 1.0f, 1.0f);
        super.jump();
    }
    private static final byte FALLING_PARTICLES = 42;
    @Override
    public void onLanding() {
        if(isFalling()){
            setIsFalling(false);
            if(this.getWorld().getBlockState(this.getBlockPos().down()).getFluidState().isOf(Fluids.EMPTY)
                    && !this.getWorld().getBlockState(this.getBlockPos()).isIn(TCOTS_Blocks.NEGATES_DEVOURER_JUMP)
                    && !this.getWorld().getBlockState(this.getBlockPos().down()).isIn(TCOTS_Blocks.NEGATES_DEVOURER_JUMP))
            {
                this.triggerAnim("LandingController","landing");
                EntitiesUtil.pushAndDamageEntities(this, 6 + (fallDistance*2f), 3.0f + (fallDistance * 0.5f), 3, 3.0);
                this.playSound(TCOTS_Sounds.MEDIUM_IMPACT, 1.0f, 1.0f);
                this.getWorld().sendEntityStatus(this, FALLING_PARTICLES);
            }
        }

        super.onLanding();
    }
    @Override
    public void handleStatus(byte status) {
        if(status==FALLING_PARTICLES){
            EntitiesUtil.spawnImpactParticles(this, 1.9975f + (2.8f + (getFallingDistance() * 0.5f)), this.getFallingDistance());
        } else{
            super.handleStatus(status);
        }
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


    public void setIsFalling(boolean isFalling) {
        this.dataTracker.set(FALLING, isFalling);
    }

    public boolean isFalling() {
        return this.dataTracker.get(FALLING);
    }

    public void setFallingDistance(float fallingDistance) {
        this.dataTracker.set(FALLING_DISTANCE, fallingDistance);
    }

    public double getFallingDistance() {
        return this.dataTracker.get(FALLING_DISTANCE);
    }

    @Override
    public boolean isInvulnerableTo(DamageSource damageSource) {
        return super.isInvulnerableTo(damageSource) || this.isFalling();
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return TCOTS_Sounds.CYCLOPS_IDLE;
    }

    @Override
    protected SoundEvent getAttackSound() {
        return TCOTS_Sounds.CYCLOPS_ATTACK;
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return TCOTS_Sounds.CYCLOPS_HURT;
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return TCOTS_Sounds.CYCLOPS_DEATH;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @SuppressWarnings("deprecation, unused")
    public static boolean canCyclopsSpawn(EntityType<? extends MobEntity> type, WorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
        BlockPos blockPos = pos.down();
        int radiusToSearchCyclops=80;

        return spawnReason == SpawnReason.SPAWNER
                || (world.getBlockState(blockPos).allowsSpawning(world, blockPos, type)
                && pos.getY() < (world.getSeaLevel() - 20)
                //To no spawn two Cyclops close
                && world.getEntitiesByClass(CyclopsEntity.class,
                new Box(pos.getX()-radiusToSearchCyclops, pos.getY()-radiusToSearchCyclops, pos.getZ()-radiusToSearchCyclops,
                        pos.getX()+radiusToSearchCyclops, pos.getY()+radiusToSearchCyclops, pos.getZ()+radiusToSearchCyclops),
                cyclopsEntity -> true).isEmpty());
    }
}
