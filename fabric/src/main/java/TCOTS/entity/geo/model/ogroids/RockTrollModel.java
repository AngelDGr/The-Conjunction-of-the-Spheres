package TCOTS.entity.geo.model.ogroids;

import TCOTS.TCOTS_Main;
import TCOTS.entity.geo.model.TrollGeoModelBase;
import TCOTS.entity.ogroids.RockTrollEntity;
import net.minecraft.resources.ResourceLocation;

public class RockTrollModel extends TrollGeoModelBase<RockTrollEntity> {
    @Override
    public ResourceLocation getModelResource(RockTrollEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "geo/ogroids/rock_troll.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(RockTrollEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "textures/entity/ogroids/troll/rock_troll.png");
    }

    @Override
    public ResourceLocation getAnimationResource(RockTrollEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "animations/ogroids/rock_troll.animation.json");
    }
}
