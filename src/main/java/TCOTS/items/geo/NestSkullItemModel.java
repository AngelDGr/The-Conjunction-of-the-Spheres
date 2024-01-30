package TCOTS.items.geo;

import TCOTS.TCOTS_Main;
import TCOTS.blocks.skull.NestSkullBlockEntity;
import TCOTS.items.blocks.NestSkullItem;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.model.GeoModel;

public class NestSkullItemModel extends GeoModel<NestSkullItem> {
    @Override
    public Identifier getModelResource(NestSkullItem animatable) {
        return new Identifier(TCOTS_Main.MOD_ID, "geo/misc/nest_skull.geo.json");
    }

    @Override
    public Identifier getTextureResource(NestSkullItem animatable) {
        return new Identifier(TCOTS_Main.MOD_ID, "textures/block/nest_skull.png");
    }

    @Override
    public Identifier getAnimationResource(NestSkullItem animatable) {
        return new Identifier(TCOTS_Main.MOD_ID, "animations/misc/nest_skull.animation.json");
    }
}
