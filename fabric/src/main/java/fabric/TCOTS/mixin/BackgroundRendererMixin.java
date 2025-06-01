package fabric.TCOTS.mixin;

import TCOTS.utils.MiscUtil;
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
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FogRenderer.class)
public class BackgroundRendererMixin {

    @Unique
    private static Entity entity;

    @Inject(method = "setupColor", at = @At("HEAD"))
    private static void renderGet(Camera camera, float tickDelta, ClientLevel world, int viewDistance, float skyDarkness, CallbackInfo ci){
        entity = camera.getEntity();
    }

    @ModifyVariable(method = "setupColor", at = @At(value = "STORE"), ordinal = 2)
    private static float modifyCatFogBrightness(float s){
        if(entity instanceof LivingEntity entityP && MiscUtil.canHaveCatEffect(entityP)){
            return 1.1f;
        }

        return s;
    }

}
