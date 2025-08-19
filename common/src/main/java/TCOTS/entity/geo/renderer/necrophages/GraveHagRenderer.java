package TCOTS.entity.geo.renderer.necrophages;

import TCOTS.entity.geo.model.necrophages.GraveHagModel;
import TCOTS.entity.monsters.necrophages.GraveHagEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class GraveHagRenderer extends GeoEntityRenderer<GraveHagEntity> {
    public GraveHagRenderer(final EntityRendererProvider.Context renderManager) {
        super(renderManager, new GraveHagModel());

        this.shadowRadius = 0.5f;
    }

    @Override
    public float getMotionAnimThreshold(final GraveHagEntity animatable) {
        return 0.005f;
    }
}
