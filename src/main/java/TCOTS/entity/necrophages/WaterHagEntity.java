package TCOTS.entity.necrophages;

import TCOTS.sounds.TCOTS_Sounds;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

import java.util.EnumSet;
import java.util.List;

public class WaterHagEntity extends Necrophage_Base implements GeoEntity {

    //TODO: Add proper sounds
    //TODO: Add mud ball attack
    //TODO: Add drops (Water Hag mutagen)
        //TODO: Add Water Hag Decoction (Damage increased when full health)
    //TODO: Add spawn


    private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    public static final RawAnimation IDLE = RawAnimation.begin().thenLoop("idle");
    public static final RawAnimation WALKING = RawAnimation.begin().thenLoop("move.walking");
    public static final RawAnimation ATTACK1 = RawAnimation.begin().thenPlay("attack.swing1");
    public static final RawAnimation ATTACK2 = RawAnimation.begin().thenPlay("attack.swing2");

    public static final RawAnimation ATTACK_MUD = RawAnimation.begin().thenPlay("attack.mud_launch");

    public WaterHagEntity(EntityType<? extends WaterHagEntity> entityType, World world) {
        super(entityType, world);
        this.experiencePoints = 10;
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));

        this.goalSelector.add(2, new WaterHag_MeleeAttack(this, 1.2D, false));

        this.goalSelector.add(3, new WanderAroundGoal(this, 0.75, 20));

        this.goalSelector.add(4, new LookAroundGoal(this));

        //Objectives
        this.targetSelector.add(1, new RevengeGoal(this, new Class[0]));
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, PlayerEntity.class, true));
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, MerchantEntity.class, true));
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, IronGolemEntity.class, true));
    }

    private static class WaterHag_MeleeAttack extends MeleeAttackGoal{

        public WaterHag_MeleeAttack(WaterHagEntity mob, double speed, boolean pauseWhenMobIdle) {
            super(mob, speed, pauseWhenMobIdle);
        }
    }

    public static DefaultAttributeContainer.Builder setAttributes() {
        return AnimalEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 20.0D)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 2.0f) //Amount of health that hurts you
//                .add(EntityAttributes.GENERIC_ATTACK_SPEED, 2.0f)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.20f)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 0.5)
                .add(EntityAttributes.GENERIC_ARMOR,2f);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        //Walk/Idle Controller
        controllers.add(new AnimationController<>(this, "Idle/Walk/Run", 5, state -> {

            //If it's moving
            if (state.isMoving() && this.isAttacking()) {
                state.setControllerSpeed(0.8f);
                return state.setAndContinue(WALKING);
            } else if (state.isMoving()) {
                state.setControllerSpeed(0.5f);
                return state.setAndContinue(WALKING);
            }
            //Anything else
            else {
                state.setControllerSpeed(0.8f);
                return state.setAndContinue(IDLE);
            }

        }));

        //Attack Controller
        controllers.add(
                new AnimationController<>(this, "AttackController", 1, state -> {
                    state.getController().forceAnimationReset();
                    // Random instance
                    // Generates two random numbers
                    if (this.handSwinging) {
                        int r = WaterHagEntity.this.random.nextInt(2);
                        switch (r) {
                            case 0:
                                return state.setAndContinue(ATTACK1);

                            case 1:
                                return state.setAndContinue(ATTACK2);
                        }
                    }

                    return PlayState.CONTINUE;
                })
        );
    }


    //Water creature things
    @Override
    public boolean canBreatheInWater() {
        return true;
    }
    @Override
    public boolean isPushedByFluids() {
        return false;
    }
    //Speed on water
    @Override
    protected float getBaseMovementSpeedMultiplier() {
        return 0.91F;
    }

    //Sounds
    @Override
    protected SoundEvent getAmbientSound() {
        return TCOTS_Sounds.GRAVE_HAG_IDLE;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return TCOTS_Sounds.GRAVE_HAG_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return TCOTS_Sounds.GRAVE_HAG_DEATH;
    }

    //Footsteps sounds
    protected SoundEvent getStepSound() {
        return TCOTS_Sounds.DROWNER_FOOTSTEP;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(this.getStepSound(), 0.15F, 1.0F);
    }

    //Attack Sound
    @Override
    public boolean tryAttack(Entity target) {
        this.playSound(TCOTS_Sounds.GRAVE_HAG_ATTACK, 1.0F, 1.0F);
        return super.tryAttack(target);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
