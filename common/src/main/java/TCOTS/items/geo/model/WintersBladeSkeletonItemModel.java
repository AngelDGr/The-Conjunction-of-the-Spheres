package TCOTS.items.geo.model;

import TCOTS.TCOTS_Main;
import TCOTS.items.blocks.WintersBladeSkeletonItem;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

public class WintersBladeSkeletonItemModel extends GeoModel<WintersBladeSkeletonItem> {
    @Override
    public ResourceLocation getModelResource(WintersBladeSkeletonItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "geo/block/winters_blade_skeleton.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(WintersBladeSkeletonItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "textures/block/winters_blade_skeleton.png");
    }

    @Override
    public ResourceLocation getAnimationResource(WintersBladeSkeletonItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "animations/misc/dummy.animation.json");
    }

    @Override
    public RenderType getRenderType(WintersBladeSkeletonItem animatable, ResourceLocation texture) {
        return RenderType.entityTranslucent(texture);
    }

    @Override
    public void setCustomAnimations(WintersBladeSkeletonItem animatable, long instanceId, AnimationState<WintersBladeSkeletonItem> animationState) {
        GeoBone block = getAnimationProcessor().getBone("block");
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
