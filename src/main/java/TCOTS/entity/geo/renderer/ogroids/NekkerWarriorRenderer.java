package TCOTS.entity.geo.renderer.ogroids;

import TCOTS.entity.geo.model.ogroids.NekkerWarriorModel;
import TCOTS.entity.ogroids.NekkerWarriorEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class NekkerWarriorRenderer extends GeoEntityRenderer<NekkerWarriorEntity> {
    public NekkerWarriorRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new NekkerWarriorModel());

        this.shadowRadius = 0.45f;
    }
}
