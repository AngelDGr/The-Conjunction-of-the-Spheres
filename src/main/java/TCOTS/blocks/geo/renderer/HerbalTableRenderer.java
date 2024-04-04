package TCOTS.blocks.geo.renderer;

import TCOTS.blocks.entity.HerbalTableBlockEntity;
import TCOTS.blocks.geo.model.HerbalTableModel;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

@SuppressWarnings("unused")
public class HerbalTableRenderer extends GeoBlockRenderer<HerbalTableBlockEntity> {
    public HerbalTableRenderer(BlockEntityRendererFactory.Context ctx) {
        super(new HerbalTableModel());
    }
}
