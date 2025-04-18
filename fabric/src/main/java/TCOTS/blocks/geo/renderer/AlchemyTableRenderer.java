package TCOTS.blocks.geo.renderer;

import TCOTS.blocks.entity.AlchemyTableBlockEntity;
import TCOTS.blocks.geo.model.AlchemyTableModel;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class AlchemyTableRenderer extends GeoBlockRenderer<AlchemyTableBlockEntity> {
    public AlchemyTableRenderer(BlockEntityRendererProvider.Context ctx) {
        super(new AlchemyTableModel());
    }
}
