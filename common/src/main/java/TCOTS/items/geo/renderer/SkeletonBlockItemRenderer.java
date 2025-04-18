package TCOTS.items.geo.renderer;

import TCOTS.items.blocks.SkeletonBlockItem;
import TCOTS.items.geo.model.SkeletonBlockItemModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class SkeletonBlockItemRenderer extends GeoItemRenderer<SkeletonBlockItem> {
    public SkeletonBlockItemRenderer() {
        super(new SkeletonBlockItemModel());
    }
}
