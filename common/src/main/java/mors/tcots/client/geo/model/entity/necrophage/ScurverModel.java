package mors.tcots.client.geo.model.entity.necrophage;

import mors.tcots.TCOTS_Main;
import mors.tcots.client.geo.animation.entity.necrophage.RotfiendAnimations;
import mors.tcots.client.geo.model.defaulted.monsters.DefaultedNecrophageModel;
import mors.tcots.entity.monsters.necrophages.ScurverEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;

public class ScurverModel extends DefaultedNecrophageModel<ScurverEntity> {

    public ScurverModel() {
        super(ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "scurver"), true);
    }

    @Override
    public void setCustomAnimations(final ScurverEntity entity, final long instanceId, final AnimationState<ScurverEntity> animationState) {
        super.setCustomAnimations(entity, instanceId, animationState);

        final AnimationController<GeoAnimatable> controller = entity.getAnimatableInstanceCache().getManagerForId(entity.getId()).getAnimationControllers().get("base_controller");

        final GeoBone head = getAnimationProcessor().getBone("head");
        if(head!=null){
            //Avoid weird sync with all nearby scurvers
            if (controller.isPlayingTriggeredAnimation() && controller.getCurrentRawAnimation() == RotfiendAnimations.EXPLOSION) return;
            if(head.getRotZ()!=0) head.setRotZ(0);
        }
    }
}
