package TCOTS.entity.geo.model.ogroids;

import TCOTS.TCOTS_Main;
import TCOTS.entity.geo.model.TrollGeoModelBase;
import TCOTS.entity.monsters.ogroids.ForestTrollEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.animation.AnimationState;

public class ForestTrollModel extends TrollGeoModelBase<ForestTrollEntity> {
    @Override
    public ResourceLocation getModelResource(final ForestTrollEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "geo/ogroids/forest_troll.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(final ForestTrollEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "textures/entity/ogroids/troll/forest_troll.png");
    }

    @Override
    public ResourceLocation getAnimationResource(final ForestTrollEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "animations/ogroids/forest_troll.animation.json");
    }

    @Override
    public void setCustomAnimations(final ForestTrollEntity troll, final long instanceId, final AnimationState<ForestTrollEntity> animationState) {
        super.setCustomAnimations(troll, instanceId, animationState);

        final GeoBone band_right_up = getAnimationProcessor().getBone("band_right_up");
        final GeoBone band_left_up = getAnimationProcessor().getBone("band_left_up");
        final GeoBone crown = getAnimationProcessor().getBone("crown");
        final GeoBone barrel = getAnimationProcessor().getBone("barrel");
        final GeoBone neck_bone = getAnimationProcessor().getBone("neck_bone");


        band_right_up.setHidden(!troll.getClothing(0));
        band_left_up.setHidden(!troll.getClothing(1));
        crown.setHidden(!troll.getClothing(2));
        barrel.setHidden(!troll.getClothing(3));
        neck_bone.setHidden(!troll.getClothing(4));
    }

    @Override
    protected boolean hasArmZMovement(final ForestTrollEntity troll) {
        return troll.isCharging();
    }

    @Override
    protected float getLegsSpeed(final ForestTrollEntity entity) {
        return entity.isCharging()? 0.6f :0.5f;
    }

    @Override
    protected float getArmsAmount(final ForestTrollEntity entity) {
        return entity.isCharging()? 0.7f :0.6f;
    }
}
