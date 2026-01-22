package mors.tcots.client.geo.renderer.block;

import mors.tcots.block.HerbalTableBlock;
import mors.tcots.block.entity.HerbalTableBlockEntity;
import mors.tcots.client.geo.model.block.HerbalTableModel;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import software.bernie.geckolib.renderer.GeoBlockRenderer;
import software.bernie.geckolib.renderer.GeoItemRenderer;

@SuppressWarnings("unused")
public class HerbalTableRenderer extends GeoBlockRenderer<HerbalTableBlockEntity> {
    public HerbalTableRenderer(final BlockEntityRendererProvider.Context ctx) {
        super(new HerbalTableModel());
    }

    public static class Item extends GeoItemRenderer<HerbalTableBlock.Item> {
        public Item() {
            super(new HerbalTableModel.Item());
        }
    }
}
