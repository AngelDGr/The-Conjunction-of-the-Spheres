package TCOTS.entity.geo.renderer.necrophages;

import TCOTS.entity.geo.model.necrophages.ScurverModel;
import TCOTS.entity.necrophages.ScurverEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class ScurverRenderer extends GeoEntityRenderer<ScurverEntity> {
    public ScurverRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new ScurverModel());
    }

}
