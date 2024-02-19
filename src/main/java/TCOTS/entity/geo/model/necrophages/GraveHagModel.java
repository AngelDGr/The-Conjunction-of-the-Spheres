package TCOTS.entity.geo.model.necrophages;

import TCOTS.TCOTS_Main;
import TCOTS.entity.necrophages.GraveHagEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class GraveHagModel extends GeoModel<GraveHagEntity> {
    @Override
    public Identifier getModelResource(GraveHagEntity animatable) {
        return new Identifier(TCOTS_Main.MOD_ID, "geo/necrophages/grave_hag.geo.json");
    }

    @Override
    public Identifier getTextureResource(GraveHagEntity animatable) {
        return new Identifier(TCOTS_Main.MOD_ID, "textures/entity/necrophages/grave_hag/grave_hag.png");
    }

    @Override
    public Identifier getAnimationResource(GraveHagEntity animatable) {
        return new Identifier(TCOTS_Main.MOD_ID, "animations/necrophages/grave_hag.animation.json");
    }

    @Override
    public void setCustomAnimations(GraveHagEntity entity, long instanceId, AnimationState<GraveHagEntity> animationState) {

        CoreGeoBone head = getAnimationProcessor().getBone("head");


        if (head != null) {

            EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
            if(animationState.isMoving()){
                head.setRotY(((entityData.netHeadYaw()+17.5f) * MathHelper.RADIANS_PER_DEGREE));
            }
            else{
                head.setRotY(entityData.netHeadYaw() * MathHelper.RADIANS_PER_DEGREE);
            }

            head.setRotX((entityData.headPitch() * MathHelper.RADIANS_PER_DEGREE));


        }
    }
}
