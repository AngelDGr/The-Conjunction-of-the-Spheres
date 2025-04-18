package TCOTS.blocks.geo.model;

import TCOTS.TCOTS_Main;
import TCOTS.blocks.entity.WintersBladeSkeletonBlockEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.SkullBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RotationSegment;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.model.GeoModel;

public class WintersBladeSkeletonModel extends GeoModel<WintersBladeSkeletonBlockEntity> {
    @Override
    public ResourceLocation getModelResource(WintersBladeSkeletonBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "geo/block/winters_blade_skeleton.geo.json");
    }


    @Override
    public ResourceLocation getTextureResource(WintersBladeSkeletonBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "textures/block/winters_blade_skeleton.png");
    }

    @Override
    public ResourceLocation getAnimationResource(WintersBladeSkeletonBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "animations/misc/dummy.animation.json");
    }

    @Override
    public void setCustomAnimations(WintersBladeSkeletonBlockEntity animatable, long instanceId, AnimationState<WintersBladeSkeletonBlockEntity> animationState) {
        GeoBone block = getAnimationProcessor().getBone("block");

        BlockState blockState = animatable.getBlockState();

        block.setPosY(0.001f);

        if(block.getPosX()!=0){
            block.setPosX(0);
        }
        if(block.getPosZ()!=0){
            block.setPosZ(0);
        }

        block.setRotY(RotationSegment.convertToDegrees(blockState.getValue(SkullBlock.ROTATION)) * ((float)Math.PI / -180));
    }
}
