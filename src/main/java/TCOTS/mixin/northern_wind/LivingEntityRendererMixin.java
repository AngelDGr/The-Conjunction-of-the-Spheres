package TCOTS.mixin.northern_wind;

import TCOTS.items.potions.bombs.NorthernWindBomb;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntityRenderer.class)
public class LivingEntityRendererMixin<T extends LivingEntity, M extends EntityModel<T>> {
    @Unique
    private BlockRenderManager blockRenderManager;
    @Inject(method = "<init>", at = @At("TAIL"))
    private void injectInConstructor(EntityRendererFactory.Context ctx, M model, float shadowRadius, CallbackInfo ci) {
        this.blockRenderManager = ctx.getBlockRenderManager();
    }

    @Inject(method = "render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
            at = @At(value = "TAIL"))
    private void renderIceOnEntity(T livingEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo ci) {
        if (livingEntity.theConjunctionOfTheSpheres$isFrozen()) {
            NorthernWindBomb.renderIce(livingEntity, matrixStack, vertexConsumerProvider, blockRenderManager);
        }
    }
}
