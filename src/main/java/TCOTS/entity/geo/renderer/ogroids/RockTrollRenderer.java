package TCOTS.entity.geo.renderer.ogroids;

import TCOTS.entity.geo.model.ogroids.RockTrollModel;
import TCOTS.entity.ogroids.RockTrollEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class RockTrollRenderer extends GeoEntityRenderer<RockTrollEntity> {
    public RockTrollRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new RockTrollModel());
    }
}
