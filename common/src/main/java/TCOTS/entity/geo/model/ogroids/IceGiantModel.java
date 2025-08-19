package TCOTS.entity.geo.model.ogroids;

import TCOTS.TCOTS_Main;
import TCOTS.entity.geo.model.BipedGeoModelBase;
import TCOTS.entity.monsters.ogroids.IceGiantEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.animation.AnimationState;

public class IceGiantModel extends BipedGeoModelBase<IceGiantEntity> {
    @Override
    public ResourceLocation getModelResource(final IceGiantEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "geo/ogroids/ice_giant.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(final IceGiantEntity animatable) {
        if(animatable.isGiantSleeping() && !animatable.isGiantWakingUp()){
            return ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "textures/entity/ogroids/ice_giant/ice_giant_sleeping.png");
        }

        return ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "textures/entity/ogroids/ice_giant/ice_giant.png");
    }

    @Override
    public ResourceLocation getAnimationResource(final IceGiantEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "animations/ogroids/ice_giant.animation.json");
    }

    @Override
    public void setCustomAnimations(final IceGiantEntity entity, final long instanceId, final AnimationState<IceGiantEntity> animationState) {
        super.setCustomAnimations(entity, instanceId, animationState);

        final GeoBone cloth_front =  getAnimationProcessor().getBone("clothFront");
        final GeoBone cloth_back =  getAnimationProcessor().getBone("clothBack");


        if(cloth_front!=null && cloth_back!=null){
            cloth_front.setRotX((float)-(Math.sin(animationState.getLimbSwing()*getLegsSpeed(entity))*(animationState.getLimbSwingAmount()*getLegsAmount(entity))));
            cloth_back.setRotX((float) (Math.sin(animationState.getLimbSwing()*getLegsSpeed(entity))*(animationState.getLimbSwingAmount()*getLegsAmount(entity))));
        }
    }

    @Override
    protected boolean hasArmZMovement(final IceGiantEntity entity) {
        return entity.isCharging();
    }

    @Override
    protected float getLegsSpeed(final IceGiantEntity entity) {
        return entity.isCharging()? 0.6f :0.5f;
    }

    @Override
    protected float getArmsAmount(final IceGiantEntity entity) {
        return entity.isCharging()? 0.7f :0.6f;
    }
}
