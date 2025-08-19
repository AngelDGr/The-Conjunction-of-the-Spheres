package TCOTS.entity.geo.renderer.necrophages;

import TCOTS.TCOTS_Main;
import TCOTS.entity.geo.model.necrophages.DrownerModel;
import TCOTS.entity.monsters.necrophages.DrownerEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class DrownerRenderer extends GeoEntityRenderer<DrownerEntity> {
    public DrownerRenderer(final EntityRendererProvider.Context renderManager) {
        super(renderManager, new DrownerModel());

        this.shadowRadius = 0.5f;
    }

    @Override
    public ResourceLocation getTextureLocation(final DrownerEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "textures/entity/necrophages/drowner/drowner.png");
    }

}
