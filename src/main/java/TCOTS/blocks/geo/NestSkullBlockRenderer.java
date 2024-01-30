package TCOTS.blocks.geo;

import TCOTS.blocks.skull.NestSkullBlockEntity;
import TCOTS.blocks.skull.NestWallSkullBlock;
import net.minecraft.block.AbstractSkullBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.SkullBlock;
import net.minecraft.block.WallSkullBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.SkullBlockEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.block.entity.SkullBlockEntityModel;
import net.minecraft.client.render.block.entity.SkullBlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationPropertyHelper;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class NestSkullBlockRenderer extends GeoBlockRenderer<NestSkullBlockEntity> {

    public NestSkullBlockRenderer(BlockEntityRendererFactory.Context ctx) {
        super(new NestSkullBlockModel());
    }

    @Override
    public void preRender(MatrixStack poseStack, NestSkullBlockEntity animatable, BakedGeoModel model, VertexConsumerProvider bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        BlockState blockState = animatable.getCachedState();
        Direction direction = this.getFacing(animatable);

        super.preRender(poseStack, animatable, model, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }

    //    @Override
//    public void actuallyRender(MatrixStack poseStack, NestSkullBlockEntity animatable, BakedGeoModel model, RenderLayer renderType, VertexConsumerProvider bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
//
//        float g = animatable.getPoweredTicks(alpha);
//        BlockState blockState = animatable.getCachedState();
//        boolean bl = blockState.getBlock() instanceof WallSkullBlock;
//        Direction direction = bl ? blockState.get(WallSkullBlock.FACING) : null;
//        int k = bl ? RotationPropertyHelper.fromDirection(direction.getOpposite()) : blockState.get(SkullBlock.ROTATION);
//        float h = RotationPropertyHelper.toDegrees(k);
//        SkullBlock.SkullType skullType = ((AbstractSkullBlock)blockState.getBlock()).getSkullType();
////        SkullBlockEntityModel skullBlockEntityModel = this.MODELS.get(skullType);
////        RenderLayer renderLayer = SkullBlockEntityRenderer.getRenderLayer(skullType, skullBlockEntity.getOwner());
////        RenderLayer renderLayer = RenderLayer.getEntityCutoutNoCullZOffset(identifier);
//        renderSkull(direction, h, g, poseStack, buffer, packedLight, getGeoModel());
//
//    }


//    public static void renderSkull(@Nullable Direction direction, float yaw, float animationProgress, MatrixStack matrices, VertexConsumer vertexConsumers, int light, GeoModel<NestSkullBlockEntity> model) {
//        matrices.push();
//        if (direction == null) {
//            matrices.translate(0.5f, 0.0f, 0.5f);
//        } else {
//            float f = 0.25f;
//            matrices.translate(0.5f - (float)direction.getOffsetX() * 0.25f, 0.25f, 0.5f - (float)direction.getOffsetZ() * 0.25f);
//        }
//        matrices.scale(-1.0f, -1.0f, 1.0f);
////        VertexConsumer vertexConsumer = vertexConsumers.getBuffer(renderLayer);
////        model.setHeadRotation(animationProgress, yaw, 0.0f);
//        model.render(matrices, vertexConsumers, light, OverlayTexture.DEFAULT_UV, 1.0f, 1.0f, 1.0f, 1.0f);
//        matrices.pop();
//    }

    //    @Override
//    public void render(NestSkullBlockEntity skullBlockEntity, float f, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, int j) {
//        float g = skullBlockEntity.getPoweredTicks(f);
//        BlockState blockState = skullBlockEntity.getCachedState();
//        boolean bl = blockState.getBlock() instanceof WallSkullBlock;
//        Direction direction = bl ? blockState.get(WallSkullBlock.FACING) : null;
//        int k = bl ? RotationPropertyHelper.fromDirection(direction.getOpposite()) : blockState.get(SkullBlock.ROTATION);
//        float h = RotationPropertyHelper.toDegrees(k);
//        SkullBlock.SkullType skullType = ((AbstractSkullBlock)blockState.getBlock()).getSkullType();
//        SkullBlockEntityModel skullBlockEntityModel = this.MODELS.get(skullType);
//        RenderLayer renderLayer = SkullBlockEntityRenderer.getRenderLayer(skullType, skullBlockEntity.getOwner());
//        SkullBlockEntityRenderer.renderSkull(direction, h, g, matrixStack, vertexConsumerProvider, i, skullBlockEntityModel, renderLayer);
//    }
}
