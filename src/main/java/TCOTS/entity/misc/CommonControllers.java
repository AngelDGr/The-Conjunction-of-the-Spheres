package TCOTS.entity.misc;

import net.minecraft.util.math.random.Random;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

public class CommonControllers {

    public static final RawAnimation ATTACK1 = RawAnimation.begin().thenPlay("attack.swing1");
    public static final RawAnimation ATTACK2 = RawAnimation.begin().thenPlay("attack.swing2");
    public static final RawAnimation ATTACK3 = RawAnimation.begin().thenPlay("attack.swing3");

    public static  <T extends GeoAnimatable> PlayState animationTwoAttacksPredicate(AnimationState<T> state, boolean handSwinging, Random random) {
        state.getController().forceAnimationReset();
        // Random instance
        // Generates two random numbers
        if (handSwinging){
            int r = random.nextInt(2);
            if (r == 1) {
                return state.setAndContinue(ATTACK1);
            } else {
                return state.setAndContinue(ATTACK2);
            }
        }
        return PlayState.CONTINUE;
    }

    public static  <T extends GeoAnimatable> PlayState animationThreeAttacksPredicate(AnimationState<T> state, boolean handSwinging, Random random) {
        state.getController().forceAnimationReset();
        // Random instance
        // Generates three random numbers
        if (handSwinging) {
            int r = random.nextInt(3);
            switch (r) {
                case 0:
                    return state.setAndContinue(ATTACK1);

                case 1:
                    return state.setAndContinue(ATTACK2);

                case 2:
                    return state.setAndContinue(ATTACK3);
            }
        }
        return PlayState.CONTINUE;
    }


}
