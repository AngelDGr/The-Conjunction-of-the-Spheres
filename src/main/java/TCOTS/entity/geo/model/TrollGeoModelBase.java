package TCOTS.entity.geo.model;

import TCOTS.entity.ogroids.AbstractTrollEntity;
import TCOTS.entity.ogroids.RockTrollEntity;
import TCOTS.utils.GeoControllersUtil;
import net.minecraft.util.math.MathHelper;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.model.data.EntityModelData;

public abstract class TrollGeoModelBase<T extends AbstractTrollEntity> extends BipedGeoModelBase<T>  {

    protected boolean hasArmZMovement(T troll){
        return false;
    }

    @Override
    protected boolean hasNormalHead(T troll) {
        return false;
    }

    @Override
    public void setCustomAnimations(T troll, long instanceId, AnimationState<T> animationState) {
        GeoBone head = getAnimationProcessor().getBone("head");
        GeoBone left_leg =  getAnimationProcessor().getBone("left_leg_swing");
        GeoBone right_leg = getAnimationProcessor().getBone("right_leg_swing");
        GeoBone left_arm =  getAnimationProcessor().getBone("left_arm_swing");
        GeoBone right_arm = getAnimationProcessor().getBone("right_arm_swing");
        GeoBone left_hand = getAnimationProcessor().getBone("left_hand");
        GeoBone right_hand = getAnimationProcessor().getBone("right_hand");
        GeoBone low_jaw = getAnimationProcessor().getBone("lowJaw");

        if (head != null && low_jaw!= null) {
            EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
            //Admiring
            if(troll.hasBarteringItem()){
                head.setRotX(((-22f) * MathHelper.RADIANS_PER_DEGREE));
                head.setRotY((troll.isLeftHanded()? -1: 1) * (( 12f) * MathHelper.RADIANS_PER_DEGREE));
                head.setRotZ(((-10f) * MathHelper.RADIANS_PER_DEGREE));
                low_jaw.setRotX(0);
            }
            //Blocking
            else if(troll instanceof RockTrollEntity rockTroll && rockTroll.isTrollBlocking()){
                head.setRotX(((-15f) * MathHelper.RADIANS_PER_DEGREE));
                head.setRotY(((-22.5f) * MathHelper.RADIANS_PER_DEGREE));
                head.setRotZ(0);
                low_jaw.setRotX(0);
            }
            //Eating
            else if(troll.getEatingTime()!=-1){
                head.setRotX(this.getAnimationProgress(troll, animationState.getPartialTick()));
                head.setRotY((troll.isLeftHanded()? -1:1) *  (10f * MathHelper.RADIANS_PER_DEGREE));
                head.setRotZ(0);
                //Open and closes mouth
                low_jaw.setRotX(this.getAnimationProgress(troll, animationState.getPartialTick())*2.8f);
            }
            else if(animationState.isMoving() && troll.isAttacking()){
                head.setRotX(((entityData.headPitch()+getHeadExtraInAttacking(troll)) * MathHelper.RADIANS_PER_DEGREE));
                head.setRotY((entityData.netHeadYaw() * MathHelper.RADIANS_PER_DEGREE));
                head.setRotZ(0);
                low_jaw.setRotX(0);
            }
            else {
                head.setRotX((entityData.headPitch()) * MathHelper.RADIANS_PER_DEGREE);
                head.setRotY(entityData.netHeadYaw() * MathHelper.RADIANS_PER_DEGREE);
                head.setRotZ(0);
                low_jaw.setRotX(0);
            }
        }

        if(left_arm!=null && right_arm!=null && left_leg!=null && right_leg!=null && left_hand!=null && right_hand!=null){
            if(!troll.handSwinging && !troll.hasBarteringItem() && troll.getEatingTime()==-1){
                left_leg.setRotX((float)   -(Math.sin(animationState.getLimbSwing()*getLegsSpeed(troll))*(animationState.getLimbSwingAmount()*getLegsAmount(troll))));
                right_leg.setRotX((float)   (Math.sin(animationState.getLimbSwing()*getLegsSpeed(troll))*(animationState.getLimbSwingAmount()*getLegsAmount(troll))));

                left_arm.setRotX(GeoControllersUtil.getLimbSwing(animationState,  -0.9f, 0.5f, getArmsSpeed(troll), getArmsAmount(troll),false));
                right_arm.setRotX(GeoControllersUtil.getLimbSwing(animationState, -0.5f, 0.9f, getArmsSpeed(troll), getArmsAmount(troll),true));

                if(this.hasArmZMovement(troll)) {
                    right_arm.setRotZ(GeoControllersUtil.getLimbSwing(animationState, -0.0f, 0.2f, getArmsSpeed(troll), getArmsAmount(troll), false));
                    left_arm.setRotZ(GeoControllersUtil.getLimbSwing(animationState, -0.2f, 0.0f, getArmsSpeed(troll), getArmsAmount(troll), true));
                } else {
                    right_arm.setRotZ(0);
                    left_arm.setRotZ(0);
                }

                left_hand.setRotX(0);
                left_hand.setRotY(0);
                left_hand.setRotZ(0);

                right_hand.setRotX(0);
                right_hand.setRotY(0);
                right_hand.setRotZ(0);
            }
            //Admiring
            else if (troll.hasBarteringItem()){
                left_arm.setRotX(troll.isLeftHanded()? 0 : ( 41f) * MathHelper.RADIANS_PER_DEGREE);
                left_arm.setRotY(troll.isLeftHanded()? 0 : (-11) * MathHelper.RADIANS_PER_DEGREE);
                left_arm.setRotZ(troll.isLeftHanded()? 0 : ( 13f) * MathHelper.RADIANS_PER_DEGREE);

                right_arm.setRotX(troll.isLeftHanded()? (( 41f) * MathHelper.RADIANS_PER_DEGREE) : 0);
                right_arm.setRotY(troll.isLeftHanded()? (( 11) * MathHelper.RADIANS_PER_DEGREE) : 0);
                right_arm.setRotZ(troll.isLeftHanded()? ((-13f) * MathHelper.RADIANS_PER_DEGREE) : 0);

                left_leg.setRotX(0);
                left_leg.setRotY(0);
                left_leg.setRotZ(0);

                right_leg.setRotX(0);
                right_leg.setRotY(0);
                right_leg.setRotZ(0);

                left_hand.setRotX(0);
                left_hand.setRotY(0);
                left_hand.setRotZ(0);

                right_hand.setRotX(0);
                right_hand.setRotY(0);
                right_hand.setRotZ(0);
            }
            //Eating
            else if (troll.getEatingTime()!=-1){
                left_arm.setRotX(troll.isLeftHanded()? 0: ( ( 70f) * MathHelper.RADIANS_PER_DEGREE) + (this.getAnimationProgress(troll, animationState.getPartialTick())));
                left_arm.setRotY(troll.isLeftHanded()? 0: (-15f) * MathHelper.RADIANS_PER_DEGREE);
                left_arm.setRotZ(troll.isLeftHanded()? 0: ( 13f) * MathHelper.RADIANS_PER_DEGREE);

                right_arm.setRotX(troll.isLeftHanded()?  (( ( 70f) * MathHelper.RADIANS_PER_DEGREE) + (this.getAnimationProgress(troll, animationState.getPartialTick()))): 0);
                right_arm.setRotY(troll.isLeftHanded()?  (( 15f) * MathHelper.RADIANS_PER_DEGREE): 0);
                right_arm.setRotZ(troll.isLeftHanded()?  (( 13f) * MathHelper.RADIANS_PER_DEGREE): 0);

                left_leg.setRotX(0);
                left_leg.setRotY(0);
                left_leg.setRotZ(0);

                right_leg.setRotX(0);
                right_leg.setRotY(0);
                right_leg.setRotZ(0);

                left_hand.setRotX(troll.isLeftHanded()? 0: this.getAnimationProgress(troll, animationState.getPartialTick()));
                left_hand.setRotY(0);
                left_hand.setRotZ(0);

                right_hand.setRotX(troll.isLeftHanded()? this.getAnimationProgress(troll, animationState.getPartialTick()): 0);
                right_hand.setRotY(0);
                right_hand.setRotZ(0);
            }
        }
    }

    protected float getAnimationProgress(AbstractTrollEntity troll, float partialTick) {
        float g = MathHelper.lerp(partialTick, troll.prevEatingProgress, troll.eatingProgress);
        float h = MathHelper.lerp(partialTick, troll.prevMaxEatingDeviation, troll.maxEatingDeviation);
        return (MathHelper.sin(g) + 1.0f) * h;
    }
}
