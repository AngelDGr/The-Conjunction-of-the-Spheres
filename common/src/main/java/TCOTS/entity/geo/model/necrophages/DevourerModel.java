package TCOTS.entity.geo.model.necrophages;

import TCOTS.TCOTS_Main;
import TCOTS.entity.geo.model.BipedGeoModelBase;
import TCOTS.entity.monsters.necrophages.DevourerEntity;
import net.minecraft.resources.ResourceLocation;

public class DevourerModel extends BipedGeoModelBase<DevourerEntity> {
    @Override
    public ResourceLocation getModelResource(final DevourerEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "geo/necrophages/devourer.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(final DevourerEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "textures/entity/necrophages/devourer.png");
    }

    @Override
    public ResourceLocation getAnimationResource(final DevourerEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "animations/necrophages/devourer.animation.json");
    }
}
