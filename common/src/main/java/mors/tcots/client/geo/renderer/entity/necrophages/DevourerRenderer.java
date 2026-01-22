package mors.tcots.client.geo.renderer.entity.necrophages;

import mors.tcots.client.geo.model.entity.necrophage.DevourerModel;
import mors.tcots.entity.monsters.necrophages.DevourerEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class DevourerRenderer extends GeoEntityRenderer<DevourerEntity> {
    public DevourerRenderer(final EntityRendererProvider.Context renderManager) {
        super(renderManager, new DevourerModel());
        this.shadowRadius = 0.55f;
    }

    @Override
    public float getMotionAnimThreshold(final DevourerEntity animatable) {
        return 0.001f;
    }
}
