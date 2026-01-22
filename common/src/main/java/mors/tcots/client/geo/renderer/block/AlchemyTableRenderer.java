package mors.tcots.client.geo.renderer.block;

import mors.tcots.block.AlchemyTableBlock;
import mors.tcots.block.entity.AlchemyTableBlockEntity;
import mors.tcots.client.geo.model.block.AlchemyTableModel;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import software.bernie.geckolib.renderer.GeoBlockRenderer;
import software.bernie.geckolib.renderer.GeoItemRenderer;

@SuppressWarnings("unused")
public class AlchemyTableRenderer extends GeoBlockRenderer<AlchemyTableBlockEntity> {
    public AlchemyTableRenderer(final BlockEntityRendererProvider.Context ctx) {
        super(new AlchemyTableModel());
    }

    public static class Item extends GeoItemRenderer<AlchemyTableBlock.Item> {
        public Item() {
            super(new AlchemyTableModel.Item());
        }
    }
}
