package TCOTS.entity.geo.model.necrophages;

import TCOTS.TCOTS_Main;
import TCOTS.entity.geo.model.BipedGeoModelBase;
import TCOTS.entity.monsters.necrophages.GraveirEntity;
import net.minecraft.resources.ResourceLocation;

public class GraveirModel extends BipedGeoModelBase<GraveirEntity> {
    @Override
    public ResourceLocation getModelResource(final GraveirEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "geo/necrophages/graveir.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(final GraveirEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "textures/entity/necrophages/graveir/graveir.png");
    }

    @Override
    public ResourceLocation getAnimationResource(final GraveirEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "animations/necrophages/graveir.animation.json");
    }

    @Override
    protected boolean hasArmZMovement(final GraveirEntity entity) {
        return false;
    }
}
