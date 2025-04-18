package TCOTS.entity.geo.model;

import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.constant.DataTickets;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public abstract class QuadrupedGhoulModelBase <T extends GeoAnimatable> extends GeoModel<T> {

    protected float getLegsSpeed(){
        return 0.5f;
    }


    protected float getLegsAmount(){
        return 0.8f;
    }

    @Override
    public void setCustomAnimations(T animatable, long instanceId, AnimationState<T> animationState) {
        if(animatable instanceof Mob mob) {
            GeoBone head = getAnimationProcessor().getBone("head");

            GeoBone left_leg = getAnimationProcessor().getBone("left_leg_swing");

            GeoBone right_leg = getAnimationProcessor().getBone("right_leg_swing");

            GeoBone left_arm = getAnimationProcessor().getBone("left_arm_swing");

            GeoBone right_arm = getAnimationProcessor().getBone("right_arm_swing");

            if (head != null) {
                EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
                head.setRotX((entityData.headPitch() + 90f) * Mth.DEG_TO_RAD);
                head.setRotZ((entityData.netHeadYaw() * Mth.DEG_TO_RAD));
            }

            if (left_leg != null && right_leg != null && left_arm != null && right_arm != null
            ) {

                if (!mob.swinging){
                    left_leg.setRotX((float)   -(Math.sin(animationState.getLimbSwing()*getLegsSpeed())*(animationState.getLimbSwingAmount()*getLegsAmount())));
                    right_leg.setRotX((float)   (Math.sin(animationState.getLimbSwing()*getLegsSpeed())*(animationState.getLimbSwingAmount()*getLegsAmount())));

                    left_arm.setRotX((float)   -(Math.sin(animationState.getLimbSwing()*getLegsSpeed())*(animationState.getLimbSwingAmount()*getLegsAmount())));
                    right_arm.setRotX((float)   (Math.sin(animationState.getLimbSwing()*getLegsSpeed())*(animationState.getLimbSwingAmount()*getLegsAmount())));
                }
            }
        }
    }
}
