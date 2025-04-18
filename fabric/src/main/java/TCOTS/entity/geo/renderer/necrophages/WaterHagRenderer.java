package TCOTS.entity.geo.renderer.necrophages;

import TCOTS.entity.geo.model.necrophages.WaterHagModel;
import TCOTS.entity.necrophages.WaterHagEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class WaterHagRenderer extends GeoEntityRenderer<WaterHagEntity> {
    public WaterHagRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new WaterHagModel());

        this.shadowRadius = 0.5f;
    }

    @Override
    public float getMotionAnimThreshold(WaterHagEntity animatable) {
        return 0.005f;
    }
}
