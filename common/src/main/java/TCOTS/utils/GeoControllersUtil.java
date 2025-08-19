package TCOTS.utils;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
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

    public static <T extends GeoAnimatable> PlayState idleWalkRunController(final AnimationState<T> state){
        //If it's aggressive and it is moving
        final Mob entity = (Mob) state.getAnimatable();
        if (entity.isAggressive() && state.isMoving()) {
            return state.setAndContinue(RUNNING);
        }
        //It's not attacking and/or it's no moving
        else {
            //If it's attacking but NO moving
            if (entity.isAggressive()) {
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

    public static <T extends GeoAnimatable> AnimationController<?> attackController(final T animatable, final int attacks){
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

    public static float getLimbSwing(final AnimationState<?> animationState, final float min, final float max, final float speed, final float increase, final boolean negative){
        return (float) Mth.clamp((negative? -1: 1)*((Math.sin(animationState.getLimbSwing()*speed)*(animationState.getLimbSwingAmount()*increase))), min, max);
    }

    /**
    Generic idle controller
     */
    public static <T extends GeoAnimatable> AnimationController<?> genericIdleController(final T animatable){
        return
        new AnimationController<>(animatable, "IdleController", 0, state ->{
            state.getController().setAnimation(RawAnimation.begin().then("idle", Animation.LoopType.LOOP));
            return PlayState.CONTINUE;
        });
    }

    @SuppressWarnings("unused")
    public static boolean isThirdPerson(@NotNull final AnimationState<?> state){
        return state.getData(DataTickets.ITEM_RENDER_PERSPECTIVE).getId()==1 || state.getData(DataTickets.ITEM_RENDER_PERSPECTIVE).getId()==2;
    }

    @SuppressWarnings("unused")
    public static boolean isFirstPerson(@NotNull final AnimationState<?> state){
        return state.getData(DataTickets.ITEM_RENDER_PERSPECTIVE).firstPerson();
    }

    @SuppressWarnings("unused")
    public static boolean inInventory(@NotNull final AnimationState<?> state){
        return state.getData(DataTickets.ITEM_RENDER_PERSPECTIVE).getId()==6;
    }

}
