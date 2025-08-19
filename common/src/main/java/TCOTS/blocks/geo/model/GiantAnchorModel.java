package TCOTS.blocks.geo.model;

import TCOTS.TCOTS_Main;
import TCOTS.blocks.entity.GiantAnchorBlockEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class GiantAnchorModel extends GeoModel<GiantAnchorBlockEntity> {
    @Override
    public ResourceLocation getModelResource(final GiantAnchorBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "geo/block/giant_anchor.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(final GiantAnchorBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "textures/block/giant_anchor.png");
    }

    @Override
    public ResourceLocation getAnimationResource(final GiantAnchorBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "animations/misc/dummy.animation.json");
    }
}
