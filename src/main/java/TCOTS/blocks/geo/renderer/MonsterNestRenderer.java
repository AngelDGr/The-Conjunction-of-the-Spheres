package TCOTS.blocks.geo.renderer;

import TCOTS.blocks.entity.MonsterNestBlockEntity;
import TCOTS.blocks.entity.NestSkullBlockEntity;
import TCOTS.blocks.geo.model.MonsterNestModel;
import TCOTS.blocks.geo.model.NestSkullBlockModel;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class MonsterNestRenderer extends GeoBlockRenderer<MonsterNestBlockEntity> {
    public MonsterNestRenderer(BlockEntityRendererFactory.Context ctx) {
        super(new MonsterNestModel());
    }
}
