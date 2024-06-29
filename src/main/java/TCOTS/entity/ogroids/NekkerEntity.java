package TCOTS.entity.ogroids;

import TCOTS.entity.goals.*;
import TCOTS.entity.interfaces.ExcavatorMob;
import TCOTS.entity.interfaces.LungeMob;
import TCOTS.utils.GeoControllersUtil;
import TCOTS.sounds.TCOTS_Sounds;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityStatuses;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.RevengeGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.HostileEntity;
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
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Difficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.object.PlayState;

import java.util.List;


public class NekkerEntity extends OgroidMonster implements GeoEntity, ExcavatorMob, LungeMob {

    //xTODO: Add spawn
    //xTODO: Add drops

    private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    protected static final TrackedData<Boolean> InGROUND = DataTracker.registerData(NekkerEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    protected static final TrackedData<Boolean> EMERGING = DataTracker.registerData(NekkerEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    protected static final TrackedData<Boolean> INVISIBLE = DataTracker.registerData(NekkerEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    public NekkerEntity(EntityType<? extends NekkerEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    public int getMaxHeadRotation() {
        return 40;
    }

    @Override
    protected void initGoals() {

        //Emerge from ground
        this.goalSelector.add(0, new EmergeFromGroundGoal_Excavator(this, 500));
        this.goalSelector.add(1, new SwimGoal(this));

        this.goalSelector.add(2, new LungeAttackGoal(this, 150, 1.8, 5, 40));

        //Returns to ground
        this.goalSelector.add(3, new ReturnToGroundGoal_Excavator(this));

        //Attack
        this.goalSelector.add(4, new MeleeAttackGoal_Excavator(this, 1.2D, false, 2400));


        this.goalSelector.add(5, new WanderAroundGoal_Excavator(this, 0.75f, 20));

        this.goalSelector.add(6, new LookAroundGoal_Excavator(this));

        //Objectives
        this.targetSelector.add(0, new RevengeGoal(this, NekkerEntity.class).setGroupRevenge());
        this.targetSelector.add(1, new ActiveTargetGoal<>(this, PlayerEntity.class, true));
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, MerchantEntity.class, true));
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, IronGolemEntity.class, true));
    }

    public boolean cooldownBetweenLunges = false;

    public boolean getNotCooldownBetweenLunges() {
        return !cooldownBetweenLunges;
    }

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

    public int ReturnToGround_Ticks = 20;

    public int getReturnToGround_Ticks() {
        return ReturnToGround_Ticks;
    }

    public void setReturnToGround_Ticks(int returnToGround_Ticks) {
        ReturnToGround_Ticks = returnToGround_Ticks;
    }

    public static DefaultAttributeContainer.Builder setAttributes() {
        return AnimalEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 14.0D)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 2.0f) //Amount of health that hurts you
                .add(EntityAttributes.GENERIC_ATTACK_SPEED, 0.5f)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.28f)

                .add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK, 0.2f)
                .add(EntityAttributes.GENERIC_ARMOR, 2f);
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

        //Lunge Controller // With 0 tick transition, so it can spin
        controllerRegistrar.add(
                new AnimationController<>(this, "LungeController", 0, state -> PlayState.STOP)
                        .triggerableAnim("lunge", getLungeAnimation())
        );

        //Digging Controller
        controllerRegistrar.add(
                new AnimationController<>(this, "DiggingController", 1, this::animationDiggingPredicate)
        );

        //Emerging Controller
        controllerRegistrar.add(
                new AnimationController<>(this, "EmergingController", 1, this::animationEmergingPredicate)
        );
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(InGROUND, Boolean.FALSE);
        this.dataTracker.startTracking(EMERGING, Boolean.FALSE);
        this.dataTracker.startTracking(INVISIBLE, Boolean.FALSE);
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
        nbt.putBoolean("Invisible", this.dataTracker.get(INVISIBLE));
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        this.setInGroundDataTracker(nbt.getBoolean("InGround"));
        this.ReturnToGround_Ticks = nbt.getInt("ReturnToGroundTicks");
        this.setInvisibleData(nbt.getBoolean("Invisible"));
        super.readCustomDataFromNbt(nbt);
    }

    @Override
    protected Box calculateBoundingBox() {
        if (dataTracker.get(InGROUND)) {
            return groundBox(this);
        } else {
            // Normal hit-box otherwise
            return super.calculateBoundingBox();
        }
    }

    public final boolean getIsEmerging() {
        return this.dataTracker.get(EMERGING);
    }

    public final void setIsEmerging(boolean wasEmerging) {
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

    int AnimationParticlesTicks = 36;

    public int getAnimationParticlesTicks() {
        return AnimationParticlesTicks;
    }

    public void setAnimationParticlesTicks(int animationParticlesTicks) {
        AnimationParticlesTicks = animationParticlesTicks;
    }

    @Override
    public void tick() {
        //Counter for Lunge attack
        this.tickLunge();
        //Counter for particles
        this.tickExcavator(this);

        super.tick();
    }

    @Override
    public void mobTick() {
        mobTickExcavator(
                List.of(BlockTags.DIRT),
                List.of(Blocks.SAND),
                this
        );

        this.setInvisible(this.getInvisibleData());
        super.mobTick();
    }

    //Sounds
    @Override
    protected SoundEvent getAmbientSound() {
        if (!this.getInGroundDataTracker()) {
            return TCOTS_Sounds.NEKKER_IDLE;
        } else {
            return null;
        }
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return TCOTS_Sounds.NEKKER_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return TCOTS_Sounds.NEKKER_DEATH;
    }

    @Override
    public SoundEvent getLungeSound() {
        return TCOTS_Sounds.NEKKER_LUNGE;
    }

    @Override
    public boolean isInvulnerableTo(DamageSource damageSource) {
        return this.getIsEmerging() || this.getInGroundDataTracker() || super.isInvulnerableTo(damageSource);
    }

    @Override
    public boolean isPushable() {
        return !this.getIsEmerging() && !this.getInGroundDataTracker();
    }

    //Attack Sound
    @Override
    protected SoundEvent getAttackSound() {
        return TCOTS_Sounds.NEKKER_ATTACK;
    }

    public void playSpawnEffects() {
        if (this.getWorld().isClient) {
            BlockState blockState = this.getSteppingBlockState();
            if (blockState.getRenderType() != BlockRenderType.INVISIBLE) {

                for (int i = 0; i < 40; ++i) {
                    double d = this.getX() + (double) MathHelper.nextBetween(random, -0.7F, 0.7F);
                    double e = this.getY()+0.5;
                    double f = this.getZ() + (double) MathHelper.nextBetween(random, -0.7F, 0.7F);

                    this.getWorld().addParticle(new BlockStateParticleEffect(ParticleTypes.BLOCK, blockState), d, e, f, 0.0, 0.0, 0.0);
                }
            }
        } else {
            this.getWorld().sendEntityStatus(this, EntityStatuses.PLAY_SPAWN_EFFECTS);
        }
    }


    public static boolean canSpawnNekker(EntityType<? extends OgroidMonster> type, ServerWorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
        if (world.getDifficulty() != Difficulty.PEACEFUL) {
            if (spawnReason.equals(SpawnReason.SPAWNER)) {
                return true;
            }
            return HostileEntity.isSpawnDark(world, pos, random) && HostileEntity.canMobSpawn(type, world, spawnReason, pos, random);
        }
        return false;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
