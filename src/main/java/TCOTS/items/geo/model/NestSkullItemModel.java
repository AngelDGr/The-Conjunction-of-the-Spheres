package TCOTS.items.geo.model;

import TCOTS.TCOTS_Main;
import TCOTS.items.blocks.NestSkullItem;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

public class NestSkullItemModel extends GeoModel<NestSkullItem> {
    @Override
    public Identifier getModelResource(NestSkullItem animatable) {
        return Identifier.of(TCOTS_Main.MOD_ID, "geo/block/nest_skull.geo.json");
    }

    @Override
    public Identifier getTextureResource(NestSkullItem animatable) {
        return Identifier.of(TCOTS_Main.MOD_ID, "textures/block/nest_skull.png");
    }

    @Override
    public Identifier getAnimationResource(NestSkullItem animatable) {
        return Identifier.of(TCOTS_Main.MOD_ID, "animations/misc/nest_skull.animation.json");
    }

    @Override
    public void setCustomAnimations(NestSkullItem animatable, long instanceId, AnimationState<NestSkullItem> animationState) {
        GeoBone head = getAnimationProcessor().getBone("head");
        if(head.getRotY() != 0){
            head.setRotY(0);
        }

        if(head.getPosY() != 0){
            head.setPosY(0);
        }

        if(head.getPosX() != 0){
            head.setPosX(0);
        }


        if(head.getPosZ() != 0){
            head.setPosZ(0);
        }
    }
}
