package mors.fabric.tcots.mixin;

import mors.tcots.utils.TCOTS_Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(LightTexture.class)
public class LightmapTextureMixin {

    @Shadow @Final private Minecraft minecraft;

    @ModifyVariable(method = "updateLightTexture", at = @At("STORE"), ordinal = 6)
    private float injectCatEffectLight(final float l){

        if(TCOTS_Util.canHaveCatEffect(this.minecraft.player)){
            return 1f;
        }

        return l;
    }
}
