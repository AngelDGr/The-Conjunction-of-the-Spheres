package TCOTS.entity.geo.renderer;

import TCOTS.entity.geo.model.AnchorProjectileModel;
import TCOTS.entity.misc.AnchorProjectileEntity;
import TCOTS.utils.ChainDrawerUtil;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class AnchorProjectileRenderer extends GeoEntityRenderer<AnchorProjectileEntity> {
    public AnchorProjectileRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new AnchorProjectileModel());
    }

    @Override
    public void actuallyRender(MatrixStack matrixStack, AnchorProjectileEntity anchor, BakedGeoModel model, @Nullable RenderLayer renderType, VertexConsumerProvider vertexConsumerProvider, @Nullable VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour) {
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(MathHelper.lerp(partialTick, anchor.prevYaw, anchor.getYaw()) - 90.0f));
        matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(MathHelper.lerp(partialTick, anchor.prevPitch, anchor.getPitch()) + 90.0f));

        VertexConsumer vertexConsumer = ItemRenderer.getDirectItemGlintConsumer(vertexConsumerProvider, renderType, false, anchor.isEnchanted());

        super.actuallyRender(matrixStack, animatable, model, renderType, vertexConsumerProvider, vertexConsumer, isReRender, partialTick, packedLight, packedOverlay, colour);
    }

    @Override
    public void renderFinal(MatrixStack matrixStack, AnchorProjectileEntity anchor, BakedGeoModel model, VertexConsumerProvider vertexConsumerProvider, @Nullable VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay, int colour) {
        super.renderFinal(matrixStack, animatable, model, vertexConsumerProvider, buffer, partialTick, packedLight, packedOverlay, colour);

        if(anchor.getOwner()!=null) ChainDrawerUtil.renderChain(anchor, partialTick, matrixStack, vertexConsumerProvider, anchor.getOwner());
    }

    @Override
    public boolean shouldRender(AnchorProjectileEntity entity, Frustum frustum, double x, double y, double z) {
        return true;
    }
}
