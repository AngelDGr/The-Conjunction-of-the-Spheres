package mors.tcots.client.geo.renderer.item.armor;

import mors.tcots.items.armor.WarriorsLeatherArmorItem;
import mors.tcots.client.geo.model.item.armor.WarriorsLeatherArmorModel;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class WarriorsLeatherArmorRenderer extends GeoArmorRenderer<WarriorsLeatherArmorItem> {
    public WarriorsLeatherArmorRenderer() {
        super(new WarriorsLeatherArmorModel());
    }
}
