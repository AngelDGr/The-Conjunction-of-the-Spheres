package TCOTS.entity.geo.model;

import TCOTS.TCOTS_Main;
import TCOTS.entity.misc.AnchorProjectileEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class AnchorProjectileModel extends GeoModel<AnchorProjectileEntity> {
    @Override
    public ResourceLocation getModelResource(final AnchorProjectileEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "geo/anchor.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(final AnchorProjectileEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "textures/entity/anchor.png");
    }

    @Override
    public ResourceLocation getAnimationResource(final AnchorProjectileEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "animations/misc/dummy.animation.json");
    }


}
