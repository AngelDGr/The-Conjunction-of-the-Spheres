package TCOTS.mixin;

import TCOTS.items.concoctions.TCOTS_Effects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.world.level.LightLayer;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(LightTexture.class)
public class LightmapTextureManagerMixin {

    @Shadow @Final private Minecraft minecraft;

    @ModifyVariable(method = "updateLightTexture", at = @At("STORE"), ordinal = 6)
    private float injectCatEffectLight(float l){
        assert this.minecraft.player != null;
        if(this.canHaveCatEffect()){
           return 1f;
        }

        return l;
    }

    @Unique
    private boolean isNightTicks(){
        assert this.minecraft.player != null;
        long time = this.minecraft.player.level().getDayTime() % 24000;
        return time >= 13000 && time < 23000;
    }

    @Unique
    private boolean canHaveCatEffect(){
        assert this.minecraft.player != null;
        int lightBlock = this.minecraft.player.level().getBrightness(LightLayer.BLOCK, this.minecraft.player.blockPosition());
        int lightSky   = this.minecraft.player.level().getBrightness(LightLayer.SKY,   this.minecraft.player.blockPosition());

        return this.minecraft.player.hasEffect(TCOTS_Effects.CAT_EFFECT) && !(this.minecraft.player.isSpectator()) && ((lightBlock <=4 && lightSky <= 10) || (this.isNightTicks() && lightBlock <=4));
    }
}
