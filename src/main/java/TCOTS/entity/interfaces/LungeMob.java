package TCOTS.entity.interfaces;

import net.minecraft.sound.SoundEvent;

public interface LungeMob {

    boolean getCooldownBetweenLunges();

    void setCooldownBetweenLunges(boolean cooldownBetweenLunges);

    boolean getIsLugging();
    void setIsLugging(boolean wasLugging);

    SoundEvent getLungeSound();

    int getLungeTicks();

    void setLungeTicks(int lungeTicks);
}