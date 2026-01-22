package mors.tcots.client.geo.model.entity.ogroid;

import mors.tcots.TCOTS_Main;
import mors.tcots.client.geo.model.defaulted.monsters.DefaultedOgroidGeoModel;
import mors.tcots.entity.monsters.ogroids.IceGiantEntity;
import net.minecraft.resources.ResourceLocation;

public class IceGiantModel extends DefaultedOgroidGeoModel<IceGiantEntity> {
    public IceGiantModel() {
        super(ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "ice_giant"), true);
    }
}
