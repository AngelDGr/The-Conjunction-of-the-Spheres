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
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.util.Color;

public class FoglingRenderer extends GeoEntityRenderer<FoglingEntity> {
    public FoglingRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new FoglingModel());
        this.shadowRadius = 0.6f;
    }

    @Override
    public Identifier getTextureLocation(FoglingEntity animatable) {
        return Identifier.of(TCOTS_Main.MOD_ID, "textures/entity/necrophages/foglet/fogling.png");
    }

    @Override
    public float getMotionAnimThreshold(FoglingEntity animatable) {
        return 0.005f;
    }

    @Override
    public void actuallyRender(MatrixStack poseStack, FoglingEntity animatable, BakedGeoModel model, @Nullable RenderLayer renderType, VertexConsumerProvider bufferSource, @Nullable VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour) {
        Color holdColor = new Color(colour);

        int newColor =  Color.ofRGBA(
                        holdColor.getRedFloat(),
                        holdColor.getGreenFloat(),
                        holdColor.getBlueFloat(),
                        animatable.getAlphaValue())
                .argbInt();

        super.actuallyRender(poseStack, animatable, model, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, newColor);
    }
}
