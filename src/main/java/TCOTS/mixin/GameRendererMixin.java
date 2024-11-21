package TCOTS.mixin;

import TCOTS.items.concoctions.TCOTS_Effects;
import TCOTS.items.concoctions.effects.potions.CatEffect;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.PostEffectProcessor;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.util.Identifier;
import net.minecraft.world.LightType;
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
    @Nullable PostEffectProcessor postProcessor;

    @Shadow
    protected abstract void loadPostProcessor(Identifier id);

    @Shadow @Final MinecraftClient client;

    @Shadow public abstract void disablePostProcessor();

    @Inject(method =
            "tick",
            at = @At("TAIL"))
    private void injectCatShader(CallbackInfo ci){
        this.PutCatShader();
    }

    @Unique
    private boolean catActive=false;

    @Unique
    private void PutCatShader() {

        if(this.canHaveCatEffect()){
            if (this.postProcessor != null) {
                this.postProcessor.close();
            }
            this.postProcessor = null;

            this.loadPostProcessor(CatEffect.CatShader);
            this.catActive=true;

        } else if(catActive){

            this.disablePostProcessor();
            catActive=false;
        }
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
