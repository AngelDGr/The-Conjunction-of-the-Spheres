package TCOTS.blocks.geo.renderer;

import TCOTS.blocks.entity.MonsterNestBlockEntity;
import TCOTS.blocks.entity.NestSkullBlockEntity;
import TCOTS.blocks.geo.model.MonsterNestModel;
import TCOTS.blocks.geo.model.NestSkullBlockModel;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class MonsterNestRenderer extends GeoBlockRenderer<MonsterNestBlockEntity> {
    public MonsterNestRenderer(BlockEntityRendererProvider.Context ctx) {
        super(new MonsterNestModel());
    }
}
