package TCOTS.entity.geo.renderer.necrophages;

import TCOTS.TCOTS_Main;
import TCOTS.entity.geo.model.necrophages.DevourerModel;
import TCOTS.entity.necrophages.DevourerEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class DevourerRenderer extends GeoEntityRenderer<DevourerEntity> {
    public DevourerRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new DevourerModel());
        this.shadowRadius = 0.55f;
    }

    @Override
    public float getMotionAnimThreshold(DevourerEntity animatable) {
        return 0.001f;
    }
    @Override
    public Identifier getTextureLocation(DevourerEntity animatable) {
        return new Identifier(TCOTS_Main.MOD_ID, "textures/entity/necrophages/devourer.png");
    }

}
