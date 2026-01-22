package mors.tcots.client.geo.model.entity.necrophage;

import mors.tcots.TCOTS_Main;
import mors.tcots.client.geo.model.defaulted.monsters.DefaultedNecrophageModel;
import mors.tcots.entity.monsters.necrophages.DevourerEntity;
import net.minecraft.resources.ResourceLocation;

public class DevourerModel extends DefaultedNecrophageModel<DevourerEntity> {

    public DevourerModel() {
        super(ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "devourer"), true);
    }
}
