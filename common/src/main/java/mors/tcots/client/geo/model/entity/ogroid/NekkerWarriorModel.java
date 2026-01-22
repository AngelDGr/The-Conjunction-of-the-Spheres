package mors.tcots.client.geo.model.entity.ogroid;

import mors.tcots.TCOTS_Main;
import mors.tcots.client.geo.model.defaulted.monsters.DefaultedOgroidGeoModel;
import mors.tcots.entity.monsters.ogroids.NekkerWarriorEntity;
import net.minecraft.resources.ResourceLocation;

public class NekkerWarriorModel extends DefaultedOgroidGeoModel<NekkerWarriorEntity> {

    public NekkerWarriorModel() {
        super(ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "nekker_warrior"), true);
    }
}
