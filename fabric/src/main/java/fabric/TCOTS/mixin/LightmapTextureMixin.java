package fabric.TCOTS.mixin;

import TCOTS.utils.MiscUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(LightTexture.class)
public class LightmapTextureMixin {

    @Shadow @Final private Minecraft minecraft;

    @ModifyVariable(method = "updateLightTexture", at = @At("STORE"), ordinal = 6)
    private float injectCatEffectLight(float l){

        if(MiscUtil.canHaveCatEffect(this.minecraft.player)){
            return 1f;
        }

        return l;
    }
}
