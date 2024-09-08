package TCOTS.entity.geo.renderer.necrophages;

import TCOTS.entity.geo.model.necrophages.BullvoreModel;
import TCOTS.entity.necrophages.BullvoreEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class BullvoreRenderer extends GeoEntityRenderer<BullvoreEntity> {
    public BullvoreRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new BullvoreModel());

        this.shadowRadius = 0.9f;
    }

    @Override
    public float getMotionAnimThreshold(BullvoreEntity animatable) {
        return 0.001f;
    }
}
