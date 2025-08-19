package TCOTS.entity.geo.model.necrophages;

import TCOTS.TCOTS_Main;
import TCOTS.entity.geo.model.BipedGeoModelBase;
import TCOTS.entity.monsters.necrophages.WaterHagEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.model.data.EntityModelData;

public class WaterHagModel extends BipedGeoModelBase<WaterHagEntity> {
    @Override
    public ResourceLocation getModelResource(final WaterHagEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "geo/necrophages/water_hag.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(final WaterHagEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "textures/entity/necrophages/water_hag.png");
    }

    @Override
    public ResourceLocation getAnimationResource(final WaterHagEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "animations/necrophages/water_hag.animation.json");
    }

    @Override
    protected boolean hasNormalHead(final WaterHagEntity entity) {
        return false;
    }

    @Override
    public void setCustomAnimations(final WaterHagEntity entity, final long instanceId, final AnimationState<WaterHagEntity> animationState) {

        super.setCustomAnimations(entity, instanceId, animationState);

        final GeoBone head = getAnimationProcessor().getBone("head");

        if (head != null) {
            final EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
                if(animationState.isMoving()){
                head.setRotY(((entityData.netHeadYaw()+17.5f) * Mth.DEG_TO_RAD));
                head.setRotX((entityData.headPitch() * Mth.DEG_TO_RAD));
            }
            else{
                head.setRotY(entityData.netHeadYaw() * Mth.DEG_TO_RAD);
                head.setRotX((entityData.headPitch() * Mth.DEG_TO_RAD));
            }
        }


    }
}
