package mors.tcots.client.geo.model.entity.necrophage;

import mors.tcots.TCOTS_Main;
import mors.tcots.client.geo.model.defaulted.monsters.DefaultedNecrophageModel;
import mors.tcots.entity.monsters.necrophages.DrownerEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.model.data.EntityModelData;

public class DrownerModel extends DefaultedNecrophageModel<DrownerEntity> {

    public DrownerModel() {
        super(ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "drowner"), true);
    }

    @Override
    public void setCustomAnimations(final DrownerEntity entity, final long instanceId, final AnimationState<DrownerEntity> animationState) {
//        super.setCustomAnimations(entity, instanceId, animationState);
        final GeoBone head = getAnimationProcessor().getBone("head");
        final GeoBone wholeBody = getAnimationProcessor().getBone("wholeBody");
        final EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
        if(head!=null) {
            //Swimming
            if (entity.getSwimmingDataTracker()) {
                wholeBody.setRotX((entityData.headPitch() - 65)* Mth.DEG_TO_RAD);
                wholeBody.setRotY((entityData.netHeadYaw())* Mth.DEG_TO_RAD);
                head.setRotX((55*Mth.DEG_TO_RAD));
            }
            //OnLand
            else {
                if(wholeBody.getRotX()!=0){wholeBody.setRotX(0);}
                if(wholeBody.getRotY()!=0){wholeBody.setRotY(0);}


                if(animationState.isMoving() && entity.isAggressive()){

                    head.setRotY((entityData.netHeadYaw() * Mth.DEG_TO_RAD));
                }
                else {
                    head.setRotX((entityData.headPitch()+15) * Mth.DEG_TO_RAD);
                    head.setRotY(entityData.netHeadYaw() * Mth.DEG_TO_RAD);
                }


//                super.setCustomAnimations(entity, instanceId, animationState);
            }
        }
    }

}
