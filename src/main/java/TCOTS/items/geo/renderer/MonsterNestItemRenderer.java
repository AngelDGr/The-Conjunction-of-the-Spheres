package TCOTS.items.geo.renderer;

import TCOTS.items.blocks.MonsterNestItem;
import TCOTS.items.blocks.NestSkullItem;
import TCOTS.items.geo.model.MonsterNestItemModel;
import TCOTS.items.geo.model.NestSkullItemModel;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class MonsterNestItemRenderer extends GeoItemRenderer<MonsterNestItem> {
    public MonsterNestItemRenderer() {
        super(new MonsterNestItemModel());
    }
}
