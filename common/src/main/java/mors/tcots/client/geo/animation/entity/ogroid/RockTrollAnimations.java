package mors.tcots.client.geo.animation.entity.ogroid;

import mors.tcots.entity.monsters.ogroids.RockTrollEntity;
import mors.tcots.utils.GeckoAnimationsUtil;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.constant.DefaultAnimations;

public class RockTrollAnimations {

    public static final RawAnimation GIVE_ITEM = RawAnimation.begin().thenPlay("special.give");
    public static final RawAnimation ATTACK_THROW_ROCK = RawAnimation.begin().thenPlay("attack.rock_throw");
    public static final RawAnimation ADMIRE = RawAnimation.begin().thenLoop("special.admire");
    public static final RawAnimation EAT = RawAnimation.begin().thenLoop("special.eat");

    public static final RawAnimation START_BLOCK = RawAnimation.begin().thenPlay("special.start_block");
    public static final RawAnimation BLOCK = RawAnimation.begin().thenLoop("special.block");
    public static final RawAnimation END_BLOCK = RawAnimation.begin().thenPlay("special.end_block");

    public static AnimationController<RockTrollEntity> mainController(final RockTrollEntity animatable) {
        return new AnimationController<>(animatable, state -> {
            state.setControllerSpeed(1);
            state.getController().transitionLength(0);

            if (state.getController().isPlayingTriggeredAnimation()) {
                state.setControllerSpeed(2);
                if(state.getController().getTriggeredAnimation()!=null
                        && (state.getController().getTriggeredAnimation().equals(ATTACK_THROW_ROCK))) state.setControllerSpeed(1);
                return PlayState.CONTINUE;
            }

            switch (animatable.getPose()) {
                case SLIDING:
                    state.getController().transitionLength(5);
                    return state.setAndContinue(BLOCK);
                case USING_TONGUE:
                    state.setControllerSpeed(2);
                    state.getController().transitionLength(5);
                    return state.setAndContinue(EAT);
                case CROUCHING:
                    state.getController().transitionLength(3);
                    return state.setAndContinue(ADMIRE);
                default:
                    break;
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
                .triggerableAnim("rock_attack", ATTACK_THROW_ROCK)
                .triggerableAnim("give_item", GIVE_ITEM)

                .triggerableAnim("start_block", START_BLOCK)
                .triggerableAnim("end_block", END_BLOCK);
    }
}
