package TCOTS.blocks.geo.renderer;

import TCOTS.blocks.entity.AlchemyTableBlockEntity;
import TCOTS.blocks.geo.model.AlchemyTableModel;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class AlchemyTableRenderer extends GeoBlockRenderer<AlchemyTableBlockEntity> {
    public AlchemyTableRenderer(BlockEntityRendererFactory.Context ctx) {
        super(new AlchemyTableModel());
    }
}
