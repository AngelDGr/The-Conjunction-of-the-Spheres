package mors.tcots.client.geo.renderer.item.armor;

import mors.tcots.items.armor.ManticoreArmorItem;
import mors.tcots.client.geo.model.item.armor.ManticoreArmorModel;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class ManticoreArmorRenderer extends GeoArmorRenderer<ManticoreArmorItem> {
    public ManticoreArmorRenderer() {
        super(new ManticoreArmorModel());
    }
}
