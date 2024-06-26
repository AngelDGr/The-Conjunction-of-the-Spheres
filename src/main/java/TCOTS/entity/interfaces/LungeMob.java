package TCOTS.entity.interfaces;

import net.minecraft.sound.SoundEvent;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

public interface LungeMob {
    RawAnimation LUNGE = RawAnimation.begin().thenPlay("attack.lunge");

    boolean getNotCooldownBetweenLunges();

    void setCooldownBetweenLunges(boolean cooldownBetweenLunges);

    @Nullable SoundEvent getLungeSound();

    int getLungeTicks();

    void setLungeTicks(int lungeTicks);

    default void tickLunge(){
        if (getLungeTicks() > 0) {
            setLungeTicks(getLungeTicks()-1);
        } else {
            setCooldownBetweenLunges(false);
        }
    }

    default RawAnimation getLungeAnimation() {return LUNGE;}

    default void lungeAnimationController(GeoEntity mob, AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(
                new AnimationController<>(mob, "LungeController", 1, state -> PlayState.STOP)
                        .triggerableAnim("lunge", getLungeAnimation())
        );
    }

}