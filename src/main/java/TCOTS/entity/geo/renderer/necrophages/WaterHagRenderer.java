package TCOTS.entity.geo.renderer.necrophages;

import TCOTS.entity.geo.model.necrophages.WaterHagModel;
import TCOTS.entity.necrophages.WaterHagEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class WaterHagRenderer extends GeoEntityRenderer<WaterHagEntity> {
    public WaterHagRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new WaterHagModel());

        this.shadowRadius = 0.5f;
    }

    @Override
    public float getMotionAnimThreshold(WaterHagEntity animatable) {
        return 0.005f;
    }
}
