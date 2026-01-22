package mors.tcots.client.geo.model.block;

import mors.tcots.TCOTS_Main;
import mors.tcots.block.WintersBladeSkeletonBlock;
import mors.tcots.block.entity.WintersBladeSkeletonBlockEntity;
import mors.tcots.client.geo.model.defaulted.DefaultedBlockModelNoAnimation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.SkullBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RotationSegment;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;

public class WintersBladeSkeletonModel extends DefaultedBlockModelNoAnimation<WintersBladeSkeletonBlockEntity> {
    public WintersBladeSkeletonModel() {
        super(ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "winters_blade_skeleton"));
    }

    @Override
    public ResourceLocation getTextureResource(final WintersBladeSkeletonBlockEntity animatable) {
        return getSkeletonTexture();
    }

    @Override
    public void setCustomAnimations(final WintersBladeSkeletonBlockEntity animatable, final long instanceId, final AnimationState<WintersBladeSkeletonBlockEntity> animationState) {
        final GeoBone block = getAnimationProcessor().getBone("block");

        final BlockState blockState = animatable.getBlockState();

        block.setPosY(0.001f);

        if(block.getPosX()!=0) block.setPosX(0);
        if(block.getPosZ()!=0) block.setPosZ(0);

        block.setRotY(RotationSegment.convertToDegrees(blockState.getValue(SkullBlock.ROTATION)) * ((float)Math.PI / -180));
    }

    public static class Item extends DefaultedBlockModelNoAnimation<WintersBladeSkeletonBlock.Item> {
        public Item() {
            super(ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "winters_blade_skeleton"));
        }

        @Override
        public ResourceLocation getTextureResource(final WintersBladeSkeletonBlock.Item animatable) {
            return getSkeletonTexture();
        }

        @Override
        public void setCustomAnimations(final WintersBladeSkeletonBlock.Item animatable, final long instanceId, final AnimationState<WintersBladeSkeletonBlock.Item> animationState) {
            final GeoBone block = getAnimationProcessor().getBone("block");
            if(block.getRotY() != 0) block.setRotY(0);
            if(block.getPosY() != 0) block.setPosY(0);
            if(block.getPosX() != 0) block.setPosX(0);
            if(block.getPosZ() != 0) block.setPosZ(0);
        }
    }

    public static ResourceLocation getSkeletonTexture(){
        return ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "textures/block/skeleton_block.png");
    }
}
