package TCOTS.blocks.geo.renderer;

import TCOTS.blocks.entity.MonsterNestBlockEntity;
import TCOTS.blocks.geo.model.MonsterNestModel;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class MonsterNestRenderer extends GeoBlockRenderer<MonsterNestBlockEntity> {
    public MonsterNestRenderer(BlockEntityRendererProvider.Context ctx) {
        super(new MonsterNestModel());
    }
}
