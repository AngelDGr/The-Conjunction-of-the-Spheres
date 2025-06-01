package TCOTS.entity.geo.model.necrophages;

import TCOTS.TCOTS_Main;
import TCOTS.entity.geo.model.BipedGeoModelBase;
import TCOTS.entity.necrophages.FogletEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.model.data.EntityModelData;

public class FogletModel extends BipedGeoModelBase<FogletEntity> {
    @Override
    public ResourceLocation getModelResource(FogletEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "geo/necrophages/foglet.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(FogletEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "textures/entity/necrophages/foglet/foglet.png");
    }

    @Override
    public ResourceLocation getAnimationResource(FogletEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "animations/necrophages/foglet.animation.json");
    }

    @Override
    public RenderType getRenderType(FogletEntity animatable, ResourceLocation texture) {
        return RenderType.entityTranslucent(texture);
    }

    @Override
    protected boolean hasNormalHead(FogletEntity entity) {
        return false;
    }

    @Override
    protected float getArmsAmount(FogletEntity entity) {
        return 0.8f;
    }

    @Override
    protected float getLegsAmount(FogletEntity entity) {
        return 1.0f;
    }

    @Override
    public void setCustomAnimations(FogletEntity entity, long instanceId, AnimationState<FogletEntity> animationState) {
        super.setCustomAnimations(entity,instanceId,animationState);

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
