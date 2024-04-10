package TCOTS.entity.geo.renderer.necrophages;

import TCOTS.entity.geo.model.necrophages.GhoulModel;
import TCOTS.entity.necrophages.GhoulEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class GhoulRenderer extends GeoEntityRenderer<GhoulEntity> {
    public GhoulRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new GhoulModel());
    }
}
