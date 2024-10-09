package TCOTS.items.geo.renderer;

import TCOTS.items.armor.WarriorsLeatherArmorItem;
import TCOTS.items.geo.model.WarriorsLeatherArmorModel;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class WarriorsLeatherArmorRenderer extends GeoArmorRenderer<WarriorsLeatherArmorItem> {
    public WarriorsLeatherArmorRenderer() {
        super(new WarriorsLeatherArmorModel());
    }
}
