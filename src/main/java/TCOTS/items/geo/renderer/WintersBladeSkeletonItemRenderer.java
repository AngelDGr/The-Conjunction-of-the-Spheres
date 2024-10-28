package TCOTS.items.geo.renderer;

import TCOTS.items.blocks.WintersBladeSkeletonItem;
import TCOTS.items.geo.model.WintersBladeSkeletonItemModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class WintersBladeSkeletonItemRenderer extends GeoItemRenderer<WintersBladeSkeletonItem> {
    public WintersBladeSkeletonItemRenderer() {
        super(new WintersBladeSkeletonItemModel());
    }
}
