package TCOTS.entity.geo.renderer.necrophages;

import TCOTS.TCOTS_Main;
import TCOTS.entity.geo.model.necrophages.DrownerModel;
import TCOTS.entity.necrophages.DrownerEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class DrownerRenderer extends GeoEntityRenderer<DrownerEntity> {
    public DrownerRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new DrownerModel());
    }

    @Override
    public Identifier getTextureLocation(DrownerEntity animatable) {
        return new Identifier(TCOTS_Main.MOD_ID, "textures/entity/necrophages/drowner/drowner.png");
    }

    @Override
    public void render(DrownerEntity entity, float entityYaw, float partialTick, MatrixStack poseStack, VertexConsumerProvider bufferSource, int packedLight) {
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }
}
