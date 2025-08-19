package TCOTS.blocks.geo.renderer;

import TCOTS.blocks.entity.HerbalTableBlockEntity;
import TCOTS.blocks.geo.model.HerbalTableModel;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

@SuppressWarnings("unused")
public class HerbalTableRenderer extends GeoBlockRenderer<HerbalTableBlockEntity> {
    public HerbalTableRenderer(final BlockEntityRendererProvider.Context ctx) {
        super(new HerbalTableModel());
    }
}
