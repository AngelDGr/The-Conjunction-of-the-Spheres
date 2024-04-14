package TCOTS.entity.necrophages;

import TCOTS.entity.goals.*;
import TCOTS.entity.interfaces.ExcavatorMob;
import TCOTS.entity.interfaces.LungeMob;
import TCOTS.entity.misc.CommonControllers;
import TCOTS.particles.TCOTS_Particles;
import TCOTS.sounds.TCOTS_Sounds;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.SwimGoal;
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
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

import java.util.EnumSet;
import java.util.List;

public class RotfiendEntity extends Necrophage_Base implements GeoEntity, ExcavatorMob, LungeMob {

    //xTODO: Add Emerging animation (And digging?)
    //xTODO: Add drops
    //xTODO: Add spawning
    //xTODO: Fix the invisible bug

    private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    public static final RawAnimation IDLE = RawAnimation.begin().thenLoop("idle");
    public static final RawAnimation RUNNING = RawAnimation.begin().thenLoop("move.running");
    public static final RawAnimation WALKING = RawAnimation.begin().thenLoop("move.walking");

    public static final RawAnimation EXPLOSION = RawAnimation.begin().thenPlayAndHold("special.explosion");

    protected static final TrackedData<Boolean> LUGGING = DataTracker.registerData(RotfiendEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    protected static final TrackedData<Boolean> EXPLODING = DataTracker.registerData(RotfiendEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    protected static final TrackedData<Boolean> TRIGGER_EXPLOSION = DataTracker.registerData(RotfiendEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    protected static final TrackedData<Boolean> InGROUND = DataTracker.registerData(RotfiendEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    protected static final TrackedData<Boolean> EMERGING = DataTracker.registerData(RotfiendEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    protected static final TrackedData<Boolean> INVISIBLE = DataTracker.registerData(RotfiendEntity.class, TrackedDataHandlerRegistry.BOOLEAN);


    public RotfiendEntity(EntityType<? extends RotfiendEntity> entityType, World world) {
        super(entityType, world);
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


        this.goalSelector.add(5, new WanderAroundGoal_Excavator(this, 0.75f, 20));

        this.goalSelector.add(6, new LookAroundGoal_Excavator(this));

        //Objectives
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, PlayerEntity.class, true));
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, MerchantEntity.class, true));
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, IronGolemEntity.class, true));
    }

    private final SoundEvent explosionSound = TCOTS_Sounds.ROTFIEND_EXPLODING;

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
            this.rotfiend.playSound(this.rotfiend.explosionSound, 1.0F, 1.0F);
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
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.28f);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        //Walk/Idle Controller
        controllerRegistrar.add(new AnimationController<>(this, "Idle/Walk", 5, state -> {

            //If it's aggressive and it is moving
            if (this.isAttacking() && state.isMoving()) {
                return state.setAndContinue(RUNNING);
            }
            //It's not attacking and/or it's no moving
            else {
                //If it's attacking but NO moving
                if (isAttacking()) {
                    return state.setAndContinue(RUNNING);
                } else {
                    //If it's just moving
                    if (state.isMoving()) {
                        return state.setAndContinue(WALKING);
                    }
                    //Anything else
                    else {
                        return state.setAndContinue(IDLE);
                    }
                }
            }

        }
        ));

        //Attack Controller
        controllerRegistrar.add(
                new AnimationController<>(this, "AttackController", 1, state -> CommonControllers.animationThreeAttacksPredicate(state, this.handSwinging,random))
        );

        //Lunge Controller
        lungeAnimationController(this, controllerRegistrar);

        //Explosion Controller
        controllerRegistrar.add(
                new AnimationController<>(this, "ExplosionController", 1, state -> {
                    if (this.getIsExploding()) {
                        state.setAnimation(EXPLOSION);
                        return PlayState.CONTINUE;
                    }

                    state.getController().forceAnimationReset();
                    return PlayState.CONTINUE;
                })
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
        this.dataTracker.startTracking(LUGGING, Boolean.FALSE);
        this.dataTracker.startTracking(EXPLODING, Boolean.FALSE);
        this.dataTracker.startTracking(TRIGGER_EXPLOSION, Boolean.FALSE);
        this.dataTracker.startTracking(InGROUND, Boolean.FALSE);
        this.dataTracker.startTracking(EMERGING, Boolean.FALSE);
        this.dataTracker.startTracking(INVISIBLE, Boolean.FALSE);
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

    public boolean getInGroundDataTracker() {
        return this.dataTracker.get(InGROUND);
    }
    public void setInGroundDataTracker(boolean wasInGround) {
        this.dataTracker.set(InGROUND, wasInGround);
    }

    public final boolean getInvisibleData() {
        return this.dataTracker.get(INVISIBLE);
    }
    public final void setInvisibleData(boolean isInvisible) {
        this.dataTracker.set(INVISIBLE, isInvisible);
    }
    @Override
    public void onTrackedDataSet(TrackedData<?> data) {
        super.onTrackedDataSet(data);
        if (!dataTracker.get(InGROUND) || dataTracker.get(InGROUND)) {
            this.setBoundingBox(this.calculateBoundingBox());
        }
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putBoolean("InGround", this.dataTracker.get(InGROUND));
        nbt.putInt("ReturnToGroundTicks", this.ReturnToGround_Ticks);
        nbt.putBoolean("Invisible",this.dataTracker.get(INVISIBLE));
    }
    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        this.setInGroundDataTracker(nbt.getBoolean("InGround"));
        this.ReturnToGround_Ticks = nbt.getInt("ReturnToGroundTicks");
        this.setInvisibleData(nbt.getBoolean("Invisible"));
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
        this.tickExcavator();

        super.tick();
    }

    @Override
    public void mobTick(){
        if(this.getReturnToGround_Ticks() < 200) {
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
        if (dataTracker.get(InGROUND)) {
            return groundBox(this);
        }
        else{
            // Normal hit-box otherwise
            return super.calculateBoundingBox();
        }
    }

    private void explode() {
        if (!this.getWorld().isClient) {
            this.dead = true;
            this.getWorld().createExplosion(this, null, null,
                    this.getX(), this.getY(), this.getZ(), (float)3, false, World.ExplosionSourceType.MOB,
                    TCOTS_Particles.ROTFIEND_BLOOD_EMITTER, TCOTS_Particles.ROTFIEND_BLOOD_EMITTER, TCOTS_Sounds.ROTFIEND_BLOOD_EXPLOSION);
            this.discard();
        }
    }

    public int ticksSinceDeath=60;
    @Override
    protected void updatePostDeath() {
        if(!this.isOnFire()) {
            if (ticksSinceDeath == 60) {
                this.playSound(this.explosionSound, 1.0F, 1.0F);
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

    public void spawnGroundParticles() {
        BlockState blockState = this.getSteppingBlockState();
        if (blockState.getRenderType() != BlockRenderType.INVISIBLE) {
            for (int i = 0; i < 11; ++i) {
                double d = this.getX() + (double) MathHelper.nextBetween(random, -0.7F, 0.7F);
                double e = this.getY();
                double f = this.getZ() + (double) MathHelper.nextBetween(random, -0.7F, 0.7F);

                this.getWorld().addParticle(new BlockStateParticleEffect(ParticleTypes.BLOCK, blockState), d, e, f, 0.0, 0.0, 0.0);
            }
        }
    }


    @Override
    public boolean canExplosionDestroyBlock(Explosion explosion, BlockView world, BlockPos pos, BlockState state, float explosionPower) {
        return false;
    }

    //Sounds
    @Override
    protected SoundEvent getAmbientSound() {
        if (!this.getIsExploding() && !this.getInGroundDataTracker()) {
            return TCOTS_Sounds.ROTFIEND_IDLE;
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
            return TCOTS_Sounds.ROTFIEND_DEATH;
        }
    }

    @Override
    public SoundEvent getEmergingSound() {
        return TCOTS_Sounds.ROTFIEND_EMERGING;
    }

    @Override
    public SoundEvent getDiggingSound() {
        return TCOTS_Sounds.ROTFIEND_DIGGING;
    }

    @Override
    public SoundEvent getLungeSound() {
        return TCOTS_Sounds.ROTFIEND_LUNGE;
    }

    //Attack Sound
    @Override
    protected SoundEvent getAttackSound() {
        return TCOTS_Sounds.ROTFIEND_ATTACK;
    }

    @Override
    public boolean isInvulnerableTo(DamageSource damageSource) {
        return this.getIsEmerging() || this.getInGroundDataTracker() || this.getIsExploding() || super.isInvulnerableTo(damageSource);
    }

    @Override
    public boolean isPushable() {
        return !this.getIsExploding() && !this.getIsEmerging() && !this.getInGroundDataTracker();
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
