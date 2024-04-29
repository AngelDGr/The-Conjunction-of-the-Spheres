package TCOTS.mixin;

import TCOTS.items.potions.TCOTS_Effects;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.LightType;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Debug(export = true) // Enables exporting for the targets of this mixin
@Mixin(BackgroundRenderer.class)
public class BackgroundRendererMixin {

    @Unique
    private static Entity entity;

    @Inject(method = "render", at = @At("HEAD"))
    private static void renderGet(Camera camera, float tickDelta, ClientWorld world, int viewDistance, float skyDarkness, CallbackInfo ci){
        entity = camera.getFocusedEntity();
    }

    @ModifyVariable(method = "render", at = @At(value = "STORE"), ordinal = 2)
    private static float injectCatEffectLightBackground(float s){
        if(canHaveCatEffect()){
            return 1.1f;
        }

        return s;
    }

    @Unique
    private static boolean isNightTicks(){
        assert entity != null;
        long time = entity.getWorld().getTimeOfDay() % 24000;
        return time >= 13000 && time < 23000;
    }

    @Unique
    private static boolean canHaveCatEffect(){
        assert entity != null;
        int lightBlock = entity.getWorld().getLightLevel(LightType.BLOCK, entity.getBlockPos());
        int lightSky   = entity.getWorld().getLightLevel(LightType.SKY,   entity.getBlockPos());
        if(entity instanceof LivingEntity) {

            return ((LivingEntity)entity).hasStatusEffect(TCOTS_Effects.CAT_EFFECT) && !(entity.isSpectator()) && ((lightBlock <= 4 && lightSky <= 10) || (isNightTicks() && lightBlock <= 4));
        }

        return false;
    }

}
