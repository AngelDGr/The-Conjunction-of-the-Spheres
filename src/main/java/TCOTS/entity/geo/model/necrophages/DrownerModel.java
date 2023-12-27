package TCOTS.entity.geo.model.necrophages;

import TCOTS.TCOTS_Main;
import TCOTS.entity.necrophages.DrownerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class DrownerModel extends GeoModel<DrownerEntity> {
    @Override
    public Identifier getModelResource(DrownerEntity animatable) {
        return new Identifier(TCOTS_Main.MODID, "geo/necrophages/drowner.geo.json");
    }

    @Override
    public Identifier getTextureResource(DrownerEntity animatable) {
        return new Identifier(TCOTS_Main.MODID, "textures/entity/necrophages/drowner/drowner.png");
    }

    @Override
    public Identifier getAnimationResource(DrownerEntity animatable) {
        return new Identifier(TCOTS_Main.MODID, "animations/necrophages/drowner.animation.json");
    }

    @Override
    public void setCustomAnimations(DrownerEntity animatable, long instanceId, AnimationState<DrownerEntity> animationState) {
        CoreGeoBone head = getAnimationProcessor().getBone("head");

        if (head != null) {
            EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
            head.setRotX(entityData.headPitch() * MathHelper.RADIANS_PER_DEGREE);
            head.setRotY(entityData.netHeadYaw() * MathHelper.RADIANS_PER_DEGREE);
        }
    }
}
