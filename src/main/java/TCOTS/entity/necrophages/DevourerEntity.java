package TCOTS.entity.necrophages;

import TCOTS.entity.goals.*;
import TCOTS.entity.interfaces.ExcavatorMob;
import TCOTS.entity.misc.CommonControllers;
import TCOTS.sounds.TCOTS_Sounds;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;

import java.util.List;

public class DevourerEntity extends Necrophage_Base implements GeoEntity, ExcavatorMob {

    //TODO: Make it special?
    //xTODO: Add natural spawn
    //xTODO: Add drop


    private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    protected static final TrackedData<Boolean> InGROUND = DataTracker.registerData(DevourerEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    protected static final TrackedData<Boolean> EMERGING = DataTracker.registerData(DevourerEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    protected static final TrackedData<Boolean> INVISIBLE = DataTracker.registerData(DevourerEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    public DevourerEntity(EntityType<? extends PathAwareEntity> entityType, World world) {
        super(entityType, world);
        experiencePoints=10;
    }

    public static DefaultAttributeContainer.Builder setAttributes() {
        return AnimalEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 30.0D)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 5.0f) //Amount of health that hurts you
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.22f)

                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 0.5);
    }

    @Override
    protected void initGoals() {
        //Attack
        //Emerge from ground
        this.goalSelector.add(0, new EmergeFromGroundGoal_Excavator(this, 500));
        this.goalSelector.add(1, new SwimGoal(this));


        //Returns to ground
        this.goalSelector.add(2, new ReturnToGroundGoal_Excavator(this));

        this.goalSelector.add(3, new MeleeAttackGoal_Excavator(this, 1.2D, false, 3600));

        this.goalSelector.add(4, new WanderAroundGoal_Excavator(this, 0.75f, 20));

        this.goalSelector.add(5, new LookAroundGoal_Excavator(this));

        //Objectives
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, PlayerEntity.class, true));
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, MerchantEntity.class, true));
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, IronGolemEntity.class, true));
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(InGROUND, Boolean.FALSE);
        this.dataTracker.startTracking(EMERGING, Boolean.FALSE);
        this.dataTracker.startTracking(INVISIBLE, Boolean.FALSE);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {

        //Walk/Idle Controller
        controllerRegistrar.add(new AnimationController<>(this, "Idle/Walk", 5,
                state -> CommonControllers.idleWalkRun(state, this, RUNNING, WALKING, IDLE)
        ));

        //Attack Controller
        controllerRegistrar.add(
                new AnimationController<>(this, "AttackController", 1, state -> CommonControllers.animationThreeAttacksPredicate(state, this.handSwinging, random))
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
    public void tick() {
        //Counter for particles
        this.tickExcavator(this);
        super.tick();
    }

    @Override
    protected void mobTick() {
        mobTickExcavator(
                List.of(BlockTags.DIRT, BlockTags.STONE_ORE_REPLACEABLES, BlockTags.DEEPSLATE_ORE_REPLACEABLES),
                List.of(Blocks.SAND),
                this
        );


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

    //Sounds
    @Override
    protected SoundEvent getAmbientSound() {
        if (!this.getInGroundDataTracker()) {
            return TCOTS_Sounds.DEVOURER_IDLE;
        } else {
            return null;
        }
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return TCOTS_Sounds.DEVOURER_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return TCOTS_Sounds.DEVOURER_DEATH;
    }

    @Override
    public boolean isInvulnerableTo(DamageSource damageSource) {
        return this.getIsEmerging() || this.getInGroundDataTracker() || super.isInvulnerableTo(damageSource);
    }

    @Override
    public boolean isPushable() {
        return !this.getIsEmerging() && !this.getInGroundDataTracker();
    }

    @Override
    protected SoundEvent getAttackSound() {
        return TCOTS_Sounds.DEVOURER_ATTACK;
    }

    int AnimationParticlesTicks=36;

    @Override
    public int getAnimationParticlesTicks() {
        return AnimationParticlesTicks;
    }

    @Override
    public void setAnimationParticlesTicks(int animationParticlesTicks) {
        AnimationParticlesTicks=animationParticlesTicks;
    }

    @Override
    public boolean getInGroundDataTracker() {
        return this.dataTracker.get(InGROUND);
    }

    @Override
    public void setInGroundDataTracker(boolean wasInGround) {
        this.dataTracker.set(InGROUND, wasInGround);
    }

    @Override
    public boolean getIsEmerging() {
        return this.dataTracker.get(EMERGING);
    }

    @Override
    public void setIsEmerging(boolean wasEmerging) {
        this.dataTracker.set(EMERGING, wasEmerging);
    }
    public int ReturnToGround_Ticks=20;
    @Override
    public int getReturnToGround_Ticks() {
        return ReturnToGround_Ticks;
    }

    @Override
    public void setReturnToGround_Ticks(int returnToGround_Ticks) {
        ReturnToGround_Ticks=returnToGround_Ticks;
    }

    @Override
    public boolean getInvisibleData() {
        return this.dataTracker.get(INVISIBLE);
    }

    @Override
    public void setInvisibleData(boolean isInvisible) {
        this.dataTracker.set(INVISIBLE, isInvisible);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
