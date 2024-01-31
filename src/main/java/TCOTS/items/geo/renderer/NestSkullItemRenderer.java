package TCOTS.items.geo.renderer;

import TCOTS.items.blocks.NestSkullItem;
import TCOTS.items.geo.model.NestSkullItemModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class NestSkullItemRenderer extends GeoItemRenderer<NestSkullItem> {
    public NestSkullItemRenderer() {
        super(new NestSkullItemModel());
    }
}
