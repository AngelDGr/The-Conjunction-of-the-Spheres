package mors.tcots.client.geo.renderer.entity.ogroids;

import mors.tcots.client.geo.model.entity.ogroid.NekkerModel;
import mors.tcots.entity.monsters.ogroids.NekkerEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class NekkerRenderer extends GeoEntityRenderer<NekkerEntity> {
    public NekkerRenderer(final EntityRendererProvider.Context renderManager) {
        super(renderManager, new NekkerModel());

        this.shadowRadius = 0.35f;
    }

}
