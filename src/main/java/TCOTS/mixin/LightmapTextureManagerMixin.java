package TCOTS.mixin;

import TCOTS.items.concoctions.TCOTS_Effects;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.world.LightType;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(LightmapTextureManager.class)
public class LightmapTextureManagerMixin {

    @Shadow @Final private MinecraftClient client;

    @ModifyVariable(method = "update", at = @At("STORE"), ordinal = 6)
    private float injectCatEffectLight(float l){
        assert this.client.player != null;
        if(this.canHaveCatEffect()){
           return 1f;
        }

        return l;
    }

    @Unique
    private boolean isNightTicks(){
        assert this.client.player != null;
        long time = this.client.player.getWorld().getTimeOfDay() % 24000;
        return time >= 13000 && time < 23000;
    }

    @Unique
    private boolean canHaveCatEffect(){
        assert this.client.player != null;
        int lightBlock = this.client.player.getWorld().getLightLevel(LightType.BLOCK, this.client.player.getBlockPos());
        int lightSky   = this.client.player.getWorld().getLightLevel(LightType.SKY,   this.client.player.getBlockPos());

        return this.client.player.hasStatusEffect(TCOTS_Effects.CAT_EFFECT) && !(this.client.player.isSpectator()) && ((lightBlock <=4 && lightSky <= 10) || (this.isNightTicks() && lightBlock <=4));
    }
}
