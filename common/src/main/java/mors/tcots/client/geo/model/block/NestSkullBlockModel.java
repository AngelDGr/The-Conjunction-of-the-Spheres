package mors.tcots.client.geo.model.block;

import mors.tcots.TCOTS_Main;
import mors.tcots.block.entity.NestSkullBlockEntity;
import mors.tcots.block.skull.NestSkullBlock;
import mors.tcots.block.skull.NestWallSkullBlock;
import mors.tcots.client.geo.model.defaulted.DefaultedBlockModelNoAnimation;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.SkullBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RotationSegment;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;

public class NestSkullBlockModel extends DefaultedBlockModelNoAnimation<NestSkullBlockEntity> {
    public NestSkullBlockModel() {
        super(ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "nest_skull"));
    }

    @Override
    public void setCustomAnimations(final NestSkullBlockEntity animatable, final long instanceId, final AnimationState<NestSkullBlockEntity> animationState) {
        final GeoBone head = getAnimationProcessor().getBone("head");

        final BlockState blockState = animatable.getBlockState();
        final boolean isInWall = blockState.getBlock() instanceof NestWallSkullBlock;

        final Direction direction;
        if(isInWall){
            direction=blockState.getValue(NestSkullBlock.FACING);
        }
        else{
            direction= null;
        }

        final int k;

        if (isInWall) {
            k = RotationSegment.convertToSegment(direction.getOpposite());
        } else {
            k = blockState.getValue(SkullBlock.ROTATION);
        }

        final float h = RotationSegment.convertToDegrees(k);

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

    public static class Item extends DefaultedBlockModelNoAnimation<NestSkullBlock.NestSkullItem> {
        public Item() {
            super(ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "nest_skull"));
        }

        @Override
        public void setCustomAnimations(final NestSkullBlock.NestSkullItem animatable, final long instanceId, final AnimationState<NestSkullBlock.NestSkullItem> animationState) {
            final GeoBone head = getAnimationProcessor().getBone("head");
            if(head.getRotY() != 0) head.setRotY(0);
            if(head.getPosY() != 0) head.setPosY(0);
            if(head.getPosX() != 0) head.setPosX(0);
            if(head.getPosZ() != 0) head.setPosZ(0);
        }
    }
}
