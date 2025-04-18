package TCOTS.mixin;

import TCOTS.entity.witcher_cosmetics.toxicity_face.ToxicityFaceFeatureRenderer;
import TCOTS.entity.witcher_cosmetics.witcher_eyes.WitcherEyesFeatureRenderer;
import net.minecraft.client.model.PlayerModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerRenderer.class)
public abstract class PlayerEntityRendererMixin extends LivingEntityRenderer<AbstractClientPlayer, PlayerModel<AbstractClientPlayer>> {
    public PlayerEntityRendererMixin(EntityRendererProvider.Context ctx, PlayerModel<AbstractClientPlayer> model, float shadowRadius) {
        super(ctx, model, shadowRadius);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void injectInConstructor(EntityRendererProvider.Context ctx, boolean slim, CallbackInfo ci) {
        this.addLayer(new WitcherEyesFeatureRenderer(this, ctx));

        this.addLayer(new ToxicityFaceFeatureRenderer(this, ctx));
    }
}
