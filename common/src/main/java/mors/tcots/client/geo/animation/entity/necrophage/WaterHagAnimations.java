package mors.tcots.client.geo.animation.entity.necrophage;

import mors.tcots.entity.monsters.necrophages.WaterHagEntity;
import mors.tcots.utils.GeckoAnimationsUtil;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.constant.DefaultAnimations;

public class WaterHagAnimations {

    public static final RawAnimation ATTACK_MUD = RawAnimation.begin().thenPlay("attack.mud_launch");

    public static AnimationController<WaterHagEntity> mainController(final WaterHagEntity animatable) {
        return new AnimationController<>(animatable, state -> {
            state.setControllerSpeed(1);
            state.getController().transitionLength(0);

            if (state.getController().isPlayingTriggeredAnimation()) {
                state.getController().transitionLength(5);
                state.setControllerSpeed(2);
                return PlayState.CONTINUE;
            }

            if(state.isMoving()){
                state.setControllerSpeed(state.getLimbSwingAmount() * (animatable.isAggressive()? 3f: 2f));
                return state.setAndContinue(DefaultAnimations.WALK);
            }

            state.setControllerSpeed(1f);
            state.getController().transitionLength(20);
            return state.setAndContinue(DefaultAnimations.IDLE);
        }).receiveTriggeredAnimations()
                .triggerableAnim("attack1", GeckoAnimationsUtil.ATTACK1)
                .triggerableAnim("attack2", GeckoAnimationsUtil.ATTACK2)
                .triggerableAnim("mud_attack", ATTACK_MUD);
    }
}
