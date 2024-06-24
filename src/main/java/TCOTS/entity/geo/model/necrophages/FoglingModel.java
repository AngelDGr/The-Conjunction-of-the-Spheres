package TCOTS.entity.geo.model.necrophages;

import TCOTS.TCOTS_Main;
import TCOTS.entity.geo.model.BipedGeoModelBase;
import TCOTS.entity.misc.FoglingEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.data.EntityModelData;

public class FoglingModel extends BipedGeoModelBase<FoglingEntity>{
    @Override
    public Identifier getModelResource(FoglingEntity animatable) {
        return new Identifier(TCOTS_Main.MOD_ID, "geo/necrophages/foglet.geo.json");
    }

    @Override
    public Identifier getTextureResource(FoglingEntity animatable) {
        return new Identifier(TCOTS_Main.MOD_ID, "textures/entity/necrophages/foglet/fogling.png");
    }

    @Override
    public Identifier getAnimationResource(FoglingEntity animatable) {
        return new Identifier(TCOTS_Main.MOD_ID, "animations/necrophages/foglet.animation.json");
    }

    @Override
    public RenderLayer getRenderType(FoglingEntity animatable, Identifier texture) {
        return RenderLayer.getEntityTranslucent(texture);
    }

    @Override
    protected boolean hasNormalHead() {
        return false;
    }

    @Override
    protected float getArmsAmount() {
        return 0.8f;
    }

    @Override
    protected float getLegsAmount() {
        return 1.0f;
    }

    @Override
    public void setCustomAnimations(FoglingEntity entity, long instanceId, AnimationState<FoglingEntity> animationState) {
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