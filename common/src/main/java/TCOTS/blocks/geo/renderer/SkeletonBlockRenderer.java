package TCOTS.blocks.geo.renderer;

import TCOTS.blocks.entity.SkeletonBlockEntity;
import TCOTS.blocks.geo.model.SkeletonBlockEntityModel;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

@SuppressWarnings("unused")
public class SkeletonBlockRenderer extends GeoBlockRenderer<SkeletonBlockEntity> {
    public SkeletonBlockRenderer(BlockEntityRendererProvider.Context ctx) {
        super(new SkeletonBlockEntityModel());
    }
}
