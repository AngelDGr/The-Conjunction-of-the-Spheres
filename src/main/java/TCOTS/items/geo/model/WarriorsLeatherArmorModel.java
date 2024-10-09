package TCOTS.items.geo.model;

import TCOTS.TCOTS_Main;
import TCOTS.items.armor.WarriorsLeatherArmorItem;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;

public class WarriorsLeatherArmorModel extends GeoModel<WarriorsLeatherArmorItem> {
    @Override
    public Identifier getModelResource(WarriorsLeatherArmorItem animatable) {
        return new Identifier(TCOTS_Main.MOD_ID, "geo/armor/warriors_leather_armor.geo.json");
    }

    @Override
    public Identifier getTextureResource(WarriorsLeatherArmorItem animatable) {
        return new Identifier(TCOTS_Main.MOD_ID, "textures/models/armor/warriors_leather_armor.png");
    }

    @Override
    public Identifier getAnimationResource(WarriorsLeatherArmorItem animatable) {
        return new Identifier(TCOTS_Main.MOD_ID, "animations/misc/dummy.animation.json");
    }
}
