package TCOTS.entity.geo.model.necrophages;

import TCOTS.TCOTS_Main;
import TCOTS.entity.geo.model.BipedGeoModelBase;
import TCOTS.entity.misc.FoglingEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.model.data.EntityModelData;

public class FoglingModel extends BipedGeoModelBase<FoglingEntity>{
    @Override
    public ResourceLocation getModelResource(FoglingEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "geo/necrophages/foglet.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(FoglingEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "textures/entity/necrophages/foglet/fogling.png");
    }

    @Override
    public ResourceLocation getAnimationResource(FoglingEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "animations/necrophages/foglet.animation.json");
    }

    @Override
    public RenderType getRenderType(FoglingEntity animatable, ResourceLocation texture) {
        return RenderType.entityTranslucent(texture);
    }

    @Override
    protected boolean hasNormalHead(FoglingEntity entity) {
        return false;
    }

    @Override
    protected float getArmsAmount(FoglingEntity entity) {
        return 0.8f;
    }

    @Override
    protected float getLegsAmount(FoglingEntity entity) {
        return 1.0f;
    }

    @Override
    public void setCustomAnimations(FoglingEntity entity, long instanceId, AnimationState<FoglingEntity> animationState) {
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