package TCOTS.entity.necrophages;

import TCOTS.entity.TCOTS_Entities;
import TCOTS.entity.goals.LungeAttackGoal;
import TCOTS.entity.interfaces.LungeMob;
import TCOTS.entity.misc.CommonControllers;
import TCOTS.items.potions.bombs.MoonDustBomb;
import TCOTS.sounds.TCOTS_Sounds;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.passive.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.Difficulty;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

import java.util.List;
import java.util.function.Predicate;

public class GhoulEntity extends Necrophage_Base implements GeoEntity, LungeMob {

    //xTODO: Add new combat/regeneration
    //xTODO: Add Ghoul's Blood
    //xTODO: Add monster nests & spawn
    public static final byte GHOUL_REGENERATING = 99;

    public final int GHOUL_REGENERATION_TIME=200;

    public int getGHOUL_REGENERATION_TIME() {
        return GHOUL_REGENERATION_TIME;
    }

    private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    public static final RawAnimation IDLE = RawAnimation.begin().thenLoop("idle");
    public static final RawAnimation RUNNING = RawAnimation.begin().thenLoop("move.running");
    public static final RawAnimation WALKING = RawAnimation.begin().thenLoop("move.walking");
    public static final RawAnimation START_REGEN = RawAnimation.begin().thenPlay("special.regen");

