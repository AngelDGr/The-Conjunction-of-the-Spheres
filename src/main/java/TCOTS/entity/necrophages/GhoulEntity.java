package TCOTS.entity.necrophages;

import TCOTS.entity.goals.LungeAttackGoal;
import TCOTS.entity.interfaces.LungeMob;
import TCOTS.entity.misc.CommonControllers;
import TCOTS.sounds.TCOTS_Sounds;
import net.minecraft.entity.EntityType;
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
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundEvent;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

public class GhoulEntity extends Necrophage_Base implements GeoEntity, LungeMob {

    //xTODO: Add new combat/regeneration
    //xTODO: Add Ghoul's Blood
    //TODO: Add monster nests & spawn
    public static final byte GHOUL_REGENERATING = 99;

    public static final int GHOUL_REGENERATION_TIME=200;
    private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    public static final RawAnimation IDLE = RawAnimation.begin().thenLoop("idle");
    public static final RawAnimation RUNNING = RawAnimation.begin().thenLoop("move.running");
    public static final RawAnimation WALKING = RawAnimation.begin().thenLoop("move.walking");
    public static final RawAnimation START_REGEN = RawAnimation.begin().thenPlay("special.regen");

    protected static final TrackedData<Boolean> LUGGING = DataTracker.registerData(GhoulEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    protected static final TrackedData<Boolean> REGENERATING = DataTracker.registerData(GhoulEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    protected static final TrackedData<Integer> TIME_FOR_REGEN = DataTracker.registerData(GhoulEntity.class, TrackedDataHandlerRegistry.INTEGER);


    public GhoulEntity(EntityType<? extends PathAwareEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(LUGGING, Boolean.FALSE);
        this.dataTracker.startTracking(REGENERATING, Boolean.FALSE);
        this.dataTracker.startTracking(TIME_FOR_REGEN, 0);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));

        this.goalSelector.add(1, new GhoulRegeneration(this, 60, 400, GHOUL_REGENERATION_TIME,
                0.5f));

        this.goalSelector.add(2, new LungeAttackGoal(this, 100, 1.2,10,30));

        this.goalSelector.add(3, new Ghoul_MeleeAttackGoal(this, 1.2D, false));

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

    private static class GhoulRegeneration extends Goal {
        private final int ticksAttackedBeforeRegen;
        private final int CooldownBetweenRegens;
        private final int TimeForRegen;
        private final GhoulEntity mob;

        private final float healthPercentage;

        public GhoulRegeneration(GhoulEntity mob, int ticksAttackedBeforeRegen, int CooldownBetweenRegens, int TimeForRegen, float HealthPercentageToStart) {
            this.ticksAttackedBeforeRegen = ticksAttackedBeforeRegen;
            this.mob=mob;
            this.CooldownBetweenRegens=CooldownBetweenRegens;
            this.TimeForRegen=TimeForRegen;
            this.healthPercentage=HealthPercentageToStart;
        }

        @Override
        public boolean canStart() {
            return canStartRegen();
        }

        @Override
        public void start() {
            mob.getNavigation().stop();
            mob.playSound(TCOTS_Sounds.GHOUL_SCREAMS,1,1);
            mob.triggerAnim("RegenController", "start_regen");
        }

        @Override
        public void stop() {
            this.mob.setIsRegenerating(true);
            mob.setTimeForRegen(TimeForRegen);
            if (!this.mob.isSilent()) {
                this.mob.getWorld().sendEntityStatus(this.mob, GHOUL_REGENERATING);
            }

            mob.setHasCooldownForRegen(true);
            mob.setCooldownForRegen(CooldownBetweenRegens);

            StoppedTicks = 35;
        }

        int StoppedTicks=35;
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

        private boolean canStartRegen(){
            return ((mob.getLastAttackedTime() + ticksAttackedBeforeRegen) < mob.age) && !(mob.getIsRegenerating())
                    && (mob.getHealth() < (mob.getMaxHealth() * healthPercentage)) && mob.isOnGround() && !(mob.hasCooldownForRegen());
        }
    }

    private static class Ghoul_MeleeAttackGoal extends MeleeAttackGoal {

        public Ghoul_MeleeAttackGoal(PathAwareEntity mob, double speed, boolean pauseWhenMobIdle) {
            super(mob, speed, pauseWhenMobIdle);
        }

        @Override
        public boolean shouldContinue() {
            return super.shouldContinue();
        }

        @Override
        public boolean canStart() {
            return super.canStart();
        }
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
    public void tick() {
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

    @Override
    public boolean isInvulnerableTo(DamageSource damageSource) {
        return super.isInvulnerableTo(damageSource);
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

    private int cooldownForRegen;

    public void setCooldownForRegen(int cooldownForRegen) {
        this.cooldownForRegen = cooldownForRegen;
    }

    public int getCooldownForRegen() {
        return cooldownForRegen;
    }

    public void setTimeForRegen(int timeForRegen) {
        this.dataTracker.set(TIME_FOR_REGEN, timeForRegen);
    }

    public int getTimeForRegen() {
        return this.dataTracker.get(TIME_FOR_REGEN);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        nbt.putBoolean("Regeneration",this.dataTracker.get(REGENERATING));
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
