package TCOTS.items.geo.renderer;

import TCOTS.items.blocks.MonsterNestItem;
import TCOTS.items.geo.model.MonsterNestItemModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class MonsterNestItemRenderer extends GeoItemRenderer<MonsterNestItem> {
    public MonsterNestItemRenderer() {
        super(new MonsterNestItemModel());
    }
}
