package TCOTS.utils;

import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.math.MathHelper;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

public class GeoControllersUtil {

    public static final RawAnimation ATTACK1 = RawAnimation.begin().thenPlay("attack.swing1");
    public static final RawAnimation ATTACK2 = RawAnimation.begin().thenPlay("attack.swing2");
    public static final RawAnimation ATTACK3 = RawAnimation.begin().thenPlay("attack.swing3");

    public static final RawAnimation IDLE = RawAnimation.begin().thenLoop("idle");
    public static final RawAnimation RUNNING = RawAnimation.begin().thenLoop("move.running");
    public static final RawAnimation WALKING = RawAnimation.begin().thenLoop("move.walking");

    public static <T extends GeoAnimatable> PlayState idleWalkRunController(AnimationState<T> state){
        //If it's aggressive and it is moving
        MobEntity entity = (MobEntity) state.getAnimatable();
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


    public static float getLimbSwing(AnimationState<?> animationState, float min, float max, float speed, float increase, boolean negative){
        return (float) MathHelper.clamp((negative? -1: 1)*((Math.sin(animationState.getLimbSwing()*speed)*(animationState.getLimbSwingAmount()*increase))), min, max);
    }

}
