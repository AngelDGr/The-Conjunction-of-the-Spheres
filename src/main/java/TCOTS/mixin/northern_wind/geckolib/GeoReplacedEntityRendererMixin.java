package TCOTS.mixin.northern_wind.geckolib;

import TCOTS.items.potions.bombs.NorthernWindBomb;
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
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.GeoReplacedEntityRenderer;

@Mixin(GeoReplacedEntityRenderer.class)
public abstract class GeoReplacedEntityRendererMixin<E extends Entity, T extends GeoAnimatable> extends EntityRenderer<E> implements GeoRenderer<T>{
    @Unique
    private BlockRenderManager blockRenderManager;
    protected GeoReplacedEntityRendererMixin(EntityRendererFactory.Context ctx) {
        super(ctx);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void injectInConstructor(EntityRendererFactory.Context ctx, GeoModel<T> model, GeoAnimatable animatable, CallbackInfo ci){
        this.blockRenderManager = ctx.getBlockRenderManager();
    }

    @Inject(method = "render", at = @At(value = "TAIL"))
    private void renderIceOnEntity(E entity, float entityYaw, float partialTick, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int packedLight, CallbackInfo ci) {
        if (entity instanceof LivingEntity livingEntity && livingEntity.theConjunctionOfTheSpheres$isFrozen()) {
            NorthernWindBomb.renderIce(livingEntity, matrixStack, vertexConsumerProvider, blockRenderManager);
        }
    }
}
