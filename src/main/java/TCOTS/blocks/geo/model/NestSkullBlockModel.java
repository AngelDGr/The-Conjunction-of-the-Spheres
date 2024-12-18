package TCOTS.blocks.geo.model;

import TCOTS.TCOTS_Main;
import TCOTS.blocks.entity.NestSkullBlockEntity;
import TCOTS.blocks.skull.NestWallSkullBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.SkullBlock;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationPropertyHelper;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

public class NestSkullBlockModel extends GeoModel<NestSkullBlockEntity> {
    @Override
    public Identifier getModelResource(NestSkullBlockEntity animatable) {
        return Identifier.of(TCOTS_Main.MOD_ID, "geo/block/nest_skull.geo.json");
    }
    

    @Override
    public Identifier getTextureResource(NestSkullBlockEntity animatable) {
        return Identifier.of(TCOTS_Main.MOD_ID, "textures/block/nest_skull.png");
    }

    @Override
    public Identifier getAnimationResource(NestSkullBlockEntity animatable) {
        return Identifier.of(TCOTS_Main.MOD_ID, "animations/misc/dummy.animation.json");
    }


    @Override
    public void setCustomAnimations(NestSkullBlockEntity animatable, long instanceId, AnimationState<NestSkullBlockEntity> animationState) {
        GeoBone head = getAnimationProcessor().getBone("head");

        BlockState blockState = animatable.getCachedState();
        boolean isInWall = blockState.getBlock() instanceof NestWallSkullBlock;

        Direction direction;
        if(isInWall){
            direction=blockState.get(NestWallSkullBlock.FACING);
        }
        else{
            direction= null;
        }

        int k;

        if (isInWall) {
            k = RotationPropertyHelper.fromDirection(direction.getOpposite());
        } else {
            k = blockState.get(SkullBlock.ROTATION);
        }

        float h = RotationPropertyHelper.toDegrees(k);

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
