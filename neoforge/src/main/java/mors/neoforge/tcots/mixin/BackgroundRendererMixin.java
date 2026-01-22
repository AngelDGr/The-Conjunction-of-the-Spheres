package mors.neoforge.tcots.mixin;

import mors.tcots.utils.TCOTS_Util;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FogRenderer.class)
public class BackgroundRendererMixin {

    @Unique
    private static Entity entity;

    @Inject(method = "setupColor", at = @At("HEAD"))
    private static void renderGet(final Camera camera, final float tickDelta, final ClientLevel world, final int viewDistance, final float skyDarkness, final CallbackInfo ci){
        entity = camera.getEntity();
    }

    @ModifyVariable(
            method = "setupColor",
            at = @At(value = "STORE"),
            slice = @Slice(
                    from = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;getWaterVision()F"),
                    to = @At(value = "INVOKE", target = "Ljava/lang/Math;min(FF)F")
            ),
            ordinal = 3
    )
    private static float modifyCatFogBrightness(final float o) {
        if (entity instanceof final LivingEntity living && TCOTS_Util.canHaveCatEffect(living)) {
            return 1.1f;
        }
        return o;
    }

}
