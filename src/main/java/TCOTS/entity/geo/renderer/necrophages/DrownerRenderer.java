package TCOTS.entity.geo.renderer.necrophages;

import TCOTS.TCOTS_Main;
import TCOTS.entity.geo.model.necrophages.DrownerModel;
import TCOTS.entity.necrophages.DrownerEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class DrownerRenderer extends GeoEntityRenderer<DrownerEntity> {
    public DrownerRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new DrownerModel());

        this.shadowRadius = 0.5f;
    }

    @Override
    public Identifier getTextureLocation(DrownerEntity animatable) {
        return Identifier.of(TCOTS_Main.MOD_ID, "textures/entity/necrophages/drowner/drowner.png");
    }

}
