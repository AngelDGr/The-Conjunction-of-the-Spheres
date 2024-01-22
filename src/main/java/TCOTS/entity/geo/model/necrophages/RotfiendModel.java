package TCOTS.entity.geo.model.necrophages;

import TCOTS.TCOTS_Main;
import TCOTS.entity.necrophages.DrownerEntity;
import TCOTS.entity.necrophages.RotfiendEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class RotfiendModel extends GeoModel<RotfiendEntity> {
    @Override
    public Identifier getModelResource(RotfiendEntity animatable) {
        return new Identifier(TCOTS_Main.MOD_ID, "geo/necrophages/rotfiend.geo.json");
    }

    @Override
    public Identifier getTextureResource(RotfiendEntity animatable) {
        return new Identifier(TCOTS_Main.MOD_ID, "textures/entity/necrophages/rotfiend/rotfiend.png");
    }

    @Override
    public Identifier getAnimationResource(RotfiendEntity animatable) {
        return new Identifier(TCOTS_Main.MOD_ID, "animations/necrophages/rotfiend.animation.json");
    }

    @Override
    public void setCustomAnimations(RotfiendEntity entity, long instanceId, AnimationState<RotfiendEntity> animationState) {

        CoreGeoBone head = getAnimationProcessor().getBone("head");


        if (head != null) {
            EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
            head.setRotX(entityData.headPitch() * MathHelper.RADIANS_PER_DEGREE);
            head.setRotY(entityData.netHeadYaw() * MathHelper.RADIANS_PER_DEGREE);
        }
    }
}
