package mors.tcots.client.geo.renderer.entity.necrophages;

import mors.tcots.client.geo.model.entity.necrophage.FoglingModel;
import mors.tcots.entity.misc.FoglingEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.util.Color;

public class FoglingRenderer extends GeoEntityRenderer<FoglingEntity> {
    public FoglingRenderer(final EntityRendererProvider.Context renderManager) {
        super(renderManager, new FoglingModel());
        this.shadowRadius = 0.6f;
    }

    @Override
    public float getMotionAnimThreshold(final FoglingEntity animatable) {
        return 0.005f;
    }

    @Override
    public void actuallyRender(final PoseStack poseStack, final FoglingEntity animatable, final BakedGeoModel model, @Nullable final RenderType renderType, final MultiBufferSource bufferSource, @Nullable final VertexConsumer buffer, final boolean isReRender, final float partialTick, final int packedLight, final int packedOverlay, final int colour) {
        final Color holdColor = new Color(colour);

        final int newColor =  Color.ofRGBA(
                        holdColor.getRedFloat(),
                        holdColor.getGreenFloat(),
                        holdColor.getBlueFloat(),
                        animatable.getAlphaValue())
                .argbInt();

        super.actuallyRender(poseStack, animatable, model, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, newColor);
    }
}
