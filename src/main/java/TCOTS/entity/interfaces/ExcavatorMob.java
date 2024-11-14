package TCOTS.entity.interfaces;

import TCOTS.entity.misc.DrownerPuddleEntity;
import TCOTS.sounds.TCOTS_Sounds;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.animation.PlayState;

import java.util.List;

public interface ExcavatorMob {

    RawAnimation DIGGING_OUT = RawAnimation.begin().thenPlayAndHold("special.diggingOut");
    RawAnimation DIGGING_IN = RawAnimation.begin().thenPlayAndHold("special.diggingIn");

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

    default void tickPuddle(MobEntity mob){
        if(getPuddle()==null){
            setPuddle(DetectOwnPuddle(mob));
        }
    }

    boolean getInGround();
    void setInGround(boolean wasInGround);

    boolean getIsEmerging();

    void setIsEmerging(boolean wasEmerging);

    default void spawnGroundParticles(@NotNull LivingEntity entity){
        BlockState blockState = entity.getSteppingBlockState();
        if (blockState.getRenderType() != BlockRenderType.INVISIBLE) {
            for (int i = 0; i < 11; ++i) {
                double d = entity.getX() + (double) MathHelper.nextBetween(entity.getRandom(), -0.7F, 0.7F);
                double e = entity.getY();
                double f = entity.getZ() + (double) MathHelper.nextBetween(entity.getRandom(), -0.7F, 0.7F);

                entity.getWorld().addParticle(new BlockStateParticleEffect(ParticleTypes.BLOCK, blockState), d, e, f, 0.0, 0.0, 0.0);
            }
        }
    }

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
                this.setInGround(true);
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
