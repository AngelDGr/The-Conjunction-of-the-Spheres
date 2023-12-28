package TCOTS.entity.necrophages;

import net.minecraft.entity.EntityType;

import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;


import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.ZombieEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.constant.DefaultAnimations;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.*;
import software.bernie.geckolib.core.object.PlayState;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class DrownerEntity extends HostileEntity implements GeoEntity {


//    public static final EntityDataAccessor<Boolean> ATTACKING = SynchedEntityData.defineId(YetiEntity.class, EntityDataSerializers.BOOLEAN);
    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    public DrownerEntity(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }

    public static DefaultAttributeContainer.Builder setAttributes() {
        return AnimalEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 20.0D)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 4.0f) //Amount of health that makes you
//                .add(EntityAttributes.GENERIC_ATTACK_SPEED, 2.0f)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.27f);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(1, new SwimGoal(this));
        this.goalSelector.add(2, new MeleeAttackGoal(this, 1.2D, false));

        this.goalSelector.add(3, new WanderAroundFarGoal(this, 0.75f, 1));

        this.goalSelector.add(4, new LookAroundGoal(this));

        this.targetSelector.add(2, new ActiveTargetGoal<>(this, PlayerEntity.class, true));
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, MerchantEntity.class, true));
//        this.targetSelector.add(3, new ActiveTargetGoal<>(this, ChickenEntity.class, true));
    }

    //Each Controll only can play an animation at the same time
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "Idle/Walk", 5,
                state->{
                    //If it's aggressive and it is moving
                    if(this.isAttacking() && state.isMoving()){
                        state.setAndContinue(RawAnimation.begin().thenLoop("move.running"));
//                        return PlayState.CONTINUE;
                    }
                    //It's not attacking and/or it's no moving
                    else{
                        //If it's attacking but NO moving
                        if(isAttacking()){
                            state.setAndContinue(RawAnimation.begin().thenLoop("move.running"));
                        }
                        else {
                            //If it's just moving
                            if (state.isMoving()) {
                                state.setAndContinue(RawAnimation.begin().thenLoop("move.walking"));
                            }
                            //Anything else
                            else {
                                state.setAndContinue(RawAnimation.begin().thenLoop("idle"));
                            }
//                        }
                        }
                    }
                    return PlayState.CONTINUE;
                }
//                this::predicate
        ));

        //Attack Controller
        controllerRegistrar.add(
         new AnimationController<>(this, "AttackController", 1, state -> {
            if (this.handSwinging){
                // Random instance
                Random random = new Random();
                // Generates two random numbers
                int r = ThreadLocalRandom.current().nextInt(1, 3);

                if(r==1){
                    state.setAndContinue(RawAnimation.begin().thenPlay("attack.swing1"));
                    state.getController().forceAnimationReset();
                    return PlayState.CONTINUE;
                }
                else{
                    state.setAndContinue(RawAnimation.begin().thenPlay("attack.swing2"));
                    state.getController().forceAnimationReset();
                    return PlayState.CONTINUE;
                }
            }
             return PlayState.CONTINUE;
            })
        );
    }

//    private <T extends GeoAnimatable> PlayState predicate(AnimationState<T> tAnimationState) {
//        if(this.isAttacking() && tAnimationState.isMoving()){
//            tAnimationState.getController().setAnimation(RawAnimation.begin().then("move.running", Animation.LoopType.LOOP));
//            return PlayState.CONTINUE;
//        }
//        else{
//            if(tAnimationState.isMoving()) {
//                tAnimationState.getController().setAnimation(RawAnimation.begin().then("move.walking", Animation.LoopType.LOOP));
//                return PlayState.CONTINUE;
//            }
//
//        }
//
//        tAnimationState.getController().setAnimation(RawAnimation.begin().then("idle", Animation.LoopType.LOOP));
//        return PlayState.CONTINUE;
//    }
//    }


    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
