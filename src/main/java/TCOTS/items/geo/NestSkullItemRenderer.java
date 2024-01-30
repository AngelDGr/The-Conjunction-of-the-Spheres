package TCOTS.items.geo;

import TCOTS.items.blocks.NestSkullItem;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class NestSkullItemRenderer extends GeoItemRenderer<NestSkullItem> {
    public NestSkullItemRenderer() {
        super(new NestSkullItemModel());
    }
}
