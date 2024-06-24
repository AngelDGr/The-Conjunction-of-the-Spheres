package TCOTS.entity.geo.model.necrophages;

import TCOTS.TCOTS_Main;
import TCOTS.entity.geo.model.BipedGeoModelBase;
import TCOTS.entity.necrophages.ScurverEntity;
import net.minecraft.util.Identifier;

public class ScurverModel extends BipedGeoModelBase<ScurverEntity> {

    @Override
    public Identifier getModelResource(ScurverEntity animatable) {
        return new Identifier(TCOTS_Main.MOD_ID, "geo/necrophages/scurver.geo.json");
    }

    @Override
    public Identifier getTextureResource(ScurverEntity animatable) {
        return new Identifier(TCOTS_Main.MOD_ID, "textures/entity/necrophages/scurver/scurver.png");
    }

    @Override
    public Identifier getAnimationResource(ScurverEntity animatable) {
        return new Identifier(TCOTS_Main.MOD_ID, "animations/necrophages/scurver.animation.json");
    }
}
