package TCOTS.entity.geo.renderer.ogroids;

import TCOTS.entity.geo.model.ogroids.NekkerModel;
import TCOTS.entity.ogroids.NekkerEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class NekkerRenderer extends GeoEntityRenderer<NekkerEntity> {
    public NekkerRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new NekkerModel());

        this.shadowRadius = 0.35f;
    }

}
