package TCOTS.entity.geo.model.necrophages;

import TCOTS.TCOTS_Main;
import TCOTS.entity.geo.model.QuadrupedGhoulModelBase;
import TCOTS.entity.necrophages.GhoulEntity;
import net.minecraft.util.Identifier;

public class GhoulModel extends QuadrupedGhoulModelBase<GhoulEntity> {
    @Override
    public Identifier getModelResource(GhoulEntity animatable) {
        return Identifier.of(TCOTS_Main.MOD_ID, "geo/necrophages/ghoul.geo.json");
    }

    @Override
    public Identifier getTextureResource(GhoulEntity animatable) {
        return Identifier.of(TCOTS_Main.MOD_ID, "textures/entity/necrophages/ghoul/ghoul.png");
    }

    @Override
    public Identifier getAnimationResource(GhoulEntity animatable) {
        return Identifier.of(TCOTS_Main.MOD_ID, "animations/necrophages/ghoul.animation.json");
    }
}
