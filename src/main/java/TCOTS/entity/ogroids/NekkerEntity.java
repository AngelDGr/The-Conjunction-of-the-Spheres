package TCOTS.entity.ogroids;

import TCOTS.entity.necrophages.Necrophage_Base;
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
import net.minecraft.entity.mob.PathAwareEntity;
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
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Difficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

import java.util.EnumSet;

public class NekkerEntity extends Ogroid_Base implements GeoEntity {

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

    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    protected static final TrackedData<Boolean> LUGGING = DataTracker.registerData(NekkerEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    protected static final TrackedData<Boolean> InGROUND = DataTracker.registerData(NekkerEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    protected static final TrackedData<Boolean> EMERGING = DataTracker.registerData(NekkerEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

    public NekkerEntity(EntityType<? extends NekkerEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void initGoals() {
        //Attack

        //Emerge from ground
        this.goalSelector.add(0, new Nekker_EmergeFromGround(this));
        this.goalSelector.add(1, new SwimGoal(this));


        this.goalSelector.add(2, new NekkerEntity.Attack_Lunge(this, 200, 1.2));

        //Returns to ground
        this.goalSelector.add(3, new Nekker_ReturnToGround(this));


        this.goalSelector.add(4, new Nekker_MeleeAttackGoal(this, 1.2D, false));


        this.goalSelector.add(5, new Nekker_WanderAroundGoal(this, 0.75f, 20));

        this.goalSelector.add(6, new LookAroundGoal(this));

        //Objectives
        this.targetSelector.add(1, new RevengeGoal(this, new Class[0]).setGroupRevenge(new Class[0]));
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, PlayerEntity.class, true));
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, MerchantEntity.class, true));
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, IronGolemEntity.class, true));
    }

    public boolean cooldownBetweenLunges = false;
    public int LungeTicks;

    private class Attack_Lunge extends Goal {
        private final NekkerEntity mob;
        private final int cooldownBetweenLungesAttacks;
        private final double SpeedLungeMultiplier;

        private Attack_Lunge(NekkerEntity mob, int cooldownBetweenLungesAttacks, double lungeImpulse) {
            this.mob = mob;
            this.cooldownBetweenLungesAttacks = cooldownBetweenLungesAttacks;
            this.setControls(EnumSet.of(Control.MOVE, Control.JUMP));
            this.SpeedLungeMultiplier = lungeImpulse;
        }

        @Override
        public boolean canStart() {
            LivingEntity target = this.mob.getTarget();
            if (target != null) {
                //5 square distance like 1.5 blocks approx
                //I want 7.5 blocks approx
                //So 7.5/1.5=5
                return !NekkerEntity.this.cooldownBetweenLunges && this.mob.isAttacking()
                        && this.mob.squaredDistanceTo(target) > 5 && this.mob.squaredDistanceTo(target) < 25
                        && (this.mob.getTarget().getY() - this.mob.getY()) <= 1
                        && !this.mob.getIsEmerging()
                        && !this.mob.getInGroundDataTracker()
                        ;
            } else {
                return false;
            }
        }

        @Override
        public boolean shouldContinue() {
            LivingEntity target = this.mob.getTarget();
            if (target != null) {
                //5 square distance like 1.5 blocks approx
                //I want 7.5 blocks approx
                //So 7.5/1.5=5
                return !NekkerEntity.this.cooldownBetweenLunges && this.mob.isAttacking()
                        && this.mob.squaredDistanceTo(target) > 5 && this.mob.squaredDistanceTo(target) < 25
                        && (this.mob.getTarget().getY() - this.mob.getY()) <= 1;
            } else {
                return false;
            }
        }

        Vec3d vec3D_lunge;
        int randomExtra;

        @Override
        public boolean shouldRunEveryTick() {
            return true;
        }

        @Override
        public void start() {
            this.mob.getNavigation().stop();
        }

        @Override
        public void tick() {
            LivingEntity livingEntity = this.mob.getTarget();

            if (livingEntity != null) {
//                double d = this.mob.getSquaredDistanceToAttackPosOf(livingEntity);
                LungeAttack(livingEntity);
            }
        }

        @NotNull
        private Vec3d getVec3d(LivingEntity target) {
            double dXtoTarget = target.getX() - this.mob.getEyePos().x;
            double dYtoTarget = target.getY() - this.mob.getEyePos().y;
            double dZtoTarget = target.getZ() - this.mob.getEyePos().z;
            double length = Math.sqrt(dXtoTarget * dXtoTarget + dYtoTarget * dYtoTarget + dZtoTarget * dZtoTarget);

            //Movement Vector
            return new Vec3d((dXtoTarget / length) * SpeedLungeMultiplier,
                    (dYtoTarget / length),
                    (dZtoTarget / length) * SpeedLungeMultiplier);
        }

        private void LungeAttack(LivingEntity target) {
            //Check if it can do a lunge
            if (!NekkerEntity.this.cooldownBetweenLunges) {
                //Makes the lunge
                //Extra random ticks in cooldown
                randomExtra = NekkerEntity.this.random.nextInt(51);
                //0.35 Y default
                vec3D_lunge = getVec3d(target).normalize();
                NekkerEntity.this.setIsLugging(true);

                NekkerEntity.this.setVelocity(NekkerEntity.this.getVelocity().add(vec3D_lunge.x, 0.35, vec3D_lunge.z));
                this.mob.getLookControl().lookAt(target, 30.0F, 30.0F);

                NekkerEntity.this.playSound(TCOTS_Sounds.NEKKER_LUNGE, 1.0F, 1.0F);

                //Put the cooldown
                LungeTicks = cooldownBetweenLungesAttacks + randomExtra;
                NekkerEntity.this.cooldownBetweenLunges = true;
            }

        }

    }

    //Makes the nekker occult in ground
    private class Nekker_ReturnToGround extends Goal {
        private final NekkerEntity nekker;
        int ticks=35;
        private Nekker_ReturnToGround(NekkerEntity mob) {
            this.nekker = mob;
        }
        @Override
        public boolean canStart() {
            return nekker.getInGroundDataTracker();
        }
        @Override
        public boolean shouldContinue(){
            return nekker.getInGroundDataTracker();
        }
        @Override
        public void start(){
            ticks=35;
            nekker.playSound(TCOTS_Sounds.NEKKER_DIGGING,1.0F,1.0F);
            nekker.getNavigation().stop();
            nekker.getLookControl().lookAt(0,0,0);
        }

    }

    //Makes the nekker emerge from ground
    private class Nekker_EmergeFromGround extends Goal{

        private final NekkerEntity nekker;

        protected final PathAwareEntity mob;

        int AnimationTicks=36;

        private Nekker_EmergeFromGround(NekkerEntity mob) {
            this.nekker = mob;
            this.mob = mob;
        }

        @Override
        public boolean canStart() {
            return canStartO() && nekker.getInGroundDataTracker();
        }
        public boolean canStartO(){

            LivingEntity livingEntity = this.mob.getTarget();
            //If it doesn't have target
            if (livingEntity == null) {
                return false;
            }
            //If it's the target dead
            else if (!livingEntity.isAlive()) {
                return false;
            }
            else {
                return this.mob.squaredDistanceTo(livingEntity.getX(), livingEntity.getY(), livingEntity.getZ()) <= 80;
            }
        }

        @Override
        public void start(){
            this.nekker.playSound(TCOTS_Sounds.NEKKER_EMERGING, 1.0F, 1.0F);
            this.nekker.spawnGroundParticles();
            AnimationTicks=36;
            nekker.setIsEmerging(true);
        }

        @Override
        public boolean shouldContinue(){
            return shouldContinueO() && nekker.getInGroundDataTracker();
        }
        public boolean shouldContinueO(){
            LivingEntity livingEntity = this.mob.getTarget();
            if (livingEntity == null) {
                return false;
            } else if (!livingEntity.isAlive()) {
                return false;
            } else {
                return !(livingEntity instanceof PlayerEntity) || !livingEntity.isSpectator() && !((PlayerEntity)livingEntity).isCreative();
            }
        }

        @Override
        public void tick(){
            if (AnimationTicks > 0) {
//                System.out.println("AnimationTicks"+AnimationTicks);
                --AnimationTicks;
            }else {
                stop();
            }
        }

        @Override
        public void stop(){
            nekker.setIsEmerging(false);
            if(NekkerEntity.this.getInGroundDataTracker()){
                NekkerEntity.this.ReturnToGround_Ticks=500;
                NekkerEntity.this.setInGroundDataTracker(false);}
        }

        @Override
        public boolean shouldRunEveryTick() {
            return true;
        }

    }


    private class Nekker_WanderAroundGoal extends WanderAroundGoal{

        public Nekker_WanderAroundGoal(PathAwareEntity mob, double speed, int chance) {
            super(mob, speed, chance);
        }

        @Override
        public boolean canStart(){
            return super.canStart() && !NekkerEntity.this.getInGroundDataTracker();
        }

        @Override
        public boolean shouldContinue(){
            return super.shouldContinue() && !NekkerEntity.this.getInGroundDataTracker();
        }
    }

    private class Nekker_MeleeAttackGoal extends MeleeAttackGoal{
        private final NekkerEntity nekker;

        public Nekker_MeleeAttackGoal(NekkerEntity mob, double speed, boolean pauseWhenMobIdle) {
            super(mob, speed, pauseWhenMobIdle);
            this.nekker = mob;
        }

        @Override
        public boolean canStart() {
            return super.canStart()
                    && !this.nekker.getIsEmerging()
                    && !this.nekker.getInGroundDataTracker();
        }
    }

    public int ReturnToGround_Ticks=20;

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

        //DiggingIn Controller
        controllerRegistrar.add(
                new AnimationController<>(this,"DiggingInController",1, state -> {

                    if(this.getInGroundDataTracker() && !this.getIsEmerging()){
                        state.setAnimation(DIGGING_IN);
                        return PlayState.CONTINUE;
                    }else{
                        state.getController().forceAnimationReset();
                        return PlayState.STOP;
                    }
                })
        );

        //DiggingOut Controller
        controllerRegistrar.add(
                new AnimationController<>(this, "DiggingOutController", 1, state -> {
                    if (this.getIsEmerging()){
                        state.setAnimation(DIGGING_OUT);
                        return PlayState.CONTINUE;
                    }
                    else{
                        state.getController().forceAnimationReset();
                        return PlayState.STOP;
                    }
                })
        );
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(LUGGING, Boolean.FALSE);
        this.dataTracker.startTracking(InGROUND, Boolean.FALSE);
        this.dataTracker.startTracking(EMERGING, Boolean.FALSE);
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
            return new Box(this.getX() - 0.39, this.getY() + 0.1, this.getZ() - 0.39,
                    this.getX() + 0.39, this.getY(), this.getZ() + 0.39);
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

    private void spawnGroundParticles() {
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
//                            || this.getWorld().getBlockState(entityPos.down()).isIn(BlockTags.STONE_ORE_REPLACEABLES)
            )
            ){
                this.setInGroundDataTracker(true);
            }
        }
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
