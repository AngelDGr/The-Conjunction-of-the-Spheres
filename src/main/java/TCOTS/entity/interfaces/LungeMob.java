package TCOTS.entity.interfaces;

import net.minecraft.sound.SoundEvent;

public interface LungeMob {

    boolean getNotCooldownBetweenLunges();

    void setCooldownBetweenLunges(boolean cooldownBetweenLunges);

    boolean getIsLugging();
    void setIsLugging(boolean wasLugging);

    SoundEvent getLungeSound();

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
}