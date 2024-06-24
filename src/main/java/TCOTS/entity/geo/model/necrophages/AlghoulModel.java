package TCOTS.entity.geo.model.necrophages;

import TCOTS.TCOTS_Main;
import TCOTS.entity.geo.model.QuadrupedGhoulModelBase;
import TCOTS.entity.necrophages.AlghoulEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;

public class AlghoulModel extends QuadrupedGhoulModelBase<AlghoulEntity> {

    @Override
    public Identifier getModelResource(AlghoulEntity animatable) {
        return new Identifier(TCOTS_Main.MOD_ID, "geo/necrophages/alghoul.geo.json");
    }

    @Override
    public Identifier getTextureResource(AlghoulEntity animatable) {
        return new Identifier(TCOTS_Main.MOD_ID, "textures/entity/necrophages/alghoul/alghoul.png");
    }

    @Override
    public Identifier getAnimationResource(AlghoulEntity animatable) {
        return new Identifier(TCOTS_Main.MOD_ID, "animations/necrophages/alghoul.animation.json");
    }

    @Override
    public void setCustomAnimations(AlghoulEntity entity, long instanceId, AnimationState<AlghoulEntity> animationState) {
        super.setCustomAnimations(entity, instanceId, animationState);

        CoreGeoBone large_spikes_head = getAnimationProcessor().getBone("spikes_large");
        CoreGeoBone large_spikes_body = getAnimationProcessor().getBone("spikes_large_body");

        if(large_spikes_head != null){
            large_spikes_head.setHidden(!entity.getIsSpiked());
        }
        if(large_spikes_body!=null){
            large_spikes_body.setHidden(!entity.getIsSpiked());
        }
    }
}
