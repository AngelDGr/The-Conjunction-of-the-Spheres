package TCOTS.entity.geo.renderer.necrophages;

import TCOTS.TCOTS_Main;
import TCOTS.entity.geo.model.necrophages.ScurverModel;
import TCOTS.entity.necrophages.ScurverEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class ScurverRenderer extends GeoEntityRenderer<ScurverEntity> {
    public ScurverRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new ScurverModel());
    }


    @Override
    public Identifier getTextureLocation(ScurverEntity animatable) {
        return new Identifier(TCOTS_Main.MOD_ID, "textures/entity/necrophages/scurver/scurver.png");
    }

}
