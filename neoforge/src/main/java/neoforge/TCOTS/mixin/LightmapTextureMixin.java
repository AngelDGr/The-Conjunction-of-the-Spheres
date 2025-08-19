package neoforge.TCOTS.mixin;

import TCOTS.utils.MiscUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(LightTexture.class)
public class LightmapTextureMixin {

    @Shadow @Final private Minecraft minecraft;

    @ModifyVariable(method = "updateLightTexture", at = @At("STORE"), ordinal = 7)
    private float injectCatEffectLight(final float l){

        if(MiscUtil.canHaveCatEffect(this.minecraft.player)){
            return 1f;
        }

        return l;
    }
}
