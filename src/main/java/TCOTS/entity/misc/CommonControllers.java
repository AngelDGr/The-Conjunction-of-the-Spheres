package TCOTS.entity.misc;

import TCOTS.entity.necrophages.DevourerEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

public class CommonControllers {

    public static final RawAnimation ATTACK1 = RawAnimation.begin().thenPlay("attack.swing1");
    public static final RawAnimation ATTACK2 = RawAnimation.begin().thenPlay("attack.swing2");
    public static final RawAnimation ATTACK3 = RawAnimation.begin().thenPlay("attack.swing3");

    public static  <T extends GeoAnimatable> PlayState animationTwoAttacksPredicate(AnimationState<T> state, boolean handSwinging, Random random) {
        state.getController().forceAnimationReset();
        // Random instance
        // Generates two random numbers
        if (handSwinging){
            int r = random.nextInt(2);
            if (r == 1) {
                return state.setAndContinue(ATTACK1);
            } else {
                return state.setAndContinue(ATTACK2);
            }
        }
        return PlayState.CONTINUE;
    }

    public static  <T extends GeoAnimatable> PlayState animationThreeAttacksPredicate(AnimationState<T> state, boolean handSwinging, Random random) {
        state.getController().forceAnimationReset();
        // Random instance
        // Generates three random numbers
        if (handSwinging) {
            int r = random.nextInt(3);
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
    }

    public static <T extends GeoAnimatable> PlayState idleWalkRun(AnimationState<T> state, MobEntity entity, RawAnimation RUNNING, RawAnimation WALKING, RawAnimation IDLE){
        //If it's aggressive and it is moving
        if (entity.isAttacking() && state.isMoving()) {
            return state.setAndContinue(RUNNING);
        }
        //It's not attacking and/or it's no moving
        else {
            //If it's attacking but NO moving
            if (entity.isAttacking()) {
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

    public static <T extends GeoAnimatable> PlayState idleWalkRun(AnimationState<T> state, MobEntity entity, RawAnimation RUNNING, RawAnimation WALKING, RawAnimation IDLE,
                                                                  float runningSpeed, float walkingSpeed){
        //If it's aggressive and it is moving
        if (entity.isAttacking() && state.isMoving()) {
            state.setControllerSpeed(runningSpeed);
            return state.setAndContinue(RUNNING);
        }
        //It's not attacking and/or it's no moving
        else {
            //If it's attacking but NO moving
            if (entity.isAttacking()) {
                return state.setAndContinue(RUNNING);
            } else {
                //If it's just moving
                if (state.isMoving()) {
                    state.setControllerSpeed(walkingSpeed);
                    return state.setAndContinue(WALKING);
                }
                //Anything else
                else {
                    state.setControllerSpeed(walkingSpeed);
                    return state.setAndContinue(IDLE);
                }
            }
        }
    }


    public static float getLimbSwing(AnimationState<?> animationState, float min, float max, float speed, float increase, boolean negative){
        return (float) MathHelper.clamp((negative? -1: 1)*((Math.sin(animationState.getLimbSwing()*speed)*(animationState.getLimbSwingAmount()*increase))), min, max);
    }



}
