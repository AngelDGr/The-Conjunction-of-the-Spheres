package TCOTS.entity.necrophages;

import TCOTS.utils.GeoControllersUtil;
import TCOTS.particles.TCOTS_Particles;
import TCOTS.sounds.TCOTS_Sounds;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

import java.util.EnumSet;
import java.util.List;

public class GraveHagEntity extends Necrophage_Base implements GeoEntity {

    //xTODO: Add tongue attack
    //xTODO: Add running attack
    //xTODO: Add drops
    //xTODO: Add mutagen and decoction
    //xTODO: Add spawn

    private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    public static final RawAnimation ATTACK_TONGUE = RawAnimation.begin().thenPlay("attack.tongue2");
    public static final RawAnimation ATTACK_RUN = RawAnimation.begin().thenPlay("attack.run");

    protected static final TrackedData<Boolean> TONGUE_ATTACK = DataTracker.registerData(GraveHagEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    protected static final TrackedData<Boolean> RUNNING = DataTracker.registerData(GraveHagEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    public GraveHagEntity(EntityType<? extends GraveHagEntity> entityType, World world) {
        super(entityType, world);
        this.experiencePoints = 10;
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));

        this.goalSelector.add(2, new GraveHag_MeleeAttackGoal(this, 1.2D, false,
                200, 6f, 300, 1.8f));

        this.goalSelector.add(3, new WanderAroundGoal(this, 0.75, 20));

        this.goalSelector.add(4, new LookAroundGoal(this));

        //Objectives
        this.targetSelector.add(0, new RevengeGoal(this, GraveHagEntity.class));
        this.targetSelector.add(1, new ActiveTargetGoal<>(this, PlayerEntity.class, true));
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, MerchantEntity.class, true));
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, IronGolemEntity.class, true));
    }

    boolean cooldownTongueAttack=false;
    boolean cooldownRunningAttack=false;
    int tongueAttackCooldownTicks;
    int runningAttackCooldownTicks;

    private static class GraveHag_MeleeAttackGoal extends Goal{
        private final int timeBetweenTongueAttacks;
        private final float damageWithTongue;
        private final int timeBetweenRunningAttacks;
        protected final GraveHagEntity graveHag;
        private final double speed;
        private final boolean pauseWhenMobIdle;
        private Path path;
        private double targetX;
        private double targetY;
        private double targetZ;
        private int updateCountdownTicks;
        private int cooldown;
        private long lastUpdateTime;
        int AnimationTicks = 5;
        float speedMultiplierBase =1;
        private final float speedMultiplier;
        boolean tongueTriggered=false;
        public GraveHag_MeleeAttackGoal(GraveHagEntity graveHag, double speed, boolean pauseWhenMobIdle, int timeBetweenTongueAttacks, float damageWithTongue, int timeBetweenRunningAttacks, float speedMultiplierWhenRun) {
            this.graveHag = graveHag;
            this.speed = speed;
            this.pauseWhenMobIdle = pauseWhenMobIdle;
            this.setControls(EnumSet.of(Goal.Control.MOVE, Goal.Control.LOOK));
            this.timeBetweenTongueAttacks = timeBetweenTongueAttacks;
            this.damageWithTongue = damageWithTongue;
            this.timeBetweenRunningAttacks=timeBetweenRunningAttacks;
            this.speedMultiplier=speedMultiplierWhenRun;
        }

        @Override
        public boolean canStart() {
            long l = this.graveHag.getWorld().getTime();
            if (l - this.lastUpdateTime < 20L) {
                return false;
            }
            this.lastUpdateTime = l;
            LivingEntity livingEntity = this.graveHag.getTarget();
            if (livingEntity == null) {
                return false;
            }
            if (!livingEntity.isAlive()) {
                return false;
            }
            this.path = this.graveHag.getNavigation().findPathTo(livingEntity, 0);
            if (this.path != null) {
                return true;
            }
            return this.getSquaredMaxAttackDistance(livingEntity) >= this.graveHag.squaredDistanceTo(livingEntity.getX(), livingEntity.getY(), livingEntity.getZ());
        }

        @Override
        public boolean shouldContinue() {
            LivingEntity livingEntity = this.graveHag.getTarget();
            if (livingEntity == null) {
                return false;
            }
            if (!livingEntity.isAlive()) {
                return false;
            }
            if (!this.pauseWhenMobIdle) {
                return !this.graveHag.getNavigation().isIdle();
            }
            if (!this.graveHag.isInWalkTargetRange(livingEntity.getBlockPos())) {
                return false;
            }
            return !(livingEntity instanceof PlayerEntity) || !livingEntity.isSpectator() && !((PlayerEntity)livingEntity).isCreative();
        }

        @Override
        public void start() {
            this.graveHag.getNavigation().startMovingAlong(this.path, this.speed * speedMultiplierBase);
            this.graveHag.setAttacking(true);

            this.updateCountdownTicks = 0;
            this.cooldown = 0;
        }

        @Override
        public void stop() {
            if(graveHag.getTongueAttack()){graveHag.setTongueAttack(false);}
            if(graveHag.getIsRunning()){graveHag.setIsRunning(false);}
            speedMultiplierBase = 1;
            runTriggered = false;

            LivingEntity livingEntity = this.graveHag.getTarget();
            if (!EntityPredicates.EXCEPT_CREATIVE_OR_SPECTATOR.test(livingEntity)) {
                this.graveHag.setTarget(null);
            }
            this.graveHag.setAttacking(false);
            this.graveHag.getNavigation().stop();
        }

        @Override
        public boolean shouldRunEveryTick() {
            return true;
        }

        public void tickO(LivingEntity livingEntity){
            if (livingEntity == null) {
                return;
            }
            this.graveHag.getLookControl().lookAt(livingEntity, 30.0f, 30.0f);
            double d = this.graveHag.squaredDistanceTo(livingEntity);
            this.updateCountdownTicks = Math.max(this.updateCountdownTicks - 1, 0);
            if ((this.pauseWhenMobIdle || this.graveHag.getVisibilityCache().canSee(livingEntity)) && this.updateCountdownTicks <= 0 && (this.targetX == 0.0 && this.targetY == 0.0 && this.targetZ == 0.0 || livingEntity.squaredDistanceTo(this.targetX, this.targetY, this.targetZ) >= 1.0 || this.graveHag.getRandom().nextFloat() < 0.05f)) {
                this.targetX = livingEntity.getX();
                this.targetY = livingEntity.getY();
                this.targetZ = livingEntity.getZ();
                this.updateCountdownTicks = 4 + this.graveHag.getRandom().nextInt(7);
                if (d > 1024.0) {
                    this.updateCountdownTicks += 10;
                } else if (d > 256.0) {
                    this.updateCountdownTicks += 5;
                }
                if (!this.graveHag.getNavigation().startMovingTo(livingEntity, this.speed* speedMultiplierBase)) {
                    this.updateCountdownTicks += 15;
                }
                this.updateCountdownTicks = this.getTickCount(this.updateCountdownTicks);
            }
            this.cooldown = Math.max(this.cooldown - 1, 0);
            this.attack(livingEntity, d);
        }

        protected void attack(LivingEntity target, double squaredDistance) {
            double d = this.getSquaredMaxAttackDistance(target);
            if (squaredDistance <= d && this.cooldown <= 0) {
                this.resetCooldown();
                this.graveHag.swingHand(Hand.MAIN_HAND);

                //Triggers Animation
                int randomAttack = this.graveHag.getRandom().nextBetween(0, 1);
                if (randomAttack == 0) {
                    graveHag.triggerAnim("AttackController", "attack1");
                } else {
                    graveHag.triggerAnim("AttackController", "attack2");
                }


                this.graveHag.tryAttack(target);
            }
        }

        @Override
        public void tick() {
            LivingEntity target = this.graveHag.getTarget();
            double d = this.graveHag.squaredDistanceTo(target);

            tongue_attack(d);
            running_attack(d);

            tickO(target);
        }

        private void tongue_attack(double d) {
            if(!graveHag.cooldownTongueAttack && AnimationTicks== 5 && d < 5){
                graveHag.getNavigation().stop();
                graveHag.triggerAnim("TongueAttack","tongue");


                if (graveHag.isAlive()) {
                    List<LivingEntity> listTargets = graveHag.getWorld().getEntitiesByClass(LivingEntity.class, graveHag.getBoundingBox().expand(2.0),
                            livingEntity -> livingEntity.isAlive() && !(livingEntity instanceof GraveHagEntity));

                    for (LivingEntity livingEntity : listTargets) {
                        if (!(livingEntity instanceof Necrophage_Base)) {
                            livingEntity.damage(graveHag.getDamageSources().mobAttack(graveHag), damageWithTongue);
                            if(livingEntity.isBlocking() && livingEntity instanceof PlayerEntity){
                                ((PlayerEntity) livingEntity).disableShield(true);
                            }
                        }
                        this.knockBack(livingEntity);
                    }
                }

                graveHag.playSound(TCOTS_Sounds.GRAVE_HAG_TONGUE_ATTACK, 1.5f, 1);
                --AnimationTicks;
                tongueTriggered=true;
            }

            if (AnimationTicks > 0 && !graveHag.cooldownTongueAttack && tongueTriggered) {
                graveHag.getNavigation().stop();
                --AnimationTicks;
            } else if(AnimationTicks==0){
                AnimationTicks = 5;
                tongueTriggered = false;
                graveHag.tongueAttackCooldownTicks = timeBetweenTongueAttacks;
                graveHag.cooldownTongueAttack = true;
            }
        }
        boolean runTriggered=false;

        private boolean heightBoolean(){
            return heightBoolean(1.5f);
        }

        private boolean heightBoolean(float distance){
            return (graveHag.getTarget()!=null && (graveHag.getTarget().getY() - (graveHag.getY()) <= distance));
        }

        private void running_attack(double d){
            if((!graveHag.cooldownRunningAttack) && d > 40 && !runTriggered
                    && heightBoolean()
            )
            {
                graveHag.setIsRunning(true);
                speedMultiplierBase=speedMultiplier;
                graveHag.playSound(TCOTS_Sounds.GRAVE_HAG_RUN, 1f, 1);
                runTriggered=true;
            }

            if(runTriggered && (d < 8
                    || !heightBoolean(4)
            ))
            {
                graveHag.cooldownRunningAttack = true;
                graveHag.runningAttackCooldownTicks = timeBetweenRunningAttacks;
                speedMultiplierBase = 1;
                runTriggered=false;
                graveHag.setIsRunning(false);
            }
        }

        private void knockBack(Entity entity) {
            double d = entity.getX() - graveHag.getX();
            double e = entity.getZ() - graveHag.getZ();
            double f = Math.max(d * d + e * e, 0.001);
            entity.addVelocity(d / f * 0.5, 0.1, e / f * 0.5);
        }

        protected void resetCooldown() {
            this.cooldown = this.getTickCount(20);
        }

        protected double getSquaredMaxAttackDistance(LivingEntity entity) {
            return this.graveHag.getWidth() * 2.0f * (this.graveHag.getWidth() * 2.0f) + entity.getWidth();
        }
    }
    public static DefaultAttributeContainer.Builder setAttributes() {
        return AnimalEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 30.0D)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 6.0f) //Amount of health that hurts you
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.20f)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 0.5)
                .add(EntityAttributes.GENERIC_ARMOR,4f);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(TONGUE_ATTACK, Boolean.FALSE);
        this.dataTracker.startTracking(RUNNING, Boolean.FALSE);
    }

    public final boolean getTongueAttack() {
        return this.dataTracker.get(TONGUE_ATTACK);
    }

    public final void setTongueAttack(boolean wasAttacking) {
        this.dataTracker.set(TONGUE_ATTACK, wasAttacking);
    }

    public final boolean getIsRunning() {
        return this.dataTracker.get(RUNNING);
    }

    public final void setIsRunning(boolean wasRunning) {
        this.dataTracker.set(RUNNING, wasRunning);
    }
    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putBoolean("TongueAttack", this.dataTracker.get(TONGUE_ATTACK));
        nbt.putInt("TongueAttackCooldown", tongueAttackCooldownTicks);
        nbt.putBoolean("Running", this.dataTracker.get(RUNNING));
        nbt.putInt("RunningAttackCooldown", runningAttackCooldownTicks);
    }
    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        this.setTongueAttack(nbt.getBoolean("TongueAttack"));
        this.tongueAttackCooldownTicks = nbt.getInt("TongueAttackCooldown");
        this.setIsRunning(nbt.getBoolean("Running"));
        this.runningAttackCooldownTicks = nbt.getInt("RunningAttackCooldown");
        super.readCustomDataFromNbt(nbt);
    }

    @Override
    public int getMaxLookPitchChange() {
        return this.getIsRunning()? 1: 40;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        //Walk/Idle Controller
        controllers.add(new AnimationController<>(this, "Idle/Walk/Run", 1, state -> {
            //If it's running
            if(this.getIsRunning()){
                return state.setAndContinue(ATTACK_RUN);
            }
            //If it's moving
            else if (state.isMoving()) {
                return state.setAndContinue(WALKING);
            }
            //Anything else
            else {
                return state.setAndContinue(IDLE);
            }
        }));

        //Attack Controller
        controllers.add(
                new AnimationController<>(this, "AttackController", 1, state -> PlayState.STOP)
                        .triggerableAnim("attack1", GeoControllersUtil.ATTACK1)
                        .triggerableAnim("attack2", GeoControllersUtil.ATTACK2)
        );

        //TongueAttack Controller
        controllers.add(new AnimationController<>(this, "TongueAttack", 1, state -> PlayState.STOP)
                .triggerableAnim("tongue", ATTACK_TONGUE));
    }

    @Override
    public int getNumberOfAttackAnimations() {
        return 2;
    }

    private void spawnGroundParticles() {
        BlockState blockState = this.getSteppingBlockState();
        if (blockState.getRenderType() != BlockRenderType.INVISIBLE) {
            for (int i = 0; i < 8; ++i) {
                double d = this.getX() + (double) MathHelper.nextBetween(random, -0.7F, 0.7F);
                double e = this.getY();
                double f = this.getZ() + (double) MathHelper.nextBetween(random, -0.7F, 0.7F);

                this.getWorld().addParticle(new BlockStateParticleEffect(ParticleTypes.BLOCK, blockState), d, e, f, 0.0, 0.0, 0.0);
            }
        }
    }
    @Override
    public void tick() {
        if(this.getTongueAttack()){
            Vec3d vec3dCenter = this.getBoundingBox().getCenter();
                int var3_5=0;
                while (var3_5 < 20) {
                    double xOffset = Math.cos(this.lookDirection);
                    double zOffset = Math.sin(this.lookDirection);

                    this.getWorld().addParticle(TCOTS_Particles.GRAVE_HAG_GREEN_SALIVA,
                            vec3dCenter.x,
                            this.getEyeY()-0.2,
                            vec3dCenter.z,
                            xOffset,0,zOffset);

                    ++var3_5;
            }
        }

        if (GraveHagEntity.this.tongueAttackCooldownTicks > 0) {
            --GraveHagEntity.this.tongueAttackCooldownTicks;
        } else {
            GraveHagEntity.this.cooldownTongueAttack = false;
        }

        if(getIsRunning()){
            spawnGroundParticles();
        }


        if (GraveHagEntity.this.runningAttackCooldownTicks > 0) {
            --GraveHagEntity.this.runningAttackCooldownTicks;
        } else {
            GraveHagEntity.this.cooldownRunningAttack = false;
        }

        super.tick();
    }

    //Sounds
    @Override
    protected SoundEvent getAmbientSound() {
        return TCOTS_Sounds.GRAVE_HAG_IDLE;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return TCOTS_Sounds.GRAVE_HAG_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return TCOTS_Sounds.GRAVE_HAG_DEATH;
    }

    //Attack Sound
    @Override
    protected SoundEvent getAttackSound() {
        return TCOTS_Sounds.GRAVE_HAG_ATTACK;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
