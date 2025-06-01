package TCOTS.mixin;

import TCOTS.TCOTS_Main;
import TCOTS.registry.TCOTS_Effects;
import TCOTS.screen.TCOTS_HeartTypes;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(value= EnvType.CLIENT)
@Mixin(Gui.class)
public abstract class InGameHudMixin {

    @Shadow @Final private RandomSource random;
    @Shadow protected abstract void renderTextureOverlay(GuiGraphics context, ResourceLocation texture, float opacity);
    @Shadow @Final private Minecraft minecraft;
    @Shadow
    private Player getCameraPlayer() {
        return null;
    }
    @Shadow
    protected abstract void renderHeart(GuiGraphics context, Gui.HeartType type, int x, int y, boolean hardcore, boolean blinking, boolean half);


    //MudballOverlay
    @Unique
    private static final ResourceLocation MUD_BALL_OVERLAY_1 = ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID,"textures/gui/mudball_overlay1.png");
    @Unique
    private static final ResourceLocation MUD_BALL_OVERLAY_2 = ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID,"textures/gui/mudball_overlay2.png");
    @Unique
    private static final ResourceLocation MUD_BALL_OVERLAY_3 = ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID,"textures/gui/mudball_overlay3.png");
    @Unique
    private static final ResourceLocation MUD_BALL_OVERLAY_4 = ResourceLocation.fromNamespaceAndPath(TCOTS_Main.MOD_ID,"textures/gui/mudball_overlay4.png");
    @Unique
    boolean changeOverlay=true;
    @Unique
    ResourceLocation MUD_BALL_OVERLAY=MUD_BALL_OVERLAY_1;
    @Unique
    private void changeOverlay(){
        int random = this.random.nextIntBetweenInclusive(0,3);
        if(changeOverlay) {
            switch (random) {
                case 0:
                    MUD_BALL_OVERLAY = MUD_BALL_OVERLAY_1;
                    changeOverlay=false;
                    break;
                case 1:
                    MUD_BALL_OVERLAY = MUD_BALL_OVERLAY_2;
                    changeOverlay=false;
                    break;
                case 2:
                    MUD_BALL_OVERLAY = MUD_BALL_OVERLAY_3;
                    changeOverlay=false;
                    break;
                case 3:
                    MUD_BALL_OVERLAY = MUD_BALL_OVERLAY_4;
                    changeOverlay=false;
                    break;
                default:
                    break;
            }
        }
    }
    @Inject(method = "renderCameraOverlays", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;getTicksFrozen()I"))
    private void renderMudBall(GuiGraphics context, DeltaTracker tickCounter, CallbackInfo ci){
        changeOverlay();

        assert this.minecraft.player != null;
        if(this.minecraft.player.theConjunctionOfTheSpheres$getMudInFace() > 0){
            this.renderTextureOverlay(context, MUD_BALL_OVERLAY, this.minecraft.player.theConjunctionOfTheSpheres$getMudTransparency());
        }
        else {
            if(!changeOverlay){
                changeOverlay=true;
            }
        }
    }


    @Redirect(method = "renderHearts", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/gui/Gui;renderHeart(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/client/gui/Gui$HeartType;IIZZZ)V",
            ordinal = 3))
    private void injectEffectsHearts(Gui instance, GuiGraphics context, Gui.HeartType type, int x, int y, boolean hardcore, boolean blinking, boolean half){
        Player player = this.getCameraPlayer();
        if(player!=null) {

            if (player.theConjunctionOfTheSpheres$toxicityOverThreshold() && !player.hasEffect(MobEffects.WITHER)) {
                context.blitSprite(TCOTS_HeartTypes.TOXIC.getTexture(hardcore,half,blinking), x, y, 9, 9);
            } else if (player.hasEffect(TCOTS_Effects.Cadaverine()) && !player.hasEffect(MobEffects.WITHER)){
                context.blitSprite(TCOTS_HeartTypes.CADAVERINE.getTexture(hardcore,half,blinking), x, y, 9, 9);
            }
            else {
                renderHeart(context, type, x, y, hardcore, blinking, half);
            }
        }
    }
}
