package TCOTS.entity.geo.renderer;

import TCOTS.entity.geo.model.AnchorProjectileModel;
import TCOTS.entity.misc.AnchorProjectileEntity;
import TCOTS.utils.ChainDrawerUtil;
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
    public AnchorProjectileRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new AnchorProjectileModel());
    }

    @Override
    public void actuallyRender(PoseStack matrixStack, AnchorProjectileEntity anchor, BakedGeoModel model, @Nullable RenderType renderType, MultiBufferSource vertexConsumerProvider, @Nullable VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, int colour) {
        matrixStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTick, anchor.yRotO, anchor.getYRot()) - 90.0f));
        matrixStack.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(partialTick, anchor.xRotO, anchor.getXRot()) + 90.0f));

        VertexConsumer vertexConsumer = ItemRenderer.getFoilBufferDirect(vertexConsumerProvider, renderType, false, anchor.isEnchanted());

        super.actuallyRender(matrixStack, animatable, model, renderType, vertexConsumerProvider, vertexConsumer, isReRender, partialTick, packedLight, packedOverlay, colour);
    }

    @Override
    public void renderFinal(PoseStack matrixStack, AnchorProjectileEntity anchor, BakedGeoModel model, MultiBufferSource vertexConsumerProvider, @Nullable VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay, int colour) {
        super.renderFinal(matrixStack, animatable, model, vertexConsumerProvider, buffer, partialTick, packedLight, packedOverlay, colour);

        if(anchor.getOwner()!=null) ChainDrawerUtil.renderChain(anchor, partialTick, matrixStack, vertexConsumerProvider, anchor.getOwner());
    }

    @Override
    public boolean shouldRender(AnchorProjectileEntity entity, Frustum frustum, double x, double y, double z) {
        return true;
    }
}
