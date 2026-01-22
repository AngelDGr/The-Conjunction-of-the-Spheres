package mors.tcots.client.geo.animation.entity.necrophage;

import mors.tcots.entity.monsters.necrophages.GraveHagEntity;
import mors.tcots.utils.GeckoAnimationsUtil;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.constant.DefaultAnimations;

public class GraveHagAnimations {

    public static final RawAnimation ATTACK_TONGUE = RawAnimation.begin().thenPlay("attack.tongue2");

    public static AnimationController<GraveHagEntity> mainController(final GraveHagEntity animatable) {
        return new AnimationController<>(animatable, state -> {
            state.setControllerSpeed(1);
            state.getController().transitionLength(0);

            if (state.getController().isPlayingTriggeredAnimation()) {
                state.getController().transitionLength(5);
                state.setControllerSpeed(2);
                return PlayState.CONTINUE;
            }

            if(state.isMoving()){
                if(animatable.isSprinting()){
                    state.setControllerSpeed(state.getLimbSwingAmount() * 3);
                    return state.setAndContinue(DefaultAnimations.RUN);
                }

                state.setControllerSpeed(state.getLimbSwingAmount() * (animatable.isAggressive()? 3f: 2f));
                return state.setAndContinue(DefaultAnimations.WALK);
            }

            state.setControllerSpeed(1f);
            state.getController().transitionLength(5);
            return state.setAndContinue(DefaultAnimations.IDLE);
        }).receiveTriggeredAnimations()
                .triggerableAnim("attack1", GeckoAnimationsUtil.ATTACK1)
                .triggerableAnim("attack2", GeckoAnimationsUtil.ATTACK2)
                .triggerableAnim("tongue", ATTACK_TONGUE);
    }
}
