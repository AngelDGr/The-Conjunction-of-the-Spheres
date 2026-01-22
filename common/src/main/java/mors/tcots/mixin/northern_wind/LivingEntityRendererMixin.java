package mors.tcots.mixin.northern_wind;

import mors.tcots.TCOTS_Client;
import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(value= EnvType.CLIENT)
@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin<T extends LivingEntity, M extends EntityModel<T>> {
    @Unique
    private BlockRenderDispatcher blockRenderManager;
    @Inject(method = "<init>", at = @At("TAIL"))
    private void tcots$injectInConstructor(final EntityRendererProvider.Context ctx, final M model, final float shadowRadius, final CallbackInfo ci) {
        this.blockRenderManager = ctx.getBlockRenderDispatcher();
    }

    @Inject(method = "render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
            at = @At(value = "TAIL"))
    private void tcots$renderIceOnEntity(final T livingEntity, final float f, final float g, final PoseStack matrixStack, final MultiBufferSource vertexConsumerProvider, final int i, final CallbackInfo ci) {
        if (livingEntity.tcots$isFrozen()) {
            TCOTS_Client.renderNorthernWindIce(livingEntity, matrixStack, vertexConsumerProvider, blockRenderManager);
        }
    }
}
