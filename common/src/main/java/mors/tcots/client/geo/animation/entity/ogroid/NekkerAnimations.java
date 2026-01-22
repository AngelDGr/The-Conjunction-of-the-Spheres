package mors.tcots.client.geo.animation.entity.ogroid;

import mors.tcots.entity.monsters.ogroids.NekkerEntity;
import mors.tcots.utils.GeckoAnimationsUtil;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.constant.DefaultAnimations;

public class NekkerAnimations {

    public static AnimationController<NekkerEntity> mainController(final NekkerEntity animatable) {
        return new AnimationController<>(animatable, state -> {

            state.setControllerSpeed(1);
            state.getController().transitionLength(5);

            if (state.getController().isPlayingTriggeredAnimation()) {
                state.setControllerSpeed(2);

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
                state.setControllerSpeed(state.getLimbSwingAmount() * 2f);
                return state.setAndContinue(DefaultAnimations.WALK);
            }

            return state.setAndContinue(DefaultAnimations.IDLE);
        }).receiveTriggeredAnimations()
                .triggerableAnim("attack1", GeckoAnimationsUtil.ATTACK1)
                .triggerableAnim("attack2", GeckoAnimationsUtil.ATTACK2);
    }

}
