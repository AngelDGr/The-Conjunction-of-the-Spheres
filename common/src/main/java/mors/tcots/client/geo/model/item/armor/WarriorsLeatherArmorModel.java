package mors.tcots.client.geo.model.item.armor;

import mors.tcots.TCOTS_Main;
import mors.tcots.client.geo.model.defaulted.DefaultedArmorModel;
import mors.tcots.items.armor.WarriorsLeatherArmorItem;
import net.minecraft.resources.ResourceLocation;

public class WarriorsLeatherArmorModel extends DefaultedArmorModel<WarriorsLeatherArmorItem> {
    public WarriorsLeatherArmorModel() {
        super(ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "warriors_leather"));
    }
}
