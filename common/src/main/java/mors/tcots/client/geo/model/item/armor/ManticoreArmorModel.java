package mors.tcots.client.geo.model.item.armor;

import mors.tcots.TCOTS_Main;
import mors.tcots.client.geo.model.defaulted.DefaultedArmorModel;
import mors.tcots.items.armor.ManticoreArmorItem;
import net.minecraft.resources.ResourceLocation;

public class ManticoreArmorModel extends DefaultedArmorModel<ManticoreArmorItem> {
    public ManticoreArmorModel() {
        super(ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "manticore"));
    }
}
