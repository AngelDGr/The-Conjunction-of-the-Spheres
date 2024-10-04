package TCOTS.entity.geo.model.necrophages;

import TCOTS.TCOTS_Main;
import TCOTS.entity.geo.model.BipedGeoModelBase;
import TCOTS.entity.necrophages.FogletEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.data.EntityModelData;

public class FogletModel extends BipedGeoModelBase<FogletEntity> {
    @Override
    public Identifier getModelResource(FogletEntity animatable) {
        return new Identifier(TCOTS_Main.MOD_ID, "geo/necrophages/foglet.geo.json");
    }

    @Override
    public Identifier getTextureResource(FogletEntity animatable) {
        return new Identifier(TCOTS_Main.MOD_ID, "textures/entity/necrophages/foglet/foglet.png");
    }

    @Override
    public Identifier getAnimationResource(FogletEntity animatable) {
        return new Identifier(TCOTS_Main.MOD_ID, "animations/necrophages/foglet.animation.json");
    }

    @Override
    public RenderLayer getRenderType(FogletEntity animatable, Identifier texture) {
        return RenderLayer.getEntityTranslucent(texture);
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

        CoreGeoBone head = getAnimationProcessor().getBone("head");
        if (head != null) {
            EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
            if(animationState.isMoving()){
                head.setRotY(((entityData.netHeadYaw()+17.5f) * MathHelper.RADIANS_PER_DEGREE));
                head.setRotX((entityData.headPitch() * MathHelper.RADIANS_PER_DEGREE));
            }
            else{
                head.setRotY(entityData.netHeadYaw() * MathHelper.RADIANS_PER_DEGREE);
                head.setRotX((entityData.headPitch() * MathHelper.RADIANS_PER_DEGREE));
            }
        }

    }
}
