package mors.tcots.client.geo.renderer.block;

import mors.tcots.block.GiantAnchorBlock;
import mors.tcots.block.entity.GiantAnchorBlockEntity;
import mors.tcots.client.geo.model.block.GiantAnchorModel;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import software.bernie.geckolib.renderer.GeoBlockRenderer;
import software.bernie.geckolib.renderer.GeoItemRenderer;

@SuppressWarnings("unused")
public class GiantAnchorRenderer extends GeoBlockRenderer<GiantAnchorBlockEntity> {
    public GiantAnchorRenderer(final BlockEntityRendererProvider.Context ctx) {
        super(new GiantAnchorModel());
    }

    public static class Item extends GeoItemRenderer<GiantAnchorBlock.Item> {
        public Item() {
            super(new GiantAnchorModel.Item());
        }
    }
}
