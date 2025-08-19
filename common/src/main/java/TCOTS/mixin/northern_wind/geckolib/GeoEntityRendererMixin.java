package TCOTS.mixin.northern_wind.geckolib;

import TCOTS.TCOTS_Client;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.GeoRenderer;

@Mixin(GeoEntityRenderer.class)
public abstract class GeoEntityRendererMixin<T extends Entity & GeoAnimatable> extends EntityRenderer<T> implements GeoRenderer<T>  {
    @Unique
    private BlockRenderDispatcher blockRenderManager;
    protected GeoEntityRendererMixin(final EntityRendererProvider.Context ctx) {
        super(ctx);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void injectInConstructor(final EntityRendererProvider.Context ctx, final GeoModel<T> model, final CallbackInfo ci){
        this.blockRenderManager = ctx.getBlockRenderDispatcher();
    }

    @Inject(method = "actuallyRender(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/world/entity/Entity;Lsoftware/bernie/geckolib/cache/object/BakedGeoModel;Lnet/minecraft/client/renderer/RenderType;Lnet/minecraft/client/renderer/MultiBufferSource;Lcom/mojang/blaze3d/vertex/VertexConsumer;ZFIII)V",
            at = @At(value = "TAIL"))
    private void renderIceOnEntity(final PoseStack matrixStack, final T animatable, final BakedGeoModel model, @Nullable final RenderType renderType, final MultiBufferSource vertexConsumerProvider, @Nullable final VertexConsumer buffer, final boolean isReRender, final float partialTick, final int packedLight, final int packedOverlay, final int colour, final CallbackInfo ci) {
        if (animatable instanceof final LivingEntity livingEntity && livingEntity.theConjunctionOfTheSpheres$isFrozen()) {
            TCOTS_Client.renderNorthernWindIce(livingEntity, matrixStack, vertexConsumerProvider, blockRenderManager);
        }
    }
}
