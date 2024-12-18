package TCOTS.items.geo.renderer;

import TCOTS.TCOTS_Main;
import TCOTS.items.weapons.GiantAnchorItem;
import TCOTS.items.geo.model.GiantAnchorItemModel;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.DiffuseLighting;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class GiantAnchorItemRenderer extends GeoItemRenderer<GiantAnchorItem> {
    public GiantAnchorItemRenderer() {
        super(new GiantAnchorItemModel());
    }

    @Override
    public void render(ItemStack stack, ModelTransformationMode transformType, MatrixStack poseStack, VertexConsumerProvider bufferSource, int packedLight, int packedOverlay) {
        this.animatable = (GiantAnchorItem) stack.getItem();
        this.currentItemStack = stack;
        this.renderPerspective = transformType;

        if (transformType == ModelTransformationMode.GUI) {
            if(GiantAnchorItem.wasLaunched(stack)) {
                renderInGuiChain(transformType, poseStack, bufferSource, packedLight, packedOverlay, MinecraftClient.getInstance().getRenderTickCounter().getTickDelta(true));
            } else {
                renderInGui(transformType, poseStack, bufferSource, packedLight, packedOverlay, MinecraftClient.getInstance().getRenderTickCounter().getTickDelta(true));
            }
        }
        else {

            RenderLayer renderType = getRenderType(this.animatable, getTextureLocation(this.animatable), bufferSource, MinecraftClient.getInstance().getRenderTickCounter().getTickDelta(true));
            VertexConsumer buffer = ItemRenderer.getDirectItemGlintConsumer(bufferSource, renderType, false, this.currentItemStack != null && this.currentItemStack.hasGlint());

            defaultRender(poseStack, this.animatable, bufferSource, renderType, buffer,
                    0, MinecraftClient.getInstance().getRenderTickCounter().getTickDelta(true), packedLight);
        }

    }
    /**
     Used to change the texture based only in the stack and NBT
     */
    @SuppressWarnings("unused")
    protected void renderInGuiChain(ModelTransformationMode transformType, MatrixStack poseStack,
                               VertexConsumerProvider bufferSource, int packedLight, int packedOverlay, float partialTick) {
        setupLightingForGuiRender();

        VertexConsumerProvider.Immediate defaultBufferSource =
                bufferSource instanceof VertexConsumerProvider.Immediate bufferSource2 ? bufferSource2 :
                        MinecraftClient.getInstance().worldRenderer.bufferBuilders.getEntityVertexConsumers();

        RenderLayer renderType = getRenderType(this.animatable, Identifier.of(TCOTS_Main.MOD_ID,"textures/entity/anchor_chain.png"), defaultBufferSource, partialTick);
        VertexConsumer buffer = ItemRenderer.getDirectItemGlintConsumer(bufferSource, renderType, true, this.currentItemStack != null && this.currentItemStack.hasGlint());

        poseStack.push();
        defaultRender(poseStack, this.animatable, defaultBufferSource, renderType, buffer, 0, partialTick, packedLight);
        defaultBufferSource.draw();
        RenderSystem.enableDepthTest();
        DiffuseLighting.enableGuiDepthLighting();
        poseStack.pop();
    }
}
