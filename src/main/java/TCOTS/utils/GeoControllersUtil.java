package TCOTS.utils;

import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animation.Animation;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.animation.PlayState;

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

    public static <T extends GeoAnimatable> AnimationController<?> attackController(T animatable, int attacks){
        if(attacks==2){
            return new AnimationController<>(animatable, "AttackController", 1, state -> PlayState.STOP)
                    .triggerableAnim("attack1", GeoControllersUtil.ATTACK1)
                    .triggerableAnim("attack2", GeoControllersUtil.ATTACK2);
        } else {
            return new AnimationController<>(animatable, "AttackController", 1, state -> PlayState.STOP)
                    .triggerableAnim("attack1", GeoControllersUtil.ATTACK1)
                    .triggerableAnim("attack2", GeoControllersUtil.ATTACK2)
                    .triggerableAnim("attack3", GeoControllersUtil.ATTACK3);
        }
    }

    public static float getLimbSwing(AnimationState<?> animationState, float min, float max, float speed, float increase, boolean negative){
        return (float) MathHelper.clamp((negative? -1: 1)*((Math.sin(animationState.getLimbSwing()*speed)*(animationState.getLimbSwingAmount()*increase))), min, max);
    }

    /**
    Generic idle controller
     */
    public static <T extends GeoAnimatable> AnimationController<?> genericIdleController(T animatable){
        return
        new AnimationController<>(animatable, "IdleController", 0, state ->{
            state.getController().setAnimation(RawAnimation.begin().then("idle", Animation.LoopType.LOOP));
            return PlayState.CONTINUE;
        });
    }

    @SuppressWarnings("unused")
    public static boolean isThirdPerson(@NotNull AnimationState<?> state){
        return state.getData(DataTickets.ITEM_RENDER_PERSPECTIVE).getIndex()==1 || state.getData(DataTickets.ITEM_RENDER_PERSPECTIVE).getIndex()==2;
    }

    @SuppressWarnings("unused")
    public static boolean isFirstPerson(@NotNull AnimationState<?> state){
        return state.getData(DataTickets.ITEM_RENDER_PERSPECTIVE).isFirstPerson();
    }

    @SuppressWarnings("unused")
    public static boolean inInventory(@NotNull AnimationState<?> state){
        return state.getData(DataTickets.ITEM_RENDER_PERSPECTIVE).getIndex()==6;
    }

}
