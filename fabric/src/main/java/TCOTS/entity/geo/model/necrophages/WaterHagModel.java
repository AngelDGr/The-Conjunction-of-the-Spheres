package TCOTS.entity.geo.model.necrophages;

import TCOTS.TCOTS_Main;
import TCOTS.entity.geo.model.BipedGeoModelBase;
import TCOTS.entity.necrophages.WaterHagEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.model.data.EntityModelData;

public class WaterHagModel extends BipedGeoModelBase<WaterHagEntity> {
    @Override
    public ResourceLocation getModelResource(WaterHagEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "geo/necrophages/water_hag.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(WaterHagEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "textures/entity/necrophages/water_hag.png");
    }

    @Override
    public ResourceLocation getAnimationResource(WaterHagEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "animations/necrophages/water_hag.animation.json");
    }

    @Override
    protected boolean hasNormalHead(WaterHagEntity entity) {
        return false;
    }

    @Override
    public void setCustomAnimations(WaterHagEntity entity, long instanceId, AnimationState<WaterHagEntity> animationState) {

        super.setCustomAnimations(entity, instanceId, animationState);

        GeoBone head = getAnimationProcessor().getBone("head");

        if (head != null) {
            EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
                if(animationState.isMoving()){
                head.setRotY(((entityData.netHeadYaw()+17.5f) * Mth.DEG_TO_RAD));
                head.setRotX((entityData.headPitch() * Mth.DEG_TO_RAD));
            }
            else{
                head.setRotY(entityData.netHeadYaw() * Mth.DEG_TO_RAD);
                head.setRotX((entityData.headPitch() * Mth.DEG_TO_RAD));
            }
        }


    }
}
