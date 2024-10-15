package TCOTS.entity.geo.model;

import TCOTS.utils.GeoControllersUtil;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.math.MathHelper;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public abstract class BipedGeoModelBase<T extends GeoAnimatable> extends GeoModel<T> {

    //xTODO: FIX THE FUCKING ANIMATIONS I'M SO FUCKING CLOSE TO FUCKING KILLING MYSELF AAA
    //xTODO: Implement the new bones in the other mob models

    protected float getLegsSpeed(T entity){
        return 0.5f;
    }

    protected float getLegsAmount(T entity){
        return 0.8f;
    }

    protected float getArmsSpeed(T entity){
        return getLegsSpeed(entity);
    }

    protected float getArmsAmount(T entity){
        return 0.6f;
    }

    protected float getHeadExtraInAttacking(T entity){
        return 10f;
    }

    protected boolean hasNormalHead(T entity){
        return true;
    }

    protected boolean hasArmZMovement(T entity){
        return true;
    }
    @Override
    public void setCustomAnimations(T entity, long instanceId, AnimationState<T> animationState) {
        if(entity instanceof MobEntity mob){
            CoreGeoBone head = hasNormalHead(entity)? getAnimationProcessor().getBone("head"): null;
            CoreGeoBone left_leg =  getAnimationProcessor().getBone("left_leg_swing");
            CoreGeoBone right_leg = getAnimationProcessor().getBone("right_leg_swing");
            CoreGeoBone left_arm =  getAnimationProcessor().getBone("left_arm_swing");
            CoreGeoBone right_arm = getAnimationProcessor().getBone("right_arm_swing");


            if (head != null) {
                EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
                if(animationState.isMoving() && mob.isAttacking()){
                    head.setRotX(((entityData.headPitch()+getHeadExtraInAttacking(entity)) * MathHelper.RADIANS_PER_DEGREE));
                    head.setRotY((entityData.netHeadYaw() * MathHelper.RADIANS_PER_DEGREE));
                }
                else{
                    head.setRotX((entityData.headPitch()) * MathHelper.RADIANS_PER_DEGREE);
                    head.setRotY(entityData.netHeadYaw() * MathHelper.RADIANS_PER_DEGREE);
                }
                head.setRotZ(0);
            }


            if(left_arm!=null && right_arm!=null && left_leg!=null && right_leg!=null){
                if(!mob.handSwinging){
                    left_leg.setRotX((float)   -(Math.sin(animationState.getLimbSwing()*getLegsSpeed(entity))*(animationState.getLimbSwingAmount()*getLegsAmount(entity))));
                    right_leg.setRotX((float)   (Math.sin(animationState.getLimbSwing()*getLegsSpeed(entity))*(animationState.getLimbSwingAmount()*getLegsAmount(entity))));

                    left_arm.setRotX(GeoControllersUtil.getLimbSwing(animationState,  -0.9f, 0.5f, getArmsSpeed(entity), getArmsAmount(entity),false));
                    right_arm.setRotX(GeoControllersUtil.getLimbSwing(animationState, -0.5f, 0.9f, getArmsSpeed(entity), getArmsAmount(entity),true));

                    if(this.hasArmZMovement(entity)) {
                        right_arm.setRotZ(GeoControllersUtil.getLimbSwing(animationState, -0.0f, 0.2f, getArmsSpeed(entity), getArmsAmount(entity), false));
                        left_arm.setRotZ(GeoControllersUtil.getLimbSwing(animationState, -0.2f, 0.0f, getArmsSpeed(entity), getArmsAmount(entity), true));
                    } else {
                        right_arm.setRotZ(0);
                        left_arm.setRotZ(0);
                    }
                }
            }
        }
    }
}
