package TCOTS.entity.geo.renderer.necrophages;

import TCOTS.TCOTS_Main;
import TCOTS.entity.geo.model.necrophages.GraveHagModel;
import TCOTS.entity.necrophages.GraveHagEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class GraveHagRenderer extends GeoEntityRenderer<GraveHagEntity> {
    public GraveHagRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new GraveHagModel());
    }

    @Override
    public Identifier getTextureLocation(GraveHagEntity animatable) {
        return new Identifier(TCOTS_Main.MOD_ID, "textures/entity/necrophages/grave_hag/grave_hag.png");
    }

    @Override
    public float getMotionAnimThreshold(GraveHagEntity animatable) {
        return 0.005f;
    }
}
