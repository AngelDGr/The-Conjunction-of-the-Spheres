package TCOTS.entity.geo.model.ogroids;

import TCOTS.TCOTS_Main;
import TCOTS.entity.geo.model.BipedGeoModelBase;
import TCOTS.entity.ogroids.NekkerEntity;
import net.minecraft.util.Identifier;

public class NekkerModel extends BipedGeoModelBase<NekkerEntity> {

    @Override
    protected float getLegsSpeed() {
        return 0.5f;
    }

    @Override
    protected float getArmsSpeed() {
        return 0.5f;
    }

    @Override
    protected float getArmsAmount() {
        return 0.6f;
    }

    @Override
    protected float getLegsAmount() {
        return 0.8f;
    }

    @Override
    public Identifier getModelResource(NekkerEntity animatable) {
        return new Identifier(TCOTS_Main.MOD_ID, "geo/ogroids/nekker.geo.json");
    }

    @Override
    public Identifier getTextureResource(NekkerEntity animatable) {
        return new Identifier(TCOTS_Main.MOD_ID, "textures/entity/ogroids/nekker/nekker.png");
    }

    @Override
    public Identifier getAnimationResource(NekkerEntity animatable) {
        return new Identifier(TCOTS_Main.MOD_ID, "animations/ogroids/nekker.animation.json");
    }
}
