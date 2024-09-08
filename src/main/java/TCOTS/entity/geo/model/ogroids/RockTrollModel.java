package TCOTS.entity.geo.model.ogroids;

import TCOTS.TCOTS_Main;
import TCOTS.entity.geo.model.BipedGeoModelBase;
import TCOTS.entity.ogroids.RockTrollEntity;
import TCOTS.utils.GeoControllersUtil;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.data.EntityModelData;

public class RockTrollModel extends BipedGeoModelBase<RockTrollEntity> {
    @Override
    public Identifier getModelResource(RockTrollEntity animatable) {
        return new Identifier(TCOTS_Main.MOD_ID, "geo/ogroids/rock_troll.geo.json");
    }

    @Override
    public Identifier getTextureResource(RockTrollEntity animatable) {
        return new Identifier(TCOTS_Main.MOD_ID, "textures/entity/ogroids/troll/rock_troll.png");
    }

    @Override
    public Identifier getAnimationResource(RockTrollEntity animatable) {
        return new Identifier(TCOTS_Main.MOD_ID, "animations/ogroids/rock_troll.animation.json");
    }

    protected boolean hasArmZMovement(){
        return false;
    }

    @Override
    protected boolean hasNormalHead() {
        return false;
    }

    @Override
    public void setCustomAnimations(RockTrollEntity troll, long instanceId, AnimationState<RockTrollEntity> animationState) {
        CoreGeoBone head = getAnimationProcessor().getBone("head");
        CoreGeoBone left_leg =  getAnimationProcessor().getBone("left_leg_swing");
        CoreGeoBone right_leg = getAnimationProcessor().getBone("right_leg_swing");
        CoreGeoBone left_arm =  getAnimationProcessor().getBone("left_arm_swing");
        CoreGeoBone right_arm = getAnimationProcessor().getBone("right_arm_swing");
        CoreGeoBone low_jaw = getAnimationProcessor().getBone("lowJaw");

        if (head != null && low_jaw!= null) {
            EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
            //Admiring
            if(troll.hasBarteringItem() && !troll.isLeftHanded()){
                head.setRotX(((-22f) * MathHelper.RADIANS_PER_DEGREE));
                head.setRotY((( 12f) * MathHelper.RADIANS_PER_DEGREE));
                head.setRotZ(((-10f) * MathHelper.RADIANS_PER_DEGREE));
                low_jaw.setRotX(0);
            } else if(troll.hasBarteringItem() && troll.isLeftHanded()){
                head.setRotX(((-22f) * MathHelper.RADIANS_PER_DEGREE));
                head.setRotY(((-12f) * MathHelper.RADIANS_PER_DEGREE));
                head.setRotZ(((-10f) * MathHelper.RADIANS_PER_DEGREE));
                low_jaw.setRotX(0);
            }
            //Blocking
            else if(troll.isBlocking()){
                head.setRotX(((-15f) * MathHelper.RADIANS_PER_DEGREE));
                head.setRotY(((-22.5f) * MathHelper.RADIANS_PER_DEGREE));
                head.setRotZ(0);
                low_jaw.setRotX(0);
            }
            //Eating
            else if(troll.getEatingTime()!=-1){
                //math.sin(query.anim_time*360)*3
                head.setRotX(this.getAnimationProgress(troll, animationState.getPartialTick()));
                head.setRotY( 10f * MathHelper.RADIANS_PER_DEGREE);
                head.setRotZ(0);
                //Open and closes mouth
                low_jaw.setRotX(this.getAnimationProgress(troll, animationState.getPartialTick())*2.8f);
            }
            else if(animationState.isMoving() && troll.isAttacking()){
                head.setRotX(((entityData.headPitch()+getHeadExtraInAttacking()) * MathHelper.RADIANS_PER_DEGREE));
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

        if(left_arm!=null && right_arm!=null && left_leg!=null && right_leg!=null){
            if(!troll.handSwinging && !troll.hasBarteringItem() && troll.getEatingTime()==-1){
                left_leg.setRotX((float)   -(Math.sin(animationState.getLimbSwing()*getLegsSpeed())*(animationState.getLimbSwingAmount()*getLegsAmount())));
                right_leg.setRotX((float)   (Math.sin(animationState.getLimbSwing()*getLegsSpeed())*(animationState.getLimbSwingAmount()*getLegsAmount())));

                left_arm.setRotX(GeoControllersUtil.getLimbSwing(animationState,  -0.9f, 0.5f, getArmsSpeed(), getArmsAmount(),false));
                right_arm.setRotX(GeoControllersUtil.getLimbSwing(animationState, -0.5f, 0.9f, getArmsSpeed(), getArmsAmount(),true));

                if(this.hasArmZMovement()) {
                    right_arm.setRotZ(GeoControllersUtil.getLimbSwing(animationState, -0.0f, 0.2f, getArmsSpeed(), getArmsAmount(), false));
                    left_arm.setRotZ(GeoControllersUtil.getLimbSwing(animationState, -0.2f, 0.0f, getArmsSpeed(), getArmsAmount(), true));
                } else {
                    right_arm.setRotZ(0);
                    left_arm.setRotZ(0);
                }
            }
            //Admiring
            else if (troll.hasBarteringItem() && !troll.isLeftHanded()){
                left_arm.setRotX(( 41f) * MathHelper.RADIANS_PER_DEGREE);
                left_arm.setRotY((-11) * MathHelper.RADIANS_PER_DEGREE);
                left_arm.setRotZ(( 13f) * MathHelper.RADIANS_PER_DEGREE);

                right_arm.setRotX(0);
                right_arm.setRotY(0);
                right_arm.setRotZ(0);

                left_leg.setRotX(0);
                left_leg.setRotY(0);
                left_leg.setRotZ(0);

                right_leg.setRotX(0);
                right_leg.setRotY(0);
                right_leg.setRotZ(0);
            } else if (troll.hasBarteringItem() && troll.isLeftHanded()){
                left_arm.setRotX(0);
                left_arm.setRotY(0);
                left_arm.setRotZ(0);

                right_arm.setRotX(( 41f) * MathHelper.RADIANS_PER_DEGREE);
                right_arm.setRotY(( 11) * MathHelper.RADIANS_PER_DEGREE);
                right_arm.setRotZ((-13f) * MathHelper.RADIANS_PER_DEGREE);

                left_leg.setRotX(0);
                left_leg.setRotY(0);
                left_leg.setRotZ(0);

                right_leg.setRotX(0);
                right_leg.setRotY(0);
                right_leg.setRotZ(0);
            } else if (troll.getEatingTime()!=-1 && !troll.isLeftHanded()){
                left_arm.setRotX(( ( 70f) * MathHelper.RADIANS_PER_DEGREE) + (this.getAnimationProgress(troll, animationState.getPartialTick())));
                left_arm.setRotY((-15f) * MathHelper.RADIANS_PER_DEGREE);
                left_arm.setRotZ(( 13f) * MathHelper.RADIANS_PER_DEGREE);

                right_arm.setRotX(0);
                right_arm.setRotY(0);
                right_arm.setRotZ(0);

                left_leg.setRotX(0);
                left_leg.setRotY(0);
                left_leg.setRotZ(0);

                right_leg.setRotX(0);
                right_leg.setRotY(0);
                right_leg.setRotZ(0);
            }
        }
    }

    protected float getAnimationProgress(RockTrollEntity troll, float partialTick) {
        float g = MathHelper.lerp(partialTick, troll.prevEatingProgress, troll.eatingProgress);
        float h = MathHelper.lerp(partialTick, troll.prevMaxEatingDeviation, troll.maxEatingDeviation);
        return (MathHelper.sin(g) + 1.0f) * h;
    }
}
