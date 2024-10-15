package TCOTS.entity.necrophages;

import TCOTS.entity.goals.*;
import TCOTS.entity.interfaces.ExcavatorMob;
import TCOTS.items.concoctions.TCOTS_Effects;
import TCOTS.sounds.TCOTS_Sounds;
import TCOTS.utils.GeoControllersUtil;
import net.minecraft.block.Blocks;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.RevengeGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
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

public class GraveirEntity extends NecrophageMonster implements GeoEntity, ExcavatorMob {
    //xTODO: Add sounds
    //xTODO: Add spawn
    //xTODO: Add bestiary
    //xTODO: Add drop
    //xTODO: Add new attack

    private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

//    public static final byte FALLING_PARTICLES = 42;
//    public static final RawAnimation GROUND_PUNCH = RawAnimation.begin().thenPlay("special.ground_punch");


    protected static final TrackedData<Boolean> InGROUND = DataTracker.registerData(GraveirEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    protected static final TrackedData<Boolean> EMERGING = DataTracker.registerData(GraveirEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    protected static final TrackedData<Boolean> INVISIBLE = DataTracker.registerData(GraveirEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
//    protected static final TrackedData<Boolean> PUNCHING = DataTracker.registerData(GraveirEntity.class, TrackedDataHandlerRegistry.BOOLEAN);


    public GraveirEntity(EntityType<? extends GraveirEntity> entityType, World world) {
        super(entityType, world);
        experiencePoints=20;
    }

    @Override
    public int getMaxHeadRotation() {
        return 35;
    }

    public static DefaultAttributeContainer.Builder setAttributes() {
        return HostileEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 50.0D)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 9.0f) //Amount of health that hurts you
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.20f)

                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 0.75)
                .add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK,1.5)

                .add(EntityAttributes.GENERIC_ARMOR, 8f)
                .add(EntityAttributes.GENERIC_ARMOR_TOUGHNESS, 8f);
    }


    @Override
    protected void initGoals() {
        //Emerge from ground
        this.goalSelector.add(0, new EmergeFromGroundGoal_Excavator(this, 600));
        this.goalSelector.add(1, new SwimGoal(this));

        //Returns to ground
        this.goalSelector.add(2, new ReturnToGroundGoal_Excavator(this));

        this.goalSelector.add(3, new MeleeAttackGoal_Excavator(this, 1.2D, false, 3600));

        this.goalSelector.add(4, new WanderAroundGoal_Excavator(this, 0.75f, 20));

        this.goalSelector.add(5, new LookAroundGoal_Excavator(this));

        //Objectives
        this.targetSelector.add(0, new RevengeGoal(this, GraveirEntity.class));
        this.targetSelector.add(1, new ActiveTargetGoal<>(this, PlayerEntity.class, true));
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
        controllerRegistrar.add(new AnimationController<>(this, "Idle/Walk", 5, GeoControllersUtil::idleWalkRunController)
        );

        //Attack Controller
        controllerRegistrar.add(
                new AnimationController<>(this, "AttackController", 2, state -> PlayState.STOP)
                        .triggerableAnim("attack1", GeoControllersUtil.ATTACK1)
                        .triggerableAnim("attack2", GeoControllersUtil.ATTACK2)
                        .triggerableAnim("attack3", GeoControllersUtil.ATTACK3)
        );

        //DiggingIn Controller
        controllerRegistrar.add(
                new AnimationController<>(this,"DiggingController",1, this::animationDiggingPredicate)
        );

        //DiggingOut Controller
        controllerRegistrar.add(
                new AnimationController<>(this, "EmergingController", 1, this::animationEmergingPredicate)
        );

//        //Ground Punch Controller
//        controllerRegistrar.add(
//                new AnimationController<>(this, "GroundPunchController", 1, state -> {
//                    state.getController().setAnimationSpeed(0.6f);
//                    return PlayState.STOP;
//                }).triggerableAnim("ground_punch", GROUND_PUNCH)
//        );
    }


    //Explosion Damage Resistance
    @Override
    public boolean damage(DamageSource source, float amount) {
        if(source.isOf(DamageTypes.EXPLOSION) || source.isOf(DamageTypes.PLAYER_EXPLOSION)){
            amount=amount/16;
        }

        return super.damage(source, amount);
    }

    //Excavator Common
    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);

        nbt.putBoolean("InGround", this.dataTracker.get(InGROUND));
        nbt.putInt("ReturnToGroundTicks", this.ReturnToGround_Ticks);
        nbt.putBoolean("Invisible", this.dataTracker.get(INVISIBLE));
    }

    //Cadaverine damage
    @Override
    public boolean tryAttack(Entity target) {
        boolean bl = super.tryAttack(target);
        if(target instanceof LivingEntity living && bl && this.random.nextInt()%3==0){
            living.addStatusEffect(new StatusEffectInstance(TCOTS_Effects.CADAVERINE, 160), this);
        }

        if(target instanceof PlayerEntity player && player.isBlocking()){
//            if(player.getOffHandStack().getItem() instanceof ShieldItem)
                player.getOffHandStack().damage(10, random, (ServerPlayerEntity) (player));
        }

        return bl;
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);

        this.setInGround(nbt.getBoolean("InGround"));
        this.ReturnToGround_Ticks = nbt.getInt("ReturnToGroundTicks");
        this.setInvisibleData(nbt.getBoolean("Invisible"));
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
        if (getInGround()) {
            return groundBox(this);
        }
        else{
            // Normal hit-box otherwise
            return super.calculateBoundingBox();
        }
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
    public boolean getInGround() {
        return this.dataTracker.get(InGROUND);
    }

    @Override
    public void setInGround(boolean wasInGround) {
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

    //Sounds
    @Override
    protected SoundEvent getAmbientSound() {
        if (!this.getInGround()) {
            return TCOTS_Sounds.GRAVEIR_IDLE;
        } else {
            return null;
        }
    }

    @Override
    protected SoundEvent getStepSound() {
        return SoundEvents.ENTITY_ZOGLIN_STEP;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return TCOTS_Sounds.GRAVEIR_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return TCOTS_Sounds.GRAVEIR_DEATH;
    }

    @Override
    protected SoundEvent getAttackSound() {
        return TCOTS_Sounds.GRAVEIR_ATTACK;
    }

    //Spawn
    public static boolean canSpawnGraveir(EntityType<? extends NecrophageMonster> type, ServerWorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
        if(spawnReason == SpawnReason.SPAWNER){
            return world.getDifficulty() != Difficulty.PEACEFUL && HostileEntity.isSpawnDark(world, pos, random) && HostileEntity.canMobSpawn(type, world, spawnReason, pos, random);
        } else {
            return world.getDifficulty() != Difficulty.PEACEFUL && HostileEntity.isSpawnDark(world, pos, random) && HostileEntity.canMobSpawn(type, world, spawnReason, pos, random)
                    && pos.getY() <= 63
                    && (world.getBlockState(pos.down()).isIn(BlockTags.BASE_STONE_OVERWORLD));
        }
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
