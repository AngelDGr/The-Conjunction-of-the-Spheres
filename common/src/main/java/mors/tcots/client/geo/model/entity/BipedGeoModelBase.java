package mors.tcots.client.geo.model.entity;

import mors.tcots.utils.GeckoAnimationsUtil;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public abstract class BipedGeoModelBase<T extends GeoAnimatable> extends GeoModel<T> {

    //xTODO: FIX THE FUCKING ANIMATIONS I'M SO FUCKING CLOSE TO FUCKING KILLING MYSELF AAA
    //xTODO: Implement the new bones in the other mob models

    protected float getLegsSpeed(final T entity){
        return 0.5f;
    }

    protected float getLegsAmount(final T entity){
        return 0.8f;
    }

    protected float getArmsSpeed(final T entity){
        return getLegsSpeed(entity);
    }

    protected float getArmsAmount(final T entity){
        return 0.6f;
    }

    protected float getHeadExtraInAttacking(final T entity){
        return 10f;
    }

    protected boolean hasNormalHead(final T entity){
        return true;
    }

    protected boolean hasArmZMovement(final T entity){
        return true;
    }
    @Override
    public void setCustomAnimations(final T entity, final long instanceId, final AnimationState<T> animationState) {
        if(entity instanceof final Mob mob){
            final GeoBone head = hasNormalHead(entity)? getAnimationProcessor().getBone("head"): null;
            final GeoBone left_leg =  getAnimationProcessor().getBone("left_leg_swing");
            final GeoBone right_leg = getAnimationProcessor().getBone("right_leg_swing");
            final GeoBone left_arm =  getAnimationProcessor().getBone("left_arm_swing");
            final GeoBone right_arm = getAnimationProcessor().getBone("right_arm_swing");


            if (head != null) {
                final EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
                if(animationState.isMoving() && mob.isAggressive()){
                    head.setRotX(((entityData.headPitch()+getHeadExtraInAttacking(entity)) * Mth.DEG_TO_RAD));
                    head.setRotY((entityData.netHeadYaw() * Mth.DEG_TO_RAD));
                }
                else{
                    head.setRotX((entityData.headPitch()) * Mth.DEG_TO_RAD);
                    head.setRotY(entityData.netHeadYaw() * Mth.DEG_TO_RAD);
                }
                head.setRotZ(0);
            }


            if(left_arm!=null && right_arm!=null && left_leg!=null && right_leg!=null){
                if(!mob.swinging){
                    left_leg.setRotX((float)   -(Math.sin(animationState.getLimbSwing()*getLegsSpeed(entity))*(animationState.getLimbSwingAmount()*getLegsAmount(entity))));
                    right_leg.setRotX((float)   (Math.sin(animationState.getLimbSwing()*getLegsSpeed(entity))*(animationState.getLimbSwingAmount()*getLegsAmount(entity))));

                    left_arm.setRotX(GeckoAnimationsUtil.getLimbSwing(animationState,  -0.9f, 0.5f, getArmsSpeed(entity), getArmsAmount(entity),false));
                    right_arm.setRotX(GeckoAnimationsUtil.getLimbSwing(animationState, -0.5f, 0.9f, getArmsSpeed(entity), getArmsAmount(entity),true));

                    if(this.hasArmZMovement(entity)) {
                        right_arm.setRotZ(GeckoAnimationsUtil.getLimbSwing(animationState, -0.0f, 0.2f, getArmsSpeed(entity), getArmsAmount(entity), false));
                        left_arm.setRotZ(GeckoAnimationsUtil.getLimbSwing(animationState, -0.2f, 0.0f, getArmsSpeed(entity), getArmsAmount(entity), true));
                    } else {
                        right_arm.setRotZ(0);
                        left_arm.setRotZ(0);
                    }
                }
            }
        }
    }
}
