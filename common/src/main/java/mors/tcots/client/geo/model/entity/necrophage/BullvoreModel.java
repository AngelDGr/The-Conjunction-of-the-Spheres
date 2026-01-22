package mors.tcots.client.geo.model.entity.necrophage;

import mors.tcots.TCOTS_Main;
import mors.tcots.client.geo.model.defaulted.monsters.DefaultedNecrophageModel;
import mors.tcots.entity.monsters.necrophages.BullvoreEntity;
import net.minecraft.resources.ResourceLocation;

public class BullvoreModel extends DefaultedNecrophageModel<BullvoreEntity> {

    public BullvoreModel() {
        super(ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "bullvore"), true);
    }
}
