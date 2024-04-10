package TCOTS.entity.interfaces;

import net.minecraft.sound.SoundEvent;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

public interface LungeMob {
    RawAnimation LUNGE = RawAnimation.begin().thenPlay("attack.lunge");

    boolean getNotCooldownBetweenLunges();

    void setCooldownBetweenLunges(boolean cooldownBetweenLunges);

    boolean getIsLugging();
    void setIsLugging(boolean wasLugging);

    @Nullable SoundEvent getLungeSound();

    int getLungeTicks();

    void setLungeTicks(int lungeTicks);

    default void tickLunge(){
        if (getLungeTicks() > 0) {
            this.setIsLugging(false);
            setLungeTicks(getLungeTicks()-1);
        } else {
            setCooldownBetweenLunges(false);
        }
    }

    default RawAnimation getLungeAnimation() {return LUNGE;}

    default  <T extends GeoAnimatable> PlayState animationLungePredicate(AnimationState<T> state) {
        if (this.getIsLugging()) {
            state.setAnimation(getLungeAnimation());
            return PlayState.CONTINUE;
        }

        state.getController().forceAnimationReset();
        return PlayState.CONTINUE;
    }
}