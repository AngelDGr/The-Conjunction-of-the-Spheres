package TCOTS.entity.geo.model.necrophages;

import TCOTS.TCOTS_Main;
import TCOTS.entity.geo.model.BipedGeoModelBase;
import TCOTS.entity.necrophages.DrownerEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.model.data.EntityModelData;



public class DrownerModel extends BipedGeoModelBase<DrownerEntity> {

    @Override
    public ResourceLocation getModelResource(DrownerEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "geo/necrophages/drowner.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(DrownerEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "textures/entity/necrophages/drowner/drowner.png");
    }

    @Override
    public ResourceLocation getAnimationResource(DrownerEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "animations/necrophages/drowner.animation.json");
    }

    @Override
    protected boolean hasNormalHead(DrownerEntity drowner) {
        return false;
    }

    @Override
    public void setCustomAnimations(DrownerEntity entity, long instanceId, AnimationState<DrownerEntity> animationState) {

        GeoBone head = getAnimationProcessor().getBone("head");
        GeoBone wholeBody = getAnimationProcessor().getBone("wholeBody");
        EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
        if(head!=null) {
            //Swimming
            if (entity.getSwimmingDataTracker()) {
                wholeBody.setRotX((entityData.headPitch() - 65)* Mth.DEG_TO_RAD);
                wholeBody.setRotY((entityData.netHeadYaw())* Mth.DEG_TO_RAD);
                head.setRotX((55*Mth.DEG_TO_RAD));
            }
            //OnLand
            else {
                if(wholeBody.getRotX()!=0){wholeBody.setRotX(0);}

                if(wholeBody.getRotY()!=0){wholeBody.setRotY(0);}

                GeoBone Thingy =  getAnimationProcessor().getBone("Thingy");

                if(Thingy!=null){
                    Thingy.setRotX((float)-(Math.sin(animationState.getLimbSwing()*getLegsSpeed(entity))*(animationState.getLimbSwingAmount()*getLegsAmount(entity))));
                }


                if(animationState.isMoving() && entity.isAggressive()){
                    head.setRotX(((entityData.headPitch()+getHeadExtraInAttacking(entity)+15) * Mth.DEG_TO_RAD));
                    head.setRotY((entityData.netHeadYaw() * Mth.DEG_TO_RAD));
                }
                else{
                    head.setRotX((entityData.headPitch()+15) * Mth.DEG_TO_RAD);
                    head.setRotY(entityData.netHeadYaw() * Mth.DEG_TO_RAD);
                }


                super.setCustomAnimations(entity, instanceId, animationState);
            }
        }
    }

}
