package TCOTS.entity.geo.model.necrophages;

import TCOTS.TCOTS_Main;
import TCOTS.entity.geo.model.BipedGeoModelBase;
import TCOTS.entity.monsters.necrophages.ScurverEntity;
import net.minecraft.resources.ResourceLocation;

public class ScurverModel extends BipedGeoModelBase<ScurverEntity> {

    @Override
    public ResourceLocation getModelResource(final ScurverEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "geo/necrophages/scurver.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(final ScurverEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "textures/entity/necrophages/scurver.png");
    }

    @Override
    public ResourceLocation getAnimationResource(final ScurverEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "animations/necrophages/scurver.animation.json");
    }
}
