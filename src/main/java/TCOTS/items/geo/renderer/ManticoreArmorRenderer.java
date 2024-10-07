package TCOTS.items.geo.renderer;

import TCOTS.items.armor.ManticoreArmorItem;
import TCOTS.items.geo.model.ManticoreArmorModel;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class ManticoreArmorRenderer extends GeoArmorRenderer<ManticoreArmorItem> {
    public ManticoreArmorRenderer() {
        super(new ManticoreArmorModel());
    }
}
