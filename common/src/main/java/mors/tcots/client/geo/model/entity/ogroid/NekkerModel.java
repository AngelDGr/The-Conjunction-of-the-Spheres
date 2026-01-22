package mors.tcots.client.geo.model.entity.ogroid;

import mors.tcots.TCOTS_Main;
import mors.tcots.client.geo.model.defaulted.monsters.DefaultedOgroidGeoModel;
import mors.tcots.entity.monsters.ogroids.NekkerEntity;
import net.minecraft.resources.ResourceLocation;

public class NekkerModel extends DefaultedOgroidGeoModel<NekkerEntity> {

    public NekkerModel() {
        super(ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "nekker"), true);
    }
}
