package mors.tcots.client.geo.model.entity.necrophage;

import mors.tcots.TCOTS_Main;
import mors.tcots.client.geo.model.defaulted.monsters.DefaultedNecrophageModel;
import mors.tcots.entity.monsters.necrophages.BloedzuigerEntity;
import net.minecraft.resources.ResourceLocation;

public class BloedzuigerModel extends DefaultedNecrophageModel<BloedzuigerEntity> {

    public BloedzuigerModel() {
        super(ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "bloedzuiger"), true);
    }
}
