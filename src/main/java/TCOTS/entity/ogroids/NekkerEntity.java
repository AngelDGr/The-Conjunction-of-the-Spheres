package TCOTS.entity.ogroids;

import TCOTS.entity.goals.*;
import TCOTS.entity.interfaces.ExcavatorMob;
import TCOTS.entity.interfaces.LungeMob;
import TCOTS.sounds.TCOTS_Sounds;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
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
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;


public class NekkerEntity extends Ogroid_Base implements GeoEntity, ExcavatorMob, LungeMob {

    //xTODO: Add spawn
    //xTODO: Add drops

    public static final RawAnimation IDLE = RawAnimation.begin().thenLoop("idle");
    public static final RawAnimation RUNNING = RawAnimation.begin().thenLoop("move.running");
    public static final RawAnimation WALKING = RawAnimation.begin().thenLoop("move.walking");
    public static final RawAnimation ATTACK1 = RawAnimation.begin().thenPlay("attack.swing1");
    public static final RawAnimation ATTACK2 = RawAnimation.begin().thenPlay("attack.swing2");
    public static final RawAnimation ATTACK3 = RawAnimation.begin().thenPlay("attack.swing3");
    public static final RawAnimation LUNGE = RawAnimation.begin().thenPlay("attack.lunge");
    public static final RawAnimation DIGGING_OUT = RawAnimation.begin().thenPlayAndHold("special.diggingOut");
    public static final RawAnimation DIGGING_IN = RawAnimation.begin().thenPlayAndHold("special.diggingIn");

    @Override
    public RawAnimation getDiggingAnimation() {
        return DIGGING_IN;
    }

    @Override
    public RawAnimation getEmergingAnimation() {
        return DIGGING_OUT;
    }
    private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    protected static final TrackedData<Boolean> LUGGING = DataTracker.registerData(NekkerEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    protected static final TrackedData<Boolean> InGROUND = DataTracker.registerData(NekkerEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    protected static final TrackedData<Boolean> EMERGING = DataTracker.registerData(NekkerEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    protected static final TrackedData<Boolean> INVISIBLE = DataTracker.registerData(NekkerEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    public NekkerEntity(EntityType<? extends NekkerEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void initGoals() {

        //Emerge from ground
        this.goalSelector.add(0, new EmergeFromGroundGoal_Excavator(this,500));
        this.goalSelector.add(1, new SwimGoal(this));

        this.goalSelector.add(2, new LungeAttackGoal(this, 200, 1.2));

        //Returns to ground
        this.goalSelector.add(3, new ReturnToGroundGoal_Excavator(this));

        //Attack
        this.goalSelector.add(4, new MeleeAttackGoal_Excavator(this, 1.2D, false));


        this.goalSelector.add(5, new WanderAroundGoal_Excavator(this, 0.75f, 20));

        this.goalSelector.add(6, new LookAroundGoal_Excavator(this));

        //Objectives
        this.targetSelector.add(1, new RevengeGoal(this, NekkerEntity.class).setGroupRevenge());
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, PlayerEntity.class, true));
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, MerchantEntity.class, true));
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, IronGolemEntity.class, true));
    }

    public boolean cooldownBetweenLunges = false;

    public boolean getCooldownBetweenLunges() {
        return cooldownBetweenLunges;
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

    public int ReturnToGround_Ticks=20;

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
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.26f)

                .add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK, 0.2f)
                .add(EntityAttributes.GENERIC_ARMOR,2f);

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
                new AnimationController<>(this, "AttackController", 1, state -> {
                    state.getController().forceAnimationReset();
                    // Random instance
                    // Generates three random numbers
                    if (this.handSwinging) {
                        int r = NekkerEntity.this.random.nextInt(3);
                        switch (r) {
                            case 0:
                                return state.setAndContinue(ATTACK1);

                            case 1:
                                return state.setAndContinue(ATTACK2);

                            case 2:
                                return state.setAndContinue(ATTACK3);
                        }
                    }
                    return PlayState.CONTINUE;
                })
        );

        //Lunge Controller
        controllerRegistrar.add(
                new AnimationController<>(this, "LungeController", 1, state -> {
                    if (this.getIsLugging()) {
                        state.setAnimation(LUNGE);
                        return PlayState.CONTINUE;
                    }

                    state.getController().forceAnimationReset();
                    return PlayState.CONTINUE;
                })
        );

        //Digging Controller
        controllerRegistrar.add(
                new AnimationController<>(this,"DiggingController",1, this::animationDiggingPredicate)
        );

        //Emerging Controller
        controllerRegistrar.add(
                new AnimationController<>(this, "EmergingController", 1, this::animationEmergingPredicate)
        );
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(LUGGING, Boolean.FALSE);
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
    }
    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        this.setInGroundDataTracker(nbt.getBoolean("InGround"));
        this.ReturnToGround_Ticks = nbt.getInt("ReturnToGroundTicks");
        super.readCustomDataFromNbt(nbt);
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

    public final boolean getIsLugging() {
        return this.dataTracker.get(LUGGING);
    }
    public final void setIsLugging(boolean wasLugging) {
        this.dataTracker.set(LUGGING, wasLugging);
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

    int AnimationParticlesTicks=36;
    @Override
    public void tick() {
        //Start the counter for the Lunge attack
        if (NekkerEntity.this.LungeTicks > 0) {
            NekkerEntity.this.setIsLugging(false);
            --NekkerEntity.this.LungeTicks;
        } else {
            NekkerEntity.this.cooldownBetweenLunges = false;
        }

        //Particles when return to ground
        if(this.AnimationParticlesTicks > 0 && this.getInGroundDataTracker()){
            this.spawnGroundParticles();
            --this.AnimationParticlesTicks;
        } else if (AnimationParticlesTicks==0) {
            this.setInvisibleData(true);
            AnimationParticlesTicks=-1;
        }

        //Particles when emerges from ground
        if(this.getIsEmerging()){
            this.spawnGroundParticles();
        }
        super.tick();
    }

    @Override
    public void mobTick(){
        if (NekkerEntity.this.ReturnToGround_Ticks > 0
                && NekkerEntity.this.ReturnToGround_Ticks < 200
                && !this.getIsEmerging()
                && !this.isAttacking()
        ) {
            --NekkerEntity.this.ReturnToGround_Ticks;
        }else{
            BlockPos entityPos = new BlockPos((int)this.getX(), (int)this.getY(), (int)this.getZ());

            //Makes the Rotfiend return to the dirt/sand/stone
            if(NekkerEntity.this.ReturnToGround_Ticks==0 && (
                    this.getWorld().getBlockState(entityPos.down()).isIn(BlockTags.DIRT) ||
                            this.getWorld().getBlockState(entityPos.down()).isOf(Blocks.SAND)
            )
            ){
                this.setInGroundDataTracker(true);
            }
        }

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
    public SoundEvent getEmergingSound() {
        return TCOTS_Sounds.NEKKER_EMERGING;
    }

    @Override
    public SoundEvent getDiggingSound() {
        return TCOTS_Sounds.NEKKER_DIGGING;
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
    public boolean tryAttack(Entity target) {
        this.playSound(TCOTS_Sounds.NEKKER_ATTACK, 1.0F, 1.0F);
        return super.tryAttack(target);
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


    public static boolean canSpawnNekker(EntityType<? extends Ogroid_Base> type, ServerWorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
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
