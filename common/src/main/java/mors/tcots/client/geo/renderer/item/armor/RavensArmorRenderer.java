package mors.tcots.client.geo.renderer.item.armor;

import mors.tcots.items.armor.RavensArmorItem;
import mors.tcots.client.geo.model.item.armor.RavensArmorModel;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class RavensArmorRenderer extends GeoArmorRenderer<RavensArmorItem> {
    public RavensArmorRenderer() {
        super(new RavensArmorModel());
    }
}
