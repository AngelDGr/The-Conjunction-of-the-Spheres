package TCOTS.entity.monsters.necrophages;

import TCOTS.registry.TCOTS_Sounds;
import TCOTS.entity.goals.*;
import TCOTS.entity.interfaces.ExcavatorMob;
import TCOTS.entity.misc.DrownerPuddleEntity;
import TCOTS.entity.misc.WaterHag_MudBallEntity;
import TCOTS.utils.GeoControllersUtil;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.RangedAttackGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;


public class WaterHagEntity extends NecrophageMonster implements GeoEntity, RangedAttackMob, ExcavatorMob {

    //xTODO: Add proper sounds
    //xTODO: Add mudball attack
    //xTODO: Add emerging and digging
    //xTODO: Add drops (Water Hag mutagen)
        //xTODO: Add Water Hag Decoction (Damage increased at full health)
        //TODO: Add Water Hag tooth, used in Enhanced Relict oil
        //xTODO: Add Ducal water (Water Essence), item for Northern Wind
    //xTODO: Add spawn

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public static final RawAnimation IDLE = RawAnimation.begin().thenLoop("idle");
    public static final RawAnimation WALKING = RawAnimation.begin().thenLoop("move.walking");
    public static final RawAnimation ATTACK_MUD = RawAnimation.begin().thenPlay("attack.mud_launch");

    protected static final EntityDataAccessor<Boolean> InGROUND = SynchedEntityData.defineId(WaterHagEntity.class, EntityDataSerializers.BOOLEAN);
    protected static final EntityDataAccessor<Boolean> EMERGING = SynchedEntityData.defineId(WaterHagEntity.class, EntityDataSerializers.BOOLEAN);
    protected static final EntityDataAccessor<Boolean> SPAWNED_PUDDLE = SynchedEntityData.defineId(WaterHagEntity.class, EntityDataSerializers.BOOLEAN);

    protected static final EntityDataAccessor<Boolean> INVISIBLE = SynchedEntityData.defineId(WaterHagEntity.class, EntityDataSerializers.BOOLEAN);

    public WaterHagEntity(final EntityType<? extends WaterHagEntity> entityType, final Level world) {
        super(entityType, world);
        this.xpReward = 10;
    }

    @Override
    public int getMaxHeadYRot() {
        return 70;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new WaterHag_Swim(this));

        //Emerge from ground
        this.goalSelector.addGoal(0, new EmergeFromGroundGoal_Excavator(this, 200, true));

        //Returns to ground
        this.goalSelector.addGoal(1, new ReturnToGroundGoal_Excavator(this, true));

        this.goalSelector.addGoal(2, new WaterHag_ProjectileAttackGoal(this,1.2D,10, 10.0f, 40f));
        this.goalSelector.addGoal(3, new MeleeAttackGoal_Excavator(this, 1.2D, false,3600, true));


        this.goalSelector.addGoal(4, new WanderAroundGoal_Excavator(this, 0.75, 20));

        this.goalSelector.addGoal(5, new LookAroundGoal_Excavator(this));

