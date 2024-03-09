package TCOTS.entity.geo.renderer.necrophages;

import TCOTS.TCOTS_Main;
import TCOTS.entity.geo.model.necrophages.FogletModel;
import TCOTS.entity.necrophages.FogletEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class FogletRenderer extends GeoEntityRenderer<FogletEntity> {
    public FogletRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new FogletModel());
    }

    @Override
    public Identifier getTextureLocation(FogletEntity animatable) {
        return new Identifier(TCOTS_Main.MOD_ID, "textures/entity/necrophages/foglet/foglet.png");
    }
}
