package TCOTS.entity.geo.model.necrophages;

import TCOTS.TCOTS_Main;
import TCOTS.entity.geo.model.QuadrupedGhoulModelBase;
import TCOTS.entity.necrophages.GhoulEntity;
import net.minecraft.resources.ResourceLocation;

public class GhoulModel extends QuadrupedGhoulModelBase<GhoulEntity> {
    @Override
    public ResourceLocation getModelResource(GhoulEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "geo/necrophages/ghoul.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(GhoulEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "textures/entity/necrophages/ghoul/ghoul.png");
    }

    @Override
    public ResourceLocation getAnimationResource(GhoulEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "animations/necrophages/ghoul.animation.json");
    }
}
