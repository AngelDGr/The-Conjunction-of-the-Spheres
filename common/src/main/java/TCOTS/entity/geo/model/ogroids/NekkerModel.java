package TCOTS.entity.geo.model.ogroids;

import TCOTS.TCOTS_Main;
import TCOTS.entity.geo.model.BipedGeoModelBase;
import TCOTS.entity.monsters.ogroids.NekkerEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.animation.AnimationState;

public class NekkerModel extends BipedGeoModelBase<NekkerEntity> {
    @Override
    public ResourceLocation getModelResource(final NekkerEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "geo/ogroids/nekker.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(final NekkerEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "textures/entity/ogroids/nekker/nekker.png");
    }

    @Override
    public ResourceLocation getAnimationResource(final NekkerEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "animations/ogroids/nekker.animation.json");
    }

    @Override
    public void setCustomAnimations(final NekkerEntity entity, final long instanceId, final AnimationState<NekkerEntity> animationState) {
        super.setCustomAnimations(entity, instanceId, animationState);

        final GeoBone cloth_front =  getAnimationProcessor().getBone("ClothFront");
        final GeoBone cloth_back =  getAnimationProcessor().getBone("ClothBack");

        if(cloth_front!=null && cloth_back!=null){
            cloth_front.setRotX((float)-(Math.sin(animationState.getLimbSwing()*getLegsSpeed(entity))*(animationState.getLimbSwingAmount()*getLegsAmount(entity))));
            cloth_back.setRotX((float) (Math.sin(animationState.getLimbSwing()*getLegsSpeed(entity))*(animationState.getLimbSwingAmount()*getLegsAmount(entity))));
        }
    }
}
