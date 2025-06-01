package TCOTS.items.geo.model;

import TCOTS.TCOTS_Main;
import TCOTS.items.weapons.GiantAnchorItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class GiantAnchorItemModel extends GeoModel<GiantAnchorItem> {
    @Override
    public ResourceLocation getModelResource(GiantAnchorItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "geo/anchor.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(GiantAnchorItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "textures/entity/anchor.png");
    }

    @Override
    public ResourceLocation getAnimationResource(GiantAnchorItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "animations/misc/dummy.animation.json");
    }
}
