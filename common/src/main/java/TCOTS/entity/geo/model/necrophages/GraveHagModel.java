package TCOTS.entity.geo.model.necrophages;

import TCOTS.TCOTS_Main;
import TCOTS.entity.geo.model.BipedGeoModelBase;
import TCOTS.entity.monsters.necrophages.GraveHagEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.model.data.EntityModelData;

public class GraveHagModel extends BipedGeoModelBase<GraveHagEntity> {

    //xTODO: Fix the running animation
    @Override
    public ResourceLocation getModelResource(final GraveHagEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "geo/necrophages/grave_hag.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(final GraveHagEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "textures/entity/necrophages/grave_hag.png");
    }

    @Override
    public ResourceLocation getAnimationResource(final GraveHagEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "animations/necrophages/grave_hag.animation.json");
    }

    @Override
    protected boolean hasNormalHead(final GraveHagEntity entity) {
        return false;
    }

    @Override
    public void setCustomAnimations(final GraveHagEntity entity, final long instanceId, final AnimationState<GraveHagEntity> animationState) {
        super.setCustomAnimations(entity, instanceId, animationState);

        final GeoBone head = getAnimationProcessor().getBone("head");
        final GeoBone wholeBody = getAnimationProcessor().getBone("wholeBody");

        if (head!=null && wholeBody!=null) {
            final EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
            if(entity.getIsRunning()){
                head.setRotY(entityData.netHeadYaw() * Mth.DEG_TO_RAD);
                head.setRotX(((entityData.headPitch()+60) * Mth.DEG_TO_RAD));
                wholeBody.setRotX(-(62.5f * Mth.DEG_TO_RAD));
                wholeBody.setPosY(-6);
            }
            else if(animationState.isMoving()){
                head.setRotY(((entityData.netHeadYaw()+17.5f) * Mth.DEG_TO_RAD));
                head.setRotX((entityData.headPitch() * Mth.DEG_TO_RAD));
                resetWholeBody(wholeBody);
            }
            else{
                head.setRotY(entityData.netHeadYaw() * Mth.DEG_TO_RAD);
                head.setRotX((entityData.headPitch() * Mth.DEG_TO_RAD));
                resetWholeBody(wholeBody);
            }
        }
    }

    private void resetWholeBody(final GeoBone wholeBody){
        if(wholeBody.getRotX() != 0){
            wholeBody.setRotX(0);
        }
        if(wholeBody.getPosY() != 0){
            wholeBody.setPosY(0);
        }
    }
}


