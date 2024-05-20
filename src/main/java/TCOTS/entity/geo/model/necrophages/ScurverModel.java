package TCOTS.entity.geo.model.necrophages;

import TCOTS.TCOTS_Main;
import TCOTS.entity.necrophages.ScurverEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class ScurverModel extends GeoModel<ScurverEntity> {

    @Override
    public Identifier getModelResource(ScurverEntity animatable) {
        return new Identifier(TCOTS_Main.MOD_ID, "geo/necrophages/scurver.geo.json");
    }

    @Override
    public Identifier getTextureResource(ScurverEntity animatable) {
        return new Identifier(TCOTS_Main.MOD_ID, "textures/entity/necrophages/scurver/scurver.png");
    }

    @Override
    public Identifier getAnimationResource(ScurverEntity animatable) {
        return new Identifier(TCOTS_Main.MOD_ID, "animations/necrophages/scurver.animation.json");
    }

    @Override
    public void setCustomAnimations(ScurverEntity animatable, long instanceId, AnimationState<ScurverEntity> animationState) {
        CoreGeoBone head = getAnimationProcessor().getBone("head");

        if (head != null) {
            EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
            head.setRotX(entityData.headPitch() * MathHelper.RADIANS_PER_DEGREE);
            head.setRotY(entityData.netHeadYaw() * MathHelper.RADIANS_PER_DEGREE);
        }
    }
}
