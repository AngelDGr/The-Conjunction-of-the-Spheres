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
import net.minecraft.sound.SoundEvent;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;

public class GhoulEntity extends Necrophage_Base implements GeoEntity, LungeMob {

    //TODO: Add new combat/regeneration
    //TODO: Add monster nests
    //TODO: Add Ghoul's Blood
    
    private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    public static final RawAnimation IDLE = RawAnimation.begin().thenLoop("idle");
    public static final RawAnimation RUNNING = RawAnimation.begin().thenLoop("move.running");
    public static final RawAnimation WALKING = RawAnimation.begin().thenLoop("move.walking");

    protected static final TrackedData<Boolean> LUGGING = DataTracker.registerData(GhoulEntity.class, TrackedDataHandlerRegistry.BOOLEAN);


    public GhoulEntity(EntityType<? extends PathAwareEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(LUGGING, Boolean.FALSE);
    }

    @Override
    protected void initGoals() {
        //Attack
        //Emerge from ground
        this.goalSelector.add(1, new SwimGoal(this));

        this.goalSelector.add(2, new LungeAttackGoal(this, 100, 1.2,10,30));

        this.goalSelector.add(3, new MeleeAttackGoal(this,1.2D, false));

        this.goalSelector.add(5, new WanderAroundGoal(this, 0.75f,80));

        this.goalSelector.add(6, new LookAroundGoal(this));

        //Objectives
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, PlayerEntity.class, true));
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, MerchantEntity.class, true));
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, IronGolemEntity.class, true));
        this.targetSelector.add(3, new ActiveTargetGoal<>(this, ZombieEntity.class, true));
        this.targetSelector.add(3, new ActiveTargetGoal<>(this, ZoglinEntity.class, true));
        this.targetSelector.add(3, new ActiveTargetGoal<>(this, ZombieHorseEntity.class, true));

        this.targetSelector.add(4, new ActiveTargetGoal<>(this, HoglinEntity.class, true));
        this.targetSelector.add(5, new ActiveTargetGoal<>(this, CowEntity.class, true));
        this.targetSelector.add(5, new ActiveTargetGoal<>(this, PigEntity.class, true));
        this.targetSelector.add(5, new ActiveTargetGoal<>(this, SheepEntity.class, true));
        this.targetSelector.add(6, new ActiveTargetGoal<>(this, GoatEntity.class, true));
    }

    public static DefaultAttributeContainer.Builder setAttributes() {
        return AnimalEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 15.0D)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 4.0f) //Amount of health that hurts you
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.29f);
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
        controllerRegistrar.add(
                new AnimationController<>(this, "LungeController", 0, this::animationLungePredicate)
        );
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public void tick() {

        this.tickLunge();

        super.tick();
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

    @Override
    public boolean getIsLugging() {
        return this.dataTracker.get(LUGGING);
    }

    @Override
    public void setIsLugging(boolean wasLugging) {
        this.dataTracker.set(LUGGING, wasLugging);
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
}
