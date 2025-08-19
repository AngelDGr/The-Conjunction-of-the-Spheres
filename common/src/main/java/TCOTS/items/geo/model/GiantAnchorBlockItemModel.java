package TCOTS.items.geo.model;

import TCOTS.TCOTS_Main;
import TCOTS.items.blocks.GiantAnchorBlockItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class GiantAnchorBlockItemModel extends GeoModel<GiantAnchorBlockItem> {
    @Override
    public ResourceLocation getModelResource(final GiantAnchorBlockItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "geo/block/giant_anchor.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(final GiantAnchorBlockItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "textures/block/giant_anchor.png");
    }

    @Override
    public ResourceLocation getAnimationResource(final GiantAnchorBlockItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "animations/misc/dummy.animation.json");
    }
}
