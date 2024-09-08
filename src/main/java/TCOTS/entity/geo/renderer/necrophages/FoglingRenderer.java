package TCOTS.entity.geo.renderer.necrophages;

import TCOTS.TCOTS_Main;
import TCOTS.entity.geo.model.necrophages.FoglingModel;
import TCOTS.entity.misc.FoglingEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class FoglingRenderer extends GeoEntityRenderer<FoglingEntity> {
    public FoglingRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new FoglingModel());
        this.shadowRadius = 0.6f;
    }

    @Override
    public Identifier getTextureLocation(FoglingEntity animatable) {
        return new Identifier(TCOTS_Main.MOD_ID, "textures/entity/necrophages/foglet/fogling.png");
    }

    @Override
    public float getMotionAnimThreshold(FoglingEntity animatable) {
        return 0.005f;
    }
    @Override
    public void actuallyRender(MatrixStack poseStack, FoglingEntity animatable, BakedGeoModel model, RenderLayer renderType, VertexConsumerProvider bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {

        super.actuallyRender(poseStack, animatable, model, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, animatable.getAlphaValue());
    }


}
