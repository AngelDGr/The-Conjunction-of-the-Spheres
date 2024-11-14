package TCOTS.items.geo.model;

import TCOTS.TCOTS_Main;
import TCOTS.items.armor.RavensArmorItem;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;

public class RavensArmorModel extends GeoModel<RavensArmorItem> {
    @Override
    public Identifier getModelResource(RavensArmorItem animatable) {
        return Identifier.of(TCOTS_Main.MOD_ID, "geo/armor/ravens_armor.geo.json");
    }

    @Override
    public Identifier getTextureResource(RavensArmorItem animatable) {
        return Identifier.of(TCOTS_Main.MOD_ID, "textures/models/armor/ravens_armor.png");
    }

    @Override
    public Identifier getAnimationResource(RavensArmorItem animatable) {
        return Identifier.of(TCOTS_Main.MOD_ID, "animations/misc/dummy.animation.json");
    }
}
