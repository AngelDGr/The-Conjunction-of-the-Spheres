package mors.tcots.client.geo.animation.entity.necrophage;

import mors.tcots.entity.monsters.necrophages.FogletEntity;
import mors.tcots.utils.GeckoAnimationsUtil;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.constant.DefaultAnimations;

public class FogletAnimations {

    public static final RawAnimation TRIGGER_FOG = RawAnimation.begin().thenPlay("special.fog");

    public static AnimationController<FogletEntity> mainController(final FogletEntity animatable) {
        return new AnimationController<>(animatable, state -> {
            state.setControllerSpeed(1);
            state.getController().transitionLength(5);

            if (state.getController().isPlayingTriggeredAnimation()) {
                state.setControllerSpeed(2);
                if(state.getController().getTriggeredAnimation()!=null && state.getController().getTriggeredAnimation().equals(TRIGGER_FOG)) state.setControllerSpeed(1);

                return PlayState.CONTINUE;
            }

            if(state.isMoving()){
                state.getController().transitionLength(0);
                state.setControllerSpeed(state.getLimbSwingAmount() * 2f);
                return state.setAndContinue(DefaultAnimations.WALK);
            }

            state.setControllerSpeed(1f);
            return state.setAndContinue(DefaultAnimations.IDLE);
        }).receiveTriggeredAnimations()
                .triggerableAnim("attack1", GeckoAnimationsUtil.ATTACK1)
                .triggerableAnim("attack2", GeckoAnimationsUtil.ATTACK2)
                .triggerableAnim("fog", TRIGGER_FOG);
    }
}
