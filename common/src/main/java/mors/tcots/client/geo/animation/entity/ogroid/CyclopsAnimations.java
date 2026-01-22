package mors.tcots.client.geo.animation.entity.ogroid;

import mors.tcots.entity.monsters.ogroids.CyclopsEntity;
import mors.tcots.utils.GeckoAnimationsUtil;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.constant.DefaultAnimations;

public class CyclopsAnimations {

    public static final RawAnimation LANDING = RawAnimation.begin().thenPlay("special.landing");

    public static AnimationController<CyclopsEntity> mainController(final CyclopsEntity animatable) {
        return new AnimationController<>(animatable, state -> {

            state.setControllerSpeed(1);
            state.getController().transitionLength(0);

            if (state.getController().isPlayingTriggeredAnimation()) {
                state.setControllerSpeed(2);
                if(state.getController().getTriggeredAnimation()!=null
                        && (state.getController().getTriggeredAnimation().equals(LANDING))) state.setControllerSpeed(1);
                return PlayState.CONTINUE;
            }

            //Move animations
            if(state.isMoving()){
                state.getController().transitionLength(0);
                //Run
                if (animatable.isAggressive()) {
                    state.setControllerSpeed(state.getLimbSwingAmount() * 2f);
                    return state.setAndContinue(DefaultAnimations.RUN);
                }

                //Default-Walk
                state.setControllerSpeed(state.getLimbSwingAmount() * 1.5f);
                return state.setAndContinue(DefaultAnimations.WALK);
            }

            state.getController().transitionLength(5);
            return state.setAndContinue(DefaultAnimations.IDLE);
        }).receiveTriggeredAnimations()
                .triggerableAnim("attack1", GeckoAnimationsUtil.ATTACK1)
                .triggerableAnim("attack2", GeckoAnimationsUtil.ATTACK2)
                .triggerableAnim("landing", LANDING);
    }
}
