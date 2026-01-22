package mors.tcots.client.geo.renderer.entity.ogroids;

import mors.tcots.client.geo.model.entity.ogroid.NekkerWarriorModel;
import mors.tcots.entity.monsters.ogroids.NekkerWarriorEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class NekkerWarriorRenderer extends GeoEntityRenderer<NekkerWarriorEntity> {
    public NekkerWarriorRenderer(final EntityRendererProvider.Context renderManager) {
        super(renderManager, new NekkerWarriorModel());

        this.shadowRadius = 0.45f;
    }
}
