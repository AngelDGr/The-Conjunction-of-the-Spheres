package TCOTS.items.geo.model;

import TCOTS.TCOTS_Main;
import TCOTS.items.blocks.GiantAnchorBlockItem;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;

public class GiantAnchorBlockItemModel extends GeoModel<GiantAnchorBlockItem> {
    @Override
    public Identifier getModelResource(GiantAnchorBlockItem animatable) {
        return new Identifier(TCOTS_Main.MOD_ID, "geo/misc/giant_anchor.geo.json");
    }

    @Override
    public Identifier getTextureResource(GiantAnchorBlockItem animatable) {
        return new Identifier(TCOTS_Main.MOD_ID, "textures/block/giant_anchor.png");
    }

    @Override
    public Identifier getAnimationResource(GiantAnchorBlockItem animatable) {
        return new Identifier(TCOTS_Main.MOD_ID, "animations/misc/dummy.animation.json");
    }
}
