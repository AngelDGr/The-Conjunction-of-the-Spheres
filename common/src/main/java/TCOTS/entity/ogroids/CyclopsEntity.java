package TCOTS.entity.ogroids;

import TCOTS.registry.TCOTS_Sounds;
import TCOTS.TCOTS_Tags;
import TCOTS.entity.goals.MeleeAttackGoal_Animated;
import TCOTS.utils.EntitiesUtil;
import TCOTS.utils.GeoControllersUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;
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
    public CyclopsEntity(EntityType<? extends CyclopsEntity> entityType, Level world) {
        super(entityType, world);
        this.xpReward=10;
    }

    public static final RawAnimation JUMP = RawAnimation.begin().thenPlayAndHold("special.jumping");
    public static final RawAnimation LANDING = RawAnimation.begin().thenPlay("special.landing");

    protected static final EntityDataAccessor<Boolean> FALLING = SynchedEntityData.defineId(CyclopsEntity.class, EntityDataSerializers.BOOLEAN);
    protected static final EntityDataAccessor<Float> FALLING_DISTANCE = SynchedEntityData.defineId(CyclopsEntity.class, EntityDataSerializers.FLOAT);


    @Override
    protected void defineSynchedData(SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(FALLING, Boolean.FALSE);
        builder.define(FALLING_DISTANCE, fallDistance);
    }

    public static AttributeSupplier.Builder setAttributes() {
        return Animal.createMobAttributes()
                .add(Attributes.STEP_HEIGHT, 1.5f)

                .add(Attributes.MAX_HEALTH, 45.0D)
                .add(Attributes.ATTACK_DAMAGE, 8.0f) //Amount of health that hurts you
                .add(Attributes.MOVEMENT_SPEED, 0.22f)

                .add(Attributes.ATTACK_KNOCKBACK, 2.0f)
                .add(Attributes.ARMOR, 4f)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.8f)
                .add(Attributes.ARMOR_TOUGHNESS, 2f)
                .add(Attributes.FOLLOW_RANGE, 35.0);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));

        this.goalSelector.addGoal(1, new CyclopsJumpAttack(this,200, 100));

        this.goalSelector.addGoal(2, new CyclopsMeleeAttackGoal(this, 1.2D, false));

        this.goalSelector.addGoal(3, new RandomStrollGoal(this, 0.75f, 20));

        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(0, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, AbstractVillager.class, true));
        this.targetSelector.addGoal(2, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
    }

    private static class CyclopsMeleeAttackGoal extends MeleeAttackGoal_Animated {

        private final CyclopsEntity cyclops;

        public CyclopsMeleeAttackGoal(PathfinderMob mob, double speed, boolean pauseWhenMobIdle) {
            super(mob, speed, pauseWhenMobIdle, 2);
            this.cyclops =(CyclopsEntity) mob;
        }

        @Override
        public boolean canUse() {
            return super.canUse()
                    && !cyclops.isFalling();
        }

        @Override
        public boolean canContinueToUse() {
            return super.canContinueToUse()
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
        public boolean canUse() {
            LivingEntity target = this.cyclops.getTarget();

            if (target != null) {
                //5 square distance like 1.5 blocks approx
                //I want 7.5 blocks approx
                //So 7.5/1.5=5
                return !this.cyclops.cooldownBetweenJumps
                        && this.cyclops.isAggressive()
                        && !this.cyclops.isInLiquid()
                        && !this.cyclops.isPassenger()
                        && !this.cyclops.level().getBlockState(this.cyclops.blockPosition()).is(Blocks.HONEY_BLOCK)
                        && !this.cyclops.level().getBlockState(this.cyclops.blockPosition()).is(Blocks.COBWEB)
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
            cyclops.jumpFromGround();
            cyclops.setIsFalling(true);
            cyclops.cooldownBetweenJumps=true;
            cyclops.jumpTicks= minJumpCooldown +cyclops.random.nextIntBetweenInclusive(0,maxJumpCooldown);
        }
    }

    @Override
    protected float getJumpPower() {
        return 0.7f * this.getBlockJumpFactor() + this.getJumpBoostPower();
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
    public void addAdditionalSaveData(@NotNull CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);

        nbt.putInt("JumpCooldown", this.jumpTicks);
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag nbt) {
        super.readAdditionalSaveData(nbt);

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
    protected int calculateFallDamage(float fallDistance, float damageMultiplier) {
        return super.calculateFallDamage(fallDistance, damageMultiplier) - 4;
    }

    @Override
    public void jumpFromGround() {
        this.playSound(TCOTS_Sounds.getSoundEvent("cyclops_attack"), 1.0f, 1.0f);
        super.jumpFromGround();
    }
    private static final byte FALLING_PARTICLES = 42;
    @Override
    public void resetFallDistance() {
        if(isFalling()){
            setIsFalling(false);
            if(this.level().getBlockState(this.blockPosition().below()).getFluidState().is(Fluids.EMPTY)
                    && !this.level().getBlockState(this.blockPosition()).is(TCOTS_Tags.NEGATES_DEVOURER_JUMP)
                    && !this.level().getBlockState(this.blockPosition().below()).is(TCOTS_Tags.NEGATES_DEVOURER_JUMP))
            {
                this.triggerAnim("LandingController","landing");
                EntitiesUtil.pushAndDamageEntities(this, 6 + (fallDistance*2f), 3.0f + (fallDistance * 0.5f), 3, 3.0);
                this.playSound(TCOTS_Sounds.getSoundEvent("medium_impact"), 1.0f, 1.0f);
                this.level().broadcastEntityEvent(this, FALLING_PARTICLES);
            }
        }

        super.resetFallDistance();
    }
    @Override
    public void handleEntityEvent(byte status) {
        if(status==FALLING_PARTICLES){
            EntitiesUtil.spawnImpactParticles(this, 1.9975f + (2.8f + (getFallingDistance() * 0.5f)), this.getFallingDistance());
        } else{
            super.handleEntityEvent(status);
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
        this.entityData.set(FALLING, isFalling);
    }

    public boolean isFalling() {
        return this.entityData.get(FALLING);
    }

    public void setFallingDistance(float fallingDistance) {
        this.entityData.set(FALLING_DISTANCE, fallingDistance);
    }

    public double getFallingDistance() {
        return this.entityData.get(FALLING_DISTANCE);
    }

    @Override
    public boolean isInvulnerableTo(@NotNull DamageSource damageSource) {
        return super.isInvulnerableTo(damageSource) || this.isFalling();
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return TCOTS_Sounds.getSoundEvent("cyclops_idle");
    }

    @Override
    protected SoundEvent getAttackSound() {
        return TCOTS_Sounds.getSoundEvent("cyclops_attack");
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource source) {
        return TCOTS_Sounds.getSoundEvent("cyclops_hurt");
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return TCOTS_Sounds.getSoundEvent("cyclops_death");
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @SuppressWarnings("deprecation, unused")
    public static boolean canCyclopsSpawn(EntityType<? extends Mob> type, LevelAccessor world, MobSpawnType spawnReason, BlockPos pos, RandomSource random) {
        BlockPos blockPos = pos.below();
        int radiusToSearchCyclops=80;

        return spawnReason == MobSpawnType.SPAWNER
                || (world.getBlockState(blockPos).isValidSpawn(world, blockPos, type)
                && pos.getY() < (world.getSeaLevel() - 20)
                //To no spawn two Cyclops close
                && world.getEntitiesOfClass(CyclopsEntity.class,
                new AABB(pos.getX()-radiusToSearchCyclops, pos.getY()-radiusToSearchCyclops, pos.getZ()-radiusToSearchCyclops,
                        pos.getX()+radiusToSearchCyclops, pos.getY()+radiusToSearchCyclops, pos.getZ()+radiusToSearchCyclops),
                cyclopsEntity -> true).isEmpty());
    }
}
