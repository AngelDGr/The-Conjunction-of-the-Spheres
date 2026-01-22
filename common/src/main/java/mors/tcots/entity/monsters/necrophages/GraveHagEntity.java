package mors.tcots.entity.monsters.necrophages;

import mors.tcots.client.geo.animation.entity.necrophage.GraveHagAnimations;
import mors.tcots.registry.TCOTS_Sounds;
import mors.tcots.registry.TCOTS_Particles;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
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
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.EnumSet;
import java.util.List;

public class GraveHagEntity extends NecrophageMonster implements GeoEntity {
    //xTODO: Add tongue attack
    //xTODO: Add running attack
    //xTODO: Add drops
    //xTODO: Add mutagen and decoction
    //xTODO: Add spawn

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    protected static final EntityDataAccessor<Boolean> TONGUE_ATTACK = SynchedEntityData.defineId(GraveHagEntity.class, EntityDataSerializers.BOOLEAN);

    public GraveHagEntity(final EntityType<? extends GraveHagEntity> entityType, final Level world) {
        super(entityType, world);
        this.xpReward = 10;
    }

    @Override
    public int getMaxHeadYRot() {
        return 70;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));

        this.goalSelector.addGoal(2, new GraveHag_MeleeAttackGoal(this, 1.2D, false,
                200, 6f, 300));

        this.goalSelector.addGoal(3, new RandomStrollGoal(this, 0.75, 20));

        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));

        //Objectives
        this.targetSelector.addGoal(0, new HurtByTargetGoal(this, GraveHagEntity.class));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, AbstractVillager.class, true));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
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
        boolean tongueTriggered=false;
        public GraveHag_MeleeAttackGoal(final GraveHagEntity graveHag, final double speed, final boolean pauseWhenMobIdle, final int timeBetweenTongueAttacks, final float damageWithTongue, final int timeBetweenRunningAttacks) {
            this.graveHag = graveHag;
            this.speed = speed;
            this.pauseWhenMobIdle = pauseWhenMobIdle;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
            this.timeBetweenTongueAttacks = timeBetweenTongueAttacks;
            this.damageWithTongue = damageWithTongue;
            this.timeBetweenRunningAttacks=timeBetweenRunningAttacks;
        }

        @Override
        public boolean canUse() {
            final long l = this.graveHag.level().getGameTime();
            if (l - this.lastUpdateTime < 20L) {
                return false;
            }
            this.lastUpdateTime = l;
            final LivingEntity livingEntity = this.graveHag.getTarget();
            if (livingEntity == null) {
                return false;
            }
            if (!livingEntity.isAlive()) {
                return false;
            }
            this.path = this.graveHag.getNavigation().createPath(livingEntity, 0);
            if (this.path != null) {
                return true;
            }
            return this.getSquaredMaxAttackDistance(livingEntity) >= this.graveHag.distanceToSqr(livingEntity.getX(), livingEntity.getY(), livingEntity.getZ());
        }

        @Override
        public boolean canContinueToUse() {
            final LivingEntity livingEntity = this.graveHag.getTarget();
            if (livingEntity == null) {
                return false;
            }
            if (!livingEntity.isAlive()) {
                return false;
            }
            if (!this.pauseWhenMobIdle) {
                return !this.graveHag.getNavigation().isDone();
            }
            if (!this.graveHag.isWithinRestriction(livingEntity.blockPosition())) {
                return false;
            }
            return !(livingEntity instanceof Player) || !livingEntity.isSpectator() && !((Player)livingEntity).isCreative();
        }

        @Override
        public void start() {
            this.graveHag.getNavigation().moveTo(this.path, this.speed);
            this.graveHag.setAggressive(true);

            this.updateCountdownTicks = 0;
            this.cooldown = 0;
        }

        @Override
        public void stop() {
            if(graveHag.getTongueAttack()){graveHag.setTongueAttack(false);}
            if(graveHag.isSprinting()){graveHag.setSprinting(false);}
            runTriggered = false;

            final LivingEntity livingEntity = this.graveHag.getTarget();
            if (!EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(livingEntity)) {
                this.graveHag.setTarget(null);
            }
            this.graveHag.setAggressive(false);
            this.graveHag.getNavigation().stop();
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        public void tickO(final LivingEntity livingEntity){
            if (livingEntity == null) {
                return;
            }
            this.graveHag.getLookControl().setLookAt(livingEntity, 30.0f, 30.0f);
            final double d = this.graveHag.distanceToSqr(livingEntity);
            this.updateCountdownTicks = Math.max(this.updateCountdownTicks - 1, 0);
            if ((this.pauseWhenMobIdle || this.graveHag.getSensing().hasLineOfSight(livingEntity)) && this.updateCountdownTicks <= 0 && (this.targetX == 0.0 && this.targetY == 0.0 && this.targetZ == 0.0 || livingEntity.distanceToSqr(this.targetX, this.targetY, this.targetZ) >= 1.0 || this.graveHag.getRandom().nextFloat() < 0.05f)) {
                this.targetX = livingEntity.getX();
                this.targetY = livingEntity.getY();
                this.targetZ = livingEntity.getZ();
                this.updateCountdownTicks = 4 + this.graveHag.getRandom().nextInt(7);
                if (d > 1024.0) {
                    this.updateCountdownTicks += 10;
                } else if (d > 256.0) {
                    this.updateCountdownTicks += 5;
                }
                if (!this.graveHag.getNavigation().moveTo(livingEntity, this.speed)) {
                    this.updateCountdownTicks += 15;
                }
                this.updateCountdownTicks = this.adjustedTickDelay(this.updateCountdownTicks);
            }
            this.cooldown = Math.max(this.cooldown - 1, 0);
            this.attack(livingEntity, d);
        }

        protected void attack(final LivingEntity target, final double squaredDistance) {
            final double d = this.getSquaredMaxAttackDistance(target);
            if (squaredDistance <= d && this.cooldown <= 0) {
                this.resetCooldown();
                this.graveHag.swing(InteractionHand.MAIN_HAND);

                //Triggers Animation
                final int randomAttack = this.graveHag.getRandom().nextIntBetweenInclusive(0, 1);
                if (randomAttack == 0) {
                    graveHag.triggerAnim("base_controller", "attack1");
                } else {
                    graveHag.triggerAnim("base_controller", "attack2");
                }


                this.graveHag.doHurtTarget(target);
            }
        }

        @Override
        public void tick() {
            final LivingEntity target = this.graveHag.getTarget();
            final double d = this.graveHag.distanceToSqr(target);

            tongue_attack(d);
            running_attack(d);

            tickO(target);
        }

        private void tongue_attack(final double d) {
            if(!graveHag.cooldownTongueAttack && AnimationTicks== 5 && d < 5){
                graveHag.getNavigation().stop();
                graveHag.triggerAnim("base_controller","tongue");


                if (graveHag.isAlive()) {
                    final List<LivingEntity> listTargets = graveHag.level().getEntitiesOfClass(LivingEntity.class, graveHag.getBoundingBox().inflate(2.0),
                            livingEntity -> livingEntity.isAlive() && !(livingEntity instanceof GraveHagEntity));

                    for (final LivingEntity livingEntity : listTargets) {
                        if (!(livingEntity instanceof NecrophageMonster)) {
                            livingEntity.hurt(graveHag.damageSources().mobAttack(graveHag), damageWithTongue);
                            if(livingEntity.isBlocking() && livingEntity instanceof final Player player){
                                player.disableShield();
                            }
                        }
                        this.knockBack(livingEntity);
                    }
                }

                graveHag.playSound(TCOTS_Sounds.getSoundEvent("grave_hag_tongue_attack"), 1.5f, 1);
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

        private boolean heightBoolean(final float distance){
            return (graveHag.getTarget()!=null && (graveHag.getTarget().getY() - (graveHag.getY()) <= distance));
        }

        private void running_attack(final double d){
            if((!graveHag.cooldownRunningAttack) && d > 40 && !runTriggered
                    && heightBoolean())
            {
                graveHag.setSprinting(true);
                graveHag.playSound(TCOTS_Sounds.getSoundEvent("grave_hag_run"), 1f, 1);
                runTriggered=true;
            }

            if(runTriggered && (d < 8
                    || !heightBoolean(4)
            ))
            {
                graveHag.cooldownRunningAttack = true;
                graveHag.runningAttackCooldownTicks = timeBetweenRunningAttacks;
                runTriggered=false;
                graveHag.setSprinting(false);
            }
        }

        private void knockBack(final Entity entity) {
            final double d = entity.getX() - graveHag.getX();
            final double e = entity.getZ() - graveHag.getZ();
            final double f = Math.max(d * d + e * e, 0.001);
            entity.push(d / f * 0.5, 0.1, e / f * 0.5);
        }

        protected void resetCooldown() {
            this.cooldown = this.adjustedTickDelay(20);
        }

        protected double getSquaredMaxAttackDistance(final LivingEntity entity) {
            return this.graveHag.getBbWidth() * 2.0f * (this.graveHag.getBbWidth() * 2.0f) + entity.getBbWidth();
        }
    }
    public static AttributeSupplier.Builder setAttributes() {
        return Animal.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 30.0D)
                .add(Attributes.ATTACK_DAMAGE, 6.0f) //Amount of health that hurts you
                .add(Attributes.MOVEMENT_SPEED, 0.20f)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.5)
                .add(Attributes.ARMOR,4f);
    }

    @Override
    protected void defineSynchedData(final SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(TONGUE_ATTACK, Boolean.FALSE);
    }

    public final boolean getTongueAttack() {
        return this.entityData.get(TONGUE_ATTACK);
    }

    public final void setTongueAttack(final boolean wasAttacking) {
        this.entityData.set(TONGUE_ATTACK, wasAttacking);
    }

    @Override
    public void addAdditionalSaveData(@NotNull final CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putBoolean("TongueAttack", this.entityData.get(TONGUE_ATTACK));
        nbt.putInt("TongueAttackCooldown", tongueAttackCooldownTicks);
        nbt.putInt("RunningAttackCooldown", runningAttackCooldownTicks);
    }
    @Override
    public void readAdditionalSaveData(final CompoundTag nbt) {
        this.setTongueAttack(nbt.getBoolean("TongueAttack"));
        this.tongueAttackCooldownTicks = nbt.getInt("TongueAttackCooldown");
        this.runningAttackCooldownTicks = nbt.getInt("RunningAttackCooldown");
        super.readAdditionalSaveData(nbt);
    }

    @Override
    public int getMaxHeadXRot() {
        return this.isSprinting()? 1: 40;
    }

    @Override
    public float getSpeed() {
        return this.isSprinting()? super.getSpeed()*1.8f : super.getSpeed();
    }

    @Override
    public void registerControllers(final AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(GraveHagAnimations.mainController(this));

    }

    @Override
    public void tick() {
        if(this.getTongueAttack()){
            final Vec3 vec3dCenter = this.getBoundingBox().getCenter();
                int var3_5=0;
                while (var3_5 < 20) {
                    final double xOffset = Math.cos(this.animStep);
                    final double zOffset = Math.sin(this.animStep);

                    this.level().addParticle(TCOTS_Particles.GraveHagGreenSaliva(),
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

//        if(getIsRunning()){
//            spawnGroundParticles();
//        }


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
        return TCOTS_Sounds.getSoundEvent("grave_hag_idle");
    }

    @Override
    protected @NotNull SoundEvent getHurtSound(@NotNull final DamageSource source) {
        return TCOTS_Sounds.getSoundEvent("grave_hag_hurt");
    }

    @Override
    protected @NotNull SoundEvent getDeathSound() {
        return TCOTS_Sounds.getSoundEvent("grave_hag_death");
    }

    //Attack Sound
    @Override
    protected SoundEvent getAttackSound() {
        return TCOTS_Sounds.getSoundEvent("grave_hag_attack");
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
