package TCOTS.entity.geo.model.ogroids;

import TCOTS.TCOTS_Main;
import TCOTS.entity.geo.model.TrollGeoModelBase;
import TCOTS.entity.monsters.ogroids.RockTrollEntity;
import net.minecraft.resources.ResourceLocation;

public class RockTrollModel extends TrollGeoModelBase<RockTrollEntity> {
    @Override
    public ResourceLocation getModelResource(final RockTrollEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "geo/ogroids/rock_troll.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(final RockTrollEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "textures/entity/ogroids/troll/rock_troll.png");
    }

    @Override
    public ResourceLocation getAnimationResource(final RockTrollEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "animations/ogroids/rock_troll.animation.json");
    }
}
