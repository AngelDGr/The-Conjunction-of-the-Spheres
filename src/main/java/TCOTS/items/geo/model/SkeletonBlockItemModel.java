package TCOTS.items.geo.model;

import TCOTS.TCOTS_Main;
import TCOTS.items.blocks.SkeletonBlockItem;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

public class SkeletonBlockItemModel extends GeoModel<SkeletonBlockItem> {
    @Override
    public Identifier getModelResource(SkeletonBlockItem animatable) {
        return Identifier.of(TCOTS_Main.MOD_ID, "geo/block/skeleton_block.geo.json");
    }

    @Override
    public Identifier getTextureResource(SkeletonBlockItem animatable) {
        return Identifier.of(TCOTS_Main.MOD_ID, "textures/block/skeleton_block.png");
    }

    @Override
    public Identifier getAnimationResource(SkeletonBlockItem animatable) {
        return Identifier.of(TCOTS_Main.MOD_ID, "animations/misc/dummy.animation.json");
    }

    @Override
    public RenderLayer getRenderType(SkeletonBlockItem animatable, Identifier texture) {
        return RenderLayer.getEntityTranslucent(texture);
    }

    @Override
    public void setCustomAnimations(SkeletonBlockItem animatable, long instanceId, AnimationState<SkeletonBlockItem> animationState) {
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
