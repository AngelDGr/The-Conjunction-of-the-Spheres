package mors.tcots.mixin;

import mors.tcots.registry.TCOTS_Effects;
import mors.tcots.effects.potions.CatEffect;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.LightLayer;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(value= EnvType.CLIENT)
@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {
    @Shadow
    @Nullable PostChain postEffect;

    @Shadow
    protected abstract void loadEffect(ResourceLocation id);

    @Shadow @Final Minecraft minecraft;

    @Shadow public abstract void shutdownEffect();

    @Inject(method =
            "tick",
            at = @At("TAIL"))
    private void tcots$catShader(final CallbackInfo ci){
        this.tcots$putCatShader();
    }

    @Unique
    private boolean tcots$catActive =false;

    @Unique
    private void tcots$putCatShader() {

        if(this.tcots$canHaveCatEffect()){
            if (this.postEffect != null) {
                this.postEffect.close();
            }
            this.postEffect = null;

            this.loadEffect(CatEffect.CatShader);
            this.tcots$catActive =true;

        } else if(tcots$catActive){

            this.shutdownEffect();
            tcots$catActive =false;
        }
    }

    @Unique
    private boolean tcots$isNightTicks(){
        assert this.minecraft.player != null;
        final long time = this.minecraft.player.level().getDayTime() % 24000;
        return time >= 13000 && time < 23000;
    }

    @Unique
    private boolean tcots$canHaveCatEffect(){
        assert this.minecraft.player != null;
        final int lightBlock = this.minecraft.player.level().getBrightness(LightLayer.BLOCK, this.minecraft.player.blockPosition());
        final int lightSky   = this.minecraft.player.level().getBrightness(LightLayer.SKY,   this.minecraft.player.blockPosition());
        return this.minecraft.player.hasEffect(TCOTS_Effects.CatEffect()) && !(this.minecraft.player.isSpectator()) && ((lightBlock <=4 && lightSky <= 10) || (this.tcots$isNightTicks() && lightBlock <=4));
    }

}
