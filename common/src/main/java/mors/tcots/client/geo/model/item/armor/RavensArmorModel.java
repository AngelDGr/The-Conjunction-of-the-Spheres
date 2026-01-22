package mors.tcots.client.geo.model.item.armor;

import mors.tcots.TCOTS_Main;
import mors.tcots.client.geo.model.defaulted.DefaultedArmorModel;
import mors.tcots.items.armor.RavensArmorItem;
import net.minecraft.resources.ResourceLocation;

public class RavensArmorModel extends DefaultedArmorModel<RavensArmorItem> {
    public RavensArmorModel() {
        super(ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "raven"));
    }
}