    protected static final TrackedData<Boolean> LUGGING = DataTracker.registerData(GhoulEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    protected static final TrackedData<Boolean> REGENERATING = DataTracker.registerData(GhoulEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    protected static final TrackedData<Boolean> INVOKING_REGENERATING = DataTracker.registerData(GhoulEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    protected static final TrackedData<Integer> TIME_FOR_REGEN = DataTracker.registerData(GhoulEntity.class, TrackedDataHandlerRegistry.INTEGER);


    public GhoulEntity(EntityType<? extends PathAwareEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(LUGGING, Boolean.FALSE);
        this.dataTracker.startTracking(REGENERATING, Boolean.FALSE);
        this.dataTracker.startTracking(INVOKING_REGENERATING, Boolean.FALSE);
        this.dataTracker.startTracking(TIME_FOR_REGEN, 0);
    }

    @Override
    public int getMaxLookPitchChange() {
        return 15;
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));

        this.goalSelector.add(1, new GhoulRegeneration(this, 60, 400, GHOUL_REGENERATION_TIME,
                0.5f,35));

        this.goalSelector.add(2, new LungeAttackGoal(this, 100, 1.2,10,30));

        this.goalSelector.add(3, new Ghoul_MeleeAttackGoal(this, 1.2D, false));

        this.goalSelector.add(4, new GhoulEatFlesh(this,1D));

        this.goalSelector.add(5, new WanderAroundGoal(this, 0.75f,80));

        this.goalSelector.add(6, new LookAroundGoal(this));

        //Objectives
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, PlayerEntity.class, true));
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, ZombieEntity.class, true));
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, ZoglinEntity.class, true));
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, ZombieHorseEntity.class, true));
        this.targetSelector.add(3, new ActiveTargetGoal<>(this, MerchantEntity.class, true));
        this.targetSelector.add(3, new ActiveTargetGoal<>(this, IronGolemEntity.class, true));

        this.targetSelector.add(4, new ActiveTargetGoal<>(this, HoglinEntity.class, true));
        this.targetSelector.add(5, new ActiveTargetGoal<>(this, CowEntity.class, true));
        this.targetSelector.add(5, new ActiveTargetGoal<>(this, PigEntity.class, true));
        this.targetSelector.add(5, new ActiveTargetGoal<>(this, SheepEntity.class, true));
        this.targetSelector.add(6, new ActiveTargetGoal<>(this, GoatEntity.class, true));
    }

    public static DefaultAttributeContainer.Builder setAttributes() {
        return AnimalEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 18.0D)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 4.0f) //Amount of health that hurts you
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.29f);
    }

    protected static class GhoulRegeneration extends Goal {
        private final int ticksAttackedBeforeRegen;
        private final int CooldownBetweenRegens;
        private final int TimeForRegen;
        private final GhoulEntity mob;
        private final int stopTicks;
        protected final float healthPercentage;

        public GhoulRegeneration(GhoulEntity mob, int ticksAttackedBeforeRegen, int CooldownBetweenRegens, int TimeForRegen, float HealthPercentageToStart, int stopTicks) {
            this.ticksAttackedBeforeRegen = ticksAttackedBeforeRegen;
            this.mob=mob;
            this.CooldownBetweenRegens=CooldownBetweenRegens;
            this.TimeForRegen=TimeForRegen;
            this.healthPercentage=HealthPercentageToStart;
            this.stopTicks=stopTicks;
        }

        @Override
        public boolean canStart() {
            return canStartRegen() && !MoonDustBomb.checkEffect(mob);
        }

        @Override
        public boolean shouldContinue() {
            return !mob.getIsRegenerating() && !MoonDustBomb.checkEffect(mob);
        }

        private int StoppedTicks;
        @Override
        public void start() {
            StoppedTicks=stopTicks;
            mob.setIsInvokingRegen(true);
            mob.getNavigation().stop();
            mob.playSound(mob.getScreamSound(),1,1);
            mob.triggerAnim("RegenController", "start_regen");
        }

        @Override
        public void stop() {
            this.mob.setIsRegenerating(!MoonDustBomb.checkEffect(mob));
            mob.setTimeForRegen(TimeForRegen);
            if (!this.mob.isSilent()) {
                this.mob.getWorld().sendEntityStatus(this.mob, GHOUL_REGENERATING);
            }

            mob.setHasCooldownForRegen(true);
            mob.setCooldownForRegen(CooldownBetweenRegens);

            StoppedTicks=stopTicks;
            mob.setIsInvokingRegen(false);
        }
        @Override
        public void tick() {
            if (StoppedTicks > 0) {
                mob.getNavigation().stop();
                --StoppedTicks;
            } else {
                stop();
            }
        }

        @Override
        public boolean shouldRunEveryTick() {
            return true;
        }

        protected boolean canStartRegen(){
            return ((mob.getLastAttackedTime() + ticksAttackedBeforeRegen) < mob.age) && !(mob.getIsRegenerating())
                    && (mob.getHealth() < (mob.getMaxHealth() * healthPercentage)) && mob.isOnGround() && !(mob.hasCooldownForRegen());
        }
    }

    protected static class Ghoul_MeleeAttackGoal extends MeleeAttackGoal {

        public Ghoul_MeleeAttackGoal(PathAwareEntity mob, double speed, boolean pauseWhenMobIdle) {
            super(mob, speed, pauseWhenMobIdle);
        }

        @Override
        public boolean shouldContinue() {
            return super.shouldContinue() && !((GhoulEntity)mob).getIsInvokingRegen();
        }

        @Override
        public boolean canStart() {
            return super.canStart() && !((GhoulEntity)mob).getIsInvokingRegen();
        }
    }

    protected static class GhoulEatFlesh extends Goal {

        private final GhoulEntity ghoul;

        private final double speed;

        public GhoulEatFlesh(GhoulEntity ghoul, double speed){
            this.ghoul=ghoul;
            this.speed=speed;
        }

        public static final Predicate<ItemEntity> CAN_EAT = item -> !item.cannotPickup() && item.isAlive() &&
                ((item.getStack().getItem() == Items.ROTTEN_FLESH)
                        || (item.getStack().getItem() == Items.BEEF)
                        || (item.getStack().getItem() == Items.PORKCHOP)
                        || (item.getStack().getItem() == Items.MUTTON))
                ;
        @Override
        public boolean canStart() {
            List<ItemEntity> list = searchFleshList();

            return !list.isEmpty();
        }

        @Override
        public boolean shouldContinue() {
            List<ItemEntity> list = searchFleshList();

            return !list.isEmpty();
        }

        @Override
        public void start() {
            List<ItemEntity> list = searchFleshList();

            if (!list.isEmpty()) {
                this.ghoul.getNavigation().startMovingTo(list.get(0), speed);
            }
        }

        int timerToEat;

        @Override
        public void tick() {
            List<ItemEntity> list = searchFleshList();

            if(timerToEat==0) {
                if (!list.isEmpty()) {
                    this.ghoul.getNavigation().startMovingTo(list.get(0), speed);
                    this.ghoul.getLookControl().lookAt(list.get(0), 30.0f, 30.0f);
                }

                //Search the flesh
                if (ghoul.distanceTo(list.get(0)) < 2) {
                    list.get(0).discard();
                    ItemStack stack = list.get(0).getStack();
                    int i = stack.getCount();

                    this.ghoul.playSound(SoundEvents.ENTITY_GENERIC_EAT, 1.0f, 1.0f);
                    this.ghoul.getWorld().sendEntityStatus(ghoul, EntityStatuses.CREATE_EATING_PARTICLES);
                    this.ghoul.heal(i);
                    timerToEat = 10;
                }
            }

            if(timerToEat>0) {
                --timerToEat;
            }
        }

        private List<ItemEntity> searchFleshList(){
            return
                    this.ghoul.getWorld().getEntitiesByClass(ItemEntity.class,
                    this.ghoul.getBoundingBox().expand(8.0, 2.0, 8.0), CAN_EAT);
        }
    }

    @Override
    public boolean isPushable() {
        if(this.getIsInvokingRegen()){
            return false;
        }
        return super.isPushable();
    }

    public static boolean canSpawnGhoul(EntityType<? extends Necrophage_Base> type, ServerWorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
        if(spawnReason == SpawnReason.SPAWNER){
            return world.getDifficulty() != Difficulty.PEACEFUL;
        } else {
            return world.getDifficulty() != Difficulty.PEACEFUL && HostileEntity.isSpawnDark(world, pos, random) && HostileEntity.canMobSpawn(type, world, spawnReason, pos, random) &&
                    pos.getY() >= 0;
        }
    }

    @Nullable
    @Override
    public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable NbtCompound entityNbt) {
        Random random = world.getRandom();
        if(!(spawnReason == SpawnReason.SPAWN_EGG) && !(spawnReason == SpawnReason.STRUCTURE)) {
            //Can spawn an Alghoul with it instead
            if (random.nextInt() % 5 == 0) {
                AlghoulEntity alghoul = TCOTS_Entities.ALGHOUL.create(this.getWorld());
                if (alghoul != null) {
                    alghoul.refreshPositionAndAngles(this.getX(), this.getY(), this.getZ(), this.getYaw(), 0.0f);
                    this.getWorld().spawnEntity(alghoul);
                }
            }
        }
        return super.initialize(world, difficulty, spawnReason, entityData, entityNbt);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        //Walk/Idle Controller
        controllerRegistrar.add(new AnimationController<>(this, "Idle/Walk/Run", 5, state -> {
            //If it's aggressive and it is moving
            if (this.isAttacking() && state.isMoving()) {
                state.setControllerSpeed(1.5f);
                return state.setAndContinue(RUNNING);
            }
            //It's not attacking and/or it's no moving
            else {
                //If it's attacking but NO moving
                if (isAttacking()) {
                    state.setControllerSpeed(1.5f);
                    return state.setAndContinue(RUNNING);
                } else {
                    //If it's just moving
                    if (state.isMoving()) {
                        state.setControllerSpeed(1f);
                        return state.setAndContinue(WALKING);
                    }
                    //Anything else
                    else {
                        state.setControllerSpeed(1f);
                        return state.setAndContinue(IDLE);
                    }

                }
            }
        }));

        //Attack Controller
        controllerRegistrar.add(
                new AnimationController<>(this, "AttackController", 1, state -> CommonControllers.animationTwoAttacksPredicate(state,this.handSwinging,random))
        );

        //Lunge Controller
        lungeAnimationController(this, controllerRegistrar);

        //RegenAnimation Controller
        controllerRegistrar.add(new AnimationController<>(this, "RegenController", 1, state -> PlayState.STOP)
                .triggerableAnim("start_regen", START_REGEN)
        );
    }

    boolean hasCooldownForRegen=false;

    public boolean hasCooldownForRegen() {
        return hasCooldownForRegen;
    }

    public void setHasCooldownForRegen(boolean hasCooldownForRegen) {
        this.hasCooldownForRegen = hasCooldownForRegen;
    }

    @Override
    public void handleStatus(byte status) {
        if (status == EntityStatuses.CREATE_EATING_PARTICLES) {
            for (int i = 0; i < 8; ++i) {
                Vec3d vec3d = new Vec3d(((double) this.random.nextFloat() - 0.5) * 0.1, Math.random() * 0.1 + 0.1, 0.0).rotateX(-this.getPitch() * ((float) Math.PI / 180)).rotateY(-this.getYaw() * ((float) Math.PI / 180));
                this.getWorld().addParticle(
                        new ItemStackParticleEffect(ParticleTypes.ITEM, Items.ROTTEN_FLESH.getDefaultStack()),
                        this.getX() + this.getRotationVector().x,
                        this.getY(),
                        this.getZ() + this.getRotationVector().z,
                        vec3d.x,
                        vec3d.y + 0.05,
                        vec3d.z);
            }
        } else {
            super.handleStatus(status);
        }
    }

    @Override
    public void tick() {
        if(getIsRegenerating() && MoonDustBomb.checkEffect(this)){
            setIsRegenerating(false);
        }

        this.tickLunge();

        if(getIsRegenerating() && (this.age%10==0) && this.getHealth() < this.getMaxHealth()){
            this.heal(1);
        }

        timersTick();

        super.tick();
    }

    private void timersTick(){
        if(this.getTimeForRegen() > 0){
            this.setTimeForRegen(getTimeForRegen()-1);
        } else if (getIsRegenerating()) {
            this.setIsRegenerating(false);
        }

        if(this.getCooldownForRegen()>0){
            this.setCooldownForRegen(this.getCooldownForRegen()-1);
        } else if (hasCooldownForRegen()) {
            this.setHasCooldownForRegen(false);
        }
    }

    public boolean cooldownBetweenLunges=false;
    @Override
    public boolean getNotCooldownBetweenLunges() {
        return !cooldownBetweenLunges;
    }

    @Override
    public void setCooldownBetweenLunges(boolean cooldownBetweenLunges) {
        this.cooldownBetweenLunges=cooldownBetweenLunges;
    }

    public boolean getIsRegenerating() {
        return this.dataTracker.get(REGENERATING);
    }

    public void setIsRegenerating(boolean isRegenerating) {
        this.dataTracker.set(REGENERATING, isRegenerating);
    }

    public boolean getIsInvokingRegen() {
        return this.dataTracker.get(INVOKING_REGENERATING);
    }

    public void setIsInvokingRegen(boolean isRegenerating) {
        this.dataTracker.set(INVOKING_REGENERATING, isRegenerating);
    }

    private int cooldownForRegen;

    public void setCooldownForRegen(int cooldownForRegen) {
        this.cooldownForRegen = cooldownForRegen;
    }

    public int getCooldownForRegen() {
        return cooldownForRegen;
    }

    protected void setTimeForRegen(int timeForRegen) {
        this.dataTracker.set(TIME_FOR_REGEN, timeForRegen);
    }

    protected int getTimeForRegen() {
        return this.dataTracker.get(TIME_FOR_REGEN);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        nbt.putBoolean("Regeneration",getIsRegenerating());
        nbt.putInt("CooldownRegen", getCooldownForRegen());
        nbt.putBoolean("CooldownRegenActive", hasCooldownForRegen);
        nbt.putInt("RegenerationTime", getTimeForRegen());
        super.writeCustomDataToNbt(nbt);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        this.setIsRegenerating(nbt.getBoolean("Regeneration"));
        this.setCooldownForRegen(nbt.getInt("CooldownRegen"));
        this.setTimeForRegen(nbt.getInt("RegenerationTime"));
        this.setHasCooldownForRegen(nbt.getBoolean("CooldownRegenActive"));
        super.readCustomDataFromNbt(nbt);
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return TCOTS_Sounds.GHOUL_HURT;
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return TCOTS_Sounds.GHOUL_DEATH;
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return TCOTS_Sounds.GHOUL_IDLE;
    }

    @Override
    public SoundEvent getLungeSound() {
        return TCOTS_Sounds.GHOUL_LUNGES;
    }

    @Override
    protected SoundEvent getAttackSound() {
        return TCOTS_Sounds.GHOUL_ATTACK;
    }

    protected SoundEvent getScreamSound() {
        return TCOTS_Sounds.GHOUL_SCREAMS;
    }

    public SoundEvent getRegeneratingSound(){
        return TCOTS_Sounds.GHOUL_REGEN;
    }

    public int LungeTicks;
    @Override
    public int getLungeTicks() {
        return LungeTicks;
    }

    @Override
    public void setLungeTicks(int lungeTicks) {
        LungeTicks=lungeTicks;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
