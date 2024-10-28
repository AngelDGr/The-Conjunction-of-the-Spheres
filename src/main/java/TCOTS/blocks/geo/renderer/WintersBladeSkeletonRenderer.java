package TCOTS.blocks.geo.renderer;

import TCOTS.blocks.entity.WintersBladeSkeletonBlockEntity;
import TCOTS.blocks.geo.model.WintersBladeSkeletonModel;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

@SuppressWarnings("unused")
public class WintersBladeSkeletonRenderer extends GeoBlockRenderer<WintersBladeSkeletonBlockEntity> {
    public WintersBladeSkeletonRenderer(BlockEntityRendererFactory.Context ctx) {
        super(new WintersBladeSkeletonModel());
    }
}
