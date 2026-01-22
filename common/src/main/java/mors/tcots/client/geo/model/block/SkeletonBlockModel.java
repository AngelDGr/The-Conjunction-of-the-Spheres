package mors.tcots.client.geo.model.block;

import mors.tcots.TCOTS_Main;
import mors.tcots.block.SkeletonBlock;
import mors.tcots.block.entity.SkeletonBlockEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RotationSegment;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.model.DefaultedBlockGeoModel;

public class SkeletonBlockModel extends DefaultedBlockGeoModel<SkeletonBlockEntity> {
    public SkeletonBlockModel() {
        super(ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "skeleton_block"));
    }

    @Override
    public void setCustomAnimations(final SkeletonBlockEntity animatable, final long instanceId, final AnimationState<SkeletonBlockEntity> animationState) {
        final GeoBone block = getAnimationProcessor().getBone("block");
        final GeoBone head = getAnimationProcessor().getBone("head");
        final BlockState blockState = animatable.getBlockState();

        final String[] armorBones = {
                "armorHead",
                "armorBody",
                "armorLeftArm",
                "armorRightArm",
                "armorRightLeg",
                "armorRightBoot",
                "armorLeftLeg",
                "armorLeftBoot"
        };

        for (final String boneName : armorBones) {
            final GeoBone bone = getAnimationProcessor().getBone(boneName);
            if (bone != null) bone.setHidden(!blockState.getValue(SkeletonBlock.HAS_ARMOR));
        }

        if(block!=null) block.setRotY(RotationSegment.convertToDegrees(blockState.getValue(SkeletonBlock.ROTATION)) * ((float)Math.PI / -180));
        if(head!=null) head.setHidden(blockState.getValue(SkeletonBlock.HIDE_HEAD));
    }

    public static class Item extends DefaultedBlockGeoModel<SkeletonBlock.Item> {

        public Item() {
            super(ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "skeleton_block"));
        }

        @Override
        public void setCustomAnimations(final SkeletonBlock.Item animatable, final long instanceId, final AnimationState<SkeletonBlock.Item> animationState) {
            final String[] armorBones = {
                    "armorHead",
                    "armorBody",
                    "armorLeftArm",
                    "armorRightArm",
                    "armorRightLeg",
                    "armorRightBoot",
                    "armorLeftLeg",
                    "armorLeftBoot"
            };

            for (final String boneName : armorBones) {
                final GeoBone bone = getAnimationProcessor().getBone(boneName);
                if (bone != null) bone.setHidden(true);
            }

            final GeoBone skeleton = getAnimationProcessor().getBone("skeleton");
            if(skeleton.getRotZ()!=0) skeleton.setRotZ(0);
            if(skeleton.getRotX()!=0) skeleton.setRotX(0);
            if(skeleton.getRotY()!=0) skeleton.setRotY(0);
            final GeoBone block = getAnimationProcessor().getBone("block");
            if(block.getRotZ()!=0) block.setRotZ(0);
            if(block.getRotX()!=0) block.setRotX(0);
            if(block.getRotY()!=0) block.setRotY(0);
            final GeoBone head = getAnimationProcessor().getBone("head");
            head.setHidden(false);
        }
    }
}
