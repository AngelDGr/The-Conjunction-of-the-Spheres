package TCOTS.blocks.geo.model;

import TCOTS.TCOTS_Main;
import TCOTS.blocks.entity.HerbalTableBlockEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class HerbalTableModel extends GeoModel<HerbalTableBlockEntity> {
    @Override
    public ResourceLocation getModelResource(HerbalTableBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "geo/block/herbal_table.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(HerbalTableBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "textures/block/herbal_table.png");
    }

    @Override
    public ResourceLocation getAnimationResource(HerbalTableBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "animations/misc/dummy.animation.json");
    }
}
