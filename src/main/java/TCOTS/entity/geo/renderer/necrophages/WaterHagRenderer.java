package TCOTS.entity.geo.renderer.necrophages;

import TCOTS.TCOTS_Main;
import TCOTS.entity.geo.model.necrophages.WaterHagModel;
import TCOTS.entity.necrophages.WaterHagEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class WaterHagRenderer extends GeoEntityRenderer<WaterHagEntity> {
    public WaterHagRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new WaterHagModel());
    }

    @Override
    public Identifier getTextureLocation(WaterHagEntity animatable) {
        return new Identifier(TCOTS_Main.MOD_ID, "textures/entity/necrophages/water_hag/water_hag.png");
    }
}
