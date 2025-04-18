package TCOTS;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;

public class TCOTS_Client {
    public static ModelLayerLocation WITCHER_EYES_LAYER = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "witcher_eyes"), "witcher_eyes");
    public static ModelLayerLocation TOXICITY_FACE_LAYER = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID, "toxicity_face"), "toxicity_face");

    public static void init(){

    }

    public static void renderNorthernWindIce(LivingEntity livingEntity, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, BlockRenderDispatcher blockRenderManager){

        matrixStack.pushPose();
        float blockSize = 1.75f;
        AABB boundingBox = livingEntity.getBoundingBox();
        BlockPos blockPos = BlockPos.containing(livingEntity.getX(), boundingBox.minY, livingEntity.getZ());
        matrixStack.scale(
                blockSize * (float)boundingBox.getXsize(),
                blockSize * (float)boundingBox.getYsize(),
                blockSize * (float)boundingBox.getZsize()
        );
        matrixStack.translate(-0.5, -0.3, -0.5);

        blockRenderManager
                .getModelRenderer()
                .tesselateBlock(
                        livingEntity.level(),
                        blockRenderManager.getBlockModel(Blocks.ICE.defaultBlockState()),
                        Blocks.ICE.defaultBlockState(),
                        blockPos,
                        matrixStack,
                        vertexConsumerProvider.getBuffer(ItemBlockRenderTypes.getMovingBlockRenderType(Blocks.ICE.defaultBlockState())),
                        false,
                        RandomSource.create(),
                        Blocks.ICE.defaultBlockState().getSeed(blockPos),
                        OverlayTexture.NO_OVERLAY
                );

        matrixStack.popPose();
    }
}
