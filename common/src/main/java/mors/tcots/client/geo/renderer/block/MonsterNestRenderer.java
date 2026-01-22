package mors.tcots.client.geo.renderer.block;

import mors.tcots.block.MonsterNestBlock;
import mors.tcots.block.entity.MonsterNestBlockEntity;
import mors.tcots.client.geo.model.block.MonsterNestModel;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import software.bernie.geckolib.renderer.GeoBlockRenderer;
import software.bernie.geckolib.renderer.GeoItemRenderer;

@SuppressWarnings("unused")
public class MonsterNestRenderer extends GeoBlockRenderer<MonsterNestBlockEntity> {
    public MonsterNestRenderer(final BlockEntityRendererProvider.Context ctx) {
        super(new MonsterNestModel());
    }

    public static class Item extends GeoItemRenderer<MonsterNestBlock.Item> {
        public Item() {
            super(new MonsterNestModel.Item());
        }
    }
}
