package TCOTS.mixin;

import TCOTS.registry.TCOTS_Effects;
import TCOTS.effects.potions.CatEffect;
import TCOTS.utils.MiscUtil;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.PostChain;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.LightLayer;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

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
    private void injectCatShader(CallbackInfo ci){
        this.PutCatShader();
    }

    @Unique
    private boolean catActive=false;

    @Unique
    private void PutCatShader() {

        if(this.canHaveCatEffect()){
            if (this.postEffect != null) {
                this.postEffect.close();
            }
            this.postEffect = null;

            this.loadEffect(CatEffect.CatShader);
            this.catActive=true;

        } else if(catActive){

            this.shutdownEffect();
            catActive=false;
        }
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
        return this.minecraft.player.hasEffect(TCOTS_Effects.CatEffect()) && !(this.minecraft.player.isSpectator()) && ((lightBlock <=4 && lightSky <= 10) || (this.isNightTicks() && lightBlock <=4));
    }

}
