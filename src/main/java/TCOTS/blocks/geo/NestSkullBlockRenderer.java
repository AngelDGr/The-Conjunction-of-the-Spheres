package TCOTS.blocks.geo;

import TCOTS.blocks.skull.NestSkullBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class NestSkullBlockRenderer extends GeoBlockRenderer<NestSkullBlockEntity> {
    public NestSkullBlockRenderer(BlockEntityRendererFactory.Context ctx) {
        super(new NestSkullBlockModel());
    }
}
