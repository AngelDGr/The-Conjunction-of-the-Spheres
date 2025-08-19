package TCOTS.entity.geo.model.ogroids;

import TCOTS.TCOTS_Main;
import TCOTS.entity.geo.model.BipedGeoModelBase;
import TCOTS.entity.monsters.ogroids.CyclopsEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.animation.AnimationState;

public class CyclopsModel extends BipedGeoModelBase<CyclopsEntity> {
    @Override
    public ResourceLocation getModelResource(final CyclopsEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "geo/ogroids/cyclops.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(final CyclopsEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "textures/entity/ogroids/cyclops.png");
    }

    @Override
    public ResourceLocation getAnimationResource(final CyclopsEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "animations/ogroids/cyclops.animation.json");
    }

    @Override
    public void setCustomAnimations(final CyclopsEntity entity, final long instanceId, final AnimationState<CyclopsEntity> animationState) {
        super.setCustomAnimations(entity, instanceId, animationState);

        final GeoBone cloth_front =  getAnimationProcessor().getBone("clothFront");
        final GeoBone cloth_back =  getAnimationProcessor().getBone("clothBack");

        if(cloth_front!=null && cloth_back!=null){
            cloth_front.setRotX((float)-(Math.sin(animationState.getLimbSwing()*getLegsSpeed(entity))*(animationState.getLimbSwingAmount()*getLegsAmount(entity))));
            cloth_back.setRotX((float) (Math.sin(animationState.getLimbSwing()*getLegsSpeed(entity))*(animationState.getLimbSwingAmount()*getLegsAmount(entity))));
        }
    }
}
