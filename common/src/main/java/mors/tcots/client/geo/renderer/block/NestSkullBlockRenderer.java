package mors.tcots.client.geo.renderer.block;

import mors.tcots.block.entity.NestSkullBlockEntity;
import mors.tcots.block.skull.NestSkullBlock;
import mors.tcots.client.geo.model.block.NestSkullBlockModel;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import software.bernie.geckolib.renderer.GeoBlockRenderer;
import software.bernie.geckolib.renderer.GeoItemRenderer;

@SuppressWarnings("unused")
public class NestSkullBlockRenderer extends GeoBlockRenderer<NestSkullBlockEntity> {

    public NestSkullBlockRenderer(final BlockEntityRendererProvider.Context ctx) {
        super(new NestSkullBlockModel());
    }

    public static class Item extends GeoItemRenderer<NestSkullBlock.NestSkullItem> {
        public Item() {
            super(new NestSkullBlockModel.Item());
        }
    }
}
