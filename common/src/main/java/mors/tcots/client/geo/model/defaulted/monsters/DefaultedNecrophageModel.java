package mors.tcots.client.geo.model.defaulted.monsters;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public abstract class DefaultedNecrophageModel<T extends GeoAnimatable> extends DefaultedEntityGeoModel<T> {
    public DefaultedNecrophageModel(final ResourceLocation assetSubpath, final boolean turnsHead) {
        super(assetSubpath, turnsHead);
    }

    @Override
    protected String subtype() {
        return "entity/necrophage";
    }

    @Override
    public ResourceLocation buildFormattedTexturePath(final ResourceLocation basePath) {
        return basePath.withPath("textures/entity/monster/necrophage/" + basePath.getPath() + ".png");
    }
}
