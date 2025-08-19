package TCOTS.items.geo.model;

import TCOTS.TCOTS_Main;
import TCOTS.items.blocks.HerbalTableItem;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class HerbalTableItemModel extends GeoModel<HerbalTableItem> {
    @Override
    public ResourceLocation getModelResource(final HerbalTableItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "geo/block/herbal_table.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(final HerbalTableItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "textures/block/herbal_table.png");
    }

    @Override
    public ResourceLocation getAnimationResource(final HerbalTableItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "animations/misc/dummy.animation.json");
    }

    @Override
    public RenderType getRenderType(final HerbalTableItem animatable, final ResourceLocation texture) {
        return RenderType.entityTranslucent(texture);
    }
}
