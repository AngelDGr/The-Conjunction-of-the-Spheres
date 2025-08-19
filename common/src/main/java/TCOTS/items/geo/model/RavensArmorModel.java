package TCOTS.items.geo.model;

import TCOTS.TCOTS_Main;
import TCOTS.items.armor.RavensArmorItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class RavensArmorModel extends GeoModel<RavensArmorItem> {
    @Override
    public ResourceLocation getModelResource(final RavensArmorItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "geo/armor/ravens_armor.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(final RavensArmorItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "textures/models/armor/ravens_armor.png");
    }

    @Override
    public ResourceLocation getAnimationResource(final RavensArmorItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "animations/misc/dummy.animation.json");
    }
}
