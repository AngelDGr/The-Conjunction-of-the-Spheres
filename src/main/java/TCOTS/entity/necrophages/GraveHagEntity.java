package TCOTS.entity.necrophages;

import TCOTS.sounds.TCOTS_Sounds;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.*;
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
import net.minecraft.sound.SoundEvent;
import net.minecraft.world.World;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

public class GraveHagEntity extends Necrophage_Base implements GeoEntity {

    //TODO: Add tongue attack
    //TODO: Add running attack
    //TODO: Add drops
    //TODO: Add spawn

    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    public static final RawAnimation IDLE = RawAnimation.begin().thenLoop("idle");
    public static final RawAnimation WALKING = RawAnimation.begin().thenLoop("move.walking");
    public static final RawAnimation ATTACK1 = RawAnimation.begin().thenPlay("attack.swing1");
    public static final RawAnimation ATTACK2 = RawAnimation.begin().thenPlay("attack.swing2");

    public static final RawAnimation ATTACK_TONGUE1 = RawAnimation.begin().thenPlay("attack.tongue1");
    public static final RawAnimation ATTACK_TONGUE2 = RawAnimation.begin().thenPlay("attack.tongue2");
    public static final RawAnimation ATTACK_RUN = RawAnimation.begin().thenPlay("attack.run");

    protected static final TrackedData<Boolean> TONGE_ATTACK = DataTracker.registerData(GraveHagEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    public GraveHagEntity(EntityType<? extends GraveHagEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void initGoals() {


        this.goalSelector.add(0, new SwimGoal(this));

//        this.goalSelector.add(1, new GraveHag_TongueAttack(this, 80, 5));

//        this.goalSelector.add(2, new MeleeAttackGoal(this, 1.2D, false));
        this.goalSelector.add(2, new GraveHag_MeleeAttack(this, 1.2D, false, 80));

        this.goalSelector.add(3, new WanderAroundGoal(this, 0.75, 20));

        this.goalSelector.add(4, new LookAroundGoal(this));

        //Objectives
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, PlayerEntity.class, true));
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, MerchantEntity.class, true));
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, IronGolemEntity.class, true));
    }

    boolean cooldownTongueAttack=false;
    int tongueAttackCooldownTicks;

    private static class GraveHag_MeleeAttack extends MeleeAttackGoal{
        private final GraveHagEntity graveHag;
        private final int timeBetweenAttacks;
        public GraveHag_MeleeAttack(GraveHagEntity mob, double speed, boolean pauseWhenMobIdle, int timeBetweenAttacks) {
            super(mob, speed, pauseWhenMobIdle);
            this.graveHag=mob;
            this.timeBetweenAttacks=timeBetweenAttacks;
        }

        int AnimationTicks = 5;

        boolean tongueTriggered=false;
        @Override
        public void tick() {
            LivingEntity livingEntity = this.mob.getTarget();
            double d = this.mob.squaredDistanceTo(livingEntity);
            double d2 = this.mob.getSquaredDistanceToAttackPosOf(livingEntity);

            if(!graveHag.cooldownTongueAttack && AnimationTicks==5 && d < 20){
                graveHag.getNavigation().stop();
                this.mob.getLookControl().lookAt(livingEntity, 30.0f, 30.0f);
                graveHag.setTongueAttack(true);
                graveHag.tryAttack(livingEntity);

                graveHag.playSound(TCOTS_Sounds.GRAVE_HAG_TONGUE_ATTACK, 1, 1);

                --AnimationTicks;


                tongueTriggered=true;
            }



            if (AnimationTicks > 0 && !graveHag.cooldownTongueAttack && tongueTriggered) {
                System.out.println("AnimationTicks: " + AnimationTicks);
                graveHag.getNavigation().stop();
                --AnimationTicks;
            } else if(AnimationTicks==0){
                AnimationTicks = 5;
                tongueTriggered=false;
                graveHag.setTongueAttack(false);
                graveHag.tongueAttackCooldownTicks = timeBetweenAttacks;
                graveHag.cooldownTongueAttack = true;
            }

            super.tick();

        }
    }


    public static DefaultAttributeContainer.Builder setAttributes() {
        return AnimalEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 26.0D)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 4.0f) //Amount of health that hurts you
//                .add(EntityAttributes.GENERIC_ATTACK_SPEED, 2.0f)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.20f)
        .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 0.5);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(TONGE_ATTACK, Boolean.FALSE);
    }

    public final boolean getTongueAttack() {
        return this.dataTracker.get(TONGE_ATTACK);
    }

    public final void setTongueAttack(boolean wasAttacking) {
        this.dataTracker.set(TONGE_ATTACK, wasAttacking);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        //Walk/Idle Controller
        controllers.add(new AnimationController<>(this, "Idle/Walk", 5, state -> {
            //If it's moving
            if (state.isMoving()) {
                return state.setAndContinue(WALKING);
            }
            //Anything else
            else {
                return state.setAndContinue(IDLE);
            }

        }));

        //Attack Controller
        controllers.add(
                new AnimationController<>(this, "AttackController", 1, state -> {
                    state.getController().forceAnimationReset();
                    // Random instance
                    // Generates two random numbers
                    if (this.handSwinging && !this.getTongueAttack()) {
                        int r = GraveHagEntity.this.random.nextInt(2);
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

        //TongueAttack Controller
        controllers.add(new AnimationController<>(this, "TongueAttack", 1, state -> {

            if (this.getTongueAttack()) {

                return state.setAndContinue(ATTACK_TONGUE2);
            }

            state.getController().forceAnimationReset();
            return PlayState.CONTINUE;
        }));
    }


    @Override
    public void tick() {
        if (GraveHagEntity.this.tongueAttackCooldownTicks > 0) {
            System.out.println(tongueAttackCooldownTicks);
            --GraveHagEntity.this.tongueAttackCooldownTicks;
        } else {
//            System.out.println("Cooldown restored");
//            System.out.println(tongueAttackCooldownTicks);
            GraveHagEntity.this.cooldownTongueAttack = false;
        }

        super.tick();
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
