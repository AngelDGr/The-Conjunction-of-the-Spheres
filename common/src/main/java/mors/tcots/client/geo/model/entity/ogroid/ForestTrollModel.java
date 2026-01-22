package mors.tcots.client.geo.model.entity.ogroid;

import mors.tcots.TCOTS_Main;
import mors.tcots.client.geo.model.defaulted.monsters.DefaultedOgroidGeoModel;
import mors.tcots.entity.monsters.ogroids.ForestTrollEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Pose;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;

public class ForestTrollModel extends DefaultedOgroidGeoModel<ForestTrollEntity> {

    public ForestTrollModel() {
        super(ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "forest_troll"), true);
    }

    @Override
    public void setCustomAnimations(final ForestTrollEntity animatable, final long instanceId, final AnimationState<ForestTrollEntity> animationState) {
        final GeoBone band_right_up = getAnimationProcessor().getBone("band_right_up");
        final GeoBone band_left_up = getAnimationProcessor().getBone("band_left_up");
        final GeoBone crown = getAnimationProcessor().getBone("crown");
        final GeoBone barrel = getAnimationProcessor().getBone("barrel");
        final GeoBone neck_bone = getAnimationProcessor().getBone("neck_wood");

        band_right_up.setHidden(!animatable.getClothing(0));
        band_left_up.setHidden(!animatable.getClothing(1));
        crown.setHidden(!animatable.getClothing(2));
        barrel.setHidden(!animatable.getClothing(3));
        neck_bone.setHidden(!animatable.getClothing(4));

        if(animatable.hasPose(Pose.CROUCHING) || animatable.hasPose(Pose.USING_TONGUE)) return;

        super.setCustomAnimations(animatable, instanceId, animationState);
        if (this.getBone("head").isPresent())
            this.getBone("head").get().setRotZ(0);

        super.setCustomAnimations(animatable, instanceId, animationState);
    }
}
