package mors.tcots.client.geo.renderer.entity.necrophages;

import mors.tcots.client.geo.model.entity.necrophage.RotfiendModel;
import mors.tcots.entity.monsters.necrophages.RotfiendEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class RotfiendRenderer extends GeoEntityRenderer<RotfiendEntity> {
    public RotfiendRenderer(final EntityRendererProvider.Context renderManager) {
        super(renderManager, new RotfiendModel());

        this.shadowRadius = 0.5f;
    }
}
