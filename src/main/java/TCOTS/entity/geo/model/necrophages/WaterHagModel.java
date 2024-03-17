package TCOTS.entity.geo.model.necrophages;

import TCOTS.TCOTS_Main;
import TCOTS.entity.necrophages.WaterHagEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class WaterHagModel extends GeoModel<WaterHagEntity> {
    @Override
    public Identifier getModelResource(WaterHagEntity animatable) {
        return new Identifier(TCOTS_Main.MOD_ID, "geo/necrophages/water_hag.geo.json");
    }

    @Override
    public Identifier getTextureResource(WaterHagEntity animatable) {
        return new Identifier(TCOTS_Main.MOD_ID, "textures/entity/necrophages/water_hag/water_hag.png");
    }

    @Override
    public Identifier getAnimationResource(WaterHagEntity animatable) {
        return new Identifier(TCOTS_Main.MOD_ID, "animations/necrophages/water_hag.animation.json");
    }

    @Override
    public void setCustomAnimations(WaterHagEntity entity, long instanceId, AnimationState<WaterHagEntity> animationState) {

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