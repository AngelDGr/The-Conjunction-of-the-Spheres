package TCOTS.entity.geo.model.ogroids;

import TCOTS.TCOTS_Main;
import TCOTS.entity.geo.model.TrollGeoModelBase;
import TCOTS.entity.ogroids.ForestTrollEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.animation.AnimationState;

public class ForestTrollModel extends TrollGeoModelBase<ForestTrollEntity> {
    @Override
    public Identifier getModelResource(ForestTrollEntity animatable) {
        return Identifier.of(TCOTS_Main.MOD_ID, "geo/ogroids/forest_troll.geo.json");
    }

    @Override
    public Identifier getTextureResource(ForestTrollEntity animatable) {
        return Identifier.of(TCOTS_Main.MOD_ID, "textures/entity/ogroids/troll/forest_troll.png");
    }

    @Override
    public Identifier getAnimationResource(ForestTrollEntity animatable) {
        return Identifier.of(TCOTS_Main.MOD_ID, "animations/ogroids/forest_troll.animation.json");
    }

    @Override
    public void setCustomAnimations(ForestTrollEntity troll, long instanceId, AnimationState<ForestTrollEntity> animationState) {
        super.setCustomAnimations(troll, instanceId, animationState);

        GeoBone band_right_up = getAnimationProcessor().getBone("band_right_up");
        GeoBone band_left_up = getAnimationProcessor().getBone("band_left_up");
        GeoBone crown = getAnimationProcessor().getBone("crown");
        GeoBone barrel = getAnimationProcessor().getBone("barrel");
        GeoBone neck_bone = getAnimationProcessor().getBone("neck_bone");


        band_right_up.setHidden(!troll.getClothing(0));
        band_left_up.setHidden(!troll.getClothing(1));
        crown.setHidden(!troll.getClothing(2));
        barrel.setHidden(!troll.getClothing(3));
        neck_bone.setHidden(!troll.getClothing(4));
    }

    @Override
    protected boolean hasArmZMovement(ForestTrollEntity troll) {
        return troll.isCharging();
    }

    @Override
    protected float getLegsSpeed(ForestTrollEntity entity) {
        return entity.isCharging()? 0.6f :0.5f;
    }

    @Override
    protected float getArmsAmount(ForestTrollEntity entity) {
        return entity.isCharging()? 0.7f :0.6f;
    }
}
