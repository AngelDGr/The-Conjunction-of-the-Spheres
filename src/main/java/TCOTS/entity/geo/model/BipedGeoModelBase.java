package TCOTS.entity.geo.model;

import TCOTS.entity.interfaces.LungeMob;
import TCOTS.entity.misc.CommonControllers;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.math.MathHelper;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public abstract class BipedGeoModelBase<T extends GeoAnimatable> extends GeoModel<T> {

    protected float getLegsSpeed(){
        return 1.0f;
    }

    protected float getLegsAmount(){
        return 1.0f;
    }

    protected float getArmsAmount(){
        return 1.0f;
    }

    protected float getArmsSpeed(){
        return 1f;
    }

    @Override
    public void setCustomAnimations(T entity, long instanceId, AnimationState<T> animationState) {
        if(entity instanceof MobEntity mob){
            CoreGeoBone head = getAnimationProcessor().getBone("head");

            CoreGeoBone left_leg = getAnimationProcessor().getBone("left_leg");
            CoreGeoBone right_leg = getAnimationProcessor().getBone("right_leg");
            CoreGeoBone left_arm = getAnimationProcessor().getBone("left_arm");
            CoreGeoBone right_arm = getAnimationProcessor().getBone("right_arm");

            if (head != null) {
                EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
                if(animationState.isMoving() && mob.isAttacking()){
                    head.setRotY((entityData.netHeadYaw() * MathHelper.RADIANS_PER_DEGREE));
                    head.setRotX(((entityData.headPitch()+10) * MathHelper.RADIANS_PER_DEGREE));
                }
                else{
                    head.setRotX(entityData.headPitch() * MathHelper.RADIANS_PER_DEGREE);
                    head.setRotY(entityData.netHeadYaw() * MathHelper.RADIANS_PER_DEGREE);
                }
            }


            if(left_arm!=null && right_arm!=null && left_leg!=null && right_leg!=null){
                if(mob instanceof LungeMob lunge){
                    if(lunge.getIsLunging()) return;
                }

                if(!mob.handSwinging){
                    left_leg.setRotX((float)   -(Math.sin(animationState.getLimbSwing()*getLegsSpeed())*(animationState.getLimbSwingAmount()*getLegsAmount())));
                    right_leg.setRotX((float)   (Math.sin(animationState.getLimbSwing()*getLegsSpeed())*(animationState.getLimbSwingAmount()*getLegsAmount())));

                    left_arm.setRotX(CommonControllers.getLimbSwing(animationState,  -0.9f, 0.5f, getArmsSpeed(), getArmsAmount(),false));
                    right_arm.setRotX(CommonControllers.getLimbSwing(animationState, -0.2f, 0.9f, getArmsSpeed(), getArmsAmount(),true));

                    right_arm.setRotZ(CommonControllers.getLimbSwing(animationState,  -0.0f, 0.2f, getArmsSpeed(), getArmsAmount(), false));
                    left_arm.setRotZ(CommonControllers.getLimbSwing(animationState,   -0.2f, 0.0f, getArmsSpeed(),getArmsAmount(), true));
                }
            }
        }



    }
}
