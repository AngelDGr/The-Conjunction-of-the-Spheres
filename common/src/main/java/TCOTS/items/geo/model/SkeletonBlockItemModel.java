package TCOTS.items.geo.model;

import TCOTS.TCOTS_Main;
import TCOTS.items.blocks.SkeletonBlockItem;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

public class SkeletonBlockItemModel extends GeoModel<SkeletonBlockItem> {
    @Override
    public ResourceLocation getModelResource(final SkeletonBlockItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "geo/block/skeleton_block.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(final SkeletonBlockItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "textures/block/skeleton_block.png");
    }

    @Override
    public ResourceLocation getAnimationResource(final SkeletonBlockItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "animations/misc/dummy.animation.json");
    }

    @Override
    public RenderType getRenderType(final SkeletonBlockItem animatable, final ResourceLocation texture) {
        return RenderType.entityTranslucent(texture);
    }

    @Override
    public void setCustomAnimations(final SkeletonBlockItem animatable, final long instanceId, final AnimationState<SkeletonBlockItem> animationState) {
        final GeoBone block = getAnimationProcessor().getBone("block");
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
