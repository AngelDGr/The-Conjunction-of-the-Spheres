package mors.tcots.client.geo.model.entity.ogroid;

import mors.tcots.TCOTS_Main;
import mors.tcots.client.geo.model.defaulted.monsters.DefaultedOgroidGeoModel;
import mors.tcots.entity.monsters.ogroids.RockTrollEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Pose;
import software.bernie.geckolib.animation.AnimationState;

public class RockTrollModel extends DefaultedOgroidGeoModel<RockTrollEntity> {

    public RockTrollModel() {
        super(ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "rock_troll"), true);
    }

    @Override
    public void setCustomAnimations(final RockTrollEntity animatable, final long instanceId, final AnimationState<RockTrollEntity> animationState) {
        if(animatable.hasPose(Pose.CROUCHING) || animatable.hasPose(Pose.USING_TONGUE)) return;

        super.setCustomAnimations(animatable, instanceId, animationState);
        if (this.getBone("head").isPresent())
            this.getBone("head").get().setRotZ(0);
    }
}
