package TCOTS.blocks.geo.renderer;

import TCOTS.blocks.entity.NestSkullBlockEntity;
import TCOTS.blocks.geo.model.NestSkullBlockModel;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class NestSkullBlockRenderer extends GeoBlockRenderer<NestSkullBlockEntity> {

    public NestSkullBlockRenderer(BlockEntityRendererFactory.Context ctx) {
        super(new NestSkullBlockModel());
    }

}
