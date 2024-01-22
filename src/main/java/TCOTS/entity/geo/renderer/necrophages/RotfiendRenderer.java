package TCOTS.entity.geo.renderer.necrophages;

import TCOTS.TCOTS_Main;
import TCOTS.entity.geo.model.necrophages.DrownerModel;
import TCOTS.entity.geo.model.necrophages.RotfiendModel;
import TCOTS.entity.necrophages.DrownerEntity;
import TCOTS.entity.necrophages.RotfiendEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class RotfiendRenderer extends GeoEntityRenderer<RotfiendEntity> {
    public RotfiendRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new RotfiendModel());
    }


    @Override
    public Identifier getTextureLocation(RotfiendEntity animatable) {
        return new Identifier(TCOTS_Main.MOD_ID, "textures/entity/necrophages/rotfiend/rotfiend.png");
    }

    @Override
    public void render(RotfiendEntity entity, float entityYaw, float partialTick, MatrixStack poseStack, VertexConsumerProvider bufferSource, int packedLight) {
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }

}
