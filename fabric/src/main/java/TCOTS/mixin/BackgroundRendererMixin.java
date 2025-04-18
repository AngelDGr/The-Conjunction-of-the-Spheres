package TCOTS.mixin;

import TCOTS.items.concoctions.TCOTS_Effects;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.LightLayer;
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
    private static float injectCatEffectLightBackground(float s){
        if(canHaveCatEffect()){
            return 1.1f;
        }

        return s;
    }

    @Unique
    private static boolean isNightTicks(){
        assert entity != null;
        long time = entity.level().getDayTime() % 24000;
        return time >= 13000 && time < 23000;
    }

    @Unique
    private static boolean canHaveCatEffect(){
        assert entity != null;
        int lightBlock = entity.level().getBrightness(LightLayer.BLOCK, entity.blockPosition());
        int lightSky   = entity.level().getBrightness(LightLayer.SKY,   entity.blockPosition());
        if(entity instanceof LivingEntity) {

            return ((LivingEntity)entity).hasEffect(TCOTS_Effects.CAT_EFFECT) && !(entity.isSpectator()) && ((lightBlock <= 4 && lightSky <= 10) || (isNightTicks() && lightBlock <= 4));
        }

        return false;
    }

}
