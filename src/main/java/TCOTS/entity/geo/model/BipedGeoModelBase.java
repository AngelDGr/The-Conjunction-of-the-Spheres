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

    protected float getLegsSpeed(){
        return 0.5f;
    }

    protected float getLegsAmount(){
        return 0.8f;
    }

    protected float getArmsSpeed(){
        return getLegsSpeed();
    }

    protected float getArmsAmount(){
        return 0.6f;
    }

    protected float getHeadExtraInAttacking(){
        return 10f;
    }

    protected boolean hasNormalHead(){
        return true;
    }

    protected boolean hasArmZMovement(){
        return true;
    }
    @Override
    public void setCustomAnimations(T entity, long instanceId, AnimationState<T> animationState) {
        if(entity instanceof MobEntity mob){
            CoreGeoBone head = hasNormalHead()? getAnimationProcessor().getBone("head"): null;
            CoreGeoBone left_leg =  getAnimationProcessor().getBone("left_leg_swing");
            CoreGeoBone right_leg = getAnimationProcessor().getBone("right_leg_swing");
            CoreGeoBone left_arm =  getAnimationProcessor().getBone("left_arm_swing");
            CoreGeoBone right_arm = getAnimationProcessor().getBone("right_arm_swing");


            if (head != null) {
                EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
                if(animationState.isMoving() && mob.isAttacking()){
                    head.setRotX(((entityData.headPitch()+getHeadExtraInAttacking()) * MathHelper.RADIANS_PER_DEGREE));
                    head.setRotY((entityData.netHeadYaw() * MathHelper.RADIANS_PER_DEGREE));
                }
                else{
                    head.setRotX((entityData.headPitch()) * MathHelper.RADIANS_PER_DEGREE);
                    head.setRotY(entityData.netHeadYaw() * MathHelper.RADIANS_PER_DEGREE);
                }
            }


            if(left_arm!=null && right_arm!=null && left_leg!=null && right_leg!=null){
                if(!mob.handSwinging){
                    left_leg.setRotX((float)   -(Math.sin(animationState.getLimbSwing()*getLegsSpeed())*(animationState.getLimbSwingAmount()*getLegsAmount())));
                    right_leg.setRotX((float)   (Math.sin(animationState.getLimbSwing()*getLegsSpeed())*(animationState.getLimbSwingAmount()*getLegsAmount())));

                    left_arm.setRotX(GeoControllersUtil.getLimbSwing(animationState,  -0.9f, 0.5f, getArmsSpeed(), getArmsAmount(),false));
                    right_arm.setRotX(GeoControllersUtil.getLimbSwing(animationState, -0.5f, 0.9f, getArmsSpeed(), getArmsAmount(),true));

                    if(this.hasArmZMovement()) {
                        right_arm.setRotZ(GeoControllersUtil.getLimbSwing(animationState, -0.0f, 0.2f, getArmsSpeed(), getArmsAmount(), false));
                        left_arm.setRotZ(GeoControllersUtil.getLimbSwing(animationState, -0.2f, 0.0f, getArmsSpeed(), getArmsAmount(), true));
                    }
                }
            }
        }
    }
}
