package TCOTS.entity.geo.model.necrophages;

import TCOTS.TCOTS_Main;
import TCOTS.entity.geo.model.QuadrupedGhoulModelBase;
import TCOTS.entity.monsters.necrophages.AlghoulEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.animation.AnimationState;

public class AlghoulModel extends QuadrupedGhoulModelBase<AlghoulEntity> {

    @Override
    public ResourceLocation getModelResource(final AlghoulEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "geo/necrophages/alghoul.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(final AlghoulEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "textures/entity/necrophages/alghoul/alghoul.png");
    }

    @Override
    public ResourceLocation getAnimationResource(final AlghoulEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "animations/necrophages/alghoul.animation.json");
    }

    @Override
    public void setCustomAnimations(final AlghoulEntity entity, final long instanceId, final AnimationState<AlghoulEntity> animationState) {
        super.setCustomAnimations(entity, instanceId, animationState);

        final GeoBone large_spikes_head = getAnimationProcessor().getBone("spikes_large");
        final GeoBone large_spikes_body = getAnimationProcessor().getBone("spikes_large_body");

        if(large_spikes_head != null){
            large_spikes_head.setHidden(!entity.getIsSpiked());
        }
        if(large_spikes_body!=null){
            large_spikes_body.setHidden(!entity.getIsSpiked());
        }
    }
}
