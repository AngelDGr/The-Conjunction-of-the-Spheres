package TCOTS.entity.geo.model.ogroids;

import TCOTS.TCOTS_Main;
import TCOTS.entity.geo.model.BipedGeoModelBase;
import TCOTS.entity.ogroids.NekkerEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;

public class NekkerModel extends BipedGeoModelBase<NekkerEntity> {
    @Override
    public Identifier getModelResource(NekkerEntity animatable) {
        return new Identifier(TCOTS_Main.MOD_ID, "geo/ogroids/nekker.geo.json");
    }

    @Override
    public Identifier getTextureResource(NekkerEntity animatable) {
        return new Identifier(TCOTS_Main.MOD_ID, "textures/entity/ogroids/nekker/nekker.png");
    }

    @Override
    public Identifier getAnimationResource(NekkerEntity animatable) {
        return new Identifier(TCOTS_Main.MOD_ID, "animations/ogroids/nekker.animation.json");
    }

    @Override
    public void setCustomAnimations(NekkerEntity entity, long instanceId, AnimationState<NekkerEntity> animationState) {
        super.setCustomAnimations(entity, instanceId, animationState);

        CoreGeoBone cloth_front =  getAnimationProcessor().getBone("ClothFront");
        CoreGeoBone cloth_back =  getAnimationProcessor().getBone("ClothBack");

        if(cloth_front!=null && cloth_back!=null){
            cloth_front.setRotX((float)-(Math.sin(animationState.getLimbSwing()*getLegsSpeed(entity))*(animationState.getLimbSwingAmount()*getLegsAmount(entity))));
            cloth_back.setRotX((float) (Math.sin(animationState.getLimbSwing()*getLegsSpeed(entity))*(animationState.getLimbSwingAmount()*getLegsAmount(entity))));
        }
    }
}
