package TCOTS.entity.geo.model.ogroids;

import TCOTS.TCOTS_Main;
import TCOTS.entity.geo.model.TrollGeoModelBase;
import TCOTS.entity.ogroids.IceTrollEntity;
import net.minecraft.util.Identifier;

public class IceTrollModel extends TrollGeoModelBase<IceTrollEntity> {
    @Override
    public Identifier getModelResource(IceTrollEntity animatable) {
        return Identifier.of(TCOTS_Main.MOD_ID, "geo/ogroids/ice_troll.geo.json");
    }

    @Override
    public Identifier getTextureResource(IceTrollEntity animatable) {
        return Identifier.of(TCOTS_Main.MOD_ID, "textures/entity/ogroids/troll/ice_troll.png");
    }

    @Override
    public Identifier getAnimationResource(IceTrollEntity animatable) {
        return Identifier.of(TCOTS_Main.MOD_ID, "animations/ogroids/ice_troll.animation.json");
    }
}
