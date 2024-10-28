package TCOTS.blocks.geo.model;

import TCOTS.TCOTS_Main;
import TCOTS.blocks.entity.GiantAnchorBlockEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;

public class GiantAnchorModel extends GeoModel<GiantAnchorBlockEntity> {
    @Override
    public Identifier getModelResource(GiantAnchorBlockEntity animatable) {
        return new Identifier(TCOTS_Main.MOD_ID, "geo/block/giant_anchor.geo.json");
    }

    @Override
    public Identifier getTextureResource(GiantAnchorBlockEntity animatable) {
        return new Identifier(TCOTS_Main.MOD_ID, "textures/block/giant_anchor.png");
    }

    @Override
    public Identifier getAnimationResource(GiantAnchorBlockEntity animatable) {
        return new Identifier(TCOTS_Main.MOD_ID, "animations/misc/dummy.animation.json");
    }
}
