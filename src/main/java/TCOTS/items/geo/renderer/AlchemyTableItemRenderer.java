package TCOTS.items.geo.renderer;

import TCOTS.items.blocks.AlchemyTableItem;
import TCOTS.items.geo.model.AlchemyTableItemModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class AlchemyTableItemRenderer extends GeoItemRenderer<AlchemyTableItem> {
    public AlchemyTableItemRenderer() {
        super(new AlchemyTableItemModel());
    }
}
