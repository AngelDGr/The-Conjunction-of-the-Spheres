package TCOTS.entity.geo.model.ogroids;

import TCOTS.TCOTS_Main;
import TCOTS.entity.geo.model.TrollGeoModelBase;
import TCOTS.entity.monsters.ogroids.IceTrollEntity;
import net.minecraft.resources.ResourceLocation;

public class IceTrollModel extends TrollGeoModelBase<IceTrollEntity> {
    @Override
    public ResourceLocation getModelResource(final IceTrollEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "geo/ogroids/ice_troll.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(final IceTrollEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "textures/entity/ogroids/troll/ice_troll.png");
    }

    @Override
    public ResourceLocation getAnimationResource(final IceTrollEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "animations/ogroids/ice_troll.animation.json");
    }
}
