package TCOTS.entity.geo.model.necrophages;

import TCOTS.TCOTS_Main;
import TCOTS.entity.geo.model.BipedGeoModelBase;
import TCOTS.entity.necrophages.DevourerEntity;
import net.minecraft.util.Identifier;

public class DevourerModel extends BipedGeoModelBase<DevourerEntity> {
    @Override
    public Identifier getModelResource(DevourerEntity animatable) {
        return new Identifier(TCOTS_Main.MOD_ID, "geo/necrophages/devourer.geo.json");
    }

    @Override
    public Identifier getTextureResource(DevourerEntity animatable) {
        return new Identifier(TCOTS_Main.MOD_ID, "textures/entity/necrophages/devourer.png");
    }

    @Override
    public Identifier getAnimationResource(DevourerEntity animatable) {
        return new Identifier(TCOTS_Main.MOD_ID, "animations/necrophages/devourer.animation.json");
    }

    @Override
    public void applyMolangQueries(DevourerEntity animatable, double animTime) {
        super.applyMolangQueries(animatable, animTime);
    }
}
