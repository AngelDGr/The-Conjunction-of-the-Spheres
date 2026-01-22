package mors.tcots.client.geo.renderer.entity;

import mors.tcots.client.geo.model.entity.AnchorProjectileModel;
import mors.tcots.entity.misc.AnchorProjectileEntity;
import mors.tcots.utils.ChainDrawerUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class AnchorProjectileRenderer extends GeoEntityRenderer<AnchorProjectileEntity> {
    public AnchorProjectileRenderer(final EntityRendererProvider.Context renderManager) {
        super(renderManager, new AnchorProjectileModel());
    }

    @Override
    public void actuallyRender(final PoseStack matrixStack, final AnchorProjectileEntity anchor, final BakedGeoModel model, @Nullable final RenderType renderType, final MultiBufferSource vertexConsumerProvider, @Nullable final VertexConsumer buffer, final boolean isReRender, final float partialTick, final int packedLight, final int packedOverlay, final int colour) {
        matrixStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTick, anchor.yRotO, anchor.getYRot()) - 90.0f));
        matrixStack.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(partialTick, anchor.xRotO, anchor.getXRot()) + 90.0f));

        final VertexConsumer vertexConsumer = ItemRenderer.getFoilBufferDirect(vertexConsumerProvider, renderType, false, anchor.isEnchanted());

        super.actuallyRender(matrixStack, animatable, model, renderType, vertexConsumerProvider, vertexConsumer, isReRender, partialTick, packedLight, packedOverlay, colour);
    }

    @Override
    public void renderFinal(final PoseStack matrixStack, final AnchorProjectileEntity anchor, final BakedGeoModel model, final MultiBufferSource vertexConsumerProvider, @Nullable final VertexConsumer buffer, final float partialTick, final int packedLight, final int packedOverlay, final int colour) {
        super.renderFinal(matrixStack, animatable, model, vertexConsumerProvider, buffer, partialTick, packedLight, packedOverlay, colour);

        if(anchor.getOwner()!=null) ChainDrawerUtil.renderChain(anchor, partialTick, matrixStack, vertexConsumerProvider, anchor.getOwner());
    }

    @Override
    public boolean shouldRender(final AnchorProjectileEntity entity, final Frustum frustum, final double x, final double y, final double z) {
        return true;
    }
}
