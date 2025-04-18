package TCOTS.blocks.geo.model;

import TCOTS.TCOTS_Main;
import TCOTS.blocks.TCOTS_Blocks;
import TCOTS.blocks.entity.SkeletonBlockEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RotationSegment;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

public class SkeletonBlockEntityModel extends GeoModel<SkeletonBlockEntity> {
    @Override
    public ResourceLocation getModelResource(SkeletonBlockEntity animatable) {
        BlockState blockState = animatable.getBlockState();
        return switch (blockState.getValue(TCOTS_Blocks.SHAPE)){
            case 0  -> ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "geo/block/skeleton/half_body.geo.json");

            case 1  -> ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "geo/block/skeleton/legs_only.geo.json");

            case 2  -> ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "geo/block/skeleton/sitting.geo.json");

            case 3  -> ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "geo/block/skeleton/half_body-up.geo.json");

            case 4  -> ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "geo/block/skeleton/crossed_arms.geo.json");

            case 5  -> ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "geo/block/skeleton/reaching.geo.json");

            //Default
            default -> ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "geo/block/skeleton_block.geo.json");
        };
    }

    @Override
    public ResourceLocation getTextureResource(SkeletonBlockEntity animatable) {
        BlockState blockState = animatable.getBlockState();
        if(blockState.getValue(TCOTS_Blocks.HAS_ARMOR)){
            return ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "textures/block/skeleton_block_armor.png");
        } else {
            return ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "textures/block/skeleton_block.png");
        }
    }

    @Override
    public ResourceLocation getAnimationResource(SkeletonBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "animations/misc/dummy.animation.json");
    }

    @Override
    public void setCustomAnimations(SkeletonBlockEntity animatable, long instanceId, AnimationState<SkeletonBlockEntity> animationState) {
        GeoBone block = getAnimationProcessor().getBone("block");
        GeoBone head = getAnimationProcessor().getBone("head");

        BlockState blockState = animatable.getBlockState();

        block.setPosY(0.001f);

        if(block.getPosX()!=0){
            block.setPosX(0);
        }
        if(block.getPosZ()!=0){
            block.setPosZ(0);
        }

        block.setRotY(RotationSegment.convertToDegrees(blockState.getValue(TCOTS_Blocks.ROTATION)) * ((float)Math.PI / -180));

        if(head!=null){
            head.setHidden(blockState.getValue(TCOTS_Blocks.HIDE_HEAD));
        }
    }
}
