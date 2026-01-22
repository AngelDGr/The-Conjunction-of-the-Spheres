package mors.tcots.client.geo.model.defaulted;

import mors.tcots.TCOTS_Main;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.model.DefaultedItemGeoModel;

public abstract class DefaultedArmorModel<T extends GeoAnimatable> extends DefaultedItemGeoModel<T> {

    public DefaultedArmorModel(final ResourceLocation assetSubpath) {
        super(assetSubpath);
    }

    @Override
    public ResourceLocation getAnimationResource(final T animatable) {
        return ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "animations/misc/dummy.animation.json");
    }

    @Override
    protected String subtype() {
        return "item/armor";
    }

    @Override
    public ResourceLocation buildFormattedTexturePath(final ResourceLocation basePath) {
        return basePath.withPath("textures/models/armor/" + basePath.getPath() + ".png");
    }
}
