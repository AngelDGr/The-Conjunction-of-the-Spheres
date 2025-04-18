package TCOTS.items.geo.model;

import TCOTS.TCOTS_Main;
import TCOTS.items.armor.WarriorsLeatherArmorItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class WarriorsLeatherArmorModel extends GeoModel<WarriorsLeatherArmorItem> {
    @Override
    public ResourceLocation getModelResource(WarriorsLeatherArmorItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "geo/armor/warriors_leather_armor.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(WarriorsLeatherArmorItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "textures/models/armor/warriors_leather_armor.png");
    }

    @Override
    public ResourceLocation getAnimationResource(WarriorsLeatherArmorItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "animations/misc/dummy.animation.json");
    }
}
