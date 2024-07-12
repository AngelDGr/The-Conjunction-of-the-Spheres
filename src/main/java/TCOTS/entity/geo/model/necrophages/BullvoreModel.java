package TCOTS.entity.geo.model.necrophages;

import TCOTS.TCOTS_Main;
import TCOTS.entity.geo.model.BipedGeoModelBase;
import TCOTS.entity.necrophages.BullvoreEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.core.animation.AnimationState;

public class BullvoreModel extends BipedGeoModelBase<BullvoreEntity> {
    protected boolean hasArmZMovement(){
        return false;
    }

    @Override
    public Identifier getModelResource(BullvoreEntity animatable) {
        return new Identifier(TCOTS_Main.MOD_ID, "geo/necrophages/bullvore.geo.json");
    }

    @Override
    public Identifier getTextureResource(BullvoreEntity animatable) {
        return new Identifier(TCOTS_Main.MOD_ID, "textures/entity/necrophages/bullvore.png");
    }

    @Override
    public Identifier getAnimationResource(BullvoreEntity animatable) {
        return new Identifier(TCOTS_Main.MOD_ID, "animations/necrophages/bullvore.animation.json");
    }

    @Override
    public void setCustomAnimations(BullvoreEntity entity, long instanceId, AnimationState<BullvoreEntity> animationState) {
        super.setCustomAnimations(entity, instanceId, animationState);
    }
}
