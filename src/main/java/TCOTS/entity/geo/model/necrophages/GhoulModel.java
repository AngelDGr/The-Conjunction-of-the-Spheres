package TCOTS.entity.geo.model.necrophages;

import TCOTS.TCOTS_Main;
import TCOTS.entity.necrophages.GhoulEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class GhoulModel extends GeoModel<GhoulEntity> {
    @Override
    public Identifier getModelResource(GhoulEntity animatable) {
        return new Identifier(TCOTS_Main.MOD_ID, "geo/necrophages/ghoul.geo.json");
    }

    @Override
    public Identifier getTextureResource(GhoulEntity animatable) {
        return new Identifier(TCOTS_Main.MOD_ID, "textures/entity/necrophages/ghoul/ghoul.png");
    }

    @Override
    public Identifier getAnimationResource(GhoulEntity animatable) {
        return new Identifier(TCOTS_Main.MOD_ID, "animations/necrophages/ghoul.animation.json");
    }

    @Override
    public void setCustomAnimations(GhoulEntity entity, long instanceId, AnimationState<GhoulEntity> animationState) {

        CoreGeoBone head = getAnimationProcessor().getBone("head");
        if (head != null) {
            EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
            head.setRotX((entityData.headPitch()+90f) * MathHelper.RADIANS_PER_DEGREE);
            head.setRotZ((entityData.netHeadYaw() * MathHelper.RADIANS_PER_DEGREE));
        }
    }
}
