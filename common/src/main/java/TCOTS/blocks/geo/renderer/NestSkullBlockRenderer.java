package TCOTS.blocks.geo.renderer;

import TCOTS.blocks.entity.NestSkullBlockEntity;
import TCOTS.blocks.geo.model.NestSkullBlockModel;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

@SuppressWarnings("unused")
public class NestSkullBlockRenderer extends GeoBlockRenderer<NestSkullBlockEntity> {

    public NestSkullBlockRenderer(final BlockEntityRendererProvider.Context ctx) {
        super(new NestSkullBlockModel());
    }

}
