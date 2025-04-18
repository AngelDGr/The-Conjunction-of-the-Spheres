package TCOTS.items.geo.renderer;

import TCOTS.items.blocks.GiantAnchorBlockItem;
import TCOTS.items.geo.model.GiantAnchorBlockItemModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class GiantAnchorBlockItemRenderer extends GeoItemRenderer<GiantAnchorBlockItem> {
    public GiantAnchorBlockItemRenderer() {
        super(new GiantAnchorBlockItemModel());
    }
}
