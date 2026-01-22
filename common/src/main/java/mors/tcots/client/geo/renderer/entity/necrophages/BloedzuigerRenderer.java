package mors.tcots.client.geo.renderer.entity.necrophages;

import mors.tcots.client.geo.model.entity.necrophage.BloedzuigerModel;
import mors.tcots.entity.monsters.necrophages.BloedzuigerEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class BloedzuigerRenderer extends GeoEntityRenderer<BloedzuigerEntity> {
    public BloedzuigerRenderer(final EntityRendererProvider.Context renderManager) {
        super(renderManager, new BloedzuigerModel());
        this.shadowRadius = 0.55f;
    }
}
