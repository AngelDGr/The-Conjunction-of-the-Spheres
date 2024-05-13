package TCOTS.entity.geo.renderer.ogroids;

import TCOTS.TCOTS_Main;
import TCOTS.entity.geo.model.ogroids.NekkerModel;
import TCOTS.entity.ogroids.NekkerEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class NekkerRenderer  extends GeoEntityRenderer<NekkerEntity> {
    public NekkerRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new NekkerModel());
    }

    @Override
    public Identifier getTextureLocation(NekkerEntity animatable) {
        return new Identifier(TCOTS_Main.MOD_ID, "textures/entity/ogroids/nekker/nekker.png");
    }

}
