package TCOTS.entity.geo.renderer.necrophages;

import TCOTS.TCOTS_Main;
import TCOTS.entity.geo.model.necrophages.FogletModel;
import TCOTS.entity.necrophages.FogletEntity;
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

public class FogletRenderer extends GeoEntityRenderer<FogletEntity> {
    public FogletRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new FogletModel());
        this.shadowRadius = 0.6f;
    }
    @Override
    public Identifier getTextureLocation(FogletEntity animatable) {
        return Identifier.of(TCOTS_Main.MOD_ID, "textures/entity/necrophages/foglet/foglet.png");
    }

    @Override
    public float getMotionAnimThreshold(FogletEntity animatable) {
        return 0.005f;
    }

    @Override
    public void actuallyRender(MatrixStack poseStack, FogletEntity animatable, BakedGeoModel model, @Nullable RenderLayer renderType, VertexConsumerProvider bufferSource, @Nullable VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour) {
        Color holdColor = new Color(colour);

        int newColor =  Color.ofARGB(animatable.getAlphaValue(), holdColor.getRedFloat(), holdColor.getGreenFloat(), holdColor.getBlueFloat()).argbInt();

        super.actuallyRender(poseStack, animatable, model, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, newColor);
    }
}
