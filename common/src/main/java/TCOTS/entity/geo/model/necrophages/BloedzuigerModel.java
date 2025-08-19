package TCOTS.entity.geo.model.necrophages;

import TCOTS.TCOTS_Main;
import TCOTS.entity.geo.model.BipedGeoModelBase;
import TCOTS.entity.monsters.necrophages.BloedzuigerEntity;
import net.minecraft.resources.ResourceLocation;

public class BloedzuigerModel extends BipedGeoModelBase<BloedzuigerEntity> {
    protected float getArmsAmount(final BloedzuigerEntity entity){
        return 1.2f;
    }

    protected float getLegsAmount(final BloedzuigerEntity entity){
        return 1.0f;
    }

    @Override
    public ResourceLocation getModelResource(final BloedzuigerEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "geo/necrophages/bloedzuiger.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(final BloedzuigerEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "textures/entity/necrophages/bloedzuiger.png");
    }

    @Override
    public ResourceLocation getAnimationResource(final BloedzuigerEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "animations/necrophages/bloedzuiger.animation.json");
    }
}
