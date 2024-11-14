package TCOTS.entity.geo.model.ogroids;

import TCOTS.TCOTS_Main;
import TCOTS.entity.geo.model.BipedGeoModelBase;
import TCOTS.entity.ogroids.NekkerWarriorEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.animation.AnimationState;

public class NekkerWarriorModel extends BipedGeoModelBase<NekkerWarriorEntity> {
    @Override
    public Identifier getModelResource(NekkerWarriorEntity animatable) {
        return Identifier.of(TCOTS_Main.MOD_ID, "geo/ogroids/nekker_warrior.geo.json");
    }

    @Override
    public Identifier getTextureResource(NekkerWarriorEntity animatable) {
        return Identifier.of(TCOTS_Main.MOD_ID, "textures/entity/ogroids/nekker/nekker_warrior.png");
    }

    @Override
    public Identifier getAnimationResource(NekkerWarriorEntity animatable) {
        return Identifier.of(TCOTS_Main.MOD_ID, "animations/ogroids/nekker_warrior.animation.json");
    }

    @Override
    public void setCustomAnimations(NekkerWarriorEntity entity, long instanceId, AnimationState<NekkerWarriorEntity> animationState) {
        super.setCustomAnimations(entity, instanceId, animationState);

        GeoBone cloth_front =  getAnimationProcessor().getBone("ClothFront");
        GeoBone cloth_back =  getAnimationProcessor().getBone("ClothBack");

        if(cloth_front!=null && cloth_back!=null){
            cloth_front.setRotX((float)-(Math.sin(animationState.getLimbSwing()*getLegsSpeed(entity))*(animationState.getLimbSwingAmount()*getLegsAmount(entity))));
            cloth_back.setRotX((float) (Math.sin(animationState.getLimbSwing()*getLegsSpeed(entity))*(animationState.getLimbSwingAmount()*getLegsAmount(entity))));
        }
    }
}
