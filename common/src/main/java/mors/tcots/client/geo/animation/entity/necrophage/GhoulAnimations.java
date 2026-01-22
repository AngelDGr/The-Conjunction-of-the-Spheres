package mors.tcots.client.geo.animation.entity.necrophage;

import mors.tcots.entity.monsters.necrophages.GhoulEntity;
import mors.tcots.utils.GeckoAnimationsUtil;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.constant.DefaultAnimations;

public class GhoulAnimations {

    public static final RawAnimation START_REGEN = RawAnimation.begin().thenPlay("special.regen");
    public static final RawAnimation SCREAM = RawAnimation.begin().thenPlay("special.scream");

    public static AnimationController<GhoulEntity> mainController(final GhoulEntity animatable) {
        return new AnimationController<>(animatable, state -> {
            final double speed = animatable.getDeltaMovement().length();

            state.setControllerSpeed(1);
            state.getController().transitionLength(5);

            if (state.getController().isPlayingTriggeredAnimation()) {
                state.setControllerSpeed(2);
                if(state.getController().getTriggeredAnimation()!=null && state.getController().getTriggeredAnimation().equals(START_REGEN)) state.setControllerSpeed(0.5f);

                return PlayState.CONTINUE;
            }

            //Move animations
            if(state.isMoving()){
                state.getController().transitionLength(0);
                //Run
                if (speed > 0.2 && animatable.onGround()) {
                    state.setControllerSpeed(state.getLimbSwingAmount() * 2f);
                    return state.setAndContinue(DefaultAnimations.RUN);
                }

                //Default-Walk
                state.setControllerSpeed(state.getLimbSwingAmount() * 1.5f);
                return state.setAndContinue(DefaultAnimations.WALK);
            }

            return state.setAndContinue(DefaultAnimations.IDLE);
        }).receiveTriggeredAnimations()
                .triggerableAnim("attack1", GeckoAnimationsUtil.ATTACK1)
                .triggerableAnim("attack2", GeckoAnimationsUtil.ATTACK2)
                .triggerableAnim("start_regen", START_REGEN)
                .triggerableAnim("scream", SCREAM);
    }
}
