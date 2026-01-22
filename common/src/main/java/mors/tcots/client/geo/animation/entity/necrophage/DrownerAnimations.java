package mors.tcots.client.geo.animation.entity.necrophage;

import mors.tcots.entity.monsters.necrophages.DrownerEntity;
import mors.tcots.utils.GeckoAnimationsUtil;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.constant.DefaultAnimations;

public class DrownerAnimations {

    public static final RawAnimation WATER_IDLE = RawAnimation.begin().thenLoop("misc.idle_water");
    public static final RawAnimation WATER_ATTACK1 = RawAnimation.begin().thenPlay("attack.water_swing1");
    public static final RawAnimation WATER_ATTACK2 = RawAnimation.begin().thenPlay("attack.water_swing2");

    public static AnimationController<DrownerEntity> mainController(final DrownerEntity animatable) {
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
                //Swim
                if(animatable.getSwimmingDataTracker()){
                    state.setControllerSpeed(1);
                    return state.setAndContinue(DefaultAnimations.SWIM);
                }

                //Run
                if (animatable.isAggressive()) {
                    state.setControllerSpeed(state.getLimbSwingAmount() * 2f);
                    return state.setAndContinue(DefaultAnimations.RUN);
                }

                //Default-Walk
                state.setControllerSpeed(state.getLimbSwingAmount() * 2f);
                return state.setAndContinue(DefaultAnimations.WALK);
            }

            return state.setAndContinue(animatable.getSwimmingDataTracker()? WATER_IDLE: DefaultAnimations.IDLE);

        }).receiveTriggeredAnimations()
                .triggerableAnim("attack1", GeckoAnimationsUtil.ATTACK1)
                .triggerableAnim("attack2", GeckoAnimationsUtil.ATTACK2)
                .triggerableAnim("water_attack1", WATER_ATTACK1)
                .triggerableAnim("water_attack2", WATER_ATTACK2);
    }
}
