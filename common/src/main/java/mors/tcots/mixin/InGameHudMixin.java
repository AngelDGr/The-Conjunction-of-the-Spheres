package mors.tcots.mixin;

import mors.tcots.TCOTS_Main;
import mors.tcots.registry.TCOTS_Effects;
import mors.tcots.client.screen.TCOTS_HeartTypes;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.systems.RenderSystem;
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
    boolean tcots$changeOverlay =true;
    @Unique
    ResourceLocation MUD_BALL_OVERLAY=MUD_BALL_OVERLAY_1;
    @Unique
    private void tcots$changeOverlay(){
        final int random = this.random.nextIntBetweenInclusive(0,3);
        if(tcots$changeOverlay) {
            switch (random) {
                case 0:
                    MUD_BALL_OVERLAY = MUD_BALL_OVERLAY_1;
                    tcots$changeOverlay =false;
                    break;
                case 1:
                    MUD_BALL_OVERLAY = MUD_BALL_OVERLAY_2;
                    tcots$changeOverlay =false;
                    break;
                case 2:
                    MUD_BALL_OVERLAY = MUD_BALL_OVERLAY_3;
                    tcots$changeOverlay =false;
                    break;
                case 3:
                    MUD_BALL_OVERLAY = MUD_BALL_OVERLAY_4;
                    tcots$changeOverlay =false;
                    break;
                default:
                    break;
            }
        }
    }
    @Inject(method = "renderCameraOverlays", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;getTicksFrozen()I"))
    private void tcots$renderMudBall(final GuiGraphics context, final DeltaTracker tickCounter, final CallbackInfo ci){
        tcots$changeOverlay();

        assert this.minecraft.player != null;
        if(this.minecraft.player.tcots$$getMudInFace() > 0){
            this.renderTextureOverlay(context, MUD_BALL_OVERLAY, this.minecraft.player.tcots$getMudTransparency());
        }
        else {
            if(!tcots$changeOverlay){
                tcots$changeOverlay =true;
            }
        }
    }

    @WrapOperation(
            method = "renderHearts",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Gui;renderHeart(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/client/gui/Gui$HeartType;IIZZZ)V", ordinal = 3)
    )
    private void tcots$effectsHearts(final Gui instance, final GuiGraphics context, final Gui.HeartType heartType, final int x, final int y, final boolean hardcore, final boolean blinking, final boolean half, final Operation<Void> original) {
        final Player player = this.getCameraPlayer();

        if (player!=null&& player.tcots$toxicityOverThreshold() && !player.hasEffect(MobEffects.WITHER)) {
            RenderSystem.enableBlend();
            context.blitSprite(TCOTS_HeartTypes.TOXIC.getTexture(hardcore,half,blinking), x, y, 9, 9);
            RenderSystem.disableBlend();
        } else if (player!=null&& player.hasEffect(TCOTS_Effects.Cadaverine()) && !player.hasEffect(MobEffects.WITHER)){
            RenderSystem.enableBlend();
            context.blitSprite(TCOTS_HeartTypes.CADAVERINE.getTexture(hardcore,half,blinking), x, y, 9, 9);
            RenderSystem.disableBlend();
        } else {
            original.call(instance, context, heartType, x, y, hardcore, blinking, half);
        }
    }
}
