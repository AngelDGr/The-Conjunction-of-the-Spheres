package TCOTS.entity.monsters.necrophages;

import TCOTS.entity.goals.*;
import TCOTS.entity.interfaces.ExcavatorMob;
import TCOTS.entity.misc.DrownerPuddleEntity;
import TCOTS.registry.TCOTS_Effects;
import TCOTS.registry.TCOTS_Particles;
import TCOTS.registry.TCOTS_Sounds;
import TCOTS.utils.BombsUtil;
import TCOTS.utils.GeoControllersUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.EnumSet;
import java.util.List;

public class BloedzuigerEntity extends NecrophageMonster implements GeoEntity, ExcavatorMob {

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    protected static final EntityDataAccessor<Boolean> InGROUND = SynchedEntityData.defineId(BloedzuigerEntity.class, EntityDataSerializers.BOOLEAN);
    protected static final EntityDataAccessor<Boolean> EMERGING = SynchedEntityData.defineId(BloedzuigerEntity.class, EntityDataSerializers.BOOLEAN);
    protected static final EntityDataAccessor<Boolean> INVISIBLE = SynchedEntityData.defineId(BloedzuigerEntity.class, EntityDataSerializers.BOOLEAN);
    protected static final EntityDataAccessor<Boolean> SPAWNED_PUDDLE = SynchedEntityData.defineId(BloedzuigerEntity.class, EntityDataSerializers.BOOLEAN);

    protected static final EntityDataAccessor<Boolean> EXPLODING = SynchedEntityData.defineId(BloedzuigerEntity.class, EntityDataSerializers.BOOLEAN);
    protected static final EntityDataAccessor<Boolean> TRIGGER_EXPLOSION = SynchedEntityData.defineId(BloedzuigerEntity.class, EntityDataSerializers.BOOLEAN);

    public static final RawAnimation EXPLOSION = RawAnimation.begin().thenPlayAndHold("special.explosion");

    public BloedzuigerEntity(final EntityType<? extends NecrophageMonster> entityType, final Level world) {
        super(entityType, world);
    }

    //Natural Spawn
    public static boolean canSpawnBloedzuiger(final EntityType<? extends NecrophageMonster> type, final ServerLevelAccessor world, final MobSpawnType spawnReason, final BlockPos pos, final RandomSource random) {
        return
                spawnReason == MobSpawnType.SPAWNER ||(
                        world.getDifficulty() != Difficulty.PEACEFUL &&
                                pos.getY() >= world.getSeaLevel() - 10 &&

                                (world.getBlockState(pos.below()).is(Blocks.SAND)
                                        || world.getBlockState(pos.below()).is(Blocks.DIRT)
                                        || world.getBlockState(pos.below()).is(BlockTags.FROGS_SPAWNABLE_ON))
                );

    }

    public static AttributeSupplier.Builder setAttributes() {
        return Animal.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 30.0D)
                .add(Attributes.ATTACK_DAMAGE, 5.0f) //Amount of health that hurts you
                .add(Attributes.MOVEMENT_SPEED, 0.20f)

