package TCOTS.items.geo.renderer;

import TCOTS.items.blocks.HerbalTableItem;
import TCOTS.items.geo.model.HerbalTableItemModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class HerbalTableItemRenderer extends GeoItemRenderer<HerbalTableItem> {
    public HerbalTableItemRenderer() {
        super(new HerbalTableItemModel());
    }
}
