package mors.tcots.entity.interfaces;

import mors.tcots.registry.TCOTS_Sounds;
import mors.tcots.entity.misc.DrownerPuddleEntity;
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

    default AABB groundBox(final Mob mob){
        return new AABB(mob.getX() - 0.39, mob.getY() + 0.1, mob.getZ() - 0.39,
                mob.getX() + 0.39, mob.getY(), mob.getZ() + 0.39);
    }

    default DrownerPuddleEntity DetectOwnPuddle(final Mob mob) {
        final List<DrownerPuddleEntity> list = mob.level().getEntitiesOfClass(DrownerPuddleEntity.class,
                new AABB(mob.getX() + 2, mob.getY() + 2, mob.getZ() + 2,
                        mob.getX() - 2, mob.getY() - 2, mob.getZ() - 2),
                (T) -> true);

        //Detect
        if (!list.isEmpty()) {
            for (final DrownerPuddleEntity puddleEntity : list) {
                if (puddleEntity.getOwnerUUID() != null && puddleEntity.getOwnerUUID().equals(mob.getUUID())) {
                    return puddleEntity;
                }
            }
        }

        return null;
    }

    default  <T extends GeoAnimatable> PlayState animationEmergingPredicate(final AnimationState<T> state) {
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

    default  <T extends GeoAnimatable> PlayState animationDiggingPredicate(final AnimationState<T> state) {
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

    default void setPuddle(final DrownerPuddleEntity puddle) {

    }

    default SoundEvent getEmergingSound(){
        return TCOTS_Sounds.getSoundEvent("monster_emerging");
    }

    default SoundEvent getDiggingSound(){
        return TCOTS_Sounds.getSoundEvent("monster_digging");
    }


    default boolean getSpawnedPuddleDataTracker() {
        return false;
    }
    default void setSpawnedPuddleDataTracker(final boolean puddleSpawned) {

    }

     int getAnimationParticlesTicks();

     void setAnimationParticlesTicks(int animationParticlesTicks);

    default void tickExcavator(final LivingEntity entity) {
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

    default void tickPuddle(final Mob mob){
        if(getPuddle()==null){
            setPuddle(DetectOwnPuddle(mob));
        }
    }

    boolean getInGround();
    void setInGround(boolean wasInGround);

    boolean getIsEmerging();

    void setIsEmerging(boolean wasEmerging);

    default void spawnGroundParticles(@NotNull final LivingEntity entity){
        final BlockState blockState = entity.getBlockStateOn();
        if (blockState.getRenderShape() != RenderShape.INVISIBLE) {
            for (int i = 0; i < 11; ++i) {
                final double d = entity.getX() + (double) Mth.randomBetween(entity.getRandom(), -0.7F, 0.7F);
                final double e = entity.getY();
                final double f = entity.getZ() + (double) Mth.randomBetween(entity.getRandom(), -0.7F, 0.7F);

                entity.level().addParticle(new BlockParticleOption(ParticleTypes.BLOCK, blockState), d, e, f, 0.0, 0.0, 0.0);
            }
        }
    }

     int getReturnToGround_Ticks();

     void setReturnToGround_Ticks(int returnToGround_Ticks);

    boolean getInvisibleData();

    void setInvisibleData(boolean isInvisible);

    default void mobTickExcavator(@Nullable final List<TagKey<Block>> blockTags, @Nullable final List<Block> blocks, final Mob mob){
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

    private boolean checkBlocks(@Nullable final List<TagKey<Block>> blockTags, @Nullable final List<Block> blocks, final Mob mob){
        final BlockPos entityPos = new BlockPos((int)mob.getX(), (int)mob.getY(), (int)mob.getZ());
        final BlockPos entityDown = entityPos.below();
        final Level world = mob.level();

        if(blockTags != null){
            for (final TagKey<Block> tags : blockTags) {
                if(world.getBlockState(entityDown).is(tags)){
                    return true;
                }
            }
        }
        if(blocks != null) {
            for (final Block block : blocks) {
                if(world.getBlockState(entityDown).is(block)){
                    return true;
                }
            }
        }
        return false;
    }
}
