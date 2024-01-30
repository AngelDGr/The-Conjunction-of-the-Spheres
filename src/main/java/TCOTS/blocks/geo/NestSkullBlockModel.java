package TCOTS.blocks.geo;

import TCOTS.TCOTS_Main;
import TCOTS.blocks.skull.NestSkullBlockEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;

public class NestSkullBlockModel extends GeoModel<NestSkullBlockEntity> {
    @Override
    public Identifier getModelResource(NestSkullBlockEntity animatable) {
        return new Identifier(TCOTS_Main.MOD_ID, "geo/misc/nest_skull.geo.json");
    }

    @Override
    public Identifier getTextureResource(NestSkullBlockEntity animatable) {
        return new Identifier(TCOTS_Main.MOD_ID, "textures/block/nest_skull.png");
    }

    @Override
    public Identifier getAnimationResource(NestSkullBlockEntity animatable) {
        return new Identifier(TCOTS_Main.MOD_ID, "animations/misc/nest_skull.animation.json");
    }
}
