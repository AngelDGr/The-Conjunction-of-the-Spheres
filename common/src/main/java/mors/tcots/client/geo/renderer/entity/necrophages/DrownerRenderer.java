package mors.tcots.client.geo.renderer.entity.necrophages;

import mors.tcots.client.geo.model.entity.necrophage.DrownerModel;
import mors.tcots.entity.monsters.necrophages.DrownerEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class DrownerRenderer extends GeoEntityRenderer<DrownerEntity> {
    public DrownerRenderer(final EntityRendererProvider.Context renderManager) {
        super(renderManager, new DrownerModel());

        this.shadowRadius = 0.5f;
    }

}
