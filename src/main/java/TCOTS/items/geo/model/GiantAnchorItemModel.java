package TCOTS.items.geo.model;

import TCOTS.TCOTS_Main;
import TCOTS.items.weapons.GiantAnchorItem;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;

public class GiantAnchorItemModel extends GeoModel<GiantAnchorItem> {
    @Override
    public Identifier getModelResource(GiantAnchorItem animatable) {
        return Identifier.of(TCOTS_Main.MOD_ID, "geo/anchor.geo.json");
    }

    @Override
    public Identifier getTextureResource(GiantAnchorItem animatable) {
        return Identifier.of(TCOTS_Main.MOD_ID, "textures/entity/anchor.png");
    }

    @Override
    public Identifier getAnimationResource(GiantAnchorItem animatable) {
        return Identifier.of(TCOTS_Main.MOD_ID, "animations/misc/dummy.animation.json");
    }
}
