package TCOTS.entity.geo.renderer.necrophages;

import TCOTS.entity.geo.model.necrophages.RotfiendModel;
import TCOTS.entity.necrophages.RotfiendEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class RotfiendRenderer extends GeoEntityRenderer<RotfiendEntity> {
    public RotfiendRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new RotfiendModel());

        this.shadowRadius = 0.5f;
    }

}