                .add(Attributes.KNOCKBACK_RESISTANCE, 0.5);
    }

    @Override
    public int getMaxHeadYRot() {
        return 20;
    }

    @Override
    protected void registerGoals() {
        //Emerge from ground
        this.goalSelector.addGoal(0, new Bloedzuiger_Explosion(this, 0.25f));
        this.goalSelector.addGoal(0, new EmergeFromGroundGoal_Excavator(this, 500, true));
        this.goalSelector.addGoal(1, new FloatGoal(this));

        //Returns to ground
        this.goalSelector.addGoal(2, new ReturnToGroundGoal_Excavator(this, true));

        this.goalSelector.addGoal(3, new MeleeAttackGoal_Excavator(this, 1.2D, false, 3600));

        this.goalSelector.addGoal(4, new WanderAroundGoal_Excavator(this, 0.75f, 20));

        this.goalSelector.addGoal(5, new LookAroundGoal_Excavator(this));

        //Objectives
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this, BloedzuigerEntity.class));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, AbstractVillager.class, true));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
    }

    private class Bloedzuiger_Explosion extends Goal {
        int AnimationTicks = 10;

        final float percentageHealth;

        private final BloedzuigerEntity bloedzuiger;

        private Bloedzuiger_Explosion(final BloedzuigerEntity mob, final float percentageHealth) {
            this.bloedzuiger = mob;
            this.percentageHealth=percentageHealth;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            return (BloedzuigerEntity.this.getHealth() < (BloedzuigerEntity.this.getMaxHealth() * percentageHealth) && !bloedzuiger.isOnFire());
        }

        @Override
        public void start() {
            this.bloedzuiger.playSound(this.bloedzuiger.getExplosionSound(), 1.0F, 1.0F);
            this.bloedzuiger.triggerAnim("ExplosionController", "explosion");
            this.bloedzuiger.setIsExploding(true);
            bloedzuiger.getNavigation().stop();
            bloedzuiger.getLookControl().setLookAt(0, 0, 0);
            AnimationTicks = 10;
        }

        @Override
        public void tick() {
            if (AnimationTicks > 0) {
                --AnimationTicks;
            } else {
                stop();
            }
        }

        @Override
        public void stop() {
            bloedzuiger.setIsTriggerExplosion(true);
        }
    }

    @Override
    protected void defineSynchedData(final SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(EXPLODING, Boolean.FALSE);
        builder.define(TRIGGER_EXPLOSION, Boolean.FALSE);

        builder.define(InGROUND, Boolean.FALSE);
        builder.define(EMERGING, Boolean.FALSE);
        builder.define(INVISIBLE, Boolean.FALSE);
        builder.define(SPAWNED_PUDDLE, Boolean.FALSE);
    }

    public final boolean getIsExploding() {
        return this.entityData.get(EXPLODING);
    }

    public final void setIsExploding(final boolean wasExploding) {
        this.entityData.set(EXPLODING, wasExploding);
    }

    public final boolean getIsTriggerExplosion() {
        return this.entityData.get(TRIGGER_EXPLOSION);
    }
    public final void setIsTriggerExplosion(final boolean wasExplosion) {
        this.entityData.set(TRIGGER_EXPLOSION, wasExplosion);
    }

    //Excavator Common
    @Override
    public void addAdditionalSaveData(@NotNull final CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putBoolean("InGround", this.entityData.get(InGROUND));
        nbt.putInt("ReturnToGroundTicks", this.ReturnToGround_Ticks);
        nbt.putBoolean("Invisible", this.entityData.get(INVISIBLE));

        nbt.putBoolean("PuddleSpawned",this.entityData.get(SPAWNED_PUDDLE));
    }

    @Override
    public void readAdditionalSaveData(final CompoundTag nbt) {
        this.setInGround(nbt.getBoolean("InGround"));
        this.ReturnToGround_Ticks = nbt.getInt("ReturnToGroundTicks");
        this.setInvisibleData(nbt.getBoolean("Invisible"));

        this.setSpawnedPuddleDataTracker(nbt.getBoolean("PuddleSpawned"));
        super.readAdditionalSaveData(nbt);
    }

    @Override
    public void tick() {
        //Detect own puddle
        tickPuddle(this);

        //Triggers the blood explosion
        if (this.getIsTriggerExplosion()) {
            this.explode();
        }

        //Counter for particles
        this.tickExcavator(this);

        super.tick();
    }

    protected void explode() {
        if (!this.level().isClientSide) {
            this.dead = true;
            this.level().explode(this, null, null,
                    this.getX(), this.getY(), this.getZ(), (float)3, false, Level.ExplosionInteraction.MOB,
                    TCOTS_Particles.BloedzuigerBloodEmitter(), TCOTS_Particles.BloedzuigerBloodEmitter(), TCOTS_Sounds.getRotfiendBloodExplosion());

            final List<LivingEntity> list = this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(3.1),
                    livingEntity -> !(livingEntity instanceof ArmorStand));


            for (final LivingEntity entity : list) {
                //To not apply effect across walls
                if(BombsUtil.getExposure(entity.position(), this) == 0) continue;

                entity.addEffect(new MobEffectInstance(TCOTS_Effects.Cadaverine(), 200), this);
            }

            this.discard();
        }
    }

    @Override
    public boolean shouldBlockExplode(@NotNull final Explosion explosion, @NotNull final BlockGetter world, @NotNull final BlockPos pos, @NotNull final BlockState state, final float explosionPower) {
        return false;
    }

    @Override
    protected void dropFromLootTable(@NotNull final DamageSource damageSource, final boolean causedByPlayer) {
        if(this.isOnFire()){
            super.dropFromLootTable(damageSource, causedByPlayer);
        }
    }

    @Override
    public int getBaseExperienceReward() {
        if(this.isOnFire()){
            return super.getBaseExperienceReward();
        }
        return 0;
    }

    public int ticksSinceDeath=30;
    @Override
    protected void tickDeath() {
        if(!this.isOnFire()) {
            if (ticksSinceDeath == 30) {
                this.playSound(this.getExplosionSound(), 1.0F, 1.0F);
                this.triggerAnim("ExplosionController","explosion");
                this.setIsExploding(true);
                this.getNavigation().stop();
                this.getLookControl().setLookAt(0, 0, 0);
            }
            if (ticksSinceDeath > 0) {
                --this.ticksSinceDeath;
            } else {
                this.setIsTriggerExplosion(true);
            }
        }
        else {
            super.tickDeath();
        }
    }

    @Override
    protected void customServerAiStep() {
        mobTickExcavator(
                List.of(BlockTags.DIRT),
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
        return this.getIsEmerging() || this.getInGround() || this.getIsExploding() || super.isInvulnerableTo(damageSource);
    }

    @Override
    public boolean isPushable() {
        return super.isPushable() && !this.getIsExploding() && !this.getIsEmerging() && !this.getInGround();
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

    private DrownerPuddleEntity puddle;

    public DrownerPuddleEntity getPuddle() {
        return puddle;
    }

    public void setPuddle(final DrownerPuddleEntity puddle) {
        this.puddle = puddle;
    }

    public boolean getSpawnedPuddleDataTracker() {
        return this.entityData.get(SPAWNED_PUDDLE);
    }
    public void setSpawnedPuddleDataTracker(final boolean puddleSpawned) {this.entityData.set(SPAWNED_PUDDLE, puddleSpawned);}

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

    //Cadaverine damage
    @Override
    public boolean doHurtTarget(final @NotNull Entity target) {
        final boolean bl = super.doHurtTarget(target);
        //12.5% probability of infecting with cadaverine
        if(target instanceof final LivingEntity living && bl && this.random.nextInt()%8==0){
            living.addEffect(new MobEffectInstance(TCOTS_Effects.Cadaverine(), 200), this);
        }

        if(target instanceof final Player player && player.isBlocking()){
            player.getOffhandItem().hurtAndBreak(3, player, getSlotForHand(player.getUsedItemHand()));
        }

        return bl;
    }

    @Override
    public void registerControllers(final AnimatableManager.ControllerRegistrar controllerRegistrar) {
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

        //Explosion Controller
        controllerRegistrar.add(
                new AnimationController<>(this, "ExplosionController", 1, state -> PlayState.CONTINUE)
                        .triggerableAnim("explosion", EXPLOSION)
        );

        //DiggingIn Controller
        controllerRegistrar.add(
                new AnimationController<>(this,"DiggingController",1, this::animationDiggingPredicate)
        );

        //DiggingOut Controller
        controllerRegistrar.add(
                new AnimationController<>(this, "EmergingController", 1, this::animationEmergingPredicate)
        );
    }

    //Sounds
    @Override
    protected SoundEvent getAmbientSound() {
        if (!this.getIsExploding() && !this.getInGround()) {
            return TCOTS_Sounds.getSoundEvent("bloedzuiger_idle");
        } else {
            return null;
        }
    }

    @Override
    public boolean canBeAffected(final MobEffectInstance effect) {
        return effect.getEffect() != TCOTS_Effects.SamumEffect() && effect.getEffect() != TCOTS_Effects.Cadaverine() && super.canBeAffected(effect);
    }

    @Override
    protected SoundEvent getHurtSound(@NotNull final DamageSource source) {
        return TCOTS_Sounds.getSoundEvent("bloedzuiger_hurt");
    }

    @Override
    protected SoundEvent getDeathSound() {
        if (!this.isOnFire()) {
            return null;
        } else {
            return TCOTS_Sounds.getSoundEvent("bloedzuiger_death");
        }
    }

    @Override
    protected SoundEvent getAttackSound() {
        return TCOTS_Sounds.getSoundEvent("bloedzuiger_attack");
    }

    public SoundEvent getExplosionSound(){
        return TCOTS_Sounds.getSoundEvent("bloedzuiger_exploding");
    }
    
    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
