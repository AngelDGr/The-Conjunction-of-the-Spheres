package TCOTS.mixin.northern_wind.geckolib;

import TCOTS.TCOTS_Client;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.GeoReplacedEntityRenderer;

@Mixin(GeoReplacedEntityRenderer.class)
public abstract class GeoReplacedEntityRendererMixin<E extends Entity, T extends GeoAnimatable> extends EntityRenderer<E> implements GeoRenderer<T>{
    @Unique
    private BlockRenderDispatcher blockRenderManager;
    protected GeoReplacedEntityRendererMixin(final EntityRendererProvider.Context ctx) {
        super(ctx);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void injectInConstructor(final EntityRendererProvider.Context ctx, final GeoModel<T> model, final GeoAnimatable animatable, final CallbackInfo ci){
        this.blockRenderManager = ctx.getBlockRenderDispatcher();
    }

    @Inject(method = "render", at = @At(value = "TAIL"))
    private void renderIceOnEntity(final E entity, final float entityYaw, final float partialTick, final PoseStack matrixStack, final MultiBufferSource vertexConsumerProvider, final int packedLight, final CallbackInfo ci) {
        if (entity instanceof final LivingEntity livingEntity && livingEntity.theConjunctionOfTheSpheres$isFrozen()) {
            TCOTS_Client.renderNorthernWindIce(livingEntity, matrixStack, vertexConsumerProvider, blockRenderManager);
        }
    }
}
