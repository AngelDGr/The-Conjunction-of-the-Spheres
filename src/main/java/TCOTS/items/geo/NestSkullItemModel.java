package TCOTS.items.geo;

import TCOTS.TCOTS_Main;
import TCOTS.blocks.skull.NestSkullBlockEntity;
import TCOTS.blocks.skull.NestWallSkullBlock;
import TCOTS.items.blocks.NestSkullItem;
import net.minecraft.block.BlockState;
import net.minecraft.block.SkullBlock;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationPropertyHelper;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

public class NestSkullItemModel extends GeoModel<NestSkullItem> {
    @Override
    public Identifier getModelResource(NestSkullItem animatable) {
        return new Identifier(TCOTS_Main.MOD_ID, "geo/misc/nest_skull.geo.json");
    }

    @Override
    public Identifier getTextureResource(NestSkullItem animatable) {
        return new Identifier(TCOTS_Main.MOD_ID, "textures/block/nest_skull.png");
    }

    @Override
    public Identifier getAnimationResource(NestSkullItem animatable) {
        return new Identifier(TCOTS_Main.MOD_ID, "animations/misc/nest_skull.animation.json");
    }

    @Override
    public void setCustomAnimations(NestSkullItem animatable, long instanceId, AnimationState<NestSkullItem> animationState) {
        CoreGeoBone head = getAnimationProcessor().getBone("head");
        if(head.getRotY() != 0){
            head.setRotY(0);
        }

        if(head.getPosY() != 0){
            head.setPosY(0);
        }

        if(head.getPosX() != 0){
            head.setPosX(0);
        }


        if(head.getPosZ() != 0){
            head.setPosZ(0);
        }
    }
}
