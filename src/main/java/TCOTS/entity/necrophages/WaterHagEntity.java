package TCOTS.entity.necrophages;

import TCOTS.entity.goals.*;
import TCOTS.entity.interfaces.ExcavatorMob;
import TCOTS.entity.misc.DrownerPuddleEntity;
import TCOTS.entity.misc.WaterHag_MudBallEntity;
import TCOTS.sounds.TCOTS_Sounds;
import TCOTS.utils.GeoControllersUtil;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.RangedAttackMob;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.ProjectileAttackGoal;
import net.minecraft.entity.ai.goal.RevengeGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
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

    protected static final TrackedData<Boolean> InGROUND = DataTracker.registerData(WaterHagEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    protected static final TrackedData<Boolean> EMERGING = DataTracker.registerData(WaterHagEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    protected static final TrackedData<Boolean> SPAWNED_PUDDLE = DataTracker.registerData(WaterHagEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    protected static final TrackedData<Boolean> INVISIBLE = DataTracker.registerData(WaterHagEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    public WaterHagEntity(EntityType<? extends WaterHagEntity> entityType, World world) {
        super(entityType, world);
        this.experiencePoints = 10;
    }

    @Override
    public int getMaxHeadRotation() {
        return 70;
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new WaterHag_Swim(this));

        //Emerge from ground
        this.goalSelector.add(0, new EmergeFromGroundGoal_Excavator(this, 200, true));

        //Returns to ground
        this.goalSelector.add(1, new ReturnToGroundGoal_Excavator(this, true));

        this.goalSelector.add(2, new WaterHag_ProjectileAttackGoal(this,1.2D,10, 10.0f, 40f));
        this.goalSelector.add(3, new MeleeAttackGoal_Excavator(this, 1.2D, false,3600, true));


        this.goalSelector.add(4, new WanderAroundGoal_Excavator(this, 0.75, 20));

        this.goalSelector.add(5, new LookAroundGoal_Excavator(this));

        //Objectives
        this.targetSelector.add(0, new RevengeGoal(this, WaterHagEntity.class));
        this.targetSelector.add(1, new ActiveTargetGoal<>(this, PlayerEntity.class, true));
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, MerchantEntity.class, true));
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, IronGolemEntity.class, true));
    }

    int ticksBetweenMudBalls;
    boolean mudballCooldown;

    //Projectile Attack
    @Override
    public void shootAt(LivingEntity target, float pullProgress) {
        this.getNavigation().stop();
        WaterHag_MudBallEntity mud_ballEntity = new WaterHag_MudBallEntity(this.getWorld(), this, 6);
        double d = target.getEyeY() - (double)1.1f;
        double e = target.getX() - this.getX();
        double f = d - mud_ballEntity.getY();
        double g = target.getZ() - this.getZ();
        double h = Math.sqrt(e * e + g * g) * (double)0.2f;
        mud_ballEntity.setVelocity(e, f + h, g, 1.6f, 12.0f);
        this.triggerAnim("MudController", "mud_attack");
        this.playSound(TCOTS_Sounds.WATER_HAG_MUD_BALL_LAUNCH, 1.0f, 0.4f / (this.getRandom().nextFloat() * 0.4f + 0.8f));
        this.getWorld().spawnEntity(mud_ballEntity);

        this.ticksBetweenMudBalls=100;
        this.mudballCooldown = true;
    }

    private static class WaterHag_ProjectileAttackGoal extends ProjectileAttackGoal{

        private final WaterHagEntity waterHag;
        private final float distanceForAttack;

        public WaterHag_ProjectileAttackGoal(WaterHagEntity mob, double mobSpeed, int intervalTicks, float maxShootRange, float distanceForAttack) {
            super(mob, mobSpeed, intervalTicks, maxShootRange);
            this.waterHag= mob;
            this.distanceForAttack=distanceForAttack;
        }

        private boolean distanceCondition(){
            if(waterHag.getTarget() != null){
                LivingEntity target = this.waterHag.getTarget();
                double d = this.waterHag.squaredDistanceTo(target);

                return !(d < distanceForAttack);
            }
            else {
                return true;
            }
        }

        @Override
        public boolean canStart() {

            return super.canStart() && !waterHag.mudballCooldown && distanceCondition()
                    && !this.waterHag.getIsEmerging()
                    && !this.waterHag.getInGround();
        }

        @Override
        public boolean shouldContinue() {
            return super.shouldContinue() && !waterHag.mudballCooldown && distanceCondition();
        }
    }

    private class WaterHag_Swim extends SwimGoal{

        public WaterHag_Swim(MobEntity mob) {
            super(mob);
        }

        @Override
        public boolean canStart() {
            return super.canStart()
                    && !WaterHagEntity.this.getInGround()
                    && !WaterHagEntity.this.getIsEmerging();
        }

        @Override
        public boolean shouldContinue() {
            return super.shouldContinue()
                    && !WaterHagEntity.this.getInGround()
                    && !WaterHagEntity.this.getIsEmerging();
        }
    }

    private DrownerPuddleEntity puddle;

    public DrownerPuddleEntity getPuddle() {
        return puddle;
    }

    public void setPuddle(DrownerPuddleEntity puddle) {
        this.puddle = puddle;
    }

    public int ReturnToGround_Ticks=20;

    public int getReturnToGround_Ticks() {
        return ReturnToGround_Ticks;
    }

    public void setReturnToGround_Ticks(int returnToGround_Ticks) {
        ReturnToGround_Ticks = returnToGround_Ticks;
    }

    @Override
    public boolean isInvulnerableTo(DamageSource damageSource) {
        return this.getIsEmerging() || this.getInGround() || super.isInvulnerableTo(damageSource);
    }
    @Override
    public boolean isPushable() {
        return super.isPushable() && !this.getIsEmerging() && !this.getInGround();
    }

    @Override
    public boolean isFireImmune() {
        return super.isFireImmune() || this.getInGround();
    }

    public static DefaultAttributeContainer.Builder setAttributes() {
        return AnimalEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 20.0D)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 4.0f) //Amount of health that hurts you
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.20f)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 0.5)
                .add(EntityAttributes.GENERIC_ARMOR,2f);
    }

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        super.initDataTracker(builder);
        builder.add(InGROUND, Boolean.FALSE);
        builder.add(EMERGING, Boolean.FALSE);
        builder.add(SPAWNED_PUDDLE, Boolean.FALSE);
        builder.add(INVISIBLE, Boolean.FALSE);
    }

    @Override
    protected Box calculateBoundingBox() {
        if (dataTracker.get(InGROUND)) {
            return groundBox(this);
        }
        else{
            // Normal hit-box otherwise
            return super.calculateBoundingBox();
        }
    }

    @Override
    protected EntityDimensions getBaseDimensions(EntityPose pose) {
        return this.getInGround()? this.getType().getDimensions().withEyeHeight(0.1f): super.getBaseDimensions(pose);
    }
    @Override
    public void onTrackedDataSet(TrackedData<?> data) {
        super.onTrackedDataSet(data);
        if (!this.getInGround() || this.getInGround()) {
            this.setBoundingBox(this.calculateBoundingBox());
            this.calculateDimensions();
        }
    }

    public final boolean getIsEmerging(){
        return this.dataTracker.get(EMERGING);
    }
    public final void setIsEmerging(boolean wasEmerging){
        this.dataTracker.set(EMERGING, wasEmerging);
    }

    public boolean getInGround() {
        return this.dataTracker.get(InGROUND);
    }
    public void setInGround(boolean wasInGround) {
        this.dataTracker.set(InGROUND, wasInGround);
    }

    public boolean getSpawnedPuddleDataTracker() {
        return this.dataTracker.get(SPAWNED_PUDDLE);
    }
    public void setSpawnedPuddleDataTracker(boolean puddleSpawned) {this.dataTracker.set(SPAWNED_PUDDLE, puddleSpawned);}

    public final boolean getInvisibleData() {
        return this.dataTracker.get(INVISIBLE);
    }
    public final void setInvisibleData(boolean isInvisible) {
        this.dataTracker.set(INVISIBLE, isInvisible);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        //Walk/Idle Controller
        controllers.add(new AnimationController<>(this, "Idle/Walk/Run", 5, state -> {
            //If it's attacking and moving
            if(this.isAttacking() && state.isMoving() ){
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
    public void spawnGroundParticles(@NotNull LivingEntity entity) {
        Random random = entity.getRandom();
        BlockState blockState = entity.getSteppingBlockState();
        if (blockState.getRenderType() != BlockRenderType.INVISIBLE) {
            for(int i = 0; i < 11; ++i) {
                double d = entity.getX() + (double) MathHelper.nextBetween(random, -0.7F, 0.7F);
                double e = entity.getY();
                double f = entity.getZ() + (double)MathHelper.nextBetween(random, -0.7F, 0.7F);

                if(i==5
                ){
                    entity.getWorld().addParticle(ParticleTypes.SPLASH, d, e, f, 0.0, 0.0, 0.0);
                }
                else{
                    entity.getWorld().addParticle(new BlockStateParticleEffect(ParticleTypes.BLOCK, blockState), d, e, f, 0.0, 0.0, 0.0);
                }
            }
        }
    }

    int AnimationParticlesTicks=36;

    public int getAnimationParticlesTicks() {
        return AnimationParticlesTicks;
    }

    public void setAnimationParticlesTicks(int animationParticlesTicks) {
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
    protected void mobTick() {
        if(!mudballCooldown){
            this.mobTickExcavator(
                    List.of(BlockTags.DIRT),
                    List.of(Blocks.SAND),
                    this
            );
        }

        this.setInvisible(this.getInvisibleData());
        super.mobTick();
    }



    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putInt("MudAttackTicks", ticksBetweenMudBalls);
        nbt.putBoolean("InGround", this.dataTracker.get(InGROUND));
        nbt.putInt("ReturnToGroundTicks", this.ReturnToGround_Ticks);
        nbt.putBoolean("PuddleSpawned",this.dataTracker.get(SPAWNED_PUDDLE));
        nbt.putBoolean("Invisible",this.dataTracker.get(INVISIBLE));
    }
    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        this.ticksBetweenMudBalls = nbt.getInt("MudAttackTicks");
        this.setInGround(nbt.getBoolean("InGround"));
        this.ReturnToGround_Ticks = nbt.getInt("ReturnToGroundTicks");
        this.setSpawnedPuddleDataTracker(nbt.getBoolean("PuddleSpawned"));
        this.setInvisibleData(nbt.getBoolean("Invisible"));
    }

    //Water creature things
    @Override
    public boolean isPushedByFluids() {
        return false;
    }

    //Speed on water
    @Override
    protected float getBaseMovementSpeedMultiplier() {
        return 0.91F;
    }

    //Sounds
    @Override
    protected SoundEvent getAmbientSound() {
        if(!this.getInGround()){
            return TCOTS_Sounds.WATER_HAG_IDLE;
        }
        else{
            return null;
        }
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return TCOTS_Sounds.WATER_HAG_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return TCOTS_Sounds.WATER_HAG_DEATH;
    }

    @Override
    public SoundEvent getEmergingSound() {
        return TCOTS_Sounds.WATER_HAG_EMERGING;
    }

    @Override
    public SoundEvent getDiggingSound() {
        return TCOTS_Sounds.WATER_HAG_DIGGING;
    }

    //Footsteps sounds
    protected SoundEvent getStepSound() {
        return TCOTS_Sounds.WATERY_FOOTSTEP;
    }

    //Attack Sound
    @Override
    protected SoundEvent getAttackSound() {
        return TCOTS_Sounds.WATER_HAG_ATTACK;
    }

    @Override
    public void checkDespawn() {
        if (this.getWorld().getDifficulty() == Difficulty.PEACEFUL && this.isDisallowedInPeaceful()) {
            if(this.puddle!=null){
               this.puddle.discard();
            }
            this.discard();
        } else if (!this.isPersistent() && !this.cannotDespawn()) {
            Entity entity = this.getWorld().getClosestPlayer(this, -1.0);
            if (entity != null) {
                double d = entity.squaredDistanceTo(this);
                int i = this.getType().getSpawnGroup().getImmediateDespawnRange();
                int j = i * i;
                if (d > (double)j && this.canImmediatelyDespawn(d)) {
                    if(puddle!=null){
                        puddle.discard();
                    }
                    this.discard();
                }

                int k = this.getType().getSpawnGroup().getDespawnStartRange();
                int l = k * k;
                if (this.despawnCounter > 600 && this.random.nextInt(800) == 0 && d > (double)l && this.canImmediatelyDespawn(d)) {
                    if(puddle!=null){
                        puddle.discard();
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

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
