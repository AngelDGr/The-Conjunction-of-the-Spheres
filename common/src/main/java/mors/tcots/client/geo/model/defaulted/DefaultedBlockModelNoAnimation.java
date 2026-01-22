package mors.tcots.client.geo.model.defaulted;

import mors.tcots.TCOTS_Main;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.model.DefaultedBlockGeoModel;

public abstract class DefaultedBlockModelNoAnimation<T extends GeoAnimatable> extends DefaultedBlockGeoModel<T> {

    public DefaultedBlockModelNoAnimation(final ResourceLocation assetSubpath) {
        super(assetSubpath);
    }

    @Override
    public ResourceLocation getAnimationResource(final T animatable) {
        return ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "animations/misc/dummy.animation.json");
    }
}
