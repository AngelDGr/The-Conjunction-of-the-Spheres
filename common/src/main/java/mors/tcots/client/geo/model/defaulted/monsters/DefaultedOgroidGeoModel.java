package mors.tcots.client.geo.model.defaulted.monsters;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public abstract class DefaultedOgroidGeoModel<T extends GeoAnimatable> extends DefaultedEntityGeoModel<T> {
    public DefaultedOgroidGeoModel(final ResourceLocation assetSubpath, final boolean turnsHead) {
        super(assetSubpath, turnsHead);
    }

    @Override
    protected String subtype() {
        return "entity/ogroid";
    }

    @Override
    public ResourceLocation buildFormattedTexturePath(final ResourceLocation basePath) {
        return basePath.withPath("textures/entity/monster/ogroid/" + basePath.getPath() + ".png");
    }
}
