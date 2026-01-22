package mors.tcots.client.geo.model.entity.necrophage;

import mors.tcots.TCOTS_Main;
import mors.tcots.client.geo.model.defaulted.monsters.DefaultedNecrophageModel;
import mors.tcots.entity.monsters.necrophages.AlghoulEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.animation.AnimationState;

public class AlghoulModel extends DefaultedNecrophageModel<AlghoulEntity> {

    public AlghoulModel() {
        super(ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "alghoul"), true);
    }

    @Override
    public ResourceLocation getTextureResource(final AlghoulEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "textures/entity/monster/necrophage/alghoul/alghoul.png");
    }

    @Override
    public void setCustomAnimations(final AlghoulEntity entity, final long instanceId, final AnimationState<AlghoulEntity> animationState) {
        super.setCustomAnimations(entity, instanceId, animationState);

        final GeoBone spikesOutHead = getAnimationProcessor().getBone("spikesOutHead");
        final GeoBone spikesOutBody = getAnimationProcessor().getBone("spikesOutBody");

        if(spikesOutHead != null){
            spikesOutHead.setHidden(!entity.getIsSpiked());
        }
        if(spikesOutBody!=null){
            spikesOutBody.setHidden(!entity.getIsSpiked());
        }
    }
}
