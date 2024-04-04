package TCOTS.blocks.geo.model;

import TCOTS.TCOTS_Main;
import TCOTS.blocks.entity.HerbalTableBlockEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;

public class HerbalTableModel extends GeoModel<HerbalTableBlockEntity> {
    @Override
    public Identifier getModelResource(HerbalTableBlockEntity animatable) {
        return new Identifier(TCOTS_Main.MOD_ID, "geo/misc/herbal_table.geo.json");
    }

    @Override
    public Identifier getTextureResource(HerbalTableBlockEntity animatable) {
        return new Identifier(TCOTS_Main.MOD_ID, "textures/block/herbal_table.png");
    }

    @Override
    public Identifier getAnimationResource(HerbalTableBlockEntity animatable) {
        return new Identifier(TCOTS_Main.MOD_ID, "animations/misc/dummy.animation.json");
    }
}
