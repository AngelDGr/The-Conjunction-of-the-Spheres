package TCOTS.entity.geo.renderer.ogroids;

import TCOTS.entity.geo.model.ogroids.CyclopsModel;
import TCOTS.entity.ogroids.CyclopsEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class CyclopsRenderer extends GeoEntityRenderer<CyclopsEntity> {
    public CyclopsRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new CyclopsModel());
        this.shadowRadius = 1.2f;
    }
}
