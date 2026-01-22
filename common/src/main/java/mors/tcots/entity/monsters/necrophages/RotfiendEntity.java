package mors.tcots.entity.monsters.necrophages;

import mors.tcots.client.geo.animation.entity.necrophage.RotfiendAnimations;
import mors.tcots.entity.goal.*;
import mors.tcots.registry.TCOTS_Criteria;
import mors.tcots.registry.TCOTS_Sounds;
import mors.tcots.entity.interfaces.ExcavatorMob;
import mors.tcots.entity.interfaces.LungeMob;
import mors.tcots.registry.TCOTS_Particles;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.TraceableEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.EnumSet;
import java.util.List;
import java.util.UUID;

public class RotfiendEntity extends NecrophageMonster implements GeoEntity, ExcavatorMob, LungeMob, TraceableEntity {

    //xTODO: Add Emerging animation (And digging?)
    //xTODO: Add drops
    //xTODO: Add spawning
    //xTODO: Fix the invisible bug
    //xTODO: Add follow leader goal

    @Nullable
    private Mob owner;
    @Nullable
    private UUID ownerUuid;
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public static final RawAnimation EXPLOSION = RawAnimation.begin().thenPlayAndHold("special.explosion");

    protected static final EntityDataAccessor<Boolean> EXPLODING = SynchedEntityData.defineId(RotfiendEntity.class, EntityDataSerializers.BOOLEAN);
    protected static final EntityDataAccessor<Boolean> TRIGGER_EXPLOSION = SynchedEntityData.defineId(RotfiendEntity.class, EntityDataSerializers.BOOLEAN);

    protected static final EntityDataAccessor<Boolean> InGROUND = SynchedEntityData.defineId(RotfiendEntity.class, EntityDataSerializers.BOOLEAN);
    protected static final EntityDataAccessor<Boolean> EMERGING = SynchedEntityData.defineId(RotfiendEntity.class, EntityDataSerializers.BOOLEAN);
    protected static final EntityDataAccessor<Boolean> INVISIBLE = SynchedEntityData.defineId(RotfiendEntity.class, EntityDataSerializers.BOOLEAN);
    protected static final EntityDataAccessor<Boolean> BULLVORE_GROUP = SynchedEntityData.defineId(RotfiendEntity.class, EntityDataSerializers.BOOLEAN);


    public RotfiendEntity(final EntityType<? extends RotfiendEntity> entityType, final Level world) {
        super(entityType, world);
    }

    @Override
    public int getMaxHeadYRot() {
        return 45;
    }

    @Override
    protected void registerGoals() {
        //Attack
        //Emerge from ground
        this.goalSelector.addGoal(0, new Rotfiend_Explosion(this, 0.25f));
        this.goalSelector.addGoal(0, new EmergeFromGroundGoal_Excavator(this, 500));
        this.goalSelector.addGoal(1, new FloatGoal(this));


        this.goalSelector.addGoal(2, new LungeAttackGoal(this, 100, 0.6,5,25));

        //Returns to ground
        this.goalSelector.addGoal(3, new ReturnToGroundGoal_Excavator(this));

        this.goalSelector.addGoal(4, new MeleeAttackGoal_Excavator(this, 1.2D, false, 0, true));

        this.goalSelector.addGoal(5, new FollowMonsterOwnerGoal(this, 0.75));

        this.goalSelector.addGoal(6, new WanderAroundGoal_Excavator(this, 0.75f, 100));

        this.goalSelector.addGoal(7, new LookAroundGoal_Excavator(this));

        //Objectives
        this.targetSelector.addGoal(0, new AttackOwnerAttackerTarget(this));
        this.targetSelector.addGoal(1, new AttackOwnerEnemyTarget(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, AbstractVillager.class, true));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
    }


    public boolean cooldownBetweenLunges = false;
    @Override
    public boolean getNotCooldownBetweenLunges() {
        return !cooldownBetweenLunges;
    }

