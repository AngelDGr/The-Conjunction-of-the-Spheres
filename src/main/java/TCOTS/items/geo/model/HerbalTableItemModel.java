package TCOTS.items.geo.model;

import TCOTS.TCOTS_Main;
import TCOTS.items.blocks.HerbalTableItem;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;

public class HerbalTableItemModel extends GeoModel<HerbalTableItem> {
    @Override
    public Identifier getModelResource(HerbalTableItem animatable) {
        return new Identifier(TCOTS_Main.MOD_ID, "geo/misc/herbal_table.geo.json");
    }

    @Override
    public Identifier getTextureResource(HerbalTableItem animatable) {
        return new Identifier(TCOTS_Main.MOD_ID, "textures/block/herbal_table.png");
    }

    @Override
    public Identifier getAnimationResource(HerbalTableItem animatable) {
        return new Identifier(TCOTS_Main.MOD_ID, "animations/misc/dummy.animation.json");
    }

    @Override
    public RenderLayer getRenderType(HerbalTableItem animatable, Identifier texture) {
        return RenderLayer.getEntityTranslucent(texture);
    }
}
