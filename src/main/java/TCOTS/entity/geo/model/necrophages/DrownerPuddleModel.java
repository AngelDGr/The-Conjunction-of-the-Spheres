package TCOTS.entity.geo.model.necrophages;

import TCOTS.TCOTS_Main;
import TCOTS.entity.misc.DrownerPuddleEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

public class DrownerPuddleModel extends GeoModel<DrownerPuddleEntity> {
    @Override
    public Identifier getModelResource(DrownerPuddleEntity animatable) {
        return Identifier.of(TCOTS_Main.MOD_ID, "geo/necrophages/drowner_puddle.geo.json");
    }

    @Override
    public Identifier getTextureResource(DrownerPuddleEntity animatable) {
        return Identifier.of(TCOTS_Main.MOD_ID, "textures/entity/necrophages/drowner/drowner_puddle.png");
    }

    @Override
    public Identifier getAnimationResource(DrownerPuddleEntity animatable) {
        return Identifier.of(TCOTS_Main.MOD_ID, "animations/necrophages/drowner_puddle.animation.json");
    }

    @Override
    public RenderLayer getRenderType(DrownerPuddleEntity animatable, Identifier texture) {
        return RenderLayer.getEntityTranslucent(texture);
    }

    @Override
    public void setCustomAnimations(DrownerPuddleEntity animatable, long instanceId, AnimationState<DrownerPuddleEntity> animationState) {
        super.setCustomAnimations(animatable, instanceId, animationState);
    }
}
