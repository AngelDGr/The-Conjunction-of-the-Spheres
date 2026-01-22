package mors.tcots.client.geo.model.entity.necrophage;

import mors.tcots.TCOTS_Main;
import mors.tcots.client.geo.model.defaulted.monsters.DefaultedNecrophageModel;
import mors.tcots.entity.monsters.necrophages.WaterHagEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.model.data.EntityModelData;

public class WaterHagModel extends DefaultedNecrophageModel<WaterHagEntity> {

    public WaterHagModel() {
        super(ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "water_hag"), false);
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
