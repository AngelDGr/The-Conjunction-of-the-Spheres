package TCOTS.entity.necrophages;

import TCOTS.advancements.TCOTS_Criteria;
import TCOTS.entity.goals.*;
import TCOTS.entity.interfaces.ExcavatorMob;
import TCOTS.entity.interfaces.LungeMob;
import TCOTS.particles.TCOTS_Particles;
import TCOTS.sounds.TCOTS_Sounds;
import TCOTS.utils.GeoControllersUtil;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.Goal;
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
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

import java.util.EnumSet;
import java.util.List;
import java.util.UUID;

public class RotfiendEntity extends NecrophageMonster implements GeoEntity, ExcavatorMob, LungeMob, Ownable {

    //xTODO: Add Emerging animation (And digging?)
    //xTODO: Add drops
    //xTODO: Add spawning
    //xTODO: Fix the invisible bug
    //xTODO: Add follow leader goal

    @Nullable
    private MobEntity owner;
    @Nullable
    private UUID ownerUuid;
    private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    public static final RawAnimation EXPLOSION = RawAnimation.begin().thenPlayAndHold("special.explosion");

    protected static final TrackedData<Boolean> EXPLODING = DataTracker.registerData(RotfiendEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    protected static final TrackedData<Boolean> TRIGGER_EXPLOSION = DataTracker.registerData(RotfiendEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    protected static final TrackedData<Boolean> InGROUND = DataTracker.registerData(RotfiendEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    protected static final TrackedData<Boolean> EMERGING = DataTracker.registerData(RotfiendEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    protected static final TrackedData<Boolean> INVISIBLE = DataTracker.registerData(RotfiendEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    protected static final TrackedData<Boolean> BULLVORE_GROUP = DataTracker.registerData(RotfiendEntity.class, TrackedDataHandlerRegistry.BOOLEAN);


    public RotfiendEntity(EntityType<? extends RotfiendEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public int getMaxHeadRotation() {
        return 45;
    }

    @Override
    protected void initGoals() {
        //Attack
        //Emerge from ground
        this.goalSelector.add(0, new Rotfiend_Explosion(this, 0.25f));
        this.goalSelector.add(0, new EmergeFromGroundGoal_Excavator(this, 500));
        this.goalSelector.add(1, new SwimGoal(this));


        this.goalSelector.add(2, new LungeAttackGoal(this, 100, 0.6,5,25));

        //Returns to ground
        this.goalSelector.add(3, new ReturnToGroundGoal_Excavator(this));

        this.goalSelector.add(4, new MeleeAttackGoal_Excavator(this, 1.2D, false));

        this.goalSelector.add(5, new FollowMonsterOwnerGoal(this, 0.75));

        this.goalSelector.add(6, new WanderAroundGoal_Excavator(this, 0.75f, 100));

        this.goalSelector.add(7, new LookAroundGoal_Excavator(this));

        //Objectives
        this.targetSelector.add(0, new AttackOwnerAttackerTarget(this));
        this.targetSelector.add(1, new AttackOwnerEnemyTarget(this));
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, PlayerEntity.class, true));
        this.targetSelector.add(3, new ActiveTargetGoal<>(this, MerchantEntity.class, true));
        this.targetSelector.add(4, new ActiveTargetGoal<>(this, IronGolemEntity.class, true));
    }


    public boolean cooldownBetweenLunges = false;
    @Override
    public boolean getNotCooldownBetweenLunges() {
        return !cooldownBetweenLunges;
    }

    @Override
    public void setCooldownBetweenLunges(boolean cooldownBetweenLunges) {
        this.cooldownBetweenLunges = cooldownBetweenLunges;
    }
    public int LungeTicks;
    @Override
    public int getLungeTicks() {
        return LungeTicks;
    }


    @Override
    public void setLungeTicks(int lungeTicks) {
        LungeTicks = lungeTicks;
    }

    @Nullable
    @Override
    public Entity getOwner() {
        Entity entity;
        if (this.owner == null && this.ownerUuid != null && this.getWorld() instanceof ServerWorld && (entity = ((ServerWorld)this.getWorld()).getEntity(this.ownerUuid)) instanceof LivingEntity) {
            this.owner = (MobEntity) entity;
        }
        return this.owner;
    }

    public void setOwner(@Nullable MobEntity owner) {
        this.owner = owner;
        this.ownerUuid = owner == null ? null : owner.getUuid();
    }

    private class Rotfiend_Explosion extends Goal {
        int AnimationTicks = 30;

        final float percentageHealth;

        private final RotfiendEntity rotfiend;

        private Rotfiend_Explosion(RotfiendEntity mob, float percentageHealth) {
            this.rotfiend = mob;
            this.percentageHealth=percentageHealth;
            this.setControls(EnumSet.of(Goal.Control.MOVE));
        }

        @Override
        public boolean canStart() {
            return (RotfiendEntity.this.getHealth() < (RotfiendEntity.this.getMaxHealth() * percentageHealth) && !rotfiend.isOnFire());
        }

        @Override
        public void start() {
            this.rotfiend.playSound(this.rotfiend.getExplosionSound(), 1.0F, 1.0F);
            this.rotfiend.triggerAnim("ExplosionController", "explosion");
            this.rotfiend.setIsExploding(true);
            rotfiend.getNavigation().stop();
            rotfiend.getLookControl().lookAt(0, 0, 0);
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

    public void setReturnToGround_Ticks(int returnToGround_Ticks) {
        ReturnToGround_Ticks = returnToGround_Ticks;
    }

    public static DefaultAttributeContainer.Builder setAttributes() {
        return AnimalEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 20.0D)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 3.0f) //Amount of health that hurts you
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.27f);
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
                        .triggerableAnim("attack3", GeoControllersUtil.ATTACK3)
        );

        //Lunge Controller
        lungeAnimationController(this, controllerRegistrar);

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

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(EXPLODING, Boolean.FALSE);
        this.dataTracker.startTracking(TRIGGER_EXPLOSION, Boolean.FALSE);
        this.dataTracker.startTracking(InGROUND, Boolean.FALSE);
        this.dataTracker.startTracking(EMERGING, Boolean.FALSE);
        this.dataTracker.startTracking(INVISIBLE, Boolean.FALSE);

        this.dataTracker.startTracking(BULLVORE_GROUP, Boolean.FALSE);
    }

    public final boolean getIsExploding() {
        return this.dataTracker.get(EXPLODING);
    }

    public final void setIsExploding(boolean wasExploding) {
        this.dataTracker.set(EXPLODING, wasExploding);
    }

    public final boolean getIsTriggerExplosion() {
        return this.dataTracker.get(TRIGGER_EXPLOSION);
    }
    public final void setIsTriggerExplosion(boolean wasExplosion) {
        this.dataTracker.set(TRIGGER_EXPLOSION, wasExplosion);
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

    public final boolean getInvisibleData() {
        return this.dataTracker.get(INVISIBLE);
    }
    public final void setInvisibleData(boolean isInvisible) {
        this.dataTracker.set(INVISIBLE, isInvisible);
    }
    @Override
    protected float getActiveEyeHeight(EntityPose pose, EntityDimensions dimensions) {
        if(this.getInGround()){
            return 0.1f;
        } else {
            return super.getActiveEyeHeight(pose, dimensions);
        }
    }
    @Override
    public void onTrackedDataSet(TrackedData<?> data) {
        super.onTrackedDataSet(data);
        if (!this.getInGround() || this.getInGround()) {
            this.setBoundingBox(this.calculateBoundingBox());
            this.calculateDimensions();
        }
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putBoolean("InGround", this.dataTracker.get(InGROUND));
        nbt.putInt("ReturnToGroundTicks", this.ReturnToGround_Ticks);
        nbt.putBoolean("Invisible",this.dataTracker.get(INVISIBLE));

        if (this.ownerUuid != null) {
            nbt.putUuid("Owner", this.ownerUuid);
        }
    }
    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        this.setInGround(nbt.getBoolean("InGround"));
        this.ReturnToGround_Ticks = nbt.getInt("ReturnToGroundTicks");
        this.setInvisibleData(nbt.getBoolean("Invisible"));

        if (nbt.containsUuid("Owner")) {
            this.ownerUuid = nbt.getUuid("Owner");
        }
        super.readCustomDataFromNbt(nbt);
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
    public void mobTick(){
        if(this.getReturnToGround_Ticks() < 200 && this.getOwner() == null) {
            mobTickExcavator(
                    List.of(BlockTags.DIRT, BlockTags.STONE_ORE_REPLACEABLES, BlockTags.DEEPSLATE_ORE_REPLACEABLES),
                    List.of(Blocks.SAND),
                    this
            );
        }

        this.setInvisible(this.getInvisibleData());
        super.mobTick();
    }

    @Override
    protected Box calculateBoundingBox() {
        if (getInGround()) {
            return groundBox(this);
        }
        else{
            // Normal hit-box otherwise
            return super.calculateBoundingBox();
        }
    }

    protected void explode() {
        if (!this.getWorld().isClient) {
            this.dead = true;
            this.getWorld().createExplosion(this, null, null,
                    this.getX(), this.getY(), this.getZ(), (float)3, false, World.ExplosionSourceType.MOB,
                    TCOTS_Particles.ROTFIEND_BLOOD_EMITTER, TCOTS_Particles.ROTFIEND_BLOOD_EMITTER, TCOTS_Sounds.ROTFIEND_BLOOD_EXPLOSION);
            this.discard();
        }
    }

    @Override
    public void onDeath(DamageSource damageSource) {
        super.onDeath(damageSource);

        if(this.isOnFire() && damageSource.getSource()!=null && damageSource.getSource() instanceof PlayerEntity player){
            if(player instanceof ServerPlayerEntity serverPlayer){
                TCOTS_Criteria.KILL_ROTFIEND.trigger(serverPlayer);
            }
        }
    }

    public int ticksSinceDeath=60;
    @Override
    protected void updatePostDeath() {
        if(!this.isOnFire()) {
            if (ticksSinceDeath == 60) {
                this.playSound(this.getExplosionSound(), 1.0F, 1.0F);
                this.triggerAnim("ExplosionController","explosion");
                this.setIsExploding(true);
                this.getNavigation().stop();
                this.getLookControl().lookAt(0, 0, 0);
            }
            if (ticksSinceDeath > 0) {
                --this.ticksSinceDeath;
            } else {
                this.setIsTriggerExplosion(true);
            }
        }
        else {
            super.updatePostDeath();
        }
    }


    @Override
    public boolean canExplosionDestroyBlock(Explosion explosion, BlockView world, BlockPos pos, BlockState state, float explosionPower) {
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
    protected SoundEvent getHurtSound(DamageSource source) {
        return TCOTS_Sounds.ROTFIEND_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        if (!this.isOnFire()) {
            return null;
        } else {
            return getDeathSound(this);
        }
    }

    protected SoundEvent getDeathSound(RotfiendEntity rotfiend) {
        return TCOTS_Sounds.ROTFIEND_DEATH;
    }


    protected SoundEvent getIdleSound() {
        return TCOTS_Sounds.ROTFIEND_IDLE;
    }

    @Override
    public SoundEvent getLungeSound() {
        return TCOTS_Sounds.ROTFIEND_LUNGE;
    }

    public SoundEvent getExplosionSound(){
        return TCOTS_Sounds.ROTFIEND_EXPLODING;
    }

    //Attack Sound
    @Override
    protected SoundEvent getAttackSound() {
        return TCOTS_Sounds.ROTFIEND_ATTACK;
    }

    @Override
    public boolean isInvulnerableTo(DamageSource damageSource) {
        return this.getIsEmerging() || this.getInGround() || this.getIsExploding() || super.isInvulnerableTo(damageSource);
    }

    @Override
    public boolean isPushable() {
        return super.isPushable() && !this.getIsExploding() && !this.getIsEmerging() && !this.getInGround();
    }

    @Override
    public boolean isFireImmune() {
        return super.isFireImmune() || this.getInGround();
    }

    @Override
    protected void dropLoot(DamageSource damageSource, boolean causedByPlayer) {
        if(this.isOnFire()){
            super.dropLoot(damageSource, causedByPlayer);
        }
    }

    @Override
    public int getXpToDrop() {
        if(this.isOnFire()){
            return super.getXpToDrop();
        }
        return 0;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
