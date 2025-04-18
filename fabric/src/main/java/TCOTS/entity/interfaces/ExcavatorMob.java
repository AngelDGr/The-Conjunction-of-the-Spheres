package TCOTS.entity.interfaces;

import TCOTS.sounds.TCOTS_Sounds;
import TCOTS.entity.misc.DrownerPuddleEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.animation.PlayState;

import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

public interface ExcavatorMob {

    RawAnimation DIGGING_OUT = RawAnimation.begin().thenPlayAndHold("special.diggingOut");
    RawAnimation DIGGING_IN = RawAnimation.begin().thenPlayAndHold("special.diggingIn");

    default AABB groundBox(Mob mob){
        return new AABB(mob.getX() - 0.39, mob.getY() + 0.1, mob.getZ() - 0.39,
                mob.getX() + 0.39, mob.getY(), mob.getZ() + 0.39);
    }

    default DrownerPuddleEntity DetectOwnPuddle(Mob mob) {
        List<DrownerPuddleEntity> list = mob.level().getEntitiesOfClass(DrownerPuddleEntity.class,
                new AABB(mob.getX() + 2, mob.getY() + 2, mob.getZ() + 2,
                        mob.getX() - 2, mob.getY() - 2, mob.getZ() - 2),
                (T) -> true);

        //Detect
        if (!list.isEmpty()) {
            for (DrownerPuddleEntity puddleEntity : list) {
                if (puddleEntity.getOwnerUUID() != null && puddleEntity.getOwnerUUID().equals(mob.getUUID())) {
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

    default RawAnimation getEmergingAnimation(){
        return DIGGING_OUT;
    }

    default RawAnimation getDiggingAnimation(){
        return DIGGING_IN;
    }

    default  <T extends GeoAnimatable> PlayState animationDiggingPredicate(AnimationState<T> state) {
        if(this.getInGround() && !this.getIsEmerging()){
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

    default SoundEvent getEmergingSound(){
        return TCOTS_Sounds.MONSTER_EMERGING;
    }

    default SoundEvent getDiggingSound(){
        return TCOTS_Sounds.MONSTER_DIGGING;
    }


    default boolean getSpawnedPuddleDataTracker() {
        return false;
    }
    default void setSpawnedPuddleDataTracker(boolean puddleSpawned) {

    }

     int getAnimationParticlesTicks();

     void setAnimationParticlesTicks(int animationParticlesTicks);

    default void tickExcavator(LivingEntity entity) {
        //Particles when return to ground
        if(this.getAnimationParticlesTicks() > 0 && this.getInGround()
                && !(this.getIsEmerging())
        ){
            this.spawnGroundParticles(entity);
            setAnimationParticlesTicks(getAnimationParticlesTicks()-1);
        } else if (getAnimationParticlesTicks()==0) {
            this.setInvisibleData(true);
            setAnimationParticlesTicks(-1);
        }

        if(!this.getInGround() && !this.getIsEmerging()){
            setAnimationParticlesTicks(36);
        }

        //Particles when emerges from ground
        if(this.getIsEmerging()){
            this.spawnGroundParticles(entity);
        }
    }

    default void tickPuddle(Mob mob){
        if(getPuddle()==null){
            setPuddle(DetectOwnPuddle(mob));
        }
    }

    boolean getInGround();
    void setInGround(boolean wasInGround);

    boolean getIsEmerging();

    void setIsEmerging(boolean wasEmerging);

    default void spawnGroundParticles(@NotNull LivingEntity entity){
        BlockState blockState = entity.getBlockStateOn();
        if (blockState.getRenderShape() != RenderShape.INVISIBLE) {
            for (int i = 0; i < 11; ++i) {
                double d = entity.getX() + (double) Mth.randomBetween(entity.getRandom(), -0.7F, 0.7F);
                double e = entity.getY();
                double f = entity.getZ() + (double) Mth.randomBetween(entity.getRandom(), -0.7F, 0.7F);

                entity.level().addParticle(new BlockParticleOption(ParticleTypes.BLOCK, blockState), d, e, f, 0.0, 0.0, 0.0);
            }
        }
    }

     int getReturnToGround_Ticks();

     void setReturnToGround_Ticks(int returnToGround_Ticks);

    boolean getInvisibleData();

    void setInvisibleData(boolean isInvisible);

    default void mobTickExcavator(@Nullable List<TagKey<Block>> blockTags, @Nullable List<Block> blocks, Mob mob){
        if (this.getReturnToGround_Ticks() > 0
                && !this.getIsEmerging()
                && !mob.isAggressive()
        ) {
            this.setReturnToGround_Ticks(this.getReturnToGround_Ticks() - 1 );
        }else{
            if(this.getReturnToGround_Ticks()==0 && checkBlocks(blockTags, blocks, mob)
            ){
                this.setInGround(true);
            }
        }
    }

    private boolean checkBlocks(@Nullable List<TagKey<Block>> blockTags, @Nullable List<Block> blocks, Mob mob){
        BlockPos entityPos = new BlockPos((int)mob.getX(), (int)mob.getY(), (int)mob.getZ());
        BlockPos entityDown = entityPos.below();
        Level world = mob.level();

        if(blockTags != null){
            for (TagKey<Block> tags : blockTags) {
                if(world.getBlockState(entityDown).is(tags)){
                    return true;
                }
            }
        }
        if(blocks != null) {
            for (Block block : blocks) {
                if(world.getBlockState(entityDown).is(block)){
                    return true;
                }
            }
        }
        return false;
    }
}
