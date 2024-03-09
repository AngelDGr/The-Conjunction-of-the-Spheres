package TCOTS.entity.geo.model.necrophages;

import TCOTS.TCOTS_Main;
import TCOTS.entity.necrophages.FogletEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class FogletModel extends GeoModel<FogletEntity> {
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
    public void setCustomAnimations(FogletEntity entity, long instanceId, AnimationState<FogletEntity> animationState) {

        CoreGeoBone head = getAnimationProcessor().getBone("head");


        if (head != null) {
            EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
            head.setRotX(entityData.headPitch() * MathHelper.RADIANS_PER_DEGREE);
            head.setRotY(entityData.netHeadYaw() * MathHelper.RADIANS_PER_DEGREE);
        }
    }
}
