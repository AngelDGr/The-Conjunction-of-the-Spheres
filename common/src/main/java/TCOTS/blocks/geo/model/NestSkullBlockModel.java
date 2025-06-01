package TCOTS.blocks.geo.model;

import TCOTS.TCOTS_Main;
import TCOTS.registry.TCOTS_Blocks;
import TCOTS.blocks.entity.NestSkullBlockEntity;
import TCOTS.blocks.skull.NestWallSkullBlock;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.SkullBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RotationSegment;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

public class NestSkullBlockModel extends GeoModel<NestSkullBlockEntity> {
    @Override
    public ResourceLocation getModelResource(NestSkullBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "geo/block/nest_skull.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(NestSkullBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "textures/block/nest_skull.png");
    }

    @Override
    public ResourceLocation getAnimationResource(NestSkullBlockEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "animations/misc/dummy.animation.json");
    }


    @Override
    public void setCustomAnimations(NestSkullBlockEntity animatable, long instanceId, AnimationState<NestSkullBlockEntity> animationState) {
        GeoBone head = getAnimationProcessor().getBone("head");

        BlockState blockState = animatable.getBlockState();
        boolean isInWall = blockState.getBlock() instanceof NestWallSkullBlock;

        Direction direction;
        if(isInWall){
            direction=blockState.getValue(TCOTS_Blocks.FACING);
        }
        else{
            direction= null;
        }

        int k;

        if (isInWall) {
            k = RotationSegment.convertToSegment(direction.getOpposite());
        } else {
            k = blockState.getValue(SkullBlock.ROTATION);
        }

        float h = RotationSegment.convertToDegrees(k);

        if(isInWall){
            head.setRotY(0);
            head.setPosY(4);
            head.setPosX(0);
            head.setPosZ(3.999f);
        }
        else{
            head.setPosY(0.001f);

            if(head.getPosX()!=0){
                head.setPosX(0);
            }
            if(head.getPosZ()!=0){
                head.setPosZ(0);
            }
            head.setRotY( h * ((float)Math.PI / -180));
        }
    }

}
