package TCOTS.items.geo.model;

import TCOTS.TCOTS_Main;
import TCOTS.items.armor.ManticoreArmorItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class ManticoreArmorModel extends GeoModel<ManticoreArmorItem> {
    @Override
    public ResourceLocation getModelResource(ManticoreArmorItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "geo/armor/manticore_armor.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(ManticoreArmorItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "textures/models/armor/manticore_armor.png");
    }

    @Override
    public ResourceLocation getAnimationResource(ManticoreArmorItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "animations/misc/dummy.animation.json");
    }
}
