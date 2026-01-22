package mors.tcots.client.geo.model.defaulted.monsters;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public abstract class DefaultedSpecterModel<T extends GeoAnimatable> extends DefaultedEntityGeoModel<T> {
    public DefaultedSpecterModel(final ResourceLocation assetSubpath, final boolean turnsHead) {
        super(assetSubpath, turnsHead);
    }

    @Override
    protected String subtype() {
        return "entity/specter";
    }

    @Override
    public ResourceLocation buildFormattedTexturePath(final ResourceLocation basePath) {
        return basePath.withPath("textures/entity/monster/specter/" + basePath.getPath() + ".png");
    }
}
