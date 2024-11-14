package TCOTS.entity.geo.model.necrophages;

import TCOTS.TCOTS_Main;
import TCOTS.entity.geo.model.BipedGeoModelBase;
import TCOTS.entity.necrophages.RotfiendEntity;
import net.minecraft.util.Identifier;

public class RotfiendModel extends BipedGeoModelBase<RotfiendEntity> {

    @Override
    public Identifier getModelResource(RotfiendEntity animatable) {
        return Identifier.of(TCOTS_Main.MOD_ID, "geo/necrophages/rotfiend.geo.json");
    }

    @Override
    public Identifier getTextureResource(RotfiendEntity animatable) {
        return Identifier.of(TCOTS_Main.MOD_ID, "textures/entity/necrophages/rotfiend.png");
    }

    @Override
    public Identifier getAnimationResource(RotfiendEntity animatable) {
        return Identifier.of(TCOTS_Main.MOD_ID, "animations/necrophages/rotfiend.animation.json");
    }

}
