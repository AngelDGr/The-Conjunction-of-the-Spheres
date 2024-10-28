package TCOTS.items.geo.model;

import TCOTS.TCOTS_Main;
import TCOTS.items.blocks.WintersBladeSkeletonItem;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

public class WintersBladeSkeletonItemModel extends GeoModel<WintersBladeSkeletonItem> {
    @Override
    public Identifier getModelResource(WintersBladeSkeletonItem animatable) {
        return new Identifier(TCOTS_Main.MOD_ID, "geo/block/winters_blade_skeleton.geo.json");
    }

    @Override
    public Identifier getTextureResource(WintersBladeSkeletonItem animatable) {
        return new Identifier(TCOTS_Main.MOD_ID, "textures/block/winters_blade_skeleton.png");
    }

    @Override
    public Identifier getAnimationResource(WintersBladeSkeletonItem animatable) {
        return new Identifier(TCOTS_Main.MOD_ID, "animations/misc/dummy.animation.json");
    }

    @Override
    public RenderLayer getRenderType(WintersBladeSkeletonItem animatable, Identifier texture) {
        return RenderLayer.getEntityTranslucent(texture);
    }

    @Override
    public void setCustomAnimations(WintersBladeSkeletonItem animatable, long instanceId, AnimationState<WintersBladeSkeletonItem> animationState) {
        CoreGeoBone block = getAnimationProcessor().getBone("block");
        if(block.getRotY() != 0){
            block.setRotY(0);
        }

        if(block.getPosY() != 0){
            block.setPosY(0);
        }

        if(block.getPosX() != 0){
            block.setPosX(0);
        }

        if(block.getPosZ() != 0){
            block.setPosZ(0);
        }
    }
}
