package TCOTS.entity.geo.model.necrophages;

import TCOTS.TCOTS_Main;
import TCOTS.entity.geo.model.BipedGeoModelBase;
import TCOTS.entity.necrophages.GraveHagEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.model.data.EntityModelData;

public class GraveHagModel extends BipedGeoModelBase<GraveHagEntity> {

    //xTODO: Fix the running animation
    @Override
    public Identifier getModelResource(GraveHagEntity animatable) {
        return Identifier.of(TCOTS_Main.MOD_ID, "geo/necrophages/grave_hag.geo.json");
    }

    @Override
    public Identifier getTextureResource(GraveHagEntity animatable) {
        return Identifier.of(TCOTS_Main.MOD_ID, "textures/entity/necrophages/grave_hag.png");
    }

    @Override
    public Identifier getAnimationResource(GraveHagEntity animatable) {
        return Identifier.of(TCOTS_Main.MOD_ID, "animations/necrophages/grave_hag.animation.json");
    }

    @Override
    protected boolean hasNormalHead(GraveHagEntity entity) {
        return false;
    }

    @Override
    public void setCustomAnimations(GraveHagEntity entity, long instanceId, AnimationState<GraveHagEntity> animationState) {
        super.setCustomAnimations(entity, instanceId, animationState);

        GeoBone head = getAnimationProcessor().getBone("head");
        GeoBone wholeBody = getAnimationProcessor().getBone("wholeBody");

        if (head!=null && wholeBody!=null) {
            EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
            if(entity.getIsRunning()){
                head.setRotY(entityData.netHeadYaw() * MathHelper.RADIANS_PER_DEGREE);
                head.setRotX(((entityData.headPitch()+60) * MathHelper.RADIANS_PER_DEGREE));
                wholeBody.setRotX(-(62.5f * MathHelper.RADIANS_PER_DEGREE));
                wholeBody.setPosY(-6);
            }
            else if(animationState.isMoving()){
                head.setRotY(((entityData.netHeadYaw()+17.5f) * MathHelper.RADIANS_PER_DEGREE));
                head.setRotX((entityData.headPitch() * MathHelper.RADIANS_PER_DEGREE));
                resetWholeBody(wholeBody);
            }
            else{
                head.setRotY(entityData.netHeadYaw() * MathHelper.RADIANS_PER_DEGREE);
                head.setRotX((entityData.headPitch() * MathHelper.RADIANS_PER_DEGREE));
                resetWholeBody(wholeBody);
            }
        }
    }

    private void resetWholeBody(GeoBone wholeBody){
        if(wholeBody.getRotX() != 0){
            wholeBody.setRotX(0);
        }
        if(wholeBody.getPosY() != 0){
            wholeBody.setPosY(0);
        }
    }
}


