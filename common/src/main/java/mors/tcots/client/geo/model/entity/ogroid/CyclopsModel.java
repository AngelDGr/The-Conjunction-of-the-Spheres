package mors.tcots.client.geo.model.entity.ogroid;

import mors.tcots.TCOTS_Main;
import mors.tcots.client.geo.model.defaulted.monsters.DefaultedOgroidGeoModel;
import mors.tcots.entity.monsters.ogroids.CyclopsEntity;
import net.minecraft.resources.ResourceLocation;

public class CyclopsModel extends DefaultedOgroidGeoModel<CyclopsEntity> {
    public CyclopsModel() {
        super(ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "cyclops"), true);
    }
}
