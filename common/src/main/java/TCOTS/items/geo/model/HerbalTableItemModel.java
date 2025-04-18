package TCOTS.items.geo.model;

import TCOTS.TCOTS_Main;
import TCOTS.items.blocks.HerbalTableItem;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class HerbalTableItemModel extends GeoModel<HerbalTableItem> {
    @Override
    public ResourceLocation getModelResource(HerbalTableItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "geo/block/herbal_table.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(HerbalTableItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "textures/block/herbal_table.png");
    }

    @Override
    public ResourceLocation getAnimationResource(HerbalTableItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "animations/misc/dummy.animation.json");
    }

    @Override
    public RenderType getRenderType(HerbalTableItem animatable, ResourceLocation texture) {
        return RenderType.entityTranslucent(texture);
    }
}
