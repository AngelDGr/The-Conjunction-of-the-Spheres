package TCOTS.entity.geo.model.necrophages;

import TCOTS.TCOTS_Main;
import TCOTS.entity.geo.model.BipedGeoModelBase;
import TCOTS.entity.necrophages.GraveirEntity;
import net.minecraft.util.Identifier;

public class GraveirModel extends BipedGeoModelBase<GraveirEntity> {
    @Override
    public Identifier getModelResource(GraveirEntity animatable) {
        return Identifier.of(TCOTS_Main.MOD_ID, "geo/necrophages/graveir.geo.json");
    }

    @Override
    public Identifier getTextureResource(GraveirEntity animatable) {
        return Identifier.of(TCOTS_Main.MOD_ID, "textures/entity/necrophages/graveir/graveir.png");
    }

    @Override
    public Identifier getAnimationResource(GraveirEntity animatable) {
        return Identifier.of(TCOTS_Main.MOD_ID, "animations/necrophages/graveir.animation.json");
    }

    @Override
    protected boolean hasArmZMovement(GraveirEntity entity) {
        return false;
    }
}
