package TCOTS.entity.geo.renderer.ogroids;

import TCOTS.entity.geo.model.ogroids.CyclopsModel;
import TCOTS.entity.ogroids.CyclopsEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class CyclopsRenderer extends GeoEntityRenderer<CyclopsEntity> {
    public CyclopsRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new CyclopsModel());
        this.shadowRadius = 1.2f;
    }
}
