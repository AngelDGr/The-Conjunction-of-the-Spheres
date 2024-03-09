package TCOTS.entity.interfaces;

import TCOTS.entity.misc.DrownerPuddleEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.Box;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.core.animatable.GeoAnimatable;


import java.util.List;

public interface ExcavatorMob {

    default Box groundBox(MobEntity mob){
        return new Box(mob.getX() - 0.39, mob.getY() + 0.1, mob.getZ() - 0.39,
                mob.getX() + 0.39, mob.getY(), mob.getZ() + 0.39);
    }

    default DrownerPuddleEntity DetectOwnPuddle(MobEntity mob) {
        List<DrownerPuddleEntity> list = mob.getWorld().getEntitiesByClass(DrownerPuddleEntity.class,
                new Box(mob.getX() + 2, mob.getY() + 2, mob.getZ() + 2,
                        mob.getX() - 2, mob.getY() - 2, mob.getZ() - 2),
                (T) -> true);

        //Detect
        if (!list.isEmpty()) {
            for (DrownerPuddleEntity puddleEntity : list) {
                if (puddleEntity.getOwnerUUID() != null && puddleEntity.getOwnerUUID().equals(mob.getUuid())) {
                    return puddleEntity;
                }
            }
        }

        return null;
    }

    default  <T extends GeoAnimatable> PlayState animationEmergingPredicate(AnimationState<T> state) {
        if (this.getIsEmerging()){
            state.setAnimation(getEmergingAnimation());
            return PlayState.CONTINUE;
        }
        else{
            state.getController().forceAnimationReset();
            return PlayState.STOP;
        }
    }

    RawAnimation getEmergingAnimation();

    RawAnimation getDiggingAnimation();

    default  <T extends GeoAnimatable> PlayState animationDiggingPredicate(AnimationState<T> state) {
        if(this.getInGroundDataTracker() && !this.getIsEmerging()){
            state.setAnimation(getDiggingAnimation());
            return PlayState.CONTINUE;
        }else{
            state.getController().forceAnimationReset();
            return PlayState.STOP;
        }
    }

    default DrownerPuddleEntity getPuddle() {
        return null;
    }

    default void setPuddle(DrownerPuddleEntity puddle) {

    }



    SoundEvent getEmergingSound();

    SoundEvent getDiggingSound();


    default boolean getSpawnedPuddleDataTracker() {
        return false;
    }
    default void setSpawnedPuddleDataTracker(boolean puddleSpawned) {

    }

    boolean getInGroundDataTracker();
    void setInGroundDataTracker(boolean wasInGround);

    boolean getIsEmerging();

    void setIsEmerging(boolean wasEmerging);

    void spawnGroundParticles();

     int getReturnToGround_Ticks();

     void setReturnToGround_Ticks(int returnToGround_Ticks);
}
