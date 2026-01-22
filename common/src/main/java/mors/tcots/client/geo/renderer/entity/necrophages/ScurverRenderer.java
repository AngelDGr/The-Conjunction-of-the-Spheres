package mors.tcots.client.geo.renderer.entity.necrophages;

import mors.tcots.client.geo.model.entity.necrophage.ScurverModel;
import mors.tcots.entity.monsters.necrophages.ScurverEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class ScurverRenderer extends GeoEntityRenderer<ScurverEntity> {
    public ScurverRenderer(final EntityRendererProvider.Context renderManager) {
        super(renderManager, new ScurverModel());

        this.shadowRadius = 0.5f;
    }

}
