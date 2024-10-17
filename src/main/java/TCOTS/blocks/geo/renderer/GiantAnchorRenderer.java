package TCOTS.blocks.geo.renderer;

import TCOTS.blocks.entity.GiantAnchorBlockEntity;
import TCOTS.blocks.geo.model.GiantAnchorModel;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

@SuppressWarnings("unused")
public class GiantAnchorRenderer extends GeoBlockRenderer<GiantAnchorBlockEntity> {
    public GiantAnchorRenderer(BlockEntityRendererFactory.Context ctx) {
        super(new GiantAnchorModel());
    }
}
