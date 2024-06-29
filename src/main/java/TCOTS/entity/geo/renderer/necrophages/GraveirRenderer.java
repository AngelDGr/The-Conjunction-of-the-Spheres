package TCOTS.entity.geo.renderer.necrophages;

import TCOTS.entity.geo.model.necrophages.GraveirModel;
import TCOTS.entity.necrophages.GraveirEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

public class GraveirRenderer extends GeoEntityRenderer<GraveirEntity> {
    public GraveirRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new GraveirModel());

        addRenderLayer(new AutoGlowingGeoLayer<>(this));
    }

    @Override
    public float getMotionAnimThreshold(GraveirEntity animatable) {
        return 0.001f;
    }
}
