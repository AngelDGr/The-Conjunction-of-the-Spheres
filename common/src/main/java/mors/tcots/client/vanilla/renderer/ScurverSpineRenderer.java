package mors.tcots.client.vanilla.renderer;

import mors.tcots.TCOTS_Main;
import mors.tcots.entity.misc.ScurverSpineEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.CommonColors;
import net.minecraft.util.Mth;

public class ScurverSpineRenderer<T extends ScurverSpineEntity> extends EntityRenderer<T> {
    public static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID,"textures/entity/scurver_spike.png");

    public ScurverSpineRenderer(final EntityRendererProvider.Context ctx) {
        super(ctx);
    }

    @Override
    public void render(final T persistentProjectileEntity, final float f, final float g, final PoseStack matrixStack, final MultiBufferSource vertexConsumerProvider, final int i) {
        matrixStack.pushPose();
        matrixStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(g, persistentProjectileEntity.yRotO, persistentProjectileEntity.getYRot()) - 90.0f));
        matrixStack.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(g, persistentProjectileEntity.xRotO, persistentProjectileEntity.getXRot())));
        final float s = (float) persistentProjectileEntity.shakeTime - g;
        if (s > 0.0f) {
            final float t = -Mth.sin(s * 3.0f) * s;
            matrixStack.mulPose(Axis.ZP.rotationDegrees(t));
        }
        matrixStack.mulPose(Axis.XP.rotationDegrees(45.0f));
        matrixStack.scale(0.05625f, 0.05625f, 0.05625f);
        matrixStack.translate(-2.0f, 0.0f, 0.0f);
        final VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(RenderType.entityCutout(this.getTextureLocation(persistentProjectileEntity)));
        final PoseStack.Pose entry = matrixStack.last();

        //Cross-Section
        for (int u = 0; u < 4; ++u) {
            matrixStack.mulPose(Axis.XP.rotationDegrees(90.0f));
            this.vertex(entry, vertexConsumer, -5, -2, 0, 0.0f, 0.0f, 0, 1, 0, i);
            this.vertex(entry, vertexConsumer, 5, -2, 0, 0.625f, 0.0f, 0, 1, 0, i);
            this.vertex(entry, vertexConsumer, 5, 2, 0, 0.625f, 0.3125f, 0, 1, 0, i);
            this.vertex(entry, vertexConsumer, -5, 2, 0, 0.0f, 0.3125f, 0, 1, 0, i);
        }
        matrixStack.popPose();
        super.render(persistentProjectileEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    public void vertex(
            final PoseStack.Pose matrix, final VertexConsumer vertexConsumer, final int x, final int y, final int z, final float u, final float v, final int normalX, final int normalZ, final int normalY, final int light
    ) {
        vertexConsumer.addVertex(matrix, (float)x, (float)y, (float)z)
                .setColor(CommonColors.WHITE)
                .setUv(u, v)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(light)
                .setNormal(matrix, (float)normalX, (float)normalY, (float)normalZ);
    }

    @Override
    public ResourceLocation getTextureLocation(final ScurverSpineEntity arrowEntity) {
        return TEXTURE;
    }
}
