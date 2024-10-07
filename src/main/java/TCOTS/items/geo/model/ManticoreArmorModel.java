package TCOTS.items.geo.model;

import TCOTS.TCOTS_Main;
import TCOTS.items.armor.ManticoreArmorItem;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;

public class ManticoreArmorModel extends GeoModel<ManticoreArmorItem> {
    @Override
    public Identifier getModelResource(ManticoreArmorItem animatable) {
        return new Identifier(TCOTS_Main.MOD_ID, "geo/armor/manticore_armor.geo.json");
    }

    @Override
    public Identifier getTextureResource(ManticoreArmorItem animatable) {
        return new Identifier(TCOTS_Main.MOD_ID, "textures/models/armor/manticore_armor.png");
    }

    @Override
    public Identifier getAnimationResource(ManticoreArmorItem animatable) {
        return new Identifier(TCOTS_Main.MOD_ID, "animations/misc/dummy.animation.json");
    }
}
