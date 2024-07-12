package TCOTS.entity.necrophages;

import TCOTS.entity.goals.*;
import TCOTS.entity.interfaces.ExcavatorMob;
import TCOTS.sounds.TCOTS_Sounds;
import TCOTS.utils.EntitiesUtil;
import TCOTS.utils.GeoControllersUtil;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.RevengeGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
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

import java.util.List;

public class GraveirEntity extends NecrophageMonster implements GeoEntity, ExcavatorMob {
    //xTODO: Add sounds
    //xTODO: Add spawn
    //xTODO: Add bestiary
    //xTODO: Add drop
    //xTODO: Add new attack

    private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    public static final byte FALLING_PARTICLES = 42;
    public static final RawAnimation GROUND_PUNCH = RawAnimation.begin().thenPlay("special.ground_punch");


    protected static final TrackedData<Boolean> InGROUND = DataTracker.registerData(GraveirEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    protected static final TrackedData<Boolean> EMERGING = DataTracker.registerData(GraveirEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    protected static final TrackedData<Boolean> INVISIBLE = DataTracker.registerData(GraveirEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    protected static final TrackedData<Boolean> PUNCHING = DataTracker.registerData(GraveirEntity.class, TrackedDataHandlerRegistry.BOOLEAN);


    public GraveirEntity(EntityType<? extends PathAwareEntity> entityType, World world) {
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
                .add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK,1.5);
    }


    @Override
    protected void initGoals() {
        //Emerge from ground
        this.goalSelector.add(0, new EmergeFromGroundGoal_Excavator(this, 600));
        this.goalSelector.add(1, new SwimGoal(this));

        //Returns to ground
        this.goalSelector.add(2, new ReturnToGroundGoal_Excavator(this));

        this.goalSelector.add(3, new Graveir_GroundPunch(this, 200));

        this.goalSelector.add(4, new Graveir_MeleeAttackGoal(this, 1.2D, false, 3600));

        this.goalSelector.add(5, new WanderAroundGoal_Excavator(this, 0.75f, 20));

        this.goalSelector.add(6, new LookAroundGoal_Excavator(this));

        //Objectives
        this.targetSelector.add(0, new RevengeGoal(this, GraveirEntity.class));
        this.targetSelector.add(1, new ActiveTargetGoal<>(this, PlayerEntity.class, true));
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, MerchantEntity.class, true));
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, IronGolemEntity.class, true));
    }

    private static class Graveir_MeleeAttackGoal extends MeleeAttackGoal_Excavator{

        private final GraveirEntity graveir;

        public Graveir_MeleeAttackGoal(PathAwareEntity mob, double speed, boolean pauseWhenMobIdle, int ticksBeforeToGround) {
            super(mob, speed, pauseWhenMobIdle, ticksBeforeToGround);
            this.graveir=(GraveirEntity) mob;
        }

        @Override
        public boolean canStart() {
            return super.canStart() && !graveir.isPunching();
        }

        @Override
        public boolean shouldContinue() {
            return super.shouldContinue() && !graveir.isPunching();
        }
    }

    private static class Graveir_GroundPunch extends Goal {

        private final GraveirEntity graveir;

        private final int punchCooldown;

        public Graveir_GroundPunch(GraveirEntity graveir, int punchCooldown){
            this.graveir=graveir;
            this.punchCooldown=punchCooldown;
        }

        @Override
        public boolean shouldRunEveryTick() {
            return true;
        }

        @Override
        public boolean canStart() {
            LivingEntity target = this.graveir.getTarget();

            if (target != null) {
                //5 square distance like 1.5 blocks approx
                //I want 7.5 blocks approx
                //So 7.5/1.5=5
                return !this.graveir.cooldownBetweenPunch
                        && this.graveir.isAttacking()
                        && !this.graveir.isInFluid()
                        && !this.graveir.hasVehicle()
//                        && !this.devourer.getWorld().getBlockState(this.devourer.getBlockPos()).isOf(Blocks.HONEY_BLOCK)
//                        && !this.devourer.getWorld().getBlockState(this.devourer.getBlockPos()).isOf(Blocks.COBWEB)
                        && this.graveir.squaredDistanceTo(target) < 10;
            } else {
                return false;
            }
        }


        @Override
        public boolean shouldContinue() {
            return !graveir.cooldownBetweenPunch
                    && !this.graveir.isInFluid()
                    && !this.graveir.hasVehicle();
        }

        int ticks=34;

        @Override
        public void start() {
            ticks=34;
        }

        @Override
        public void tick() {
            LivingEntity target = this.graveir.getTarget();

            graveir.getNavigation().stop();
            graveir.setTarget(this.graveir.getTarget());
            graveir.setAttacking(true);

            if(target!=null && ticks==34){
                triggersAttack();
            }

            if(ticks>0){
                ticks--;
            }

            if(ticks==0){
                punchAttack();
            }
        }

        private void triggersAttack(){
            graveir.setIsPunching(true);
            graveir.playSound(TCOTS_Sounds.GRAVEIR_GROUND_PUNCH,1.0f,1.0f);
            graveir.triggerAnim("GroundPunchController","ground_punch");
        }

        private void punchAttack(){

            EntitiesUtil.pushAndDamageEntities(this.graveir, 12f, 2.0, 2.0, 2.2, GraveirEntity.class);
            graveir.getWorld().sendEntityStatus(graveir, FALLING_PARTICLES);
            graveir.playSound(TCOTS_Sounds.GROUND_PUNCH,1.0f,1.0f);
            graveir.punchTicks=punchCooldown;
            ticks=34;
            graveir.setIsPunching(false);
            graveir.cooldownBetweenPunch=true;
        }

    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(InGROUND, Boolean.FALSE);
        this.dataTracker.startTracking(EMERGING, Boolean.FALSE);
        this.dataTracker.startTracking(INVISIBLE, Boolean.FALSE);

        this.dataTracker.startTracking(PUNCHING, Boolean.FALSE);
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

        //Ground Punch Controller
        controllerRegistrar.add(
                new AnimationController<>(this, "GroundPunchController", 1, state -> {
                    state.getController().setAnimationSpeed(0.6f);
                    return PlayState.STOP;
                }).triggerableAnim("ground_punch", GROUND_PUNCH)
        );
    }


    //Explosion Damage Resistance
    @Override
    public boolean damage(DamageSource source, float amount) {
        if(source.isOf(DamageTypes.EXPLOSION) || source.isOf(DamageTypes.PLAYER_EXPLOSION)){
            amount=amount/16;
        }

        return super.damage(source, amount);
    }

    public boolean cooldownBetweenPunch=false;
    private int counter=0;
    private int punchTicks;

    private void tickPunch(){
        if(counter>0){
            counter=0;
        }

        if (punchTicks > 0) {
            --punchTicks;
        } else {
            cooldownBetweenPunch=false;
        }
    }


    @Override
    public void handleStatus(byte status) {
        if(status==FALLING_PARTICLES){
            spawnSmokeParticles(counter);
        } else{
            super.handleStatus(status);
        }
    }

    private void spawnSmokeParticles(int counter){
        BlockState blockState = this.getSteppingBlockState();

        while (counter < 80) {
            double Vd = this.random.nextGaussian() * 0.2;
            double Ve = this.random.nextGaussian() * 0.2;
            double Vf = this.random.nextGaussian() * 0.2;

            double d = this.getX() + (double) MathHelper.nextBetween(this.getRandom(), -0.7F, 0.7F);
            double e = this.getY();
            double f = this.getZ() + (double) MathHelper.nextBetween(this.getRandom(), -0.7F, 0.7F);

            if(counter % 4 ==0){
                this.getWorld().addParticle(ParticleTypes.WHITE_SMOKE, this.getX(), this.getY()-0.5f, this.getZ(), Vd, Ve, Vf);
            } else if (blockState.getRenderType() != BlockRenderType.INVISIBLE){
                this.getWorld().addParticle(new BlockStateParticleEffect(ParticleTypes.BLOCK, blockState), d, e, f, 0.0, 0.0, 0.0);
            }

            ++counter;
        }
    }


    public boolean isPunching() {
        return this.dataTracker.get(PUNCHING);
    }

    public void setIsPunching(boolean isPunching) {
        this.dataTracker.set(PUNCHING, isPunching);
    }

    //Excavator Common
    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putInt("PunchCooldown", this.punchTicks);

        nbt.putBoolean("InGround", this.dataTracker.get(InGROUND));
        nbt.putInt("ReturnToGroundTicks", this.ReturnToGround_Ticks);
        nbt.putBoolean("Invisible", this.dataTracker.get(INVISIBLE));
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        this.punchTicks = nbt.getInt("PunchCooldown");

        this.setInGroundDataTracker(nbt.getBoolean("InGround"));
        this.ReturnToGround_Ticks = nbt.getInt("ReturnToGroundTicks");
        this.setInvisibleData(nbt.getBoolean("Invisible"));
    }

    @Override
    public void tick() {
        //Counter for particles
        this.tickExcavator(this);

        //Punch timer
        this.tickPunch();

        if(this.isPunching() && !this.isAttacking()){
            this.setIsPunching(false);
        }

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
        if (getInGroundDataTracker()) {
            return groundBox(this);
        }
        else{
            // Normal hit-box otherwise
            return super.calculateBoundingBox();
        }
    }

    @Override
    public boolean isPushable() {
        return !this.getIsEmerging() && !this.getInGroundDataTracker() && !this.isPunching();
    }

    @Override
    public boolean isInvulnerableTo(DamageSource damageSource) {
        return this.getIsEmerging() || this.getInGroundDataTracker() || super.isInvulnerableTo(damageSource) || this.isPunching();
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


    //Sounds
    @Override
    protected SoundEvent getAmbientSound() {
        if (!this.getInGroundDataTracker()) {
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
