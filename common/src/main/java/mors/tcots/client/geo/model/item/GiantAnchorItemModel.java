package mors.tcots.client.geo.model.item;

import mors.tcots.TCOTS_Main;
import mors.tcots.items.weapons.GiantAnchorItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class GiantAnchorItemModel extends GeoModel<GiantAnchorItem> {
    @Override
    public ResourceLocation getModelResource(final GiantAnchorItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "geo/anchor.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(final GiantAnchorItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "textures/entity/anchor.png");
    }

    @Override
    public ResourceLocation getAnimationResource(final GiantAnchorItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "animations/misc/dummy.animation.json");
    }
}
