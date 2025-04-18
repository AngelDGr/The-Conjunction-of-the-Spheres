package TCOTS.entity.geo.model.necrophages;

import TCOTS.TCOTS_Main;
import TCOTS.entity.geo.model.BipedGeoModelBase;
import TCOTS.entity.necrophages.GraveirEntity;
import net.minecraft.resources.ResourceLocation;

public class GraveirModel extends BipedGeoModelBase<GraveirEntity> {
    @Override
    public ResourceLocation getModelResource(GraveirEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "geo/necrophages/graveir.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(GraveirEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "textures/entity/necrophages/graveir/graveir.png");
    }

    @Override
    public ResourceLocation getAnimationResource(GraveirEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "animations/necrophages/graveir.animation.json");
    }

    @Override
    protected boolean hasArmZMovement(GraveirEntity entity) {
        return false;
    }
}
