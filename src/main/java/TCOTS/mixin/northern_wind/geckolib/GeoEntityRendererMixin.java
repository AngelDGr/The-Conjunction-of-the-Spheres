package TCOTS.mixin.northern_wind.geckolib;

import TCOTS.items.potions.bombs.NorthernWindBomb;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.GeoRenderer;

@Mixin(GeoEntityRenderer.class)
public abstract class GeoEntityRendererMixin<T extends Entity & GeoAnimatable> extends EntityRenderer<T> implements GeoRenderer<T>  {
    @Unique
    private BlockRenderManager blockRenderManager;
    protected GeoEntityRendererMixin(EntityRendererFactory.Context ctx) {
        super(ctx);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void injectInConstructor(EntityRendererFactory.Context ctx, GeoModel<T> model, CallbackInfo ci){
        this.blockRenderManager = ctx.getBlockRenderManager();
    }

    @Inject(method = "actuallyRender(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/entity/Entity;Lsoftware/bernie/geckolib/cache/object/BakedGeoModel;Lnet/minecraft/client/render/RenderLayer;Lnet/minecraft/client/render/VertexConsumerProvider;Lnet/minecraft/client/render/VertexConsumer;ZFIIFFFF)V",
            at = @At(value = "TAIL"))
    private void renderIceOnEntity(MatrixStack matrixStack, T animatable, BakedGeoModel model, RenderLayer renderType, VertexConsumerProvider vertexConsumerProvider, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha, CallbackInfo ci) {
        if (animatable instanceof LivingEntity livingEntity && livingEntity.theConjunctionOfTheSpheres$isFrozen()) {
            NorthernWindBomb.renderIce(livingEntity, matrixStack, vertexConsumerProvider, blockRenderManager);
        }
    }
}