    @Override
    public void setCooldownBetweenLunges(final boolean cooldownBetweenLunges) {
        this.cooldownBetweenLunges = cooldownBetweenLunges;
    }
    public int LungeTicks;
    @Override
    public int getLungeTicks() {
        return LungeTicks;
    }


    @Override
    public void setLungeTicks(final int lungeTicks) {
        LungeTicks = lungeTicks;
    }

    @Nullable
    @Override
    public Entity getOwner() {
        final Entity entity;
        if (this.owner == null && this.ownerUuid != null && this.level() instanceof ServerLevel && (entity = ((ServerLevel)this.level()).getEntity(this.ownerUuid)) instanceof LivingEntity) {
            this.owner = (Mob) entity;
        }
        return this.owner;
    }

    public void setOwner(@Nullable final Mob owner) {
        this.owner = owner;
        this.ownerUuid = owner == null ? null : owner.getUUID();
    }

    private class Rotfiend_Explosion extends Goal {
        int AnimationTicks = 30;

        final float percentageHealth;

        private final RotfiendEntity rotfiend;

        private Rotfiend_Explosion(final RotfiendEntity mob, final float percentageHealth) {
            this.rotfiend = mob;
            this.percentageHealth=percentageHealth;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            return (RotfiendEntity.this.getHealth() < (RotfiendEntity.this.getMaxHealth() * percentageHealth) && !rotfiend.isOnFire());
        }

        @Override
        public void start() {
            this.rotfiend.playSound(this.rotfiend.getExplosionSound(), 1.0F, 1.0F);
            this.rotfiend.triggerAnim("base_controller", "explosion");
            this.rotfiend.setIsExploding(true);
            rotfiend.getNavigation().stop();
            rotfiend.getLookControl().setLookAt(0, 0, 0);
            AnimationTicks = 30;
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
            rotfiend.setIsTriggerExplosion(true);
        }
    }

    public int ReturnToGround_Ticks=20;

    public int getReturnToGround_Ticks() {
        return ReturnToGround_Ticks;
    }

    public void setReturnToGround_Ticks(final int returnToGround_Ticks) {
        ReturnToGround_Ticks = returnToGround_Ticks;
    }

