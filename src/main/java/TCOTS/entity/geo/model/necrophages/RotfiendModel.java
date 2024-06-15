package TCOTS.entity.geo.model.necrophages;

import TCOTS.TCOTS_Main;
import TCOTS.entity.geo.model.BipedGeoModelBase;
import TCOTS.entity.necrophages.RotfiendEntity;
import net.minecraft.util.Identifier;

public class RotfiendModel extends BipedGeoModelBase<RotfiendEntity> {

    @Override
    protected float getLegsSpeed() {
        return 0.5f;
    }

    @Override
    protected float getArmsSpeed() {
        return 0.5f;
    }

    @Override
    protected float getLegsAmount() {
        return 0.8f;
    }

    @Override
    public Identifier getModelResource(RotfiendEntity animatable) {
        return new Identifier(TCOTS_Main.MOD_ID, "geo/necrophages/rotfiend.geo.json");
    }

    @Override
    public Identifier getTextureResource(RotfiendEntity animatable) {
        return new Identifier(TCOTS_Main.MOD_ID, "textures/entity/necrophages/rotfiend/rotfiend.png");
    }

    @Override
    public Identifier getAnimationResource(RotfiendEntity animatable) {
        return new Identifier(TCOTS_Main.MOD_ID, "animations/necrophages/rotfiend.animation.json");
    }

}
