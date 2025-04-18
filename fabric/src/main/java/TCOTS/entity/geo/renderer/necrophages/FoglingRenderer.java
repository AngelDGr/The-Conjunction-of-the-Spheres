package TCOTS.entity.geo.renderer.necrophages;

import TCOTS.TCOTS_Main;
import TCOTS.entity.geo.model.necrophages.FoglingModel;
import TCOTS.entity.misc.FoglingEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.util.Color;

public class FoglingRenderer extends GeoEntityRenderer<FoglingEntity> {
    public FoglingRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new FoglingModel());
        this.shadowRadius = 0.6f;
    }

    @Override
    public ResourceLocation getTextureLocation(FoglingEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "textures/entity/necrophages/foglet/fogling.png");
    }

    @Override
    public float getMotionAnimThreshold(FoglingEntity animatable) {
        return 0.005f;
    }

    @Override
    public void actuallyRender(PoseStack poseStack, FoglingEntity animatable, BakedGeoModel model, @Nullable RenderType renderType, MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour) {
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
