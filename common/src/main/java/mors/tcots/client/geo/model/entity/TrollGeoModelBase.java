package mors.tcots.client.geo.model.entity;

import mors.tcots.entity.monsters.ogroids.AbstractTrollEntity;
import mors.tcots.entity.monsters.ogroids.RockTrollEntity;
import mors.tcots.utils.GeckoAnimationsUtil;
import net.minecraft.util.Mth;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.model.data.EntityModelData;

public abstract class TrollGeoModelBase<T extends AbstractTrollEntity> extends BipedGeoModelBase<T> {

    protected boolean hasArmZMovement(final T troll){
        return false;
    }

    @Override
    protected boolean hasNormalHead(final T troll) {
        return false;
    }

    @Override
    public void setCustomAnimations(final T troll, final long instanceId, final AnimationState<T> animationState) {
        final GeoBone head = getAnimationProcessor().getBone("head");
        final GeoBone left_leg =  getAnimationProcessor().getBone("left_leg_swing");
        final GeoBone right_leg = getAnimationProcessor().getBone("right_leg_swing");
        final GeoBone left_arm =  getAnimationProcessor().getBone("left_arm_swing");
        final GeoBone right_arm = getAnimationProcessor().getBone("right_arm_swing");
        final GeoBone left_hand = getAnimationProcessor().getBone("left_hand");
        final GeoBone right_hand = getAnimationProcessor().getBone("right_hand");
        final GeoBone low_jaw = getAnimationProcessor().getBone("lowJaw");

        if (head != null && low_jaw!= null) {
            final EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
            //Admiring
            if(troll.hasBarteringItem()){
                head.setRotX(((-22f) * Mth.DEG_TO_RAD));
                head.setRotY((troll.isLeftHanded()? -1: 1) * (( 12f) * Mth.DEG_TO_RAD));
                head.setRotZ(((-10f) * Mth.DEG_TO_RAD));
                low_jaw.setRotX(0);
            }
            //Blocking
            else if(troll instanceof final RockTrollEntity rockTroll && rockTroll.isTrollBlocking()){
                head.setRotX(((-15f) * Mth.DEG_TO_RAD));
                head.setRotY(((-22.5f) * Mth.DEG_TO_RAD));
                head.setRotZ(0);
                low_jaw.setRotX(0);
            }
            //Eating
            else if(troll.getEatingTime()!=-1){
                head.setRotX(this.getAnimationProgress(troll, animationState.getPartialTick()));
                head.setRotY((troll.isLeftHanded()? -1:1) *  (10f * Mth.DEG_TO_RAD));
                head.setRotZ(0);
                //Open and closes mouth
                low_jaw.setRotX(this.getAnimationProgress(troll, animationState.getPartialTick())*2.8f);
            }
            else if(animationState.isMoving() && troll.isAggressive()){
                head.setRotX(((entityData.headPitch()+getHeadExtraInAttacking(troll)) * Mth.DEG_TO_RAD));
                head.setRotY((entityData.netHeadYaw() * Mth.DEG_TO_RAD));
                head.setRotZ(0);
                low_jaw.setRotX(0);
            }
            else {
                head.setRotX((entityData.headPitch()) * Mth.DEG_TO_RAD);
                head.setRotY(entityData.netHeadYaw() * Mth.DEG_TO_RAD);
                head.setRotZ(0);
                low_jaw.setRotX(0);
            }
        }

        if(left_arm!=null && right_arm!=null && left_leg!=null && right_leg!=null && left_hand!=null && right_hand!=null){
            if(!troll.swinging && !troll.hasBarteringItem() && troll.getEatingTime()==-1){
                left_leg.setRotX((float)   -(Math.sin(animationState.getLimbSwing()*getLegsSpeed(troll))*(animationState.getLimbSwingAmount()*getLegsAmount(troll))));
                right_leg.setRotX((float)   (Math.sin(animationState.getLimbSwing()*getLegsSpeed(troll))*(animationState.getLimbSwingAmount()*getLegsAmount(troll))));

                left_arm.setRotX(GeckoAnimationsUtil.getLimbSwing(animationState,  -0.9f, 0.5f, getArmsSpeed(troll), getArmsAmount(troll),false));
                right_arm.setRotX(GeckoAnimationsUtil.getLimbSwing(animationState, -0.5f, 0.9f, getArmsSpeed(troll), getArmsAmount(troll),true));

                if(this.hasArmZMovement(troll)) {
                    right_arm.setRotZ(GeckoAnimationsUtil.getLimbSwing(animationState, -0.0f, 0.2f, getArmsSpeed(troll), getArmsAmount(troll), false));
                    left_arm.setRotZ(GeckoAnimationsUtil.getLimbSwing(animationState, -0.2f, 0.0f, getArmsSpeed(troll), getArmsAmount(troll), true));
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
                left_arm.setRotX(troll.isLeftHanded()? 0 : ( 41f) * Mth.DEG_TO_RAD);
                left_arm.setRotY(troll.isLeftHanded()? 0 : (-11) * Mth.DEG_TO_RAD);
                left_arm.setRotZ(troll.isLeftHanded()? 0 : ( 13f) * Mth.DEG_TO_RAD);

                right_arm.setRotX(troll.isLeftHanded()? (( 41f) * Mth.DEG_TO_RAD) : 0);
                right_arm.setRotY(troll.isLeftHanded()? (( 11) * Mth.DEG_TO_RAD) : 0);
                right_arm.setRotZ(troll.isLeftHanded()? ((-13f) * Mth.DEG_TO_RAD) : 0);

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
                left_arm.setRotX(troll.isLeftHanded()? 0: ( ( 70f) * Mth.DEG_TO_RAD) + (this.getAnimationProgress(troll, animationState.getPartialTick())));
                left_arm.setRotY(troll.isLeftHanded()? 0: (-15f) * Mth.DEG_TO_RAD);
                left_arm.setRotZ(troll.isLeftHanded()? 0: ( 13f) * Mth.DEG_TO_RAD);

                right_arm.setRotX(troll.isLeftHanded()?  (( ( 70f) * Mth.DEG_TO_RAD) + (this.getAnimationProgress(troll, animationState.getPartialTick()))): 0);
                right_arm.setRotY(troll.isLeftHanded()?  (( 15f) * Mth.DEG_TO_RAD): 0);
                right_arm.setRotZ(troll.isLeftHanded()?  (( 13f) * Mth.DEG_TO_RAD): 0);

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

    protected float getAnimationProgress(final AbstractTrollEntity troll, final float partialTick) {
        final float g = Mth.lerp(partialTick, troll.prevEatingProgress, troll.eatingProgress);
        final float h = Mth.lerp(partialTick, troll.prevMaxEatingDeviation, troll.maxEatingDeviation);
        return (Mth.sin(g) + 1.0f) * h;
    }
}
