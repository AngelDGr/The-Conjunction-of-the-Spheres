package TCOTS.mixin.northern_wind;

import TCOTS.TCOTS_Client;
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
    private void injectInConstructor(EntityRendererProvider.Context ctx, M model, float shadowRadius, CallbackInfo ci) {
        this.blockRenderManager = ctx.getBlockRenderDispatcher();
    }

    @Inject(method = "render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
            at = @At(value = "TAIL"))
    private void renderIceOnEntity(T livingEntity, float f, float g, PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int i, CallbackInfo ci) {
        if (livingEntity.theConjunctionOfTheSpheres$isFrozen()) {
            TCOTS_Client.renderNorthernWindIce(livingEntity, matrixStack, vertexConsumerProvider, blockRenderManager);
        }
    }
}
