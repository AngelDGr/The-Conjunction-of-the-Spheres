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
        return new Identifier(TCOTS_Main.MOD_ID, "geo/necrophages/drowner.geo.json");
    }

    @Override
    public Identifier getTextureResource(DrownerEntity animatable) {
        return new Identifier(TCOTS_Main.MOD_ID, "textures/entity/necrophages/drowner/drowner.png");
    }

    @Override
    public Identifier getAnimationResource(DrownerEntity animatable) {
        return new Identifier(TCOTS_Main.MOD_ID, "animations/necrophages/drowner.animation.json");
    }

    @Override
    public void setCustomAnimations(DrownerEntity animatable, long instanceId, AnimationState<DrownerEntity> animationState) {

        CoreGeoBone head = getAnimationProcessor().getBone("head");
        EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);

        //Swimming
        if(animatable.isSubmergedInWater()){
            head.setRotX((entityData.headPitch() + 70) * MathHelper.RADIANS_PER_DEGREE);
        }
        //OnLand
        else {
            if (animatable.isAttacking()) {
                head.setRotX((entityData.headPitch() + 30) * MathHelper.RADIANS_PER_DEGREE);
            } else {
                head.setRotX((entityData.headPitch() + 20) * MathHelper.RADIANS_PER_DEGREE);
            }
            head.setRotY(entityData.netHeadYaw() * MathHelper.RADIANS_PER_DEGREE);
        }
    }
}
