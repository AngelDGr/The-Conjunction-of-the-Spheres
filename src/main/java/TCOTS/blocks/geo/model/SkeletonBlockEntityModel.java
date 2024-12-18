package TCOTS.blocks.geo.model;

import TCOTS.TCOTS_Main;
import TCOTS.blocks.SkeletonBlock;
import TCOTS.blocks.entity.SkeletonBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationPropertyHelper;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

public class SkeletonBlockEntityModel extends GeoModel<SkeletonBlockEntity> {
    @Override
    public Identifier getModelResource(SkeletonBlockEntity animatable) {
        BlockState blockState = animatable.getCachedState();
        return switch (blockState.get(SkeletonBlock.SHAPE)){
            case 0  -> Identifier.of(TCOTS_Main.MOD_ID, "geo/block/skeleton/half_body.geo.json");

            case 1  -> Identifier.of(TCOTS_Main.MOD_ID, "geo/block/skeleton/legs_only.geo.json");

            case 2  -> Identifier.of(TCOTS_Main.MOD_ID, "geo/block/skeleton/sitting.geo.json");

            case 3  -> Identifier.of(TCOTS_Main.MOD_ID, "geo/block/skeleton/half_body-up.geo.json");

            case 4  -> Identifier.of(TCOTS_Main.MOD_ID, "geo/block/skeleton/crossed_arms.geo.json");

            case 5  -> Identifier.of(TCOTS_Main.MOD_ID, "geo/block/skeleton/reaching.geo.json");

            //Default
            default -> Identifier.of(TCOTS_Main.MOD_ID, "geo/block/skeleton_block.geo.json");
        };
    }

    @Override
    public Identifier getTextureResource(SkeletonBlockEntity animatable) {
        BlockState blockState = animatable.getCachedState();
        if(blockState.get(SkeletonBlock.HAS_ARMOR)){
            return Identifier.of(TCOTS_Main.MOD_ID, "textures/block/skeleton_block_armor.png");
        } else {
            return Identifier.of(TCOTS_Main.MOD_ID, "textures/block/skeleton_block.png");
        }
    }

    @Override
    public Identifier getAnimationResource(SkeletonBlockEntity animatable) {
        return Identifier.of(TCOTS_Main.MOD_ID, "animations/misc/dummy.animation.json");
    }

    @Override
    public void setCustomAnimations(SkeletonBlockEntity animatable, long instanceId, AnimationState<SkeletonBlockEntity> animationState) {
        GeoBone block = getAnimationProcessor().getBone("block");
        GeoBone head = getAnimationProcessor().getBone("head");

        BlockState blockState = animatable.getCachedState();

        block.setPosY(0.001f);

        if(block.getPosX()!=0){
            block.setPosX(0);
        }
        if(block.getPosZ()!=0){
            block.setPosZ(0);
        }

        block.setRotY(RotationPropertyHelper.toDegrees(blockState.get(SkeletonBlock.ROTATION)) * ((float)Math.PI / -180));

        if(head!=null){
            head.setHidden(blockState.get(SkeletonBlock.HIDE_HEAD));
        }
    }
}
