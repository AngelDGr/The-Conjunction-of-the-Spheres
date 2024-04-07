package TCOTS.entity.interfaces;

import TCOTS.entity.misc.DrownerPuddleEntity;
import net.minecraft.block.Block;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
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

     int getAnimationParticlesTicks();

     void setAnimationParticlesTicks(int animationParticlesTicks);

    default void tickExcavator() {

        //Particles when return to ground
        if(this.getAnimationParticlesTicks() > 0 && this.getInGroundDataTracker()
                && !(this.getIsEmerging())
        ){
            this.spawnGroundParticles();
            setAnimationParticlesTicks(getAnimationParticlesTicks()-1);
        } else if (getAnimationParticlesTicks()==0) {
            this.setInvisibleData(true);
            setAnimationParticlesTicks(-1);
        }

        if(!this.getInGroundDataTracker() && !this.getIsEmerging()){
            setAnimationParticlesTicks(36);
        }

        //Particles when emerges from ground
        if(this.getIsEmerging()){
            this.spawnGroundParticles();
        }
    }

    default void tickPuddle(MobEntity mob){
        if(getPuddle()==null){
            setPuddle(DetectOwnPuddle(mob));
        }
    }

    boolean getInGroundDataTracker();
    void setInGroundDataTracker(boolean wasInGround);

    boolean getIsEmerging();

    void setIsEmerging(boolean wasEmerging);

    void spawnGroundParticles();

     int getReturnToGround_Ticks();

     void setReturnToGround_Ticks(int returnToGround_Ticks);

    boolean getInvisibleData();

    void setInvisibleData(boolean isInvisible);

    default void mobTickExcavator(@Nullable List<TagKey<Block>> blockTags, @Nullable List<Block> blocks, MobEntity mob){
        if (this.getReturnToGround_Ticks() > 0
                && !this.getIsEmerging()
                && !mob.isAttacking()
        ) {
            this.setReturnToGround_Ticks(this.getReturnToGround_Ticks() - 1 );
        }else{
            if(this.getReturnToGround_Ticks()==0 && checkBlocks(blockTags, blocks, mob)
            ){
                this.setInGroundDataTracker(true);
            }
        }
    }

    private boolean checkBlocks(@Nullable List<TagKey<Block>> blockTags, @Nullable List<Block> blocks, MobEntity mob){
        BlockPos entityPos = new BlockPos((int)mob.getX(), (int)mob.getY(), (int)mob.getZ());
        BlockPos entityDown = entityPos.down();
        World world = mob.getWorld();

        if(blockTags != null){
            for (TagKey<Block> tags : blockTags) {
                if(world.getBlockState(entityDown).isIn(tags)){
                    return true;
                }
            }
        }
        if(blocks != null) {
            for (Block block : blocks) {
                if(world.getBlockState(entityDown).isOf(block)){
                    return true;
                }
            }
        }
        return false;
    }
}
