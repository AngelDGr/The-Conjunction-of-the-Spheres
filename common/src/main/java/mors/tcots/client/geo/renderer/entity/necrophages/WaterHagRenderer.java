package mors.tcots.client.geo.renderer.entity.necrophages;

import mors.tcots.client.geo.model.entity.necrophage.WaterHagModel;
import mors.tcots.entity.monsters.necrophages.WaterHagEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class WaterHagRenderer extends GeoEntityRenderer<WaterHagEntity> {
    public WaterHagRenderer(final EntityRendererProvider.Context renderManager) {
        super(renderManager, new WaterHagModel());

        this.shadowRadius = 0.5f;
    }

    @Override
    public float getMotionAnimThreshold(final WaterHagEntity animatable) {
        return 0.005f;
    }
}
