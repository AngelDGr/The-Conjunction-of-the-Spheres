package TCOTS.entity.geo.model.necrophages;

import TCOTS.TCOTS_Main;
import TCOTS.entity.geo.model.BipedGeoModelBase;
import TCOTS.entity.necrophages.BullvoreEntity;
import net.minecraft.util.Identifier;

public class BullvoreModel extends BipedGeoModelBase<BullvoreEntity> {

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
    protected boolean hasArmZMovement(BullvoreEntity entity) {
        return entity.isCharging();
    }

    @Override
    protected float getLegsSpeed(BullvoreEntity entity) {
        return entity.isCharging()? 0.8f :0.5f;
    }

    @Override
    protected float getArmsAmount(BullvoreEntity entity) {
        return entity.isCharging()? 0.8f :0.6f;
    }
}