        //Objectives
        this.targetSelector.addGoal(0, new HurtByTargetGoal(this, WaterHagEntity.class));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, AbstractVillager.class, true));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
    }

    int ticksBetweenMudBalls;
    boolean mudballCooldown;

    //Projectile Attack
    @Override
    public void performRangedAttack(final LivingEntity target, final float pullProgress) {
        this.getNavigation().stop();
        final WaterHag_MudBallEntity mud_ballEntity = new WaterHag_MudBallEntity(this.level(), this, 6);
        final double d = target.getEyeY() - (double)1.1f;
        final double e = target.getX() - this.getX();
        final double f = d - mud_ballEntity.getY();
        final double g = target.getZ() - this.getZ();
        final double h = Math.sqrt(e * e + g * g) * (double)0.2f;
        mud_ballEntity.shoot(e, f + h, g, 1.6f, 12.0f);
        this.triggerAnim("MudController", "mud_attack");
        this.playSound(TCOTS_Sounds.getSoundEvent("water_hag_mud_ball_launch"), 1.0f, 0.4f / (this.getRandom().nextFloat() * 0.4f + 0.8f));
        this.level().addFreshEntity(mud_ballEntity);

        this.ticksBetweenMudBalls=100;
        this.mudballCooldown = true;
    }

    private static class WaterHag_ProjectileAttackGoal extends RangedAttackGoal{

        private final WaterHagEntity waterHag;
        private final float distanceForAttack;

        public WaterHag_ProjectileAttackGoal(final WaterHagEntity mob, final double mobSpeed, final int intervalTicks, final float maxShootRange, final float distanceForAttack) {
            super(mob, mobSpeed, intervalTicks, maxShootRange);
            this.waterHag= mob;
            this.distanceForAttack=distanceForAttack;
        }

        private boolean distanceCondition(){
            if(waterHag.getTarget() != null){
                final LivingEntity target = this.waterHag.getTarget();
                final double d = this.waterHag.distanceToSqr(target);

                return !(d < distanceForAttack);
            }
            else {
                return true;
            }
        }

        @Override
        public boolean canUse() {

            return super.canUse() && !waterHag.mudballCooldown && distanceCondition()
                    && !this.waterHag.getIsEmerging()
                    && !this.waterHag.getInGround();
        }

        @Override
        public boolean canContinueToUse() {
            return super.canContinueToUse() && !waterHag.mudballCooldown && distanceCondition();
        }
    }

    private class WaterHag_Swim extends FloatGoal{

        public WaterHag_Swim(final Mob mob) {
            super(mob);
        }

        @Override
        public boolean canUse() {
            return super.canUse()
                    && !WaterHagEntity.this.getInGround()
                    && !WaterHagEntity.this.getIsEmerging();
        }

        @Override
        public boolean canContinueToUse() {
            return super.canContinueToUse()
                    && !WaterHagEntity.this.getInGround()
                    && !WaterHagEntity.this.getIsEmerging();
        }
    }

    private DrownerPuddleEntity puddle;

    public DrownerPuddleEntity getPuddle() {
        return puddle;
    }

    public void setPuddle(final DrownerPuddleEntity puddle) {
        this.puddle = puddle;
    }

    public int ReturnToGround_Ticks=20;

    public int getReturnToGround_Ticks() {
        return ReturnToGround_Ticks;
    }

    public void setReturnToGround_Ticks(final int returnToGround_Ticks) {
        ReturnToGround_Ticks = returnToGround_Ticks;
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

    public static AttributeSupplier.Builder setAttributes() {
        return Animal.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.ATTACK_DAMAGE, 4.0f) //Amount of health that hurts you
                .add(Attributes.MOVEMENT_SPEED, 0.20f)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.5)
                .add(Attributes.ARMOR,2f);
    }

    @Override
    protected void defineSynchedData(final SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(InGROUND, Boolean.FALSE);
        builder.define(EMERGING, Boolean.FALSE);
        builder.define(SPAWNED_PUDDLE, Boolean.FALSE);
        builder.define(INVISIBLE, Boolean.FALSE);
    }

    @Override
    protected @NotNull AABB makeBoundingBox() {
        if (entityData.get(InGROUND)) {
            return groundBox(this);
        }
        else{
            // Normal hit-box otherwise
            return super.makeBoundingBox();
        }
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

    @Override
    public void registerControllers(final AnimatableManager.ControllerRegistrar controllers) {
        //Walk/Idle Controller
        controllers.add(new AnimationController<>(this, "Idle/Walk/Run", 5, state -> {
            //If it's attacking and moving
            if(this.isAggressive() && state.isMoving() ){
                state.setControllerSpeed(0.8f);
                return state.setAndContinue(WALKING);
            }else
                if(state.isMoving()){
                    state.setControllerSpeed(0.5f);
                    return state.setAndContinue(WALKING);
                }
                else{
                    state.setControllerSpeed(0.8f);
                    return state.setAndContinue(IDLE);
                }

        }));

        //Attack Controller
        controllers.add(
                new AnimationController<>(this, "AttackController", 1, state -> PlayState.STOP)
                        .triggerableAnim("attack1", GeoControllersUtil.ATTACK1)
                        .triggerableAnim("attack2", GeoControllersUtil.ATTACK2)
        );

        //Mud ball Controller
        controllers.add(
                new AnimationController<>(this, "MudController", 1, state -> PlayState.STOP)
                        .triggerableAnim("mud_attack", ATTACK_MUD)
        );

        //DiggingIn Controller
        controllers.add(
                new AnimationController<>(this,"DiggingController",1, this::animationDiggingPredicate)
        );

        //DiggingOut Controller
        controllers.add(
                new AnimationController<>(this, "EmergingController", 1, this::animationEmergingPredicate)
        );
    }

    @Override
    public void spawnGroundParticles(@NotNull final LivingEntity entity) {
        final RandomSource random = entity.getRandom();
        final BlockState blockState = entity.getBlockStateOn();
        if (blockState.getRenderShape() != RenderShape.INVISIBLE) {
            for(int i = 0; i < 11; ++i) {
                final double d = entity.getX() + (double) Mth.randomBetween(random, -0.7F, 0.7F);
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

    int AnimationParticlesTicks=36;

    public int getAnimationParticlesTicks() {
        return AnimationParticlesTicks;
    }

    public void setAnimationParticlesTicks(final int animationParticlesTicks) {
        AnimationParticlesTicks = animationParticlesTicks;
    }
    @Override
    public void tick() {
        //Detect own puddle
        tickPuddle(this);

        if(ticksBetweenMudBalls>0){
            --ticksBetweenMudBalls;
        }
        else {
            mudballCooldown =false;
        }

        //Counter for particles
        this.tickExcavator(this);

        super.tick();
    }

    @Override
    protected void customServerAiStep() {
        if(!mudballCooldown){
            this.mobTickExcavator(
                    List.of(BlockTags.DIRT),
                    List.of(Blocks.SAND),
                    this
            );
        }

        this.setInvisible(this.getInvisibleData());
        super.customServerAiStep();
    }



    @Override
    public void addAdditionalSaveData(@NotNull final CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putInt("MudAttackTicks", ticksBetweenMudBalls);
        nbt.putBoolean("InGround", this.entityData.get(InGROUND));
        nbt.putInt("ReturnToGroundTicks", this.ReturnToGround_Ticks);
        nbt.putBoolean("PuddleSpawned",this.entityData.get(SPAWNED_PUDDLE));
        nbt.putBoolean("Invisible",this.entityData.get(INVISIBLE));
    }
    @Override
    public void readAdditionalSaveData(final CompoundTag nbt) {
        this.ticksBetweenMudBalls = nbt.getInt("MudAttackTicks");
        this.setInGround(nbt.getBoolean("InGround"));
        this.ReturnToGround_Ticks = nbt.getInt("ReturnToGroundTicks");
        this.setSpawnedPuddleDataTracker(nbt.getBoolean("PuddleSpawned"));
        this.setInvisibleData(nbt.getBoolean("Invisible"));
    }

    //Water creature things
    @Override
    public boolean isPushedByFluid() {
        return false;
    }

    //Speed on water
    @Override
    protected float getWaterSlowDown() {
        return 0.91F;
    }

    //Sounds
    @Override
    protected SoundEvent getAmbientSound() {
        if(!this.getInGround()){
            return TCOTS_Sounds.getSoundEvent("water_hag_idle");
        }
        else{
            return null;
        }
    }

    @Override
    protected @NotNull SoundEvent getHurtSound(@NotNull final DamageSource source) {
        return TCOTS_Sounds.getSoundEvent("water_hag_hurt");
    }

    @Override
    protected @NotNull SoundEvent getDeathSound() {
        return TCOTS_Sounds.getSoundEvent("water_hag_death");
    }

    @Override
    public SoundEvent getEmergingSound() {
        return TCOTS_Sounds.getSoundEvent("water_hag_emerging");
    }

    @Override
    public SoundEvent getDiggingSound() {
        return TCOTS_Sounds.getSoundEvent("water_hag_digging");
    }

    //Footsteps sounds
    protected SoundEvent getStepSound() {
        return TCOTS_Sounds.getSoundEvent("drowner_footstep");
    }

    //Attack Sound
    @Override
    protected SoundEvent getAttackSound() {
        return TCOTS_Sounds.getSoundEvent("water_hag_attack");
    }

    @Override
    public void checkDespawn() {
        if (this.level().getDifficulty() == Difficulty.PEACEFUL && this.shouldDespawnInPeaceful()) {
            if(this.puddle!=null){
               this.puddle.discard();
            }
            this.discard();
        } else if (!this.isPersistenceRequired() && !this.requiresCustomPersistence()) {
            final Entity entity = this.level().getNearestPlayer(this, -1.0);
            if (entity != null) {
                final double d = entity.distanceToSqr(this);
                final int i = this.getType().getCategory().getDespawnDistance();
                final int j = i * i;
                if (d > (double)j && this.removeWhenFarAway(d)) {
                    if(puddle!=null){
                        puddle.discard();
                    }
                    this.discard();
                }

                final int k = this.getType().getCategory().getNoDespawnDistance();
                final int l = k * k;
                if (this.noActionTime > 600 && this.random.nextInt(800) == 0 && d > (double)l && this.removeWhenFarAway(d)) {
                    if(puddle!=null){
                        puddle.discard();
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
