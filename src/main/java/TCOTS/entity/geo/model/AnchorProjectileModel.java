package TCOTS.entity.geo.model;

import TCOTS.TCOTS_Main;
import TCOTS.entity.misc.AnchorProjectileEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;

public class AnchorProjectileModel extends GeoModel<AnchorProjectileEntity> {
    @Override
    public Identifier getModelResource(AnchorProjectileEntity animatable) {
        return new Identifier(TCOTS_Main.MOD_ID, "geo/anchor.geo.json");
    }

    @Override
    public Identifier getTextureResource(AnchorProjectileEntity animatable) {
        return new Identifier(TCOTS_Main.MOD_ID, "textures/entity/anchor.png");
    }

    @Override
    public Identifier getAnimationResource(AnchorProjectileEntity animatable) {
        return new Identifier(TCOTS_Main.MOD_ID, "animations/misc/dummy.animation.json");
    }


}
