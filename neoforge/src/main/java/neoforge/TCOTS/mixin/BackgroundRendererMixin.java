package neoforge.TCOTS.mixin;

import TCOTS.utils.MiscUtil;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Debug;
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
    private static void renderGet(Camera camera, float tickDelta, ClientLevel world, int viewDistance, float skyDarkness, CallbackInfo ci){
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
    private static float modifyCatFogBrightness(float o) {
        if (entity instanceof LivingEntity living && MiscUtil.canHaveCatEffect(living)) {
            return 1.1f;
        }
        return o;
    }

}
