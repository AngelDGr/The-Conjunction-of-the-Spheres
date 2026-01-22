package mors.tcots.client.geo.renderer.entity.necrophages;

import mors.tcots.client.geo.model.entity.necrophage.BullvoreModel;
import mors.tcots.entity.monsters.necrophages.BullvoreEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class BullvoreRenderer extends GeoEntityRenderer<BullvoreEntity> {
    public BullvoreRenderer(final EntityRendererProvider.Context renderManager) {
        super(renderManager, new BullvoreModel());

        this.shadowRadius = 0.9f;
    }

    @Override
    public float getMotionAnimThreshold(final BullvoreEntity animatable) {
        return 0.001f;
    }
}
