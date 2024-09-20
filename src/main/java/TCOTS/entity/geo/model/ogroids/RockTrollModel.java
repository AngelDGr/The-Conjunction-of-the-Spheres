package TCOTS.entity.geo.model.ogroids;

import TCOTS.TCOTS_Main;
import TCOTS.entity.geo.model.TrollGeoModelBase;
import TCOTS.entity.ogroids.RockTrollEntity;
import net.minecraft.util.Identifier;

public class RockTrollModel extends TrollGeoModelBase<RockTrollEntity> {
    @Override
    public Identifier getModelResource(RockTrollEntity animatable) {
        return new Identifier(TCOTS_Main.MOD_ID, "geo/ogroids/rock_troll.geo.json");
    }

    @Override
    public Identifier getTextureResource(RockTrollEntity animatable) {
        return new Identifier(TCOTS_Main.MOD_ID, "textures/entity/ogroids/troll/rock_troll.png");
    }

    @Override
    public Identifier getAnimationResource(RockTrollEntity animatable) {
        return new Identifier(TCOTS_Main.MOD_ID, "animations/ogroids/rock_troll.animation.json");
    }
}
