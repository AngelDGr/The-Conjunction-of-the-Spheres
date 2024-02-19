package TCOTS.entity.geo.renderer.necrophages;

import TCOTS.TCOTS_Main;
import TCOTS.entity.geo.model.necrophages.RotfiendModel;
import TCOTS.entity.necrophages.RotfiendEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class RotfiendRenderer extends GeoEntityRenderer<RotfiendEntity> {
    public RotfiendRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new RotfiendModel());
    }


    @Override
    public Identifier getTextureLocation(RotfiendEntity animatable) {
        return new Identifier(TCOTS_Main.MOD_ID, "textures/entity/necrophages/rotfiend/rotfiend.png");
    }

}
