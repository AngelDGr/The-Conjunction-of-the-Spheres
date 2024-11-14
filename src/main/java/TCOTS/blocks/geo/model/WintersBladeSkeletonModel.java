package TCOTS.blocks.geo.model;

import TCOTS.TCOTS_Main;
import TCOTS.blocks.entity.WintersBladeSkeletonBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.SkullBlock;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationPropertyHelper;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.model.GeoModel;

public class WintersBladeSkeletonModel extends GeoModel<WintersBladeSkeletonBlockEntity> {
    @Override
    public Identifier getModelResource(WintersBladeSkeletonBlockEntity animatable) {
        return Identifier.of(TCOTS_Main.MOD_ID, "geo/block/winters_blade_skeleton.geo.json");
    }


    @Override
    public Identifier getTextureResource(WintersBladeSkeletonBlockEntity animatable) {
        return Identifier.of(TCOTS_Main.MOD_ID, "textures/block/winters_blade_skeleton.png");
    }

    @Override
    public Identifier getAnimationResource(WintersBladeSkeletonBlockEntity animatable) {
        return Identifier.of(TCOTS_Main.MOD_ID, "animations/misc/dummy.animation.json");
    }

    @Override
    public void setCustomAnimations(WintersBladeSkeletonBlockEntity animatable, long instanceId, AnimationState<WintersBladeSkeletonBlockEntity> animationState) {
        GeoBone block = getAnimationProcessor().getBone("block");

        BlockState blockState = animatable.getCachedState();

        block.setPosY(0.001f);

        if(block.getPosX()!=0){
            block.setPosX(0);
        }
        if(block.getPosZ()!=0){
            block.setPosZ(0);
        }

        block.setRotY(RotationPropertyHelper.toDegrees(blockState.get(SkullBlock.ROTATION)) * ((float)Math.PI / -180));
    }
}
