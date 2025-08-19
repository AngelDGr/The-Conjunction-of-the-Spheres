package TCOTS.entity.geo.model.necrophages;

import TCOTS.TCOTS_Main;
import TCOTS.entity.geo.model.BipedGeoModelBase;
import TCOTS.entity.monsters.necrophages.RotfiendEntity;
import net.minecraft.resources.ResourceLocation;

public class RotfiendModel extends BipedGeoModelBase<RotfiendEntity> {

    @Override
    public ResourceLocation getModelResource(final RotfiendEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "geo/necrophages/rotfiend.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(final RotfiendEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "textures/entity/necrophages/rotfiend.png");
    }

    @Override
    public ResourceLocation getAnimationResource(final RotfiendEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "animations/necrophages/rotfiend.animation.json");
    }

}
