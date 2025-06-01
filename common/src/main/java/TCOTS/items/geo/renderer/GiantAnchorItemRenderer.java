package TCOTS.items.geo.renderer;

import TCOTS.TCOTS_Main;
import TCOTS.items.weapons.GiantAnchorItem;
import TCOTS.items.geo.model.GiantAnchorItemModel;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class GiantAnchorItemRenderer extends GeoItemRenderer<GiantAnchorItem> {
    public GiantAnchorItemRenderer() {
        super(new GiantAnchorItemModel());
    }

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext transformType, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        this.animatable = (GiantAnchorItem) stack.getItem();
        this.currentItemStack = stack;
        this.renderPerspective = transformType;

        if (transformType == ItemDisplayContext.GUI) {
            if(GiantAnchorItem.wasLaunched(stack)) {
                renderInGuiChain(transformType, poseStack, bufferSource, packedLight, packedOverlay, Minecraft.getInstance().getTimer().getGameTimeDeltaPartialTick(true));
            } else {
                renderInGui(transformType, poseStack, bufferSource, packedLight, packedOverlay, Minecraft.getInstance().getTimer().getGameTimeDeltaPartialTick(true));
            }
        }
        else {

            RenderType renderType = getRenderType(this.animatable, getTextureLocation(this.animatable), bufferSource, Minecraft.getInstance().getTimer().getGameTimeDeltaPartialTick(true));
            VertexConsumer buffer = ItemRenderer.getFoilBufferDirect(bufferSource, renderType, false, this.currentItemStack != null && this.currentItemStack.hasFoil());

            defaultRender(poseStack, this.animatable, bufferSource, renderType, buffer,
                    0, Minecraft.getInstance().getTimer().getGameTimeDeltaPartialTick(true), packedLight);
        }

    }
    /**
     Used to change the texture based only in the stack and NBT
     */
    @SuppressWarnings("unused")
    protected void renderInGuiChain(ItemDisplayContext transformType, PoseStack poseStack,
                               MultiBufferSource bufferSource, int packedLight, int packedOverlay, float partialTick) {
        setupLightingForGuiRender();

        MultiBufferSource.BufferSource defaultBufferSource =
                bufferSource instanceof MultiBufferSource.BufferSource bufferSource2 ? bufferSource2 :
                        Minecraft.getInstance().levelRenderer.renderBuffers.bufferSource();

        RenderType renderType = getRenderType(this.animatable, ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID,"textures/entity/anchor_chain.png"), defaultBufferSource, partialTick);
        VertexConsumer buffer = ItemRenderer.getFoilBufferDirect(bufferSource, renderType, true, this.currentItemStack != null && this.currentItemStack.hasFoil());

        poseStack.pushPose();
        defaultRender(poseStack, this.animatable, defaultBufferSource, renderType, buffer, 0, partialTick, packedLight);
        defaultBufferSource.endBatch();
        RenderSystem.enableDepthTest();
        Lighting.setupFor3DItems();
        poseStack.popPose();
    }
}
