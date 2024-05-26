package TCOTS.entity.geo.model.necrophages;

import TCOTS.TCOTS_Main;
import TCOTS.entity.necrophages.DevourerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class DevourerModel extends GeoModel<DevourerEntity> {
    @Override
    public Identifier getModelResource(DevourerEntity animatable) {
        return new Identifier(TCOTS_Main.MOD_ID, "geo/necrophages/devourer.geo.json");
    }

    @Override
    public Identifier getTextureResource(DevourerEntity animatable) {
        return new Identifier(TCOTS_Main.MOD_ID, "textures/entity/necrophages/devourer.png");
    }

    @Override
    public Identifier getAnimationResource(DevourerEntity animatable) {
        return new Identifier(TCOTS_Main.MOD_ID, "animations/necrophages/devourer.animation.json");
    }

    @Override
    public void setCustomAnimations(DevourerEntity entity, long instanceId, AnimationState<DevourerEntity> animationState) {
        CoreGeoBone head = getAnimationProcessor().getBone("head");

        if (head != null) {
            EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
            if(animationState.isMoving() && entity.isAttacking()){
                head.setRotY((entityData.netHeadYaw() * MathHelper.RADIANS_PER_DEGREE));
                head.setRotX(((entityData.headPitch()+10) * MathHelper.RADIANS_PER_DEGREE));
            }
            else{
                head.setRotX(entityData.headPitch() * MathHelper.RADIANS_PER_DEGREE);
                head.setRotY(entityData.netHeadYaw() * MathHelper.RADIANS_PER_DEGREE);
            }
        }
    }
}
