package TCOTS.entity.geo.renderer.necrophages;

import TCOTS.entity.geo.model.necrophages.GraveHagModel;
import TCOTS.entity.necrophages.GraveHagEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class GraveHagRenderer extends GeoEntityRenderer<GraveHagEntity> {
    public GraveHagRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new GraveHagModel());
    }

    @Override
    public float getMotionAnimThreshold(GraveHagEntity animatable) {
        return 0.005f;
    }
}
