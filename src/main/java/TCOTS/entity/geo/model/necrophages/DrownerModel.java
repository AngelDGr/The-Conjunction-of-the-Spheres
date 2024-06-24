package TCOTS.entity.geo.model.necrophages;

import TCOTS.TCOTS_Main;
import TCOTS.entity.geo.model.BipedGeoModelBase;
import TCOTS.entity.necrophages.DrownerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.data.EntityModelData;



public class DrownerModel extends BipedGeoModelBase<DrownerEntity> {

    @Override
    public Identifier getModelResource(DrownerEntity animatable) {
        return new Identifier(TCOTS_Main.MOD_ID, "geo/necrophages/drowner.geo.json");
    }

    @Override
    public Identifier getTextureResource(DrownerEntity animatable) {
        return new Identifier(TCOTS_Main.MOD_ID, "textures/entity/necrophages/drowner/drowner.png");
    }

    @Override
    public Identifier getAnimationResource(DrownerEntity animatable) {
        return new Identifier(TCOTS_Main.MOD_ID, "animations/necrophages/drowner.animation.json");
    }

    @Override
    protected boolean hasNormalHead() {
        return false;
    }

    @Override
    public void setCustomAnimations(DrownerEntity entity, long instanceId, AnimationState<DrownerEntity> animationState) {

        CoreGeoBone head = getAnimationProcessor().getBone("head");
        CoreGeoBone wholeBody = getAnimationProcessor().getBone("wholeBody");
        EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
        if(head!=null) {
            //Swimming
            if (entity.getSwimmingDataTracker()) {
                wholeBody.setRotX((entityData.headPitch() - 65)* MathHelper.RADIANS_PER_DEGREE);
                wholeBody.setRotY((entityData.netHeadYaw())* MathHelper.RADIANS_PER_DEGREE);
                head.setRotX((55*MathHelper.RADIANS_PER_DEGREE));
            }
            //OnLand
            else {
                if(wholeBody.getRotX()!=0){wholeBody.setRotX(0);}

                if(wholeBody.getRotY()!=0){wholeBody.setRotY(0);}

                CoreGeoBone Thingy =  getAnimationProcessor().getBone("Thingy");

                if(Thingy!=null){
                    Thingy.setRotX((float)-(Math.sin(animationState.getLimbSwing()*getLegsSpeed())*(animationState.getLimbSwingAmount()*getLegsAmount())));
                }


                if(animationState.isMoving() && entity.isAttacking()){
                    head.setRotX(((entityData.headPitch()+getHeadExtraInAttacking()+15) * MathHelper.RADIANS_PER_DEGREE));
                    head.setRotY((entityData.netHeadYaw() * MathHelper.RADIANS_PER_DEGREE));
                }
                else{
                    head.setRotX((entityData.headPitch()+15) * MathHelper.RADIANS_PER_DEGREE);
                    head.setRotY(entityData.netHeadYaw() * MathHelper.RADIANS_PER_DEGREE);
                }


                super.setCustomAnimations(entity, instanceId, animationState);
            }
        }
    }

}
