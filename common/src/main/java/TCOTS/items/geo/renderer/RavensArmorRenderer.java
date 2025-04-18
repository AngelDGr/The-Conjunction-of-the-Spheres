package TCOTS.items.geo.renderer;

import TCOTS.items.armor.RavensArmorItem;
import TCOTS.items.geo.model.RavensArmorModel;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public class RavensArmorRenderer extends GeoArmorRenderer<RavensArmorItem> {
    public RavensArmorRenderer() {
        super(new RavensArmorModel());
    }
}
