package mors.tcots.client.geo.renderer.entity.ogroids;

import mors.tcots.client.geo.model.entity.ogroid.CyclopsModel;
import mors.tcots.entity.monsters.ogroids.CyclopsEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class CyclopsRenderer extends GeoEntityRenderer<CyclopsEntity> {
    public CyclopsRenderer(final EntityRendererProvider.Context renderManager) {
        super(renderManager, new CyclopsModel());
        this.shadowRadius = 1.2f;
    }
}
