package TCOTS.entity.misc.renderers;

import TCOTS.entity.misc.bolts.WitcherBolt;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Colors;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;

public abstract class BoltEntityRenderer<T extends WitcherBolt> extends EntityRenderer<T> {
    public BoltEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public void render(T persistentProjectileEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        matrixStack.push();
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(MathHelper.lerp(g, persistentProjectileEntity.prevYaw, persistentProjectileEntity.getYaw()) - 90.0f));
        matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(MathHelper.lerp(g, persistentProjectileEntity.prevPitch, persistentProjectileEntity.getPitch())));
        float s = (float) persistentProjectileEntity.shake - g;
        if (s > 0.0f) {
            float t = -MathHelper.sin(s * 3.0f) * s;
            matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(t));
        }
        matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(45.0f));
        matrixStack.scale(0.05625f, 0.05625f, 0.05625f);
        matrixStack.translate(-2.0f, 0.0f, 0.0f);
        VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getEntityCutout(this.getTexture(persistentProjectileEntity)));
        MatrixStack.Entry entry = matrixStack.peek();

        //Cross-Section
        for (int u = 0; u < 4; ++u) {
            matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90.0f));
            this.vertex(entry, vertexConsumer, -5, -2, 0, 0.0f, 0.0f, 0, 1, 0, i);
            this.vertex(entry, vertexConsumer, 5, -2, 0, 0.625f, 0.0f, 0, 1, 0, i);
            this.vertex(entry, vertexConsumer, 5, 2, 0, 0.625f, 0.3125f, 0, 1, 0, i);
            this.vertex(entry, vertexConsumer, -5, 2, 0, 0.0f, 0.3125f, 0, 1, 0, i);
        }
        matrixStack.pop();
        super.render(persistentProjectileEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    public void vertex(
            MatrixStack.Entry matrix, VertexConsumer vertexConsumer, int x, int y, int z, float u, float v, int normalX, int normalZ, int normalY, int light
    ) {
        vertexConsumer.vertex(matrix, (float)x, (float)y, (float)z)
                .color(Colors.WHITE)
                .texture(u, v)
                .overlay(OverlayTexture.DEFAULT_UV)
                .light(light)
                .normal(matrix, (float)normalX, (float)normalY, (float)normalZ);
    }
}