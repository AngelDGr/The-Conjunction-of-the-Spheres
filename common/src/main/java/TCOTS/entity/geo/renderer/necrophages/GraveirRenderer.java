package TCOTS.entity.geo.renderer.necrophages;

import TCOTS.entity.geo.model.necrophages.GraveirModel;
import TCOTS.entity.monsters.necrophages.GraveirEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

public class GraveirRenderer extends GeoEntityRenderer<GraveirEntity> {
    public GraveirRenderer(final EntityRendererProvider.Context renderManager) {
        super(renderManager, new GraveirModel());

        this.shadowRadius = 0.7f;

        addRenderLayer(new AutoGlowingGeoLayer<>(this));
    }

    @Override
    public float getMotionAnimThreshold(final GraveirEntity animatable) {
        return 0.001f;
    }
}
