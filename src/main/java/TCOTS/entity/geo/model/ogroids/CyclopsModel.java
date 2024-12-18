package TCOTS.entity.geo.model.ogroids;

import TCOTS.TCOTS_Main;
import TCOTS.entity.geo.model.BipedGeoModelBase;
import TCOTS.entity.ogroids.CyclopsEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.animation.AnimationState;

public class CyclopsModel extends BipedGeoModelBase<CyclopsEntity> {
    @Override
    public Identifier getModelResource(CyclopsEntity animatable) {
        return Identifier.of(TCOTS_Main.MOD_ID, "geo/ogroids/cyclops.geo.json");
    }

    @Override
    public Identifier getTextureResource(CyclopsEntity animatable) {
        return Identifier.of(TCOTS_Main.MOD_ID, "textures/entity/ogroids/cyclops.png");
    }

    @Override
    public Identifier getAnimationResource(CyclopsEntity animatable) {
        return Identifier.of(TCOTS_Main.MOD_ID, "animations/ogroids/cyclops.animation.json");
    }

    @Override
    public void setCustomAnimations(CyclopsEntity entity, long instanceId, AnimationState<CyclopsEntity> animationState) {
        super.setCustomAnimations(entity, instanceId, animationState);

        GeoBone cloth_front =  getAnimationProcessor().getBone("clothFront");
        GeoBone cloth_back =  getAnimationProcessor().getBone("clothBack");

        if(cloth_front!=null && cloth_back!=null){
            cloth_front.setRotX((float)-(Math.sin(animationState.getLimbSwing()*getLegsSpeed(entity))*(animationState.getLimbSwingAmount()*getLegsAmount(entity))));
            cloth_back.setRotX((float) (Math.sin(animationState.getLimbSwing()*getLegsSpeed(entity))*(animationState.getLimbSwingAmount()*getLegsAmount(entity))));
        }
    }
}
