package TCOTS.blocks.geo;

import TCOTS.TCOTS_Main;
import TCOTS.blocks.skull.NestSkullBlockEntity;
import TCOTS.blocks.skull.NestWallSkullBlock;
import net.minecraft.block.AbstractSkullBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.SkullBlock;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationPropertyHelper;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

public class NestSkullBlockModel extends GeoModel<NestSkullBlockEntity> {
    @Override
    public Identifier getModelResource(NestSkullBlockEntity animatable) {
        return new Identifier(TCOTS_Main.MOD_ID, "geo/misc/nest_skull.geo.json");
    }

    @Override
    public Identifier getTextureResource(NestSkullBlockEntity animatable) {
        return new Identifier(TCOTS_Main.MOD_ID, "textures/block/nest_skull.png");
    }

    @Override
    public Identifier getAnimationResource(NestSkullBlockEntity animatable) {
        return new Identifier(TCOTS_Main.MOD_ID, "animations/misc/nest_skull.animation.json");
    }


//    public void setHeadRotation(float animationProgress, float yaw, float pitch) {
//        this.head.setRotZ(yaw * ((float)Math.PI / 180));
////        this.head.pitch = ;
//        this.head.setRotX(pitch * ((float)Math.PI / 180));
//
//    }

    @Override
    public void setCustomAnimations(NestSkullBlockEntity animatable, long instanceId, AnimationState<NestSkullBlockEntity> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);
        CoreGeoBone head = getAnimationProcessor().getBone("head");

        BlockState blockState = animatable.getCachedState();
        boolean bl = blockState.getBlock() instanceof NestWallSkullBlock;

        Direction direction;
        if(bl){
            direction=blockState.get(NestWallSkullBlock.FACING);
        }
        else{
            direction= null;
        }

        int k;

        if (bl) {
            k = RotationPropertyHelper.fromDirection(direction.getOpposite());
        } else {
            k = blockState.get(SkullBlock.ROTATION);
        }

        float h = RotationPropertyHelper.toDegrees(k);

        if(bl){
            head.setRotY(0);
            head.setPosY(4);
            head.setPosX(0);
            head.setPosZ(3.999f);
        }
        else{
            if(head.getPosY()!=0){
                head.setPosY(0);
            }
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
