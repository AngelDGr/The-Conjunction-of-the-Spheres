package TCOTS.entity.geo.model.ogroids;

import TCOTS.TCOTS_Main;
import TCOTS.entity.geo.model.BipedGeoModelBase;
import TCOTS.entity.ogroids.IceGiantEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;

public class IceGiantModel extends BipedGeoModelBase<IceGiantEntity> {
    @Override
    public Identifier getModelResource(IceGiantEntity animatable) {
        return new Identifier(TCOTS_Main.MOD_ID, "geo/ogroids/ice_giant.geo.json");
    }

    @Override
    public Identifier getTextureResource(IceGiantEntity animatable) {
        if(animatable.isGiantSleeping() && !animatable.isGiantWakingUp()){
            return new Identifier(TCOTS_Main.MOD_ID, "textures/entity/ogroids/ice_giant/ice_giant_sleeping.png");
        }

        return new Identifier(TCOTS_Main.MOD_ID, "textures/entity/ogroids/ice_giant/ice_giant.png");
    }

    @Override
    public Identifier getAnimationResource(IceGiantEntity animatable) {
        return new Identifier(TCOTS_Main.MOD_ID, "animations/ogroids/ice_giant.animation.json");
    }

    @Override
    public void setCustomAnimations(IceGiantEntity entity, long instanceId, AnimationState<IceGiantEntity> animationState) {
        super.setCustomAnimations(entity, instanceId, animationState);

        CoreGeoBone cloth_front =  getAnimationProcessor().getBone("clothFront");
        CoreGeoBone cloth_back =  getAnimationProcessor().getBone("clothBack");

        if(cloth_front!=null && cloth_back!=null){
            cloth_front.setRotX((float)-(Math.sin(animationState.getLimbSwing()*getLegsSpeed(entity))*(animationState.getLimbSwingAmount()*getLegsAmount(entity))));
            cloth_back.setRotX((float) (Math.sin(animationState.getLimbSwing()*getLegsSpeed(entity))*(animationState.getLimbSwingAmount()*getLegsAmount(entity))));
        }

    }

    @Override
    protected boolean hasArmZMovement(IceGiantEntity entity) {
        return entity.isCharging();
    }

    @Override
    protected float getLegsSpeed(IceGiantEntity entity) {
        return entity.isCharging()? 0.6f :0.5f;
    }

    @Override
    protected float getArmsAmount(IceGiantEntity entity) {
        return entity.isCharging()? 0.7f :0.6f;
    }
}
