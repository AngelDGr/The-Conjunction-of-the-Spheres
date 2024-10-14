package TCOTS.entity.geo.renderer.ogroids;

import TCOTS.entity.geo.model.ogroids.IceGiantModel;
import TCOTS.entity.ogroids.IceGiantEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class IceGiantRenderer extends GeoEntityRenderer<IceGiantEntity> {
    public IceGiantRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new IceGiantModel());
        this.shadowRadius = 1.2f;
    }
}
