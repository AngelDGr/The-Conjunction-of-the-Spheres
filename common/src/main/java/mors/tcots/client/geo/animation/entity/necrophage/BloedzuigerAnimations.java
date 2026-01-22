package mors.tcots.client.geo.animation.entity.necrophage;

import mors.tcots.entity.monsters.necrophages.BloedzuigerEntity;
import mors.tcots.utils.GeckoAnimationsUtil;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.constant.DefaultAnimations;

public class BloedzuigerAnimations {

    public static final RawAnimation EXPLOSION = RawAnimation.begin().thenPlayAndHold("special.explosion");

    public static AnimationController<BloedzuigerEntity> mainController(final BloedzuigerEntity animatable) {
        return new AnimationController<>(animatable, state -> {
            state.setControllerSpeed(1);
            state.getController().transitionLength(0);

            if (state.getController().isPlayingTriggeredAnimation()) {
                state.setControllerSpeed(2);
                if(state.getController().getTriggeredAnimation()!=null && state.getController().getTriggeredAnimation().equals(EXPLOSION)) state.setControllerSpeed(1);
                return PlayState.CONTINUE;
            }

            //Move animations
            if(state.isMoving()){
                //Run
                if (animatable.isAggressive()) {
                    state.setControllerSpeed(state.getLimbSwingAmount() * 3f);
                    return state.setAndContinue(DefaultAnimations.RUN);
                }

                //Default-Walk
                state.setControllerSpeed(state.getLimbSwingAmount() * 2f);
                return state.setAndContinue(DefaultAnimations.WALK);
            }

            state.getController().transitionLength(5);
            return state.setAndContinue(DefaultAnimations.IDLE);
        }).receiveTriggeredAnimations()
                .triggerableAnim("attack1", GeckoAnimationsUtil.ATTACK1)
                .triggerableAnim("attack2", GeckoAnimationsUtil.ATTACK2)
                .triggerableAnim("explosion", EXPLOSION);
    }
}