    public static AttributeSupplier.Builder setAttributes() {
        return Animal.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20.0D)
                .add(Attributes.ATTACK_DAMAGE, 3.0f) //Amount of health that hurts you
                .add(Attributes.MOVEMENT_SPEED, 0.27f);
    }

    @Override
    public void registerControllers(final AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(RotfiendAnimations.mainController(this));

        //Lunge Controller
        lungeAnimationController(this, controllerRegistrar);

        //DiggingIn Controller
        controllerRegistrar.add(
                new AnimationController<>(this,"DiggingController",1, this::animationDiggingPredicate)
        );

        //DiggingOut Controller
        controllerRegistrar.add(
                new AnimationController<>(this, "EmergingController", 1, this::animationEmergingPredicate)
        );
    }

    @Override
    protected void defineSynchedData(final SynchedEntityData.@NotNull Builder builder) {
        super.defineSynchedData(builder);

        builder.define(EXPLODING, Boolean.FALSE);
        builder.define(TRIGGER_EXPLOSION, Boolean.FALSE);
        builder.define(InGROUND, Boolean.FALSE);
        builder.define(EMERGING, Boolean.FALSE);
        builder.define(INVISIBLE, Boolean.FALSE);

        builder.define(BULLVORE_GROUP, Boolean.FALSE);
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

    public final boolean getInvisibleData() {
        return this.entityData.get(INVISIBLE);
    }
    public final void setInvisibleData(final boolean isInvisible) {
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

    @Override
    public void addAdditionalSaveData(@NotNull final CompoundTag nbt) {
        super.addAdditionalSaveData(nbt);
        nbt.putBoolean("InGround", this.entityData.get(InGROUND));
        nbt.putInt("ReturnToGroundTicks", this.ReturnToGround_Ticks);
        nbt.putBoolean("Invisible",this.entityData.get(INVISIBLE));

        if (this.ownerUuid != null) {
            nbt.putUUID("Owner", this.ownerUuid);
        }
    }
    @Override
    public void readAdditionalSaveData(final CompoundTag nbt) {
        this.setInGround(nbt.getBoolean("InGround"));
        this.ReturnToGround_Ticks = nbt.getInt("ReturnToGroundTicks");
        this.setInvisibleData(nbt.getBoolean("Invisible"));

        if (nbt.hasUUID("Owner")) {
            this.ownerUuid = nbt.getUUID("Owner");
        }
        super.readAdditionalSaveData(nbt);
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
        //Triggers the blood explosion
        if (this.getIsTriggerExplosion()) {
            this.explode();
        }

        //Counter for Lunge attack
        this.tickLunge();

        //Counter for particles
        this.tickExcavator(this);

        super.tick();
    }

    @Override
    public void customServerAiStep(){
        if(this.getReturnToGround_Ticks() < 200 && this.getOwner() == null) {
            mobTickExcavator(
                    List.of(BlockTags.DIRT, BlockTags.STONE_ORE_REPLACEABLES, BlockTags.DEEPSLATE_ORE_REPLACEABLES),
                    List.of(Blocks.SAND),
                    this
            );
        }

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

    protected void explode() {
        if (!this.level().isClientSide) {
            this.dead = true;
            this.level().explode(this, null, null,
                    this.getX(), this.getY(), this.getZ(), (float)3, false, Level.ExplosionInteraction.MOB,
                    TCOTS_Particles.RotfiendBloodEmitter(), TCOTS_Particles.RotfiendBloodEmitter(), TCOTS_Sounds.getRotfiendBloodExplosion());
            this.discard();
        }
    }

    @Override
    public void die(@NotNull final DamageSource damageSource) {
        super.die(damageSource);

        if(this.isOnFire() && damageSource.getDirectEntity()!=null && damageSource.getDirectEntity() instanceof final Player player){
            if(player instanceof final ServerPlayer serverPlayer){
                TCOTS_Criteria.KillRotfiend().trigger(serverPlayer);
            }
        }
    }

    public int ticksSinceDeath=60;
    @Override
    protected void tickDeath() {
        if(!this.isOnFire()) {
            if (ticksSinceDeath == 60) {
                this.playSound(this.getExplosionSound(), 1.0F, 1.0F);
                this.triggerAnim("base_controller","explosion");
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
    public boolean shouldBlockExplode(@NotNull final Explosion explosion, @NotNull final BlockGetter world, @NotNull final BlockPos pos, @NotNull final BlockState state, final float explosionPower) {
        return false;
    }



    //Sounds
    @Override
    protected SoundEvent getAmbientSound() {
        if (!this.getIsExploding() && !this.getInGround()) {
            return getIdleSound();
        } else {
            return null;
        }
    }

    @Override
    protected @NotNull SoundEvent getHurtSound(@NotNull final DamageSource source) {
        return TCOTS_Sounds.getSoundEvent("rotfiend_hurt");
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        if (!this.isOnFire()) {
            return null;
        } else {
            return getDeathSound(this);
        }
    }

    protected SoundEvent getDeathSound(final RotfiendEntity rotfiend) {
        return TCOTS_Sounds.getSoundEvent("rotfiend_death");
    }


    protected SoundEvent getIdleSound() {
        return TCOTS_Sounds.getSoundEvent("rotfiend_idle");
    }

    @Override
    public SoundEvent getLungeSound() {
        return TCOTS_Sounds.getSoundEvent("rotfiend_lunge");
    }

    public SoundEvent getExplosionSound(){
        return TCOTS_Sounds.getSoundEvent("rotfiend_exploding");
    }

    //Attack Sound
    @Override
    protected SoundEvent getAttackSound() {
        return TCOTS_Sounds.getSoundEvent("rotfiend_attack");
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

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
